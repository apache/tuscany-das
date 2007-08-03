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

import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.util.ComplexTypeNamespaceQualifier;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class EDataObjectCreator
implements DASConstants
{
    /**
     * Create the LDAP entry for the EDataObject argument
     * 
     * @param eDataObject the e data object
     * @param containerContext the container context
     * 
     * @throws NamingException the naming exception
     */
    public static void create(
    		EDataObject eDataObject, 
    		LdapContext containerContext,
    		Map<EDataObject, String> dataObjectToRelativeDNCache) 
    throws NamingException
    {
    	String rdn                                                     =
            null;
        Object dataObjectID                                   =
            null;
        
        EClass eClass                                              = 
            eDataObject.eClass();
        
    	EAttribute idEAttribute                              =
        	eClass.getEIDAttribute();
        
        String namespaceURI                                  =
            eClass.getEPackage().getNsURI();
        
        String qualifiedEAttributeName                 =
            null;
        String normalizedEAttributeName             =
            null;

        Attributes attributes                                   =
            new BasicAttributes();
        
        attributes                                                     =
        	EDataObjectCreatorHelper.processEAttributes(
        			attributes, 
        			eDataObject, 
        			eClass, 
        			namespaceURI);
        
        attributes                                                     =
        	EDataObjectCreatorHelper.processEReferences(
        			attributes, 
        			eDataObject, 
        			eClass, 
        			namespaceURI);
           
        String qualifiedEClassName                        =
            ComplexTypeNamespaceQualifier.
            qualify( 
                namespaceURI, 
                eClass.getName() );
        
        String normalizedEClassName                   =
        	QualifiedNameNormalizer.
        	normalize(qualifiedEClassName);

        Attribute objectClassAttribute                  =
        	new BasicAttribute("objectClass");
        
        objectClassAttribute.
        add(normalizedEClassName);
        
        attributes.put(objectClassAttribute);
        
        dataObjectID                                               =
            eDataObject.eGet(idEAttribute );
        
        qualifiedEAttributeName                            =
            SimpleTypeNamespaceQualifier.
            qualify( 
                namespaceURI, 
                eClass.getName(), 
                idEAttribute.getName() );
        
        normalizedEAttributeName                        =
        	QualifiedNameNormalizer.
        	normalize(qualifiedEAttributeName);
        
        rdn                                                                 = 
        	normalizedEAttributeName + "=" + dataObjectID;  
        
        containerContext.createSubcontext( 
            rdn, 
            attributes );
        
        EDataObjectCreatorHelper.
        updateDataObjectToRelativeDNCache(
        		eDataObject, 
        		dataObjectToRelativeDNCache, 
        		rdn);
    }
}