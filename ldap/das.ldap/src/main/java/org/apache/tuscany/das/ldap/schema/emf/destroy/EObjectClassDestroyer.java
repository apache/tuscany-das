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

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.create.ComplexTypeRDNCreator;
import org.apache.tuscany.das.ldap.schema.emf.create.EcoreTypeSystemHelper;
import org.eclipse.emf.ecore.EClass;

/**
 * The Class EObjectClassDestroyer.
 */
public class EObjectClassDestroyer
implements AttributeTypeConstants
{
    public static void destroy(
        DirContext metaContext,
        DirContext attributeTypesContext,
        DirContext objectClassesContext,
        EClass eClass,
        String oidPrefix)
    throws NamingException, Exception
    {
        String namespaceURI          = 
            eClass.getEPackage().
            getNsURI();

        String rdn = 
            ComplexTypeRDNCreator.
            create( 
                oidPrefix,
                namespaceURI,
                eClass.getName());
        
        objectClassesContext.
        destroySubcontext(
            rdn);

        EcoreTypeSystemHelper.
        destroyAttributeTypes( 
            attributeTypesContext, 
            eClass, 
            oidPrefix ); 

        LdapContext parentMetaContext = 
            null;
        
        EClass eClassParent          =
            EcoreTypeSystemHelper.
            getEClassParent( eClass );
        
        /*
         * After destroying the ObjecClass and its attributes
         * we must also clean up the meta context.
         */
        
        if (eClassParent != null)
        {
            rdn = CN + "=" + eClassParent.getName();
            
            parentMetaContext = 
                ( LdapContext ) 
                metaContext.lookup( rdn );
            
            rdn = CN + "=" + eClass.getName();
            
            parentMetaContext.destroySubcontext( rdn );
        }
    }
}