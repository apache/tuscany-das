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
package org.apache.tuscany.das.ldap.schema.create;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.ObjectClassConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaAttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaObjectClassConstants;
import org.apache.tuscany.das.ldap.util.ComplexTypeNamespaceQualifier;

/**
 * The Class SyntaxEntryCreator.
 */
public class SyntaxEntryCreator 
implements 
AttributeTypeConstants,
SchemaAttributeTypeConstants,
ObjectClassConstants,
SchemaObjectClassConstants
{
    /**
     * Create.
     * 
     * @param oidPrefix the oid prefix
     * @param namespaceURI the namespace URI
     * @param dataTypeName the name of the DataType
     * @param syntaxesContext the directory context
     * 
     * @throws Exception the exception
     */
    public static void create(
        String oidPrefix,
        String namespaceURI,
        String dataTypeName,
        LdapContext syntaxesContext,
        LdapContext syntaxCheckersContext) 
    throws Exception
    {
        String syntaxDescription =
            ComplexTypeNamespaceQualifier.
            qualify(
            		namespaceURI, 
            		dataTypeName);
        
        String rdn = 
            ComplexTypeRDNCreator.
            create( 
                oidPrefix,
                namespaceURI,
                dataTypeName);

        Attributes syntaxAttributes = 
            prepareSyntaxAttributes( 
                syntaxDescription );
        
        Attributes syntaxCheckerAttributes = 
        	prepareSyntaxCheckerAttributes();
        
        //TODO Put back in when bug gets resolved.
        //TODO Remember to apply the namespace qualifier or we could just leave because it's in the description effectively
        //attributes.put(M_NAME, name );
        //attributes.put("x-humandReadible", "false");
        //attributes.put("m-obsolete, "false");
        syntaxesContext.createSubcontext(
            rdn, 
            syntaxAttributes);
        
        syntaxCheckersContext.createSubcontext(
        		rdn, 
        		syntaxCheckerAttributes);
    }

    /**
     * TODO - Move to helper
     * TODO - Optimize - we are prepping these each time when they could be cached
     * Prepare syntax attributes.
     * 
     * @param syntaxDescription the syntax description
     * 
     * @return the attributes
     */
    public static Attributes prepareSyntaxAttributes(
        String syntaxDescription)
    {
        Attribute objectClassAttribute = new BasicAttribute(
            OBJECT_CLASS, TOP);
        objectClassAttribute.add( META_TOP );
        objectClassAttribute.add( META_SYNTAX );
        
        Attribute descriptionAttribute = new BasicAttribute(
            M_DESCRIPTION, 
            syntaxDescription);
        
        Attributes attributes = new BasicAttributes();
        
        attributes.put( objectClassAttribute );
        attributes.put( descriptionAttribute );

        return attributes;
    }
    
    /**
     * TODO - Move to helper
     * TODO - Optimize - we are prepping these each time when they could be cached
     * Prepare SyntaxChecker attributes.
     * 
     * @return the attributes
     */
    public static Attributes prepareSyntaxCheckerAttributes()
    {
        Attribute objectClassAttribute = 
        	new BasicAttribute(
        			OBJECT_CLASS, 
        			TOP);
        objectClassAttribute.add( META_TOP );
        objectClassAttribute.add( META_SYNTAX_CHECKER );
        
        Attribute fqcnAttribute = 
        	new BasicAttribute(
        			M_FQCN,
        			M_FQCN_DEFAULT_VALUE);
        
        Attributes attributes = new BasicAttributes();
        
        attributes.put( objectClassAttribute );
        attributes.put( fqcnAttribute );

        return attributes;
    }
    
}