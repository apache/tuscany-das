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

import java.sql.Types;

public class SQLTypeChecker {
	
	public static int getSQLTypeFromString(String sqlTypeString) {
		if(sqlTypeString.equals("CHAR")) return Types.CHAR;
		if(sqlTypeString.equals("VARCHAR")) return Types.VARCHAR;
		if(sqlTypeString.equals("LONGVARCHAR")) return Types.LONGVARCHAR;
		if(sqlTypeString.equals("NUMERIC")) return Types.NUMERIC;
		if(sqlTypeString.equals("DECIMAL")) return Types.DECIMAL;
		if(sqlTypeString.equals("BIT")) return Types.BIT;
		if(sqlTypeString.equals("BOOLEAN")) return Types.BOOLEAN;
		if(sqlTypeString.equals("TINYINT")) return Types.TINYINT;
		if(sqlTypeString.equals("SMALLINT")) return Types.SMALLINT;
		if(sqlTypeString.equals("INTEGER")) return Types.INTEGER;
		if(sqlTypeString.equals("BIGINT")) return Types.BIGINT;
		if(sqlTypeString.equals("REAL")) return Types.REAL;
		if(sqlTypeString.equals("FLOAT")) return Types.FLOAT;
		if(sqlTypeString.equals("DOUBLE")) return Types.DOUBLE;
		if(sqlTypeString.equals("BINARY")) return Types.BINARY;
		if(sqlTypeString.equals("VARBINARY")) return Types.VARBINARY;
		if(sqlTypeString.equals("LONGVARBINARY")) return Types.LONGVARBINARY;
		if(sqlTypeString.equals("DATE")) return Types.DATE;
		if(sqlTypeString.equals("TIME")) return Types.TIME;
		if(sqlTypeString.equals("TIMESTAMP")) return Types.TIMESTAMP;
		if(sqlTypeString.equals("CLOB")) return Types.CLOB;
		if(sqlTypeString.equals("BLOB")) return Types.BLOB;
		if(sqlTypeString.equals("ARRAY")) return Types.ARRAY;
		if(sqlTypeString.equals("DISTINCT")) return Types.DISTINCT;
		if(sqlTypeString.equals("STRUCT")) return Types.STRUCT;
		if(sqlTypeString.equals("REF")) return Types.REF;
		if(sqlTypeString.equals("DATALINK")) return Types.DATALINK;
		if(sqlTypeString.equals("JAVA_OBJECT")) return Types.JAVA_OBJECT;
		return Types.OTHER;
	}

