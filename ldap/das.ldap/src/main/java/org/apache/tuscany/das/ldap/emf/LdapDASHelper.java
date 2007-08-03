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

import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.emf.create.EDataGraphCreator;
import org.apache.tuscany.das.ldap.emf.read.EDataGraphReader;
import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.ObjectClassConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaObjectClassConstants;
import org.apache.tuscany.das.ldap.schema.create.ComplexTypeRDNCreator;
import org.apache.tuscany.das.ldap.schema.emf.create.EcoreTypeSystemHelper;
import org.apache.tuscany.das.ldap.schema.emf.create.ModelTypeSystemCreator;
import org.apache.tuscany.model.ConfigurationFactory;
import org.apache.tuscany.model.DASMeta;
import org.apache.tuscany.model.impl.ConfigurationPackageImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.sdo.EDataGraph;
import org.eclipse.emf.ecore.sdo.EDataObject;
import org.eclipse.emf.ecore.sdo.SDOFactory;

public class LdapDASHelper
implements 
DASConstants, 
ObjectClassConstants,
AttributeTypeConstants,
SchemaObjectClassConstants
{
    public static EDataGraph readDasMeta(
        Context context,
        Map<EDataObject, String> metaDataObjectToRelativeDNCache) 
    throws Exception
    {
        EDataGraph dasMetaDataGraph = null;
        
        try
        {
            dasMetaDataGraph                            =
                EDataGraphReader.read(
                    ConfigurationPackageImpl.
                    eINSTANCE.getDASMeta(),  
                    DAS_META_ID_DEFAULT, 
                    context.
                    getDasMetaContext(),
                    metaDataObjectToRelativeDNCache);
        }
        catch(NamingException e)
        {
            List<EClass> eClassifiers = 
                EcoreTypeSystemHelper.
                createEClassifiersList(
                    ConfigurationPackageImpl.
                    eINSTANCE);
            
            ModelTypeSystemCreator.
            create( 
                context.getDasMetaContext(), 
                context.getEcoreAttributeTypesContext(), 
                context.getEcoreObjectClassesContext(), 
                eClassifiers, 
                TUSCANY_OID_PREFIX_VALUE );           
            
            DASMeta dasMeta = 
                ConfigurationFactory.
                INSTANCE.
                createDASMeta();
            
            dasMetaDataGraph =
                    SDOFactory.
                    eINSTANCE.
                    createEDataGraph();
            
            dasMetaDataGraph.
            setERootObject( 
                ( EObject ) dasMeta );
            
            EDataGraphCreator.create(
                dasMetaDataGraph, 
                context.
                getDasMetaContext(),
                metaDataObjectToRelativeDNCache);
        }
        return dasMetaDataGraph;
    }
    
	public static boolean isSchemaReady(
			String xsdNamespace, 
			DASMeta dasMeta)
	{
		return 
		dasMeta.
		getSupportedSchemas().
		contains(xsdNamespace);
	}
	
	public static boolean isSchemaWritten(
			EClass eClass,
			LdapContext ecoreObjectClassesContext)
	throws Exception
	{
		String xsdNamespace      =
			eClass.getEPackage().getNsURI();
		
        String eObjectClassRDN = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClass.getName());
        try {
            ecoreObjectClassesContext.
            lookup( eObjectClassRDN );
        }
        catch (Exception e)
        {
        	return false;
        }
        return true;
	}
	
	
	public static boolean isSchemaSupported(
	    String xsdNamespaceURI, 
	    DASMeta dasMeta)
	{
		return 
		dasMeta.
		getSupportedSchemas().
		contains(xsdNamespaceURI);
	}

	public static void initializeSchemaContexts(
	    LdapContext schemaContext,
	    LdapContext ecoreObjectClassesContext,
	    LdapContext ecoreAttributeTypesContext) 
	throws NamingException
	{
	    LdapContext ecoreContext   =
	        createEcoreContext(schemaContext);
	    
	    ecoreObjectClassesContext  = 
	        createEcoreObjectClassesContext(ecoreContext);
	    
	    ecoreAttributeTypesContext = 
	            createEcoreAttributeTypesContext( ecoreContext );
	}

	
	public static LdapContext createEcoreContext(
	    LdapContext schemaContext) 
	throws NamingException
    {
	    String ecoreContextRDN = 
	        CN + "=" + ECORE_CONTEXT_NAME;
	    
	    try
	    {
	        return ( LdapContext ) schemaContext.lookup( ecoreContextRDN );
	    }
	    catch (Exception e)
	    {
	        //Create the context
	    }

	    Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(META_SCHEMA);
    
        contextAttributes.put( objectClassAttribute );
        contextAttributes.put( CN, ECORE_CONTEXT_NAME );
        
        return 
        ( LdapContext ) 
        schemaContext.
        createSubcontext(
            ecoreContextRDN, 
            contextAttributes );
    }
	
    public static LdapContext createEcoreObjectClassesContext(
        LdapContext schemaContext) 
    throws NamingException
    {
        LdapContext ecoreContext            =
            createEcoreContext(schemaContext);
        
        String ecoreObjectClassesContextRDN = 
            OU + "=" + OBJECT_CLASSES_CONTEXT_NAME;
        
        try
        {
            return 
            ( LdapContext ) 
            ecoreContext.
            lookup( 
                ecoreObjectClassesContextRDN );
        }
        catch (Exception e)
        {
            //Create the context
        }

        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute = new BasicAttribute(
            OU, 
            OBJECT_CLASSES_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return 
        ( LdapContext ) 
        ecoreContext.
        createSubcontext( 
            ecoreObjectClassesContextRDN, 
            contextAttributes );
    }
    
    public static LdapContext createEcoreAttributeTypesContext(
        LdapContext schemaContext) 
    throws NamingException
    {
        LdapContext ecoreContext             =
            createEcoreContext(schemaContext);

        String ecoreAttributeTypesContextRDN = 
            OU + "=" + ATTRIBUTE_TYPES_CONTEXT_NAME;
        
        try
        {
            return 
            ( LdapContext ) 
            ecoreContext.
            lookup( 
                ecoreAttributeTypesContextRDN );
        }
        catch (Exception e)
        {
            //Create the context
        }

        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        contextAttributes.put( OU, ATTRIBUTE_TYPES_CONTEXT_NAME );
        contextAttributes.put( objectClassAttribute );
        
        return 
        ( LdapContext ) 
        ecoreContext.createSubcontext(
            ecoreAttributeTypesContextRDN, 
            contextAttributes );
    }
}