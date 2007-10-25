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
package org.apache.tuscany.das.rdb.merge.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import org.apache.tuscany.das.rdb.config.Config;
import org.apache.tuscany.das.rdb.config.wrapper.MappingWrapper;
import org.apache.tuscany.das.rdb.config.wrapper.QualifiedColumn;
import org.apache.tuscany.das.rdb.graphbuilder.impl.MultiTableRegistry;
import org.apache.tuscany.das.rdb.graphbuilder.impl.TableRegistry;
import org.apache.tuscany.sdo.api.SDOUtil;

import commonj.sdo.ChangeSummary;
import commonj.sdo.DataGraph;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;
import commonj.sdo.helper.TypeHelper;
import commonj.sdo.impl.HelperProvider;

public class GraphMerger {

    private static Logger logger = Logger.getLogger("GraphMerger");

    private Map keys = new HashMap();

    private TableRegistry registry = new MultiTableRegistry();
    
    private Config config = null;//JIRA-962 for any new tests with schema , we need this

    // TODO lots of cleanup/design
    public GraphMerger() {
        // Empty Constructor
    }

    //JIRA-952
    public GraphMerger(Config cfg) {
        this.config = cfg;
    }
    
    public DataObject emptyGraph() {
    	if(this.config != null)
    		return emptyGraph(this.config);
    	else
    		return null;
    }
    
    // TODO Replace EMF reference with SDOUtil function when available
    // (Tuscany-583)
    public DataObject emptyGraph(Config config) {

        if (config.getDataObjectModel() == null) {
            throw new RuntimeException("DataObjectModel must be specified in the Config");
        }

        Type rootType = createDataGraphRoot();
        
        List types = SDOUtil.getTypes(HelperProvider.getDefaultContext(), config.getDataObjectModel());
        if (types == null) {
            throw new RuntimeException("SDO Types have not been registered for URI " + config.getDataObjectModel());
        }

        Iterator i = types.iterator();
        while (i.hasNext()) {
            Type type = (Type) i.next();
            Property property = getOrCreateProperty(rootType, type);
        }

        // Create the DataGraph
        DataGraph g = SDOUtil.createDataGraph();

        // Create the root object
        g.createRootObject(rootType);

        ChangeSummary summary = g.getChangeSummary();
        summary.beginLogging();

        return g.getRootObject();
    }

    private Type createDataGraphRoot() {
	    String uri = "http:///org.apache.tuscany.das.rdb/das";
	    TypeHelper typeHelper = HelperProvider.getDefaultContext().getTypeHelper();
	    Type rootType = null;
	    rootType = typeHelper.getType(uri+ "/DataGraphRoot", "DataGraphRoot");
	    if(rootType == null){
	    	rootType = SDOUtil.createType(HelperProvider.getDefaultContext(), uri+ "/DataGraphRoot", "DataGraphRoot", false);
	    	if (logger.isDebugEnabled()) {
	    		logger.debug("GraphMerger.createDataGraphRoot():creating type for "+uri);
	    	}        	
	    }
	    return rootType;
    }

    private Property getOrCreateProperty(Type rootType, Type type) {
        Property property = rootType.getProperty(type.getName());
        if( !(property != null && 
           (property.getType().isDataType()== type.isDataType()) &&
           (property.isContainment() == true) &&
           (property.isMany() == true)) ){
            property = SDOUtil.createProperty(rootType, type.getName(), type);
            SDOUtil.setContainment(property, true);
            SDOUtil.setMany(property, true);
            property = rootType.getProperty(type.getName());
        }
        return property;
     }
    
    private boolean isContainedWithChangeSummary(DataObject object) {
    	if(!object.getType().getName().equals("DataGraphRoot") || object.getChangeSummary() == null) {
    		return false;
    	}
    	return true;
    }
    
    private DataObject containWithChangeSummary(Type rootType, DataObject object) {
    	Property property = rootType.getProperty(object.getType().getName());
        
        if( property == null) {
            property = SDOUtil.createProperty(rootType, object.getType().getName(), object.getType());
            SDOUtil.setContainment(property, true);
            SDOUtil.setMany(property, true);
            property = rootType.getProperty(object.getType().getName());
        }
        
        DataGraph dataGraph = SDOUtil.createDataGraph();
        DataObject root = dataGraph.createRootObject(rootType);
        root.getDataGraph().getChangeSummary().beginLogging();
        createObjectWithSubtree(root, property, object);
        
        if(root.getDataGraph().getChangeSummary().getChangedDataObjects() != null) {
        	List cos = root.getDataGraph().getChangeSummary().getChangedDataObjects();
        	for(int i=0; i<cos.size(); i++) {
        		DataObject changedDO = (DataObject)cos.get(i);
        	}
        }
        //as we are just wrapping individual DOs in DataGraphRoot, and later during merge we will merge all such DOs, no need to have registry entry
        //right now.
        registry.remove(object.getType().getName(), Collections.singletonList(getPrimaryKey(object)));
        return root;
    }
    
