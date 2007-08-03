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
package org.apache.tuscany.das.ldap.emf.create;

import java.util.Map;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class EDataGraphCreatorHelper
implements AttributeTypeConstants
{
	/*
	 * TODO Make mutlithreaded
	 */
	public static void createChildren(
			EList<EObject> children,
			LdapContext rootContext,
			Map<EDataObject, String> dataObjectToRelativeDNCache) 
	throws NamingException
	{
		LdapContext eContainmentFeatureContext = 
			null;

		for (EObject eObject : children)
		{
			String parentContextRDN                            =
				dataObjectToRelativeDNCache.get(eObject.eContainer());

			LdapContext parentContext                        =
				(LdapContext) 
				rootContext.
				lookup(parentContextRDN);

			EStructuralFeature eContainmentFeature  =
				eObject.eContainingFeature();
				
			String eContainingFeatureContextName    =
				CN + "="+ eContainmentFeature.getName();
			
			try 
			{
				eContainmentFeatureContext                     = 
					(LdapContext) 
					parentContext.lookup(
							eContainingFeatureContextName);
			}
			catch (Exception e)
			{
				eContainmentFeatureContext                     = 
					(LdapContext) 
					parentContext.
					createSubcontext(
							eContainingFeatureContextName);
			}

			EDataObjectCreator.create((
				EDataObject) eObject, 
				eContainmentFeatureContext,
				dataObjectToRelativeDNCache);
			
			children                                                          = 
				eObject.eContents();
			
			if ( children.size()> 0)
			{
				createChildren(
						children,
						rootContext,
						dataObjectToRelativeDNCache);
			}
		}
	}
}