
package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.apache.directory.apacheds.testing.setup.JNDIConnectionTemplate;
import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.EnumeratedSchemaAttributeTypeValues;
import org.apache.tuscany.das.ldap.schema.constants.ObjectClassConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaAttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaObjectClassConstants;

public abstract class AbstractTestSetup 
extends JNDIConnectionTemplate
implements 
EnumeratedSchemaAttributeTypeValues,
SchemaAttributeTypeConstants,
AttributeTypeConstants,
SchemaObjectClassConstants,
ObjectClassConstants,
DASConstants
{
    protected LdapContext schemaContext                         = 
        null;

    protected static String PROVIDER_URL_VALUE                 =
        "ldap://localhost:10389/ou=schema";
    
    protected static final String SYNTAXES_CONTEXT_RDN         = 
        OU + "=" + SYNTAXES_CONTEXT_NAME;

    protected static final String ATTRIBUTE_TYPES_CONTEXT_RDN  = 
        OU + "=" + ATTRIBUTE_TYPES_CONTEXT_NAME;
    
    protected static final String OBJECT_CLASSES_CONTEXT_RDN   = 
        OU + "=" + OBJECT_CLASSES_CONTEXT_NAME;
    
    protected static final String XSD_CONTEXT_RDN              = 
        CN + "=" + XSD_CONTEXT_NAME;
    protected static final String DAS_CONTEXT_RDN              = 
        CN + "=" + DAS_CONTEXT_NAME;
    protected static final String ECORE_CONTEXT_RDN            = 
        CN + "=" + ECORE_CONTEXT_NAME;
    
    protected String xsdNamespace                   = 
        "http://example.com/users/accounts";

}