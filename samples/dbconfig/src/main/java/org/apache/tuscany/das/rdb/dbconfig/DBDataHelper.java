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

package org.apache.tuscany.das.rdb.dbconfig;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class DBDataHelper {
    private static final String CLASS_NAME = "DBDataHelper";

    private final Logger logger = Logger.getLogger("DBDataHelper.class");

    private final DBConfig dbConfig;

    protected DBDataHelper(DBConfig dbConfig) {
        this.logger.log(Level.DEBUG, "DBDataHelper()");
        this.dbConfig = dbConfig;
    }

    public boolean isDatabasePopulated() {
        boolean isPopulated = true;

        this.logger.log(Level.DEBUG, CLASS_NAME + ".isDatabasePopulated()");

        Iterator tableIterator = dbConfig.getTable().iterator();
        while (tableIterator.hasNext()) {
            Table table = (Table) tableIterator.next();

            this.logger.log(Level.DEBUG, CLASS_NAME + ".isDatabasePopulated() calling isTablePopulated() for '" + table.getName() + "'");
            isPopulated = this.isTablePopulated(table.getName());
            if (isPopulated == false) {
                break;
            }
        }
        return isPopulated;
    }

    /**
     * 
     * @param tableName
     * @return Count of rows present in the table specified by tableName
     */
    protected boolean isTablePopulated(String tableName) {
        boolean isPopulated = false;
        Connection dbConnection = null;
        Statement dbStatement = null;

        try {
            dbConnection = DBConnectionHelper.createConnection(dbConfig.getConnectionInfo());
            dbStatement = dbConnection.createStatement();
            String sqlString = "select count(*) from " + tableName;

            this.logger.log(Level.DEBUG, CLASS_NAME + ".isTablePopulated()=> sqlString => '" + sqlString + "'");

            ResultSet rs = dbStatement.executeQuery(sqlString);
            rs.next();

            if (rs != null) {
                this.logger.log(Level.DEBUG, CLASS_NAME + ".isTablePopulated()=> pointer set");
            }

            int count = rs.getInt(1);
            this.logger.log(Level.DEBUG, CLASS_NAME + ".isTablePopulated()=> '" + tableName + "' => " + count);

            if (count > 0) {
                isPopulated = true;
            }

        } catch (SQLException e) {
            // ignore and return false
        } finally {
            try {
                dbStatement.close();
            } catch (SQLException e1) {
                // ignore and return false
            }
        }

        return isPopulated;
    }

    public void initializeDatabaseData() {
        Connection dbConnection = null;
        Statement dbStatement = null;

        try {
            dbConnection = DBConnectionHelper.createConnection(dbConfig.getConnectionInfo());
            dbStatement = dbConnection.createStatement();

            Iterator tableIterator = dbConfig.getTable().iterator();
            while (tableIterator.hasNext()) {
                Table table = (Table) tableIterator.next();

                Iterator dataIterator = table.getRow().iterator();
                while (dataIterator.hasNext()) {
                    String tableRow = (String) dataIterator.next();

                    String sqlString = "INSERT INTO " + table.getName() + " VALUES (" + tableRow + ")";

                    this.logger.log(Level.DEBUG, CLASS_NAME + ".initializeDatabaseData()=> sqlString => '" + sqlString + "'");

                    try {
                        dbStatement.executeQuery(sqlString);
                    }catch(SQLException e){
                        //ignore and jump to new table
                        this.logger.log(Level.DEBUG, CLASS_NAME + ".initializeDatabaseData() - Error inserting table data : " + e.getMessage(), e);
                    }

                }
            }
        } catch (SQLException e) {
            // ignore and return false
            this.logger.log(Level.DEBUG, CLASS_NAME + ".initializeDatabaseData() - Internal error : " +  e.getMessage(), e);
        } finally {
            try {
                dbStatement.close();
            } catch (SQLException e1) {
                // ignore and return false
            }
        }
    }

    // /**
    // *
    // * @return true, if data is present for all tables specified in Config, else
    // * return false.
    // */
    // protected boolean checkDataCreated(){
    // this.logger.log(Level.DEBUG, "DBDataHelper.checkDataCreated()");
    // if(this.dbInitHelper.tableNames != null){
    // for(int i=0; i<this.dbInitHelper.tableNames.size(); i++){
    // this.logger.log(Level.DEBUG, "DBDataHelper.checkDataCreated() calling checkDataExists() for "+
    // this.dbInitHelper.tableNames.get(i));
    // if(checkDataExists((String)this.dbInitHelper.tableNames.get(i)) == 0){
    // return false;
    // }
    // }
    // }
    // return true;
    // }
    //		
    // /**
    // *Create data in table only in case it is empty, for all the tables
    // *specified in Config.
    // */
    // protected void checkCreateTablesData()throws Exception{
    // if(this.dbInitHelper.tableNames != null){
    // for(int i=0; i<this.dbInitHelper.tableNames.size(); i++){
    // String tableName = (String)this.dbInitHelper.tableNames.get(i);
    // if(checkDataExists(tableName) == 0){
    // if(this.dbInitHelper.createOrImport.equals("create")){
    // createTableData(tableName);
    // }
    // if(this.dbInitHelper.createOrImport.equals("import")){
    // importTableData(tableName);
    // }
    // }
    // }
    // }
    // }
    //	
    // /**
    // *
    // * @param tableName
    // * @return Count of rows present in the table specified by tableName
    // */
    // protected int checkDataExists(String tableName){
    // Statement s = null;
    // try{
    // s = this.dbInitHelper.connection.createStatement();
    // String sqlString = "select count(*) from "+tableName;
    //			
    // this.logger.log(Level.DEBUG, "DBDataHelper.checkDataExists()-> sqlString:"+sqlString);
    //			
    // ResultSet rs = s.executeQuery(sqlString);
    // rs.next();
    //			
    // if(rs != null){
    // this.logger.log(Level.DEBUG, "DBDataHelper.checkDataExists()-> pointer set");
    // }
    //			
    // int cnt = rs.getInt(1);
    // this.logger.log(Level.DEBUG, "DBDataHelper.checkDataExists()-> "+tableName+" "+cnt);
    // return cnt;
    // }catch(SQLException e){
    // return 0;//table does not exist
    // }finally{
    // try{
    // s.close();
    // }catch(SQLException e1){
    // //ignore;
    // }
    // }
    // }
    //	
    // /**
    // * Test utility to print COMPANY, DEPARTMENT data to given PrintStream
    // * @param stream
    // * @throws SQLException
    // */
    // protected void readDBstdout(PrintStream stream) throws SQLException {
    // try {
    // Statement s = this.dbInitHelper.connection.createStatement();
    // ResultSet rs = s.executeQuery("SELECT ID, NAME FROM COMPANY ORDER BY ID");
    // while (rs.next()) {
    // stream.print(rs.getString(1));
    // stream.print(" ");
    // stream.print(rs.getString(2));
    // stream.print(" ");
    // stream.println();
    // int id = rs.getInt(1);
    // Statement s1 = this.dbInitHelper.connection.createStatement();
    // ResultSet rs1 = s1.executeQuery("SELECT ID, NAME, LOCATION, DEPNUMBER FROM DEPARTMENT WHERE ID=" + id);
    // stream.println("====Company Departments");
    // while (rs1.next()) {
    // stream.print("\t");
    // stream.print(rs1.getString(1));
    // stream.print(" ");
    // stream.print(rs1.getString(2));
    // stream.print(" ");
    // stream.print(rs1.getString(3));
    // stream.print(" ");
    // stream.print(rs1.getString(4));
    // stream.println();
    // }
    // rs1.close();
    // s1.close();
    // stream.println();
    // }
    // rs.close();
    // s.close();
    // this.dbInitHelper.connection.commit();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    //
    //	
    // /**
    // * Test utility to print table data for tables like COMPANY, DEPARTMENT
    // * when logging is ON
    // */
    // protected void checkTableData(String tableName){
    // Statement s = null;
    // try{
    // s = this.dbInitHelper.connection.createStatement();
    // String sqlString = "select * from "+tableName;
    //			
    // this.logger.log(Level.DEBUG, "DBDataHelper.checkTableData()-> sqlString:"+sqlString);
    //			
    // ResultSet rs = s.executeQuery(sqlString);
    //			
    // if(rs != null){
    // while(rs.next()){
    // if(tableName.equals("COMPANY")){
    // int ID = rs.getInt(1);
    // String NAME = rs.getString(2);
    // int EOTMID = rs.getInt(3);
    // this.logger.log(Level.DEBUG, "COMPANY:"+ ID+","+NAME+","+EOTMID);
    // }
    //					
    // if(tableName.equals("DEPARTMENT")){
    // int ID = rs.getInt(1);
    // String NAME = rs.getString(2);
    // String LOCATION = rs.getString(3);
    // String DEPNUMBER = rs.getString(4);
    // int COMPANYID = rs.getInt(5);
    // this.logger.log(Level.DEBUG, "DEPARTMENT:"+ ID+","+NAME+","+LOCATION+","+DEPNUMBER+","+COMPANYID);
    // }
    // }
    // }
    //			
    // }catch(SQLException e){
    // }finally{
    // try{
    // s.close();
    // }catch(SQLException e1){
    // //ignore;
    // }
    // }
    // }
    //	
    // /**
    // * Delete data from all the tables specified in Config.
    // * Assumption:table names listed in Config are in parent first fashion
    // * @return true upon successful data deletion, false otherwise
    // */
    // protected boolean deleteData(){
    // try{
    // Statement s = this.dbInitHelper.connection.createStatement();
    //		
    //			
    // if(this.dbInitHelper.tableNames != null){
    // //can generalize as below based on assumption
    // String sqlString = "DELETE FROM ";
    // for(int i=this.dbInitHelper.tableNames.size()-1; i>-1; i--){
    // String tableName = (String)this.dbInitHelper.tableNames.get(i);
    // s.execute(sqlString+tableName);
    // this.logger.log(Level.DEBUG, "DBDataHelper.deleteData()->deleted "+tableName +" data");
    // }
    // }
    //		
    // s.close();
    // this.logger.log(Level.DEBUG, "DBDataHelper.deleteData -> returning");
    // }catch(Exception e){
    // this.logger.log(Level.DEBUG, "DBDataHelper.deleteData ->encountered exception "+e.getMessage());
    // return false;
    // }
    // return true;
    // }
    //	
    // /**
    // * Delete existing data and "create" hardcoded set of data for all tables
    // * specified in Config based on data in classes like CompanyData,
    // * DepartmentData...
    // * @throws SQLException
    // */
    // protected void createTablesData() throws SQLException{
    // if(this.dbInitHelper.tableNames != null){
    // this.logger.log(Level.DEBUG, "DBDataHelper.createTablesData()->calling deleteData()");
    //			
    // //always force=true
    // deleteData();
    // for(int i=0; i<this.dbInitHelper.tableNames.size(); i++){
    // String tableName = (String)this.dbInitHelper.tableNames.get(i);
    //				
    // this.logger.log(Level.DEBUG, "DBDataHelper.createTablesData()->calling createTableData() for "+tableName);
    // createTableData(tableName);
    // }
    // }
    // }
    //	

    // /**
    // * "Import" data specified in Config into tables.
    // * @throws SQLException
    // */
    // protected void importTablesData() throws SQLException{
    // this.logger.log(Level.DEBUG, "DBDataHelper.importTablesData()");
    // String tableName=null;
    //		
    // if(this.dbInitHelper.dbConfig.getTablesData() != null){
    // TablesData tablesData = this.dbInitHelper.dbConfig.getTablesData();
    //			
    // List tableDataList = tablesData.getTableData();
    //			
    // if(tableDataList != null){
    // for(int i=0; i<tableDataList.size(); i++){
    // TableData tableData = (TableData)tableDataList.get(i);
    // tableName = tableData.getTableName();
    // this.logger.log(Level.DEBUG, "DBDataHelper.importTablesData()->calling importTableData() for "+tableName);
    // importTableData(tableName);
    // }
    // }
    // }
    // }
    //	
    // /**
    // * "Import" data specified in Config into table specified by tableName.
    // * @throws SQLException
    // */
    // protected void importTableData(String tableName) throws SQLException{
    // TestDataWithExplicitColumns testDataWithExplicitColumns = null;
    // String[] columns=null;
    // int[] sqlTypes=null;
    // Object[][] data=null;
    // TableData tableData = null;
    //		
    // if(this.dbInitHelper.dbConfig.getTablesData() != null){
    // TablesData tablesData = this.dbInitHelper.dbConfig.getTablesData();
    //			
    // List tableDataList = tablesData.getTableData();
    // if(tableDataList != null){
    // for(int i=0; i<tableDataList.size(); i++){
    // tableData = (TableData)tableDataList.get(i);
    // String curTableName = tableData.getTableName();
    // if(curTableName.equals(tableName)){
    // break;
    // }
    // }
    // }
    //			
    // if(tableData != null){
    // this.logger.log(Level.DEBUG, "DBDataHelper.importTableData() got TableData for "+tableName);
    // //column names
    // columns = formColumnsArray(tableData.getColumns());
    //				
    // this.logger.log(Level.DEBUG, "DBDataHelper.importTableData() formed columns");
    //				
    // //sql data types
    // sqlTypes = formSqlTypesArray(tableData.getSqlTypes());
    // this.logger.log(Level.DEBUG, "DBDataHelper.importTableData() formed sqlTypes");
    //				
    // List tableRows = tableData.getTableRow();
    // if(tableRows != null){
    // //actual data to be inserted
    // data = formData(tableRows);
    // this.logger.log(Level.DEBUG, "DBDataHelper.importTableData() formed data");
    // }
    //				
    // testDataWithExplicitColumns =
    // new TestDataWithExplicitColumns(this.dbInitHelper.connection, tableName, data, columns, sqlTypes);
    //				
    // testDataWithExplicitColumns.refresh();
    // this.logger.log(Level.DEBUG, "DBDataHelper.importTableData() - refreshed");
    // }
    // }
    // }
    //	
    // /**
    // *
    // * @param columns comma separated list of column names
    // * @return array of colum names
    // */
    // protected String[] formColumnsArray(String columns){
    // Vector columnsVect = formValues(columns);
    //		
    // //dump vector into array
    // String[] columnsArray = new String[columnsVect.size()];
    // for(int i=0; i<columnsVect.size(); i++){
    // columnsArray[i] = (String)columnsVect.get(i);
    // }
    // return columnsArray;
    // }
    //	
    // /**
    // *
    // * @param curStr comma separated list of column names
    // * @return vector of column names
    // */
    // protected Vector formValues(String curStr){
    // Vector<String> resultVect = new Vector<String>();
    // int quoteIdx;
    // int nextQuoteIdx;
    //
    // while(curStr != null && curStr.length() != 0){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formValues():"+curStr);
    // quoteIdx = curStr.indexOf("'");
    // if(quoteIdx == -1) break;
    //			
    // nextQuoteIdx = curStr.indexOf("'", quoteIdx+1);
    // if(nextQuoteIdx == -1) break;
    //		
    // this.logger.log(Level.DEBUG, "DBDataHelper.formValues() got:"+curStr.substring(quoteIdx+1, nextQuoteIdx));
    //			
    // resultVect.add(curStr.substring(quoteIdx+1, nextQuoteIdx));
    // if(curStr.length()>nextQuoteIdx+1){
    // curStr = curStr.substring(nextQuoteIdx+1);
    // }
    // else{
    // break;
    // }
    // }
    // this.logger.log(Level.DEBUG, "DBDataHelper.formValues(): returning");
    // return resultVect;
    // }
    //	
    // /**
    // *
    // * @param sqlTypes comma separated list of sql types
    // * @return int array corresponding to columns' sql types
    // */
    // protected int[] formSqlTypesArray(String sqlTypes){
    // Vector<Integer> typesVect = new Vector<Integer>();
    // StringTokenizer strtok = new StringTokenizer(sqlTypes, ",");
    //		
    // while(strtok.hasMoreTokens()){
    // String sqlTypeStr = strtok.nextToken();
    //			
    // if(sqlTypeStr.trim().equals("Types.VARCHAR")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding varchar");
    // typesVect.add(new Integer(java.sql.Types.VARCHAR));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.INTEGER")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding int");
    // typesVect.add(new Integer(java.sql.Types.INTEGER));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.DATE")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding date");
    // typesVect.add(new Integer(java.sql.Types.DATE));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.FLOAT")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding float");
    // typesVect.add(new Integer(java.sql.Types.FLOAT));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.DOUBLE")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding double");
    // typesVect.add(new Integer(java.sql.Types.DOUBLE));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.DECIMAL")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding decimal");
    // typesVect.add(new Integer(java.sql.Types.DECIMAL));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.ARRAY")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding array");
    // typesVect.add(new Integer(java.sql.Types.ARRAY));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.BIGINT")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding bigint");
    // typesVect.add(new Integer(java.sql.Types.BIGINT));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.BINARY")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding binary");
    // typesVect.add(new Integer(java.sql.Types.BINARY));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.BIT")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding bit");
    // typesVect.add(new Integer(java.sql.Types.BIT));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.BLOB")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding blob");
    // typesVect.add(new Integer(java.sql.Types.BLOB));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.BOOLEAN")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding boolean");
    // typesVect.add(new Integer(java.sql.Types.BOOLEAN));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.CHAR")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding char");
    // typesVect.add(new Integer(java.sql.Types.CHAR));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.CLOB")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding clob");
    // typesVect.add(new Integer(java.sql.Types.CLOB));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.DATALINK")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding datalink");
    // typesVect.add(new Integer(java.sql.Types.DATALINK));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.DISTINCT")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding distinct");
    // typesVect.add(new Integer(java.sql.Types.DISTINCT));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.JAVA_OBJECT")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding java_object");
    // typesVect.add(new Integer(java.sql.Types.JAVA_OBJECT));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.LONGVARBINARY")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding longvarbinary");
    // typesVect.add(new Integer(java.sql.Types.LONGVARBINARY));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.LONGVARCHAR")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding longvarchar");
    // typesVect.add(new Integer(java.sql.Types.LONGVARCHAR));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.NULL")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding null");
    // typesVect.add(new Integer(java.sql.Types.NULL));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.NUMERIC")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding numeric");
    // typesVect.add(new Integer(java.sql.Types.NUMERIC));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.OTHER")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding other");
    // typesVect.add(new Integer(java.sql.Types.OTHER));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.REAL")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding real");
    // typesVect.add(new Integer(java.sql.Types.REAL));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.REF")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding ref");
    // typesVect.add(new Integer(java.sql.Types.REF));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.SMALLINT")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding smallint");
    // typesVect.add(new Integer(java.sql.Types.SMALLINT));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.STRUCT")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding struct");
    // typesVect.add(new Integer(java.sql.Types.STRUCT));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.TIME")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding time");
    // typesVect.add(new Integer(java.sql.Types.TIME));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.TIMESTAMP")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding timestamp");
    // typesVect.add(new Integer(java.sql.Types.TIMESTAMP));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.TINYINT")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding tinyint");
    // typesVect.add(new Integer(java.sql.Types.TINYINT));
    // }
    //			
    // else if(sqlTypeStr.trim().equals("Types.VARBINARY")){
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): adding varbinary");
    // typesVect.add(new Integer(java.sql.Types.VARBINARY));
    // }
    // else{
    // throw new RuntimeException("Invalid SQL Type specified in TablesData in Config "+sqlTypeStr.trim());
    // }
    // }
    //		
    // //dump vector into array
    // int[] typesArray = new int[typesVect.size()];
    // for(int i=0; i<typesVect.size(); i++){
    // typesArray[i] = ((Integer)typesVect.get(i)).intValue();
    // }
    //		
    // this.logger.log(Level.DEBUG, "DBDataHelper.formSqlTypesArray(): returning");
    // return typesArray;
    // }
    //	
    // /**
    // *
    // * @param tableRows list of string, where each string has comma separated values
    // * @return array of rows of the table
    // */
    // protected Object[][] formData(List tableRows){
    // Vector currentRow;
    // Vector <Vector>allRows = new Vector<Vector>();
    //		
    // for(int i=0; i<tableRows.size(); i++){
    // String curRowStr = (String)tableRows.get(i);
    // currentRow = formValues(curRowStr);
    // this.logger.log(Level.DEBUG, "DBDataHelper.formData() adding in allRows");
    // allRows.add(currentRow);
    // }
    //		
    // //form 2D array
    // Object[][]data = new Object[allRows.size()][];
    // for(int i=0; i<allRows.size(); i++){
    // data[i] = new Object[ ((Vector)allRows.get(i)).size()];
    //			
    // for(int j=0; j<((Vector)allRows.get(i)).size(); j++){
    // data[i][j] = ((Vector)allRows.get(i)).get(j);
    // }
    // }
    //		
    // this.logger.log(Level.DEBUG, "DBDataHelper.formData() returning");
    // return data;
    // }
    //
}
