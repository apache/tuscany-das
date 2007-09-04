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
package org.apache.tuscany.das.rdb.test;

/*
 * Test capability to participate in an extenrlly managed transaction. 
 * The client is managing the transaction boundary so the DAS will not issue
 * commit/rollback
 * 
 */

import org.apache.tuscany.das.rdb.Command;
import org.apache.tuscany.das.rdb.DAS;
import org.apache.tuscany.das.rdb.impl.DASImpl;
import org.apache.tuscany.das.rdb.test.data.BankAccountData;
import org.apache.tuscany.das.rdb.test.data.CustomerData;
import org.apache.tuscany.das.rdb.test.data.DepartmentsData;
import org.apache.tuscany.das.rdb.test.data.EmployeesData;
import org.apache.tuscany.das.rdb.test.framework.DasTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;

import commonj.sdo.DataObject;
import commonj.sdo.helper.XMLHelper;

public class TransactionTests extends DasTest {

    protected void setUp() throws Exception {
        super.setUp();
        new CustomerData(getAutoConnection()).refresh();
        new DepartmentsData(getAutoConnection()).refresh();
        new EmployeesData(getAutoConnection()).refresh();
        new BankAccountData(getAutoConnection()).refresh();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Read and modify a customer. Uses a "passive" connection
     */
    public void testReadModifyApply() throws Exception {

        // Create and initialize a DAS connection and initialize for externally
        // managed transaction boundaries
        java.sql.Connection c = getConnection();

        DAS das = DAS.FACTORY.createDAS(c);
        // Read customer 1
        Command select = das.createCommand("select * from CUSTOMER");
        DataObject root = select.executeQuery();
        
        int numCustomers = root.getList("CUSTOMER").size();

        //Create two identical customers.  Will force insert error on dup key
        
        DataObject custA = root.createDataObject("CUSTOMER");
        custA.set("ID", new Integer(100));
        custA.set("ADDRESS", "5528 Wells Fargo Drive");
        custA.set("LASTNAME", "Gerkin");
       
        DataObject custB = root.createDataObject("CUSTOMER");
        custB.set("ID", new Integer(100));
        custB.set("ADDRESS", "5528 Wells Fargo Drive");
        custB.set("LASTNAME", "Gerkin");
        try {
            das.applyChanges(root);
            fail("An exception was expected");
        } catch (Exception e) {
            //do nothing
        }

        // Verify that the changes did not go through
        // Roll back should discard al changes
        root = select.executeQuery();
        assertEquals(numCustomers, root.getList("CUSTOMER").size());

    }

    /**
     * Demonstrate that DAS allows external control of transaction when using
     * external connection. i.e. ConnectionInfo with only managedtx=false
     */
    public void testAbleToControlExternallyInitedTransaction() throws Exception {
        // Create and initialize a DAS connection and initialize for externally
        // managed transaction boundaries. 
        java.sql.Connection c = getConnection();
        
        //"John Jones" is in "Advanced Technologies"(Department1)
        DAS das = DAS.FACTORY.createDAS(getConfig("extTxWithExtConnection.xml"),c);        
        Command selEmpForDep1 = das.getCommand("SelectEmployeesFromDepartment1");
        DataObject root1 = selEmpForDep1.executeQuery();
        DataObject department1 = (DataObject)root1.getList("DEPARTMENTS").get(0);
        assertEquals("Advanced Technologies", department1.getString("NAME"));
        DataObject employee1 = (DataObject)department1.getList("employed").get(0);
        assertEquals("John Jones", employee1.getString("NAME"));
        
        //remove "John Jones" from "Advanced Technologies"(Department1)
        employee1.delete();
        das.applyChanges(root1);
        
        //user decided to revert the decision to remove and thus issues rollback
        c.rollback();
        
        //user wants to ensure that "John Jones" is still in Department1
        root1 = selEmpForDep1.executeQuery();
        department1 = (DataObject)root1.getList("DEPARTMENTS").get(0);
        assertEquals("Advanced Technologies", department1.getString("NAME"));
        boolean employeeRetained = isEmployeeInDepartment(root1, "John Jones");
        assertEquals(true, employeeRetained);//this shows that employee is not removed due to rollback
        //so proves that user is managing tx and not DAS
    }
    
    private boolean isEmployeeInDepartment(DataObject root, String employeeName){
        //iterate for all employees in department1 is ensure that employeeName is there
        Iterator itr = ((DataObject)root.getList("DEPARTMENTS").get(0)).getList("employed").iterator();
        boolean employeeRetained = false;
        while(itr.hasNext()){
        	DataObject employee = (DataObject)itr.next();
        	if(employee.getString("NAME").equals(employeeName)){
        		employeeRetained = true;
        	}
        }
        return employeeRetained;
    }
    
    /**
     * Demonstrate that user can control tx when connection is from DAS for single command
     */
    public void testAbleToCommitTransaction() throws Exception {
        //"John Jones" is in "Advanced Technologies"(Department1)
        DAS das = DAS.FACTORY.createDAS(getConfig("extTxWithIntConnection.xml"));
        Connection conn = ((DASImpl)das).getConnection();
        //connection is created from config, not passed by user.
        Command selEmpForDep1 = das.getCommand("SelectEmployeesFromDepartment1");
        DataObject root1 = selEmpForDep1.executeQuery();
        DataObject department1 = (DataObject)root1.getList("DEPARTMENTS").get(0);
        assertEquals("Advanced Technologies", department1.getString("NAME"));
        DataObject employee1 = (DataObject)department1.getList("employed").get(0);
        assertEquals("John Jones", employee1.getString("NAME"));
        //remove "John Jones" from "Advanced Technologies"(Department1)
        employee1.delete();
        das.applyChanges(root1);
        conn.commit();
        //now open a fresh connection and check data in database
        java.sql.Connection c = getConnection();
        DAS dasFresh = DAS.FACTORY.createDAS(getConfig("extTxWithExtConnection.xml"),c);
        Command selEmpForDepFresh1 = dasFresh.getCommand("SelectEmployeesFromDepartment1");
        DataObject rootFresh1 = selEmpForDepFresh1.executeQuery();
        boolean employeeRetained1 = isEmployeeInDepartment(rootFresh1, "John Jones");

        assertEquals(false, employeeRetained1);
    }
    
    /**
     * Demonstrate that user can control tx for group of commands when connection is created in DAS 
     */
    public void testDataIntegrity() throws Exception {
        //remove $200 from account1 and add $200 to account2
    	//when doing add to account2 operation user rolls back
    	//account1 and account2 both should have original balance.
        DAS das = DAS.FACTORY.createDAS(getConfig("extTxnIntConnectionRollback.xml"));
        //connection is created from config, not passed by user.
        
        //check original balance from account1
        Command SelectBalanceFromAccount1 = das.getCommand("SelectBalanceFromAccount1");
        DataObject root1 = SelectBalanceFromAccount1.executeQuery();
        DataObject bankAccount1 = (DataObject)root1.getList("BANKACCOUNT").get(0);
        assertEquals(10000, bankAccount1.getInt("BALANCE"));
        
        //remove $200 from account1
        bankAccount1.setInt("BALANCE", (bankAccount1.getInt("BALANCE")-200));
        das.applyChanges(root1);

        //check original balance from account2
        Command SelectBalanceFromAccount2 = das.getCommand("SelectBalanceFromAccount2");
        DataObject root2 = SelectBalanceFromAccount2.executeQuery();
        DataObject bankAccount2 = (DataObject)root2.getList("BANKACCOUNT").get(0);
        assertEquals(5000, bankAccount2.getInt("BALANCE"));
        
        //add  "$200"  to  account2
        bankAccount2.setInt("BALANCE", (bankAccount2.getInt("BALANCE")+200));
       	das.applyChanges(root2);
       	das.getConnection().rollback();//say user may get exception here in a try-catch and decides to rollback

        //now open a fresh DAS and check data in database
        DAS dasFresh = DAS.FACTORY.createDAS(getConfig("extTxnIntConnectionRollback.xml"));
        
        Command SelectBalanceFromAccount1Fresh = dasFresh.getCommand("SelectBalanceFromAccount1");
       	DataObject rootFresh1 = SelectBalanceFromAccount1Fresh.executeQuery();  
       	DataObject bankAccount1Fresh = (DataObject)rootFresh1.getList("BANKACCOUNT").get(0);
       	assertEquals(10000, bankAccount1Fresh.getInt("BALANCE"));//this shows $200 is not removed from account1, expected,
        //as rollbacked
       	
        Command SelectBalanceFromAccount2Fresh = dasFresh.getCommand("SelectBalanceFromAccount2");
       	DataObject rootFresh2 = SelectBalanceFromAccount2Fresh.executeQuery();  
       	DataObject bankAccount2Fresh = (DataObject)rootFresh2.getList("BANKACCOUNT").get(0);
        assertEquals(5000, bankAccount2Fresh.getInt("BALANCE"));//this shows $200 is not added to account2, expected,
        //as have rollbacked
    }
}
