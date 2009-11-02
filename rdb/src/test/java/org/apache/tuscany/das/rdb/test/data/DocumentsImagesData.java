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
package org.apache.tuscany.das.rdb.test.data;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.tuscany.das.rdb.test.framework.TestDataWithExplicitColumns;

public class DocumentsImagesData extends TestDataWithExplicitColumns {

	public DocumentsImagesData(Connection connection) {
		super(connection, null, null, null);
	}
	
    public String getTableName() {
        return "DOCUMENTS_IMAGES";
    }

    //Due to special processing, not using superclass methods.
    protected void insertRows() throws SQLException {    	
    	try{
    		InputStream fileAsciiStream = this.getClass().getClassLoader().getResourceAsStream("asciifile.txt");
			PreparedStatement ps = this.connection.prepareStatement("INSERT INTO DOCUMENTS_IMAGES VALUES (?, ?, ?)");
			ps.setInt(1, 100);
			
			// - set the value of the input parameter to the input stream
			ps.setAsciiStream(2, fileAsciiStream, fileAsciiStream.available());
			
			//Blob
			InputStream fileImgStream = this.getClass().getClassLoader().getResourceAsStream("moin-www.png");			
			byte[] imgBytes = new byte[fileImgStream.available()];
			int i=0;
			while(fileImgStream.available()>0){					
				imgBytes[i] = (byte)fileImgStream.read();
				i++;
			}
			Blob blob = createBlob(imgBytes);
			ps.setBlob(3, blob);
			ps.execute(); 
			ps.close();
    	}catch(IOException ioe){
    		ioe.printStackTrace();
    		throw new RuntimeException("Could not insert data for Blob/Clob..Please check resources!"+ioe.getMessage());
    	}
    }
    
     private Blob createBlob(byte[] bytes) {
        Blob blob = null;
        Class blobClazz;
        try {
            blobClazz = Class.forName("javax.sql.rowset.serial.SerialBlob", true, Thread.currentThread().getContextClassLoader());
            Constructor blobConstructor = blobClazz.getConstructor(new Class[] { bytes.getClass() });
            blob = (Blob) blobConstructor.newInstance(new Object[] { bytes });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return blob;
    }
    
}
