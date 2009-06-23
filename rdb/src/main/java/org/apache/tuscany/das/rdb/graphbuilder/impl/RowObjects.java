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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.tuscany.das.rdb.config.KeyPair;
import org.apache.tuscany.das.rdb.config.Relationship;
import org.apache.tuscany.das.rdb.config.wrapper.MappingWrapper;

import commonj.sdo.DataObject;
import commonj.sdo.Property;

public class RowObjects {
    private static final Logger logger = Logger.getLogger(RowObjects.class);

    private Map objectsByTableName;

    private List tableObjects;
    
    private Set newTableObjectNames;

    private Map tableTypeNames;
    
    private Map relationshipMap;

    private final GraphBuilderMetadata metadata;

    private final TableRegistry registry;
    
    private final MappingWrapper configWrapper;
    
    private final boolean hasRecursiveRelationships;

    public RowObjects(GraphBuilderMetadata metadata, TableRegistry registry) {
        objectsByTableName = new HashMap();
        tableObjects = new ArrayList();
        newTableObjectNames = new HashSet();
        tableTypeNames = new HashMap();
        this.metadata = metadata;
        this.registry = registry;
        this.configWrapper = metadata.getConfigWrapper();
        this.hasRecursiveRelationships = configWrapper.hasRecursiveRelationships();
        if (!hasRecursiveRelationships) buildRelationshipMap();
    }
    
    private void buildRelationshipMap() {
    	relationshipMap = new HashMap();
        Iterator i = metadata.getRelationships().iterator();
        while (i.hasNext()) {
            Relationship r = (Relationship) i.next();
            String parentTypeName = getTableTypeName(r.getPrimaryKeyTable());
            String childTypeName = getTableTypeName(r.getForeignKeyTable());
            // Add relationship under child type name
            List relationships = (List) relationshipMap.get(childTypeName);
            if (relationships == null) {
            	relationships = new ArrayList();
            	relationshipMap.put(childTypeName, relationships);
            }
            relationships.add(r);
            // Add relationship under parent type name
            relationships = (List) relationshipMap.get(parentTypeName);
            if (relationships == null) {
            	relationships = new ArrayList();
            	relationshipMap.put(parentTypeName, relationships);
            }
            relationships.add(r);
        }
    }
    
    public void clear() {
    	objectsByTableName.clear();
    	tableObjects.clear();
    	newTableObjectNames.clear();
    }

    public void put(String key, DataObject value, boolean newlyCreated) {
        objectsByTableName.put(key, value);
        tableObjects.add(value);
        if (newlyCreated) newTableObjectNames.add(key);
    }

    public DataObject get(String tablePropertyName) {
        return (DataObject) objectsByTableName.get(tablePropertyName);
    }
    
    private String getTableTypeName(String tableName) {
    	String typeName = (String) tableTypeNames.get(tableName);
    	if (typeName == null) {
    		typeName = configWrapper.getTableTypeName(tableName);
    		tableTypeNames.put(tableName, typeName);
    	}
    	return typeName;
    }

    public void processRelationships() {
        if (hasRecursiveRelationships) {
            processRecursiveRelationships(configWrapper);
            return;
        }
        
        // the relationship needs to be set only if the parent or the child is newly created
        // otherwise the relationship has already been set
        Set relationshipsToSet = new HashSet();
        Iterator itNewTableObjectNames = newTableObjectNames.iterator();
        while (itNewTableObjectNames.hasNext()) {
        	List relationships = (List) relationshipMap.get((String) itNewTableObjectNames.next());
        	if (relationships != null) relationshipsToSet.addAll(relationships);
        }
        
        Iterator i = relationshipsToSet.iterator();
        while (i.hasNext()) {
            Relationship r = (Relationship) i.next();

            String parentTypeName = getTableTypeName(r.getPrimaryKeyTable());
            String childTypeName = getTableTypeName(r.getForeignKeyTable());
            DataObject parent = get(parentTypeName);
            DataObject child = get(childTypeName);

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Parent table: " + parent);
                this.logger.debug("Child table: " + child);
            }

            setOrAdd(parent, child, r.getName());
        }
    }

    private void processRecursiveRelationships(MappingWrapper wrapper) {
        Iterator i = tableObjects.iterator();
        while (i.hasNext()) {
            DataObject table = (DataObject) i.next();

            Iterator relationships = wrapper.getRelationshipsByChildTable(table.getType().getName()).iterator();
            while (relationships.hasNext()) {
                Relationship r = (Relationship) relationships.next();

                DataObject parentTable = findParentTable(table, r, wrapper);


                setOrAdd(parentTable, table, r.getName());
            }

        }
    }

    private void setOrAdd(DataObject parent, DataObject child, String propertyName) {
        if (parent == null || child == null) return;
        Property p = parent.getType().getProperty(propertyName);
        if (p.isMany()) {
            parent.getList(p).add(child);
        } else {
            parent.set(p, child);
        }
    }

    private DataObject findParentTable(DataObject childTable, Relationship r, MappingWrapper wrapper) {

        List fkValue = new ArrayList();
        Iterator keyPairs = r.getKeyPair().iterator();
        while (keyPairs.hasNext()) {
            KeyPair pair = (KeyPair) keyPairs.next();
            String childProperty = wrapper.getColumnPropertyName(r.getPrimaryKeyTable(), pair.getForeignKeyColumn());

            Property p = childTable.getType().getProperty(childProperty);
            fkValue.add(childTable.get(p));
        }

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Trying to find parent of " + r.getForeignKeyTable() + " with FK " + fkValue);
        }

        DataObject parentTable = registry.get(r.getPrimaryKeyTable(), fkValue);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Parent table from registry: " + parentTable);
        }

        return parentTable;
    }

}
