package org.apache.tuscany.das.ldap.prototype.setup;

import org.apache.tuscany.das.ldap.testing.constants.DASTestingConstants;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.sdo.impl.DynamicEDataObjectImpl;

public class PrototypeUtilities 
implements DASTestingConstants {

	/*
	 * The User EClass is intended to be the root of the DataGraph
	 * It should have a multiplicity many containment reference of type 
	 * Authorization and a containment reference of type Configuration
	 * The Configuration should have a single non-containment reference
	 * of type Authorization.  
	 */

    public static EClass createUserEClass()
    {
        EClass userEClass                                     = 
        	EcoreFactory.eINSTANCE.createEClass();
        
        userEClass.setName("User");
        
        return userEClass;
    }
    
    public static EAttribute createUserIDEAttribute()
    {
        EAttribute id                         = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        id.setName( "id" );
        id.setEType( EcorePackage.eINSTANCE.getEString() );
        id.setID(true);
        return id;
    }

    public static EAttribute createUserNameEAttribute()
    {
        EAttribute userName                         = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        userName.setName( "userName" );
        userName.setEType( EcorePackage.eINSTANCE.getEString() );
        return userName;
    }

    public static EAttribute createUserAliasesEAttribute()
    {
        EAttribute userAliases                         = 
            EcoreFactory.eINSTANCE.createEAttribute();
        userAliases.setName( "userAliases" );
        userAliases.setLowerBound( 0 );
        userAliases.setUpperBound( -1 );
        userAliases.setEType( EcorePackage.eINSTANCE.getEString() );
        return userAliases;
    }

    public static EAttribute createUserPasswordEAttribute()
    {
        EAttribute userPassword                    = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        userPassword.setName( "userPassword" );
        userPassword.setEType( EcorePackage.eINSTANCE.getEString() );
        return userPassword;
    }
    
    public static EAttribute createUserAgeEAttribute()
    {
        EAttribute userAge                         = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        userAge.setName( "userAge" );
        userAge.setEType( EcorePackage.eINSTANCE.getEInt() );
        return userAge;
    }

    public static EAttribute createUserHeightEAttribute()
    {
        EAttribute userHeight                         = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        userHeight.setName( "userHeight" );
        userHeight.setEType( EcorePackage.eINSTANCE.getEDouble() );
        return userHeight;
    }

    public static EAttribute createUserIsMaleEAttribute()
    {
        EAttribute isMale                         = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        isMale.setName( "isMale" );
        isMale.setEType( EcorePackage.eINSTANCE.getEBoolean() );
        return isMale;
    }
    
    public static EReference createUserAuthorizationEReference()
    {
    	EReference userAuthorizationEReference =
    		EcoreFactory.eINSTANCE.createEReference();
    	userAuthorizationEReference.setLowerBound(0);
    	userAuthorizationEReference.setUpperBound(ETypedElement.UNBOUNDED_MULTIPLICITY);
    	userAuthorizationEReference.setContainment(true);
    	userAuthorizationEReference.setName("authorizationList");
    	
    	return userAuthorizationEReference;
    }

    public static EReference createUserConfigurationEReference()
    {
    	EReference userConfigurationEReference =
    		EcoreFactory.eINSTANCE.createEReference();
    	userConfigurationEReference.setLowerBound(0);
    	userConfigurationEReference.setUpperBound(1);
    	userConfigurationEReference.setContainment(true);
    	userConfigurationEReference.setName("configuration");
    	
    	return userConfigurationEReference;
    }

    
    public static EPackage createUserEPackage()
    {
        EPackage userEPackage                     = 
        	EcoreFactory.eINSTANCE.createEPackage();
        userEPackage.setName( "userPackage" );
        userEPackage.setNsPrefix( "user" );
        userEPackage.setNsURI( xsdNamespace );
        
        userEPackage.setEFactoryInstance(
            new DynamicEDataObjectImpl.FactoryImpl());
        
        return userEPackage;
    }
    
    public static EClass createAuthorizationEClass()
    {
        EClass authorizationEClass         = 
        	EcoreFactory.eINSTANCE.createEClass();
        
        authorizationEClass.setName("Authorization");

        return authorizationEClass;
    }
    
    public static EAttribute createAuthorizationIDEAttribute()
    {
        EAttribute id                         = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        id.setName( "id" );
        id.setEType( EcorePackage.eINSTANCE.getEString() );
        id.setID(true);
        return id;
    }
    
    public static EAttribute createFileEAttribute()
    {
        EAttribute fileNameEAttribute     = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        fileNameEAttribute.setName( "fileName" );
        fileNameEAttribute.setEType( EcorePackage.eINSTANCE.getEString() );
        return fileNameEAttribute;
        
    }
    
    public static EAttribute createWriteAuthorizationEAttribute()
    {
        EAttribute writeAuthorizationEAttribute  = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        writeAuthorizationEAttribute.setName( "writeAuthorization" );
        writeAuthorizationEAttribute.setEType( EcorePackage.eINSTANCE.getEBoolean() );
        return writeAuthorizationEAttribute;
    }

    public static EAttribute createReadAuthorizationEAttribute()
    {
        EAttribute readAuthorizationEAttribute  = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        readAuthorizationEAttribute.setName( "readAuthorization" );
        readAuthorizationEAttribute.setEType( EcorePackage.eINSTANCE.getEBoolean() );
        return readAuthorizationEAttribute;
    }

    public static EAttribute createExecuteAuthorizationEAttribute()
    {
        EAttribute readExecuteEAttribute  = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        readExecuteEAttribute.setName( "executeAuthorization" );
        readExecuteEAttribute.setEType( EcorePackage.eINSTANCE.getEBoolean() );
        return readExecuteEAttribute;
    }

    
    public static EClass createConfigurationEClass()
    {
        EClass configurationEClass                                     = 
        	EcoreFactory.eINSTANCE.createEClass();
        
        configurationEClass.setName("Configuration");
        
        return configurationEClass;
    }
    
    public static EAttribute createConfigurationIDEAttribute()
    {
        EAttribute id                         = 
        	EcoreFactory.eINSTANCE.createEAttribute();
        id.setName( "id" );
        id.setEType( EcorePackage.eINSTANCE.getEString() );
        id.setID(true);
        return id;
    }
    
    public static EReference createConfigurationAuthorizationEReference()
    {
    	EReference authorizationEReference =
    		EcoreFactory.eINSTANCE.createEReference();
    	authorizationEReference.setLowerBound(0);
    	authorizationEReference.setUpperBound(1);
    	authorizationEReference.setContainment(false);
    	//authorizationEReference.setEType(createAuthorizationEClass()); Do this in the testing code.
    	authorizationEReference.setName("authorization");
    	
    	return authorizationEReference;
    }
    
    public static EReference createConfigurationAuthorizationsEReference()
    {
    	EReference authorizationsEReference =
    		EcoreFactory.eINSTANCE.createEReference();
    	authorizationsEReference.setLowerBound(0);
    	authorizationsEReference.setUpperBound(ETypedElement.UNBOUNDED_MULTIPLICITY);
    	authorizationsEReference.setContainment(false);
    	//authorizationEReference.setEType(createAuthorizationEClass()); Do this in the testing code.
    	authorizationsEReference.setName("authorizations");
    	
    	return authorizationsEReference;
    }

}
