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
package org.apache.tuscany.das.ldap.schema.emf.create;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.apache.tuscany.das.ldap.schema.create.SyntaxEntryCreator;
import org.apache.tuscany.das.ldap.schema.emf.destroy.EStructuralFeatureTypeDestroyer;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The Class EcoreTypeSystemCreatorHelper.
 */
public class EcoreTypeSystemHelper
{
    
    /**
     * Creates the E data types list.
     * 
     * @param ePackage the e package
     * 
     * @return the list< E data type>
     */
    public static List<EDataType> createEDataTypesList(
        EPackage ePackage)
    {
        List<EObject> list         = ePackage.eContents();
        List<EDataType> eDataTypes = new ArrayList<EDataType>();
        Iterator<EObject> iterator = list.iterator();
        while (iterator.hasNext())
        {
            Object object = iterator.next();
            if (object instanceof EDataType )
            {
                eDataTypes.add( ( EDataType ) object );
            }
        }
        return eDataTypes;
    }
    
    public static void createSyntaxCheckerEntries(        
    		String oidPrefix,
            LdapContext syntaxCheckersContext) throws Exception

    {
    	
    }
    
    /**
     * Creates the syntax entries.
     * 
     * @param oidPrefix the oid prefix
     * @param eDataTypes the e data types
     * @param syntaxesContext the syntaxes context
     * 
     * @throws Exception the exception
     */
    public static void createSyntaxEntries(
        List<EDataType> eDataTypes,
        String oidPrefix,
        LdapContext syntaxesContext,
        LdapContext syntaxCheckersContext) throws Exception
    {
        Iterator<EDataType> eDataTypeIterator = 
            eDataTypes.iterator();                          

        String eDataTypeName                  = 
            null;
        String eDataTypeNamespaceURI          = 
            null;
        
        while (eDataTypeIterator.hasNext())
        {
            EDataType eDataType        = 
                eDataTypeIterator.next();
            eDataTypeName              = 
                eDataType.getName();
            eDataTypeNamespaceURI      = 
                eDataType.getEPackage().getNsURI();
            
            SyntaxEntryCreator.create( 
                oidPrefix,
                eDataTypeNamespaceURI,
                eDataTypeName, 
                syntaxesContext,
                syntaxCheckersContext);
        }
    }

    /**
     * Creates the E classifiers list.
     * 
     * @param ePackage the e package
     * 
     * @return the list< E class>
     */
    public static List<EClass> createEClassifiersList(
        EPackage ePackage)
    {
        List<EObject> list               = ePackage.eContents();
        List<EClass> eClassifiers   = new ArrayList<EClass>();
        Iterator<EObject> iterator = list.iterator();
        while (iterator.hasNext())
        {
            Object object = iterator.next();
            if (object instanceof EClassifier )
            {
                eClassifiers.add( ( EClass ) object );
            }
        }
        return eClassifiers;
    }
    
    /**
     * Creates the E classifier entries.
     * 
     * @param eClassifiers the e classifiers
     */
    public static void createEClassifierEntries(
        List<EClass> eClassifiers)
    {
        
    }

    /**
     * Load parent E classifier stack.
     * 
     * @param parentEClassifierStack the parent E classifier stack
     * @param eClass the e class
     * 
     * @return the stack< E class>
     * 
     * Note that clients should pass null for the
     * parentEClassifierStack argument.  The method
     * creates the stack when needed, and uses
     * it during recursion.
     */
    public static Stack<EClass> loadParentEClassifierStack(
        EClass eClass, 
        Stack<EClass> parentEClassifierStack)
    {
        List<EClass> eClassParents   = 
            eClass.getESuperTypes();
        
        if (eClassParents.size() > 0)
        {
            Iterator<EClass> eClassParentIterator = 
                eClassParents.iterator();
            
            while(eClassParentIterator.hasNext())
            {
                EClass parentEClass = 
                    eClassParentIterator.next();
                
                if (!parentEClass.isInterface())
                {
                    if (parentEClassifierStack == null)
                    {
                        parentEClassifierStack = new Stack<EClass>();
                    }

                    parentEClassifierStack.push( parentEClass );
                    
                    eClassParents = parentEClass.getESuperTypes();
                    
                    if (eClassParents.size() == 0)
                    {
                        return parentEClassifierStack;
                    }
                    else
                    {
                        loadParentEClassifierStack( 
                            parentEClass, 
                            parentEClassifierStack );
                    }
                }
            }
        }
        return parentEClassifierStack;            
    }
    
