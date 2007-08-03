/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.tuscany.model.impl;

import org.apache.tuscany.model.Configuration;
import org.apache.tuscany.model.ConfigurationFactory;
import org.apache.tuscany.model.DASMeta;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.apache.tuscany.model.ConfigurationFactory
 * @model kind="package"
 * @generated
 */
public class ConfigurationPackageImpl extends EPackageImpl
{
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final String eNAME = "model";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final String eNS_URI = "http://org.apache.tuscany/das.ldap.configuration.model.ecore.v100";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final String eNS_PREFIX = "tuscany";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final ConfigurationPackageImpl eINSTANCE = org.apache.tuscany.model.impl.ConfigurationPackageImpl.init();

    /**
     * The meta object id for the '{@link org.apache.tuscany.model.impl.ConfigurationImpl <em>Configuration</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.apache.tuscany.model.impl.ConfigurationImpl
     * @see org.apache.tuscany.model.impl.ConfigurationPackageImpl#getConfiguration()
     * @generated
     */
    public static final int CONFIGURATION = 0;

    /**
     * The feature id for the '<em><b>Host</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__HOST = 0;

    /**
     * The feature id for the '<em><b>Port</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__PORT = 1;

    /**
     * The feature id for the '<em><b>Initial Context Factory</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__INITIAL_CONTEXT_FACTORY = 2;

    /**
     * The feature id for the '<em><b>Das Partition Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__DAS_PARTITION_NAME = 3;

    /**
     * The feature id for the '<em><b>Schema Partition Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__SCHEMA_PARTITION_NAME = 4;

    /**
     * The feature id for the '<em><b>Security Principal</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__SECURITY_PRINCIPAL = 5;

    /**
     * The feature id for the '<em><b>Security Authentication Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__SECURITY_AUTHENTICATION_TYPE = 6;

    /**
     * The feature id for the '<em><b>Security Credentials</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__SECURITY_CREDENTIALS = 7;

    /**
     * The feature id for the '<em><b>Nlog4j Configuration File Path</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__NLOG4J_CONFIGURATION_FILE_PATH = 8;

    /**
     * The feature id for the '<em><b>Embedded</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION__EMBEDDED = 9;

    /**
     * The number of structural features of the '<em>Configuration</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int CONFIGURATION_FEATURE_COUNT = 10;

    /**
     * The meta object id for the '{@link org.apache.tuscany.model.impl.DASMetaImpl <em>DAS Meta</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.apache.tuscany.model.impl.DASMetaImpl
     * @see org.apache.tuscany.model.impl.ConfigurationPackageImpl#getDASMeta()
     * @generated
     */
    public static final int DAS_META = 1;

