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

import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.emf.Context;
import org.apache.tuscany.das.ldap.emf.LdapDAS;
import org.apache.tuscany.das.ldap.prototype.setup.test.LdapDASSetupTest;
import org.apache.tuscany.model.Configuration;
import org.apache.tuscany.model.ConfigurationFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.sdo.EChangeSummary;
import org.eclipse.emf.ecore.sdo.EDataGraph;
import org.eclipse.emf.ecore.sdo.EDataObject;


public class LdapDASTest extends LdapDASSetupTest
{
    Configuration configuration       = null;
    LdapDAS ldapDAS                   = null;
    
    public void setUp() throws Exception
    {
        super.setUp();
        configuration                 = 
            ConfigurationFactory.
            INSTANCE.
            createConfiguration();
        
        configuration.setEmbedded( true );
        ldapDAS = new LdapDAS(configuration);
    }
    
    public void tearDown() throws NamingException, Exception
    {
        ldapDAS.getAdsEmbeddedConnection().shutdown();
        super.tearDown();
    }
    
    public void testConstructor() throws NamingException
    {
        Context context = ldapDAS.getContext();
        assertEquals(
            "ou=das", 
            context.getDasContext().getNameInNamespace());
        assertEquals(
            "cn=meta,ou=das", 
            context.getDasMetaContext().getNameInNamespace());
        assertEquals(
            "ou=attributeTypes,cn=ecore,ou=schema", 
            context.getEcoreAttributeTypesContext().getNameInNamespace());
        assertEquals(
            "ou=objectClasses,cn=ecore,ou=schema", 
            context.getEcoreObjectClassesContext().getNameInNamespace());
    }
    
