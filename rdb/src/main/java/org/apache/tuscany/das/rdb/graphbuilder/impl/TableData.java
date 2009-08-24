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
package org.apache.tuscany.das.rdb.graphbuilder.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public final class TableData {
    private static final Logger logger = Logger.getLogger(TableData.class);

    private Map columnData;

    private List primaryKey;;

    private final String name;
    
    private int index = 0;

    private boolean hasValidPrimaryKey = true;

    private boolean hasNullPrimaryKey = false;
    
    public TableData(String tableName) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Creating TableData for table " + tableName);
        }

        this.name = tableName;
        this.columnData = new HashMap();
        this.primaryKey = new ArrayList();
    }
    
    public TableData(String tableName, int index) {
    	this(tableName);
    	this.index = index;
    }
    
    public void clear() {
    	columnData.clear();
    	primaryKey.clear();
    	hasValidPrimaryKey = true;
    	hasNullPrimaryKey = false;
    }

    public void addData(String columnName, boolean isPrimaryKeyColumn, Object data) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Adding column " + columnName + " with value " + data);
        }

        if(data != null)
        	columnData.put(columnName, data);
        if (isPrimaryKeyColumn) {
            if (data == null) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Column " + columnName + " is a primary key column and is null");
                }
            	hasNullPrimaryKey = true;
                //hasValidPrimaryKey = false; - if uncommented and JIRA-1464, RecursiveTests.testReadEngineParts() will fail
            } else {
            	primaryKey.add(data);
            }
        }
    }

    public Object getColumnData(String columnName) {
        return columnData.get(columnName);
    }

    public String getTableName() {
        return this.name;
    }
    
    public int getIndex() {
    	return index;
    }

    /**
     * @return
     */
    public List getPrimaryKeyValues() {
    	// the primary key is kept in the table registry
    	// so return a copy to prevent deletion
    	List primaryKeyCopy = new ArrayList(primaryKey.size());
    	primaryKeyCopy.addAll(primaryKey);
        return primaryKeyCopy;
    }

    public boolean hasValidPrimaryKey() {
        return hasValidPrimaryKey;
    }
    
    public void setValidPrimaryKey(boolean hasValidPK){
    	this.hasValidPrimaryKey = hasValidPK;
    }
    
    public boolean isTableEmpty(){
    	return columnData.keySet().isEmpty();
    }
    
    public boolean hasNullPrimaryKey(){
    	return this.hasNullPrimaryKey;
    }    
}