    /**
     * If list contains at least one with DG and CS, select it, if none, return -1, if more than one, select the first one matching condition
     * @param graphs
     * @return
     */
    private int getPrimaryFromList(List graphs) {
    	//select primary
    	Iterator itr = graphs.iterator();
    	int index = -1;
    	int primaryIndex = -1;
    	while(itr.hasNext()) {
    		index++;
    		DataObject currentGraph = (DataObject)itr.next();
    		if(isContainedWithChangeSummary(currentGraph) && currentGraph.getChangeSummary() != null) {
    			primaryIndex = index;
    		}
    	}
    	return primaryIndex;
    }
    
    //JIRA-1815
    //preservePrimaryChangeSummary - default true - primary CS is preserved, secondary DOs are added without alteration to CS of primary - old behavior
    //preservePrimaryChangeSummary - false - secondaries are treated as CREATE and primary CS is altered for merges - way to INSERT with adhoc SDOs.
    public DataObject merge(List graphs, boolean preservePrimaryChangeSummary) {
    	DataObject primaryGraph = null;
    	int primaryIndex = getPrimaryFromList(graphs);

    	if(primaryIndex > -1) {
    		primaryGraph = (DataObject)graphs.remove(primaryIndex);
    	}

    	Iterator i = graphs.iterator();    	
    	if(primaryGraph == null) {
            if (i.hasNext()) {
            	primaryGraph = (DataObject)i.next();
            }    		
    	}
    	
        while (i.hasNext()) {
            primaryGraph = merge(primaryGraph, (DataObject) i.next(), preservePrimaryChangeSummary);
        }

        return primaryGraph;    	
    }
    
    public DataObject merge(List graphs) {
    	return merge(graphs, true);
    }

    public DataObject merge(DataObject primary, DataObject secondary, boolean preservePrimaryChangeSummary) {
    	Type rootType = createDataGraphRoot();
    	DataObject root1 = null;
    	DataObject root2 = null;

    	if(!isContainedWithChangeSummary(primary) && isContainedWithChangeSummary(secondary)) {
    	//swap as secondary has change history
    		DataObject temp = primary;
    		primary = secondary;
    		secondary = temp;
    	}
    	
        //if primary top type name is not DataGraphRoot , add it
    	if(!isContainedWithChangeSummary(primary)) {
    		root1 = containWithChangeSummary(rootType, primary);
    	} else {
    		root1 = primary;
    	}

        //if secondary top type name is not DataGraphRoot , add it
    	if(!isContainedWithChangeSummary(secondary)) {
    		root2 = containWithChangeSummary(rootType, secondary);
    	} else {
    		root2 = secondary;
    	}
    	       
        return mergeContained(root1, root2, preservePrimaryChangeSummary);    	
    }
    
    public DataObject merge(DataObject primary, DataObject secondary) {
    	return merge(primary, secondary, true);
    }
    
    public DataObject mergeContained(DataObject primary, DataObject secondary, boolean preservePrimaryChangeSummary) {   	
        addGraphToRegistry(primary);

        Iterator i = secondary.getType().getProperties().iterator();
        ChangeSummary summary = primary.getDataGraph().getChangeSummary();

        if(preservePrimaryChangeSummary && summary.isLogging()) {
        	summary.endLogging();
        }

        while (i.hasNext()) {
            Property p = (Property) i.next();

            Iterator objects = secondary.getList(p.getName()).iterator();            
            while (objects.hasNext()) {
                DataObject object = (DataObject) objects.next();
                createObjectWithSubtree(primary, p, object);
            }
        }

        if(preservePrimaryChangeSummary && !summary.isLogging()) {
        	summary.beginLogging();
        }
        
        return primary;
    }

