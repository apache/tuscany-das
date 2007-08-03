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
package org.apache.tuscany.das.ldap.emf.create.test;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.prototype.setup.test.EDataGraphSetupTest;


public class EDataGraphCreatorTest 
extends EDataGraphSetupTest
{
    public void testCreate() throws NamingException
    {
    	assertEquals(
    			"example-com-users-accounts-User-id=1,cn=accounts,cn=users,cn=example,cn=com,ou=das",
    			userContainerContext.getNameInNamespace());
    	
    	Attributes userEntryAttributes = 
    		userContainerContext.getAttributes("");
    	
    	assertEquals(
    			userEntryAttributes.get(
    					"example-com-users-accounts-User-userPassword").get(),
    					"secret");

    	assertEquals(
    			userEntryAttributes.get(
    					"example-com-users-accounts-User-authorizationList").get(0), "2");

    	assertEquals(
    			userEntryAttributes.get(
    					"example-com-users-accounts-User-authorizationList").get(1), "3");
    	
    	assertEquals(
    			userEntryAttributes.get("example-com-users-accounts-User-userName").get(),
    			"ole");
    	
    	assertEquals(
    			"cn=authorizationList,example-com-users-accounts-User-id=1,cn=accounts,cn=users,cn=example,cn=com,ou=das",
    			authorizationContainerContext.getNameInNamespace());
    	
    	LdapContext authorizationEntry1Context           =
    		(LdapContext) 
    		rootContext.
    		lookup(authorizationDataObject1RDN);

    	assertEquals(
    			"example-com-users-accounts-Authorization-id=2,cn=authorizationList,example-com-users-accounts-User-id=1,cn=accounts,cn=users,cn=example,cn=com,ou=das",
    			authorizationEntry1Context.getNameInNamespace());

    	Attributes authorizationEntry1Attributes            =
    		rootContext.
    		getAttributes(authorizationDataObject1RDN);
    	
    	assertEquals(
    			authorizationEntry1Attributes.get("example-com-users-accounts-Authorization-fileName").get(),
    			"somefile.text");
    	
    	assertEquals(
    			authorizationEntry1Attributes.get("example-com-users-accounts-Authorization-writeAuthorization").get(),
    			"TRUE");

    	
    	LdapContext authorizationEntry2Context           =
    		(LdapContext) 
    		rootContext.
    		lookup(authorizationDataObject2RDN);

    	assertEquals(
    			"example-com-users-accounts-Authorization-id=3,cn=authorizationList,example-com-users-accounts-User-id=1,cn=accounts,cn=users,cn=example,cn=com,ou=das",
    			authorizationEntry2Context.getNameInNamespace());
    	
    	
    	Attributes authorizationEntry2Attributes            =
    		rootContext.
    		getAttributes(authorizationDataObject2RDN);
    	
    	assertEquals(
    			authorizationEntry2Attributes.get("example-com-users-accounts-Authorization-fileName").get(),
    			"someOtherfile.text");
    	
    	assertEquals(
    			"cn=configuration,example-com-users-accounts-User-id=1,cn=accounts,cn=users,cn=example,cn=com,ou=das",
    			configurationContainerContext.getNameInNamespace());

    	String configurationEntryRDN                            =
    		"example-com-users-accounts-Configuration-id=5";
    	
    	LdapContext configurationEntryContext           =
    		(LdapContext) 
    		configurationContainerContext.
    		lookup(configurationEntryRDN);

    	assertEquals(
    			"example-com-users-accounts-Configuration-id=5,cn=configuration,example-com-users-accounts-User-id=1,cn=accounts,cn=users,cn=example,cn=com,ou=das",
    			configurationEntryContext.getNameInNamespace());
    	
    	Attributes configurationEntryAttributes            =
    		configurationContainerContext.
    		getAttributes(configurationEntryRDN);

    	assertEquals(
    			configurationEntryAttributes.get("example-com-users-accounts-Configuration-authorization").get(),
    			"2");
    }
}