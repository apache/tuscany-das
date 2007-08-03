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
package org.apache.tuscany.das.ldap.emf.read;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.tuscany.das.ldap.constants.DASConstants;
import org.apache.tuscany.das.ldap.util.QualifiedNameNormalizer;
import org.apache.tuscany.das.ldap.util.SimpleTypeNamespaceQualifier;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.sdo.EDataObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class EDataObjectReaderHelper
implements DASConstants
{
    public static void restoreEAttributes(
        EClass eClass,
        EDataObject eDataObject,
        String namespaceURI,
        Attributes attributes) 
    throws NamingException
    {
    	List<EAttribute> eAttributes        = 
            eClass.getEAllAttributes();

    	for (EAttribute eAttribute : eAttributes)
    	{
            String qualifiedEAttributeName  =
                SimpleTypeNamespaceQualifier.
                qualify( 
                    namespaceURI, 
                    eClass.getName(), 
                    eAttribute.getName() );
            
        	String normalizedEAttributeName =
            	QualifiedNameNormalizer.
            	normalize(qualifiedEAttributeName);
        	
        	if (eAttribute.isMany())
        	{
        	    EList<Object> values = 
        	        new BasicEList<Object>();
        	    
        	    Attribute attribute  = 
        	        attributes.
        	        get(normalizedEAttributeName);

        	    for (int i = 0; i < attribute.size(); i++)
        	    {
        	        String value = (String) attribute.get(i);
        	        
        	        values.add(EcoreUtil.createFromString(
                        eAttribute.getEAttributeType(), 
                        value));
        	    }
        	    
                eDataObject.eSet(
                    eAttribute, values);
        	}
        	else
        	{
                String value          = 
                    (String) 
                    attributes.
                    get(normalizedEAttributeName).
                    get();
                
                eDataObject.eSet(
                        eAttribute, 
                        EcoreUtil.createFromString(
                                eAttribute.getEAttributeType(), 
                                value));
        	}
    	}
    }

    
    //TODO Test all of these verifying that we get out what we put in
    //TODO Move to helper
    //TODO Note that we are using         	eDataObject.eSet(eAttribute, EcoreUtil.createFromString(eAttribute.getEAttributeType(), value)); instead
    /*
    public static void setEDataObjectFeature(
    		EDataObject eDataObject,
    		String value,
    		EAttribute eAttribute)
    {
    	if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEString())
    	{
    		eDataObject.eSet(eAttribute, value);
    	}
    	else if (
    			eAttribute.getEType() == EcorePackage.eINSTANCE.getEInt() ||
    			eAttribute.getEType() == EcorePackage.eINSTANCE.getEIntegerObject())
    	{
    		eDataObject.eSet(eAttribute, new Integer(value));
    	}
    	else if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEBoolean() ||
    			eAttribute.getEType() == EcorePackage.eINSTANCE.getEBooleanObject())
    	{
    		eDataObject.eSet(eAttribute, new Boolean(value));
    	}
    	else if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEFloat() ||
    			eAttribute.getEType() == EcorePackage.eINSTANCE.getEFloatObject())
    	{
    		eDataObject.eSet(eAttribute, new Float(value));
    	}
    	else if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEDouble() ||
    			eAttribute.getEType() == EcorePackage.eINSTANCE.getEDoubleObject())
    	{
    		eDataObject.eSet(eAttribute, new Double(value));
    	}
    	else if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEBigDecimal()) 
    	{
    		eDataObject.eSet(eAttribute, new BigDecimal(value));
    	}
    	else if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEBigInteger() )
    	{
    		eDataObject.eSet(eAttribute, new BigInteger(value));
    	}
    	else if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEByte() ||
    			eAttribute.getEType() == EcorePackage.eINSTANCE.getEByteObject())
    	{
    		eDataObject.eSet(eAttribute, new Byte(value));
    	}
    	else if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEByte() ||
    			eAttribute.getEType() == EcorePackage.eINSTANCE.getEByteObject())
    	{
    		eDataObject.eSet(eAttribute, new Byte(value));
    	}
      	else if (eAttribute.getEType() == EcorePackage.eINSTANCE.getEByteArray())
    	{
      		throw new RuntimeException("Sorry - ByteArrays are not supported.");
    	}
    }
    */
}