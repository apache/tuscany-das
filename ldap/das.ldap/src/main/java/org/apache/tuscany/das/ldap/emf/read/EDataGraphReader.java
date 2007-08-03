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
package org.apache.tuscany.das.ldap.emf.read;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.directory.shared.ldap.exception.LdapNameNotFoundException;
import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.sdo.EDataGraph;
import org.eclipse.emf.ecore.sdo.EDataObject;
import org.eclipse.emf.ecore.sdo.SDOFactory;

public class EDataGraphReader
implements DASConstants
{
    public static EDataGraph read(
    		EClass rootDataObjectEClass,
    		String id,
    		LdapContext rootContext,
    		Map<EDataObject, String> dataObjectToRelativeDNCache) 
    throws NamingException, LdapNameNotFoundException
    {
    	EDataGraph eDataGraph                     =
        		SDOFactory.eINSTANCE.createEDataGraph();

    	String namespaceURI                       =
            rootDataObjectEClass.getEPackage().
            getNsURI();

        EAttribute idEAttribute                   =
    		rootDataObjectEClass.getEIDAttribute();

        String qualifiedIDEAttributeName          =
            SimpleTypeNamespaceQualifier.
            qualify( 
                namespaceURI, 
                rootDataObjectEClass.getName(), 
                idEAttribute.getName() );
        
    	String normalizedIDEAttributeName         =
        	QualifiedNameNormalizer.
        	normalize(qualifiedIDEAttributeName);
        
    	String eDataObjectRDN                     = 
        	normalizedIDEAttributeName + "=" + id;
        
        LdapContext eDataObjectContext            =
        	(LdapContext) 
        	rootContext.
        	lookup(eDataObjectRDN);
        
        Attributes attributes                     = 
        	rootContext.
        	getAttributes(eDataObjectRDN);
        
        Map<EDataObject, Map<EStructuralFeature, List<String>>> crossReferenceIDCache                      =
        	new Hashtable<EDataObject, Map<EStructuralFeature, List<String>>>();

    	EDataObject rootDataObject          =
    		EDataGraphReaderHelper.
    		restoreEDataObject(
    				crossReferenceIDCache,
    				rootDataObjectEClass, 
    				namespaceURI, 
    				attributes);

        String relativeDN                          =
        	EDataGraphReaderHelper.calculateRelativeDN(
        			rootContext, 
        			eDataObjectContext);
        
        dataObjectToRelativeDNCache.
        put(rootDataObject, relativeDN);
    	
    	List<EReference> eReferences               =
    		rootDataObjectEClass.getEAllContainments();
    	
    	if (rootDataObjectEClass.getEAllContainments().size() > 0)
    	{
    		EDataGraphReaderHelper.
        	addContainmentDataObjects(
        			crossReferenceIDCache,
        			dataObjectToRelativeDNCache,
        			rootDataObject, 
        			attributes, 
        			namespaceURI, 
        			eDataObjectContext,
        			rootContext);
    	}

    	eDataGraph.setERootObject(rootDataObject);

    	Resource resource = eDataGraph.getRootResource();
    	
    	EDataGraphReaderHelper.restoreCrossReferences(
    			crossReferenceIDCache, 
    			resource);

    	return eDataGraph;
    }
}