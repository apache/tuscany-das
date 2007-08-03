package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;

import org.apache.tuscany.das.ldap.constants.DASConstants;

public class EcoreSchemaContextsSetupTest 
extends EcoreSchemaContextsSetup
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
        assertNotNull(ecoreContext);
    }
    
    public void testCreateEcoreObjectClassesContext()
    {
        assertNotNull(ecoreObjectClassesContext);
    }
    
    public void testCreateEcoreAttributeTypesContext()
    {
        assertNotNull(ecoreAttributeTypesContext);
    }
    
    public void testCreateEcoreSyntaxesContext()
    {
        assertNotNull(ecoreSyntaxesContext);
    }
}