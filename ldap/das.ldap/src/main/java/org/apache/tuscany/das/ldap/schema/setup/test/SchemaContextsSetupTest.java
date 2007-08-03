package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;

public class SchemaContextsSetupTest 
extends SchemaContextsSetup
{
    public void tearDown() throws NamingException, Exception
    {
        super.tearDown();
    }
    
    public void setUp() throws NamingException, Exception
    {
        super.setUp();
    }
    
    public void testConnect()
    {
        assertNotNull(schemaContext);
    }
    

    public void testCreateDASContext()
    {
        assertNotNull(dasContext);
    }

    
    public void testCreateXSDContext()
    {
        assertNotNull(xsdContext);
    }


    
    public void testCreateXSDSyntaxesContext()
    {
        assertNotNull(xsdSyntaxesContext);
    }


    public void testCreateEcoreContext()
    {
        assertNotNull(ecoreContext);
    }

    
    
    public void testCreateEcoreSyntaxesContext()
    {
        assertNotNull(ecoreSyntaxesContext);
    }
    
    public void testCreateSyntaxesContext()
    {
        assertNotNull(dasSyntaxesContext);
    }
    
    public void testCreateAttributeTypesContext()
    {
        assertNotNull(dasAttributeTypesContext);
    }
    
    
    public void testCreateObjectClassesContext()
    {
        assertNotNull(dasObjectClassesContext);
    }
    
}