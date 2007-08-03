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
package org.apache.tuscany.das.ldap.emf.read.test;

import java.util.Hashtable;

import javax.naming.NamingException;

import org.apache.tuscany.das.ldap.emf.read.EDataGraphReader;
import org.apache.tuscany.das.ldap.prototype.setup.test.EDataGraphSetupTest;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class EDataGraphReaderTest 
extends EDataGraphSetupTest
{
    public void testRead() throws NamingException
    {
    	dataObjectToRelativeDNCache         =
    		new Hashtable<EDataObject, String>();
    	
    	eDataGraph                          =
    		EDataGraphReader.read(
				userEClass,  
				"1", 
				rootContext,
				dataObjectToRelativeDNCache);
    	
    	EDataObject retrievedUserDataObject =
    		(EDataObject) eDataGraph.getRootObject();
    	
    	assertEquals(
    			dataObjectToRelativeDNCache.
    			get(retrievedUserDataObject).toString(), 
    			"example-com-users-accounts-User-id=1");

    	assertEquals(retrievedUserDataObject.eGet(userNameEAttribute), "ole");
    	assertEquals(retrievedUserDataObject.eGet(userPasswordEAttribute), "secret");
    	assertEquals(retrievedUserDataObject.eGet(userAgeEAttribute), 33);
    	assertEquals(retrievedUserDataObject.eGet(userHeightEAttribute), 6.11);
    	assertEquals(retrievedUserDataObject.eGet(userIsMaleEAttribute), true);
    	
    	EList<EDataObject> retrievedAuthorizationContainmentList =
    		(EList<EDataObject>) 
    		retrievedUserDataObject.
    		eGet(userAuthorizationEReference);
    	
    	assertEquals(retrievedAuthorizationContainmentList.size(), 2);
    	
    	EDataObject retrievedAuthorizationEDataObject1                     =
    		retrievedAuthorizationContainmentList.get(0);
    	
    	assertEquals(
    			dataObjectToRelativeDNCache.
    			get(retrievedAuthorizationEDataObject1).toString(), 
    			"example-com-users-accounts-Authorization-id=2,cn=authorizationList,example-com-users-accounts-User-id=1");

    	assertEquals(
    			retrievedAuthorizationEDataObject1.
    			eGet(authorizationIDEAttribute), "2");
    	assertEquals(
    			retrievedAuthorizationEDataObject1.
    			eGet(authorizationFileEAttribute), "somefile.text");
    	assertEquals(
    			retrievedAuthorizationEDataObject1.
    			eGet(authorizationWriteEAttribute), true);
    	assertEquals(
    			retrievedAuthorizationEDataObject1.
    			eGet(authorizationReadEAttribute), true);
    	assertEquals(
    			retrievedAuthorizationEDataObject1.
    			eGet(authorizationExecuteEAttribute), true);

    	EDataObject retrievedAuthorizationEDataObject2                     =
    		retrievedAuthorizationContainmentList.get(1);

    	assertEquals(
    			dataObjectToRelativeDNCache.
    			get(retrievedAuthorizationEDataObject2).toString(), 
    			"example-com-users-accounts-Authorization-id=3,cn=authorizationList,example-com-users-accounts-User-id=1");

    	assertEquals(
    			retrievedAuthorizationEDataObject2.
    			eGet(authorizationIDEAttribute), "3");
    	
    	EDataObject retrievedConfigurationEDataObject                   =
    		(EDataObject) retrievedUserDataObject.
    		eGet(userConfigurationEReference);

    	assertEquals(
    			dataObjectToRelativeDNCache.
    			get(retrievedConfigurationEDataObject).toString(), 
    			"example-com-users-accounts-Configuration-id=5,cn=configuration,example-com-users-accounts-User-id=1");
    	
    	assertEquals(
    			"5", 
    			retrievedConfigurationEDataObject.
    			eGet(configurationIDEAttribute));
    	
    	EDataObject referencedAuthorizationDataObject =
    		(EDataObject) retrievedConfigurationEDataObject.
    		eGet(configurationAuthorizationEReference);
    		
    	assertSame(
    			retrievedAuthorizationEDataObject1, 
    			referencedAuthorizationDataObject);
    	
    	EList<EDataObject> referenceAuthorizationsList =
    		(EList<EDataObject>) retrievedConfigurationEDataObject.
    		eGet(configurationAuthorizationsEReference);
    	
    	assertEquals(2, referenceAuthorizationsList.size() );
    }
    
    public void testReadException()
    {
        dataObjectToRelativeDNCache         =
            new Hashtable<EDataObject, String>();
        
        try 
        {
            eDataGraph                          =
                EDataGraphReader.read(
                    userEClass,  
                    "1", 
                    metaContext,
                    dataObjectToRelativeDNCache);
        }
        catch ( NamingException e )
        {
            readException = true;
        }
        
        assertTrue(readException);
    }
    
    
    
}