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
package org.apache.tuscany.das.ldap.oid.create;

import org.apache.tuscany.das.ldap.encryption.constants.EncryptionConstants;
import org.apache.tuscany.das.ldap.encryption.util.ChecksumUtils;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;

/**
 * The Class SimpleTypeOIDCreator.
 * Creates unique OIDs for namespaced
 * metadata such as the XSD DataType
 * with (Class specific term) qualifiedNameURI 
 * http://www.w3.org/2001/XMLSchema/string
 * 
 */
public class SimpleTypeOIDCreator implements EncryptionConstants
{
    /**
     * Creates a unique OID.
     * 
     * @param instanceClassName the instance class name
     * @param oidPrefix the OID Branch
     * @param complexTypeName the name
     * @param namespaceURI the metadata namespace URI 
     * 
     * @return the string
     * 
     * @throws Exception the exception
     */
    public static String create(
        String oidPrefix,
        String namespaceURI,
        String complexTypeName,
        String simpleTypeName) 
    throws Exception
    {
        String qualifiedNameURI = 
            SimpleTypeNamespaceQualifier.qualify(
                namespaceURI, 
                complexTypeName, 
                simpleTypeName);

        String postfixOID = ChecksumUtils.computeMD5Hash(qualifiedNameURI);
        
        postfixOID = OIDCreatorHelper.calculateSegmentedOID(postfixOID);
        return oidPrefix + "." + postfixOID;
    }
}