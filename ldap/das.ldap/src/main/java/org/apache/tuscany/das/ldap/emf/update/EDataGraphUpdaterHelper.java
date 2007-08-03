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
package org.apache.tuscany.das.ldap.emf.update;

import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.emf.create.EDataGraphCreatorHelper;
import org.apache.tuscany.das.ldap.emf.create.EDataObjectCreatorHelper;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.sdo.EChangeSummary;
import org.eclipse.emf.ecore.sdo.EDataObject;


public class EDataGraphUpdaterHelper
{
	public static void processDestroyedDataObjects(
			EChangeSummary eChangeSummary,
    		LdapContext containerContext,
    		Map<EDataObject, String> dataObjectToRelativeDNCache) 
	throws NamingException
	{
		EList<EObject> destroyedObjects                                           = 
			eChangeSummary.
			getObjectsToAttach();
    	
    	for (EObject destroyedObject : destroyedObjects)
    	{
    		String destroyedObjectRelativeDN = dataObjectToRelativeDNCache.get(destroyedObject);
    		containerContext.destroySubcontext(destroyedObjectRelativeDN);
    		dataObjectToRelativeDNCache.remove(destroyedObject);
    	}
	}
	
	public static void processCreatedDataObjects(
			EChangeSummary eChangeSummary,
    		LdapContext rootContext,
    		Map<EDataObject, String> dataObjectToRelativeDNCache ) 
	throws NamingException 
	{
		EList<EObject> createdObjects                                           = 
			eChangeSummary.
			getObjectsToDetach();
    		
            EDataGraphCreatorHelper.createChildren(
            		createdObjects,
            		rootContext,
            		dataObjectToRelativeDNCache);
	}
	
    public static void processChangedDataObjects(
    		EChangeSummary eChangeSummary,
    		LdapContext containerContext,
    		Map<EDataObject, String> dataObjectToRelativeDNCache ) 
    throws NamingException
    {
		EMap<EObject, EList<FeatureChange>> objectChanges      = 
			eChangeSummary.getObjectChanges();
    	
    	for (EObject changedDataObject : objectChanges.keySet())
    	{
    		EList<FeatureChange> featureChanges                              =
    			objectChanges.get(changedDataObject);
    		
    			ModificationItem[] modificationItems   =
    				new ModificationItem[featureChanges.size()];
    			
    	        EClass changedDataObjectEClass                                         = 
    	        	changedDataObject.eClass();
    	        
    	        String namespaceURI                                                              =
    	            changedDataObjectEClass.
    	            getEPackage().
    	            getNsURI();

        		for (int i = 0; i < featureChanges.size(); i++)
        		{
        			FeatureChange featureChange              = 
        				featureChanges.get(i);
        			
        			EStructuralFeature eStructuralFeature = 
        				featureChange.getFeature();
        			
        			if (eStructuralFeature instanceof EAttribute)
        			{
        				//TODO Note that we are not checking multiplicity many on EAttributes
        		        String qualifiedEAttributeName                            =
        		            SimpleTypeNamespaceQualifier.
        		            qualify( 
        		                namespaceURI, 
        		                changedDataObjectEClass.getName(), 
        		                eStructuralFeature.getName() );
        		        
        		        String normalizedEAttributeName                        =
        		        	QualifiedNameNormalizer.
        		        	normalize(qualifiedEAttributeName);
        		        
        		        //TODO PUt processEAttribute in  a more generic class
        		        Attribute attribute = 
        		        	EDataObjectCreatorHelper.processEAttribute(
        		        		(EAttribute)eStructuralFeature,
        		        		namespaceURI,
        		        		changedDataObjectEClass,
        		        		(EDataObject) changedDataObject);

        				modificationItems[i] = 
        					new ModificationItem(
        							DirContext.REPLACE_ATTRIBUTE,
        							attribute);
        			}
        			else
        			{
        				String qualifiedEReferenceName                            =
        		            SimpleTypeNamespaceQualifier.
        		            qualify( 
        		                namespaceURI, 
        		                changedDataObjectEClass.getName(), 
        		                eStructuralFeature.getName() );
        		        
        		        String normalizedEReferenceName                        =
        		        	QualifiedNameNormalizer.
        		        	normalize(qualifiedEReferenceName);

        		        Attribute attribute = 
        		        	new BasicAttribute(normalizedEReferenceName);

        		        if (eStructuralFeature.isMany())
        		        {
        		        	EList<EDataObject> referenceList = 
        		        		(EList<EDataObject>) changedDataObject.eGet(eStructuralFeature);
        		        	
        		        	if (referenceList.size() > 0 && referenceList!=null)
        		        	{
                		        for (EDataObject eDataObject : referenceList)
                		        {
                		        	attribute.add(
                		        			eDataObject.
                		        			eGet(
                		        					eDataObject.
                		        					eClass().
                		        					getEIDAttribute()));
                		        	
                    				modificationItems[i] = 
                    					new ModificationItem(
                    							DirContext.REPLACE_ATTRIBUTE,
                    							attribute);
                		        }
        		        	}
        		        	else 
        		        	{
        		        		modificationItems[i] = 
                					new ModificationItem(
                							DirContext.REMOVE_ATTRIBUTE,
                							attribute);
        		        	}
        		        }
        		        else
        		        {
        		        	EDataObject eDataObject = 
        		        		(EDataObject) 
        		        		changedDataObject.
        		        		eGet(eStructuralFeature);
        		        	
        		        	if (eDataObject != null)
        		        	{
            		        	attribute.add(
            		        			eDataObject.
            		        			eGet(
            		        					eDataObject.
            		        					eClass().
            		        					getEIDAttribute()));
            		        	
                				modificationItems[i] = 
                					new ModificationItem(
                							DirContext.REPLACE_ATTRIBUTE,
                							attribute);
        		        	}
        		        	else
        		        	{
                				modificationItems[i] = 
                					new ModificationItem(
                							DirContext.REMOVE_ATTRIBUTE,
                							attribute);
        		        	}
        		        }
        			}
    		}
    		String relativeDN = 
				dataObjectToRelativeDNCache.
				get(changedDataObject);

    		containerContext.modifyAttributes(
					relativeDN, 
					modificationItems);
    	}
    }
}