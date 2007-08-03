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
 */
package org.apache.tuscany.das.ldap.schema.emf.create;

import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.schema.create.AbstractTypeCreator;
import org.apache.tuscany.das.ldap.schema.create.ComplexTypeRDNCreator;
import org.apache.tuscany.das.ldap.util.ComplexTypeNamespaceQualifier;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.eclipse.emf.ecore.EClass;

public class EObjectClassCreator
extends AbstractTypeCreator
{
    public EObjectClassCreator()
    {
        super();
    }
    /**
     * Create.
     * 
     * @param oidPrefix the oid prefix
     * @param eAttribute the e attribute
     * @param attributeTypesContext the attribute types context
     * 
     * @throws Exception the exception  
     * @throws NamingException the naming exception
     * 
     * Note that null can passed for the processedEClassifiers list
     * only when the EClass has not parents.
     */
    public void create(
        DirContext metaContext,
        DirContext attributeTypesContext,
        DirContext objectClassesContext,
        EClass eClass,
        List<EClass> processedEClassifiers,
        String oidPrefix)
    throws NamingException, Exception
    {
        if ( processedEClassifiers != null )
        {
            EClass eClassParent           = 
                EcoreTypeSystemHelper.
                getEClassParent( eClass );
            
            if (eClassParent != null)
            {
                boolean isEClassProcessed = 
                    EObjectClassCreatorHelper.
                    isEClassProcessed(
                        eClassParent, 
                        processedEClassifiers);
                
                if (!isEClassProcessed)
                {
                    this.create(
                        metaContext,
                        attributeTypesContext,
                        objectClassesContext,
                        eClassParent,
                        processedEClassifiers,
                        oidPrefix);
                }
            }
        }
            
        objectClassAttribute.add( META_OBJECT_CLASS );

        EcoreTypeSystemHelper.
        createAttributeTypes( 
            attributeTypesContext, 
            eClass, 
            oidPrefix );
        
        String namespaceURI           = 
            eClass.getEPackage().
            getNsURI();
        
        String qualifiedEClassNameURI = 
            ComplexTypeNamespaceQualifier.
            qualify(
                namespaceURI, 
                eClass.getName());
        
        String normalizedEObjectName   =
        	QualifiedNameNormalizer.
        	normalize(qualifiedEClassNameURI);
        

        basicAttributes.put( 
            M_NAME, 
            normalizedEObjectName );

        basicAttributes.put( 
            M_DESCRIPTION, 
            eClass.getName() );
        
        basicAttributes.put( 
            M_OBSOLETE, 
            LDAP_FALSE );

        //TODO Remember to test with parent combination.
        EObjectClassCreatorHelper.
        addParentAttribute( 
            basicAttributes, 
            namespaceURI,
            eClass );

        if (!eClass.isAbstract())
        {
            basicAttributes.put( 
                M_TYPE_OBJECT_CLASS, 
                STRUCTURAL );
        }
        else
        {
            basicAttributes.put( 
                M_TYPE_OBJECT_CLASS, 
                ABSTRACT );
        }

        
        /*
        EObjectClassCreatorHelper.
        addEAttributes( 
            basicAttributes,
            namespaceURI, 
            eClass );
        
        EObjectClassCreatorHelper.
        addEReferences( 
            basicAttributes, 
            namespaceURI, 
            eClass );
        */
        
        
        EObjectClassCreatorHelper.
        addEStructuralFeatures( 
            basicAttributes, 
            namespaceURI, 
            eClass );
        
        String rdn = 
            ComplexTypeRDNCreator.
            create( 
                oidPrefix,
                namespaceURI,
                eClass.getName());
        
        EObjectClassCreatorHelper.
        createInheritanceMetaData( 
            metaContext, 
            eClass );
        
        /* TODO - Remove once everything is simmered down
        NamingEnumeration namingEnumeration = 
        	basicAttributes.getAll();
        
        while (namingEnumeration.hasMore())
        {
        	Attribute attribute = (Attribute) namingEnumeration.next();
        	System.out.println(attribute);
        }
        */
        

        objectClassesContext.
        createSubcontext(
            rdn, 
            basicAttributes );
        
        
        if ( processedEClassifiers != null )
        {
            processedEClassifiers.add(eClass);
        }
    }
}