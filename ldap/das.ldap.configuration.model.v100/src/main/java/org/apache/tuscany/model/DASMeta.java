/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.tuscany.model;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DAS Meta</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.apache.tuscany.model.DASMeta#getSupportedSchemas <em>Supported Schemas</em>}</li>
 *   <li>{@link org.apache.tuscany.model.DASMeta#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @model
 * @generated
 */
public interface DASMeta
{
    /**
     * Returns the value of the '<em><b>Supported Schemas</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Supported Schemas</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Supported Schemas</em>' attribute list.
     * @model default="localhost"
     * @generated
     */
    List<String> getSupportedSchemas();

    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(String)
     * @model default="0" id="true"
     * @generated
     */
    String getId();

    /**
     * Sets the value of the '{@link org.apache.tuscany.model.DASMeta#getId <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(String value);

} // DASMeta