    private void createObjectWithSubtree(DataObject root, Property p, DataObject object) {
        Object pk = getPrimaryKey(object);

        if (!registry.contains(object.getType().getName(), Collections.singletonList(pk))) {           
           	Iterator attrs = object.getType().getProperties().iterator();
    	    DataObject newObject = root.createDataObject(p.getName());
    	    
    	    createDataProperties(attrs, newObject, object);
            registry.put(object.getType().getName(), Collections.singletonList(pk), newObject);
            createReferenceProperties(root, newObject, object);            
        }

    }

    private void createDataProperties(Iterator attrs, DataObject newObject, DataObject object) {
	    while (attrs.hasNext()) {
	        Property attr = (Property) attrs.next();
	        if (attr.getType().isDataType()) {
	        	try {
	        		newObject.set(attr.getName(), object.get(attr));
	        	} catch(Exception e) {
	        		throw new RuntimeException("Graph structures do not match, can not merge! " + attr.getName());
	        	}
	        }
	    }
    }
    
    private void createReferenceProperties(DataObject root, DataObject newObject, DataObject object) {
        Iterator refs = object.getType().getProperties().iterator();
        while (refs.hasNext()) {
            Property ref = (Property) refs.next();
            if (!ref.getType().isDataType()) {
                List refObjects;
                if (!ref.isMany()) {
                    refObjects = Collections.singletonList(object.get(ref));
                } else {
                    refObjects = (List) object.get(ref);
                }
                Iterator iter = refObjects.iterator();
                while (iter.hasNext()) {
                    DataObject refObject = (DataObject) iter.next();
                    createObjectWithSubtree(root, refObject.getContainmentProperty(), refObject);
                    refObject = registry.get(refObject.getType().getName(), 
                            Collections.singletonList(getPrimaryKey(refObject)));
                    if (ref.isMany()) {
                        newObject.getList(newObject.getType().getProperty(ref.getName())).add(refObject);
                    } else {
                        newObject.set(newObject.getType().getProperty(ref.getName()), refObject);
                    }
                }
            }
        }    	
    }
    
    private void addGraphToRegistry(DataObject graph1) {
        Iterator i = graph1.getType().getProperties().iterator();
        while (i.hasNext()) {
            Property p = (Property) i.next();
            Iterator objects = graph1.getList(p).iterator();
            while (objects.hasNext()) {
                DataObject object = (DataObject) objects.next();                
                Object pk = object.get(getPrimaryKeyName(object));
                if (logger.isDebugEnabled()) {
                	logger.debug("Adding object with pk " + pk + " to registry");
                }
                registry.put(object.getType().getName(), Collections.singletonList(pk), object);
            }
        }
    }

    private Object getPrimaryKey(DataObject object) {
        String pkName = getPrimaryKeyName(object);
        return object.get(pkName);
    }

    private String getPrimaryKeyName(DataObject object) {
        return (String) keys.get(object.getType().getName());
    }

    //JIRA-952
    //if the table and column have SDO type name and property name use it else use database table and column name
    public void addPrimaryKey(String key) {
    	QualifiedColumn column = null;
    	if(this.config != null && this.config.isDatabaseSchemaNameSupported()){
    		column = new QualifiedColumn(key, this.config.isDatabaseSchemaNameSupported()); 
    	}
    	else{
    		column = new QualifiedColumn(key);
    	}
        
    	String tableName = column.getTableName();
    	String columnName = column.getColumnName();
    	String schemaName = column.getSchemaName();
    	String qualifiedTableName = null;
    	
    	if(this.config != null && this.config.isDatabaseSchemaNameSupported()) {
    		qualifiedTableName = schemaName+"."+tableName; 
    	} else {
    		qualifiedTableName = tableName;
    	}
    	MappingWrapper configWrapper = new MappingWrapper(this.config);
    	
    	String typeName = configWrapper.getTableTypeName(qualifiedTableName);
    	String propertyName = configWrapper.getColumnPropertyName(qualifiedTableName, columnName);
    	
    	if(typeName != null && propertyName != null) {
    		if (logger.isDebugEnabled()) {
        		logger.debug("Adding " + typeName + " " + propertyName + " to keys");	
        	}
            keys.put(typeName, propertyName);
    	} else {
        	if (logger.isDebugEnabled()) {
        		logger.debug("Adding " + column.getTableName() + " " + column.getColumnName() + " to keys");	
        	}
            keys.put(column.getTableName(), column.getColumnName());    		
    	}    	
    }
}
