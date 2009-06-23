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
    private Set recursiveTablePropertyNames;
    private Set tablesWithNoPK = new HashSet();
    private String[] tablePropertyNames;
    private String[] columnPropertyNames;
    private boolean[] isPKColumn;
    private int[] indexesForPKs;
    private Map indexesByTablePropertyName = new HashMap();
    private Map tableMap = new HashMap();
    private List allTableData = new ArrayList();
    private ResultSet currentResultSet;

    /**
     * Method ResultSetRow.
     * 
     * @param m
     *            the result metadata
     */
    public ResultSetRow(ResultMetadata m) throws SQLException {
        this.metadata = m;
        this.recursiveTablePropertyNames = m.getRecursiveTypeNames();
        this.recursive = (recursiveTablePropertyNames.size() > 0);
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
    public final void processRow(ResultSet rs)  throws SQLException {
    	// clear previous data
    	for (Iterator itTableData = allTableData.iterator(); itTableData.hasNext(); ) {
    		TableData tableData = (TableData) itTableData.next();
    		tableData.clear();
    	}
        allTableData = new ArrayList();
        // set current resultSet
        currentResultSet = rs;
        // process row
        if (recursive) {
            processRecursiveRow();
        } else {
        	processNonRecursiveRow();
        }
    }
    
    public final TableData processRowForTable(String tablePropertyName) throws SQLException {
        int[] indexes = (int[]) indexesByTablePropertyName.get(tablePropertyName);
		TableData table = getRawData(tablePropertyName);
        int count = indexes.length;
        for (int j = 0; j < count; j++) {
        	int i = indexes[j];
        	if (!isPKColumn[i]) {
        		// skipping primary key columns since they've already been processed by processRow()
	        	Object data = getObject(currentResultSet, i);
	            if (this.logger.isDebugEnabled()) {
	                this.logger.debug("Adding column: " + columnPropertyNames[i] + "\tValue: " 
	                        + data + "\tTable: "
	                        + tablePropertyNames[i]);
	            }
	            table.addData(columnPropertyNames[i], false, data);
        	}
		}
        return table;
    }

    private void processNonRecursiveRow() throws SQLException {
        // parse primary keys only
        // the rest will be parsed as needed
        int count = indexesForPKs.length;
        for (int j = 0; j < count; j++) {
        	int i = indexesForPKs[j];
        	Object data = getObject(currentResultSet, i);
        	if (data == null) {
        		// primary key is null, check other columns
        		String tablePropertyName = tablePropertyNames[i];
        		// if table data already exists then this has already been done
        		if (!tableMap.containsKey(tablePropertyName)) {
        			TableData table = getRawData(tablePropertyName);
        			processRowForTable(tablePropertyName);
        			// add table data only if not empty
        			if (!table.isTableEmpty()) {
        	            table.addData(columnPropertyNames[i], true, data);
        				allTableData.add(table);
        			}
        		}
        	} else {
        		// add table data
				TableData table = getRawData(tablePropertyNames[i]);
				if (!allTableData.contains(table)) allTableData.add(table);
	            if (this.logger.isDebugEnabled()) {
	                this.logger.debug("Adding column: " + columnPropertyNames[i] + "\tValue: " 
	                        + data + "\tTable: "
	                        + tablePropertyNames[i]);
	            }
	            table.addData(columnPropertyNames[i], true, data);
        	}
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
    	String tablePropertyName = null;
    	boolean isPK;
    	List pkColumnList = new ArrayList();
    	// loop thru indexes
        for (int i = 1; i <= resultSetSize; i++) {
        	columnPropertyNames[i] = metadata.getColumnPropertyName(i);
        	tablePropertyName = metadata.getTablePropertyName(i);
        	tablePropertyNames[i] = tablePropertyName;
        	List indexes = (List) indexesByTablePropertyName.get(tablePropertyName);
        	if (indexes == null) {
        		indexes = new ArrayList();
        		indexesByTablePropertyName.put(tablePropertyName, indexes);
        	}
        	indexes.add(new Integer(i));
        	isPK = metadata.isPKColumn(i);
        	isPKColumn[i] = isPK;
        	if (isPK) {
        		pkColumnList.add(new Integer(i));
        	}
        }
        // reorganize indexes by table property name
        for (Iterator itTablePropertyNames = indexesByTablePropertyName.keySet().iterator(); itTablePropertyNames.hasNext(); ) {
        	tablePropertyName = (String) itTablePropertyNames.next();
        	List indexes = (List) indexesByTablePropertyName.get(tablePropertyName);
        	int count = indexes.size();
        	int[] indexArray = new int[count];
        	for (int i = 0; i < count; i++) {
        		indexArray[i] = ((Integer) indexes.get(i)).intValue();
        	}
        	indexesByTablePropertyName.put(tablePropertyName, indexArray);
        }
        // reorganize indexes for PKs
        int count = pkColumnList.size();
        indexesForPKs = new int[count];
        for (int i = 0; i < count; i++) {
        	indexesForPKs[i] = ((Integer) pkColumnList.get(i)).intValue();
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
			allTableData.add(table);
        }
    }
    
    private void processRecursiveRow() throws SQLException {
        int i = 1;
        // create map to keep track of recursive indexes
        // each recursive table contains a 0-based index to keep track of the sequence
        Map recursiveIndexes = new HashMap();
        for (Iterator itTablePropertyNames = recursiveTablePropertyNames.iterator(); itTablePropertyNames.hasNext(); ) {
        	recursiveIndexes.put(itTablePropertyNames.next(), new Integer(-1));
        }
        
        // loop thru result set columns
        // assuming that the columns of each recursive table are grouped together (metadata do not allow for further granularity)
        while (i <= resultSetSize) {
        	TableData table;
        	String tablePropertyName = tablePropertyNames[i];
        	if (recursiveTablePropertyNames.contains(tablePropertyName)) {
        		// increment current recursive index for table
        		int recursiveIndex = ((Integer) recursiveIndexes.get(tablePropertyName)).intValue() + 1;
        		recursiveIndexes.put(tablePropertyName, new Integer(recursiveIndex));
        		// get table data
                table = getRecursiveRawData(tablePropertyName, recursiveIndex);
        	} else {
                table = getRawData(tablePropertyNames[i]);
        	}
 
            while ((i <= resultSetSize) && (isPKColumn[i])) {
                Object data = getObject(currentResultSet, i);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Adding column: " + columnPropertyNames[i]
                            + "\tValue: " + data + "\tTable: "
                            + tablePropertyNames[i]);
                }
                table.addData(columnPropertyNames[i], true, data);
                i++;
            }

            while ((i <= resultSetSize) && (!isPKColumn[i])) {
                Object data = getObject(currentResultSet, i);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Adding column: " + columnPropertyNames[i] 
                            + "\tValue: " + data + "\tTable: "
                            + tablePropertyNames[i]);
                }
                table.addData(columnPropertyNames[i], false, data);
                i++;
            }
            
            // skip table if empty
            if (table.isTableEmpty()) {
            	table.clear();
            } else {
            	this.allTableData.add(table);
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
            
        return metadata.convert(i, data);
        
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
    
    private TableData getRecursiveRawData(String tableName, int recursiveIndex) {
    	String key = tableName + ":" + recursiveIndex;
        TableData table = (TableData) tableMap.get(key);

        if (table == null) {
            table = new TableData(tableName, recursiveIndex);
            tableMap.put(key, table);
        }

        return table;
    }

    public List getAllTableData() {
        return this.allTableData;
    }
    
    public boolean isRecursive() {
    	return recursive;
    }
    
}
