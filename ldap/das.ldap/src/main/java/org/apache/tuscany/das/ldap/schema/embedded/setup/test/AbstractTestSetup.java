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
import javax.naming.ldap.LdapContext;

import org.apache.directory.apacheds.testing.setup.ADSEmbeddedHotPartitionTemplate;
import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.EnumeratedSchemaAttributeTypeValues;
import org.apache.tuscany.das.ldap.schema.constants.ObjectClassConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaAttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaObjectClassConstants;
import org.apache.tuscany.das.ldap.testing.constants.DASTestingConstants;

public abstract class AbstractTestSetup 
extends ADSEmbeddedHotPartitionTemplate
implements 
EnumeratedSchemaAttributeTypeValues,
SchemaAttributeTypeConstants,
AttributeTypeConstants,
SchemaObjectClassConstants,
ObjectClassConstants,
DASConstants,
DASTestingConstants
{
    public void tearDown() throws NamingException, Exception
    {
        super.tearDown();
    }
    
    public void setUp() throws Exception
    {
        super.setUp();
        
        dasPartitionContext = 
        	connect("das");
        
        schemaPartitionContext = 
        	connect("schema");
    }
    
    protected LdapContext schemaPartitionContext                          =
    	null;
    
    protected LdapContext dasPartitionContext                                 =
    	null;
    
    protected static final String SYNTAXES_CONTEXT_RDN               = 
        OU + "=" + SYNTAXES_CONTEXT_NAME;

    protected static final String SYNTAX_CHECKERS_CONTEXT_RDN               = 
        OU + "=" + SYNTAX_CHECKERS_CONTEXT_NAME;

    protected static final String ATTRIBUTE_TYPES_CONTEXT_RDN  = 
        OU + "=" + ATTRIBUTE_TYPES_CONTEXT_NAME;
    
    protected static final String OBJECT_CLASSES_CONTEXT_RDN    = 
        OU + "=" + OBJECT_CLASSES_CONTEXT_NAME;
    
    
    
    protected static final String XSD_CONTEXT_RDN                         = 
        CN + "=" + XSD_CONTEXT_NAME;
    protected static final String DAS_CONTEXT_RDN                         = 
        CN + "=" + DAS_CONTEXT_NAME;
    protected static final String ECORE_CONTEXT_RDN                     = 
        CN + "=" + ECORE_CONTEXT_NAME;
}