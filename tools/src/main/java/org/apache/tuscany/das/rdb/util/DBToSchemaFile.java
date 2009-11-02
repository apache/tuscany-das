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
package org.apache.tuscany.das.rdb.util;

import java.io.File;
import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.apache.tools.ant.Project;
import org.apache.torque.task.TorqueJDBCTransformTask;

public class DBToSchemaFile {
	private static final Logger logger = Logger.getLogger(DBToSchemaFile.class);
	private static ModelXSDGenOption mo = null;//schemaFileName should not be null. id modelFileName null STDOUT
	
	protected static void schemaFileFromDB() throws Exception {
		Project p = new Project();
	    p.setBaseDir(new File("."));
		TorqueJDBCTransformTask tsk = new TorqueJDBCTransformTask();
		tsk.setProject(p);
		tsk.setDbDriver(mo.getDriverClass());
		tsk.setDbUrl(mo.getDatabaseURL());
		tsk.setSameJavaName(true);
		tsk.setDbSchema(mo.getSchemaName());
		tsk.setTaskName("jdbc");
		tsk.setDbUser(mo.getUserName());
		tsk.setDbPassword(mo.getPassword());
		if(!mo.getSchemaFile().trim().equals("")) {
			File schemaFile = new File( mo.getSchemaFile());
			schemaFile.createNewFile();
		}
		tsk.setOutputFile(mo.getSchemaFile());
		tsk.execute();
	}
	
	protected static void read(String dbInfoFileName) throws Exception {
		XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
		
		XMLStreamReader reader = xmlFactory.createXMLStreamReader(new InputStreamReader(DBToXSDGenerator.getStream(dbInfoFileName)));
		mo = new ModelXSDGenOption();
        while (true) {
            int event = reader.next();
            if(javax.xml.stream.XMLStreamConstants.END_DOCUMENT == event) {
            	break;
            }
            
            switch (event) {            
	            case javax.xml.stream.XMLStreamConstants.START_ELEMENT: {
	               if (reader.getName().getLocalPart().equals("ConnectionProperties")) {
	            	   mo.setDriverClass(reader.getAttributeValue(null, "driverClass"));
	                   mo.setDatabaseURL(reader.getAttributeValue(null, "databaseURL"));
	                   mo.setSchemaName(reader.getAttributeValue(null, "schemaName"));
	                   mo.setUserName(reader.getAttributeValue(null, "userName"));
	                	if(mo.getUserName() == null)
	                		mo.setUserName("");
	                	mo.setPassword(reader.getAttributeValue(null, "password"));
	                	if(mo.getPassword() == null)
	                		mo.setPassword("");
	                } else if (reader.getName().getLocalPart().equals("ConnectionInfo")) {	                
	                	//ignore
	                } else if (reader.getName().getLocalPart().equals("Config")) {
	                	//ignore
	                } else if (reader.getName().getLocalPart().equals("OutFiles")) {	                
	                	mo.setSchemaFile(reader.getAttributeValue(null, "schemaFile"));
	                	mo.setModelFile(reader.getAttributeValue(null, "modelFile"));
	                } else {
	                	throw new RuntimeException("not got dbInfo  - tableNames List or connectionInfo:"+reader.getName()+":");
	                }
	                break;
	            }
            }            
        }
        
        if (logger.isDebugEnabled()) {
			logger.debug("driverClass:"+mo.getDriverClass());
			logger.debug("url:"+mo.getDatabaseURL());
			logger.debug("schemaName:"+mo.getSchemaName());
			logger.debug("schemaFileName:"+mo.getSchemaFile());
			logger.debug("modelFileName:"+mo.getModelFile());
			logger.debug("userName:"+mo.getUserName());
			logger.debug("password:"+mo.getPassword());
		}            

        if(mo.getDriverClass() == null || mo.getDatabaseURL() == null || mo.getSchemaName() == null || mo.getSchemaFile() == null 
        		|| mo.getModelFile() == null) {
     	   throw new RuntimeException("Required inputs missing - check driverClass, url, schemaName, schemaFile, modelFile!");
        }

        return;
    }

	public static ModelXSDGenOption getMo() {
		return mo;
	}

	public static void setMo(ModelXSDGenOption mo) {
		DBToSchemaFile.mo = mo;
	}
	
}
