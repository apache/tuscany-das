/**
 *
 *  Copyright 2005 The Apache Software Foundation or its licensors, as applicable.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.tuscany.das.rdb.test;

import org.apache.tuscany.das.rdb.ApplyChangesCommand;
import org.apache.tuscany.das.rdb.Command;
import org.apache.tuscany.das.rdb.Key;
import org.apache.tuscany.das.rdb.test.data.OrderDetailsData;
import org.apache.tuscany.das.rdb.test.framework.DasTest;

import commonj.sdo.DataObject;

/**
 * Tests for Compound Keys
 */
public class CompoundKeyTests extends DasTest {

    protected void setUp() throws Exception {
        super.setUp();

        new OrderDetailsData(getAutoConnection()).refresh();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRead() throws Exception {

        Command getOrderDetails = Command.FACTORY
                .createCommand("Select * from ORDERDETAILS where ORDERID = :ORDERID AND PRODUCTID = :PRODUCTID");
        getOrderDetails.setConnection(getConnection());

        getOrderDetails.setParameterValue("ORDERID", new Integer(1));
        getOrderDetails.setParameterValue("PRODUCTID", new Integer(1));

        DataObject root = getOrderDetails.executeQuery();

        DataObject orderDetail = (DataObject) root.get("ORDERDETAILS[1]");
        assertEquals(1.1f, orderDetail.getFloat("PRICE"), 0.01);

    }

    public void testReadModifyWrite() throws Exception {

        Command getOrderDetails = Command.FACTORY
                .createCommand("Select * from ORDERDETAILS where ORDERID = 1 AND PRODUCTID = 1");
        getOrderDetails.setConnection(getConnection());
        DataObject root = getOrderDetails.executeQuery();

        DataObject orderDetails = (DataObject) root.get("ORDERDETAILS[1]");
        assertEquals(1.1f, orderDetails.getFloat("PRICE"), 0.01);

        // Modify
        orderDetails.setFloat("PRICE", 0f);

        // Build apply changes command
        ApplyChangesCommand apply = Command.FACTORY.createApplyChangesCommand();
        apply.setConnection(getConnection());
        // programatically add the only part of the mapping model needed for
        // this simple case
        apply.addPrimaryKey(new Key(new String[] { "ORDERDETAILS.ORDERID", "ORDERDETAILS.PRODUCTID" }));

        // Write
        apply.execute(root);

        // Verify
        orderDetails = root.getDataObject("ORDERDETAILS[1]");
        assertEquals(0f, orderDetails.getFloat("PRICE"), 0.01);

    }

    public void testReadOrdersAndDetails() throws Exception {

        Command read = Command.FACTORY
                .createCommand("SELECT * FROM ANORDER LEFT JOIN ORDERDETAILS ON ANORDER.ID = ORDERDETAILS.ORDERID ORDER BY ANORDER.ID");
        read.setConnection(getConnection());

        Key pk = new Key("ANORDER.ID");
        read.addPrimaryKey(pk);
          
        Key detailsPk = new Key(new String[] { "ORDERDETAILS.ORDERID", "ORDERDETAILS.PRODUCTID" });
        read.addPrimaryKey(detailsPk);

        Key fk = new Key("ORDERDETAILS.ORDERID");

        read.addRelationship(pk, fk);

        DataObject root = read.executeQuery();

        DataObject firstOrder = root.getDataObject("ANORDER[1]");
        assertEquals(1, firstOrder.getInt("ID"));
        assertEquals(2, firstOrder.getList("ORDERDETAILS").size());

    }

}
