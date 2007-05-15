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
package org.apache.tuscany.das.rdb.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * Superclass to refresh data in the table named tableName
 * when the column names and sqlTypes are specified. Extends 
 * TestData.
 *
 */
public class TestDataWithExplicitColumns extends TestData {

    private String[] columns;

    private int[] sqlTypes;
    
    public String getTableName(){
    	return this.tableName;
    }
    
    public void setTableName(String tableName){
    	this.tableName = tableName;
    }
    
    public TestDataWithExplicitColumns(Connection c, Object[][] data, String[] columns, int[] sqlTypes) {
        super(c, data);
        this.columns = columns;
        this.sqlTypes = sqlTypes;
    }

    public TestDataWithExplicitColumns(Connection c, String tableName, Object[][] data, String[] columns, int[] sqlTypes) {
        this(c, data, columns, sqlTypes);
        this.setTableName(tableName);
    }
    
    private String getColumn(int i) {
        return columns[i - 1];
    }

    private int getSqlType(int i) {
        return sqlTypes[i - 1];
    }

    // Create an insert statement of the following form ...
    // "INSERT INTO table_name (column1, column2,...) VALUES (value1, value2,....)"
    // This is necessary for tables with a generated column since the PK value is not provided
    protected void insertRows() throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(getTableName());

        sql.append(" (");
        for (int i = 1; i <= size(); i++) {
            sql.append(getColumn(i));
            if (i < size()) {
                sql.append(',');
            }
        }
        sql.append(" )");

        sql.append(" values (");
        for (int i = 1; i < size(); i++) {
            sql.append("?,");
        }
        sql.append("?)");

        PreparedStatement ps = connection.prepareStatement(sql.toString());

        while (next()) {
            for (int i = 1; i <= size(); i++) {
                ps.setObject(i, getObject(i), getSqlType(i));
            }
            ps.execute();
            ps.clearParameters();
        }
    }
}
