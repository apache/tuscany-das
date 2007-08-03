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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class EDataGraphReaderHelper
implements DASConstants
{
	public static String calculateRelativeDN(
			LdapContext containerContext, 
			LdapContext dataObjectContext) throws NamingException
	{
    	String containerDN    = containerContext.getNameInNamespace();
    	String dataObjectDN = dataObjectContext.getNameInNamespace();
    	
    	int beginIndex = 0;
    	int endIndex    = 
    		dataObjectDN.length() - containerDN.length() - 1;

    	String result    = 
    		dataObjectDN.substring(
    				beginIndex, 
    				endIndex);
    	
    	return result;
	}
	

    public static EDataObject restoreEDataObject(
    		Map<EDataObject, Map<EStructuralFeature, List<String>>> crossReferenceIDCache,
    		EClass eClass,
    		String namespaceURI,
    		Attributes attributes) 
    throws NamingException
    {
        EDataObject eDataObject =
        	(EDataObject)
        	eClass.getEPackage().
        	getEFactoryInstance().
        	create(eClass);
        
        EDataObjectReaderHelper.
        restoreEAttributes( 
            eClass, 
            eDataObject, 
            namespaceURI, 
            attributes );

        EStructuralFeature[] eCrossReferences = 
        	((EClassImpl.FeatureSubsetSupplier)
        			eDataObject.
        			eClass().
        			getEAllStructuralFeatures()).
        			crossReferences();
        
        if (eCrossReferences != null)
        {
        	Map<EStructuralFeature, List<String>> eReferenceToEObjectIDs = 
        		new Hashtable<EStructuralFeature, List<String>>();
        	
            for (EStructuralFeature eReference : eCrossReferences)
            {
                String qualifiedEReferenceName             =
                    SimpleTypeNamespaceQualifier.
                    qualify( 
                        namespaceURI, 
                        eClass.getName(), 
                        eReference.getName() );
                
            	String normalizedEReferenceName         =
                	QualifiedNameNormalizer.
                	normalize(qualifiedEReferenceName);

            	if (eReference.isMany())
            	{
                	List<String> idList                                =
                		new ArrayList<String>();

            		Attribute eReferenceAttribute             = 
            			attributes.get(normalizedEReferenceName);
            		
            		NamingEnumeration namingEnumeration = 
            			eReferenceAttribute.getAll();
            		
            		while (namingEnumeration.hasMore())
            		{
            			String id = (String) namingEnumeration.next();
            			idList.add(id);
                    	eReferenceToEObjectIDs.put(eReference, idList);
            		}
            	}
            	else
            	{
                	List<String> idList                                   =
                		new ArrayList<String>();

                	String value                                               = 
                		(String) 
                		attributes.
                		get(normalizedEReferenceName).
                		get();
                	
                	idList.add(value);
                	eReferenceToEObjectIDs.put(eReference, idList);
            	}
            	crossReferenceIDCache.put(
            			eDataObject, 
            			eReferenceToEObjectIDs);
            }
        }
        return eDataObject;
    }
    
    public static void restoreCrossReferences(
    		Map<EDataObject, Map<EStructuralFeature, List<String>>> crossReferenceIDCache,
    		Resource resource)
    {
    	for (EDataObject eDataObject : crossReferenceIDCache.keySet())
    	{
    		Map<EStructuralFeature, List<String>> eReferenceToIDListMap = 
    			crossReferenceIDCache.get(eDataObject);
    		
    		for (EStructuralFeature eStructuralFeature : eReferenceToIDListMap.keySet())
    		{
    			if (eStructuralFeature.isMany())
    			{
    				List<String> eReferenceIDs = 
    					eReferenceToIDListMap.
    					get(eStructuralFeature);
    				
    				EList<EDataObject> multiplicityManyList = null;
    				if (eReferenceIDs.size() > 0)
    				{
    			    	multiplicityManyList =
    			    		new BasicEList<EDataObject>();
    				}
    				for (String eReferenceID : eReferenceIDs)
    				{
        				Object referencedDataObject = resource.getEObject(eReferenceID);
        				multiplicityManyList.add((EDataObject) referencedDataObject);
        				eDataObject.eSet(
        						eStructuralFeature, 
        						multiplicityManyList);
    				}
    			}
    			else
    			{
    				String eReferenceID = 
    					eReferenceToIDListMap.
    					get(eStructuralFeature).
    					get(0);
    				
    				Object referencedDataObject = resource.getEObject(eReferenceID);
    				eDataObject.eSet(
    						eStructuralFeature, 
    						referencedDataObject);
    			}
    		}
    	}
    }
    
    public static void addContainmentDataObjects(
    		Map crossReferenceIDCache,
    		Map<EDataObject, String> dataObjectToRelativeDNCache,
    		EDataObject containerDataObject,
    		Attributes attributes,
    		String namespaceURI,
    		LdapContext eDataObjectContext,
    		LdapContext rootContext) throws NamingException
    {
    	EClass eClass                                            =
    		containerDataObject.eClass();
    	
    	List<EReference> eReferences               =
    		eClass.getEAllContainments();
    	
    	for (EReference eReference : eReferences)
    	{
            String qualifiedEReferenceName          =
                SimpleTypeNamespaceQualifier.
                qualify( 
                    namespaceURI, 
                    eClass.getName(), 
                    eReference.getName() );
            
        	String normalizedReferenceName         =
            	QualifiedNameNormalizer.
            	normalize(qualifiedEReferenceName);
        	
        	Attribute attribute                                  =
        		attributes.
        		get(normalizedReferenceName);
        	
        	if (attribute.size() > 0)
        	{
        		LdapContext eReferenceContainmentContext = 
        			(LdapContext) 
        			eDataObjectContext.
        			lookup("cn=" + eReference.getName());
        		
        		EClass eReferenceType                                 =
        			(EClass) eReference.getEType();
        		
        		EAttribute eReferenceTypeEIDAttribute     =
        			eReferenceType.getEIDAttribute();
        
        		String qualifiedEReferenceTypeIDEAttributeName  =
                    SimpleTypeNamespaceQualifier.
                    qualify( 
                        namespaceURI, 
                        eReferenceType.getName(), 
                        eReferenceTypeEIDAttribute.getName() );
                
            	String normalizedReferenceTypeEIDAttributeName         =
                	QualifiedNameNormalizer.
                	normalize(qualifiedEReferenceTypeIDEAttributeName);
            	
        		if (eReference.isMany())
        		{
        	    	EList<EDataObject> containmentList =
        	    		new BasicEList<EDataObject>();
        	    	
                	NamingEnumeration idNamingEnumeration =
                		attribute.getAll();
                	
                	while(idNamingEnumeration.hasMore())
                	{
                		String containedDataObjectID                    = 
                			(String) idNamingEnumeration.next();
                		
                		String containedDataObjectEntryRDN       =
                			normalizedReferenceTypeEIDAttributeName
                			+ "=" + containedDataObjectID;
                		
                		Attributes eReferenceAttributes                =
                			eReferenceContainmentContext.
                			getAttributes(containedDataObjectEntryRDN);
                		
                		
                		EDataObject eReferenceDataObject             = 
                			EDataGraphReaderHelper.
                			restoreEDataObject(
                					crossReferenceIDCache, 
                					eReferenceType,
                					namespaceURI,
                					eReferenceAttributes);

                		//TODO Consider using aspects
                		String relativeDN = 
                			calculateRelativeDN(
                					rootContext, 
                					(LdapContext) 
                					eReferenceContainmentContext.
                					lookup(containedDataObjectEntryRDN));
                		dataObjectToRelativeDNCache.put(eReferenceDataObject, relativeDN);
                		//End of Aspect

                		containmentList.add(eReferenceDataObject);
                		
                		if (eReferenceDataObject.eClass().getEAllContainments().size() > 0)
                		{
                			addContainmentDataObjects(
                		    		crossReferenceIDCache,
                		    		dataObjectToRelativeDNCache,
                		    		eReferenceDataObject,
                		    		eReferenceAttributes,
                		    		namespaceURI,
                		    		(LdapContext) eReferenceContainmentContext.
                		    		lookup(containedDataObjectEntryRDN),
                		    		rootContext);
                		}
                	}
                	containerDataObject.eSet(eReference, containmentList);
        		}
        		else
        		{
        			String containedDataObjectID                    = 
            			(String) attribute.get();

            		String containedDataObjectEntryRDN       =
            			normalizedReferenceTypeEIDAttributeName
            			+ "=" + containedDataObjectID;
            		
            		Attributes eReferenceAttributes                =
            			eReferenceContainmentContext.
            			getAttributes(containedDataObjectEntryRDN);
            		
            		EDataObject eReferenceDataObject          = 
            			EDataGraphReaderHelper.
            			restoreEDataObject(
            					crossReferenceIDCache, 
            					eReferenceType,
            					namespaceURI,
            					eReferenceAttributes);

            		//TODO Consider using aspects
            		String relativeDN = 
            			calculateRelativeDN(
            					rootContext, 
            					(LdapContext) 
            					eReferenceContainmentContext.
            					lookup(containedDataObjectEntryRDN));
            		
            		dataObjectToRelativeDNCache.put(eReferenceDataObject, relativeDN);
            		//End of Aspect

                	containerDataObject.eSet(eReference, eReferenceDataObject);

                	if (eReferenceDataObject.eClass().getEAllContainments().size() > 0)
            		{
            			addContainmentDataObjects(
            		    		crossReferenceIDCache, 
            		    		dataObjectToRelativeDNCache,
            		    		eReferenceDataObject,
            		    		eReferenceAttributes,
            		    		namespaceURI,
            		    		(LdapContext) eReferenceContainmentContext.
            		    		lookup(containedDataObjectEntryRDN),
            		    		rootContext);
            		}
            	}
       		}
       	}
    }
}