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
 */
package org.apache.tuscany.das.ldap.schema.emf.create;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.constants.SyntaxOIDValues;
import org.apache.tuscany.das.ldap.schema.create.AbstractAttributeTypeCreator;
import org.apache.tuscany.das.ldap.schema.create.SimpleTypeRDNCreator;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

public class EReferenceTypeCreator
extends AbstractAttributeTypeCreator
implements SyntaxOIDValues
{
    public EReferenceTypeCreator()
    {
        super();
    }
    
    public void create(
        DirContext attributeTypesContext,
        EReference eReference,
        String oidPrefix) 
    throws NamingException, Exception
    {
        String eReferenceName             = 
            eReference.getName();
        
        EClass eContainingClass           = 
            eReference.getEContainingClass();
        
        String eContainingClassName       = 
            eContainingClass.getName();
        
        String namespaceURI               = 
            eContainingClass.
            getEPackage().
            getNsURI();

        String qualifiedEReferenceNameURI = 
            SimpleTypeNamespaceQualifier.qualify(
                namespaceURI, 
                eContainingClassName,
                eReferenceName);
        
        
        String normalizedEReferenceName   =
        	QualifiedNameNormalizer.
        	normalize(qualifiedEReferenceNameURI);
        
        basicAttributes.put( 
            M_DESCRIPTION, 
            eReferenceName);
        
        basicAttributes.put( 
            M_SYNTAX,      
            SYNTAX_STRING_OID_VALUE);
        
        basicAttributes.put( 
            M_NAME,        
            normalizedEReferenceName );

        String rdn = 
            SimpleTypeRDNCreator.create( 
                oidPrefix,
                namespaceURI,
                eContainingClassName,
                eReferenceName);
        
        attributeTypesContext.createSubcontext(
            rdn, 
            basicAttributes);
    }
}