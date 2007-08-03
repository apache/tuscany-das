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
package org.apache.tuscany.das.ldap.prototype.setup.test;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.tuscany.das.ldap.prototype.setup.Prototype;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.sdo.EDataGraph;
import org.eclipse.emf.ecore.sdo.EDataObject;
import org.eclipse.emf.ecore.sdo.SDOFactory;

public class LdapDASSetupTest 
extends TestCase
implements Prototype
{
    protected Map<EDataObject, String> dataObjectToRelativeDNCache   = null;
    protected EDataGraph eDataGraph                       = null;
    protected EDataObject userDataObject                  = null;
    protected EDataObject authorizationDataObject0        = null;
    protected EDataObject authorizationDataObject1        = null;
    protected EDataObject configurationDataObject         = null;

	public void tearDown() throws NamingException, Exception
	{
        FileUtils.deleteDirectory(new File("server-work"));
	}
	
	public void setUp() throws Exception
	{
		dataObjectToRelativeDNCache          =
		    new Hashtable<EDataObject, String>();
		
        userAuthorizationEReference.
        setEType(authorizationEClass);
        
        userConfigurationEReference.
        setEType(configurationEClass);
        
        configurationAuthorizationEReference.
        setEType(authorizationEClass);

        configurationAuthorizationsEReference.
        setEType(authorizationEClass);

        userEClass.
        getEStructuralFeatures().
        add( userNameEAttribute );
        
        userEClass.
        getEStructuralFeatures().
        add( userAliasesEAttribute );
        
        userEClass.
        getEStructuralFeatures().
        add( userPasswordEAttribute );
        userEClass.
        getEStructuralFeatures().
        add( userAgeEAttribute );
        userEClass.
        getEStructuralFeatures().
        add( userHeightEAttribute );
        userEClass.
        getEStructuralFeatures().
        add( userIsMaleEAttribute );
        userEClass.
        getEStructuralFeatures().
        add( userIDEAttribute );
        userEClass.
        getEStructuralFeatures().
        add( userAuthorizationEReference );
        userEClass.
        getEStructuralFeatures().
        add( userConfigurationEReference );

        userEPackage.getEClassifiers().add(userEClass);
        
        userDataObject =
            (EDataObject) 
            userEFactory.create(userEClass);
        
        userDataObject.eSet(userNameEAttribute,"ole");
        userDataObject.eSet(userPasswordEAttribute,"secret");
        userDataObject.eSet(userIDEAttribute, "1");
        userDataObject.eSet(userAgeEAttribute, 33);
        userDataObject.eSet(userHeightEAttribute, 6.11);
        userDataObject.eSet(userIsMaleEAttribute, true);
        
        EList<String> userAliases = new BasicEList<String>();
        userAliases.add("neo");
        userAliases.add("trinity");
        userAliases.add("morpheus");
        
        userDataObject.eSet( userAliasesEAttribute, userAliases );

        authorizationEClass.
        getEStructuralFeatures().
        add(authorizationFileEAttribute);

        authorizationEClass.
        getEStructuralFeatures().
        add(authorizationWriteEAttribute);

        authorizationEClass.
        getEStructuralFeatures().
        add(authorizationReadEAttribute);
        
        authorizationEClass.
        getEStructuralFeatures().
        add(authorizationExecuteEAttribute);

        authorizationEClass.
        getEStructuralFeatures().
        add(authorizationIDEAttribute);
        
        userEPackage.getEClassifiers().
        add(authorizationEClass);
                
        authorizationDataObject0  =
            (EDataObject) 
            userEFactory.create(authorizationEClass);
        
        authorizationDataObject1  =
            (EDataObject) 
            userEFactory.create(authorizationEClass);
        
        authorizationDataObject0.eSet(authorizationFileEAttribute,"somefile.text");
        authorizationDataObject0.eSet(authorizationWriteEAttribute, true);
        authorizationDataObject0.eSet(authorizationReadEAttribute,true);
        authorizationDataObject0.eSet(authorizationExecuteEAttribute,true);
        authorizationDataObject0.eSet(authorizationIDEAttribute, "2");

        authorizationDataObject1.eSet(authorizationFileEAttribute,"someOtherfile.text");
        authorizationDataObject1.eSet(authorizationWriteEAttribute,Boolean.TRUE);
        authorizationDataObject1.eSet(authorizationReadEAttribute,true);
        authorizationDataObject1.eSet(authorizationExecuteEAttribute,true);
        authorizationDataObject1.eSet(authorizationIDEAttribute, "3");
        
        EList<EDataObject> userDataObjectAuthorizationList =
            new BasicEList<EDataObject>();
        
        userDataObjectAuthorizationList.add(authorizationDataObject0);
        userDataObjectAuthorizationList.add(authorizationDataObject1);
        
        userDataObject.eSet(
                userAuthorizationEReference, 
                userDataObjectAuthorizationList);
        
        configurationEClass.
        getEStructuralFeatures().
        add(configurationIDEAttribute);
        
        configurationEClass.
        getEStructuralFeatures().
        add(configurationAuthorizationEReference);
        
        configurationEClass.
        getEStructuralFeatures().
        add(configurationAuthorizationsEReference);
        
        userEPackage.getEClassifiers().
        add(configurationEClass);
        
        configurationDataObject  =
            (EDataObject) 
            userEFactory.create(configurationEClass);
        
        userDataObject.eSet(
                userConfigurationEReference, 
                configurationDataObject);
        
        configurationDataObject.eSet(
                configurationIDEAttribute, 
                "5");

        configurationDataObject.eSet(
                configurationAuthorizationEReference, 
                authorizationDataObject0);
        
        configurationDataObject.eSet(
                configurationAuthorizationsEReference, 
                userDataObjectAuthorizationList);
        

        eDataGraph             =
            SDOFactory.
            eINSTANCE.
            createEDataGraph();
                
        eDataGraph.setERootObject(userDataObject);

	}
}