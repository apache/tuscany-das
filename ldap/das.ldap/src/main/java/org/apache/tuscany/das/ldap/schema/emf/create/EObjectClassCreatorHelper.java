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

import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.schema.constants.AttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.EnumeratedSchemaAttributeTypeValues;
import org.apache.tuscany.das.ldap.schema.constants.ObjectClassConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaAttributeTypeConstants;
import org.apache.tuscany.das.ldap.schema.constants.SchemaObjectClassConstants;
import org.apache.tuscany.das.ldap.util.ComplexTypeNamespaceQualifier;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The Class EObjectClassCreatorHelper.
 */
public class EObjectClassCreatorHelper
implements 
EnumeratedSchemaAttributeTypeValues,
SchemaAttributeTypeConstants,
AttributeTypeConstants,
SchemaObjectClassConstants,
ObjectClassConstants,
DASConstants
{
    /**
     * Adds the parent attribute.
     * 
     * @param namespaceURI the namespace URI
     * @param objectClassAttributes the object class attributes
     * @param eClass the e class
     */
    public static void addParentAttribute(
        Attributes objectClassAttributes,
        String namespaceURI,
        EClass eClass)
    {
        EClass eClassParent     = 
            EcoreTypeSystemHelper.getEClassParent( eClass );

        if(eClassParent !=null)
        {
            String qualifiedEClassParentNameURI = 
                ComplexTypeNamespaceQualifier.
                qualify(
                    namespaceURI, 
                    eClassParent.
                    getName());
            
            String normalizedEObjectName   =
            	QualifiedNameNormalizer.
            	normalize(qualifiedEClassParentNameURI);

            objectClassAttributes.put( 
                M_SUP_OBJECT_CLASS, 
                normalizedEObjectName);
        }
        else
        {
            objectClassAttributes.put( 
                M_SUP_OBJECT_CLASS, 
                META_OBJECT_CLASS );
        }
    }
    
    /**
     * Adds the E attributes.
     * 
     * @param namespaceURI the namespace URI
     * @param objectClassAttributes the object class attributes
     * @param eClass the e class
     */
    public static void addEAttributes(
        Attributes objectClassAttributes,
        String namespaceURI,
        EClass eClass)
    {
        List<EAttribute> eAttributes =
            eClass.getEAttributes();
        
        Iterator<EAttribute> eAttributeIterator  =
            eAttributes.iterator();
        
        EAttribute eAttribute          = 
            null;
        
        String qualifiedEAttributeName = 
            null;
        
        Attribute mMayAttribute        =
            null;
        
        Attribute mMustAttribute       =
            null;
        
        while( eAttributeIterator.hasNext())
        {
            eAttribute = eAttributeIterator.next();

            qualifiedEAttributeName    = 
                SimpleTypeNamespaceQualifier.
                qualify(
                    namespaceURI,
                    eClass.getName(),
                    eAttribute.getName());
            
            String normalizedEAttributeName   =
            	QualifiedNameNormalizer.
            	normalize(qualifiedEAttributeName);
            
            if (!eAttribute.isRequired())
            {
                if (mMayAttribute == null)
                {
                    mMayAttribute = new BasicAttribute(M_MAY);
                }
                mMayAttribute.add(normalizedEAttributeName);
            }
            else
            {
                if (mMustAttribute == null)
                {
                    mMustAttribute = new BasicAttribute(M_MUST);
                }
                mMustAttribute.add(normalizedEAttributeName);
            }
        }

        if (mMayAttribute != null)
        {
            objectClassAttributes.put( mMayAttribute );
        }
        if (mMustAttribute != null)
        {
            objectClassAttributes.put( mMustAttribute );
        }
    }
    
    /**
     * Adds the E references.
     * 
     * @param namespaceURI the namespace URI
     * @param objectClassAttributes the object class attributes
     * @param eClass the e class
     */
    public static void addEReferences(
        Attributes objectClassAttributes,
        String namespaceURI,
        EClass eClass)
    {
        List<EReference> eReferences            = 
            eClass.getEReferences();
        
        Iterator<EReference> eReferenceIterator =
            eReferences.iterator();
        
        EReference eReference          = 
            null;
        
        String qualifiedEReferenceName = 
            null;
        
        Attribute mMayAttribute        =
            null;
        
        Attribute mMustAttribute       =
            null;
        
        while ( eReferenceIterator.hasNext())
        {
            eReference = eReferenceIterator.next();

            qualifiedEReferenceName    = 
                SimpleTypeNamespaceQualifier.
                qualify(
                    namespaceURI,
                    eClass.getName(),
                    eReference.getName());

            String normalizedEReferenceName   =
            	QualifiedNameNormalizer.
            	normalize(qualifiedEReferenceName);

            if (!eReference.isRequired())
            {
                if (mMayAttribute == null)
                {
                    mMayAttribute = new BasicAttribute(M_MAY);
                }
                mMayAttribute.add( normalizedEReferenceName );
            }
            else
            {
                if (mMustAttribute == null)
                {
                    mMustAttribute = new BasicAttribute(M_MUST);
                }
                mMustAttribute.add( normalizedEReferenceName );
            }
        }
        if (mMayAttribute != null)
        {
            objectClassAttributes.put( mMayAttribute );
        }
        if (mMustAttribute != null)
        {
            objectClassAttributes.put( mMustAttribute );
        }
    }
    
    /**
     * Adds the E references.
     * 
     * @param namespaceURI the namespace URI
     * @param objectClassAttributes the object class attributes
     * @param eClass the e class
     */
    public static void addEStructuralFeatures(
        Attributes objectClassAttributes,
        String namespaceURI,
        EClass eClass)
    {
        List<EStructuralFeature> eStructuralFeatures            = 
            eClass.getEStructuralFeatures();
        
        Iterator<EStructuralFeature> eStructuralFeatureIterator =
            eStructuralFeatures.iterator();
        
        EStructuralFeature eStructuralFeature                   = 
            null;
        
        String qualifiedEStructuralFeatureName                  = 
            null;
        
        Attribute mMayAttribute        =
            null;
        
        Attribute mMustAttribute       =
            null;
        
        while ( eStructuralFeatureIterator.hasNext())
        {
            eStructuralFeature         = 
                eStructuralFeatureIterator.next();

            qualifiedEStructuralFeatureName    = 
                SimpleTypeNamespaceQualifier.
                qualify(
                    namespaceURI,
                    eClass.getName(),
                    eStructuralFeature.getName());
            
            String normalizedEStructuralFeatureName   =
            	QualifiedNameNormalizer.
            	normalize(qualifiedEStructuralFeatureName);
            
            
            if (!eStructuralFeature.isRequired())
            {
                if (mMayAttribute == null)
                {
                    mMayAttribute = new BasicAttribute(M_MAY);
                }
                mMayAttribute.add( normalizedEStructuralFeatureName );
            }
            else
            {
                if (mMustAttribute == null)
                {
                    mMustAttribute = new BasicAttribute(M_MUST);
                }
                mMustAttribute.add( normalizedEStructuralFeatureName );
            }
        }
        if (mMayAttribute != null)
        {
            objectClassAttributes.put( mMayAttribute );
        }
        if (mMustAttribute != null)
        {
            objectClassAttributes.put( mMustAttribute );
        }

    }

    
    /**
     * Checks if is E class processed.
     * 
     * @param processedEClassifiers the processed E classifiers
     * @param eClass the e class
     * 
     * @return true, if is E class processed
     */
    public static boolean isEClassProcessed(
        EClass eClass, 
        List<EClass> processedEClassifiers)
    {
        return processedEClassifiers.contains( eClass );
    }
   
    /**
     * Creates the inheritance meta data.
     * 
     * @param metaContext the das model meta context
     * @param eClass the e class
     * 
     * @throws NamingException the naming exception
     */
    public static void createInheritanceMetaData(
        DirContext metaContext,
        EClass eClass) 
    throws NamingException
    {
        EClass eClassParent = 
            EcoreTypeSystemHelper.getEClassParent( eClass );
        
        String rdn = null;
        
        if (eClassParent != null)
        {
            DirContext eClassParentMetaContext     = 
                null;
            try
            {
                eClassParentMetaContext            =
                    ( DirContext ) 
                    metaContext.
                    lookup(eClassParent.getName());
            }
            catch ( NamingException e )
            {
                rdn = 
                    CN + "=" + eClassParent.getName();
                
                eClassParentMetaContext            = 
                    ( DirContext ) 
                    metaContext.
                    createSubcontext( rdn );
            }

            rdn = CN + "=" + eClass.getName();
            
            eClassParentMetaContext.
            createSubcontext( rdn );
        }
    }
}