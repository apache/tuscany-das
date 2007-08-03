/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.tuscany.das.ldap.connect;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.directory.server.core.configuration.MutablePartitionConfiguration;
import org.apache.directory.server.core.configuration.MutableStartupConfiguration;
import org.apache.directory.server.core.configuration.ShutdownConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.apache.tuscany.model.Configuration;

/**
 * The Class JNDIEmbeddedConnectionTemplate.
 */
public class ADSEmbeddedConnection extends JNDIConnection {
	private Configuration configuration = null;

	public ADSEmbeddedConnection(Configuration configuration) {
		super(configuration);
		this.configuration = configuration;
	}

	/**
	 * Shutdown.
	 * 
	 * Shuts down an embedded instance of ADS.
	 * 
	 * @throws NamingException
	 *             the naming exception
	 * @throws Exception
	 *             the exception
	 */
	public void shutdown() throws NamingException, Exception {
		Hashtable<String, Object> env = new Hashtable<String, Object>();

		env = createEnvironment(env);

		env.put(
				Context.PROVIDER_URL, 
				SYSTEM_PROVIDER_PATH);
		env.put(
				Context.INITIAL_CONTEXT_FACTORY, 
				APACHE_CONTEXT_FACTORY);
		env.putAll(
				new ShutdownConfiguration().toJndiEnvironment());

		new InitialContext(env);
		Runtime.getRuntime().gc();
	}

	/**
	 * Startup.
	 * 
	 * Starts an embedded instance of ADS and creates the the DAS partition, if
	 * it does not already exist.
	 * 
	 * @throws NamingException
	 *             the naming exception
	 */
	public LdapContext startup(String partitionName) 
	throws NamingException {
    	PropertyConfigurator.
    	configure(
    			configuration.
    			getNlog4jConfigurationFilePath());

		MutableStartupConfiguration mutableStartupConfiguration = 
			new MutableStartupConfiguration();

		Hashtable<String, Object> adminEnv = 
			new Hashtable<String, Object>();

		adminEnv.putAll(mutableStartupConfiguration.toJndiEnvironment());

		adminEnv.put(
				Context.PROVIDER_URL, 
				SYSTEM_PROVIDER_PATH);
		adminEnv.put(
				Context.SECURITY_PRINCIPAL,
				configuration.getSecurityPrincipal());
		adminEnv.put(
				Context.SECURITY_CREDENTIALS, 
				configuration.getSecurityCredentials());
		adminEnv.put(
				Context.SECURITY_AUTHENTICATION, 
				configuration.getSecurityAuthenticationType());
		new InitialLdapContext(adminEnv, null);

		MutablePartitionConfiguration dasPartition = 
			new MutablePartitionConfiguration();

		Attributes suffixAttributes                            = 
			new BasicAttributes();
		suffixAttributes.put(OBJECT_CLASS, TOP);
		suffixAttributes.get(OBJECT_CLASS).add(ORGANIZATIONAL_UNIT);
		suffixAttributes.put(OU, partitionName);

		dasPartition.setId(partitionName);
		dasPartition.setSuffix(getProviderPath(partitionName));
		dasPartition.setContextEntry(suffixAttributes);

		Set<MutablePartitionConfiguration> partitions = 
			new HashSet<MutablePartitionConfiguration>();

		partitions.add(dasPartition);

		mutableStartupConfiguration.setPartitionConfigurations(partitions);

		Hashtable<String, Object> env                           = 
			new Hashtable<String, Object>();

		env.put(
				Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.directory.server.core.jndi.CoreContextFactory");
		env.put(
				Context.SECURITY_PRINCIPAL, 
				DEFAULT_SECURITY_PRINCIPAL_VALUE);
		env.put(
				Context.SECURITY_CREDENTIALS,
				DEFAULT_SECURITY_CREDENTIALS_VALUE);
		env.put(
				Context.SECURITY_AUTHENTICATION,
				SIMPLE_SECURITY_AUTHENTICATION_VALUE);
		env.put(
				Context.PROVIDER_URL, getProviderPath(partitionName));

		env.put(
				org.apache.directory.server.core.configuration.Configuration.JNDI_KEY,
				mutableStartupConfiguration);

		return new InitialLdapContext(env, null);
	}
	
	public LdapContext connect(String partitionName) throws NamingException
    {
        return startup(partitionName);
    }
    
	/**
	 * Gets the provider path. TODO Move to helper
	 * 
	 * @return the provider path
	 */
	public String getProviderPath(String partitionName) {
		return OU + "=" + partitionName;
	}
}