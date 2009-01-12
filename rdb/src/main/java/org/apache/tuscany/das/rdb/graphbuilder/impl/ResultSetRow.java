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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 
 * A ResultSetRow is used to transform a row of a ResultSet into a set of EDataObjects.
 */
public class ResultSetRow {
    private static final Logger logger = Logger.getLogger(ResultSetRow.class);

    private final ResultMetadata metadata;
    private final boolean recursive;
    private final int resultSetSize;
    private Collection allTableNames;
    private Set tablesWithNoPK = new HashSet();
    private String[] tablePropertyNames;
    private String[] columnPropertyNames;
    private boolean[] isPKColumn;
    private Map tableMap = new HashMap();
    private List allTableData;

    /**
     * Method ResultSetRow.
     * 
     * @param m
     *            the result metadata
     */
    public ResultSetRow(ResultMetadata m) throws SQLException {
        this.metadata = m;
        this.recursive = m.isRecursive();
        this.resultSetSize = m.getResultSetSize();
        cacheMetadata();
        getAllTableNamesForRS();
        getTablesWithNoPK();
    }
    
    /**
     * Processes a single row in the ResultSet.
     * 
     * @param rs
     *            A ResultSet positioned on the desired row
     */
    public void processRow(ResultSet rs)  throws SQLException {
    	// clear previous data 
    	for (Iterator itTableData = tableMap.values().iterator(); itTableData.hasNext(); ) {
    		TableData tableData = (TableData) itTableData.next();
    		tableData.clear();
    	}
        allTableData = null;

        // process row
        if (recursive) {
            processRecursiveRow(rs);
        } else {
        	processNonRecursiveRow(rs);
        }
    }

