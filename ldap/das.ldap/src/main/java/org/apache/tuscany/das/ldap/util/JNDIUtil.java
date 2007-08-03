
package org.apache.tuscany.das.ldap.util;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

/**
 * The Class JNDIUtil.
 */
public class JNDIUtil
{
    /**
     * Calculate DN components.
     * 
     * @param context the context
     * 
     * @return the string[] containing the components of the DN
     * 
     * @throws NamingException the naming exception
     */
    public static String[] calculateDNComponents(
        DirContext context) 
    throws NamingException
    {
        String contextDN                  = 
            context.getNameInNamespace();
        
        String[] initialContextComponents = 
            contextDN.split( "[,]" );

        return initialContextComponents;
    }

    
    /**
     * Calculate DN components.
     * 
     * @param contextDN the context DN
     * 
     * @return the string[] containing the components of the DN
     * 
     * @throws NamingException the naming exception
     */
    public static String[] calculateDNComponents(
        String contextDN) 
    throws NamingException
    {
        String[] initialContextComponents = 
            contextDN.split( "[,]" );

        return initialContextComponents;
    }
    
    /**
     * Gets the parent context.
     * 
     * @param partitionContext the partition context
     * @param childContext the child context
     * 
     * @return the parent context (Null if the child context is the root)
     * 
     * @throws NamingException the naming exception
     */
    public static DirContext getParentContext(
        DirContext childContext, 
        DirContext partitionContext) 
    throws NamingException
    {
        String[] childContextDNComponents = 
            calculateDNComponents( childContext );
        
        String[] partitionDNComponents    = 
            calculateDNComponents( partitionContext );
        
        DirContext parentContext          = null; 
        
        if (childContextDNComponents.length == partitionDNComponents.length)
        {
            return null;
        }
        else if ( (childContextDNComponents.length - 1) == partitionDNComponents.length)
        {
            return partitionContext;
        }
        else
        {
            String parentLookupDN = 
                calculateParentRelativeDN( 
                    childContext, partitionContext.getNameInNamespace() );
            
            
            
            parentContext = 
                ( DirContext ) partitionContext.lookup( parentLookupDN );
        }
        return parentContext;
    }

    
    /**
     * Calculate parent relative DN.
     * The parent relative DN is the
     * DN of the parent context, relative
     * to the partition context.
     * 
     * So if the partition context is
     * <i>ou=system</i> and
     * the child context has DN
     * <i>cn=accounts, cn=users, cn=example, ou=system</i>
     * 
     * then the parent relative DN is
     * <i>cn=users, cn=example</i>
     * 
     * @param childContext the child context
     * @param partitionDN the partition DN
     * 
     * @return the string
     * 
     * @throws NamingException the naming exception
     */
    public static String calculateParentRelativeDN(
        DirContext childContext, 
        String partitionDN) 
    throws NamingException
    {
        String parentRelativeDN           = null;
        
        String[] childContextDNComponents = 
            calculateDNComponents( childContext );
        
        String[] partitionDNComponents    = 
            calculateDNComponents( partitionDN );
        
        if (childContextDNComponents.length == 
            partitionDNComponents.length)
        {
            return null;
        }
        else if (childContextDNComponents.length ==
             partitionDNComponents.length + 1)
        {
            String childRDN            = childContextDNComponents[0];
            String childContextDN      = childContext.getNameInNamespace();
            
            int beginIndex = childRDN.length() + 1;
            int endIndex   = childContextDN.length();

            parentRelativeDN = childContextDN.substring( 
                beginIndex, endIndex );
            
            return parentRelativeDN;
        }
        else
        {
            String childRDN            = childContextDNComponents[0];
            String childContextDN      = childContext.getNameInNamespace();
            
            int beginIndex = childRDN.length() + 1;
            int endIndex   = 
                childContextDN.length() - 
                ( partitionDN.length() +1);
            
            parentRelativeDN = childContextDN.substring( 
                beginIndex, endIndex );
        }
        return parentRelativeDN;
    }
}
