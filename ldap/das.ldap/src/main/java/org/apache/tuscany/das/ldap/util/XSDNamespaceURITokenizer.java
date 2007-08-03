
package org.apache.tuscany.das.ldap.util;

import org.eclipse.emf.common.util.URI;

public class XSDNamespaceURITokenizer
{
    
    public static String[] createAuthorityTokens( URI xsdNamespaceURI )
    {
        String authority = xsdNamespaceURI.authority();
        String[] authorityTokens = authority.split( "[.]" );
        return authorityTokens;
    }
    
    public static String[] createPathTokens(URI xsdNamespace)
    {
        String path              = xsdNamespace.path(); 
        if (path != null)
        {
            return path.split("[/]"); 
        }
        else
            return null;
    }
}
