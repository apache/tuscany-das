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
package org.apache.tuscany.das.ldap.schema.embedded.setup.test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;

public class XSDSchemaContextsSetup 
extends AbstractTestSetup
implements DASConstants
{
    public void tearDown() throws NamingException, Exception
    {
        xsdSyntaxesContext.
        close();
        
        xsdContext.
        destroySubcontext( 
        		SYNTAXES_CONTEXT_RDN );
        
        xsdContext.
        close();
        
        schemaPartitionContext.
        destroySubcontext( 
        		XSD_CONTEXT_RDN );
        
        schemaPartitionContext.
        close();
        super.tearDown();
    }
    
    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        xsdContext                                = 
        	createXsdContext();
        xsdSyntaxesContext                 = 
        	createXsdSyntaxesContext();
    }
    
    private LdapContext createXsdContext() throws NamingException
    {
        Attributes contextAttributes    = 
        	new BasicAttributes();
        
        Attribute objectClassAttribute = 
        	new BasicAttribute(
        		OBJECT_CLASS,
        		TOP);
        objectClassAttribute.add(META_SCHEMA);

        Attribute ecoreAttribute           = 
        	new BasicAttribute(
            CN, 
            XSD_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return (LdapContext) schemaPartitionContext.createSubcontext( 
            XSD_CONTEXT_RDN, contextAttributes );
    }
    
    private LdapContext createXsdSyntaxesContext() throws NamingException
    {
        Attributes contextAttributes    = 
        	new BasicAttributes();
        
        Attribute objectClassAttribute = 
        	new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute           = 
        	new BasicAttribute(
            OU, 
            SYNTAXES_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return (LdapContext) xsdContext.createSubcontext( 
            SYNTAXES_CONTEXT_RDN, contextAttributes );
    }
    protected LdapContext xsdContext                            = 
    	null;
    protected LdapContext xsdSyntaxCheckersContext  = 
    	null;
    protected LdapContext xsdSyntaxesContext             = 
    	null;
}