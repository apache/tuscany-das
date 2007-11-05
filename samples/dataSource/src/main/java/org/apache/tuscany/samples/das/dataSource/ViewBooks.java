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
package org.apache.tuscany.samples.das.dataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tuscany.das.rdb.ConfigHelper;
import org.apache.tuscany.das.rdb.DAS;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import commonj.sdo.DataObject;

public class ViewBooks {
	ConfigHelper configHelper = null;
	
	public static void main(String[] args) throws Exception {
		if(args == null || args.length != 3 ) {
			System.out.println("Usage: java ViewBooks configFileName databaseName JNDIProviderFactory");
			System.exit(-1);
		}
		
		String configFileName = args[0];
		String databaseName = args[1];
		String JNDIProviderName = args[2];
		
		ViewBooks vb = new ViewBooks(configFileName);
		vb.bindAndcreateDataSource(databaseName, JNDIProviderName);
		vb.viewBooks();
	}
	
	protected ViewBooks(String configFileName) {
		configHelper = new ConfigHelper(getConfig(configFileName));
	}
	
    protected void bindAndcreateDataSource(String databaseName, String contextFactoryPath) throws Exception {
    	System.setProperty(Context.INITIAL_CONTEXT_FACTORY, contextFactoryPath);//"com.sun.jndi.fscontext.RefFSContextFactory"
		InitialContext initCtx = new InitialContext();		
		MysqlDataSource ds = new MysqlDataSource();
		ds.setCreateDatabaseIfNotExist(true);
		ds.setDatabaseName(databaseName);
		String urlStr = "jdbc:mysql://localhost/"+databaseName;
		ds.setUrl(urlStr);
		ds.setURL(urlStr);
				
		String userName = configHelper.getConfig().getConnectionInfo().getConnectionProperties().getUserName();
		String password = configHelper.getConfig().getConnectionInfo().getConnectionProperties().getPassword();
		String dataSource = configHelper.getConfig().getConnectionInfo().getDataSource();
		
		ds.setPassword(password);
		ds.setUser(userName);
		try{
			initCtx.rebind(dataSource, (javax.sql.DataSource)ds);
		}catch(NamingException ne){
			initCtx.bind(dataSource, (javax.sql.DataSource)ds);	
		}
		
		Connection con = ds.getConnection(userName, password);
		Statement stmt = con.createStatement();
		try {
			stmt.executeQuery("select * from BOOK");
			stmt.execute("delete from BOOK");
		} catch(SQLException sqe) {
			try {
				stmt.execute("create table BOOK (ID int, NAME varchar(30))");
			} catch(SQLException e) {
				e.printStackTrace();
				//ignore
			}
		} finally {
			try {
				stmt.execute("insert into BOOK values (1, 'StoryBook1')");
			} catch(SQLException e) {
				e.printStackTrace();
				//ignore
			}			
		}
    }
    
    /*As database is created with id/pwd, when same is used when getting DS Connection, works correct*/
    protected void viewBooks() throws Exception {
   		try {
	   		DAS das = DAS.FACTORY.createDAS(configHelper.getConfig());
	   		DataObject root = das.getCommand("getBook").executeQuery();
	   		List books = root.getList("BOOK");
	   		
	   		System.out.println("Books in store:"+books.size());
	   		System.out.println( ((DataObject)books.get(0)).getInt(0)+"    "+((DataObject)books.get(0)).getString(1));
	   		System.out.println("Success!");
   		} catch(Exception e) {
   			e.printStackTrace();
   		}
    }
    
    protected InputStream getConfig(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}
