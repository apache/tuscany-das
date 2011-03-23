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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tuscany.sdo.util.DataObjectUtil;

import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;
import commonj.sdo.helper.DataFactory;

public final class DataObjectMaker {

    private final DataObject rootObject;
    private final Map containmentPropertyMap;
    private final Map typeMap;

    private static final Logger logger = Logger.getLogger(DataObjectMaker.class);

    public DataObjectMaker(DataObject root) {
        this.rootObject = root;
        containmentPropertyMap = new HashMap();
        typeMap = new HashMap();
        Iterator i = this.rootObject.getType().getProperties().iterator();
        while (i.hasNext()) {
            Property p = (Property) i.next();
            Type type = p.getType();
            String typeName = type.getName();
            typeMap.put(typeName, type);
            if (p.isContainment()) {
            	containmentPropertyMap.put(typeName, p);
            }
        }
    }

    /**
     * @param tableData
     * @return
     */
    public DataObject createAndAddDataObject(TableData tableData, ResultMetadata resultMetadata) {
        // Get a Type from the package and create a standalone DataObject

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Looking for Type for " + tableData.getTableName());
        }

        String tableName = tableData.getTableName();
        Type tableClass = (Type) typeMap.get(tableName);

        if (tableClass == null) {
            throw new RuntimeException("An SDO Type with name " + tableData.getTableName() + " was not found");
        }

        DataObject obj = DataFactory.INSTANCE.create(tableClass);

        // Now, check to see if the root data object has a containment reference
        // to this EClass. If so, add it to the graph. If not, it will be taken
        // care of when we process relationships
        Property containmentProp = (Property) containmentPropertyMap.get(tableName);
        if (containmentProp != null) {
            if (containmentProp.isMany()) {
                rootObject.getList(containmentProp).add(obj);
            } else {
                this.rootObject.set(containmentProp, obj);
            }
        }

        // Set the column values
        Iterator columnNames = resultMetadata.getPropertyNames(tableData.getTableName()).iterator();
        Type objType = obj.getType();
        while (columnNames.hasNext()) {
            String propertyName = (String) columnNames.next();
            Property p = objType.getProperty(propertyName);
            if (p == null) {
            	// Try again, ignoring case
            	p = findProperty(objType, propertyName);
                if (p == null) {
                    throw new RuntimeException("Type " + obj.getType().getName() 
                            + " does not contain a property named " + propertyName);
                }
            }

            Object value = tableData.getColumnData(propertyName);
            try {
                obj.set(p, value);
            } catch (ClassCastException e) {
            	// a mismatch between the value and property types may happen in some cases
            	// e.g. when the property is a boolean but the database doesn't have a boolean data type
            	if (value != null) {
            		Object convertedValue = DataObjectUtil.getSetValue(p, value.toString());
            		obj.set(p, convertedValue);
            	}
            }
        }

        return obj;
    }

    // temporary, ignoring case
    private Property findProperty(Type type, String columnName) {
        Iterator properties = type.getProperties().iterator();
        while (properties.hasNext()) {
            Property p = (Property) properties.next();
            if (columnName.equalsIgnoreCase(p.getName())) {
                return p;
            }
        }
        return null;
    }

}
