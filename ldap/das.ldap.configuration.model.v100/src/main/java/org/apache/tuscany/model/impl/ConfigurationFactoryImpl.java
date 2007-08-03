/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.tuscany.model.impl;

import org.apache.tuscany.model.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigurationFactoryImpl extends EFactoryImpl implements ConfigurationFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final ConfigurationFactoryImpl eINSTANCE = init();

    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static ConfigurationFactoryImpl init()
    {
        try
        {
            ConfigurationFactoryImpl theConfigurationFactory = (ConfigurationFactoryImpl)EPackage.Registry.INSTANCE.getEFactory("http://org.apache.tuscany/das.ldap.configuration.model.ecore.v100"); 
            if (theConfigurationFactory != null)
            {
                return theConfigurationFactory;
            }
        }
        catch (Exception exception)
        {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new ConfigurationFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConfigurationFactoryImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass)
    {
        switch (eClass.getClassifierID())
        {
            case ConfigurationPackageImpl.CONFIGURATION: return (EObject)createConfiguration();
            case ConfigurationPackageImpl.DAS_META: return (EObject)createDASMeta();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Configuration createConfiguration()
    {
        ConfigurationImpl configuration = new ConfigurationImpl();
        return configuration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DASMeta createDASMeta()
    {
        DASMetaImpl dasMeta = new DASMetaImpl();
        return dasMeta;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ConfigurationPackageImpl getConfigurationPackageImpl()
    {
        return (ConfigurationPackageImpl)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static ConfigurationPackageImpl getPackage()
    {
        return ConfigurationPackageImpl.eINSTANCE;
    }

} //ConfigurationFactoryImpl
