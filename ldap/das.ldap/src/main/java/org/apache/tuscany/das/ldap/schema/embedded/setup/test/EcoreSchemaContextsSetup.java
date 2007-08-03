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

public class EcoreSchemaContextsSetup 
extends AbstractTestSetup
implements DASConstants
{
    public void tearDown() 
    throws NamingException, Exception
    {
        ecoreSyntaxesContext.
        close();
        
        ecoreSchemaSubContext.
        destroySubcontext(
        		SYNTAXES_CONTEXT_RDN );

        ecoreSyntaxCheckersContext.
        close();
        
        ecoreSchemaSubContext.
        destroySubcontext(
        		SYNTAX_CHECKERS_CONTEXT_RDN );

        ecoreAttributeTypesContext.
        close();
        ecoreSchemaSubContext.
        destroySubcontext(
        		ATTRIBUTE_TYPES_CONTEXT_RDN );
        
        ecoreObjectClassesContext.
        close();
        
        ecoreSchemaSubContext.
        destroySubcontext( 
        		OBJECT_CLASSES_CONTEXT_RDN );
        
        ecoreSchemaSubContext.
        close();
        schemaPartitionContext.
        destroySubcontext( 
        		ECORE_CONTEXT_RDN );
        
        schemaPartitionContext.close();
        super.tearDown();
    }

    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        ecoreSchemaSubContext        = createEcoreSchemaSubContext();
        ecoreSyntaxesContext            = createEcoreSyntaxesContext();
        ecoreSyntaxCheckersContext = createEcoreSyntaxCheckersContext();
        ecoreObjectClassesContext    = createEcoreObjectClassesContext();
        ecoreAttributeTypesContext   = createEcoreAttributeTypesContext();
    }
    
    private LdapContext createEcoreSchemaSubContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(META_SCHEMA);

        Attribute ecoreAttribute = new BasicAttribute(
            CN, 
            ECORE_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return (LdapContext) 
        schemaPartitionContext.createSubcontext( 
            ECORE_CONTEXT_RDN, contextAttributes );
    }
    

    private LdapContext createEcoreObjectClassesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute = new BasicAttribute(
            OU, 
            OBJECT_CLASSES_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return (LdapContext) 
        ecoreSchemaSubContext.createSubcontext( 
            OBJECT_CLASSES_CONTEXT_RDN, contextAttributes );
    }
    
    

    private LdapContext createEcoreAttributeTypesContext() 
    throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute = new BasicAttribute(
            OU, 
            ATTRIBUTE_TYPES_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return (LdapContext) 
        ecoreSchemaSubContext.createSubcontext( 
            ATTRIBUTE_TYPES_CONTEXT_RDN, contextAttributes );
    }
    
    private LdapContext createEcoreSyntaxesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute = new BasicAttribute(
            OU, 
            SYNTAXES_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return (LdapContext) 
        ecoreSchemaSubContext.createSubcontext( 
            SYNTAXES_CONTEXT_RDN, contextAttributes );
    }
    
    private LdapContext createEcoreSyntaxCheckersContext() 
    throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute = new BasicAttribute(
            OU, 
            SYNTAX_CHECKERS_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return (LdapContext) 
        ecoreSchemaSubContext.createSubcontext( 
            SYNTAX_CHECKERS_CONTEXT_RDN, 
            contextAttributes);
    }

    protected LdapContext ecoreSchemaSubContext         = null;
    protected LdapContext ecoreObjectClassesContext    = null;
    protected LdapContext ecoreAttributeTypesContext   = null;
    protected LdapContext ecoreSyntaxesContext            = null;
    protected LdapContext ecoreSyntaxCheckersContext = null;
}