/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tuscany.samples.das.tx.manager;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.geronimo.transaction.manager.TransactionManagerImpl;
import org.apache.geronimo.transaction.manager.XidFactoryImpl;

/**
 * A <code>TransactionManager</code> that delegates to a Geronimo JTA transaction manager. This class serves as a
 * wrapper for initializing the Geronimo TM as a system service.
 *
 * @version $Rev$ $Date$
 */
public class GeronimoTransactionManagerService implements TransactionManager {
    private TransactionManager transactionManager;
    private int timeout = 250;

    public GeronimoTransactionManagerService() {
    }

    /**
     * Returns the transaction timeout in seconds
     *
     * @return the transaction timeout in seconds
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the transaction timeout in seconds
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void init() throws Exception {
        XidFactoryImpl factory = new XidFactoryImpl();
        transactionManager = new TransactionManagerImpl(timeout, factory, null);        
    }

    public void begin() throws NotSupportedException, SystemException {
        transactionManager.begin();
    }

    public void commit() throws HeuristicMixedException,
                                HeuristicRollbackException,
                                IllegalStateException,
                                RollbackException,
                                SecurityException,
                                SystemException {
        transactionManager.commit();
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public int getStatus() throws SystemException {
        return transactionManager.getStatus();
    }

    public Transaction getTransaction() throws SystemException {
        return transactionManager.getTransaction();
    }

    public void resume(Transaction transaction)
        throws IllegalStateException, InvalidTransactionException, SystemException {
        transactionManager.resume(transaction);
    }

    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        transactionManager.rollback();
    }

    public void setRollbackOnly() throws IllegalStateException, SystemException {
        transactionManager.setRollbackOnly();
    }

    public void setTransactionTimeout(int i) throws SystemException {
        transactionManager.setTransactionTimeout(i);
    }

    public Transaction suspend() throws SystemException {
        return transactionManager.suspend();
    }

    public void begin(long timeout) throws NotSupportedException, SystemException {
        transactionManager.begin();
        return;
    }

}
