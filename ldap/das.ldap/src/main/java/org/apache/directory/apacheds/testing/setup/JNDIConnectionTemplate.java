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
package org.apache.directory.apacheds.testing.setup;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.ApacheDSConnectionConstants;

import junit.framework.TestCase;

/*
 * Just a class template class
 */
public abstract class JNDIConnectionTemplate
extends TestCase
implements ApacheDSConnectionConstants
{
    protected String providerHost                    =
        "ldap://localhost:10389/";

    protected String providerPath                    =
    	SYSTEM_PROVIDER_PATH;

    private String  providerURL                          =
        providerHost + providerPath;

    protected String initialContextFactory     = 
        SUN_CONTEXT_FACTORY;
    
    protected String  securityAuthentication = 
        SIMPLE_SECURITY_AUTHENTICATION_VALUE;
    
    protected String  securityPrincipal            = 
        DEFAULT_SECURITY_PRINCIPAL_VALUE;
    
    protected String credentials                      =
        DEFAULT_SECURITY_CREDENTIALS_VALUE;

    public Hashtable<String,Object> createEnvironment(
    		Hashtable<String, Object> env)
    {
      providerURL                                                  =
            providerHost + providerPath;

        env.put( 
            Context.PROVIDER_URL,
            providerURL);

        env.put( 
            Context.INITIAL_CONTEXT_FACTORY, 
            initialContextFactory );

        env.put( 
            Context.SECURITY_AUTHENTICATION, 
            securityAuthentication);
        
        env.put( 
            Context.SECURITY_PRINCIPAL, 
            securityPrincipal );
        
        env.put( 
            Context.SECURITY_CREDENTIALS, 
            credentials );
        
        return env;
    }

    public LdapContext connect() throws NamingException
    {
    	Hashtable<String, Object> env = 
    		new Hashtable<String, Object>();
    	
        return new InitialLdapContext(
        		createEnvironment(env), null);
    }
    
    public void setUp() throws Exception
    {
    	super.setUp();
    }
}