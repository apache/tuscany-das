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
package org.apache.tuscany.das.ldap.schema.emf.create.test;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.schema.create.SimpleTypeRDNCreator;
import org.apache.tuscany.das.ldap.schema.emf.create.EReferenceTypeCreator;
import org.apache.tuscany.das.ldap.schema.emf.destroy.EStructuralFeatureTypeDestroyer;
import org.apache.tuscany.das.ldap.schema.embedded.setup.test.EcoreSchemaContextsSetup;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;

public class EReferenceTypeCreatorTest 
extends EcoreSchemaContextsSetup
{
    private static final String namespaceURI = 
        "http://org.apache.tuscany/datatypes";
    
    private static EcoreFactory ecoreFactory = 
        EcoreFactory.eINSTANCE;
    
    private static EReference eReference     = 
        ecoreFactory.createEReference();
    
    private static String eReferenceName     = 
        "ole";
    
    private static DirContext eReferenceEntryContext = 
        null;
    
    public void setUp() 
    throws NamingException, Exception
    {
        super.setUp();
    }
    
    public void tearDown() 
    throws NamingException, Exception
    {
        EStructuralFeatureTypeDestroyer.
        destroy(
            ecoreAttributeTypesContext, 
            eReference, 
            TUSCANY_OID_PREFIX_VALUE);
        
        super.tearDown();
    }
    
    public void testCreate() throws NamingException, Exception
    {
        EPackage ePackage            = 
            ecoreFactory.
            createEPackage();
        
        ePackage.
        setNsURI(namespaceURI);
        
        EClass eContainingClass      = 
            ecoreFactory.
            createEClass();
        
        String eContainingClassName  = 
            eContainingClass.getName();

        ePackage.
        getEClassifiers().
        add( eContainingClass );
        
        eContainingClass.
        getEStructuralFeatures().
        add(eReference);
        
        eReference.
        setName(eReferenceName);
        
        eReference.
        setEType(eContainingClass);
        
        EReferenceTypeCreator
        eReferenceTypeCreator =
            new EReferenceTypeCreator();
        
        eReferenceTypeCreator.
        create(
            ecoreAttributeTypesContext, 
            eReference, 
            TUSCANY_OID_PREFIX_VALUE );

        String rdn = 
            SimpleTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                namespaceURI, 
                eContainingClassName,
                eReferenceName);
  
        eReferenceEntryContext = 
        ( DirContext ) 
        ecoreAttributeTypesContext.
        lookup( rdn );
        
        assertNotNull(eReferenceEntryContext);
    }
}