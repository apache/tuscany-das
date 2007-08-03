/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.tuscany.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.apache.tuscany.model.Configuration#getHost <em>Host</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#getPort <em>Port</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#getInitialContextFactory <em>Initial Context Factory</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#getDasPartitionName <em>Das Partition Name</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#getSchemaPartitionName <em>Schema Partition Name</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#getSecurityPrincipal <em>Security Principal</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#getSecurityAuthenticationType <em>Security Authentication Type</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#getSecurityCredentials <em>Security Credentials</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#getNlog4jConfigurationFilePath <em>Nlog4j Configuration File Path</em>}</li>
 *   <li>{@link org.apache.tuscany.model.Configuration#isEmbedded <em>Embedded</em>}</li>
 * </ul>
 * </p>
 *
 * @model
 * @generated
 */
public interface Configuration
{
    /**
     * Returns the value of the '<em><b>Host</b></em>' attribute.
     * The default value is <code>"localhost"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Host</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Host</em>' attribute.
     * @see #setHost(String)
     * @model default="localhost"
     * @generated
     */
    String getHost();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getHost <em>Host</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Host</em>' attribute.
     * @see #getHost()
     * @generated
     */
    void setHost(String value);

    /**
     * Returns the value of the '<em><b>Port</b></em>' attribute.
     * The default value is <code>"10389"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Port</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Port</em>' attribute.
     * @see #setPort(String)
     * @model default="10389"
     * @generated
     */
    String getPort();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getPort <em>Port</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Port</em>' attribute.
     * @see #getPort()
     * @generated
     */
    void setPort(String value);

    /**
     * Returns the value of the '<em><b>Initial Context Factory</b></em>' attribute.
     * The default value is <code>"org.apache.directory.server.core.jndi.CoreContextFactory"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Initial Context Factory</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Initial Context Factory</em>' attribute.
     * @see #setInitialContextFactory(String)
     * @model default="org.apache.directory.server.core.jndi.CoreContextFactory"
     * @generated
     */
    String getInitialContextFactory();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getInitialContextFactory <em>Initial Context Factory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Initial Context Factory</em>' attribute.
     * @see #getInitialContextFactory()
     * @generated
     */
    void setInitialContextFactory(String value);

    /**
     * Returns the value of the '<em><b>Das Partition Name</b></em>' attribute.
     * The default value is <code>"das"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Das Partition Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Das Partition Name</em>' attribute.
     * @see #setDasPartitionName(String)
     * @model default="das"
     * @generated
     */
    String getDasPartitionName();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getDasPartitionName <em>Das Partition Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Das Partition Name</em>' attribute.
     * @see #getDasPartitionName()
     * @generated
     */
    void setDasPartitionName(String value);

    /**
     * Returns the value of the '<em><b>Schema Partition Name</b></em>' attribute.
     * The default value is <code>"schema"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Schema Partition Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Schema Partition Name</em>' attribute.
     * @see #setSchemaPartitionName(String)
     * @model default="schema"
     * @generated
     */
    String getSchemaPartitionName();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getSchemaPartitionName <em>Schema Partition Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Schema Partition Name</em>' attribute.
     * @see #getSchemaPartitionName()
     * @generated
     */
    void setSchemaPartitionName(String value);

    /**
     * Returns the value of the '<em><b>Security Principal</b></em>' attribute.
     * The default value is <code>"uid=admin,ou=system"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Security Principal</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Security Principal</em>' attribute.
     * @see #setSecurityPrincipal(String)
     * @model default="uid=admin,ou=system"
     * @generated
     */
    String getSecurityPrincipal();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getSecurityPrincipal <em>Security Principal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Security Principal</em>' attribute.
     * @see #getSecurityPrincipal()
     * @generated
     */
    void setSecurityPrincipal(String value);

    /**
     * Returns the value of the '<em><b>Security Authentication Type</b></em>' attribute.
     * The default value is <code>"simple"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Security Authentication Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Security Authentication Type</em>' attribute.
     * @see #setSecurityAuthenticationType(String)
     * @model default="simple"
     * @generated
     */
    String getSecurityAuthenticationType();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getSecurityAuthenticationType <em>Security Authentication Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Security Authentication Type</em>' attribute.
     * @see #getSecurityAuthenticationType()
     * @generated
     */
    void setSecurityAuthenticationType(String value);

    /**
     * Returns the value of the '<em><b>Security Credentials</b></em>' attribute.
     * The default value is <code>"secret"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Security Credentials</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Security Credentials</em>' attribute.
     * @see #setSecurityCredentials(String)
     * @model default="secret"
     * @generated
     */
    String getSecurityCredentials();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getSecurityCredentials <em>Security Credentials</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Security Credentials</em>' attribute.
     * @see #getSecurityCredentials()
     * @generated
     */
    void setSecurityCredentials(String value);

    /**
     * Returns the value of the '<em><b>Nlog4j Configuration File Path</b></em>' attribute.
     * The default value is <code>"src/test/resources/log4j.properties"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Nlog4j Configuration File Path</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Nlog4j Configuration File Path</em>' attribute.
     * @see #setNlog4jConfigurationFilePath(String)
     * @model default="src/test/resources/log4j.properties"
     * @generated
     */
    String getNlog4jConfigurationFilePath();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#getNlog4jConfigurationFilePath <em>Nlog4j Configuration File Path</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Nlog4j Configuration File Path</em>' attribute.
     * @see #getNlog4jConfigurationFilePath()
     * @generated
     */
    void setNlog4jConfigurationFilePath(String value);

    /**
     * Returns the value of the '<em><b>Embedded</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Embedded</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Embedded</em>' attribute.
     * @see #setEmbedded(boolean)
     * @model default="false"
     * @generated
     */
    boolean isEmbedded();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.Configuration#isEmbedded <em>Embedded</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Embedded</em>' attribute.
     * @see #isEmbedded()
     * @generated
     */
    void setEmbedded(boolean value);

} // Configuration
