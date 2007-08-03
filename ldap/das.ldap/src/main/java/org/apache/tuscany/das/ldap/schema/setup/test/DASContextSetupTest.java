package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;

import org.apache.tuscany.das.ldap.constants.DASConstants;

public class DASContextSetupTest 
extends DASContextSetup
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
    
    public void testCreateDASPartitionContext()
    {
        assertNotNull(dasPartitionContext);
    }
    
    public void testModelContext()
    {
        assertNotNull(modelContext);
    }
    public void testMetaContext()
    {
        assertNotNull(metaContext);
    }
}