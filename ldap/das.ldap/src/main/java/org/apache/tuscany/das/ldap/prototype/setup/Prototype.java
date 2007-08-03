package org.apache.tuscany.das.ldap.prototype.setup;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;

public interface Prototype {
	EClass userEClass                      = 
		PrototypeUtilities.createUserEClass();
	EClass authorizationEClass             = 
		PrototypeUtilities.createAuthorizationEClass();
	EClass configurationEClass             =
		PrototypeUtilities.createConfigurationEClass();
	
	EAttribute userNameEAttribute          = 
		PrototypeUtilities.createUserNameEAttribute();
	EAttribute userAliasesEAttribute       = 
        PrototypeUtilities.createUserAliasesEAttribute();
    
	EAttribute userPasswordEAttribute      = 
		PrototypeUtilities.createUserPasswordEAttribute();
	EAttribute userIDEAttribute            =
		PrototypeUtilities.createUserIDEAttribute();
	EAttribute userAgeEAttribute           =
		PrototypeUtilities.createUserAgeEAttribute();
	EAttribute userHeightEAttribute        =
		PrototypeUtilities.createUserHeightEAttribute();
	EAttribute userIsMaleEAttribute        =
		PrototypeUtilities.createUserIsMaleEAttribute();
	EReference userAuthorizationEReference = 
		PrototypeUtilities.createUserAuthorizationEReference();
	
	EReference userConfigurationEReference = 
		PrototypeUtilities.createUserConfigurationEReference();
	

	
	
	EAttribute authorizationFileEAttribute = 
		PrototypeUtilities.createFileEAttribute();
	EAttribute authorizationReadEAttribute = 
		PrototypeUtilities.createReadAuthorizationEAttribute();
	EAttribute authorizationWriteEAttribute= 
		PrototypeUtilities.createWriteAuthorizationEAttribute();
	EAttribute authorizationExecuteEAttribute              = 
		PrototypeUtilities.createExecuteAuthorizationEAttribute();
	EAttribute authorizationIDEAttribute                        = 
		PrototypeUtilities.createAuthorizationIDEAttribute();
	
	EAttribute configurationIDEAttribute                        =
		PrototypeUtilities.createConfigurationIDEAttribute();
	
	EReference configurationAuthorizationEReference =
		PrototypeUtilities.createConfigurationAuthorizationEReference();
	
	EReference configurationAuthorizationsEReference =
		PrototypeUtilities.createConfigurationAuthorizationsEReference();
	
	
	EPackage  userEPackage                     = 
		PrototypeUtilities.createUserEPackage();
	
	EFactory userEFactory                        =
		userEPackage.getEFactoryInstance();
	
    EcoreFactory ecoreFactory                 = 
    	EcoreFactory.eINSTANCE;
    EcorePackage ecorePackage               = 
    	EcorePackage.eINSTANCE;
}
