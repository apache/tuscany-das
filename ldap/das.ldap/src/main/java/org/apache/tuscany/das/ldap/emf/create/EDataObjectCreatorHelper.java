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
package org.apache.tuscany.das.ldap.emf.create;

import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class EDataObjectCreatorHelper
implements DASConstants
{
	
	public static void updateDataObjectToRelativeDNCache(
			EDataObject eDataObject,
			Map<EDataObject, String> dataObjectToRelativeDNCache,
			String rdn)
	{
        EDataObject parentDataObject = 
        	(EDataObject) eDataObject.eContainer();
        
	    if (parentDataObject !=null)
	    {
	    	String parentRDN = 
	    		dataObjectToRelativeDNCache.
	    		get(parentDataObject);
	    	rdn                         = 
	    		rdn + 
	    		"," + 
	    		"cn=" + 
	    		eDataObject.eContainingFeature().getName() + 
	    		"," +  
	    		parentRDN;
	    	dataObjectToRelativeDNCache.put(eDataObject, rdn);
	    }
	    else 
	    {
	    	dataObjectToRelativeDNCache.put(eDataObject, rdn);
	    }
	}
    
    
    /**
     * Process EAttributes adding the normalized EAttribute instance's
     * name and corresponding values to the attributes argument.
     * 
     * @param attributes the attributes
     * @param eDataObject the e data object
     * @param eClass the e class
     * @param namespaceURI the namespace URI
     * 
     * @return the attributes
     * 
     * @throws NamingException the naming exception
     * 
     * Note that ApacheDS supports the Syntaxes (DataTypes) 
     * Boolean, Integer, String.  This means that all values that
     * are not Boolean or Integer are stored as Strings.
     */
    public static Attributes processEAttributes(
    		Attributes attributes,
    		EDataObject eDataObject,
    		EClass eClass,
    	    String namespaceURI) 
    throws NamingException
    {
        EcorePackage ecorePackage                   =
        	EcorePackage.eINSTANCE;
        
    	EAttribute idEAttribute                     =
        	eClass.getEIDAttribute();
        
        List<EAttribute> eAttributes                = 
            eClass.getEAllAttributes();
        
        String qualifiedEAttributeName              =
            null;
        String normalizedEAttributeName             =
            null;

        for (EAttribute eAttribute : eAttributes)
        {
        	Attribute attribute = 
        		processEAttribute(
        				eAttribute, 
        				namespaceURI, 
        				eClass, eDataObject);
        	attributes.put(attribute);
        }
		return attributes;
    }
    
    /*
     * TODO This is also used in updates so consider moving to a utility
     */
    public static Attribute processEAttribute(
    		EAttribute eAttribute,
    		String namespaceURI,
    		EClass eClass,
    		EDataObject eDataObject)
    {
        String qualifiedEAttributeName                     =
            SimpleTypeNamespaceQualifier.
            qualify( 
                namespaceURI, 
                eClass.getName(), 
                eAttribute.getName() );
        
        String normalizedEAttributeName                    =
        	QualifiedNameNormalizer.
        	normalize(qualifiedEAttributeName);
        
        Attribute attribute                                = 
        	new BasicAttribute(normalizedEAttributeName);
        
        if (eAttribute.isMany())
        {
            if ( !(eAttribute.getEType() == EcorePackage.eINSTANCE.getEBoolean() || 
                         eAttribute.getEType() == EcorePackage.eINSTANCE.getEBooleanObject() ) )
            {
                EList<Object> eAttributeValues             = 
                    ( EList<Object> ) eDataObject.eGet( eAttribute );
                
                for (Object eAttributeValue : eAttributeValues)
                {
                    attribute.add(eAttributeValue.toString() );
                }
            }
            else
            {
                EList<Boolean> eAttributeValues             = 
                    ( EList<Boolean> ) eDataObject.eGet( eAttribute );
                
                for (Boolean eAttributeValue : eAttributeValues)
                {
                    if (eAttributeValue == false)
                    {
                        attribute.add(Boolean.FALSE.toString().toUpperCase() );
                    }
                    else
                    {
                        attribute.add(Boolean.TRUE.toString().toUpperCase() );
                    }
                }
            }
            return attribute;
        }
    
    	if ( !(eAttribute.getEType() == EcorePackage.eINSTANCE.getEBoolean() || 
    	     eAttribute.getEType() == EcorePackage.eINSTANCE.getEBooleanObject() ) )
    	{
            Object eAttributeValue                        = 
                eDataObject.eGet( eAttribute );
            
            attribute.add(eAttributeValue.toString() );
    	}
    	else
    	{
            Boolean eAttributeValue                      = 
                (Boolean) eDataObject.eGet( eAttribute );
            
            if (eAttributeValue == false)
            {
                attribute.add(Boolean.FALSE.toString().toUpperCase() );
            }
            else
            {
                attribute.add(Boolean.TRUE.toString().toUpperCase() );
            }
    	}
    	return attribute;
    }
    
    /**
     * Process references adding the normalized EReference instance's
     * name and corresponding id (IDs for multiplicity many references)
     * to the attributes argument.
     * 
     * @param attributes the attributes
     * @param eDataObject the e data object
     * @param eClass the e class
     * @param namespaceURI the namespace URI
     * 
     * @return the attributes
     */
    public static Attributes processEReferences(
    		Attributes attributes,
    		EDataObject eDataObject,
    		EClass eClass,
    	    String namespaceURI)
    {
        List<EReference> eReferences   =
        	eClass.getEAllReferences();
        
        for (EReference eReference : eReferences)
        {
        	String qualifiedEReferenceName                   =
                SimpleTypeNamespaceQualifier.
                qualify( 
                    namespaceURI, 
                    eClass.getName(), 
                    eReference.getName() );
            
            String normalizedEReferenceName                =
            	QualifiedNameNormalizer.
            	normalize(qualifiedEReferenceName);
        	
            if (eReference.isMany())
            {
            	List<EDataObject> referencedEDataObjects =
            		(List<EDataObject>) eDataObject.eGet(eReference);
            	
            	Attribute idAttribute                                        =
            		new BasicAttribute(normalizedEReferenceName);

            	for (EDataObject containedEDataObject : referencedEDataObjects)
            	{
            		EClass containedEDataObjectEClass          =
                		containedEDataObject.eClass();
                	
                	String containedEDataObjectID                   =
                		(String) containedEDataObject.eGet(
        					containedEDataObjectEClass.
        					getEIDAttribute());
                	
                	idAttribute.add(containedEDataObjectID);
            	}
            	attributes.put(idAttribute);
            }
            else
            {
            	EDataObject containedEDataObject          =
            		(EDataObject) 
            		eDataObject.eGet(eReference);

            	if (containedEDataObject != null)
            	{
                	EClass containedEDataObjectEClass      =
                		containedEDataObject.eClass();
                	
                	String containedEDataObjectID               =
                		(String) containedEDataObject.eGet(
        					containedEDataObjectEClass.
        					getEIDAttribute());
                	
                	attributes.put(
                			normalizedEReferenceName, 
                			containedEDataObjectID);
            	}
            }
        }
		return attributes;
    }
}