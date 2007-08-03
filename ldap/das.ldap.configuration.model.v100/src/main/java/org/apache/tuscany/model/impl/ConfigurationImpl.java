/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.tuscany.model.impl;

import org.apache.tuscany.model.Configuration;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getHost <em>Host</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getPort <em>Port</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getInitialContextFactory <em>Initial Context Factory</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getDasPartitionName <em>Das Partition Name</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getSchemaPartitionName <em>Schema Partition Name</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getSecurityPrincipal <em>Security Principal</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getSecurityAuthenticationType <em>Security Authentication Type</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getSecurityCredentials <em>Security Credentials</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#getNlog4jConfigurationFilePath <em>Nlog4j Configuration File Path</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.ConfigurationImpl#isEmbedded <em>Embedded</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConfigurationImpl extends EDataObjectImpl implements Configuration
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * The default value of the '{@link #getHost() <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHost()
     * @generated
     * @ordered
     */
    protected static final String HOST_EDEFAULT = "localhost";

    /**
     * The cached value of the '{@link #getHost() <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getHost()
     * @generated
     * @ordered
     */
    protected String host = HOST_EDEFAULT;

    /**
     * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPort()
     * @generated
     * @ordered
     */
    protected static final String PORT_EDEFAULT = "10389";

    /**
     * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPort()
     * @generated
     * @ordered
     */
    protected String port = PORT_EDEFAULT;

    /**
     * The default value of the '{@link #getInitialContextFactory() <em>Initial Context Factory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInitialContextFactory()
     * @generated
     * @ordered
     */
    protected static final String INITIAL_CONTEXT_FACTORY_EDEFAULT = "org.apache.directory.server.core.jndi.CoreContextFactory";

    /**
     * The cached value of the '{@link #getInitialContextFactory() <em>Initial Context Factory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInitialContextFactory()
     * @generated
     * @ordered
     */
    protected String initialContextFactory = INITIAL_CONTEXT_FACTORY_EDEFAULT;

    /**
     * The default value of the '{@link #getDasPartitionName() <em>Das Partition Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDasPartitionName()
     * @generated
     * @ordered
     */
    protected static final String DAS_PARTITION_NAME_EDEFAULT = "das";

    /**
     * The cached value of the '{@link #getDasPartitionName() <em>Das Partition Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDasPartitionName()
     * @generated
     * @ordered
     */
    protected String dasPartitionName = DAS_PARTITION_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getSchemaPartitionName() <em>Schema Partition Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchemaPartitionName()
     * @generated
     * @ordered
     */
    protected static final String SCHEMA_PARTITION_NAME_EDEFAULT = "schema";

    /**
     * The cached value of the '{@link #getSchemaPartitionName() <em>Schema Partition Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSchemaPartitionName()
     * @generated
     * @ordered
     */
    protected String schemaPartitionName = SCHEMA_PARTITION_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getSecurityPrincipal() <em>Security Principal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSecurityPrincipal()
     * @generated
     * @ordered
     */
    protected static final String SECURITY_PRINCIPAL_EDEFAULT = "uid=admin,ou=system";

    /**
     * The cached value of the '{@link #getSecurityPrincipal() <em>Security Principal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSecurityPrincipal()
     * @generated
     * @ordered
     */
    protected String securityPrincipal = SECURITY_PRINCIPAL_EDEFAULT;

    /**
     * The default value of the '{@link #getSecurityAuthenticationType() <em>Security Authentication Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSecurityAuthenticationType()
     * @generated
     * @ordered
     */
    protected static final String SECURITY_AUTHENTICATION_TYPE_EDEFAULT = "simple";

    /**
     * The cached value of the '{@link #getSecurityAuthenticationType() <em>Security Authentication Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSecurityAuthenticationType()
     * @generated
     * @ordered
     */
    protected String securityAuthenticationType = SECURITY_AUTHENTICATION_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getSecurityCredentials() <em>Security Credentials</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSecurityCredentials()
     * @generated
     * @ordered
     */
    protected static final String SECURITY_CREDENTIALS_EDEFAULT = "secret";

    /**
     * The cached value of the '{@link #getSecurityCredentials() <em>Security Credentials</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSecurityCredentials()
     * @generated
     * @ordered
     */
    protected String securityCredentials = SECURITY_CREDENTIALS_EDEFAULT;

    /**
     * The default value of the '{@link #getNlog4jConfigurationFilePath() <em>Nlog4j Configuration File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNlog4jConfigurationFilePath()
     * @generated
     * @ordered
     */
    protected static final String NLOG4J_CONFIGURATION_FILE_PATH_EDEFAULT = "src/test/resources/log4j.properties";

    /**
     * The cached value of the '{@link #getNlog4jConfigurationFilePath() <em>Nlog4j Configuration File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNlog4jConfigurationFilePath()
     * @generated
     * @ordered
     */
    protected String nlog4jConfigurationFilePath = NLOG4J_CONFIGURATION_FILE_PATH_EDEFAULT;

    /**
     * The default value of the '{@link #isEmbedded() <em>Embedded</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isEmbedded()
     * @generated
     * @ordered
     */
    protected static final boolean EMBEDDED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isEmbedded() <em>Embedded</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isEmbedded()
     * @generated
     * @ordered
     */
    protected boolean embedded = EMBEDDED_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ConfigurationImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass()
    {
        return ConfigurationPackageImpl.Literals.CONFIGURATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getHost()
    {
        return host;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setHost(String newHost)
    {
        String oldHost = host;
        host = newHost;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__HOST, oldHost, host));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPort()
    {
        return port;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPort(String newPort)
    {
        String oldPort = port;
        port = newPort;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__PORT, oldPort, port));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInitialContextFactory()
    {
        return initialContextFactory;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInitialContextFactory(String newInitialContextFactory)
    {
        String oldInitialContextFactory = initialContextFactory;
        initialContextFactory = newInitialContextFactory;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__INITIAL_CONTEXT_FACTORY, oldInitialContextFactory, initialContextFactory));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDasPartitionName()
    {
        return dasPartitionName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDasPartitionName(String newDasPartitionName)
    {
        String oldDasPartitionName = dasPartitionName;
        dasPartitionName = newDasPartitionName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__DAS_PARTITION_NAME, oldDasPartitionName, dasPartitionName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSchemaPartitionName()
    {
        return schemaPartitionName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSchemaPartitionName(String newSchemaPartitionName)
    {
        String oldSchemaPartitionName = schemaPartitionName;
        schemaPartitionName = newSchemaPartitionName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__SCHEMA_PARTITION_NAME, oldSchemaPartitionName, schemaPartitionName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSecurityPrincipal()
    {
        return securityPrincipal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSecurityPrincipal(String newSecurityPrincipal)
    {
        String oldSecurityPrincipal = securityPrincipal;
        securityPrincipal = newSecurityPrincipal;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__SECURITY_PRINCIPAL, oldSecurityPrincipal, securityPrincipal));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSecurityAuthenticationType()
    {
        return securityAuthenticationType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSecurityAuthenticationType(String newSecurityAuthenticationType)
    {
        String oldSecurityAuthenticationType = securityAuthenticationType;
        securityAuthenticationType = newSecurityAuthenticationType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__SECURITY_AUTHENTICATION_TYPE, oldSecurityAuthenticationType, securityAuthenticationType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSecurityCredentials()
    {
        return securityCredentials;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSecurityCredentials(String newSecurityCredentials)
    {
        String oldSecurityCredentials = securityCredentials;
        securityCredentials = newSecurityCredentials;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__SECURITY_CREDENTIALS, oldSecurityCredentials, securityCredentials));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getNlog4jConfigurationFilePath()
    {
        return nlog4jConfigurationFilePath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNlog4jConfigurationFilePath(String newNlog4jConfigurationFilePath)
    {
        String oldNlog4jConfigurationFilePath = nlog4jConfigurationFilePath;
        nlog4jConfigurationFilePath = newNlog4jConfigurationFilePath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__NLOG4J_CONFIGURATION_FILE_PATH, oldNlog4jConfigurationFilePath, nlog4jConfigurationFilePath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isEmbedded()
    {
        return embedded;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEmbedded(boolean newEmbedded)
    {
        boolean oldEmbedded = embedded;
        embedded = newEmbedded;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.CONFIGURATION__EMBEDDED, oldEmbedded, embedded));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType)
    {
        switch (featureID)
        {
            case ConfigurationPackageImpl.CONFIGURATION__HOST:
                return getHost();
            case ConfigurationPackageImpl.CONFIGURATION__PORT:
                return getPort();
            case ConfigurationPackageImpl.CONFIGURATION__INITIAL_CONTEXT_FACTORY:
                return getInitialContextFactory();
            case ConfigurationPackageImpl.CONFIGURATION__DAS_PARTITION_NAME:
                return getDasPartitionName();
            case ConfigurationPackageImpl.CONFIGURATION__SCHEMA_PARTITION_NAME:
                return getSchemaPartitionName();
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_PRINCIPAL:
                return getSecurityPrincipal();
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_AUTHENTICATION_TYPE:
                return getSecurityAuthenticationType();
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_CREDENTIALS:
                return getSecurityCredentials();
            case ConfigurationPackageImpl.CONFIGURATION__NLOG4J_CONFIGURATION_FILE_PATH:
                return getNlog4jConfigurationFilePath();
            case ConfigurationPackageImpl.CONFIGURATION__EMBEDDED:
                return isEmbedded() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case ConfigurationPackageImpl.CONFIGURATION__HOST:
                setHost((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__PORT:
                setPort((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__INITIAL_CONTEXT_FACTORY:
                setInitialContextFactory((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__DAS_PARTITION_NAME:
                setDasPartitionName((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__SCHEMA_PARTITION_NAME:
                setSchemaPartitionName((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_PRINCIPAL:
                setSecurityPrincipal((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_AUTHENTICATION_TYPE:
                setSecurityAuthenticationType((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_CREDENTIALS:
                setSecurityCredentials((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__NLOG4J_CONFIGURATION_FILE_PATH:
                setNlog4jConfigurationFilePath((String)newValue);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__EMBEDDED:
                setEmbedded(((Boolean)newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID)
    {
        switch (featureID)
        {
            case ConfigurationPackageImpl.CONFIGURATION__HOST:
                setHost(HOST_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__PORT:
                setPort(PORT_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__INITIAL_CONTEXT_FACTORY:
                setInitialContextFactory(INITIAL_CONTEXT_FACTORY_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__DAS_PARTITION_NAME:
                setDasPartitionName(DAS_PARTITION_NAME_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__SCHEMA_PARTITION_NAME:
                setSchemaPartitionName(SCHEMA_PARTITION_NAME_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_PRINCIPAL:
                setSecurityPrincipal(SECURITY_PRINCIPAL_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_AUTHENTICATION_TYPE:
                setSecurityAuthenticationType(SECURITY_AUTHENTICATION_TYPE_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_CREDENTIALS:
                setSecurityCredentials(SECURITY_CREDENTIALS_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__NLOG4J_CONFIGURATION_FILE_PATH:
                setNlog4jConfigurationFilePath(NLOG4J_CONFIGURATION_FILE_PATH_EDEFAULT);
                return;
            case ConfigurationPackageImpl.CONFIGURATION__EMBEDDED:
                setEmbedded(EMBEDDED_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID)
    {
        switch (featureID)
        {
            case ConfigurationPackageImpl.CONFIGURATION__HOST:
                return HOST_EDEFAULT == null ? host != null : !HOST_EDEFAULT.equals(host);
            case ConfigurationPackageImpl.CONFIGURATION__PORT:
                return PORT_EDEFAULT == null ? port != null : !PORT_EDEFAULT.equals(port);
            case ConfigurationPackageImpl.CONFIGURATION__INITIAL_CONTEXT_FACTORY:
                return INITIAL_CONTEXT_FACTORY_EDEFAULT == null ? initialContextFactory != null : !INITIAL_CONTEXT_FACTORY_EDEFAULT.equals(initialContextFactory);
            case ConfigurationPackageImpl.CONFIGURATION__DAS_PARTITION_NAME:
                return DAS_PARTITION_NAME_EDEFAULT == null ? dasPartitionName != null : !DAS_PARTITION_NAME_EDEFAULT.equals(dasPartitionName);
            case ConfigurationPackageImpl.CONFIGURATION__SCHEMA_PARTITION_NAME:
                return SCHEMA_PARTITION_NAME_EDEFAULT == null ? schemaPartitionName != null : !SCHEMA_PARTITION_NAME_EDEFAULT.equals(schemaPartitionName);
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_PRINCIPAL:
                return SECURITY_PRINCIPAL_EDEFAULT == null ? securityPrincipal != null : !SECURITY_PRINCIPAL_EDEFAULT.equals(securityPrincipal);
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_AUTHENTICATION_TYPE:
                return SECURITY_AUTHENTICATION_TYPE_EDEFAULT == null ? securityAuthenticationType != null : !SECURITY_AUTHENTICATION_TYPE_EDEFAULT.equals(securityAuthenticationType);
            case ConfigurationPackageImpl.CONFIGURATION__SECURITY_CREDENTIALS:
                return SECURITY_CREDENTIALS_EDEFAULT == null ? securityCredentials != null : !SECURITY_CREDENTIALS_EDEFAULT.equals(securityCredentials);
            case ConfigurationPackageImpl.CONFIGURATION__NLOG4J_CONFIGURATION_FILE_PATH:
                return NLOG4J_CONFIGURATION_FILE_PATH_EDEFAULT == null ? nlog4jConfigurationFilePath != null : !NLOG4J_CONFIGURATION_FILE_PATH_EDEFAULT.equals(nlog4jConfigurationFilePath);
            case ConfigurationPackageImpl.CONFIGURATION__EMBEDDED:
                return embedded != EMBEDDED_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString()
    {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (host: ");
        result.append(host);
        result.append(", port: ");
        result.append(port);
        result.append(", initialContextFactory: ");
        result.append(initialContextFactory);
        result.append(", dasPartitionName: ");
        result.append(dasPartitionName);
        result.append(", schemaPartitionName: ");
        result.append(schemaPartitionName);
        result.append(", securityPrincipal: ");
        result.append(securityPrincipal);
        result.append(", securityAuthenticationType: ");
        result.append(securityAuthenticationType);
        result.append(", securityCredentials: ");
        result.append(securityCredentials);
        result.append(", nlog4jConfigurationFilePath: ");
        result.append(nlog4jConfigurationFilePath);
        result.append(", embedded: ");
        result.append(embedded);
        result.append(')');
        return result.toString();
    }

} //ConfigurationImpl
