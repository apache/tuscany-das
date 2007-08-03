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
package org.apache.tuscany.das.ldap.emf.read;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class EDataObjectReader
implements DASConstants
{
    /**
     * Read.
     * 
     * @param eClass the e class
     * @param id the id
     * @param containerContext the container context
     * 
     * @return the e data object
     * 
     * @throws NamingException the naming exception
     * 
     * Note that this is more of a utility, as only this class's
     * helper is used in restoring the EDataGraph. 
     */
    public static EDataObject read(
    		EClass eClass,
    		String id,
    		LdapContext containerContext) 
    throws NamingException
    {
        String namespaceURI                 =
            eClass.getEPackage().
            getNsURI();

        EAttribute idEAttribute             =
    		eClass.getEIDAttribute();

        String qualifiedIDEAttributeName    =
            SimpleTypeNamespaceQualifier.
            qualify( 
                namespaceURI, 
                eClass.getName(), 
                idEAttribute.getName() );
        
    	String normalizedIDEAttributeName   =
        	QualifiedNameNormalizer.
        	normalize(qualifiedIDEAttributeName);
        
    	String eDataObjectRDN               = 
        	normalizedIDEAttributeName + "=" + id;
        
        LdapContext eDataObjectContext      =
        	(LdapContext) 
        	containerContext.
        	lookup(eDataObjectRDN);
        
        Attributes attributes               = 
        	eDataObjectContext.getAttributes("");
        
        EDataObject eDataObject =
        	(EDataObject)
        	eClass.getEPackage().
        	getEFactoryInstance().
        	create(eClass);

        EDataObjectReaderHelper.
        restoreEAttributes( 
            eClass, 
            eDataObject, 
            namespaceURI, 
            attributes );
        
        return eDataObject;
    }
}