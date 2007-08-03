package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;

public class XSDSchemaContextsSetup 
extends AbstractTestSetup
implements DASConstants
{
    public void tearDown() throws NamingException, Exception
    {
        xsdSyntaxesContext.close();
        xsdContext.destroySubcontext( SYNTAXES_CONTEXT_RDN );
        xsdContext.close();
        schemaContext.destroySubcontext( XSD_CONTEXT_RDN );
        schemaContext.close();
        super.tearDown();
    }
    
    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        schemaContext         = connect();
        xsdContext                = createXsdContext();
        xsdSyntaxesContext = createXsdSyntaxesContext();
    }
    
    private DirContext createXsdContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(META_SCHEMA);

        Attribute ecoreAttribute = new BasicAttribute(
            CN, 
            XSD_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return schemaContext.createSubcontext( 
            XSD_CONTEXT_RDN, contextAttributes );
    }
    
    private DirContext createXsdSyntaxesContext() throws NamingException
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
        
        return xsdContext.createSubcontext( 
            SYNTAXES_CONTEXT_RDN, contextAttributes );
    }

    protected DirContext xsdContext                = null;
    protected DirContext xsdSyntaxesContext = null;
}