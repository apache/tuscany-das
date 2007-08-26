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
package org.apache.tuscany.das.ldap.schema.create;

import org.apache.tuscany.das.ldap.oid.create.ComplexTypeOIDCreator;
import org.apache.tuscany.das.ldap.schema.constants.SchemaAttributeTypeConstants;

/**
 * The Class DataTypeRDNCreator.
 */
public class ComplexTypeRDNCreator
implements SchemaAttributeTypeConstants
{
    /**
     * Create.
     * 
     * @param oidPrefix the oid prefix
     * @param namespaceURI the namespace URI
     * @param complexTypeName the complex type name
     * 
     * @return the string that is the rdn
     * 
     * @throws Exception the exception
     */
    public static String create(
        String oidPrefix,
        String namespaceURI,
        String complexTypeName
        )
    throws Exception
    {
        String oid = 
            ComplexTypeOIDCreator.create( 
                oidPrefix,
                namespaceURI,
                complexTypeName );
        
        String rdn = 
            M_OID + "="  + oid;
        
        return rdn;
    }
}