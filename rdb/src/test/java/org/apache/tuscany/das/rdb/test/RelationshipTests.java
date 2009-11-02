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
 * 
 * 
 */

import java.sql.SQLException;
import java.util.List;

import org.apache.tuscany.das.rdb.Command;
import org.apache.tuscany.das.rdb.ConfigHelper;
import org.apache.tuscany.das.rdb.DAS;
import org.apache.tuscany.das.rdb.config.Config;
import org.apache.tuscany.das.rdb.config.Relationship;
import org.apache.tuscany.das.rdb.config.wrapper.MappingWrapper;
import org.apache.tuscany.das.rdb.test.data.CompanyData;
import org.apache.tuscany.das.rdb.test.data.CompanyDeptData;
import org.apache.tuscany.das.rdb.test.data.CompanyEmpData;
import org.apache.tuscany.das.rdb.test.data.CustomerData;
import org.apache.tuscany.das.rdb.test.data.DepEmpData;
import org.apache.tuscany.das.rdb.test.data.DepartmentData;
import org.apache.tuscany.das.rdb.test.data.EmployeeData;
import org.apache.tuscany.das.rdb.test.data.OrderData;
import org.apache.tuscany.das.rdb.test.data.ProductData;
import org.apache.tuscany.das.rdb.test.data.ProductProdSpecData;
import org.apache.tuscany.das.rdb.test.data.ProductSpecData;
import org.apache.tuscany.das.rdb.test.framework.DasTest;

import commonj.sdo.DataObject;

public class RelationshipTests extends DasTest {

