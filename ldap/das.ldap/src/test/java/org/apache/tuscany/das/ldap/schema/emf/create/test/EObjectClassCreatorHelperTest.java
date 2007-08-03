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

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.schema.emf.create.EObjectClassCreatorHelper;
import org.apache.tuscany.das.ldap.schema.emf.create.EcoreTypeSystemHelper;
import org.apache.tuscany.das.ldap.schema.embedded.setup.test.DASContextSetup;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;

public class EObjectClassCreatorHelperTest
extends DASContextSetup
{
    private static EcoreFactory ecoreFactory = 
        EcoreFactory.eINSTANCE;
    
    private EClass eClassL0                  =
        null;

    private EClass eClassL1                  =
        null;

    public void setUp() 
    throws NamingException, Exception
    {
        super.setUp();
        
        eClassL0             = 
            ecoreFactory.
            createEClass();
        eClassL0.setName( "L0" );

        eClassL1             = 
            ecoreFactory.
            createEClass();
        eClassL1.setName( "L1" );
        
        eClassL1.
        getESuperTypes().
        add(eClassL0);
    }

    public void testCreateInheritanceMetaData() 
    throws NamingException
    {
        EObjectClassCreatorHelper.
        createInheritanceMetaData( 
            metaContext, 
            eClassL1 );
        
        DirContext parentContext = 
            ( DirContext ) 
            metaContext.
            lookup( "cn=" + eClassL0.getName() );
        
        assertNotNull(parentContext);
        
        DirContext childContext = 
            ( DirContext ) 
            parentContext.
            lookup( "cn=" + eClassL1.getName() );

        assertNotNull(childContext);

        parentContext.destroySubcontext( 
            "cn=" + eClassL1.getName() );
        
        metaContext.destroySubcontext( 
            "cn=" + eClassL0.getName() );
    }

    public void testGetEClassParent()
    {
        EClass eClassParent =
            null;
        
        eClassParent        = 
            EcoreTypeSystemHelper.
            getEClassParent(eClassL1);
        
        assertSame(eClassL0, eClassParent);
    }
}
