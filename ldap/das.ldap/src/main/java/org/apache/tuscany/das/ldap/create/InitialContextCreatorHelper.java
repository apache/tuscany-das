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
package org.apache.tuscany.das.ldap.create;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.ObjectClassConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaObjectClassConstants;

/**
 * The Class InitialContextCreatorHelper.
 */
public class InitialContextCreatorHelper
implements
ObjectClassConstants,
SchemaObjectClassConstants, 
AttributeTypeConstants
{

    /**
     * Creates the sub context if it does not already exist.
     * 
     * @param ldapContext the ldap context
     * @param subContextRDN the sub context
     * @param attributes the attributes
     * 
     * @return the ldap context
     * 
     * @throws NamingException the naming exception
     */
    public static LdapContext createSubContext(
        LdapContext ldapContext, 
        String subContextRDN,
        Attributes attributes) 
    throws NamingException
    {
        try
        {
            ldapContext = 
                ( LdapContext ) 
                ldapContext.
                lookup( subContextRDN );
        }
        catch ( NamingException e )
        {
            if (attributes == null)
            {
                ldapContext = 
                    ( LdapContext )
                    ldapContext.
                    createSubcontext( subContextRDN );                
            }
            else
            {
                ldapContext = 
                    ( LdapContext ) 
                    ldapContext.
                    createSubcontext( 
                        subContextRDN, 
                        attributes );
            }
        }
        return ldapContext;
    }

    /**
     * Creates the authority context.  Authority 
     * means the Authority component of a URL.  
     * For example if the authority is "example.com",
     * then the authority context will be "cn=example, cn=com".
     * 
     * @param ldapContext the ldap context
     * @param authorityTokens the authority tokens
     * 
     * @return the ldap context
     * 
     * @throws NamingException the naming exception
     * 
     */
    public static LdapContext createAuthorityContext(
    		LdapContext ldapContext, 
        String[] authorityTokens) 
    throws NamingException
    {
        for (int i = authorityTokens.length-1; i >= (0); i--)
        {
            String subContext = CN+ "=" + authorityTokens[i];
            ldapContext = (LdapContext) createSubContext( 
            		ldapContext, 
            		subContext, null );
        }
        return ldapContext;
    }
    
    /**
     * Creates the path context.
     * Path means the path component of a URL.  
     * For example if the authority is "example.com/users/accounts",
     * then the path context will be "cn=accounts, cn=users".  The full
     * model namespaced context will be "cn=accounts, cn=users, cn=example, cn=com"
     * 
     * @param authorityContext the ldap context
     * @param pathTokens the path tokens
     * 
     * @return the ldap context
     * 
     * @throws NamingException the naming exception
     */
    public static LdapContext createPathContext(
        LdapContext authorityContext, 
        String[] pathTokens) 
    throws NamingException
    {
        for (int i = 1; i < (pathTokens.length); i++)
        {
            String subContext = CN + "=" + pathTokens[i];
            authorityContext = (LdapContext) createSubContext( 
            		authorityContext, 
            		subContext, null );
        }
        return authorityContext;
    }
}