    public void testCreate() throws Exception, NamingException
    {
        ldapDAS.create( eDataGraph );
        
        Context context              = 
            ldapDAS.getContext();
        
        dataObjectToRelativeDNCache  =
            context.getDataObjectToRelativeDNCache();
        
        Map<String, LdapContext> xsdNamespaceToLdapContextMap = 
            context.
            getXsdNamespaceToLdapContextMap();
        
        EDataObject rootObject = 
            ( EDataObject ) 
            eDataGraph.getRootObject();
        
        String namespace   = 
            rootObject.
            eClass().
            getEPackage().
            getNsURI();
        
        assertEquals(
            "http://example.com/users/accounts",
            namespace);
        
        LdapContext rootContext  = 
            xsdNamespaceToLdapContextMap.
            get( namespace );
        
        assertEquals(
            "cn=accounts,cn=users,cn=example,cn=com,ou=das", 
            rootContext.getNameInNamespace() );
        
        String userDataObjectRDN = 
            dataObjectToRelativeDNCache.get( rootObject );

        //============================================================
        //Verify the RDN
        //============================================================

        assertEquals(
            "example-com-users-accounts-User-id=1", 
            userDataObjectRDN );
        
        //============================================================
        //Verify the attribute values
        //============================================================

        Attributes attributes    = 
            rootContext.
            getAttributes( userDataObjectRDN );
        
        assertEquals(
            attributes.get("objectClass").get(), 
                "example-com-users-accounts-User");
        
        assertEquals(
            attributes.get("objectClass").get(1), 
                "metaObjectClass");
        
        assertEquals(
            attributes.get("objectClass").get(2), 
                "metaTop");

        assertEquals(
            attributes.get("objectClass").get(3), 
                "top");
        
        assertEquals(
            attributes.get("example-com-users-accounts-User-userAge").get(), 
                "33");

        assertEquals(
            attributes.get("example-com-users-accounts-User-userName").get(), 
                "ole");
        
        assertEquals(
            attributes.get("example-com-users-accounts-User-userPassword").get(), 
                "secret");
        
        assertEquals(
            attributes.get("example-com-users-accounts-User-userHeight").get(), 
                "6.11");

        assertEquals(
            attributes.get("example-com-users-accounts-User-id").get(), 
                "1");
        
        assertEquals(
            attributes.get("example-com-users-accounts-User-isMale").get(), 
                "TRUE");
        
        
        assertEquals(
            attributes.get("example-com-users-accounts-User-userAliases").get(), 
                "neo");

        assertEquals(
            attributes.get("example-com-users-accounts-User-userAliases").get(1), 
                "trinity");
        
        assertEquals(
            attributes.get("example-com-users-accounts-User-userAliases").get(2), 
                "morpheus");

        assertEquals(
            attributes.get("example-com-users-accounts-User-configuration").get(), 
                "5");

        assertEquals(
            attributes.get("example-com-users-accounts-User-authorizationList").get(), 
                "2");

        assertEquals(
            attributes.get("example-com-users-accounts-User-authorizationList").get(1), 
                "3");

        
        EDataObject configurationObject = 
            ( EDataObject ) rootObject.
            eGet( userConfigurationEReference );
        
        String userConfigurationDataObjectRDN = 
            dataObjectToRelativeDNCache.
            get( configurationObject );

        //============================================================
        //Verify the RDN
        //============================================================

        assertEquals(
            "example-com-users-accounts-Configuration-id=5,cn=configuration,example-com-users-accounts-User-id=1", 
            userConfigurationDataObjectRDN);

        attributes = rootContext.getAttributes( userConfigurationDataObjectRDN );
        
        //============================================================
        //Verify the Attributes
        //============================================================

        assertEquals(
            attributes.get("objectClass").get(), 
                "example-com-users-accounts-Configuration");
        
        assertEquals(
            attributes.get("objectClass").get(1), 
                "metaObjectClass");
        
        assertEquals(
            attributes.get("objectClass").get(2), 
                "metaTop");

        assertEquals(
            attributes.get("objectClass").get(3), 
                "top");
        
        assertEquals(
            attributes.get("example-com-users-accounts-Configuration-authorization").get(), 
                "2");
        
        assertEquals(
            attributes.get("example-com-users-accounts-Configuration-id").get(), 
                "5");
        
        assertEquals(
            attributes.get("example-com-users-accounts-Configuration-authorizations").get(), 
                "2");
        
        assertEquals(
            attributes.get("example-com-users-accounts-Configuration-authorizations").get(1), 
                "3");
        
        EList<EDataObject> authorizationList = 
                ( EList<EDataObject> ) rootObject.
                eGet( userAuthorizationEReference );
        
        EDataObject authorizationDataObject1 = 
            authorizationList.get( 0 );
        
        String userAuthorizationDataObject1RDN = 
            dataObjectToRelativeDNCache.
            get( authorizationDataObject1 );

        //============================================================
        //Verify the RDN
        //============================================================

        assertEquals(
            "example-com-users-accounts-Authorization-id=2,cn=authorizationList,example-com-users-accounts-User-id=1", 
            userAuthorizationDataObject1RDN);
        
        attributes = rootContext.getAttributes( userAuthorizationDataObject1RDN );
        
        //============================================================
        //Verify the Attributes
        //============================================================

        assertEquals(
            attributes.get("objectClass").get(), 
                "example-com-users-accounts-Authorization");

        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-id").get(), 
                "2");

        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-executeAuthorization").get(), 
                "TRUE");
        
        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-readAuthorization").get(), 
                "TRUE");
        
        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-writeAuthorization").get(), 
                "TRUE");
  
        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-fileName").get(), 
                "somefile.text");


        EDataObject authorizationDataObject2 = 
            authorizationList.get( 1 );
        
        String userAuthorizationDataObject2RDN = 
            dataObjectToRelativeDNCache.
            get( authorizationDataObject2 );

        //============================================================
        //Verify the RDN
        //============================================================

        assertEquals(
            "example-com-users-accounts-Authorization-id=3,cn=authorizationList,example-com-users-accounts-User-id=1", 
            userAuthorizationDataObject2RDN);
        
        attributes = rootContext.getAttributes( userAuthorizationDataObject2RDN );
        
        //============================================================
        //Verify the Attributes
        //============================================================

        assertEquals(
            attributes.get("objectClass").get(), 
                "example-com-users-accounts-Authorization");

        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-id").get(), 
                "3");

        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-executeAuthorization").get(), 
                "TRUE");
        
        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-readAuthorization").get(), 
                "TRUE");
        
        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-writeAuthorization").get(), 
                "TRUE");
  
        assertEquals(
            attributes.get("example-com-users-accounts-Authorization-fileName").get(), 
                "someOtherfile.text");


        /* Finding out the values of the attributes
        NamingEnumeration namingEnumeration = attributes.getAll();
        while (namingEnumeration.hasMore())
        {
            Attribute attribute = ( Attribute ) namingEnumeration.next();
            System.out.println(attribute);
        }
        */
    }
    
    public void testRead() throws NamingException, Exception
    {
        /*
         * More exhaustive testing has been done on the 
         * class that LdapDAS.read() delegates to.
         */
            ldapDAS.create( eDataGraph );
        
        EDataGraph newEDataGraph = 
            ldapDAS.read( userEClass, "1" );
        
        EDataObject rootObject   = 
            ( EDataObject ) newEDataGraph.getRootObject();
        
        assertEquals(
            rootObject.eGet( userNameEAttribute),
                "ole");
        
        EDataObject configurationObject = 
            ( EDataObject ) 
            rootObject.eGet( userConfigurationEReference );
        
        assertEquals(
            configurationObject.eGet( configurationIDEAttribute),
                "5");
        
    }
    
    public void testUpdate() throws NamingException, Exception
    {
        /*
         * More exhaustive testing has been done on the 
         * class that LdapDAS.update() delegates to.
         */
        ldapDAS.create( eDataGraph );
        
        EDataGraph newEDataGraph = ldapDAS.read( userEClass, "1" );
        
        
        //newEDataGraph.getChangeSummary().beginLogging();
        EChangeSummary eChangeSummary = ( EChangeSummary ) newEDataGraph.getChangeSummary();
        eChangeSummary.beginLogging();

        EDataObject rootObject   = 
            ( EDataObject ) newEDataGraph.getRootObject();

        rootObject.eSet( userNameEAttribute, "Brooke" );
        rootObject.eSet( userAgeEAttribute, 5 );
        
        //newEDataGraph.getChangeSummary().endLogging();
        
        eChangeSummary.endLogging();
        ldapDAS.update( newEDataGraph );
        
        LdapContext rootContext = ldapDAS.getContext().
        getXsdNamespaceToLdapContextMap().
        get( 
            rootObject.
            eClass().
            getEPackage().
            getNsURI());
        
        Attributes attributes = 
            rootContext.
            getAttributes( 
                "example-com-users-accounts-User-id=1" );
        
        assertEquals(
            attributes.get("example-com-users-accounts-User-userAge").get(), 
                "5");

        assertEquals(
            attributes.get("example-com-users-accounts-User-userName").get(), 
                "Brooke");
    }
}