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
package org.apache.tuscany.das.ldap.emf.update.test;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.tuscany.das.ldap.emf.read.EDataGraphReader;
import org.apache.tuscany.das.ldap.emf.update.EDataGraphUpdater;
import org.apache.tuscany.das.ldap.prototype.setup.test.EDataGraphSetupTest;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.sdo.EChangeSummary;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class EDataGraphUpdateTest 
extends EDataGraphSetupTest
{
    public void testUpdate() throws NamingException
    {
    	dataObjectToRelativeDNCache          =
    		new Hashtable<EDataObject, String>();

    	eDataGraph      =
    		EDataGraphReader.read(
				userEClass,  
				"1", 
				rootContext,
				dataObjectToRelativeDNCache);
    	
    	assertTrue(
    			dataObjectToRelativeDNCache.
    			containsValue(authorizationDataObject1RDN));
    	assertTrue(
    			dataObjectToRelativeDNCache.
    			containsValue(authorizationDataObject2RDN));
    		
    	userDataObject          =
    		(EDataObject) eDataGraph.getRootObject();

    	String userEntryRDN = dataObjectToRelativeDNCache.get(userDataObject);
    	Attributes attributes = rootContext.getAttributes(userEntryRDN);
    	
    	assertEquals(userDataObject.eGet(userNameEAttribute), "ole");
    	assertEquals(userDataObject.eGet(userPasswordEAttribute), "secret");
    	assertEquals(userDataObject.eGet(userAgeEAttribute), 33);
    	assertEquals(userDataObject.eGet(userHeightEAttribute), 6.11);
    	assertEquals(userDataObject.eGet(userIsMaleEAttribute), true);
    	assertEquals("2", attributes.get("example-com-users-accounts-User-authorizationList").get(0));
    	assertEquals("3", attributes.get("example-com-users-accounts-User-authorizationList").get(1));

    	EChangeSummary eChangeSummary = 
    		(EChangeSummary) eDataGraph.getChangeSummary();
    	    	
    	eChangeSummary.beginLogging();

    	userDataObject.eSet(userIsMaleEAttribute, false);
    	userDataObject.eSet(userHeightEAttribute, 2.00);
    	userDataObject.eSet(userAgeEAttribute, 80);
    	userDataObject.eSet(userPasswordEAttribute, "totallysecret");
    	
    	EList<EDataObject> userAuthorizationReferenceList = 
    		(EList)userDataObject
    		.eGet(userAuthorizationEReference);
    	
    	userAuthorizationReferenceList.remove(0);

    	assertEquals(userDataObject.eGet(userIsMaleEAttribute), false);     	
    	assertEquals(userDataObject.eGet(userHeightEAttribute), 2.00);     	
    	assertEquals(userDataObject.eGet(userAgeEAttribute), 80);     	
    	assertEquals(userDataObject.eGet(userPasswordEAttribute), "totallysecret");     	
    	
    	eChangeSummary.endLogging();
    	
    	EDataGraphUpdater.update(
    			eDataGraph,
    			rootContext,
    			dataObjectToRelativeDNCache); 

    	attributes = rootContext.getAttributes(userEntryRDN);
    	
    	assertEquals("FALSE", attributes.get("example-com-users-accounts-User-isMale").get());
    	assertEquals("2.0", attributes.get("example-com-users-accounts-User-userHeight").get());
    	assertEquals("80", attributes.get("example-com-users-accounts-User-userAge").get());
    	assertEquals("totallysecret", attributes.get("example-com-users-accounts-User-userPassword").get());
    	assertEquals("5", attributes.get("example-com-users-accounts-User-configuration").get());
    	
    	/*
    	 * Test that the ID of the Authorization instance with id 2 was removed
    	 */
    	assertEquals(
    			"3",
    			attributes.
    			get("example-com-users-accounts-User-authorizationList").
    			get(0));
    	
    	/*
    	 * Also make sure that the dataObjectToRelativeDNCache was updated
    	 */
    	assertFalse(
    			dataObjectToRelativeDNCache.
    			containsValue(authorizationDataObject1RDN));
    	
    	eChangeSummary.beginLogging();
    	
    	userDataObject.eUnset(userConfigurationEReference);
    	userAuthorizationReferenceList.remove(0);
    	
    	eChangeSummary.endLogging();
    	
    	EDataGraphUpdater.update(
    			eDataGraph,
    			rootContext,
    			dataObjectToRelativeDNCache);

    	attributes = rootContext.getAttributes(userEntryRDN);

    	boolean isConfigurationAttributeDeleted = false;
    	
    	try {
    		attributes.get("example-com-users-accounts-User-configuration").get();
    	}
    	catch (Exception e)
    	{
    		isConfigurationAttributeDeleted = true;
    	}
    	assertTrue(isConfigurationAttributeDeleted);
    	
    	boolean isAuthorizationlListAttributeDeleted = false;
    	
    	try {
    		attributes.get("example-com-users-accounts-User-authorizationList").get();
    	}
    	catch (Exception e)
    	{
    		isAuthorizationlListAttributeDeleted = true;
    	}
    	assertTrue(isAuthorizationlListAttributeDeleted);
    	assertFalse(
    			dataObjectToRelativeDNCache.
    			containsValue(authorizationDataObject2RDN));
    	
    	eChangeSummary.beginLogging();
    	
    	userAuthorizationReferenceList.add(authorizationDataObject0);
    	userAuthorizationReferenceList.add(authorizationDataObject1);

    	eChangeSummary.endLogging();
    	
    	EDataGraphUpdater.update(
    			eDataGraph,
    			rootContext,
    			dataObjectToRelativeDNCache);
    	
    	assertTrue(
    			dataObjectToRelativeDNCache.
    			containsValue(authorizationDataObject1RDN));
    	assertTrue(
    			dataObjectToRelativeDNCache.
    			containsValue(authorizationDataObject2RDN));
  }
}