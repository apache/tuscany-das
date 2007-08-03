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
package org.apache.tuscany.das.ldap.prototype.setup.test;

import javax.naming.NamingException;

import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.emf.create.EDataGraphCreator;
import org.apache.tuscany.das.ldap.schema.emf.destroy.EObjectClassDestroyer;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.sdo.EDataGraph;
import org.eclipse.emf.ecore.sdo.SDOFactory;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;

public class EDataGraphSetupTest 
extends CommonSetupTest
{
	protected EDataGraph eDataGraph                   = null;
	protected boolean readException                   = false;
	
	public void tearDown() throws NamingException, Exception
	{
        if (dataObjectToRelativeDNCache.containsValue(authorizationDataObject1RDN)
            || readException)
		{
			rootContext.destroySubcontext(authorizationDataObject1RDN);			
		}
		if (dataObjectToRelativeDNCache.containsValue(authorizationDataObject2RDN)
		    || readException)
		{
			rootContext.destroySubcontext(authorizationDataObject2RDN);			
		}
		if (dataObjectToRelativeDNCache.containsValue(configurationDataObjectRDN)
		    || readException)
		{
			rootContext.destroySubcontext(configurationDataObjectRDN);			
		}
		userContainerContext.destroySubcontext(configurationContainingFeatureRDN);
		
		userContainerContext.destroySubcontext(authorizationListContainingFeatureRDN);

        EObjectClassDestroyer.
        destroy(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext, 
            authorizationEClass, 
            TUSCANY_OID_PREFIX_VALUE );

        EObjectClassDestroyer.
        destroy(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext, 
            configurationEClass, 
            TUSCANY_OID_PREFIX_VALUE );
           
        super.tearDown();
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		
        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            authorizationEClass, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );

        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            configurationEClass, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );

    	eDataGraph             =
    		SDOFactory.
    		eINSTANCE.
    		createEDataGraph();
    	   		
    	eDataGraph.setERootObject(userDataObject);
    	
    	ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().
        getExtensionToFactoryMap().put
        ("xml",
        new XMLResourceFactoryImpl());
    	
    	eDataGraph.setResourceSet(resourceSet);
    	
    	EDataGraphCreator.create(
				eDataGraph,
				rootContext,
				dataObjectToRelativeDNCache);
    	
    	userContainerContext = 
    		(LdapContext) 
    		rootContext.
    		lookup(userDataObjectRDN);
    	
    	authorizationContainerContext =
    		(LdapContext) 
    		userContainerContext.
    		lookup(authorizationListContainingFeatureRDN);
    	
    	configurationContainerContext                             =
    		(LdapContext) 
    		userContainerContext.
    		lookup(configurationContainingFeatureRDN);

	}
}