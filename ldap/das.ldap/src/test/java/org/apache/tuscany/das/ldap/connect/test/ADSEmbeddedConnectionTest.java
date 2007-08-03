package org.apache.tuscany.das.ldap.connect.test;

import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.connect.ADSEmbeddedConnection;
import org.apache.tuscany.model.Configuration;
import org.apache.tuscany.model.ConfigurationFactory;

import junit.framework.TestCase;

public class ADSEmbeddedConnectionTest extends TestCase {
	
	public void testConnect() throws Exception
	{
		Configuration configuration                                         = 
			ConfigurationFactory.
			INSTANCE.
			createConfiguration();
		
		ADSEmbeddedConnection adsEmbeddedConnection = 
			new ADSEmbeddedConnection(configuration);
		
		LdapContext dasContext                                             = 
			adsEmbeddedConnection.
			connect(
					configuration.
					getDasPartitionName());
		
		assertEquals(
				dasContext.getNameInNamespace(),
				"ou=das");
		
		LdapContext schemaContext                                             = 
			adsEmbeddedConnection.
			connect(
					configuration.
					getSchemaPartitionName());
		
		assertEquals(
				schemaContext.getNameInNamespace(),
				"ou=schema");
		
		configuration.setDasPartitionName("system");
		
		LdapContext systemContext = 
			adsEmbeddedConnection.connect(configuration.getDasPartitionName());

		assertEquals(
				systemContext.getNameInNamespace(),
				"ou=system");
		
		adsEmbeddedConnection.shutdown();
	}
}
