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
package org.apache.tuscany.das.ldap.emf.update;

import java.util.Map;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.eclipse.emf.ecore.sdo.EChangeSummary;
import org.eclipse.emf.ecore.sdo.EDataGraph;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class EDataGraphUpdater
implements DASConstants
{
    public static void update(
    		EDataGraph eDataGraph,
    		LdapContext rootContext,
    		Map<EDataObject, String> dataObjectToRelativeDNCache ) 
    throws NamingException
    {
     	EChangeSummary eChangeSummary =
    		(EChangeSummary) 
    		eDataGraph.getChangeSummary();

    	EDataGraphUpdaterHelper.
    	processChangedDataObjects(
    			eChangeSummary, 
    			rootContext, 
    			dataObjectToRelativeDNCache);
    	
    	EDataGraphUpdaterHelper.
    	processCreatedDataObjects(
    			eChangeSummary, 
    			rootContext, 
    			dataObjectToRelativeDNCache);
    	
    	EDataGraphUpdaterHelper.
    	processDestroyedDataObjects(
    			eChangeSummary, 
    			rootContext, 
    			dataObjectToRelativeDNCache);
    }
}