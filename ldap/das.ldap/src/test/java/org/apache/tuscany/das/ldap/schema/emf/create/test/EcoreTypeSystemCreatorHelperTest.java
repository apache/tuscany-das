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
 */
package org.apache.tuscany.das.ldap.schema.emf.create.test;

import java.util.Stack;

import org.apache.tuscany.das.ldap.schema.emf.create.EcoreTypeSystemHelper;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;

import junit.framework.TestCase;

public class EcoreTypeSystemCreatorHelperTest 
extends TestCase
{
    EcoreFactory ecoreFactory = 
        EcoreFactory.eINSTANCE;
    
    public void testLoadParentEClassifierStack()
    {
        EClass eClassL0             = 
            ecoreFactory.createEClass();

        EClass eClassL1             = 
            ecoreFactory.createEClass();

        EClass eClassL2             = 
            ecoreFactory.createEClass();
        
        eClassL2.getESuperTypes().add(eClassL1);
        
        eClassL1.getESuperTypes().add(eClassL0);
        
        Stack<EClass> testStack = 
            EcoreTypeSystemHelper.
            loadParentEClassifierStack(
                eClassL2, 
                null);
        
        assertEquals(testStack.size(), 2);
    }
}
