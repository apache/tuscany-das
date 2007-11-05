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

import java.sql.Statement;
import java.sql.Connection;
import java.util.Properties;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.tuscany.das.rdb.DAS;
import org.apache.tuscany.das.rdb.test.framework.DasTest;

import commonj.sdo.DataObject;

public class ConnectionTests extends DasTest {
	Properties p = null;
	
    protected void setUp() throws Exception {        
        super.setUp();
        p = System.getProperties();
        p.put("derby.system.home", "target");

        EmbeddedDataSource ds = new EmbeddedDataSource();       
    	ds.setDatabaseName("dastestcon");
    	ds.setDataSourceName("java/dastestcon");
   		ds.setCreateDatabase("create");
   		ds.setUser("dasuser");
   		ds.setPassword("dasuser");
   		Connection connection = ds.getConnection("dasuser", "dasuser");//to actually create the database
   		Statement stmt = connection.createStatement();
   		try{
   			stmt.executeQuery("select * from BOOK");
   		} catch(Exception e) {
	   		stmt.execute("create table BOOK (id int, name varchar(30))");
	   		stmt.execute("insert into BOOK values (1, 'book1')");
   		}
   		connection.commit();
   		connection.close();   		
    }
    
    protected void tearDown() throws Exception {
    	super.tearDown();
    }

    /*As database is created with user, password, connection without same authentication can not look up data*/
    public void testConnectionUsingDriverManagerNoAuth() throws Exception {
    	try {
	   		DAS das = DAS.FACTORY.createDAS(getConfig("connectionInfoDriverManagerNoAuth.xml"));
	   		das.getCommand("getBook").executeQuery();
	   		fail("Expected exception, as DB is created with id, pwd, needs same to get data from DB!");
		} catch(Exception e) {
			//e.printStackTrace();
			assertTrue(true);//Expected exception
		}
    }
    
    /*As database is created with user, password, connection with same authentication can look up data*/
    public void testConnectionUsingDriverManagerAuth() throws Exception {
    	try {
	   		DAS das = DAS.FACTORY.createDAS(getConfig("connectionInfoDriverManagerAuth.xml"));
	   		DataObject root = das.getCommand("getBook").executeQuery();
	   		assertEquals(1, root.getList("BOOK").size());
		} catch(Exception e) {
			fail("Expected DAS to obtain authenticated connection using data source!");
		}
    }    
}
