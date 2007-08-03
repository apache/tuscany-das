/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.tuscany.model;


/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @generated
 */
public interface ConfigurationFactory
{
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    ConfigurationFactory INSTANCE = org.apache.tuscany.model.impl.ConfigurationFactoryImpl.eINSTANCE;

    /**
     * Returns a new object of class '<em>Configuration</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Configuration</em>'.
     * @generated
     */
    Configuration createConfiguration();

    /**
     * Returns a new object of class '<em>DAS Meta</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>DAS Meta</em>'.
     * @generated
     */
    DASMeta createDASMeta();

} //ConfigurationFactory
