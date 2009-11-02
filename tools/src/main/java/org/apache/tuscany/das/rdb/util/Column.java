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

public class Column {
	private String name;
	private String type;
	private boolean isPK;
	private boolean isRequired;
	
	protected String getName() {
		return name;
	}
	protected void setName(String name) {
		this.name = name;
	}
	protected String getType() {
		return type;
	}
	protected void setType(String type) {
		this.type = type;
	}
	protected boolean isPK() {
		return isPK;
	}
	protected void setPK(boolean isPK) {
		this.isPK = isPK;
	}
	protected boolean isRequired() {
		return isRequired;
	}
	protected void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	
	public String toString() {
		StringBuffer dbSchemaStr = new StringBuffer();
		dbSchemaStr.append("Name:"+this.getName()+", Type:"+this.getType()+", isPK:"+this.isPK()+", isRequired:"+this.isRequired()+"\n");
		return dbSchemaStr.toString();
	}	
}
