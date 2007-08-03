package org.apache.tuscany.das.ldap.schema.emf.provide;

import java.util.HashMap;
import java.util.Map;

import org.apache.tuscany.das.ldap.constants.SyntaxOIDValues;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EcorePackage;

public class SyntaxOIDProvider 
implements SyntaxOIDValues {

	private static Map<EClassifier, String> dataTypeToADSSyntaxOIDMap = null;

	private EcorePackage ecorePackage   = EcorePackage.eINSTANCE;
	
	//TODO Make this a singleton
	public SyntaxOIDProvider()
	{
		dataTypeToADSSyntaxOIDMap            = 
			new HashMap<EClassifier, String>();
		
		this.create();
		ecorePackage                                       = 
			EcorePackage.eINSTANCE;
	}
	
	public String getSyntaxOID(EClassifier eClassifier)
	{
		String syntaxOID = dataTypeToADSSyntaxOIDMap.get(eClassifier);
		if (syntaxOID == null)
		{
			return SYNTAX_STRING_OID_VALUE;
		}
		return syntaxOID;
	}
	
	private Map<EClassifier, String> create()
	{
		dataTypeToADSSyntaxOIDMap.put(
				EcorePackage.eINSTANCE.getEString(), 
				SYNTAX_STRING_OID_VALUE);
		
		dataTypeToADSSyntaxOIDMap.put(
				EcorePackage.eINSTANCE.getEInt(), 
				SYNTAX_INTEGER_OID_VALUE);

		dataTypeToADSSyntaxOIDMap.put(
				EcorePackage.eINSTANCE.getEIntegerObject(), 
				SYNTAX_INTEGER_OID_VALUE);

		dataTypeToADSSyntaxOIDMap.put(
				EcorePackage.eINSTANCE.getEBoolean(), 
				SYNTAX_BOOLEAN_OID_VALUE);

		dataTypeToADSSyntaxOIDMap.put(
				EcorePackage.eINSTANCE.getEBooleanObject(), 
				SYNTAX_BOOLEAN_OID_VALUE);

		return dataTypeToADSSyntaxOIDMap;
	}
}