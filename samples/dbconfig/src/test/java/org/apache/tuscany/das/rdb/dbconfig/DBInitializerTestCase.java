package org.apache.tuscany.das.rdb.dbconfig;

import junit.framework.TestCase;



/**
 * Tests the Converter framwork
 */
public class DBInitializerTestCase extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    
    public void testCreateDatabase() throws Exception {
        DBInitializer dbInitializer = new DBInitializer("dbConfig.xml");
        dbInitializer.initializeDatabase(true);
    }
    
    public void something() throws Exception {
//        try{            
//            //DBInitHelper dbInit = new DBInitHelper("DBConfig.xml");
//            DBInitializer dbInit = new DBInitializer(new FileInputStream("c:/DBConfig.xml"));
//            dbInit.initializeDatabase(true);
//            
//            System.out.println("check schema created:"+dbInit.isDatabaseReady());
//            System.out.println("check data created:"+dbInit.isDataCreated());
//            
//            dbInit.deleteData();
//            System.out.println("check data created after deleteData:"+dbInit.isDataCreated());
//            
//            dbInit.deleteSchema();
//            System.out.println("after deleteSchema check schema created:"+dbInit.isDatabaseReady());
//            
//            dbInit.createSchema(false);
//            System.out.println("check schema created after createSchema:"+dbInit.isDatabaseReady());
//            
//            dbInit.refreshData(true);
//            System.out.println("check data created after refreshData:"+dbInit.isDataCreated());
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }
}
