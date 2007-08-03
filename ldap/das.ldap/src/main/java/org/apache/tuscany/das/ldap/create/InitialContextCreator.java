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
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.util.XSDNamespaceURITokenizer;
import org.eclipse.emf.common.util.URI;

/**
 * The InitialContextCreator for a model's directory namespace
 */
public class InitialContextCreator
{
    /**
     * Creates the parent context for the root DataObject instance 
     * 
     * @param dasContext the dir context
     * @param xsdNamespace the xml namespace of the DataObjects
     * 
     * @return the directory context
     * 
     * @throws NamingException the naming exception
     */
    public static LdapContext create(
        String xsdNamespace, 
        LdapContext dasContext) throws NamingException
    {
        URI xsdNamespaceURI      = 
            URI.createURI( 
                xsdNamespace );
        
        String[] pathTokens      = 
            XSDNamespaceURITokenizer.
            createPathTokens( 
                xsdNamespaceURI );
        
        String[] authorityTokens = 
            XSDNamespaceURITokenizer.
            createAuthorityTokens( 
                xsdNamespaceURI );
        
        dasContext         = 
            InitialContextCreatorHelper.
            createAuthorityContext( 
                dasContext, 
                authorityTokens );
        
        dasContext         = 
            InitialContextCreatorHelper.
            createPathContext( 
                dasContext, 
                pathTokens );
        
        return dasContext;
    }
}