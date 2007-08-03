/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.tuscany.das.ldap.emf;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.directory.shared.ldap.exception.LdapNameNotFoundException;
import org.apache.tuscany.das.ldap.connect.ADSEmbeddedConnection;
import org.apache.tuscany.das.ldap.connect.JNDIConnection;
import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.create.InitialContextCreator;
import org.apache.tuscany.das.ldap.create.MetaContextCreator;
import org.apache.tuscany.das.ldap.emf.create.EDataGraphCreator;
import org.apache.tuscany.das.ldap.emf.read.EDataGraphReader;
import org.apache.tuscany.das.ldap.emf.update.EDataGraphUpdater;
import org.apache.tuscany.das.ldap.schema.emf.create.EcoreTypeSystemHelper;
import org.apache.tuscany.das.ldap.schema.emf.create.ModelTypeSystemCreator;
import org.apache.tuscany.model.Configuration;
import org.apache.tuscany.model.DASMeta;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.sdo.EDataGraph;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class LdapDAS
implements DASConstants
{
    private Map<EDataObject, String> metaDataObjectToRelativeDNCache = null;
    
    private Configuration configuration                 = null;
    private DASMeta dasMeta                             = null;
    private EDataGraph dasMetaDataGraph                 = null;
    private Context context                             = null;
    private ADSEmbeddedConnection adsEmbeddedConnection = null;

	
	public LdapDAS(Configuration configuration) 
	throws NamingException 
	{
	    metaDataObjectToRelativeDNCache             =
            new Hashtable<EDataObject, String>();
	    
	    
        this.configuration                          =
            configuration;

	    context                                     = 
	        new Context();
	    
	    JNDIConnection jndiConnection               = null;
		
		if (configuration.isEmbedded())
		{
			adsEmbeddedConnection = 
				new ADSEmbeddedConnection(configuration);
			context.
			setDasContext(
				adsEmbeddedConnection.
				connect(
						configuration.
						getDasPartitionName() ));

			context.
            setSchemaContext(
                adsEmbeddedConnection.
	            connect( 
	                configuration.
	                getSchemaPartitionName() ));
		}
		else
		{
			jndiConnection = 
				new JNDIConnection(configuration);
			
            context.
            setDasContext(
				jndiConnection.connect(
				    configuration.
				    getDasPartitionName() ));

            context.
            setSchemaContext(
				jndiConnection.connect(
				    configuration.
				    getSchemaPartitionName() ));
		}

		context.
		setDasMetaContext( 
		    MetaContextCreator.
		    create(
		        context.
		        getDasContext() ));
		
		context.setEcoreObjectClassesContext( 
		    LdapDASHelper.
		    createEcoreObjectClassesContext( 
		        context.
		        getSchemaContext() ));
		
		context.setEcoreAttributeTypesContext( 
            LdapDASHelper.
            createEcoreAttributeTypesContext( 
                context.
                getSchemaContext() ));
	}

	/**
	 * Write.
	 * 
	 * @param eDataGraph the e data graph
	 * @param dasContext the das context
	 * 
	 * @return the map< E data object, string>
	 * 
	 * @throws NamingException the naming exception
	 * 
	 * First tries to find a LdapContext that matches the xsdNamespace
	 * of the DataGraph that is about to be written.  If it finds one, then
	 * it know that the type system has been written, thus it can skip this 
	 * step.
	 * 
	 * If the LdapContext lookup fails, the type system may still exist
	 * the check:
	 * 
	 * configuration.getSupportedSchemas().contains(xsdNamespace)
	 * 
	 * is performed.
	 * 
	 * If the supported schema list contains the xsdNamespace, the 
	 * corresponding the LdapContext parent context of the root DataObject
	 * instance is created and the DataGraph written.
	 * 
	 * If the xsdNamespace is not contained by the supported schemas list,
	 * a final check is performed to see whether the type system exists.  If this
	 * check succeeds and exception is thrown, because it should not.  This could happen
	 * if the configuration write that occurs as the supported schema list is updated failed,
	 * or the the read of the supported schemas from the DIT failed.
	 * 
	 */
	public void create(
		EDataGraph eDataGraph) 
    throws NamingException, Exception
    {

    	EObject rootEObject     = 
    	    eDataGraph.
    	    getERootObject();
    	
    	EPackage ePackage       = 
    	    rootEObject.
    	    eClass().
    	    getEPackage();
    	
    	String xsdNamespace     = 
    	    ePackage.
    	    getNsURI();
    	
		LdapContext rootContext = 
			context.
			getXsdNamespaceToLdapContextMap().
			get(xsdNamespace);
		
		if (rootContext != null)
		{
        	EDataGraphCreator.create(
    				eDataGraph,
    				rootContext,
    				context.
    				getDataObjectToRelativeDNCache());
		}
		
		/*
		 * The root context was not yet created so we check if the 
		 * schema is written for the model instance, and if it is
		 * we create the root context and add it to the context map.
		 */
        metaDataObjectToRelativeDNCache =
            new Hashtable<EDataObject, String>();
        
        dasMetaDataGraph = 
            LdapDASHelper.readDasMeta( 
            context, 
            metaDataObjectToRelativeDNCache );
        
        dasMeta = ( DASMeta ) dasMetaDataGraph.getRootObject();

    	if (LdapDASHelper.isSchemaReady(xsdNamespace, dasMeta))
    	{
    		rootContext = 
    			InitialContextCreator.
    			create(
    					xsdNamespace, 
    					context.
    					getDasContext() );
    		
    		context.getXsdNamespaceToLdapContextMap().
    		put(xsdNamespace, rootContext);
    		
        	EDataGraphCreator.create(
    				eDataGraph,
    				rootContext,
    				context.getDataObjectToRelativeDNCache());
    	}
    	
        /*
         * The model's schema is not yet written (We also do a validation
         * check to make sure the server is not in an inconsistent state)
         * so we need to write the schema and then write the model instance.
         */
    	
    	if (LdapDASHelper.isSchemaWritten(
    			rootEObject.eClass(),
    			context.
    			getEcoreObjectClassesContext() ))
    	{
    		throw new RuntimeException("This should not happen");//TODO better message + constant
    	}
    	
    	List<EClass> eClassifiers = 
    		EcoreTypeSystemHelper.
    		createEClassifiersList(ePackage);
    	
		rootContext = 
			InitialContextCreator.
			create(
					xsdNamespace, 
					context.getDasContext() );
		
        context.getXsdNamespaceToLdapContextMap().
        put(xsdNamespace, rootContext);
		
		LdapContext dasModelMetaContext = 
			MetaContextCreator.
			create(rootContext);
    	
    	ModelTypeSystemCreator.create(
    			dasModelMetaContext, 
    			context.getEcoreAttributeTypesContext(), 
    			context.getEcoreObjectClassesContext(), 
    			eClassifiers, 
    			TUSCANY_OID_PREFIX_VALUE);
    	
    	dasMetaDataGraph.
    	getChangeSummary().
    	beginLogging();
    	
    	dasMeta.
    	getSupportedSchemas().
    	add(xsdNamespace);
    	
    	dasMetaDataGraph.
        getChangeSummary().
        endLogging();
    	
    	EDataGraphUpdater.update(
            dasMetaDataGraph,
            context.getDasMetaContext(),
            metaDataObjectToRelativeDNCache);
    	
        EDataGraphCreator.create(
            eDataGraph,
            rootContext,
            context.
            getDataObjectToRelativeDNCache());

    }
	

	public EDataGraph read(
	    EClass rootDataObjectEClass, 
	    String id) 
	throws LdapNameNotFoundException, NamingException
	{
	    String xsdNamespace     = 
	        rootDataObjectEClass.
	        getEPackage().
	        getNsURI();
	    
	    LdapContext rootContext =
	        context.
	        getXsdNamespaceToLdapContextMap().
	        get(xsdNamespace);
	    
	    if (rootContext == null)
	    {
            rootContext = 
                InitialContextCreator.
                create(
                        xsdNamespace, 
                        context.
                        getDasContext() );
            
            context.
            getXsdNamespaceToLdapContextMap().
            put(xsdNamespace, rootContext);
	    }
	    
	    return EDataGraphReader.read( 
	        rootDataObjectEClass, 
	        id, 
	        rootContext,
	        context.
	        getDataObjectToRelativeDNCache());
	}
		
	public void update(EDataGraph eDataGraph) 
	throws NamingException
	{
	   EDataObject rootDataObject  = 
	       ( EDataObject ) eDataGraph.getRootObject();
	   
	   EClass rootDataObjectEClass = 
	       rootDataObject.eClass();

       String xsdNamespace     = 
           rootDataObjectEClass.
           getEPackage().
           getNsURI();
       
       LdapContext rootContext =
           context.
           getXsdNamespaceToLdapContextMap().
           get(xsdNamespace);
       
       if (rootContext == null)
       {
           rootContext = 
               InitialContextCreator.
               create(
                       xsdNamespace, 
                       context.
                       getDasContext() );
           
           context.
           getXsdNamespaceToLdapContextMap().
           put(xsdNamespace, rootContext);
       }

	    EDataGraphUpdater.update( 
	        eDataGraph, 
	        rootContext,
	        context.
	        getDataObjectToRelativeDNCache());
	}

    public Context getContext()
    {
        return context;
    }

    public ADSEmbeddedConnection getAdsEmbeddedConnection()
    {
        return adsEmbeddedConnection;
    }
}