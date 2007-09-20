/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tuscany.samples.das.databaseSetup;

import java.sql.Connection;
import java.sql.ResultSet;

import org.apache.tuscany.das.rdb.dbconfig.DBConfig;
import org.apache.tuscany.das.rdb.dbconfig.DBConfigUtil;
import org.apache.tuscany.das.rdb.dbconfig.DBInitializer;
import org.apache.tuscany.samples.das.tx.manager.GeronimoTransactionManagerUtil;

/**
 * Setup database, XA resources required by the sample
 * 
 *
 */
public class DatabaseSetupUtil {
    private static DBConfig dbConfig = null;
    /**
     * Constructor for DatabaseSetupUtil.
     */
    public DatabaseSetupUtil(String dbConfigFile) throws Exception {
    	//create database tables, data
    	DBInitializer dbInitializer = new DBInitializer(dbConfigFile);
    	dbInitializer.refreshDatabaseData();
    	//read DAS Config
    	dbConfig = DBConfigUtil.loadDBConfig(ClassLoader.getSystemResourceAsStream(dbConfigFile));
    }
    
    public static DBConfig getDBConfig() {
    	return DatabaseSetupUtil.dbConfig;
    }
    
    public static void main(String[] args) throws Exception{
    	//test program
    	new DatabaseSetupUtil("BankAccountDBConfig.xml");
    	GeronimoTransactionManagerUtil tmUtil = new GeronimoTransactionManagerUtil();
    	tmUtil.initTransactionManager();
    	tmUtil.startTransaction();
    	Connection conn = tmUtil.getConnection();
    	
        ResultSet rs = conn.createStatement().executeQuery("select id, balance from bankaccount");
        while(rs.next()){
        	System.out.println("result row: "+rs.getInt(1)+","+rs.getString(2));
        }
        
        tmUtil.commitTransaction();

    }    
}
