package org.apache.tuscany.das.ldap.schema.emf.create;

import java.util.HashMap;
import java.util.Map;

import org.apache.tuscany.das.ldap.constants.SyntaxOIDValues;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcorePackage;

public class DataTypeToADSSyntaxMapProvider 
implements SyntaxOIDValues {
	
	EcorePackage ecorePackage             = 
		EcorePackage.eINSTANCE;
	
	private static Map<EDataType, String> dataTypeToADSSyntaxOIDMap = null;
	
	//TODO Make this a singleton
	public DataTypeToADSSyntaxMapProvider()
	{
		dataTypeToADSSyntaxOIDMap = new HashMap<EDataType, String>();
		create();
	}
	
	public Map<EDataType, String> getDataTypeToADSSyntaxMap()
	{
		return dataTypeToADSSyntaxOIDMap;
	}
	
	private Map<EDataType, String> create()
	{
		dataTypeToADSSyntaxOIDMap.put(
				ecorePackage.getEString(), 
				SYNTAX_STRING_OID_VALUE);
		
		dataTypeToADSSyntaxOIDMap.put(
				ecorePackage.getEInt(), 
				SYNTAX_INTEGER_OID_VALUE);

		dataTypeToADSSyntaxOIDMap.put(
				ecorePackage.getEIntegerObject(), 
				SYNTAX_INTEGER_OID_VALUE);

		dataTypeToADSSyntaxOIDMap.put(
				ecorePackage.getEBoolean(), 
				SYNTAX_BOOLEAN_OID_VALUE);

		dataTypeToADSSyntaxOIDMap.put(
				ecorePackage.getEBooleanObject(), 
				SYNTAX_BOOLEAN_OID_VALUE);

		return dataTypeToADSSyntaxOIDMap;
	}
}