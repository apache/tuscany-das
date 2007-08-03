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
package org.apache.tuscany.das.ldap.destroy.test;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.destroy.InitialContextDestroyer;
import org.apache.tuscany.das.ldap.schema.embedded.setup.test.AbstractTestSetup;

public class InitialContextDestroyerTest extends AbstractTestSetup
{
    DirContext test1Context = null;
    DirContext test2Context = null;
    DirContext test3Context = null;
    
    /*
     * Tests that the initial context destroyer 
     * destroys child projects before parent projects.
     */
    public void testDestroy() throws NamingException
    {
        test1Context = 
            ( DirContext ) 
            dasPartitionContext.
            createSubcontext( "cn=test1" );
        
        test2Context = 
            ( DirContext ) 
            test1Context.
            createSubcontext( "cn=test2" );
        
        test3Context = 
            ( DirContext ) 
            test2Context.
            createSubcontext( "cn=test3" );
        
        Object test = null; 
        test = 
            dasPartitionContext.
            lookup( "cn=test1" );
        
        assertNotNull(test);
        
        InitialContextDestroyer.
        destroy(
            test3Context, 
            dasPartitionContext);
        
        try 
        {
            test = dasPartitionContext.lookup( "cn=test1" );
        }
        catch (Exception e)
        {
            test = "caught";
        }
        
        assertSame("caught", test);
    }
}