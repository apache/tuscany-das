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
package org.apache.tuscany.das.ldap.constants;

public interface DASConstants {
    String DAS_META_ID_DEFAULT                       = 
        "0";

    
    String DAS_XSD_NAMESPACE                 = 
        "http://org.apache.tuscany/das/ldap";

    String M_META_TOP_SDO_OBJECT_CLASS        = 
        "metaTopSDO";
    
    String M_COMPLEX_MAY                      = 
        "m-complexMay";
    
    String M_COMPLEX_MAY_DESCRIPTION          = 
        "SDO Type member that is a reference";

    String M_COMPLEX_MUST                               = 
        "m-complexMust";
    
    String M_COMPLEX_MUST_DESCRIPTION       = 
        "Required SDO Type member reference.";
    
    String ID                                                         = 
        "id";
    
    String ID_DESCRIPTION                                 = 
        "Unique DataObject Instance Integer ID";
    
    //==================================================
    String XSD_CONTEXT_NAME                         = 
        "xsd";

    String ECORE_CONTEXT_NAME                     = 
        "ecore";

    String DAS_CONTEXT_NAME                         = 
        "das";

    String SYNTAXES_CONTEXT_NAME               = 
        "syntaxes";
    
    String SYNTAX_CHECKERS_CONTEXT_NAME = 
        "syntaxCheckers";
    
    String ATTRIBUTE_TYPES_CONTEXT_NAME   = 
        "attributeTypes";
    
    String OBJECT_CLASSES_CONTEXT_NAME     = 
        "objectClasses";
    
    String META_CONTEXT_NAME                        =
        "meta";
    //==================================================

    String LDAP_USER_APPLICATIONS                 = 
        "USER_APPLICATIONS";
    
    String LDAP_FALSE                                         = 
        "FALSE";
    
    String TUSCANY_OID_PREFIX_VALUE            = 
        "1.3.6.1.4.1.18060.4";
    
    
    String OID_SYNTAX_OID_VALUE                     = 
        "1.3.6.1.4.1.1466.115.121.1.38";
    
    String INTEGER_SYNTAX_OID_VALUE            = 
        "1.3.6.1.4.1.1466.115.121.1.27";
    
    String DISTINGUISHED_NAME_SYNTAX_OID_VALUE = 
        "1.3.6.1.4.1.1466.115.121.1.12";
}
