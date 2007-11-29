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

import java.io.InputStream;

public class DBToXSDGenerator {
	/**
	 * Output is dbSchemaFile as specified in dbInfoFileName.schemaFile
	 * @param dbInfoFileName  name of file which contains db connection info and output files names for dbschema and model 
	 * @throws Exception
	 */	
	public static void getSchemaFileFromDB(String dbInfoFileName) throws Exception {
		DBToSchemaFile.read(dbInfoFileName);
		DBToSchemaFile.schemaFileFromDB();
	}
	
	/**
	 * If user supplies schemaFileName and modelFileName will use it instead of the one from DBToSchemaFile.
	 * schemaFile should exist. If modelFileName is null, will use STDOUT.
	 * @param schemaFileName then one with Torque output
	 * @throws Exception
	 */
	public static void getModelFileFromSchemaFile(String schemaFileName, String modelFileName) throws Exception {
		SchemaFileToXSD.convert(schemaFileName, modelFileName);
	}
	
	/**
	 * All in one
	 * @param dbInfoFileName
	 * @throws Exception
	 */
	public static void getModelFileFromDB(String dbInfoFileName) throws Exception {
		DBToSchemaFile.read(dbInfoFileName);
		DBToSchemaFile.schemaFileFromDB();
		getModelFileFromSchemaFile(DBToSchemaFile.getSchemaFileName(), DBToSchemaFile.getModelFileName());
	}
	
    protected static InputStream getStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }    
}
