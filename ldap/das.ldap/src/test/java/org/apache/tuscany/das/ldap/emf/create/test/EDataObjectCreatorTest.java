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
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.prototype.setup.test.EDataObjectSetupTest;

public class EDataObjectCreatorTest 
extends EDataObjectSetupTest
{
	protected String eDataObjectRDN              = null;
	protected LdapContext namedContainerContext  = null;
	
	public void tearDown() throws NamingException, Exception
	{
        super.tearDown();
	}
	
	public void setUp() throws Exception
	{
        super.setUp();
    }
	
    public void testCreate() throws NamingException
    {
    	assertEquals(
    			"example-com-users-accounts-User-id=1,cn=accounts,cn=users,cn=example,cn=com,ou=das",
    			userContainerContext.getNameInNamespace());
    	
    	Attributes attributes = 
    		userContainerContext.getAttributes("");
    	
    	assertEquals(
    			attributes.get("example-com-users-accounts-User-userPassword").get(),
    			"secret");
    	
    	assertEquals(
    			attributes.get("example-com-users-accounts-User-userName").get(),
    			"ole");
    	assertEquals(
    			attributes.get("example-com-users-accounts-User-userPassword").get(),
    			"secret");
    	assertEquals(
    			attributes.get("example-com-users-accounts-User-userAge").get(),
    			"33");
    	assertEquals(
    			attributes.get("example-com-users-accounts-User-userHeight").get(),
    			"6.11");
    
    	assertEquals(
    			attributes.get("example-com-users-accounts-User-authorizationList").size(),
    			2);
    	
    	Attribute aliasesAttribute = attributes.get("example-com-users-accounts-User-userAliases");
    	
    	assertEquals(
    	    aliasesAttribute.size(),
            3);
    	
    	assertEquals(aliasesAttribute.get(0), "neo");
        assertEquals(aliasesAttribute.get(1), "trinity");
        assertEquals(aliasesAttribute.get(2), "morpheus");
    }
}