/**
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.tuscany.das.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.tuscany.das.rdb.util.DBToXSDGenerator;
import org.apache.tuscany.das.rdb.util.ModelXSDGenOption;

/**
 * @goal generate
 * @phase generate-resources
 * @description Generate XSD Model files from DB Schema or db schema XML file
 */
public class GeneratorMojo extends AbstractMojo {
    /**
     * The file containing db schema in xml
     *
     * @parameter property="schemaFile"
     */
    private String schemaFile;

    /**
     * The file that will contain Static SDO Model XSD
     * @parameter
     */
    private String modelFile;

    /**
     * Specifies the driverClass to use for connecting to database.
     *
     * @parameter
     */
    private String driverClass;

    /**
     * Specifies the databaseURL
     *
     * @parameter
     */
    private String databaseURL;

    /**
     * DB Schema name.
     *
     * @parameter
     */
    private String schemaName;

    /**
     * Optional user name.
     *
     * @parameter
     */
    private String userName;

    /**
     * Optional password.
     *
     * @parameter
     */
    private String password;

    public void execute() throws MojoExecutionException {
    	//if driverClass, databaseURL and schemaName present, use it to connect to DB ,
    	//if any from these 3 is absent, and schemaFile is present use it as input
    	ModelXSDGenOption mo =  new ModelXSDGenOption();
        if (null != driverClass && null != databaseURL && null != schemaName && null != schemaFile) {
        	mo.setDriverClass(driverClass);
        	mo.setDatabaseURL(databaseURL);
        	mo.setSchemaName(schemaName);
        	mo.setUserName(userName);
        	mo.setPassword(password);
        	mo.setSchemaFile(schemaFile);
        	mo.setModelFile(modelFile);

        	try {
        		DBToXSDGenerator.getModelFileFromDB(mo);
        	} catch(Exception e) {
        		throw new MojoExecutionException(e.getMessage());
        	}
        } else if(null != schemaFile) {
        	mo.setSchemaFile(schemaFile);
        	mo.setModelFile(modelFile); //if null, STDOUT
        	try {
        		DBToXSDGenerator.getModelFileFromSchemaFile(mo);
        	} catch(Exception e) {
        		throw new MojoExecutionException(e.getMessage());
        	}
        } else {
        	throw new MojoExecutionException("Provide DB Connection info or DB Schema XML file!");
        }
    }
}