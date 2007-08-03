package org.apache.tuscany.das.ldap.learning.test;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.schema.embedded.setup.test.AbstractTestSetup;

public class LearningTest extends AbstractTestSetup {
	
	public void testSearch() throws NamingException
	{
		LdapContext contextL0 = 
			(LdapContext) 
			dasPartitionContext.createSubcontext("cn=L0");
		
		contextL0.createSubcontext("cn=L1");
		
		Attributes searchAttributes = 
			new BasicAttributes(true); 
		
		searchAttributes.put(new BasicAttribute("cn"));
		
		NamingEnumeration answer = contextL0.search("", searchAttributes);
		while (answer.hasMore()) {
			
		    SearchResult result = (SearchResult)answer.next();
		    Attributes attributes = result.getAttributes();
		    assertEquals("L1", attributes.get("cn").get().toString());
		}
		dasPartitionContext.destroySubcontext("cn=L1, cn=L0");
		dasPartitionContext.destroySubcontext("cn=L0");
	}
}
