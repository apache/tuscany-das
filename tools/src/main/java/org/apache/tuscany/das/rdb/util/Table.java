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

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String name;
	private List columns = new ArrayList();
	private List fks = new ArrayList();
	
	protected String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	protected List getColumns() {
		return columns;
	}
	protected void setColumns(List columns) {
		this.columns = columns;
	}
	protected List getFks() {
		return fks;
	}
	protected void setFks(List fks) {
		this.fks = fks;
	}
	
	public String toString() {
		StringBuffer dbSchemaStr = new StringBuffer();
		dbSchemaStr.append("_____Table_______"+this.name+"\n");
		dbSchemaStr.append("_____Columns_______\n");
		for(int i=0; i<this.columns.size(); i++) {
			Column curColumn = (Column)this.columns.get(i);
			dbSchemaStr.append(curColumn);
		}
		
		if(this.fks.size() > 0)
			dbSchemaStr.append("_____FKs_______\n");
		for(int i=0; i<this.fks.size(); i++) {
			ForeignKey curFk = (ForeignKey)this.fks.get(i);
			dbSchemaStr.append(curFk);
		}
		
		return dbSchemaStr.toString();
	}	
}
