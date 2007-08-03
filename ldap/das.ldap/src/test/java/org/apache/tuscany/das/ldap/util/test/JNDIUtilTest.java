package org.apache.tuscany.das.ldap.util.test;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.schema.embedded.setup.test.AbstractTestSetup;
import org.apache.tuscany.das.ldap.util.JNDIUtil;

/*
 * Note that these tests rely on the ADS Partition
 * ou=system, which is also the directoryContext
 * that the tests start with.
 */
public class JNDIUtilTest 
extends AbstractTestSetup
{
    DirContext test1Context = null;
    DirContext test2Context = null;
    DirContext test3Context = null;
    
    public void setUp() throws Exception
    {
        super.setUp();
        test1Context = 
            ( DirContext ) 
            dasPartitionContext.
            createSubcontext( "cn=test1" );
        
        test2Context = 
            ( DirContext ) 
            test1Context.
            createSubcontext( "cn=test2" );
        
        test3Context = 
            ( DirContext ) 
            test2Context.
            createSubcontext( "cn=test3" );
    }
    
    public void tearDown() throws Exception
    {   
        test2Context.destroySubcontext( "cn=test3" );
        test1Context.destroySubcontext("cn=test2");
        dasPartitionContext.destroySubcontext("cn=test1");
        super.tearDown();
    }

    public void testCalculateParentRelativeDN() throws NamingException
    {
        String partitionDN = "ou=das";
        
        String testA = 
            JNDIUtil.calculateParentRelativeDN( 
                test2Context, partitionDN );
        
        String testB = 
            JNDIUtil.calculateParentRelativeDN( 
                test3Context, partitionDN );
        
        String testC = 
            JNDIUtil.calculateParentRelativeDN( test1Context, partitionDN );
        
        assertTrue(testA.equals( "cn=test1" ));
        assertTrue(testB.equals( "cn=test2,cn=test1" ));
        assertTrue(testC.equals( "ou=das" ));

    }
    
    public void testCalculateDNComponents() throws NamingException
    {
        String[] contextComponents = JNDIUtil.calculateDNComponents(  test3Context );
        assertTrue(contextComponents.length==4);
        
        contextComponents = null;
        
        contextComponents = JNDIUtil.calculateDNComponents(  test3Context.getNameInNamespace() );
        assertTrue(contextComponents.length==4);
    }
    
    public void testGetParentContext() throws NamingException
    {
        DirContext parentContext = 
            JNDIUtil.
            getParentContext( 
                test3Context, 
                dasPartitionContext );
        
        assertTrue(
            parentContext.getNameInNamespace().
            equals("cn=test2,cn=test1,ou=das"));
        
        parentContext = 
            JNDIUtil.getParentContext( 
                test1Context, 
                dasPartitionContext );
        
        assertTrue(
            parentContext.getNameInNamespace().
            equals("ou=das"));

        parentContext = 
            JNDIUtil.
            getParentContext( 
                dasPartitionContext, 
                dasPartitionContext );
        
        assertTrue(null == parentContext);
    }
}