    private void processNonRecursiveRow(ResultSet rs) throws SQLException {

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("");
        }
        for (int i = 1; i <= resultSetSize; i++) {
        	Object data = getObject(rs, i);
			TableData table = getRawData(tablePropertyNames[i]);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Adding column: " + columnPropertyNames[i] + "\tValue: " 
                        + data + "\tTable: "
                        + tablePropertyNames[i]);
            }
            table.addData(columnPropertyNames[i], isPKColumn[i], data);
		}
        checkResultSetMissesPK();
        }

    //get all table names involved in current result set
    //can not use metadata.getAllTablePropertyNames()
    //as it gives table names for all tables from Config
    private void getAllTableNamesForRS(){
    	allTableNames = new HashSet(resultSetSize);
    	for (int i = 1; i <= resultSetSize; i++) {
    		allTableNames.add(tablePropertyNames[i]);
    	}
    }
    
    private void cacheMetadata() {
    	tablePropertyNames = new String[resultSetSize + 1];
    	columnPropertyNames = new String[resultSetSize + 1];
    	isPKColumn = new boolean[resultSetSize + 1];
        for (int i = 1; i <= resultSetSize; i++) {
        	tablePropertyNames[i] = metadata.getTablePropertyName(i);
        	columnPropertyNames[i] = metadata.getColumnPropertyName(i);
        	isPKColumn[i] = metadata.isPKColumn(i);
        }
    }
    	
    private void getTablesWithNoPK(){
        //case when result set omits PK column, take care of compound PKs too
        boolean tableRSHasPK;
        Iterator itr = allTableNames.iterator();
        while(itr.hasNext()){
        	tableRSHasPK = false;
        	String currentTableName = (String)itr.next();
        	HashSet pks = metadata.getAllPKsForTable(currentTableName);
        	HashSet pksInRS = new HashSet();
        	for(int j=1; j<=resultSetSize; j++){
            	if(currentTableName.equals(tablePropertyNames[j]) &&
            			isPKColumn[j] ){
            		pksInRS.add(columnPropertyNames[j]);
            	}
            }
        	
        	//if pks null, means its classic case when all cols should be PKs
        	if(pks == null){
        		tableRSHasPK = true;
        	}        	
        	//case when there were cols in cfg but could not find any PK in it and no ID column in cfg 
        	else if(pks != null && pks.size()==1 && pks.contains("")){
        		tableRSHasPK = false;        		
        	}        	
        	else if(pks != null && pksInRS.size() == pks.size()){        		
        		Iterator itr1 = pks.iterator();
        		int indx=0;
        		while(itr1.hasNext()){
        			if(!pksInRS.contains((String)itr1.next())){
        				indx++;			
        			}
        		}
        		
	        	if(indx == 0){
	        		if (this.logger.isDebugEnabled()) {
	        			this.logger.debug("has PK TRUE - matched");	
	        		}	        		
	        		tableRSHasPK = true;	
	        	}else{
	        		if (this.logger.isDebugEnabled()) {
	        			this.logger.debug("has PK FALSE- mismatched");	
	        		}	        		
	        		tableRSHasPK = false;
	        	}
        	}
        	else{
        		if (this.logger.isDebugEnabled()) {
        			this.logger.debug("has PK FALSE - rest all cases");	
        		}
        	}        	
        	
        	if (!tableRSHasPK) tablesWithNoPK.add(currentTableName);

    		if (this.logger.isDebugEnabled()) {
            	this.logger.debug("table "+currentTableName+" hasValidPK "+tableRSHasPK);
    		}
        }
    }

    private void checkResultSetMissesPK(){
    	//Default is TRUE(from TableData), so consider only FALSE case
        Iterator itr = tablesWithNoPK.iterator();
        while(itr.hasNext()){
        	String currentTableName = (String)itr.next();
           	TableData table = getRawData(currentTableName);
           	table.setValidPrimaryKey(false);
        }
    }
    
    private void processRecursiveRow(ResultSet rs) throws SQLException {
        this.allTableData = new ArrayList();
        int i = 1;

        while (i <= resultSetSize) {
            TableData table = new TableData(tablePropertyNames[i]);
            this.allTableData.add(table);

            while ((i <= resultSetSize) && (isPKColumn[i])) {
                Object data = getObject(rs, i);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Adding column: " + columnPropertyNames[i]
                            + "\tValue: " + data + "\tTable: "
                            + tablePropertyNames[i]);
                }
                table.addData(columnPropertyNames[i], true, data);
                i++;
            }

            while ((i <= resultSetSize) && (!isPKColumn[i])) {
                Object data = getObject(rs, i);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Adding column: " + columnPropertyNames[i] 
                            + "\tValue: " + data + "\tTable: "
                            + tablePropertyNames[i]);
                }
                table.addData(columnPropertyNames[i], false, data);
                i++;
            }
        }
        
        checkResultSetMissesPK();        
    }

    /**
     * @param rs
     * @param metadata
     * @param i
     * @return
     */
    private Object getObject(ResultSet rs, int i) throws SQLException {

        Object data = rs.getObject(i);

        if (rs.wasNull()) {
            return null;
        } 
            
        return metadata.getConverter(i).getPropertyValue(data);
        
    }

    /**
     * Returns a HashMap that holds data for the specified table
     * 
     * @param tableName
     *            The name of the table
     * @return HashMap
     */
    public TableData getTable(String tableName) {
        return (TableData) tableMap.get(tableName);
    }

    /**
     * Returns a HashMap that holds data for the specified table If the HashMap 
     * doesn't exist, it will be created. This is used internally to build
     * the ResultSetRow, whereas getTable is used externally to retrieve existing table data.
     * 
     * @param tableName
     *            The name of the table
     * @return HashMap
     */
    private TableData getRawData(String tableName) {

        TableData table = (TableData) tableMap.get(tableName);

        if (table == null) {
            table = new TableData(tableName);
            tableMap.put(tableName, table);
        }

        return table;
    }
    
    public List getAllTableData() {
        if (this.allTableData == null) {
            this.allTableData = new ArrayList();
            this.allTableData.addAll(tableMap.values());
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(allTableData);
        }

        return this.allTableData;
    }

}
