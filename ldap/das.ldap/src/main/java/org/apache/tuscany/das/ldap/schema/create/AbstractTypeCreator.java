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

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.EnumeratedSchemaAttributeTypeValues;
import org.apache.tuscany.das.ldap.schema.constants.ObjectClassConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaAttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaObjectClassConstants;

public abstract class AbstractTypeCreator 
implements 
EnumeratedSchemaAttributeTypeValues,
SchemaAttributeTypeConstants,
AttributeTypeConstants,
SchemaObjectClassConstants,
ObjectClassConstants,
DASConstants
{
    protected Attribute objectClassAttribute; 
    protected Attributes basicAttributes;
    
    public AbstractTypeCreator() 
    {
        objectClassAttribute    = 
            new BasicAttribute( 
                OBJECT_CLASS, TOP );
        
        objectClassAttribute.add( 
            META_TOP );
        
        basicAttributes = new BasicAttributes();
        basicAttributes.put(objectClassAttribute);
        basicAttributes.put( M_OBSOLETE, LDAP_FALSE );
    }
}