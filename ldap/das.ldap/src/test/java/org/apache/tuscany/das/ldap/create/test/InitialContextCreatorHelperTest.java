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
package org.apache.tuscany.das.ldap.create.test;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.create.InitialContextCreatorHelper;

import org.apache.tuscany.das.ldap.schema.embedded.setup.test.AbstractTestSetup;
import org.apache.tuscany.das.ldap.util.XSDNamespaceURITokenizer;
import org.eclipse.emf.common.util.URI;

/*
 * Note these tests require a running instance of ApacheDS
 */

public class InitialContextCreatorHelperTest 
extends AbstractTestSetup
{
    protected String xsdNamespace = "http://example.com/users/accounts";
    protected URI xsdNamespaceURI = URI.createURI( xsdNamespace );
    
    LdapContext comContext     = null;
    LdapContext exampleContext = null;
    LdapContext usersContext   = null;

    public void setUp() throws Exception
    {
    	super.setUp();
    }

    public void tearDown() throws Exception
    {
    	super.tearDown();
    }

    public void testCreateSystemSubContext() throws NamingException
    {
        String testContextRDN      = "cn=newContext";

        Object testContext         = null;
        
        try {
            testContext = dasPartitionContext.lookup( testContextRDN );            
        }
        catch (Exception e)
        {
            //good 
        }
        
        assertNull(testContext);
        
        InitialContextCreatorHelper.createSubContext(
            dasPartitionContext,
            testContextRDN,
            null);
        
        testContext = dasPartitionContext.lookup( testContextRDN );
        
        assertNotNull(testContext);
        dasPartitionContext.destroySubcontext( testContextRDN );
    }

    public void testCreateExistingSystemSubContext() throws NamingException
    {
        String testContextRDN      = "cn=newContext";
        Object testContext         = null;
        
        try {
            testContext = dasPartitionContext.lookup( testContextRDN );            
        }
        catch (Exception e)
        {
            //good 
        }
        
        assertNull(testContext);
        
        InitialContextCreatorHelper.createSubContext(
            dasPartitionContext,
            testContextRDN,
            null);
        
        //Run the same code again.
        InitialContextCreatorHelper.createSubContext(
            dasPartitionContext,
            testContextRDN,
            null);
        
        testContext = dasPartitionContext.lookup( testContextRDN );
        
        assertNotNull(testContext);
        dasPartitionContext.destroySubcontext( testContextRDN );
    }
    
    
    public void testCreateAuthorityContext() 
    throws NamingException
    {        
        try
        {
            comContext = 
                ( LdapContext ) 
                dasPartitionContext.lookup("cn=com");
        }
        catch ( NamingException e )
        {
            //Good - the subcontext does not exist
        }
        assertTrue(comContext==null);

        String[] authorityTokens    = 
            XSDNamespaceURITokenizer.createAuthorityTokens(xsdNamespaceURI);
        
        InitialContextCreatorHelper.createAuthorityContext(
            dasPartitionContext, 
            authorityTokens );
        
        comContext             =
            ( LdapContext ) dasPartitionContext.lookup("cn=com");

        assertNotNull(comContext);
        
        exampleContext             =
            ( LdapContext ) comContext.lookup("cn=example");

        assertNotNull(exampleContext);
        
        comContext.destroySubcontext( "cn=example" );
        dasPartitionContext.destroySubcontext( "cn=com" );
    }
    
    public void testCreatePathContext() throws NamingException
    {
        String[] authorityTokens   =  
            XSDNamespaceURITokenizer.
            createAuthorityTokens(xsdNamespaceURI);
        
        exampleContext             =
            InitialContextCreatorHelper.
            createAuthorityContext(
                dasPartitionContext, 
                authorityTokens );

        String[] pathTokens        =  
            XSDNamespaceURITokenizer.
            createPathTokens(xsdNamespaceURI);

        InitialContextCreatorHelper.
        createPathContext(
            exampleContext, 
            pathTokens );
        
        comContext                 =
            ( LdapContext ) 
            dasPartitionContext.
            lookup("cn=com");
        
        exampleContext             =
            ( LdapContext ) 
            comContext.
            lookup("cn=example");
        
        usersContext               =            ( LdapContext ) exampleContext.lookup("cn=users");

        usersContext.destroySubcontext( "cn=accounts" );
        exampleContext.destroySubcontext( "cn=users" );
        comContext.destroySubcontext( "cn=example" );
        dasPartitionContext.destroySubcontext( "cn=com" );
    }
}