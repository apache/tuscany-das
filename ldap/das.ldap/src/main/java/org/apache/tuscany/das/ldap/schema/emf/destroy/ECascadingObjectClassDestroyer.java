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
package org.apache.tuscany.das.ldap.schema.emf.destroy;

import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.eclipse.emf.ecore.EClass;

/**
 * The Class ECascadingObjectClassDestroyer.
 * 
 * The purose of this class is to destroy a ObjectClass
 * that is the parent of other ObjectClass entries.
 * 
 * It first checks to see whether the EClass instance
 * is a parent of other EClassifiers by examining 
 * whether the metaContext contains the EClass 
 * instance's name.
 * 
 * If the EClass instance has an an metaContext child entry,
 * then this instance is a parent of other EClassifiers.  
 * Therefore we check to see whether there are EClass
 * instances left to delete that inherit from this EClass.
 * 
 * If none are left we delete the parentMetaContext entry.
 * If there are more children left, we leave the parentMetaContext
 * entry in place and delete the children.
 * 
 * If there is no parentMetaEntryContext, it means that this
 * EClass instance does not have any children.  Therefore it's
 * safe to delete the corresponding ObjectClass.
 */
public class ECascadingObjectClassDestroyer
implements AttributeTypeConstants
{
	
	
    public static void destroy(
        Map<String, EClass> eClassNameToEClassMap,
        DirContext metaContext,
        DirContext attributeTypesContext,
        DirContext objectClassesContext,
        EClass eClass,
        String oidPrefix)
    throws NamingException, Exception
    {
        String rdn                         =
            null;
        DirContext eClassParentMetaContext =
            null;
        try
        {
            eClassParentMetaContext        = 
                ( DirContext ) 
                metaContext.
                lookup( CN + "=" + eClass.getName() );
        }
        catch (Exception e)
        {
            //This eClass can be deleted, because it is not used a super class.
        }

        if (eClassParentMetaContext == null)
        {
            EObjectClassDestroyer.destroy(
                metaContext,
                attributeTypesContext, 
                objectClassesContext, 
                eClass, 
                oidPrefix );
        }
        else
        {
    		Attributes searchAttributes = 
    			new BasicAttributes(true); 
    		
    		searchAttributes.put(new BasicAttribute(CN));
    		
            NamingEnumeration<SearchResult> childEntries =
            	eClassParentMetaContext.search("", searchAttributes);
            
            if (childEntries.hasMore())
            {
                while (childEntries.hasMore()) 
                {
                	SearchResult childEntry                   =
                		childEntries.next();
                
	                Attributes childEntryAttributes      = 
	                	childEntry.getAttributes();
	                
	                String eClassName                           =
	                    ( String ) 
	                    childEntryAttributes.get( CN ).get();
	              
	                EClass childEClass                           =
	                    eClassNameToEClassMap.get( eClassName );
	                
	                destroy(
	                    eClassNameToEClassMap,
	                    metaContext, 
	                    attributeTypesContext, 
	                    objectClassesContext,
	                    childEClass,
	                    oidPrefix );
	                
	                EObjectClassDestroyer.destroy(
	                    metaContext,
	                    attributeTypesContext, 
	                    objectClassesContext, 
	                    eClass, 
	                    oidPrefix );
	                
	                rdn = CN + "=" + eClass.getName();
	                metaContext.destroySubcontext( rdn );
                } 
            }
        }
  }
}