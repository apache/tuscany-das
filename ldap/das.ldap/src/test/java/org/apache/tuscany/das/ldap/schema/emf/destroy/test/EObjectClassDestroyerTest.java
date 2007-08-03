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
package org.apache.tuscany.das.ldap.schema.emf.destroy.test;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.prototype.setup.Prototype;
import org.apache.tuscany.das.ldap.schema.create.ComplexTypeRDNCreator;
import org.apache.tuscany.das.ldap.schema.emf.create.EObjectClassCreator;
import org.apache.tuscany.das.ldap.schema.emf.destroy.EObjectClassDestroyer;
import org.apache.tuscany.das.ldap.schema.embedded.setup.test.EcoreSchemaAndDASContextsSetup;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

/*
 * TODO Make sure that we test EAttribute deletion as well.
 */
public class EObjectClassDestroyerTest
extends EcoreSchemaAndDASContextsSetup
implements Prototype
{
    public void tearDown() 
    throws NamingException, Exception
    {
        super.tearDown();
    }

    public void setUp() 
    throws NamingException, Exception
    {
        super.setUp();

        ecoreFactory                   =
            EcoreFactory.eINSTANCE;
        ecorePackage                 =
            EcorePackage.eINSTANCE;


        processedEClassifiers            = 
            new ArrayList<EClass>();
        
        eClassL0                                 =
            ecoreFactory.
            createEClass();
        
        eClassL1                                 =
            ecoreFactory.
            createEClass();
        
        eClassL0.setName( "L0" );
        eClassL1.setName( "L1" );
    }
    
    /*
     * Test the deletion of two
     * single level hierarchy ObjectClasses
     */
    public void testDestroy0() 
    throws NamingException, Exception
    {
        EPackage ePackage               = 
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
        eObjectClassCreator             =
            new EObjectClassCreator();
        
        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            eClassL0, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );
        
        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            eClassL1, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );
        
        String rdnL0                           = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL0.getName());

        String rdnL1                           = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL1.getName());

        eObjectClassL0EntryContext = 
            ( LdapContext ) 
            ecoreObjectClassesContext.
            lookup( rdnL0 );

        eObjectClassL1EntryContext = 
        ( LdapContext ) 
        ecoreObjectClassesContext.
        lookup( rdnL1 );
        
        assertNotNull(
            eObjectClassL0EntryContext);
        
        assertNotNull(
            eObjectClassL1EntryContext);
        
        EObjectClassDestroyer.
        destroy( 
            metaContext,
            ecoreAttributeTypesContext, 
            ecoreObjectClassesContext, 
            eClassL0, 
            TUSCANY_OID_PREFIX_VALUE );

        EObjectClassDestroyer.
        destroy(
            metaContext,
            ecoreAttributeTypesContext, 
            ecoreObjectClassesContext, 
            eClassL1, 
            TUSCANY_OID_PREFIX_VALUE );
        
        try
        {
            eObjectClassL0EntryContext = 
                ( LdapContext ) 
                ecoreObjectClassesContext.
                lookup( rdnL0 );
        }
        catch (Exception e)
        {
            eObjectClassL0EntryContext = 
                null;
        }

        try
        {
            eObjectClassL1EntryContext = 
                ( LdapContext ) 
                ecoreObjectClassesContext.
                lookup( rdnL1 );
        }
        catch (Exception e)
        {
            eObjectClassL1EntryContext = 
                null;
        }
        
        assertNull(
            eObjectClassL0EntryContext);
        
        assertNull(
            eObjectClassL1EntryContext);
    }

    /*
     * Testt the deletion of two
     * ObjectClasses where L1 inherits from
     * L0
     */

    public void testDestroy1() 
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
        eObjectClassCreator           =
            new EObjectClassCreator();
        
        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            eClassL1, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );
        
        String rdnL0                          = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL0.getName());

        String rdnL1                          = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                eClassL1.getName());

        eObjectClassL0EntryContext = 
            ( LdapContext ) 
            ecoreObjectClassesContext.
            lookup( rdnL0 );

        eObjectClassL1EntryContext = 
        ( LdapContext ) 
        ecoreObjectClassesContext.
        lookup( rdnL1 );
        
        assertNotNull(
            eObjectClassL0EntryContext);
        
        assertNotNull(
            eObjectClassL1EntryContext);

        eClassL0MetaContext            =
            ( LdapContext ) 
            metaContext.
            lookup( 
                "cn=" + eClassL0.getName() );

        assertNotNull(
            eClassL0MetaContext );
        
        eClassL1MetaContext            =
            ( LdapContext ) 
            eClassL0MetaContext.
            lookup( 
                "cn=" + eClassL1.getName() );

        assertNotNull(
            eClassL0MetaContext );
        
        assertNotNull(
            eClassL1MetaContext );

        EObjectClassDestroyer.
        destroy(
            metaContext,
            ecoreAttributeTypesContext, 
            ecoreObjectClassesContext, 
            eClassL1, 
            TUSCANY_OID_PREFIX_VALUE );

        EObjectClassDestroyer.
        destroy( 
            metaContext,
            ecoreAttributeTypesContext, 
            ecoreObjectClassesContext, 
            eClassL0, 
            TUSCANY_OID_PREFIX_VALUE );

        try
        {
            eObjectClassL0EntryContext = 
                ( LdapContext ) 
                ecoreObjectClassesContext.
                lookup( rdnL0 );
        }
        catch (Exception e)
        {
            eObjectClassL0EntryContext = 
                null;
        }

        try
        {
            eObjectClassL1EntryContext = 
                ( LdapContext ) 
                ecoreObjectClassesContext.
                lookup( rdnL1 );
        }
        catch (Exception e)
        {
            eObjectClassL1EntryContext = 
                null;
        }
        
        assertNull(
            eObjectClassL0EntryContext);
        
        assertNull(
            eObjectClassL1EntryContext);

        
        eClassL0MetaContext            =
            ( LdapContext ) 
            metaContext.
            lookup( 
                "cn=" + eClassL0.getName() );

        assertNotNull(
            eClassL0MetaContext );

        try
        {
            eClassL1MetaContext            =
                ( LdapContext ) 
                eClassL0MetaContext.
                lookup( 
                    "cn=" + eClassL1.getName() );

        }
        catch (Exception e)
        {
            eClassL1MetaContext = 
                null;
        }
        
        assertNull(
            eClassL1MetaContext);

        metaContext.
        destroySubcontext( 
            "cn=" + eClassL0.getName() );
    }
    
    /*
     * Test the deletion of an ObjectClass that has 
     * has attributes.
     */
    public void testDestroy2() throws NamingException, Exception
    {
    	userEClass.getEStructuralFeatures().add(userIDEAttribute);
    	userEClass.getEStructuralFeatures().add(userNameEAttribute);
    	userEClass.getEStructuralFeatures().add(userPasswordEAttribute);
    	userEClass.getEStructuralFeatures().add(userAuthorizationEReference);
    	
        EPackage ePackage        = 
            ecoreFactory.
            createEPackage();
        
        ePackage.
        setNsURI(xsdNamespace);

        ePackage.
        getEClassifiers().
        add( userEClass );

        EObjectClassCreator
        eObjectClassCreator           =
            new EObjectClassCreator();
        
        eObjectClassCreator.
        create(
            metaContext,
            ecoreAttributeTypesContext,
            ecoreObjectClassesContext,
            userEClass, 
            processedEClassifiers,
            TUSCANY_OID_PREFIX_VALUE );
    	
        String rdnL0                          = 
            ComplexTypeRDNCreator.
            create(
                TUSCANY_OID_PREFIX_VALUE,
                xsdNamespace, 
                userEClass.getName());
        
        EObjectClassDestroyer.
        destroy(
            metaContext,
            ecoreAttributeTypesContext, 
            ecoreObjectClassesContext, 
            userEClass, 
            TUSCANY_OID_PREFIX_VALUE );
    }

    private static EcoreFactory ecoreFactory =
        null;

    private static EcorePackage ecorePackage =
        null;
    
    private static EClass eClassL0           =
        null;
    
    private static EClass eClassL1           =
        null;

    private static List<EClass> 
    processedEClassifiers                            =
        null;
    
    private static LdapContext 
    eObjectClassL0EntryContext                  = 
        null;

    private static LdapContext 
    eObjectClassL1EntryContext                  = 
        null;
    
    private static LdapContext 
    eClassL0MetaContext                             = 
        null;

    private static LdapContext 
    eClassL1MetaContext                             = 
        null;
}