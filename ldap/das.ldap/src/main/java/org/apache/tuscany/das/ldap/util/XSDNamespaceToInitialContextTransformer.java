
package org.apache.tuscany.das.ldap.util;

import org.eclipse.emf.common.util.URI;

/**
 * The Class XSDNamespaceToInitialContextTransformer.
 * 
 * Takes the XSD Namespace used by the DataObjects
 * and transforms it into the DN of the initial context
 * used for the root DataObject entry.
 */
public class XSDNamespaceToInitialContextTransformer
{
    /**
     * Transform.
     * 
     * @param namespaceURIString the namespace URI string
     * 
     * @return the DN of the initial context
     * 
     * @throws Exception the exception
     */
    public static String transform(String namespaceURIString) throws Exception
    {
        URI namespaceURI = URI.createURI(namespaceURIString);
        String authority = namespaceURI.authority();

        String path      = namespaceURI.path();

        String[] authorityTokens = authority.split( "[.]" );
        String[] pathTokens      = path.split("[/]");
        
        String DN = new String("");
        
        for (int i = (pathTokens.length-1); i > 0; i--)
        {
            DN = DN + "cn=" + pathTokens[i] + ", ";
        }

        for (int i = 0; i <= (authorityTokens.length-2); i++)
        {
            DN = DN + "cn=" + authorityTokens[i] + ", ";
        }
        
        DN = DN + "ou=" + authorityTokens[authorityTokens.length-1];
        return DN;
    }
}
