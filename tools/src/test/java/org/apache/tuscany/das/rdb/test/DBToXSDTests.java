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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.tuscany.das.rdb.dbconfig.DBInitializer;
import org.apache.tuscany.das.rdb.util.DBToXSDGenerator;
/**
 * Tests the DB Schema to Model XSD converter tool
 */
public class DBToXSDTests  extends TestCase {
	protected void setUp() throws Exception {	    
        super.setUp();
        DBInitializer dbInitializer = new DBInitializer("dbConfig.xml");
        dbInitializer.initializeDatabase(true);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	static public String getContents(File aFile) {
	    //...checks on aFile are elided
	    StringBuffer contents = new StringBuffer();

	    //declared here only to make visible to finally clause
	    BufferedReader input = null;
	    try {
	      //use buffering, reading one line at a time
	      //FileReader always assumes default encoding is OK!
	      input = new BufferedReader( new FileReader(aFile) );
	      String line = null; //not declared within while loop
	      /*
	      * readLine is a bit quirky :
	      * it returns the content of a line MINUS the newline.
	      * it returns null only for the END of the stream.
	      * it returns an empty String if two newlines appear in a row.
	      */
	      while (( line = input.readLine()) != null){
	        contents.append(line);
	        contents.append(System.getProperty("line.separator"));
	      }
	    }
	    catch (FileNotFoundException ex) {
	      ex.printStackTrace();
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    finally {
	      try {
	        if (input!= null) {
	          //flush and close both "input" and its underlying FileReader
	          input.close();
	        }
	      }
	      catch (IOException ex) {
	        ex.printStackTrace();
	      }
	    }
	    return contents.toString();
	  }
	
    String compare1 = "<table javaName=\"STATES\" name=\"STATES\">";
    String compare2 = "<table javaName=\"CITIES\" name=\"CITIES\">";
    String compare3 = "<xsd:complexType name=\"CITIES\">";
    String compare4 = "<xsd:complexType name=\"STATES\">";

	public void testDBToSchemaFile() throws Exception {
		DBToXSDGenerator.getSchemaFileFromDB("DBConnectionConfig.xml");
		File file = new File("target/dbSchema.txt");
		if(file.isFile()) {
			String schemaContent = getContents(file);
			if(schemaContent.indexOf(compare1) != -1 && schemaContent.indexOf(compare2) != -1) {
				assertTrue(true);
			}
		} else {
			fail("SchemaFile does not exists or has improper contents!");
		}
	}
	
	public void testSchemaFileToXSD() throws Exception {
		DBToXSDGenerator.getModelFileFromSchemaFile("target/dbSchema.txt", "target/schemaModel.xsd");
		
		File modelfile = new File("target/schemaModel.xsd");
		if(modelfile.isFile()) {
			String modelContent = getContents(modelfile);
			if(modelContent.indexOf(compare3) != -1 && modelContent.indexOf(compare4) != -1) {
				assertTrue(true);
			}
		} else {
			fail("ModelFile does not exists or has improper contents!");
		}		
	}
	
	public void testDBToXSD() throws Exception {
		DBToXSDGenerator.getModelFileFromDB("DBConnectionConfig.xml");
		
		File file = new File("target/dbSchema.txt");
		if(file.isFile()) {
			String schemaContent = getContents(file);
			if(schemaContent.indexOf(compare1) != -1 && schemaContent.indexOf(compare2) != -1) {
				assertTrue(true);
			}
		} else {
			fail("SchemaFile does not exists or has improper contents!");
		}
		
		File modelfile = new File("target/schemaModel.xsd");
		if(modelfile.isFile()) {
			String modelContent = getContents(modelfile);
			if(modelContent.indexOf(compare3) != -1 && modelContent.indexOf(compare4) != -1) {
				assertTrue(true);
			}
		} else {
			fail("ModelFile does not exists or has improper contents!");
		}
	}
}