    /**
     * Creates the E object class parents.
     * 
     * @param oidPrefix the oid prefix
     * @param objectClassesContext the object classes context
     * @param eClass the e class
     * 
     * @throws Exception the exception
     * @throws NamingException the naming exception
     */
    public static void createEObjectClassParents(
        DirContext dasModelMetaContext,
        DirContext attributeTypesContext,
        DirContext objectClassesContext,
        EClass eClass,
        String oidPrefix) 
    throws NamingException, Exception
    {
        Stack<EClass> parentEClassifierStack = 
            EcoreTypeSystemHelper.
            loadParentEClassifierStack( 
                eClass, 
                null);
        
        EClass parentEClass = null;
        
        for (int i=0; i<parentEClassifierStack.size(); i++)            
        {
            parentEClass = parentEClassifierStack.pop();
            
            EObjectClassCreator
            eObjectClassCreator =
                new EObjectClassCreator();
            
            eObjectClassCreator.create(
                dasModelMetaContext,
                attributeTypesContext,
                objectClassesContext,
                parentEClass, 
                null,
                oidPrefix);
        }
    }
    
    /**
     * Creates the attribute types.
     * 
     * @param oidPrefix the oid prefix
     * @param eClass the e class
     * @param attributeTypesContext the attribute types context
     * 
     * @throws Exception the exception
     * @throws NamingException the naming exception
     */
    public static void createAttributeTypes(
        DirContext attributeTypesContext,
        EClass eClass,
        String oidPrefix) 
    throws NamingException, Exception
    {
        List<EAttribute> eAttributes            = 
            eClass.getEAttributes();

        Iterator<EAttribute> eAttributeIterator = 
            eAttributes.iterator();
        
        while (eAttributeIterator.hasNext())
        {
            EAttribute eAttribute = eAttributeIterator.next();
            
            EAttributeTypeCreator
            eAttributeTypeCreator =
                new EAttributeTypeCreator();
            
            eAttributeTypeCreator.create( 
                attributeTypesContext, 
                eAttribute, 
                oidPrefix );
        }
        
        List<EReference> eReferences            = 
            eClass.getEReferences();

        Iterator<EReference> eReferenceIterator = 
            eReferences.iterator();
        
        while(eReferenceIterator.hasNext())
        {
            EReference eReference = 
                eReferenceIterator.
                next();
            
            EReferenceTypeCreator
            eReferenceTypeCreator =
                new EReferenceTypeCreator();
            
            eReferenceTypeCreator.create(
                attributeTypesContext,
                eReference,
                oidPrefix);
        }
    }
    
    /**
     * Destroy attribute types.
     * 
     * @param oidPrefix the oid prefix
     * @param eClass the e class
     * @param attributeTypesContext the attribute types context
     * 
     * @throws Exception the exception
     * @throws NamingException the naming exception
     */
    public static void destroyAttributeTypes(
        DirContext attributeTypesContext,
        EClass eClass,
        String oidPrefix) 
    throws NamingException, Exception
    {
        List<EStructuralFeature> 
        eStructuralFeatures                = 
            eClass.
            getEStructuralFeatures();

        Iterator<EStructuralFeature> 
        eStructuralFeatureIterator         = 
            eStructuralFeatures.iterator();
        
        while (eStructuralFeatureIterator.hasNext())
        {  
            EStructuralFeature 
            eStructuralFeature             = 
                eStructuralFeatureIterator.next();
            
            EStructuralFeatureTypeDestroyer.destroy( 
                attributeTypesContext, 
                eStructuralFeature, 
                oidPrefix );
        }
    }
    
    /**
     * Gets the E class parent.
     * 
     * @param eClass the e class
     * 
     * @return the e class parent
     */
    public static EClass getEClassParent(EClass eClass)
    {
        List<EClass> eClassParents   = 
            eClass.getESuperTypes();
        
        if (eClassParents.size() > 0)
        {
            Iterator<EClass> eClassParentIterator = 
                eClassParents.iterator();
            
            while(eClassParentIterator.hasNext())
            {
                EClass parentEClass = 
                    eClassParentIterator.next();
                
                if (!parentEClass.isInterface())
                {
                    return parentEClass;
                }
                else
                {
                    return null;
                }
            }
        }
        return null;
    }
}
