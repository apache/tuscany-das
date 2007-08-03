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

public interface ApacheDSConnectionConstants
{
    String APACHE_CONTEXT_FACTORY                          =
        "org.apache.directory.server.core.jndi.CoreContextFactory";

    String SUN_CONTEXT_FACTORY                                =
        "com.sun.jndi.ldap.LdapCtxFactory";
    
    String SIMPLE_SECURITY_AUTHENTICATION_VALUE = 
        "simple";
    
    String DEFAULT_SECURITY_PRINCIPAL_VALUE          = 
        "uid=admin,ou=system";
    
    String DEFAULT_SECURITY_CREDENTIALS_VALUE    = 
        "secret";
    
    String SCHEMA_PROVIDER_PATH                              =
    	"ou=schema";
    
    String SYSTEM_PROVIDER_PATH                               =
    	"ou=system";
    
    String NLOG4J_CONFIGURATION_FILEPATH              =
    	"src/test/resources/log4j.properties";
    	
}