    protected void setUp() throws Exception {
        super.setUp();
        new CustomerData(getAutoConnection()).refresh();
        new OrderData(getAutoConnection()).refresh();

        new CompanyData(getAutoConnection()).refresh();
        new EmployeeData(getAutoConnection()).refresh();
        new DepartmentData(getAutoConnection()).refresh();
        new CompanyEmpData(getAutoConnection()).refresh();
        new DepEmpData(getAutoConnection()).refresh();
        new CompanyDeptData(getAutoConnection()).refresh();
        new ProductData(getAutoConnection()).refresh();
        new ProductSpecData(getAutoConnection()).refresh();
        new ProductProdSpecData(getAutoConnection()).refresh();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test ability to read a compound graph
     */
    public void testRead() throws Exception {

        String statement = "SELECT * FROM CUSTOMER LEFT JOIN ANORDER "
                + "ON CUSTOMER.ID = ANORDER.CUSTOMER_ID WHERE CUSTOMER.ID = 1";

        DAS das = DAS.FACTORY.createDAS(getConfig("customerOrderRelationshipMapping.xml"), getConnection());
        // Read some customers and related orders
        Command select = das.createCommand(statement);

        DataObject root = select.executeQuery();
        DataObject customer = root.getDataObject("Customer[1]");
        assertEquals(2, customer.getList("orders").size());

    }

    /**
     * Same as above except uses xml file for relationhip and key information.
     * Employs CUD generation.
     */
    public void testRelationshipModification2() throws Exception {

        DAS das = DAS.FACTORY.createDAS(getConfig("basicCustomerOrderMapping.xml"), getConnection());
        // Read some customers and related orders
        Command select = das
                .createCommand("SELECT * FROM CUSTOMER LEFT JOIN ANORDER ON CUSTOMER.ID = ANORDER.CUSTOMER_ID");

        DataObject root = select.executeQuery();
        
        DataObject cust1 = root.getDataObject("CUSTOMER[1]");
        DataObject cust2 = root.getDataObject("CUSTOMER[2]");

        // Save IDs
        Integer cust1ID = (Integer) cust1.get("ID");
        Integer cust2ID = (Integer) cust2.get("ID");
        // save order count
        Integer cust1OrderCount = new Integer(cust1.getList("orders").size());
        Integer cust2OrderCount = new Integer(cust2.getList("orders").size());

        // Move an order to cust1 from cust2
        DataObject order = (DataObject) cust2.getList("orders").get(0);
        cust1.getList("orders").add(order);

        // Flush changes
        das.applyChanges(root);

        // verify cust1 relationship updates
        select = das
                .createCommand("SELECT * FROM CUSTOMER LEFT JOIN ANORDER ON "
                        + "CUSTOMER.ID = ANORDER.CUSTOMER_ID where CUSTOMER.ID = ?");
        select.setParameter(1, cust1ID);

        root = select.executeQuery();
        assertEquals(cust1OrderCount.intValue() + 1, root.getList("CUSTOMER[1]/orders").size());

        // verify cust2 relationship updates
        select.setParameter(1, cust2ID);
        root = select.executeQuery();
        assertEquals(cust2OrderCount.intValue() - 1, root.getList("CUSTOMER[1]/orders").size());

    }
    
    /**
     * This scenario uses union to simmulate full outer join
     * The resulted graph will have departments without employees, and employees without departments
     * And this testcase will modify the relationship between these entities and assign the employees to the department
     * 
     * @throws Exception
     */
    public void testSimulateFullOuterJoinRelationshipModification() throws Exception {

        DAS das = DAS.FACTORY.createDAS(getConfig("companyMappingWithResultDescriptor.xml"), getConnection());
        // Read some customers and related orders
        
        Command select = das.getCommand("testFullOuterJoinRelationship");
        DataObject root = select.executeQuery();

        DataObject department = root.getDataObject("DEPARTMENT[NAME='New Technologies']"); //department with no employees
                
        DataObject emp1 = root.getDataObject("EMPLOYEE[NAME='Mary Smith']"); //employee not assgned to department
        DataObject emp2 = root.getDataObject("EMPLOYEE[NAME='John Smith']"); //employee not assgned to department
        
        department.getList("employees").add(emp1);
        department.getList("employees").add(emp2);

        das.applyChanges(root);

        //verify cust1 relationship updates
        select = das.getCommand("testEmployeesFromDepartment");
        select.setParameter(1, "New Technologies" );

        root = select.executeQuery();
        assertEquals(2, root.getDataObject("DEPARTMENT[NAME='New Technologies']").getList("employees").size());

        

    }
    
    public void testFKBehavior() throws SQLException {

        DAS das = DAS.FACTORY.createDAS(getConfig("basicCustomerOrderMapping.xml"), getConnection());
        // Read some customers and related orders
        Command select = das
                .createCommand("SELECT * FROM CUSTOMER LEFT JOIN ANORDER ON CUSTOMER.ID = ANORDER.CUSTOMER_ID");

        DataObject root = select.executeQuery();

        DataObject cust1 = root.getDataObject("CUSTOMER[1]");
        DataObject cust2 = root.getDataObject("CUSTOMER[2]");

        // Save IDs
        Integer cust1ID = (Integer) cust1.get("ID");
        
        // Move an order to cust1 from cust2
        DataObject order = (DataObject) cust2.getList("orders").get(0);
        cust1.getList("orders").add(order);
        order.setInt("CUSTOMER_ID", cust1ID.intValue());
       
        try {
            das.applyChanges(root);
            fail("An exception should be thrown");
        } catch (RuntimeException ex) {
            assertEquals("Foreign key properties should not be set when the corresponding relationship has changed", ex.getMessage());
        }

    }
    
    public void testFKBehavior2() throws SQLException {
        DAS das = DAS.FACTORY.createDAS(getConfig("basicCustomerOrderMapping.xml"), getConnection());
        // Read some customers and related orders
        Command select = das
                .createCommand("SELECT * FROM CUSTOMER LEFT JOIN ANORDER ON CUSTOMER.ID = ANORDER.CUSTOMER_ID");

        DataObject root = select.executeQuery();

        DataObject cust1 = root.getDataObject("CUSTOMER[1]");     

        // Save IDs
        Integer cust1ID = (Integer) cust1.get("ID");
        
        // Move an order to cust1 from cust2
        DataObject order = root.createDataObject("ANORDER");
        order.setInt("ID", 500);
        order.setInt("CUSTOMER_ID", cust1ID.intValue());
        cust1.getList("orders").add(order);       
       
        try {
            das.applyChanges(root);
            fail("An exception should be thrown");
        } catch (RuntimeException ex) {
            assertEquals("Foreign key properties should not be set when the corresponding relationship has changed", ex.getMessage());
        }
    }
    
    public void testInvalidFKColumn() throws SQLException {
        ConfigHelper helper = new ConfigHelper();
        Relationship r = helper.addRelationship("CUSTOMER.ID", "ANORDER.CUSTOMER_ID_INVALID");
        r.setName("orders");
       

        DAS das = DAS.FACTORY.createDAS(helper.getConfig(), getConnection());
        Command select = das.createCommand("select * from CUSTOMER left join ANORDER "
                + "ON CUSTOMER.ID = ANORDER.CUSTOMER_ID");

        DataObject root = select.executeQuery();  
        DataObject cust1 = root.getDataObject("CUSTOMER[1]");  
        DataObject order = root.createDataObject("ANORDER");
        order.setInt("ID", 500);
        cust1.getList("orders").add(order);   
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
            assertEquals("Invalid foreign key column: CUSTOMER_ID_INVALID", ex.getMessage());
        }
    }
    
