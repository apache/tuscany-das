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
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import commonj.sdo.DataObject;

/**
 *
 * A ResultSetProcessor is used to transform the data in a ResultSet into a set of inter-related EDataObjects.
 */
public class ResultSetProcessor {
    private static final Logger logger = Logger.getLogger(ResultSetProcessor.class);

    private TableRegistry registry;

    private GraphBuilderMetadata metadata;

    private final DataObjectMaker doMaker;
    
    private final RowObjects tableObjects;

    public ResultSetProcessor(DataObject g, GraphBuilderMetadata gbmd) {

        this.metadata = gbmd;
        if (metadata.getRelationships().size() == 0) {
            registry = new SingleTableRegistry();
        } else {
            registry = new MultiTableRegistry();
        }

        doMaker = new DataObjectMaker(g);
        
        tableObjects = new RowObjects(metadata, registry);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug(metadata);
        }

    }

    /**
     * Process the ResultSet. For each row in the ResultSet, a
     *
     * @link ResultSetRow object will be created to represent the row as a set of EDataObjects. Then,
     * the relevant relationships will be constructed
     *       between each object in the
     * @link ResultSetRow.
     *
     * @param start
     * @param end
     */
    public void processResults(int start, int end) throws SQLException {

        Iterator i = metadata.getResultMetadata().iterator();
        while (i.hasNext()) {
            ResultMetadata resultMetadata = (ResultMetadata) i.next();
            ResultSet results = resultMetadata.getResultSet();

            processResultSet(results, resultMetadata, start, end);

            // TODO These statements HAVE to be closed or we will have major problems
            // results.getStatement().close();
            results.close();
        }
    }

    private void processResultSet(ResultSet rs, ResultMetadata rsMetadata, int start, int end) throws SQLException {
        ResultSetRow rsr = new ResultSetRow(rsMetadata);
        if (rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {
            while (rs.next() && start < end) {
            	rsr.processRow(rs);
                int rootRowsCreated = addRowToGraph(rsr, rsMetadata);
                start += rootRowsCreated;
            }
        } else {
        	int position = start;
            while (rs.absolute(position) && start < end) {
            	rsr.processRow(rs);
            	int rootRowsCreated = addRowToGraph(rsr, rsMetadata);
                start += rootRowsCreated;
                ++position;
            }
        }
    }

    /**
     * @param row
     * @param resultMetadata
     * @return the number of root rows created
     */
    private int addRowToGraph(ResultSetRow row, ResultMetadata resultMetadata) throws SQLException {
    	int rootRowsCreated = 0;
    	int objectsCreated = 0;
    	boolean recursive = row.isRecursive();
    	Set rootTableNames = metadata.getConfigWrapper().getRootTableNames();
        tableObjects.clear();
    	Iterator tables = row.getAllTableData().iterator();
        while (tables.hasNext()) {
            TableData rawDataFromRow = (TableData) tables.next();

            if (!rawDataFromRow.hasValidPrimaryKey() ||
            		(rawDataFromRow.hasNullPrimaryKey() && !rawDataFromRow.isTableEmpty())) {//some PK null , but other data present
            	//continue; - need to throw exception as anyway the result will give a wrong impression
            	//when any one table in result set misses PK column or has null value in PK column
            	throw new RuntimeException("Table "+rawDataFromRow.getTableName()+" in query does not include Primary Key "+
            			"column or has null value in it, can not proceed!");
            }

            String tableName = rawDataFromRow.getTableName();
            DataObject tableObject = registry.get(tableName, rawDataFromRow.getPrimaryKeyValues());
            boolean newlyCreated = (tableObject == null);
            // check whether row is a new root row
            if (newlyCreated) {
            	objectsCreated++;
            	// increment root row count
            	// in case of recursive table, assuming that first table occurrence is the root
                if (rootTableNames.contains(tableName) && rawDataFromRow.getIndex() == 0) rootRowsCreated++;
                // get whole table data 
                // (only for non-recursive statements; recursive statements already have the whole table data)
                if (!recursive) rawDataFromRow = row.processRowForTable(tableName);
            	// create data object
            	tableObject = doMaker.createAndAddDataObject(rawDataFromRow, resultMetadata);
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Putting table " + tableName + " with PK "
                            + rawDataFromRow.getPrimaryKeyValues() + " into registry");
                }

                registry.put(tableName, rawDataFromRow.getPrimaryKeyValues(), tableObject);
            }
            else{
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Not Null tableObject");
                }
            }

            if(tableObject != null){
            	if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Do not allow any Null tableObject in tableObjects");
                }
            	tableObjects.put(tableName, tableObject, newlyCreated);
            }
        }
        
        if (objectsCreated == 0) {
            // duplicated row
        	if (this.logger.isDebugEnabled()) {
        		this.logger.debug("Found duplicated row");
        	}
        } else {
        	tableObjects.processRelationships();
        }
        
        return rootRowsCreated;

    }

}
