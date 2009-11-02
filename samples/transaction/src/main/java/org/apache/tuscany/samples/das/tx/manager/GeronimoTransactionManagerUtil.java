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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.geronimo.transaction.manager.XidFactory;
import org.apache.geronimo.transaction.manager.XidFactoryImpl;
import org.apache.tuscany.das.rdb.dbconfig.DBConfig;
import org.apache.tuscany.samples.das.databaseSetup.DatabaseSetupUtil;

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

public class GeronimoTransactionManagerUtil {
	private GeronimoTransactionManagerService service;
    private static XADataSource xads;
    private static XAConnection xaconn = null;
    private static XAResource xares = null;
    private static XidFactory xidFact = null;
    private static Xid xid = null;
	
    public void initTransactionManager() {
        // Get a transction manager
        try {
        	xidFact = new XidFactoryImpl();
        	// creates an instance of Geronimo TM Service        	
        	service = new GeronimoTransactionManagerService();
            service.init();
            service.begin();
            initDataSource();
            initXAConnection();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
	
    private void initDataSource() {
        //setup XADataSource
    	xads = new MysqlXADataSource();       
        try {
        	DBConfig dbcfg = DatabaseSetupUtil.getDBConfig();
        	((MysqlXADataSource) xads).setUser(dbcfg.getConnectionInfo().getConnectionProperties().getUserName());
        	((MysqlXADataSource) xads).setPassword(dbcfg.getConnectionInfo().getConnectionProperties().getPassword());
            ((MysqlXADataSource) xads).setUrl(dbcfg.getConnectionInfo().getConnectionProperties().getDatabaseURL());
            ((MysqlXADataSource) xads).setURL(dbcfg.getConnectionInfo().getConnectionProperties().getDatabaseURL());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initXAConnection() throws SQLException{
    	DBConfig dbcfg = DatabaseSetupUtil.getDBConfig();
    	xaconn = xads.getXAConnection(dbcfg.getConnectionInfo().getConnectionProperties().getUserName(), 
    			dbcfg.getConnectionInfo().getConnectionProperties().getPassword());
    }

    public void startTransaction() throws Exception {
    	try{
            //create XAResource
            xid= xidFact.createXid();
            xares = xaconn.getXAResource();
            xares.setTransactionTimeout(250); //something
            xares.start(xid, XAResource.TMNOFLAGS);            
            return;    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw e;
    	}
    }
    
    public void commitTransaction() throws Exception {
    	xares.end(xid, XAResource.TMSUCCESS);
    	xares.prepare(xid);
    	xares.commit(xid, false); //onePhase=true/false
    }
    
    public void rollbackTransaction() throws Exception {
    	xares.end(xid, XAResource.TMFAIL);
    	xares.prepare(xid);
    	xares.rollback(xid);    	
    }
    
    public Connection getConnection() throws SQLException {
        return xaconn.getConnection();
    }

    /**
     * cleanup
     */
    public void cleanup() throws Exception{
    	xaconn.close();
    	xid = null;
    	xares = null;
    	xaconn = null;
    	xads = null;
    	service.commit();
    }
}
