/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
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

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.schema.create.ComplexTypeRDNCreator;
import org.apache.tuscany.das.ldap.schema.emf.create.EObjectClassCreator;
import org.apache.tuscany.das.ldap.schema.emf.destroy.EObjectClassDestroyer;
import org.apache.tuscany.das.ldap.schema.embedded.setup.test.EcoreSchemaAndDASContextsSetup;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

public class EObjectClassCreatorTest
extends EcoreSchemaAndDASContextsSetup
{
	boolean isObjectClassL0Created = false;
	boolean isObjectClassL1Created = false;
	
    public void tearDown()
    throws NamingException, Exception
    {
    	if (isObjectClassL1Created)
    	{
            EObjectClassDestroyer.
            destroy(
                metaContext,
                ecoreAttributeTypesContext,
                ecoreObjectClassesContext, 
                eClassL1, 
                TUSCANY_OID_PREFIX_VALUE );

            metaContext.
            destroySubcontext( 
                "cn="+eClassL0.getName() );
    	}

    	if (isObjectClassL0Created)
    	{
            EObjectClassDestroyer.
            destroy(
                metaContext,
                ecoreAttributeTypesContext,
                ecoreObjectClassesContext, 
                eClassL0, 
                TUSCANY_OID_PREFIX_VALUE );
    	}

        super.tearDown();
    }

    public void setUp() 
    throws NamingException, Exception
    {
        super.setUp();

        ecoreFactory                                 =
            EcoreFactory.eINSTANCE;
        ecorePackage                                =
            EcorePackage.eINSTANCE;

        processedEClassifiers                   = 
            new ArrayList<EClass>();
        
        eClassL0                                        =
            ecoreFactory.createEClass();
        
        eClassL1                                        =
            ecoreFactory.createEClass();
        
        eClassL0.setName( "L0" );
        eClassL1.setName( "L1" );
        
    	isObjectClassL0Created = false;
    	isObjectClassL1Created = false;

    }

    /*
     * Tests that eClassL0 and eClassL1 are both
     * created an the corresponding entries exist
     * in the meta section of the model.
     */
    public void testCreate0()
    throws NamingException, Exception
    {
        eClassL1.
        getESuperTypes().
        add(eClassL0);

        EPackage ePackage        = 
            ecoreFactory.
            createEPackage();
        
        ePackage.
        setNsURI(xsdNamespace);

        ePackage.
        getEClassifiers().
        add( eClassL0 );
        
        ePackage.
        getEClassifiers().
        add( eClassL1 );

        
        EObjectClassCreator
        eObjectClassCreator      =
            new EObjectClassCreator();
        
        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            eClassL1, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );
        
        String rdnL0 = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL0.getName());

        eObjectClassL0EntryContext = 
            ( LdapContext ) 
            ecoreObjectClassesContext.
            lookup( rdnL0 );

        assertNotNull(
            eObjectClassL0EntryContext);

        isObjectClassL0Created = true;

        String rdnL1 = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL1.getName());

          eObjectClassL1EntryContext = 
            ( LdapContext ) 
            ecoreObjectClassesContext.
            lookup( rdnL1 );

        assertNotNull(
            eObjectClassL1EntryContext);

        isObjectClassL1Created = true;
    }
    
    /*
     * Test that eClassL0 is added to the list
     * of processed eClassifiers and that the 
     * corresponding ObjectClass is created.
     * 
     * Also test the employeeNameEAttribute
     * and departmentEReferences are added.
     */
    public void testCreate1() 
    throws NamingException, Exception
    {
        EPackage ePackage                = 
            ecoreFactory.
            createEPackage();
        
        ePackage.
        setNsURI(xsdNamespace);

        ePackage.
        getEClassifiers().
        add( eClassL0 );


        EAttribute employeeNameEAttribute = 
            ecoreFactory.createEAttribute();
        employeeNameEAttribute.setName( 
            "employeeName" );
        employeeNameEAttribute.setEType( 
            ecorePackage.getEString() );

        eClassL0.
        getEStructuralFeatures().
        add(employeeNameEAttribute);

        EReference departmentEReference   =
            ecoreFactory.createEReference();
        departmentEReference.setName( "department" );
        departmentEReference.setEType( eClassL0 );
        
        eClassL0.
        getEStructuralFeatures().
        add(departmentEReference);

        EObjectClassCreator 
        eObjectClassCreator                 =
            new EObjectClassCreator();

        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            eClassL0, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );

        assertTrue(processedEClassifiers.contains( eClassL0 ));
        
        String rdnL0                        = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL0.getName());

        eObjectClassL0EntryContext          = 
            ( LdapContext ) 
            ecoreObjectClassesContext.
            lookup( rdnL0 );
        
        assertNotNull(
            eObjectClassL0EntryContext);

        isObjectClassL0Created = true;
        
        Attributes attributes               = 
            eObjectClassL0EntryContext.getAttributes( "" );
        
        NamingEnumeration namingEnumeration = 
            attributes.get( M_MAY ).getAll();
        
        boolean hasDepartmentValue          = 
            false;
        boolean hasEmployeeNameValue        = 
            false;
        
        String departmentValue              =
            "example-com-users-accounts-L0-department";
        
        String employeeNameValue            =
            "example-com-users-accounts-L0-employeeName";
            
            
        while (namingEnumeration.hasMore()) 
        {
            String attributeValue           =
                namingEnumeration.next().toString();
            if (departmentValue.equals(attributeValue))
            {
                hasDepartmentValue = true;
            }
            if (employeeNameValue.equals(attributeValue))
            {
                hasEmployeeNameValue = true;
            }
        }
        assertTrue(hasDepartmentValue);
        assertTrue(hasEmployeeNameValue);
    }
    
    /*
     * Test that both ObjectClasses are created
     * when eClassL1 inherits from eClassL0,
     * but eClass0's ObjectClass entry 
     * is already created.
     */
    public void testCreate3() 
    throws NamingException, Exception
    {
        EPackage ePackage        = 
            ecoreFactory.
            createEPackage();
        
        ePackage.
        setNsURI(xsdNamespace);

        ePackage.
        getEClassifiers().
        add( eClassL0 );
        
        ePackage.
        getEClassifiers().
        add( eClassL1 );
        
        EObjectClassCreator
        eObjectClassCreator      =
            new EObjectClassCreator();

        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            eClassL0, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );

        isObjectClassL0Created = true;
        
        eClassL1.
        getESuperTypes().
        add(eClassL0);

        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            eClassL1, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );
        
        String rdnL0 = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL0.getName());

        eObjectClassL0EntryContext = 
        ( LdapContext ) 
        ecoreObjectClassesContext.
        lookup( rdnL0 );
        
        assertNotNull(
            eObjectClassL0EntryContext);

        isObjectClassL0Created = true;

        String rdnL1 = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL1.getName());

        eObjectClassL1EntryContext = 
            ( LdapContext ) 
            ecoreObjectClassesContext.
            lookup( rdnL1 );

        assertNotNull(
            eObjectClassL1EntryContext);
        
        isObjectClassL1Created = true;
    }
        
    private static EcoreFactory ecoreFactory    =
        null;

    private static EcorePackage ecorePackage =
        null;
    
    private static EClass eClassL0                     =
        null;
    
    private static EClass eClassL1                     =
        null;
    

    private static List<EClass> 
    processedEClassifiers                                      =
        null;
    
    private static LdapContext 
    eObjectClassL0EntryContext               = 
        null;

    private static LdapContext 
    eObjectClassL1EntryContext               = 
        null;
    
    private static LdapContext 
    eClassL0MetaContext                      = 
        null;

    private static LdapContext 
    eClassL1MetaContext                      = 
        null;
 } 