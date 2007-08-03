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

/**
 * The Class AbstractAttributeTypeCreator.
 */
public abstract class AbstractAttributeTypeCreator
extends AbstractTypeCreator
{
    
    public AbstractAttributeTypeCreator() 
    {
        super();

        objectClassAttribute.add( META_ATTRIBUTE_TYPE );
        basicAttributes.put( M_COLLECTIVE,           LDAP_FALSE );
        basicAttributes.put( M_EQUALITY,             M_EQUALITY__NAME_OR_NUMERIC_ID_MATCH );
        basicAttributes.put( M_NO_USER_MODIFICATION, LDAP_FALSE);
        basicAttributes.put( M_SINGLE_VALUE,         LDAP_FALSE );
        basicAttributes.put( M_USAGE,                LDAP_USER_APPLICATIONS);
    }
}