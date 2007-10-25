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
package org.apache.tuscany.das.rdb.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.tuscany.das.rdb.Command;
import org.apache.tuscany.das.rdb.ConfigHelper;
import org.apache.tuscany.das.rdb.DAS;
import org.apache.tuscany.das.rdb.config.wrapper.MappingWrapper;
import org.apache.tuscany.das.rdb.merge.impl.GraphMerger;
import org.apache.tuscany.das.rdb.test.customer.AnOrder;
import org.apache.tuscany.das.rdb.test.customer.Customer;
import org.apache.tuscany.das.rdb.test.customer.CustomerFactory;
import org.apache.tuscany.das.rdb.test.customer.impl.CustomerFactoryImpl;
import org.apache.tuscany.das.rdb.test.singer.impl.SingerFactoryImpl;
import org.apache.tuscany.das.rdb.test.singer.SINGER;
import org.apache.tuscany.das.rdb.test.data.CustomerData;
import org.apache.tuscany.das.rdb.test.data.SingerData;
import org.apache.tuscany.das.rdb.test.data.OrderData;
import org.apache.tuscany.das.rdb.test.framework.DasTest;
import org.apache.tuscany.sdo.api.SDOUtil;

import commonj.sdo.DataObject;
import commonj.sdo.Type;
import commonj.sdo.helper.DataFactory;
import commonj.sdo.helper.HelperContext;
import commonj.sdo.helper.TypeHelper;
import commonj.sdo.helper.XMLHelper;
import commonj.sdo.impl.HelperProvider;

public class GraphMergeTests extends DasTest {

    protected void setUp() throws Exception {
        super.setUp();

        new CustomerData(getAutoConnection()).refresh();
        new OrderData(getAutoConnection()).refresh();
        new SingerData(getAutoConnection()).refresh();

    }

    public void testCreateEmptyGraph() throws Exception {
        String typeUri = "http:///org.apache.tuscany.das.rdb.test/customer.xsd";
        HelperContext context = HelperProvider.getDefaultContext();
        CustomerFactory.INSTANCE.register(context);  
        ConfigHelper helper = new ConfigHelper();
        helper.setDataObjectModel(typeUri);
        DataObject graph = new GraphMerger().emptyGraph(helper.getConfig());
        assertEquals(0, graph.getList("Customer").size());
        assertEquals(0, graph.getList("AnOrder").size());

    }

    public void testCreateEmptyGraphAndAddCustomer() throws Exception {
        String typeUri = "http:///org.apache.tuscany.das.rdb.test/customer.xsd";
        HelperContext context = HelperProvider.getDefaultContext();
        CustomerFactory.INSTANCE.register(context);
        ConfigHelper helper = new ConfigHelper();
        helper.setDataObjectModel(typeUri);
        helper.addTable("CUSTOMER", "Customer");
        helper.addPrimaryKey("CUSTOMER.ID");

        DataObject graph = new GraphMerger().emptyGraph(helper.getConfig());
        Customer c = (Customer) graph.createDataObject("Customer");
        c.setID(4000);
        c.setLastName("Smith");
        c.setAddress("400 Fourth Street");

        DAS das = DAS.FACTORY.createDAS(helper.getConfig(), getConnection());
        das.applyChanges(graph);

        Command cmd = das.createCommand("select * from CUSTOMER order by ID desc");
        graph = cmd.executeQuery();
        assertEquals(6, graph.getList("Customer").size());
        assertEquals("Smith", graph.getDataObject("Customer[1]").getString("lastName"));
        assertEquals("400 Fourth Street", graph.getDataObject("Customer[1]").getString("address"));

    }

