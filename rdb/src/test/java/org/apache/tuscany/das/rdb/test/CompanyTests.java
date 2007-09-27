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

import org.apache.tuscany.das.rdb.Command;
import org.apache.tuscany.das.rdb.DAS;
import org.apache.tuscany.das.rdb.test.company.CompanyFactory;
import org.apache.tuscany.das.rdb.test.company.CompanyType;
import org.apache.tuscany.das.rdb.test.company.DepartmentType;
import org.apache.tuscany.das.rdb.test.company.EmployeeType;
import org.apache.tuscany.das.rdb.test.data.CompanyData;
import org.apache.tuscany.das.rdb.test.data.CompanyDeptData;
import org.apache.tuscany.das.rdb.test.data.DepEmpData;
import org.apache.tuscany.das.rdb.test.data.DepartmentData;
import org.apache.tuscany.das.rdb.test.data.EmployeeData;
import org.apache.tuscany.das.rdb.test.framework.DasTest;

import commonj.sdo.DataObject;
import commonj.sdo.helper.HelperContext;
import commonj.sdo.impl.HelperProvider;

public class CompanyTests extends DasTest {

    protected void setUp() throws Exception {
        super.setUp();

        new CompanyData(getAutoConnection()).refresh();
        new DepartmentData(getAutoConnection()).refresh();
        new EmployeeData(getAutoConnection()).refresh();
        new CompanyDeptData(getAutoConnection()).refresh();
        new DepEmpData(getAutoConnection()).refresh();

    }

    public void testSimple() throws Exception {

        DAS das = DAS.FACTORY.createDAS(getConfig("companyMapping.xml"), getConnection());

        // Build the select command
        Command selectCommand = das.createCommand("select COMPANY.ID, COMPANY.NAME, " 
                + "EMPLOYEE.ID, EMPLOYEE.NAME, EMPLOYEE.SN, EMPLOYEE.MANAGER, "
                + "DEPARTMENT.ID, DEPARTMENT.NAME, DEPARTMENT.LOCATION, DEPARTMENT.DEPNUMBER from COMPANY, DEPARTMENT, EMPLOYEE "
                + "where COMPANY.ID=DEPARTMENT.COMPANYID and DEPARTMENT.ID=EMPLOYEE.DEPARTMENTID");

        // Get the graph
        DataObject root = selectCommand.executeQuery();

        // Get a company
        DataObject company = (DataObject) root.getList("CompanyType").get(0);
        assertEquals("MegaCorp", company.get("NAME"));

        // Get a department
        DataObject department = (DataObject) company.getList("departments").get(0);
        assertEquals("Advanced Technologies", department.get("NAME"));

        DataObject employee = (DataObject) department.getList("employees").get(0);
        assertEquals("John Jones", employee.get("NAME"));
    }

    public void testSimpleStatic() throws Exception {

        DAS das = DAS.FACTORY.createDAS(getConfig("companyMappingWithConverters.xml"), getConnection());
        HelperContext context = HelperProvider.getDefaultContext();
        CompanyFactory.INSTANCE.register(context);
        // Build the select command
        Command selectCommand = das.createCommand("select COMPANY.ID, COMPANY.NAME, " 
                + "EMPLOYEE.ID, EMPLOYEE.NAME, EMPLOYEE.SN, EMPLOYEE.MANAGER, "
                + "DEPARTMENT.ID, DEPARTMENT.NAME, DEPARTMENT.LOCATION, DEPARTMENT.DEPNUMBER from COMPANY, DEPARTMENT, EMPLOYEE "
                + "where COMPANY.ID=DEPARTMENT.COMPANYID and DEPARTMENT.ID=EMPLOYEE.DEPARTMENTID");

        // Get the graph
        DataObject root = selectCommand.executeQuery();

        CompanyType company = (CompanyType) root.getList("CompanyType").get(0);

        assertEquals("MegaCorp", company.getName());

        // Get a department
        DepartmentType department = (DepartmentType) company.getDepartments().get(0);
        assertEquals("Advanced Technologies", department.getName());

        EmployeeType employee = (EmployeeType) department.getEmployees().get(0);

        assertEquals("John Jones", employee.getName());
    }

    /**
     * Default database setup inserts 3 records in COMPANY. This test verifies that these records have auto-gen keys
     * 1,2,3 and the new created record has auto-gen key 4 in COMPANY.ID
     * @throws Exception
     */
    public void testCreateAutoGenKeyStatic() throws Exception {
    	//get DAS and select command
    	DataObject root = null;
    	
    	DAS das = DAS.FACTORY.createDAS(getConfig("companyMappingWithConverters.xml"), getConnection());
    	HelperContext context = HelperProvider.getDefaultContext();
        CompanyFactory.INSTANCE.register(context);
        Command selectCommand = das.getCommand("AllCompanies");
        root = selectCommand.executeQuery();
        assertEquals(3, root.getList("CompanyType").size());
        //verify result
        CompanyType company = (CompanyType) root.getList("CompanyType").get(0);
        int startId = company.getId();
        assertEquals(startId, company.getId());
        assertEquals("ACME Publishing", company.getName());

        company = (CompanyType) root.getList("CompanyType").get(1);
        assertEquals(startId+1, company.getId());
        assertEquals("Do-rite plumbing", company.getName());
        
        company = (CompanyType) root.getList("CompanyType").get(2);
        assertEquals(startId+2, company.getId());
        assertEquals("MegaCorp", company.getName());
        
        //create a new company. auto get keys set in config and supported in table creation statement
        CompanyType newCompany = (CompanyType)root.createDataObject("CompanyType");
        newCompany.setName("MyNewCompany");
        das.applyChanges(root);        

        assertEquals("MyNewCompany", newCompany.getName());
        assertEquals(startId+3, newCompany.getId());
    }
    
    public void testCreateAutoGenKeyDynamic() throws Exception {
    	//get DAS and select command
    	DataObject root = null;
    	
    	DAS das = DAS.FACTORY.createDAS(getConfig("basicCompanyDepartmentMapping.xml"), getConnection());
        Command selectCommand = das.createCommand("select * from COMPANY");
        root = selectCommand.executeQuery();
        assertEquals(3, root.getList("COMPANY").size());
        //verify result
        DataObject company = (DataObject)root.getList("COMPANY").get(0);
        int startId = company.getInt("ID");
        assertEquals(startId, company.getInt("ID"));
        assertEquals("ACME Publishing", company.getString("NAME"));

        company = (DataObject)root.getList("COMPANY").get(1);
        assertEquals(startId+1, company.getInt("ID"));
        assertEquals("Do-rite plumbing", company.getString("NAME"));
        
        company = (DataObject)root.getList("COMPANY").get(2);
        assertEquals(startId+2, company.getInt("ID"));
        assertEquals("MegaCorp", company.getString("NAME"));
        
        //create a new company. auto get keys set in config and supported in table creation statement
        DataObject newCompany = root.createDataObject("COMPANY");
        newCompany.setString("NAME", "MyNewCompany");
        das.applyChanges(root);        

        assertEquals("MyNewCompany", newCompany.getString("NAME"));
        assertEquals(startId+3, newCompany.getInt("ID"));       
    }    
}
