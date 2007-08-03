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
package org.apache.tuscany.das.ldap.destroy;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.util.JNDIUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class InitialContextDestroyer.
 */
public class InitialContextDestroyer
{
    
    /**
     * Destroys the initial naming context
     * when the parent entries do not contain
     * other child entries.  If the initial
     * naming context also supports other
     * entries, then a NamingException will
     * be thrown.
     * 
     * @param partitionContext the partition context
     * @param initialContext the initial context
     * 
     * @throws NamingException the naming exception
     */
    public static void destroy(
        DirContext initialContext, 
        DirContext partitionContext) 
    throws NamingException
    {
        String[] initialContextComponents = 
            JNDIUtil.
            calculateDNComponents( initialContext );
        
        String[] partitionContextComponents = 
            JNDIUtil.calculateDNComponents( partitionContext );
        
        int numberOfPossibleSubcontexts = 
            initialContextComponents.length
            - partitionContextComponents.length;
        
        for (int i = 0; i < numberOfPossibleSubcontexts; i++)
        {
            String rdn = initialContextComponents[i];
            
            initialContext = JNDIUtil.getParentContext( 
                initialContext, 
                partitionContext );

            initialContext.destroySubcontext( rdn );
        }
    }
}