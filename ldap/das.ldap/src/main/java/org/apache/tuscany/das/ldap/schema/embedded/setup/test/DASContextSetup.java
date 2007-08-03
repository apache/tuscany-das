package org.apache.tuscany.das.ldap.schema.embedded.setup.test;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.create.InitialContextCreator;
import org.apache.tuscany.das.ldap.create.MetaContextCreator;
import org.apache.tuscany.das.ldap.destroy.InitialContextDestroyer;
import org.apache.tuscany.das.ldap.destroy.MetaContextDestroyer;

public class DASContextSetup 
extends AbstractTestSetup
implements DASConstants
{
    public void tearDown() throws NamingException, Exception
    {
        MetaContextDestroyer.
        destroy( modelContext );
        
        InitialContextDestroyer.
        destroy(
            modelContext, 
            dasPartitionContext );
        super.tearDown();
                
        dasPartitionContext.close();
    }
    
    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        modelContext                               = 
            InitialContextCreator.
            create(
                xsdNamespace,
                dasPartitionContext);

        metaContext                               =
            MetaContextCreator.
            create( modelContext);
    }
    
    protected LdapContext modelContext             = null;
    protected LdapContext metaContext              = null;
}