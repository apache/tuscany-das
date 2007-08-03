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
package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.schema.create.ComplexTypeRDNCreator;
import org.apache.tuscany.das.ldap.schema.create.SimpleTypeRDNCreator;

public class DASSchemaContextsSetup 
extends AbstractTestSetup
implements DASConstants
{
    public void tearDown() throws NamingException, Exception
    {
        dasSyntaxesContext.close();
        
        dasContext.destroySubcontext(    
            SYNTAXES_CONTEXT_RDN );
        
        dasAttributeTypesContext.close();
        
        dasContext.destroySubcontext(
            ATTRIBUTE_TYPES_CONTEXT_RDN );
        
        dasContext.destroySubcontext(
            OBJECT_CLASSES_CONTEXT_RDN );
        
        dasContext.close();
        
        schemaContext.destroySubcontext( 
            DAS_CONTEXT_RDN );
        
        schemaContext.close();
        super.tearDown();
    }
    
    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        schemaContext                   = connect();
        dasContext                          = (LdapContext) createDasContext();
        dasSyntaxesContext          = (LdapContext) createDasSyntaxesContext();
        dasAttributeTypesContext = (LdapContext) createDasAttributeTypesContext();
        dasObjectClassesContext  = (LdapContext) createDasObjectClassesContext();
        
        mComplexMayRDN           = 
            SimpleTypeRDNCreator.create( 
                TUSCANY_OID_PREFIX_VALUE, 
                DAS_XSD_NAMESPACE, 
                M_META_TOP_SDO_OBJECT_CLASS,
                M_COMPLEX_MAY);
        
        mComplexMustRDN          = 
            SimpleTypeRDNCreator.create( 
                TUSCANY_OID_PREFIX_VALUE, 
                DAS_XSD_NAMESPACE, 
                M_META_TOP_SDO_OBJECT_CLASS,
                M_COMPLEX_MUST);
        
        idRDN                    = 
            SimpleTypeRDNCreator.create( 
                TUSCANY_OID_PREFIX_VALUE, 
                DAS_XSD_NAMESPACE, 
                M_META_TOP_SDO_OBJECT_CLASS,
                ID);

        
        metaTopSDORDN            =
            ComplexTypeRDNCreator.create( 
                TUSCANY_OID_PREFIX_VALUE,
                DAS_XSD_NAMESPACE,
                M_META_TOP_SDO_OBJECT_CLASS);
    }
    
    private DirContext createDasContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(META_SCHEMA);

        Attribute ecoreAttribute = new BasicAttribute(
            CN, 
            DAS_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return schemaContext.createSubcontext( 
            DAS_CONTEXT_RDN, contextAttributes );
    }

    private DirContext createDasAttributeTypesContext() throws NamingException
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
        
        return dasContext.createSubcontext( 
            ATTRIBUTE_TYPES_CONTEXT_RDN, contextAttributes );
    }
    
    private DirContext createDasSyntaxesContext() throws NamingException
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
        
        return dasContext.createSubcontext( 
            SYNTAXES_CONTEXT_RDN, contextAttributes );
    }
    
    private DirContext createDasObjectClassesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);

        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute attributeTypes = new BasicAttribute(
            OU, 
            OBJECT_CLASSES_CONTEXT_NAME);

        contextAttributes.put( attributeTypes );
        contextAttributes.put( objectClassAttribute );
        
        return dasContext.createSubcontext( 
            OBJECT_CLASSES_CONTEXT_RDN, 
            contextAttributes );
    }

    protected LdapContext dasContext                          = null;
    protected LdapContext dasAttributeTypesContext = null;
    protected LdapContext dasSyntaxesContext           = null;
    protected LdapContext dasObjectClassesContext   = null;
    
    protected String mComplexMayRDN                         = null;
    protected String mComplexMustRDN                        = null;
    protected String idRDN                                               = null;
    protected String metaTopSDORDN                             = null;
}