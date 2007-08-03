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
package org.apache.tuscany.das.ldap.emf.test;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.ldap.LdapContext;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.tuscany.das.ldap.connect.ADSEmbeddedConnection;
import org.apache.tuscany.das.ldap.emf.Context;
import org.apache.tuscany.das.ldap.emf.LdapDAS;
import org.apache.tuscany.das.ldap.emf.LdapDASHelper;
import org.apache.tuscany.model.Configuration;
import org.apache.tuscany.model.ConfigurationFactory;
import org.apache.tuscany.model.DASMeta;
import org.eclipse.emf.ecore.sdo.EDataGraph;
import org.eclipse.emf.ecore.sdo.EDataObject;

public class LdapDASHelperTest extends TestCase
{
    
    private LdapContext schemaContext = null;
    private ADSEmbeddedConnection adsEmbeddedConnection;
    Configuration configuration                 = null;
    
    public void tearDown() throws NamingException, Exception
    {
        adsEmbeddedConnection.shutdown();
    }
    
    public void setUp() throws Exception
    {
        /*
         * Note that I had to move the schemaContext
         * creation inside each test method due to a 
         * bug in ADS.
         */
        FileUtils.deleteDirectory(new File("server-work"));

        configuration                 = 
            ConfigurationFactory.
            INSTANCE.
            createConfiguration();
        
        adsEmbeddedConnection = 
            new ADSEmbeddedConnection(
                configuration);
    }
    
    
    public void testReadDasMeta() throws Exception
    {
        Configuration configuration = 
            ConfigurationFactory.
            INSTANCE.
            createConfiguration();
        
        configuration.setEmbedded( true );
        
        LdapDAS ldapDAS = new LdapDAS(configuration);
        
        Context context = ldapDAS.getContext();
        

        Map<EDataObject, String> metaDataObjectToRelativeDNCache =
            new Hashtable<EDataObject, String>();

        EDataGraph dasMetaDataGraph = 
        LdapDASHelper.readDasMeta( 
            context, 
            metaDataObjectToRelativeDNCache );
        
        assertEquals(
            "org-apache-tuscany-das-ldap-configuration-model-ecore-v100-DASMeta-id=0",
            metaDataObjectToRelativeDNCache.values().toArray()[0]);
        
        DASMeta dasMeta = ( DASMeta ) dasMetaDataGraph.getRootObject();

        assertEquals(
            dasMeta,
            metaDataObjectToRelativeDNCache.keySet().toArray()[0] );
        
        assertEquals(
            "org-apache-tuscany-das-ldap-configuration-model-ecore-v100-DASMeta-id=0",
            metaDataObjectToRelativeDNCache.get(dasMeta));
    }
    
    public void testCreateEcoreContext() throws NamingException
    {
        schemaContext = 
            adsEmbeddedConnection.
            connect( 
                configuration.getSchemaPartitionName() );

        LdapContext ecoreContext = 
            LdapDASHelper.createEcoreContext(
                schemaContext);
        
        assertEquals(
            ecoreContext.getNameInNamespace(),
            "cn=ecore,ou=schema" );
        
        schemaContext.destroySubcontext( "cn=ecore" );
    }
    
    public void testCreateEcoreObjectClassesContext() throws NamingException
    {
        schemaContext = 
            adsEmbeddedConnection.
            connect( 
                configuration.getSchemaPartitionName() );
        
        LdapContext ecoreContext = 
            LdapDASHelper.createEcoreContext(
                schemaContext);
        
        LdapContext ecoreObjectClassesContext = 
            LdapDASHelper.
            createEcoreObjectClassesContext( 
                schemaContext );

        ecoreObjectClassesContext = 
            LdapDASHelper.
            createEcoreObjectClassesContext( 
                schemaContext );

        assertEquals(
            ecoreObjectClassesContext.getNameInNamespace(),
            "ou=objectClasses,cn=ecore,ou=schema");

                
        ecoreContext.destroySubcontext( "ou=objectClasses" );
        schemaContext.destroySubcontext( "cn=ecore" );
    }

    public void testCreateEcoreAttributeTypesContext() throws NamingException
    {
        schemaContext = 
            adsEmbeddedConnection.
            connect( 
                configuration.getSchemaPartitionName() );

        LdapContext ecoreContext = 
            LdapDASHelper.createEcoreContext(
                schemaContext);
        
        LdapContext ecoreAttributeTypesContext = 
            LdapDASHelper.
            createEcoreAttributeTypesContext( schemaContext );

        ecoreAttributeTypesContext = 
            LdapDASHelper.
            createEcoreAttributeTypesContext( schemaContext );

        assertEquals(
            ecoreAttributeTypesContext.getNameInNamespace(),
            "ou=attributeTypes,cn=ecore,ou=schema");
                
        ecoreContext.destroySubcontext( "ou=attributeTypes" );
        schemaContext.destroySubcontext( "cn=ecore" );
    }
}