    /*If <Table> is present in DAS Config with type and property mappings, with tableName/typeName and columnName/propertyName differing
     * consider relationship with proper mapping*/
    public void testRelationshipTypesAndProperties() throws Exception {
    	//existing records
        DAS das = DAS.FACTORY.createDAS(getConfig("CustomersOrdersRelationship.xml"), getConnection());
        Command cmd = das.getCommand("customer and orders");
        cmd.setParameter("ID", new Integer(1));
        DataObject root = cmd.executeQuery();
        DataObject firstCustomer = root.getDataObject("Customer[id=1]");
        
        DataObject newOrder = root.createDataObject("AnOrder");
        newOrder.setInt("OrderId", 100);
        newOrder.setString("PRODUCT", "MyProd");
        
        firstCustomer.getList("orders").add(newOrder);
        
        das.applyChanges(root);
        
        root = cmd.executeQuery();
        firstCustomer = root.getDataObject("Customer[id=1]");
        assertEquals(3, firstCustomer.getList("orders").size());
    }
    
    /*same as testRelationshipTypesAndProperties(), except no DAS Config, and so table/type and column/property name match by default*/
    public void testRelationshipWithProgrammaticConfig() throws Exception {
    	//existing records
    	ConfigHelper configHelper = new ConfigHelper();
    	Config config = configHelper.getConfig();
    	MappingWrapper wrapper = new MappingWrapper(config);
    	wrapper.addRelationship("CUSTOMER.ID", "ANORDER.CUSTOMER_ID", "orders");
    	
        DAS das = DAS.FACTORY.createDAS(config, getConnection());
        Command cmd = das.createCommand("select * from CUSTOMER left join ANORDER on CUSTOMER.ID = ANORDER.CUSTOMER_ID where CUSTOMER.ID = 1");
        DataObject root = cmd.executeQuery();
        DataObject firstCustomer = root.getDataObject("CUSTOMER[ID=1]");
        
        DataObject newOrder = root.createDataObject("ANORDER");
        newOrder.setInt("ID", 100);
        newOrder.setString("PRODUCT", "MyProd");
        
        firstCustomer.getList("orders").add(newOrder);
        
        das.applyChanges(root);
        
        root = cmd.executeQuery();
        firstCustomer = root.getDataObject("CUSTOMER[ID=1]");
    }
    
