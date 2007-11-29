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
import org.apache.torque.task.TorqueJDBCTransformTask;
import org.apache.tools.ant.Project;

public class DBToSchemaFile {
	private static final Logger logger = Logger.getLogger(DBToSchemaFile.class);
	
	private static String driverClass = null;
	private static String url = null;
	private static String schemaName = null;
	private static String userName = null;
	private static String password = null;
	private static String schemaFileName = null;//should not be null
	private static String modelFileName = null;//if null STDOUT
	
	protected static void schemaFileFromDB() throws Exception {
		Project p = new Project();
	    p.setBaseDir(new File("."));
		TorqueJDBCTransformTask tsk = new TorqueJDBCTransformTask();
		tsk.setProject(p);
		tsk.setDbDriver(driverClass);
		tsk.setDbUrl(url);
		tsk.setSameJavaName(true);
		tsk.setDbSchema(schemaName);
		tsk.setTaskName("jdbc");
		tsk.setDbUser(userName);
		tsk.setDbPassword(password);
		if(!schemaFileName.equals("")) {
			File schemaFile = new File( schemaFileName);
			schemaFile.createNewFile();
		}
		tsk.setOutputFile(schemaFileName);
		tsk.execute();
	}
	
	protected static void read(String dbInfoFileName) throws Exception {
		XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
		
		XMLStreamReader reader = xmlFactory.createXMLStreamReader(new InputStreamReader(DBToXSDGenerator.getStream(dbInfoFileName)));

        while (true) {
            int event = reader.next();
            if(javax.xml.stream.XMLStreamConstants.END_DOCUMENT == event) {
            	break;
            }
            
            switch (event) {            
	            case javax.xml.stream.XMLStreamConstants.START_ELEMENT: {
	               if (reader.getName().getLocalPart().equals("ConnectionProperties")) {
	                	driverClass = reader.getAttributeValue(null, "driverClass");
	                	url = reader.getAttributeValue(null, "databaseURL");
	                	schemaName = reader.getAttributeValue(null, "schemaName");
	                	userName = reader.getAttributeValue(null, "userName");
	                	if(userName == null)
	                		userName = "";
	                	password = reader.getAttributeValue(null, "password");
	                	if(password == null)
	                		password = "";
	                } else if (reader.getName().getLocalPart().equals("ConnectionInfo")) {	                
	                	//ignore
	                } else if (reader.getName().getLocalPart().equals("Config")) {
	                	//ignore
	                } else if (reader.getName().getLocalPart().equals("OutFiles")) {	                
	                	schemaFileName = reader.getAttributeValue(null, "schemaFile");
	                	modelFileName = reader.getAttributeValue(null, "modelFile");
	                } else {
	                	throw new RuntimeException("not got dbInfo  - tableNames List or connectionInfo:"+reader.getName()+":");
	                }
	                break;
	            }
            }            
        }
        
        if (logger.isDebugEnabled()) {
			logger.debug("driverClass:"+driverClass);
			logger.debug("url:"+url);
			logger.debug("schemaName:"+schemaName);
			logger.debug("schemaFileName:"+schemaFileName);
			logger.debug("modelFileName:"+modelFileName);
			logger.debug("userName:"+userName);
			logger.debug("password:"+password);
		}            

        if(driverClass == null || url == null || schemaName == null || schemaFileName == null || modelFileName == null) {
     	   throw new RuntimeException("Required inputs missing - check driverClass, url, schemaName, schemaFile, modelFile!");
        }

        return;
    }

	protected static String getSchemaFileName() {
		return schemaFileName;
	}

	protected static void setSchemaFileName(String schemaFileName) {
		DBToSchemaFile.schemaFileName = schemaFileName;
	}

	protected static String getModelFileName() {
		return modelFileName;
	}

	protected static void setModelFileName(String modelFileName) {
		DBToSchemaFile.modelFileName = modelFileName;
	}
	
}
