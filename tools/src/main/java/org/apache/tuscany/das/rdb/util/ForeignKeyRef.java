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

public class ForeignKeyRef {
	private String foreign;
	private String local;
	
	protected String getForeign() {
		return foreign;
	}
	protected void setForeign(String foreign) {
		this.foreign = foreign;
	}
	protected String getLocal() {
		return local;
	}
	protected void setLocal(String local) {
		this.local = local;
	}
	
	public String toString() {
		StringBuffer dbSchemaStr = new StringBuffer();
		dbSchemaStr.append("Foreign:"+this.foreign+", Local:"+this.local+"\n");

		return dbSchemaStr.toString();
	}	
}
