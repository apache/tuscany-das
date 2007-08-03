
package org.apache.tuscany.das.ldap.util.test;

import org.apache.tuscany.das.ldap.schema.embedded.setup.test.AbstractTestSetup;

import org.apache.tuscany.das.ldap.util.XSDNamespaceURITokenizer;
import org.eclipse.emf.common.util.URI;

public class XSDNamespaceURITokenizerTest 
extends AbstractTestSetup
{
    public void testCreateAuthorityTokens()
    {
        String[] authorityTokens =
            XSDNamespaceURITokenizer.
            createAuthorityTokens(
            		URI.createURI(xsdNamespace));
        
        assertTrue(authorityTokens[0].equals( "example"));
        assertTrue(authorityTokens[1].equals( "com"));
        assertTrue(authorityTokens.length == 2);
    }

    public void testCreatePathTokens()
    {
        String[] pathTokens =  
        	XSDNamespaceURITokenizer.
        	createPathTokens(
        			URI.createURI(xsdNamespace));
        
        assertTrue(pathTokens[1].equals("users"));
        assertTrue(pathTokens[2].equals("accounts"));
        assertTrue(pathTokens.length == 3);
    }

}
