/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.tuscany.model.impl;

import java.util.Collection;
import java.util.List;

import org.apache.tuscany.model.DASMeta;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DAS Meta</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.apache.tuscany.model.impl.DASMetaImpl#getSupportedSchemas <em>Supported Schemas</em>}</li>
 *   <li>{@link org.apache.tuscany.model.impl.DASMetaImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DASMetaImpl extends EDataObjectImpl implements DASMeta
{
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * The cached value of the '{@link #getSupportedSchemas() <em>Supported Schemas</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSupportedSchemas()
     * @generated
     * @ordered
     */
    protected EList<String> supportedSchemas;

    /**
     * The default value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected static final String ID_EDEFAULT = "0";

    /**
     * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getId()
     * @generated
     * @ordered
     */
    protected String id = ID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected DASMetaImpl()
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
        return ConfigurationPackageImpl.Literals.DAS_META;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public List<String> getSupportedSchemas()
    {
        if (supportedSchemas == null)
        {
            supportedSchemas = new EDataTypeUniqueEList<String>(String.class, this, ConfigurationPackageImpl.DAS_META__SUPPORTED_SCHEMAS);
        }
        return supportedSchemas;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getId()
    {
        return id;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setId(String newId)
    {
        String oldId = id;
        id = newId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ConfigurationPackageImpl.DAS_META__ID, oldId, id));
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
            case ConfigurationPackageImpl.DAS_META__SUPPORTED_SCHEMAS:
                return getSupportedSchemas();
            case ConfigurationPackageImpl.DAS_META__ID:
                return getId();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue)
    {
        switch (featureID)
        {
            case ConfigurationPackageImpl.DAS_META__SUPPORTED_SCHEMAS:
                getSupportedSchemas().clear();
                getSupportedSchemas().addAll((Collection<? extends String>)newValue);
                return;
            case ConfigurationPackageImpl.DAS_META__ID:
                setId((String)newValue);
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
            case ConfigurationPackageImpl.DAS_META__SUPPORTED_SCHEMAS:
                getSupportedSchemas().clear();
                return;
            case ConfigurationPackageImpl.DAS_META__ID:
                setId(ID_EDEFAULT);
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
            case ConfigurationPackageImpl.DAS_META__SUPPORTED_SCHEMAS:
                return supportedSchemas != null && !supportedSchemas.isEmpty();
            case ConfigurationPackageImpl.DAS_META__ID:
                return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
        result.append(" (supportedSchemas: ");
        result.append(supportedSchemas);
        result.append(", id: ");
        result.append(id);
        result.append(')');
        return result.toString();
    }

} //DASMetaImpl
