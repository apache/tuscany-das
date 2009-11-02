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
package org.apache.tuscany.samples.das.tx.sample;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.tuscany.das.rdb.Command;
import org.apache.tuscany.das.rdb.DAS;
import org.apache.tuscany.samples.das.databaseSetup.DatabaseSetupUtil;
import org.apache.tuscany.samples.das.tx.manager.GeronimoTransactionManagerUtil;

import commonj.sdo.DataObject;

/**
 * Sample using Geronimo TM to execute multiple DAS applyChanges() under one transaction.
 * 
 */
public class MoneyTransfer {

    private static final String USAGE = "usage: java MoneyTransfer [commit|rollback] [transferAmount]";
    private static final String SQL_REQUEST = "select id, name, balance from bankaccount";
    private static Connection conn = null;
	private static DAS das = null;

    private static void printTable() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(SQL_REQUEST);
            int numcols = rset.getMetaData().getColumnCount();
            for (int i = 1; i <= numcols; i++) {
                System.out.print("\t" + rset.getMetaData().getColumnName(i));
            }
            System.out.println();
            while (rset.next()) {
                for (int i = 1; i <= numcols; i++) {
                    System.out.print("\t" + rset.getString(i));
                }
                System.out.println("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	GeronimoTransactionManagerUtil tmUtil = null;
    	
        if (args.length != 2 || (!args[0].equals("commit") && !args[0].equals("rollback"))) {
            System.out.println(USAGE + "\n");
            System.exit(1);
        }

        String completion = args[0];
        float transferAmount = 0;
        try {
        	transferAmount = Float.parseFloat(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("[transferAmount] has to be a float\n");
            System.exit(1);
        }

        System.out.println("INIT database");        
        try{
        	new DatabaseSetupUtil("BankAccountDBConfig.xml");
        }catch(Exception e){
        	e.printStackTrace();
        	System.exit(1);
        }
        
        try {
            System.out.println("INIT transaction manager");
            tmUtil = new GeronimoTransactionManagerUtil();
        	tmUtil.initTransactionManager();
        	tmUtil.startTransaction();
        } catch (Exception e) {
            System.out.println("Exception of type :" + e.getClass().getName() + " has been thrown");
            System.out.println("Exception message :" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            System.out.println("Get a connection");
            conn = tmUtil.getConnection();
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Before transaction, table is:");
        printTable();

        try {
            System.out.println("Begin a transaction");            
            init(); //initialize DAS instance            
            System.out.println("Update the table - 2 accounts");
            withdrawFromBankAccount1(transferAmount);
            depositToBankAccount2(transferAmount);
            
            if (completion.equals("commit")) {
                System.out.println("*commit* the transaction");
                tmUtil.commitTransaction();
            } else {
                System.out.println("*rollback* the transaction");
                tmUtil.rollbackTransaction();
            }
        } catch (Exception e) {
            System.out.println("Exception of type :" + e.getClass().getName() + " has been thrown");
            System.out.println("Exception message :" + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("After transaction, table is:");
        printTable();

        System.out.println("Cleanup");
        try {
        	tmUtil.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("End MoneyTransfer\n");
        System.exit(0);
    }

    private static void init(){
			try{
				das = DAS.FACTORY.createDAS(getConfig("BankAccounts.xml"), conn);
			}catch(Exception e){
				e.printStackTrace();
			}
	}

	/**
	 * Withdraw asked amount from account1
	 * @return
	 */
    public static final void withdrawFromBankAccount1(float transferAmount) {
        Command read = das.getCommand("BankAccount1Data");
        DataObject root = read.executeQuery();
        DataObject account1 = (DataObject)root.getList("BANKACCOUNT").get(0);
        float currentBalance = account1.getFloat("BALANCE");
        float newBalance = currentBalance - transferAmount;
        account1.setFloat("BALANCE", newBalance);
        das.applyChanges(root);
    }

	/**
	 * Deposit asked amount into account2
	 * @return
	 */
    public static final void depositToBankAccount2(float transferAmount) {
        Command read = das.getCommand("BankAccount2Data");
        DataObject root = read.executeQuery();
        DataObject account2 = (DataObject)root.getList("BANKACCOUNT").get(0);
        float currentBalance = account2.getFloat("BALANCE");
        float newBalance = currentBalance + transferAmount;
        account2.setFloat("BALANCE", newBalance);
        das.applyChanges(root);
    }
    
	/**Utilities
     *
     * @param fileName
     * @return
     */
    private static InputStream getConfig(String fileName) {
    	MoneyTransfer jdbcExample = new MoneyTransfer(); 
        return jdbcExample.getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