    //Below is set of test cases with 1-1 key restricted, 1-1 no key restricted, 1-n
    //with parent/child having autogen keys and parent/child not having autogen keys
    //These will help in testing all corner cases of 1-1 relationship
    public void testOneToOneKeyRestrictedAutogenKeys() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConfig("OneToOneRestrictedConfig.xml"), getConnection());

        Command read = das.getCommand("get named employee with company");
        read.setParameter(1, "John Smith");
        DataObject root = read.executeQuery();
        DataObject john = root.getDataObject("EMPLOYEE[1]");

        //1-Update parent , Insert child/////////////////////////////////////////////
        john.setString("NAME", "johnNew");
        int origEOTMID = john.getInt("ID");
        
        DataObject comp = root.createDataObject("COMPANY");
        comp.setString("NAME", "comp100");
        john.setDataObject("company", comp);
        
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, "johnNew");
        root = read.executeQuery();
        john = root.getDataObject("EMPLOYEE[1]");
        assertNotNull(john);
        comp = john.getDataObject("company");        
        assertEquals(origEOTMID, comp.getInt("EOTMID"));
        assertEquals("comp100", comp.getString("NAME"));
        
        //2-Insert Parent, Update child///////////////////////////////////////////
        DataObject emp = root.createDataObject("EMPLOYEE");
        emp.setString("NAME", "NewEmp");
        emp.setDataObject("company", comp);//as key restricted, this will fail
        try {
            das.applyChanges(root);
            fail("Expected exception, as key restricted does not allow chages in relationship");
        } catch (RuntimeException ex) {
        }
        
        //3-Update Parent, Update Child
        read.setParameter(1, "johnNew");
        root = read.executeQuery();
        john = root.getDataObject("EMPLOYEE[1]");
        comp = john.getDataObject("company");
        
        john.setString("NAME", "johnOld");
        comp.setString("NAME", "comp101");
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, "johnOld");
        root = read.executeQuery();
        john = root.getDataObject("EMPLOYEE[1]");
        assertNotNull(john);
        comp = john.getDataObject("company");        
        assertEquals(origEOTMID, comp.getInt("EOTMID"));
        assertEquals("comp101", comp.getString("NAME"));
    }
    
    public void testOneToOneNoKeyRestrictedAutogenKeys() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConfig("OneToOneNonRestrictedConfig.xml"), getConnection());

        Command read = das.getCommand("get named employee with company");
        read.setParameter(1, "John Smith");
        DataObject root = read.executeQuery();
        DataObject john = root.getDataObject("EMPLOYEE[1]");
        int origEOTMID = john.getInt("ID");
        
        //1-Update parent , Insert child/////////////////////////////////////////////
        john.setString("NAME", "johnNew");
        
        DataObject comp = root.createDataObject("COMPANY");
        comp.setString("NAME", "comp100");
        john.setDataObject("company", comp);
        
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, "johnNew");
        root = read.executeQuery();
        john = root.getDataObject("EMPLOYEE[1]");
        assertNotNull(john);
        comp = john.getDataObject("company");        
        assertEquals(origEOTMID, comp.getInt("EOTMID"));
        assertEquals("comp100", comp.getString("NAME"));
        
        //2-Insert Parent, Update child///////////////////////////////////////////
        DataObject emp = root.createDataObject("EMPLOYEE");
        emp.setString("NAME", "NewEmp");
        emp.setDataObject("company", comp);//as no key restricted, this will not fail
        try {
            das.applyChanges(root);        
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        read.setParameter(1, "NewEmp");
        root = read.executeQuery();
        DataObject newEmp = root.getDataObject("EMPLOYEE[1]");
        assertNotNull(newEmp);
        int eotmid = newEmp.getInt("ID");
        comp = newEmp.getDataObject("company");        
        assertEquals(eotmid, comp.getInt("EOTMID"));
        assertEquals("comp100", comp.getString("NAME"));        
        
        //3-Update Parent, Update Child
        newEmp.setString("NAME", "johnOld");
        comp.setString("NAME", "comp101");
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, "johnOld");
        root = read.executeQuery();
        newEmp = root.getDataObject("EMPLOYEE[1]");
        assertNotNull(newEmp);
        comp = newEmp.getDataObject("company");        
        assertEquals(eotmid, comp.getInt("EOTMID"));
        assertEquals("comp101", comp.getString("NAME")); 	
    }
    
    public void testOneToOneKeyRestrictedNoAutogenKeys() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConfig("OneToOneRestrictedConfig.xml"), getConnection());

        Command read = das.getCommand("get spec with product");
        read.setParameter(1, "PQRPen");
        DataObject root = read.executeQuery();
        DataObject prod1 = root.getDataObject("PRODUCT[1]");
        int origProdId = prod1.getInt("ID");
        
        //1-Update parent , Insert child/////////////////////////////////////////////
        prod1.setString("NAME", "PQRNewPen");
        
        DataObject spec1 = root.createDataObject("PRODUCTSPEC");
        spec1.setInt("ID", 100);
        spec1.setString("NAME", "PQRNewPenSpec");
        prod1.setDataObject("prodspec", spec1);
        
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, "PQRNewPen");
        root = read.executeQuery();
        prod1 = root.getDataObject("PRODUCT[1]");
        assertNotNull(prod1);
        spec1 = prod1.getDataObject("prodspec");        
        assertEquals(origProdId, spec1.getInt("PRODUCT_ID"));
        assertEquals("PQRNewPenSpec", spec1.getString("NAME"));
        
        //2-Insert Parent, Update child///////////////////////////////////////////
        DataObject prod3 = root.createDataObject("PRODUCT");
        prod3.setString("NAME", "NewProd");
        prod3.setDataObject("prodspec", spec1);//as key restricted, this will fail
        try {
            das.applyChanges(root);
            fail("Expected exception, as key restricted does not allow changes in relationship");
        } catch (RuntimeException ex) {
        }
        
        //3-Update Parent, Update Child
        read.setParameter(1, "PQRNewPen");
        root = read.executeQuery();
        prod1 = root.getDataObject("PRODUCT[1]");
        spec1 = prod1.getDataObject("prodspec");        
        
        prod1.setString("NAME", "PQRProd");
        spec1.setString("NAME", "PQRProdSpec");
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, "PQRProd");
        root = read.executeQuery();
        prod1 = root.getDataObject("PRODUCT[1]");
        assertNotNull(prod1);
        spec1 = prod1.getDataObject("prodspec");        
        assertEquals(origProdId, spec1.getInt("PRODUCT_ID"));
        assertEquals("PQRProdSpec", spec1.getString("NAME"));    	
    }
    
    public void testOneToOneNoKeyRestrictedNoAutogenKeys() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConfig("OneToOneNonRestrictedConfig.xml"), getConnection());

        Command read = das.getCommand("get spec with product");
        read.setParameter(1, "PQRPen");
        DataObject root = read.executeQuery();
        DataObject prod1 = root.getDataObject("PRODUCT[1]");
        int origProdId = prod1.getInt("ID");
        
        //1-Update parent , Insert child/////////////////////////////////////////////
        prod1.setString("NAME", "PQRNewPen");
        
        DataObject spec1 = root.createDataObject("PRODUCTSPEC");
        spec1.setInt("ID", 100);
        spec1.setString("NAME", "PQRNewPenSpec");
        prod1.setDataObject("prodspec", spec1);
        
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, "PQRNewPen");
        root = read.executeQuery();
        prod1 = root.getDataObject("PRODUCT[1]");
        assertNotNull(prod1);
        spec1 = prod1.getDataObject("prodspec");        
        assertEquals(origProdId, spec1.getInt("PRODUCT_ID"));
        assertEquals("PQRNewPenSpec", spec1.getString("NAME"));
        
        //2-Insert Parent, Update child///////////////////////////////////////////
        DataObject prod3 = root.createDataObject("PRODUCT");
        prod3.setInt("ID", 200);
        prod3.setString("NAME", "NewProd");
        prod3.setDataObject("prodspec", spec1);//as no key restricted, this will not fail
        try {
            das.applyChanges(root);        
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        read.setParameter(1, "NewProd");
        root = read.executeQuery();
        DataObject newProd = root.getDataObject("PRODUCT[1]");
        assertNotNull(newProd);
        int product_id = newProd.getInt("ID");
        DataObject newSpec = newProd.getDataObject("prodspec");        
        assertEquals(product_id, newSpec.getInt("PRODUCT_ID"));
        assertEquals("PQRNewPenSpec", newSpec.getString("NAME"));       
        
        //3-Update Parent, Update Child
        newProd.setString("NAME", "MyProd");
        newSpec.setString("NAME", "XYZNewPenSpec");
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, "MyProd");
        root = read.executeQuery();
        newProd = root.getDataObject("PRODUCT[1]");
        assertNotNull(newProd);
        newSpec = newProd.getDataObject("prodspec");        
        assertEquals(product_id, newSpec.getInt("PRODUCT_ID"));
        assertEquals("XYZNewPenSpec", newSpec.getString("NAME"));  	
    }
    
    public void testOneToManyAutogenKeys() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConfig("CompanyConfig.xml"), getConnection());

        Command read = das.getCommand("all companies and departments");
        DataObject root = read.executeQuery();
        DataObject comp1 = (DataObject)root.getList("COMPANY").get(0);
        int compId = comp1.getInt("ID");
        
        //1-Update parent , Insert child/////////////////////////////////////////////
        comp1.setString("NAME", "MyCompany");
        
        DataObject dept1 = root.createDataObject("DEPARTMENT");
        dept1.setString("NAME", "MyCompanyDepartment");
        comp1.getList("departments").add(dept1);
        
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        //read.setParameter(1, new Integer(1));
        root = read.executeQuery();
        comp1 = root.getDataObject("COMPANY[ID="+compId+"]");
        assertNotNull(comp1);
        assertEquals("MyCompany", comp1.getString("NAME"));
        List comp1Depts = comp1.getList("departments");
        boolean foundNewDept = false;
        DataObject curDept = null;
        
        for(int i=0; i<comp1Depts.size(); i++) {
        	curDept = (DataObject)comp1Depts.get(i);
        	if(curDept.getString("NAME").equals("MyCompanyDepartment")) {
        		foundNewDept = true;
        		break;
        	}
        }
        
        if(!foundNewDept) {
        	fail("Expected new department to be available!");
        }
                
        assertEquals(comp1.getInt("ID"), curDept.getInt("COMPANYID"));
        
        //2-Insert Parent, Update child///////////////////////////////////////////
        DataObject compNew = root.createDataObject("COMPANY");
        compNew.setString("NAME", "CompanyNew");
        compNew.getList("departments").add(curDept);
        try {
            das.applyChanges(root);        
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        compNew = root.getDataObject("COMPANY[NAME='CompanyNew']");
        
        root = read.executeQuery();
        compNew = root.getDataObject("COMPANY[ID="+compNew.getInt("ID")+"]");
        assertNotNull(compNew);
        int newCompany_id = compNew.getInt("ID");
        List newCompDepts = compNew.getList("departments");        
        
        DataObject deptNew = (DataObject)newCompDepts.get(0);
        assertEquals(newCompany_id, deptNew.getInt("COMPANYID"));
        assertEquals("MyCompanyDepartment", deptNew.getString("NAME"));       
        
        //3-Update Parent, Update Child
        compNew.setString("NAME", "MyNewCompanyName");
        deptNew.setString("NAME", "XYZNewSoap123Dept");
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        //read.setParameter(1, new Integer(newCompany_id));
        root = read.executeQuery();
        compNew = root.getDataObject("COMPANY[ID="+newCompany_id+"]");
        assertNotNull(compNew);
        deptNew = (DataObject)compNew.getList("departments").get(0);        
        assertEquals(newCompany_id, deptNew.getInt("COMPANYID"));
        assertEquals("XYZNewSoap123Dept", deptNew.getString("NAME"));    	
    }
    
    public void testOneToManyNoAutogenKeys() throws Exception {    	
        DAS das = DAS.FACTORY.createDAS(getConfig("CustomersOrdersConfig.xml"), getConnection());

        Command read = das.getCommand("customer and orders");
        read.setParameter(1, new Integer(1));
        DataObject root = read.executeQuery();
        DataObject cust1 = root.getDataObject("CUSTOMER[1]");

        //1-Update parent , Insert child/////////////////////////////////////////////
        cust1.setString("LASTNAME", "MyCustomer");
        
        DataObject ord1 = root.createDataObject("ANORDER");
        ord1.setInt("ID", 100);
        ord1.setString("PRODUCT", "XYZNewProduct");
        cust1.getList("orders").add(ord1);
        
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, new Integer(1));
        root = read.executeQuery();
        cust1 = root.getDataObject("CUSTOMER[1]");
        assertNotNull(cust1);
        assertEquals("MyCustomer", cust1.getString("LASTNAME"));
        List cust1Ords = cust1.getList("orders");
        boolean foundNewOrder = false;
        DataObject curOrd = null;
        
        for(int i=0; i<cust1Ords.size(); i++) {
        	curOrd = (DataObject)cust1Ords.get(i);
        	if(curOrd.getString("PRODUCT").equals("XYZNewProduct")) {
        		foundNewOrder = true;
        		break;
        	}
        }
        
        if(!foundNewOrder) {
        	fail("Expected new order to be available!");
        }
                
        assertEquals(cust1.getInt("ID"), curOrd.getInt("CUSTOMER_ID"));
        
        //2-Insert Parent, Update child///////////////////////////////////////////
        DataObject custNew = root.createDataObject("CUSTOMER");
        custNew.setInt("ID", 100);
        custNew.setString("LASTNAME", "CustNew");
        custNew.getList("orders").add(curOrd);
        try {
            das.applyChanges(root);        
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, new Integer(100));
        root = read.executeQuery();
        custNew = root.getDataObject("CUSTOMER[1]");
        assertNotNull(custNew);
        int newCustomer_id = custNew.getInt("ID");
        List newCustOrds = custNew.getList("orders");        
        
        DataObject ordNew = (DataObject)newCustOrds.get(0);
        assertEquals(newCustomer_id, ordNew.getInt("CUSTOMER_ID"));
        assertEquals("XYZNewProduct", ordNew.getString("PRODUCT"));       
        
        //3-Update Parent, Update Child
        custNew.setString("LASTNAME", "MyNewLastName");
        ordNew.setString("PRODUCT", "XYZNewSoap123Product");
        try {
            das.applyChanges(root);
        } catch (RuntimeException ex) {
        	fail("Did not expect exception!");
        }
        
        read.setParameter(1, new Integer(100));
        root = read.executeQuery();
        custNew = root.getDataObject("CUSTOMER[1]");
        assertNotNull(custNew);
        ordNew = (DataObject)custNew.getList("orders").get(0);        
        assertEquals(100, ordNew.getInt("CUSTOMER_ID"));
        assertEquals("XYZNewSoap123Product", ordNew.getString("PRODUCT"));    	
    }
}