    /**
     * The feature id for the '<em><b>Supported Schemas</b></em>' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int DAS_META__SUPPORTED_SCHEMAS = 0;

    /**
     * The feature id for the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int DAS_META__ID = 1;

    /**
     * The number of structural features of the '<em>DAS Meta</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    public static final int DAS_META_FEATURE_COUNT = 2;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass configurationEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dasMetaEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.apache.tuscany.model.impl.ConfigurationPackageImpl#eNS_URI
     * @see #init()
     * @generated
     */
    private ConfigurationPackageImpl()
    {
        super(eNS_URI, ((EFactory)ConfigurationFactory.INSTANCE));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this
     * model, and for any others upon which it depends.  Simple
     * dependencies are satisfied by calling this method on all
     * dependent packages before doing anything else.  This method drives
     * initialization for interdependent packages directly, in parallel
     * with this package, itself.
     * <p>Of this package and its interdependencies, all packages which
     * have not yet been registered by their URI values are first created
     * and registered.  The packages are then initialized in two steps:
     * meta-model objects for all of the packages are created before any
     * are initialized, since one package's meta-model objects may refer to
     * those of another.
     * <p>Invocation of this method will not affect any packages that have
     * already been initialized.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static ConfigurationPackageImpl init()
    {
        if (isInited) return (ConfigurationPackageImpl)EPackage.Registry.INSTANCE.getEPackage(ConfigurationPackageImpl.eNS_URI);

        // Obtain or create and register package
        ConfigurationPackageImpl theConfigurationPackageImpl = (ConfigurationPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof ConfigurationPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new ConfigurationPackageImpl());

        isInited = true;

        // Create package meta-data objects
        theConfigurationPackageImpl.createPackageContents();

        // Initialize created meta-data
        theConfigurationPackageImpl.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theConfigurationPackageImpl.freeze();

        return theConfigurationPackageImpl;
    }


    /**
     * Returns the meta object for class '{@link org.apache.tuscany.model.Configuration <em>Configuration</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Configuration</em>'.
     * @see org.apache.tuscany.model.Configuration
     * @generated
     */
    public EClass getConfiguration()
    {
        return configurationEClass;
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getHost <em>Host</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Host</em>'.
     * @see org.apache.tuscany.model.Configuration#getHost()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_Host()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(0);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getPort <em>Port</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Port</em>'.
     * @see org.apache.tuscany.model.Configuration#getPort()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_Port()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(1);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getInitialContextFactory <em>Initial Context Factory</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Initial Context Factory</em>'.
     * @see org.apache.tuscany.model.Configuration#getInitialContextFactory()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_InitialContextFactory()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(2);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getDasPartitionName <em>Das Partition Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Das Partition Name</em>'.
     * @see org.apache.tuscany.model.Configuration#getDasPartitionName()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_DasPartitionName()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(3);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getSchemaPartitionName <em>Schema Partition Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Schema Partition Name</em>'.
     * @see org.apache.tuscany.model.Configuration#getSchemaPartitionName()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_SchemaPartitionName()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(4);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getSecurityPrincipal <em>Security Principal</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Security Principal</em>'.
     * @see org.apache.tuscany.model.Configuration#getSecurityPrincipal()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_SecurityPrincipal()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(5);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getSecurityAuthenticationType <em>Security Authentication Type</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Security Authentication Type</em>'.
     * @see org.apache.tuscany.model.Configuration#getSecurityAuthenticationType()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_SecurityAuthenticationType()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(6);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getSecurityCredentials <em>Security Credentials</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Security Credentials</em>'.
     * @see org.apache.tuscany.model.Configuration#getSecurityCredentials()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_SecurityCredentials()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(7);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#getNlog4jConfigurationFilePath <em>Nlog4j Configuration File Path</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Nlog4j Configuration File Path</em>'.
     * @see org.apache.tuscany.model.Configuration#getNlog4jConfigurationFilePath()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_Nlog4jConfigurationFilePath()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(8);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.Configuration#isEmbedded <em>Embedded</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Embedded</em>'.
     * @see org.apache.tuscany.model.Configuration#isEmbedded()
     * @see #getConfiguration()
     * @generated
     */
    public EAttribute getConfiguration_Embedded()
    {
        return (EAttribute)configurationEClass.getEStructuralFeatures().get(9);
    }

    /**
     * Returns the meta object for class '{@link org.apache.tuscany.model.DASMeta <em>DAS Meta</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>DAS Meta</em>'.
     * @see org.apache.tuscany.model.DASMeta
     * @generated
     */
    public EClass getDASMeta()
    {
        return dasMetaEClass;
    }

    /**
     * Returns the meta object for the attribute list '{@link org.apache.tuscany.model.DASMeta#getSupportedSchemas <em>Supported Schemas</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute list '<em>Supported Schemas</em>'.
     * @see org.apache.tuscany.model.DASMeta#getSupportedSchemas()
     * @see #getDASMeta()
     * @generated
     */
    public EAttribute getDASMeta_SupportedSchemas()
    {
        return (EAttribute)dasMetaEClass.getEStructuralFeatures().get(0);
    }

    /**
     * Returns the meta object for the attribute '{@link org.apache.tuscany.model.DASMeta#getId <em>Id</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Id</em>'.
     * @see org.apache.tuscany.model.DASMeta#getId()
     * @see #getDASMeta()
     * @generated
     */
    public EAttribute getDASMeta_Id()
    {
        return (EAttribute)dasMetaEClass.getEStructuralFeatures().get(1);
    }

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    public ConfigurationFactory getConfigurationFactory()
    {
        return (ConfigurationFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents()
    {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        configurationEClass = createEClass(CONFIGURATION);
        createEAttribute(configurationEClass, CONFIGURATION__HOST);
        createEAttribute(configurationEClass, CONFIGURATION__PORT);
        createEAttribute(configurationEClass, CONFIGURATION__INITIAL_CONTEXT_FACTORY);
        createEAttribute(configurationEClass, CONFIGURATION__DAS_PARTITION_NAME);
        createEAttribute(configurationEClass, CONFIGURATION__SCHEMA_PARTITION_NAME);
        createEAttribute(configurationEClass, CONFIGURATION__SECURITY_PRINCIPAL);
        createEAttribute(configurationEClass, CONFIGURATION__SECURITY_AUTHENTICATION_TYPE);
        createEAttribute(configurationEClass, CONFIGURATION__SECURITY_CREDENTIALS);
        createEAttribute(configurationEClass, CONFIGURATION__NLOG4J_CONFIGURATION_FILE_PATH);
        createEAttribute(configurationEClass, CONFIGURATION__EMBEDDED);

        dasMetaEClass = createEClass(DAS_META);
        createEAttribute(dasMetaEClass, DAS_META__SUPPORTED_SCHEMAS);
        createEAttribute(dasMetaEClass, DAS_META__ID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents()
    {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes

        // Initialize classes and features; add operations and parameters
        initEClass(configurationEClass, Configuration.class, "Configuration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getConfiguration_Host(), ecorePackage.getEString(), "host", "localhost", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_Port(), ecorePackage.getEString(), "port", "10389", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_InitialContextFactory(), ecorePackage.getEString(), "initialContextFactory", "org.apache.directory.server.core.jndi.CoreContextFactory", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_DasPartitionName(), ecorePackage.getEString(), "dasPartitionName", "das", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_SchemaPartitionName(), ecorePackage.getEString(), "schemaPartitionName", "schema", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_SecurityPrincipal(), ecorePackage.getEString(), "securityPrincipal", "uid=admin,ou=system", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_SecurityAuthenticationType(), ecorePackage.getEString(), "securityAuthenticationType", "simple", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_SecurityCredentials(), ecorePackage.getEString(), "securityCredentials", "secret", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_Nlog4jConfigurationFilePath(), ecorePackage.getEString(), "nlog4jConfigurationFilePath", "src/test/resources/log4j.properties", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getConfiguration_Embedded(), ecorePackage.getEBoolean(), "embedded", "false", 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(dasMetaEClass, DASMeta.class, "DASMeta", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDASMeta_SupportedSchemas(), ecorePackage.getEString(), "supportedSchemas", "localhost", 0, -1, DASMeta.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getDASMeta_Id(), ecorePackage.getEString(), "id", "0", 0, 1, DASMeta.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Create resource
        createResource(eNS_URI);
    }

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    public interface Literals
    {
        /**
         * The meta object literal for the '{@link org.apache.tuscany.model.impl.ConfigurationImpl <em>Configuration</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.apache.tuscany.model.impl.ConfigurationImpl
         * @see org.apache.tuscany.model.impl.ConfigurationPackageImpl#getConfiguration()
         * @generated
         */
        public static final EClass CONFIGURATION = eINSTANCE.getConfiguration();

        /**
         * The meta object literal for the '<em><b>Host</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__HOST = eINSTANCE.getConfiguration_Host();

        /**
         * The meta object literal for the '<em><b>Port</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__PORT = eINSTANCE.getConfiguration_Port();

        /**
         * The meta object literal for the '<em><b>Initial Context Factory</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__INITIAL_CONTEXT_FACTORY = eINSTANCE.getConfiguration_InitialContextFactory();

        /**
         * The meta object literal for the '<em><b>Das Partition Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__DAS_PARTITION_NAME = eINSTANCE.getConfiguration_DasPartitionName();

        /**
         * The meta object literal for the '<em><b>Schema Partition Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__SCHEMA_PARTITION_NAME = eINSTANCE.getConfiguration_SchemaPartitionName();

        /**
         * The meta object literal for the '<em><b>Security Principal</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__SECURITY_PRINCIPAL = eINSTANCE.getConfiguration_SecurityPrincipal();

        /**
         * The meta object literal for the '<em><b>Security Authentication Type</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__SECURITY_AUTHENTICATION_TYPE = eINSTANCE.getConfiguration_SecurityAuthenticationType();

        /**
         * The meta object literal for the '<em><b>Security Credentials</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__SECURITY_CREDENTIALS = eINSTANCE.getConfiguration_SecurityCredentials();

        /**
         * The meta object literal for the '<em><b>Nlog4j Configuration File Path</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__NLOG4J_CONFIGURATION_FILE_PATH = eINSTANCE.getConfiguration_Nlog4jConfigurationFilePath();

        /**
         * The meta object literal for the '<em><b>Embedded</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute CONFIGURATION__EMBEDDED = eINSTANCE.getConfiguration_Embedded();

        /**
         * The meta object literal for the '{@link org.apache.tuscany.model.impl.DASMetaImpl <em>DAS Meta</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.apache.tuscany.model.impl.DASMetaImpl
         * @see org.apache.tuscany.model.impl.ConfigurationPackageImpl#getDASMeta()
         * @generated
         */
        public static final EClass DAS_META = eINSTANCE.getDASMeta();

        /**
         * The meta object literal for the '<em><b>Supported Schemas</b></em>' attribute list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute DAS_META__SUPPORTED_SCHEMAS = eINSTANCE.getDASMeta_SupportedSchemas();

        /**
         * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        public static final EAttribute DAS_META__ID = eINSTANCE.getDASMeta_Id();

    }

} //ConfigurationPackageImpl