    public void testSingleTableMerge() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConnection());
        Command select = das.createCommand("Select ID, LASTNAME, ADDRESS from CUSTOMER where ID <= ?");
        select.setParameter(1, "3");
        DataObject graph1 = select.executeQuery();
        assertEquals(3, graph1.getList("CUSTOMER").size());

        select.setParameter(1, "5");
        DataObject graph2 = select.executeQuery();
        assertEquals(5, graph2.getList("CUSTOMER").size());

        GraphMerger merger = new GraphMerger();
        merger.addPrimaryKey("CUSTOMER.ID");
        DataObject mergedGraph = merger.merge(graph1, graph2);
        List custList = mergedGraph.getList("CUSTOMER");
        for(int i=0; i<custList.size(); i++) {
        	DataObject currCust = (DataObject)custList.get(i);
        }
        assertEquals(5, mergedGraph.getList("CUSTOMER").size());
    }

    public void testSingleTableMerge2() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConnection());
        Command select = das.createCommand("Select ID, LASTNAME, ADDRESS from CUSTOMER where ID <= ?");
        select.setParameter(1, "3");
        DataObject graph1 = select.executeQuery();
        assertEquals(3, graph1.getList("CUSTOMER").size());

        Command select2 = das.createCommand("Select ID, LASTNAME, ADDRESS from CUSTOMER where ID > ?");
        select2.setParameter(1, "3");
        DataObject graph2 = select2.executeQuery();
        assertEquals(2, graph2.getList("CUSTOMER").size());

        GraphMerger merger = new GraphMerger();
        merger.addPrimaryKey("CUSTOMER.ID");
        DataObject mergedGraph = merger.merge(graph1, graph2);
        List custList = mergedGraph.getList("CUSTOMER");
        for(int i=0; i<custList.size(); i++) {
        	DataObject currCust = (DataObject)custList.get(i);
        }
        assertEquals(5, mergedGraph.getList("CUSTOMER").size());
    }
    
    public void testSingleTableMergeThreeGraphs() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConnection());
        Command select = das.createCommand("Select ID, LASTNAME, ADDRESS from CUSTOMER where ID <= ?");
        select.setParameter(1, "3");
        DataObject graph1 = select.executeQuery();
        assertEquals(3, graph1.getList("CUSTOMER").size());

        select.setParameter(1, "4");
        DataObject graph2 = select.executeQuery();
        assertEquals(4, graph2.getList("CUSTOMER").size());

        select.setParameter(1, "5");
        DataObject graph3 = select.executeQuery();
        assertEquals(5, graph3.getList("CUSTOMER").size());

        GraphMerger merger = new GraphMerger();
        merger.addPrimaryKey("CUSTOMER.ID");
        List graphs = new ArrayList();
        graphs.add(graph1);
        graphs.add(graph2);
        graphs.add(graph3);
        DataObject mergedGraph = merger.merge(graphs);

        assertEquals(5, mergedGraph.getList("CUSTOMER").size());

    }

    public void testMultiTableMerge2() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConfig("CustomersOrdersConfig.xml"), getConnection());
        // Read some customers and related orders
        Command select = das.createCommand("SELECT * FROM CUSTOMER LEFT JOIN ANORDER ON " 
                + "CUSTOMER.ID = ANORDER.CUSTOMER_ID where CUSTOMER.ID = ?");

        select.setParameter(1, new Integer(1));
        DataObject graph1 = select.executeQuery();

        DataObject customer = (DataObject) graph1.getList("CUSTOMER").get(0);
        assertEquals(2, customer.getList("orders").size());

        select.setParameter(1, new Integer(2));
        DataObject graph2 = select.executeQuery();
        DataObject customer2 = (DataObject) graph2.getList("CUSTOMER").get(0);
        assertEquals(1, graph2.getList("CUSTOMER").size());
        assertEquals(1, customer2.getList("orders").size());
        assertEquals(2, customer2.getInt("ID"));

        GraphMerger merger = new GraphMerger();
        merger.addPrimaryKey("CUSTOMER.ID");
        merger.addPrimaryKey("ANORDER.ID");
        DataObject mergedGraph = merger.merge(graph1, graph2);

        assertEquals(3, mergedGraph.getList("ANORDER").size());
        assertEquals(2, mergedGraph.getList("CUSTOMER").size());

        DataObject mergedCustomer = (DataObject) mergedGraph.getList("CUSTOMER").get(1);
        assertEquals(2, mergedCustomer.getInt("ID"));
        assertEquals(1, mergedCustomer.getList("orders").size());
        DataObject mergedOrder = (DataObject) mergedCustomer.getList("orders").get(0);
        assertEquals(4, mergedOrder.getInt("ID"));

    }

    public void testMultiTableAppendSingleTable2() throws Exception {
        DAS das = DAS.FACTORY.createDAS(getConfig("CustomersOrdersConfig.xml"), getConnection());
        // Read some customers and related orders
        Command select = das.createCommand("SELECT * FROM CUSTOMER LEFT JOIN ANORDER ON " 
                + "CUSTOMER.ID = ANORDER.CUSTOMER_ID where CUSTOMER.ID = ?");

        select.setParameter(1, new Integer(1));
        DataObject graph1 = select.executeQuery();

        DataObject customer = (DataObject) graph1.getList("CUSTOMER").get(0);
        assertEquals(2, customer.getList("orders").size());

        DAS das2 = DAS.FACTORY.createDAS(getConnection());
        Command select2 = das2.createCommand("select * from ANORDER");
        DataObject graph2 = select2.executeQuery();
        assertEquals(4, graph2.getList("ANORDER").size());

        GraphMerger merger = new GraphMerger();
        merger.addPrimaryKey("CUSTOMER.ID");
        merger.addPrimaryKey("ANORDER.ID");
        DataObject mergedGraph = merger.merge(graph1, graph2);
        assertEquals(4, mergedGraph.getList("ANORDER").size());
        assertEquals(1, mergedGraph.getList("CUSTOMER").size());
    }

    /**
     * Mix dynamic DO from query result with static DO from customer.xsd. As long as the type and 
     * property names are matching for both, merge can happen - fixing bug 1827- pk hashmap was not
     * considering type and property names before.
     * @throws Exception
     */
    public void testMergeUpdateSingleTable() throws Exception {
    	//existing records
        DAS das = DAS.FACTORY.createDAS(getConfig("CustomersOrdersConfigProps.xml"), getConnection());
    	DataObject root = das.getCommand("all customers").executeQuery();
    	DataObject firstCustomer = root.getDataObject("Customer[ID=1]");
    	
    	if(firstCustomer != null)
    		firstCustomer.delete();//DAS will not automatic createOrReplace , so do delete here and later doing a create
    	
    	//create new customer using static SDO - say a service is doing it
    	String typeUri = "http:///org.apache.tuscany.das.rdb.test/customer.xsd";
        HelperContext context = HelperProvider.getDefaultContext();
        CustomerFactory.INSTANCE.register(context);
        ConfigHelper helper = new ConfigHelper();
        helper.setDataObjectModel(typeUri);
        helper.addTable("CUSTOMER", "Customer");
        helper.addPrimaryKey("CUSTOMER.ID", "ID");

        GraphMerger gm = new GraphMerger(helper.getConfig());
        DataObject graph = gm.emptyGraph();
        Customer c = (Customer) graph.createDataObject("Customer");
        c.setID(1);
        c.setLastName("WilliamsNew");
        c.setAddress("400 Fourth Street");
        
        gm.addPrimaryKey("CUSTOMER.ID");
        
        DataObject root1 = gm.merge(root, ((DataObject)c).getRootObject());       
        das.applyChanges(root1);
    }
    
    /**
     * Support merge(StaticDO1, StaticDO2, noChangeSummary) when both are not contained in "DataGraphRoot" and DataGraph (and not have changeSummary). When
     * no existing change summary, will be used for INSERT
     * @throws Exception
     */
    public void testMergeStaticDOs() throws Exception {
   	 // Populate the meta data for the test (SINGER) model xsd - WAY ONE
      	 /*String TEST_MODEL = "/SINGER.xsd";
       	 String TEST_NAMESPACE = "http:///org.apache.tuscany.das.rdb.test/SINGER.xsd";

        URL url = getClass().getResource(TEST_MODEL);
        InputStream inputStream = url.openStream();
        XSDHelper.INSTANCE.define(inputStream, url.toString());
        inputStream.close();
           
        Type singerType = TypeHelper.INSTANCE.getType(TEST_NAMESPACE, "SINGER");
        DataObject SINGER = DataFactory.INSTANCE.create(singerType);
        SINGER.set("ID", new Integer(100));
        SINGER.set("NAME", "Singer100"); */
                
   	// Populate the meta data for the test (SINGER) using generated classes - WAY TWO
        SINGER singerStaticDO1 = SingerFactoryImpl.INSTANCE.createSINGER();
        singerStaticDO1.setID(100);
        singerStaticDO1.setNAME("Singer100");
                        
        SINGER singerStaticDO2 = SingerFactoryImpl.INSTANCE.createSINGER();
        singerStaticDO2.setID(200);
        singerStaticDO2.setNAME("Singer200");
        
        GraphMerger gm = new GraphMerger();
        gm.addPrimaryKey("SINGER.ID");
        
        DataObject songs = gm.merge((DataObject)singerStaticDO1, (DataObject)singerStaticDO2, false);
 
        DAS das = DAS.FACTORY.createDAS(getConnection());
        das.applyChanges(songs);
        //printSingerData();
        Command cmd = das.createCommand("SELECT ID, NAME FROM SINGER");
    	DataObject root = cmd.executeQuery();
    	assertNotNull(root.getDataObject("SINGER[ID=100]"));
    	assertEquals("Singer100", root.getDataObject("SINGER[ID=100]").getString("NAME"));
    	assertNotNull(root.getDataObject("SINGER[ID=200]"));
    	assertEquals("Singer200", root.getDataObject("SINGER[ID=200]").getString("NAME"));
   }  

    /*Internal utility to create a property in a dynamic DO*/
    private void specifyProperty(DataObject containingTypeDO, String nameString, Type type, boolean isMany) {
        DataObject subordinateProperty = containingTypeDO.createDataObject("property");
        subordinateProperty.set("name", nameString);
        subordinateProperty.set("type", type);
        subordinateProperty.setBoolean("many", isMany);
    }
    
    /*Internal utility to form Dynamic DO*/
    private DataObject formDynamicDO(String dynamicRootType) {
    	String DYNAMIC_TYPES_URI = "http://www.example.com/dynamicTypesFromSchemaSimple";
    	String DYNAMIC_ROOT_TYPE_0 = dynamicRootType;
    	String COMMONJ_SDO = "commonj.sdo";
    	
    	HelperContext hcDO = SDOUtil.createHelperContext();

        TypeHelper thDO = hcDO.getTypeHelper();
        DataFactory dfDO = hcDO.getDataFactory();

        // create a container object type
        DataObject containerTypeDO = dfDO.create("commonj.sdo", "Type");
        containerTypeDO.set("uri", DYNAMIC_TYPES_URI);
        containerTypeDO.set("name", DYNAMIC_ROOT_TYPE_0);
        containerTypeDO.set("sequenced", Boolean.TRUE);

        specifyProperty(containerTypeDO, "NAME", thDO.getType(COMMONJ_SDO, "String"), false);
        specifyProperty(containerTypeDO, "ID", thDO.getType(COMMONJ_SDO, "Int"), false);

        Type containerType = thDO.define(containerTypeDO);
        assertNotNull(containerType);

        DataObject doFromApiAndDynTyp = dfDO.create(containerType);
        return doFromApiAndDynTyp;
    }
    
    /**merge(DynamicDO1, DynamicDO2, noChangeSummary). When no change summary associated in a DataGraph, treated for INSERT
     * 
     * @throws Exception
     */
    public void testMergeDynamicDOs() throws Exception {
    	DataObject doFromApiAndDynTyp1 = formDynamicDO("SINGER");
        assertNotNull(doFromApiAndDynTyp1);
        doFromApiAndDynTyp1.setString("NAME", "DynamicSinger1");
        doFromApiAndDynTyp1.setInt("ID", 100);
        
        DataObject doFromApiAndDynTyp2 = formDynamicDO("SINGER");
        assertNotNull(doFromApiAndDynTyp2);
        doFromApiAndDynTyp2.setString("NAME", "DynamicSinger2");
        doFromApiAndDynTyp2.setInt("ID", 200);
        
        GraphMerger gm = new GraphMerger();
        gm.addPrimaryKey("SINGER.ID");
        
        DataObject dos = gm.merge(doFromApiAndDynTyp1, doFromApiAndDynTyp2, false);
        
        DAS das = DAS.FACTORY.createDAS(getConnection());
        das.applyChanges(dos);
        //printSingerData();
        Command cmd = das.createCommand("SELECT ID, NAME FROM SINGER");
    	DataObject root = cmd.executeQuery();
    	assertNotNull(root.getDataObject("SINGER[ID=100]"));
    	assertEquals("DynamicSinger1", root.getDataObject("SINGER[ID=100]").getString("NAME"));
    	assertNotNull(root.getDataObject("SINGER[ID=200]"));
    	assertEquals("DynamicSinger2", root.getDataObject("SINGER[ID=200]").getString("NAME"));        
    }
    
    /**
     * merge(StaticDO, DynamicDO, noChangeSummary). As no change summary, used for INSERT
     * @throws Exception
     */
    public void testMergeStaticAndDynamicDOs() throws Exception {
    	DataObject singerDynamicDO1 = formDynamicDO("SINGER");
        assertNotNull(singerDynamicDO1);
        singerDynamicDO1.setString("NAME", "Singer100");
        singerDynamicDO1.setInt("ID", 100);
        
    	SINGER singerStaticDO1 = SingerFactoryImpl.INSTANCE.createSINGER();
        singerStaticDO1.setID(200);
        singerStaticDO1.setNAME("Singer200");
                        
        GraphMerger gm = new GraphMerger();
        gm.addPrimaryKey("SINGER.ID");
        
        DataObject singers = gm.merge((DataObject)singerStaticDO1, singerDynamicDO1, false);
 
        DAS das = DAS.FACTORY.createDAS(getConnection());
        das.applyChanges(singers);
        //printSingerData();
        Command cmd = das.createCommand("SELECT ID, NAME FROM SINGER");
    	DataObject root = cmd.executeQuery();
    	assertNotNull(root.getDataObject("SINGER[ID=100]"));
    	assertEquals("Singer100", root.getDataObject("SINGER[ID=100]").getString("NAME"));
    	assertNotNull(root.getDataObject("SINGER[ID=200]"));
    	assertEquals("Singer200", root.getDataObject("SINGER[ID=200]").getString("NAME"));        
    }

    /** 
     * Query result can carry ChangeSummary of CUD, Dynamic DO will be a new Object (CREATE)
     * @throws Exception
     */
    public void testMergeQueryResultAndDynamicDOs() throws Exception {
    	DataObject singerDynamicDO1 = formDynamicDO("SINGER");
        assertNotNull(singerDynamicDO1);
        singerDynamicDO1.setString("NAME", "Singer100");
        singerDynamicDO1.setInt("ID", 100);
        
        DAS das = DAS.FACTORY.createDAS(getConnection());
        Command cmd = das.createCommand("SELECT ID, NAME FROM SINGER");
        DataObject root = cmd.executeQuery();
        
        DataObject secondSinger = root.getDataObject("SINGER[ID=2]");
        secondSinger.setString("NAME", "JaneSecond");//change summary registers Update change
        
        DataObject thirdSinger = root.getDataObject("SINGER[ID=3]");
        thirdSinger.delete();//change summary registers Delete change
        
        GraphMerger gm = new GraphMerger();
        gm.addPrimaryKey("SINGER.ID");
        
        DataObject singers = gm.merge(root, singerDynamicDO1, false);//merge with primary DO having change summary and secondary DO without one.
        //as false is passed, secondary DO will be treated as INSERT in primaryChangeSummary
 
        das.applyChanges(singers);
        //printSingerData();
    	root = cmd.executeQuery();
    	assertNotNull(root.getDataObject("SINGER[ID=100]"));
    	assertEquals("Singer100", root.getDataObject("SINGER[ID=100]").getString("NAME"));
    	assertNotNull(root.getDataObject("SINGER[ID=2]"));
    	assertEquals("JaneSecond", root.getDataObject("SINGER[ID=2]").getString("NAME"));
    	assertNull(root.getDataObject("SINGER[ID=3]"));
    }
    
    /** 
     * Query result can carry ChangeSummary of CUD, Static DO will be a new Object (CREATE)
     * @throws Exception
     */
    public void testMergeQueryResultAndStaticDOs() throws Exception {
    	SINGER singerStaticDO1 = SingerFactoryImpl.INSTANCE.createSINGER();
        singerStaticDO1.setID(100);
        singerStaticDO1.setNAME("Singer100");
                        
        GraphMerger gm = new GraphMerger();
        gm.addPrimaryKey("SINGER.ID");

        DAS das = DAS.FACTORY.createDAS(getConnection());
        Command cmd = das.createCommand("SELECT ID, NAME FROM SINGER");
        DataObject root = cmd.executeQuery();

        DataObject secondSinger = root.getDataObject("SINGER[ID=2]");
        secondSinger.setString("NAME", "JaneSecond");//Change summary will register Update
        
        DataObject thirdSinger = root.getDataObject("SINGER[ID=3]");
        thirdSinger.delete();//change summary will register Delete

        DataObject singers = gm.merge(root, (DataObject)singerStaticDO1, false);//due to false, secondary DO will be treated as INSERT in primary change summary
        
        das.applyChanges(singers);
        //printSingerData();
    	root = cmd.executeQuery();
    	assertNotNull(root.getDataObject("SINGER[ID=100]"));
    	assertEquals("Singer100", root.getDataObject("SINGER[ID=100]").getString("NAME"));
    	assertNotNull(root.getDataObject("SINGER[ID=2]"));
    	assertEquals("JaneSecond", root.getDataObject("SINGER[ID=2]").getString("NAME"));
    	assertNull(root.getDataObject("SINGER[ID=3]"));        
    }    
    
    private void printSongData() throws Exception {
    	DAS das = DAS.FACTORY.createDAS(getConnection());
    	Command cmd = das.createCommand("SELECT ID, TITLE, SINGERID FROM SONG");
    	DataObject root = cmd.executeQuery();
    	System.out.println("**********SONG*************************");
    	System.out.println(XMLHelper.INSTANCE.save(root, "song", "song"));
    	System.out.println("**********************************************");
    }
    
    private void printSingerData() throws Exception {
    	DAS das = DAS.FACTORY.createDAS(getConnection());
    	Command cmd = das.createCommand("SELECT ID, NAME FROM SINGER");
    	DataObject root = cmd.executeQuery();
    	System.out.println("**************SINGER************************");
    	System.out.println(XMLHelper.INSTANCE.save(root, "singer", "singer"));
    	System.out.println("**********************************************");
    }
    
    /**
     * Mix all 3 - query result carrying change summary, static and dynamic DO
     * @throws Exception
     */
    public void testMergeListQueryResultStaticAndDynamic() throws Exception {
    	SINGER singerStaticDO1 = SingerFactoryImpl.INSTANCE.createSINGER();
        singerStaticDO1.setID(200);
        singerStaticDO1.setNAME("Singer200");
        
        GraphMerger gm = new GraphMerger();
        gm.addPrimaryKey("SINGER.ID");
        
        DAS das = DAS.FACTORY.createDAS(getConnection());
        Command cmd = das.createCommand("SELECT ID, NAME FROM SINGER");
        DataObject root = cmd.executeQuery();
        DataObject firstSinger = root.getDataObject("SINGER[ID=1]");
        firstSinger.setString("NAME", "JohnNew");//change summary will register Update
        
        DataObject secondSinger = root.getDataObject("SINGER[ID=2]");
        secondSinger.delete();//change summary will register Delete
        
        DataObject singerDynamicDO1 = formDynamicDO("SINGER");
        assertNotNull(singerDynamicDO1);
        singerDynamicDO1.setString("NAME", "Singer100");
        singerDynamicDO1.setInt("ID", 100);
        
        ArrayList singersToMerge = new ArrayList();        
        singersToMerge.add((DataObject)singerStaticDO1);
        singersToMerge.add(root);
        singersToMerge.add(singerDynamicDO1);
        
        //merge will internally shuffle DOs to detect primary having change summary. so root will be designated as Primary
        DataObject singers = gm.merge(singersToMerge, false);
        
    	das.applyChanges(singers);
        //printSingerData();
    	root = cmd.executeQuery();
    	assertNotNull(root.getDataObject("SINGER[ID=100]"));
    	assertEquals("Singer100", root.getDataObject("SINGER[ID=100]").getString("NAME"));
    	assertNotNull(root.getDataObject("SINGER[ID=200]"));
    	assertEquals("Singer200", root.getDataObject("SINGER[ID=200]").getString("NAME"));
    	assertNotNull(root.getDataObject("SINGER[ID=1]"));
    	assertEquals("JohnNew", root.getDataObject("SINGER[ID=1]").getString("NAME"));
    	assertNull(root.getDataObject("SINGER[ID=2]"));
    }

    /**
     * There is no much meaning to nested merges as the secondary DOs are at most treated for INSERTs. 
     * @throws Exception
     */
    public void testMergePrimaryQuerySecondaryStaticNested() throws Exception {
    	String typeUri = "http:///org.apache.tuscany.das.rdb.test/customer.xsd";
        HelperContext context = HelperProvider.getDefaultContext();
        CustomerFactory.INSTANCE.register(context);  
    	
    	AnOrder anOrderStaticDO1 = CustomerFactoryImpl.INSTANCE.createAnOrder();
    	anOrderStaticDO1.setID(100);
    	anOrderStaticDO1.setProduct("100 prods");
    	anOrderStaticDO1.setQuantity(100);
    	anOrderStaticDO1.setCustomer_ID(1);
        
        GraphMerger gm = new GraphMerger();
        gm.addPrimaryKey("Customer.ID");
        gm.addPrimaryKey("AnOrder.ID");
        
        ConfigHelper helper = new ConfigHelper();
        helper.setDataObjectModel(typeUri);
        helper.addTable("CUSTOMER", "Customer");
        helper.addTable("ANORDER", "AnOrder");
        MappingWrapper mappingWrapper = new MappingWrapper(helper.getConfig());
        
        helper.addColumn(mappingWrapper.getTable("CUSTOMER"), "ID", "ID");
        helper.addColumn(mappingWrapper.getTable("CUSTOMER"), "LASTNAME", "lastName");
        helper.addColumn(mappingWrapper.getTable("CUSTOMER"), "ADDRESS", "address");

        helper.addColumn(mappingWrapper.getTable("ANORDER"), "ID", "ID");
        helper.addColumn(mappingWrapper.getTable("ANORDER"), "PRODUCT", "Product");
        helper.addColumn(mappingWrapper.getTable("ANORDER"), "QUANTITY", "Quantity");
        helper.addColumn(mappingWrapper.getTable("ANORDER"), "CUSTOMER_ID", "Customer_ID");

        helper.addRelationship("CUSTOMER.ID", "ANORDER.CUSTOMER_ID", "orders");
        
        DAS das = DAS.FACTORY.createDAS(helper.getConfig(), getConnection());
        
        Command cmd = das.createCommand("SELECT * FROM CUSTOMER LEFT JOIN ANORDER ON " 
                + "CUSTOMER.ID = ANORDER.CUSTOMER_ID");
        DataObject root = cmd.executeQuery();

        DataObject result = gm.merge(root, (DataObject)anOrderStaticDO1, false);
        assertEquals(2, result.getDataObject("Customer[ID=1]").getList("orders").size()); //because only CREATE part
        //is considered for AnOrder, update part for relationship is not considered
        das.applyChanges(result);
        
        context = HelperProvider.getDefaultContext();
        CustomerFactory.INSTANCE.register(context);
        
        root = cmd.executeQuery();
        DataObject firstCustomer = root.getDataObject("Customer[ID=1]");
        List firstCustOrders = firstCustomer.getList("orders");
        assertEquals(3, root.getDataObject("Customer[ID=1]").getList("orders").size());//as now snapshot from DB
    }
    
    /**
     * Show that when structure coming from Query is partial and the secondary is complete DO structure based on model xsd,
     * on-the-fly changes to structure are not allowed and merge does not happen
     * @throws Exception
     */
    public void testMergePartialQueryAndStatic() throws Exception {
    	SINGER singerStaticDO1 = SingerFactoryImpl.INSTANCE.createSINGER();
    	singerStaticDO1.setID(100);
    	singerStaticDO1.setNAME("Singer100");
                        
        GraphMerger gm = new GraphMerger();
        gm.addPrimaryKey("SINGER.ID");

        DAS das = DAS.FACTORY.createDAS(getConnection());
        Command cmd = das.createCommand("SELECT ID FROM SINGER WHERE ID=1");
        DataObject root = cmd.executeQuery();

        try {
	        gm.merge(root, (DataObject)singerStaticDO1);
	        fail("Expected exception!");
        } catch(RuntimeException e) {        	
        	assertEquals("Graph structures do not match,", e.getMessage().substring(0, 30));
        }        
    }    
}
