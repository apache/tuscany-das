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

public class ForeignKey {
	private String foreignTableName;
	private List fkRefs = new ArrayList();
	
	protected String getForeignTableName() {
		return foreignTableName;
	}
	protected void setForeignTableName(String foreignTableName) {
		this.foreignTableName = foreignTableName;
	}
	protected List getFkRefs() {
		return fkRefs;
	}
	protected void setFkRefs(List fkRefs) {
		this.fkRefs = fkRefs;
	}
	
	public String toString() {
		StringBuffer dbSchemaStr = new StringBuffer();
		dbSchemaStr.append("_____Foreign Key_______"+this.foreignTableName+"\n");
		for(int i=0; i<fkRefs.size(); i++) {
			ForeignKeyRef curFkRef = (ForeignKeyRef)fkRefs.get(i); 
			dbSchemaStr.append(curFkRef);
		}

		return dbSchemaStr.toString();
	}	
}
