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
package org.apache.tuscany.das.ldap.connect;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import org.apache.tuscany.model.Configuration;

import org.apache.tuscany.das.ldap.constants.ApacheDSConnectionConstants;
import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.ObjectClassConstants;
/*
 * Just a class template class
 */
public class JNDIConnection
implements 
ApacheDSConnectionConstants, 
AttributeTypeConstants,
ObjectClassConstants
{    
    private Configuration configuration            = null;

    public JNDIConnection(Configuration configuration)
    {
    	this.configuration=configuration;
    }
    
    public Hashtable<String,Object> createEnvironment(
    		Hashtable<String, Object> env)
    {
        env.put( 
            Context.INITIAL_CONTEXT_FACTORY, 
            configuration.getInitialContextFactory() );

        env.put( 
            Context.SECURITY_AUTHENTICATION, 
            configuration.getSecurityAuthenticationType());
        
        env.put( 
            Context.SECURITY_PRINCIPAL, 
            configuration.getSecurityPrincipal() );
        
        env.put( 
            Context.SECURITY_CREDENTIALS, 
            configuration.getSecurityCredentials() );
        
        return env;
    }

    public LdapContext connect(String partitionName) throws NamingException
    {
    	Hashtable<String, Object> env = 
    		new Hashtable<String, Object>();
    	
    	env = createEnvironment(env);

    	env.put( 
                Context.PROVIDER_URL,
                getProviderURL(partitionName));
    	
        return new InitialLdapContext(env, null);
    }

    public String getProviderURL(String partitionName)
    {
    	return "ldap://" + 
    	configuration.getHost() + 
    	":" + 
    	configuration.getPort() + 
    	"/" + 
    	OU + 
    	"=" + 
    	partitionName;
    }
}