	/*xsd : SDO 
	anySimpleType Object
	anyType DataObject
	anyURI URI
	base64Binary Bytes
	boolean Boolean
	byte Byte
	date YearMonthDay
	dateTime DateTime
	decimal Decimal
	double Double
	duration Duration
	ENTITIES Strings
	ENTITY String
	float Float
	gDay Day
	gMonth Month
	gMonthDay MonthDay
	gYear Year
	gYearMonth YearMonth
	hexBinary Bytes
	ID String
	IDREF String
	IDREFS Strings
	int Int
	integer Integer
	language String
	long Long
	Name String
	NCName String
	negativeInteger Integer
	NMTOKEN String
	NMTOKENS Strings
	nonNegativeInteger Integer
	nonPositiveInteger Integer
	normalizedString String
	NOTATION String
	positiveInteger Integer
	QName URI
	short Short
	string String
	time Time
	token String
	unsignedByte Short
	unsignedInt long
	unsignedLong Integer
	unsignedShort Int*/
	static final String anySimpleTypeXSD = "anySimpleType";
	static final String anyTypeXSD = "anyType";
	static final String anyURIXSD = "anyURI";
	static final String base64BinaryXSD = "base64Binary";
	static final String booleanXSD = "boolean";
	static final String byteXSD = "byte";
	static final String dateXSD = "date";
	static final String dateTimeXSD = "dateTime";
	static final String decimalXSD = "decimal";
	static final String doubleXSD = "double";
	static final String durationXSD = "duration";
	static final String ENTITIESXSD = "ENTITIES";
	static final String ENTITYXSD = "ENTITY";
	static final String floatXSD = "float"; 
	static final String gDayXSD = "gDay"; 
	static final String gMonthXSD = "gMonth"; 
	static final String gMonthDayXSD = "gMonthDay"; 
	static final String gYearXSD = "gYear"; 
	static final String gYearMonthXSD = "gYearMonth"; 
	static final String hexBinaryXSD = "hexBinary"; 
	static final String IDXSD = "ID"; 
	static final String IDREFXSD = "IDREF"; 
	static final String IDREFSXSD = "IDREF"; 
	static final String intXSD = "int"; 
	static final String integerXSD = "integer"; 
	static final String languageXSD = "language"; 
	static final String longXSD = "long"; 
	static final String NameXSD = "Name"; 
	static final String NCNameXSD = "NCName"; 
	static final String negativeIntegerXSD = "negativeInteger"; 
	static final String NMTOKENXSD = "NMTOKEN"; 
	static final String NMTOKENSXSD = "NMTOKENS";
	static final String nonNegativeIntegerXSD = "nonNegativeInteger"; 
	static final String nonPositiveIntegerXSD = "nonPositiveInteger"; 
	static final String normalizedStringXSD = "normalizedString"; 
	static final String NOTATIONXSD = "NOTATION"; 
	static final String positiveIntegerXSD = "positiveInteger"; 
	static final String QNameXSD = "QName"; 
	static final String shortXSD = "short"; 
	static final String stringXSD = "string"; 
	static final String timeXSD = "time"; 
	static final String tokenXSD = "token"; 
	static final String unsignedByteXSD = "unsignedByte"; 
	static final String unsignedIntXSD = "unsignedInt"; 
	static final String unsignedLongXSD = "unsignedLong"; 
	static final String unsignedShortXSD = "unsignedShort"; 
	
	public static String xsdTypeForSDOType(String sdoTypeName) {
		if(sdoTypeName.equals("Object")) return anySimpleTypeXSD;
		if(sdoTypeName.equals("DataObject")) return anyTypeXSD;
		if(sdoTypeName.equals("URI")) return anyURIXSD;
		if(sdoTypeName.equals("Bytes")) return base64BinaryXSD;
		if(sdoTypeName.equals("Boolean")) return booleanXSD;
		if(sdoTypeName.equals("boolean")) return booleanXSD;
		if(sdoTypeName.equals("Byte")) return byteXSD;
		if(sdoTypeName.equals("YearMonthDay")) return dateXSD;
		if(sdoTypeName.equals("DateTime")) return dateTimeXSD;
		if(sdoTypeName.equals("Date")) return dateTimeXSD;
		if(sdoTypeName.equals("Decimal")) return decimalXSD;
		if(sdoTypeName.equals("Double")) return doubleXSD;
		if(sdoTypeName.equals("double")) return doubleXSD;
		if(sdoTypeName.equals("Duration")) return durationXSD;
		if(sdoTypeName.equals("Strings")) return ENTITIESXSD;
		if(sdoTypeName.equals("String")) return stringXSD;
		if(sdoTypeName.equals("Float")) return floatXSD;
		if(sdoTypeName.equals("float")) return floatXSD;
		if(sdoTypeName.equals("Day")) return gDayXSD;
		if(sdoTypeName.equals("Month")) return gMonthXSD;
		if(sdoTypeName.equals("MonthDay")) return gMonthDayXSD;
		if(sdoTypeName.equals("Year")) return gYearXSD;
		if(sdoTypeName.equals("YearMonth")) return gYearMonthXSD;
		if(sdoTypeName.equals("Bytes")) return hexBinaryXSD;
		if(sdoTypeName.equals("Int")) return intXSD;
		if(sdoTypeName.equals("IntObject")) return intXSD;
		if(sdoTypeName.equals("Integer")) return integerXSD;
		if(sdoTypeName.equals("Long")) return longXSD;		
		if(sdoTypeName.equals("long")) return longXSD;
		if(sdoTypeName.equals("Short")) return shortXSD;
		if(sdoTypeName.equals("Time")) return timeXSD;
		if(sdoTypeName.equals("long")) return unsignedIntXSD;
		return "";
	}
}
