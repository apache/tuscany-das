package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;
import org.apache.tuscany.das.ldap.constants.DASConstants;

public class SchemaContextSetup 
extends AbstractTestSetup
implements DASConstants
{
    public void tearDown() throws NamingException, Exception
    {
        schemaContext.close();
        super.tearDown();
    }
    
    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        schemaContext            = connect();
    }
}