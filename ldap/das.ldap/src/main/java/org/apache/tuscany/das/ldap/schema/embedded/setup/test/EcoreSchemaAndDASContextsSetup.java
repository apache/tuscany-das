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
package org.apache.tuscany.das.ldap.schema.embedded.setup.test;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.create.InitialContextCreator;
import org.apache.tuscany.das.ldap.create.MetaContextCreator;
import org.apache.tuscany.das.ldap.destroy.InitialContextDestroyer;
import org.apache.tuscany.das.ldap.destroy.MetaContextDestroyer;

/*
 * TODO Document that the name of a root object cannot be "meta"
 * TODO Change "meta" to meta-inf
 */
public class EcoreSchemaAndDASContextsSetup 
extends EcoreSchemaContextsSetup
implements DASConstants
{
    public void tearDown() 
    throws NamingException, Exception
    {
        MetaContextDestroyer.
        destroy( rootContext );
        
        InitialContextDestroyer.
        destroy(
            rootContext, 
            dasPartitionContext );
        
        super.tearDown();
                
        dasPartitionContext.close();
    }

    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        
        rootContext                        = 
            (LdapContext) 
            InitialContextCreator.
            create(
                xsdNamespace,
                dasPartitionContext);

        metaContext                               =
            (LdapContext) 
            MetaContextCreator.
            create( rootContext);
    }
    
    protected LdapContext rootContext = null;
    protected LdapContext metaContext = null;
}