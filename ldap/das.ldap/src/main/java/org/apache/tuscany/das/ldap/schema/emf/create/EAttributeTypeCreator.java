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
package org.apache.tuscany.das.ldap.schema.emf.create;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.schema.create.AbstractAttributeTypeCreator;
import org.apache.tuscany.das.ldap.schema.create.SimpleTypeRDNCreator;
import org.apache.tuscany.das.ldap.schema.emf.provide.SyntaxOIDProvider;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

public class EAttributeTypeCreator
extends AbstractAttributeTypeCreator
{
    public EAttributeTypeCreator()
    {
        super();
    }
    /**
     * Create.
     * 
     * @param oidPrefix the oid prefix
     * @param eAttribute the e attribute
     * @param attributeTypesContext the attribute types context
     * 
     * @throws Exception the exception
     * @throws NamingException the naming exception
     */
    public void create(
        DirContext attributeTypesContext,
        EAttribute eAttribute,
        String oidPrefix) 
    throws NamingException, Exception
    {
        String eAttributeName                     = 
            eAttribute.getName();
        
        EClass eContainingClass                  = 
            eAttribute.getEContainingClass();
        
        String eContainingClassName          = 
            eContainingClass.getName();
        
        String namespaceURI                        = 
            eContainingClass.
            getEPackage().getNsURI();

        String qualifiedEAttributeNameURI = 
            SimpleTypeNamespaceQualifier.qualify(
                namespaceURI, 
                eContainingClassName,
                eAttributeName);
        
        String normalizedEAttributeName   =
        	QualifiedNameNormalizer.
        	normalize(qualifiedEAttributeNameURI);
        
        EClassifier eDataType                       = 
            eAttribute.getEType();
        
        SyntaxOIDProvider dataTypeToADSSyntaxMapProvider = 
        	new SyntaxOIDProvider();
        
        String syntaxOID                              =
        	dataTypeToADSSyntaxMapProvider.
        	getSyntaxOID(eDataType);
        
        basicAttributes.put( 
            M_DESCRIPTION, 
            eAttributeName);
        
        basicAttributes.put( 
            M_SYNTAX,      
            syntaxOID);
        
        basicAttributes.put( 
            M_NAME,        
            normalizedEAttributeName );

        String rdn = 
            SimpleTypeRDNCreator.create( 
                oidPrefix,
                namespaceURI,
                eContainingClassName,
                eAttributeName);
        
        attributeTypesContext.createSubcontext(
            rdn, 
            basicAttributes);
    }
}