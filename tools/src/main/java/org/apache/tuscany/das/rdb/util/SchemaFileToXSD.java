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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.apache.tuscany.das.rdb.graphbuilder.schema.ResultSetTypeMap;

import commonj.sdo.Type;

public class SchemaFileToXSD {
	private static final Logger logger = Logger.getLogger(SchemaFileToXSD.class);
	private static XMLInputFactory xmlFactory;
	
	protected static DBSchema read(XMLStreamReader reader) throws Exception {
		DBSchema dbSchema = new DBSchema();
		Table table = null;
		Column column = null;
		ForeignKey fk = null;
		ForeignKeyRef fkRef = null;
		
        while (true) {
            int event = reader.next();
            if(javax.xml.stream.XMLStreamConstants.END_DOCUMENT == event) {
            	break;
            }
            
            switch (event) {
	            case javax.xml.stream.XMLStreamConstants.START_ELEMENT:
	                if (reader.getName().getLocalPart().equals("table")) {
	                	table = new Table();
	                	table.setName(reader.getAttributeValue(null, "name"));
	                } else if (reader.getName().getLocalPart().equals("column")) {
	                	column = new Column();
	                	column.setName(reader.getAttributeValue(null, "name"));
	                	column.setPK(Boolean.getBoolean(reader.getAttributeValue(null, "primaryKey")));
	                	column.setRequired(Boolean.getBoolean(reader.getAttributeValue(null, "required")));
	                	column.setType(reader.getAttributeValue(null, "type"));
	                } 
	                else if (reader.getName().getLocalPart().equals("foreign-key")) {
	                	fk = new ForeignKey();
	                	fk.setForeignTableName(reader.getAttributeValue(null,"foreignTable"));
	                }
	                else if (reader.getName().getLocalPart().equals("reference")) {
	                	fkRef = new ForeignKeyRef();
	                	fkRef.setForeign(reader.getAttributeValue(null,"foreign"));
	                	fkRef.setLocal(reader.getAttributeValue(null,"local"));
	                }
	                else if (reader.getName().getLocalPart().equals("database")) {
	                	//ignore
	                } else {
	                	throw new RuntimeException("not valid element:"+reader.getName()+":");
	                }
	                break;
	                
	            case javax.xml.stream.XMLStreamConstants.END_ELEMENT:
	            	if (reader.getName().getLocalPart().equals("table")) {
	            		dbSchema.getTables().add(table);
	            	}
	            	
	            	if (reader.getName().getLocalPart().equals("column")) {
	            		if(table != null) {
	            			table.getColumns().add(column);
	            		}
	            	}
	            	
	            	if (reader.getName().getLocalPart().equals("reference")) {
	            		if(fk != null) {
	            			fk.getFkRefs().add(fkRef);
	            		}
	            	}
	            	
	            	if (reader.getName().getLocalPart().equals("foreign-key")) {
	            		if(table != null) {
	            			table.getFks().add(fk);
	            		}
	            	}
	            	
	            	break;
            }
        }
        
        if (logger.isDebugEnabled()) {
        	logger.debug(dbSchema);
        }
        
        return dbSchema;
    }

	protected static void convert(String schemaFileName, String xsdModelFileName) throws Exception{
		xmlFactory = XMLInputFactory.newInstance();
		
		File schemaFile = new File(schemaFileName);
		FileInputStream flStrm = new FileInputStream(schemaFile);
		
		XMLStreamReader reader = xmlFactory.createXMLStreamReader(new InputStreamReader(flStrm));
		
		DBSchema dbSchema = read(reader);
		flStrm.close();
		generateXSD(dbSchema, xsdModelFileName);
	}
	
	protected static void generateXSD(DBSchema dbSchema, String xsdModelFileName) throws Exception {
		boolean writeFilesToDir = false;
		String startLine = "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:this=\"http:///org.apache.tuscany.das.rdb.test/schemaModel.xsd\" targetNamespace=\"http:///org.apache.tuscany.das.rdb.test/schemaModel.xsd\">\n";
		String endLine = "</xsd:schema>"; 
			
		if(xsdModelFileName != null) {
			writeFilesToDir = true;
		}
		
		//get all FKs to form a Map - PKtable -> FKTable , e.g. if CITIES has FK such that CITIES.STATE_ID is STATES.ID, then
		//the map shall have STATES -> CITIES, TABLE2,...
		Hashtable pkTofkTable = new Hashtable();
		
		for(int i=0; i<dbSchema.getTables().size(); i++) {
			Table curTable = (Table)dbSchema.getTables().get(i);
			for(int k=0; k<curTable.getFks().size(); k++) {
				ForeignKey curFK = (ForeignKey)curTable.getFks().get(k);
				String pkTableName = curFK.getForeignTableName();
				
				if(pkTofkTable.get(pkTableName) == null) {
					pkTofkTable.put(pkTableName, new ArrayList());
				}
				((ArrayList)pkTofkTable.get(pkTableName)).add(curTable.getName());
			}
		}
		
		String srcCode = startLine;
		
		for(int i=0; i<dbSchema.getTables().size(); i++) {
			Table curTable = (Table)dbSchema.getTables().get(i);
			srcCode = srcCode + "<xsd:complexType name=\""+curTable.getName()+"\">\n";
			srcCode = srcCode + "   <xsd:sequence>\n";
			
			for(int j=0; j<curTable.getColumns().size(); j++) {
				Column curColumn = (Column)curTable.getColumns().get(j);
				srcCode = srcCode + "      <xsd:element name=\""+	curColumn.getName();
				
				if(curColumn.isRequired()) {
					srcCode = srcCode + " nillable=\"false\"";
				}
				
				Type sdoType = ResultSetTypeMap.INSTANCE.getType(SQLTypeChecker.getSQLTypeFromString(curColumn.getType()), true);
				
				srcCode = srcCode + " type=\"xsd:"+SQLTypeChecker.xsdTypeForSDOType(sdoType.getName())+"\"/>\n";
			}
			
			if(pkTofkTable.get(curTable.getName()) != null) {
				ArrayList fkTables = (ArrayList)pkTofkTable.get(curTable.getName());
				
				for(int k=0; k<fkTables.size(); k++) {
					srcCode = srcCode + "      <xsd:element maxOccurs=\"unbounded\" name=\""+ fkTables.get(k)+"\" type=\"this:"+fkTables.get(k)+"\"/>\n";
				}
			}
			
			srcCode = srcCode + "   </xsd:sequence>\n";
			srcCode = srcCode + "</xsd:complexType>\n\n";
		}
		
		srcCode = srcCode + endLine;

		File javaFile = new File(xsdModelFileName);
		javaFile.createNewFile();
		PrintStream flStrm = new PrintStream(javaFile);
		
		if(writeFilesToDir) {
			flStrm.print(srcCode);
			flStrm.flush();
			flStrm.close();
		} else {
			System.out.print(srcCode);
		}
	}	
}