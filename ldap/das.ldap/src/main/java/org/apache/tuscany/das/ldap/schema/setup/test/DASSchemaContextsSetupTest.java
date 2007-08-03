package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;

import org.apache.tuscany.das.ldap.constants.DASConstants;

public class DASSchemaContextsSetupTest 
extends DASSchemaContextsSetup
implements DASConstants
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
    
    public void testCreateEcoreContext()
    {
        assertNotNull(dasContext);
    }

    
    public void testCreateDasAttributeTypesContext()
    {
        assertNotNull(dasAttributeTypesContext);
    }
    
    public void testCreateDasSyntaxesContext()
    {
        assertNotNull(dasSyntaxesContext);
    }
    
    public void testCreateDasObjectClassesContext()
    {
        assertNotNull(dasObjectClassesContext);
    }

}