/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.tuscany.das.ldap.oid.create;

public class OIDCreatorHelper
{
    private static int MAX_OID_SEGMENT_LENGTH = 8;
    /**
     * Calculate segmented OID.
     * 
     * @param oid the oid
     * 
     * @return the string
     * 
     * @throws Exception the exception
     * 
     * Note that the OID must be divided
     * into segments less than 9 characters
     * long.  This is an ApacheDS restriction
     * on OID segment length.
     */
    public static String calculateSegmentedOID(String oid) throws Exception
    {
        if (oid.length() < 10)
        {
            throw new Exception("The length of the oid must be greater than 9 in order to ensure uniqueness.");
        }
        int oidLength = oid.length();
        int numberOfPeriods  = oidLength / MAX_OID_SEGMENT_LENGTH;
        
        int beginIndex = 0;
        int endIndex = MAX_OID_SEGMENT_LENGTH;
        
        String segmentedOID = 
            oid.substring(beginIndex, endIndex ) + ".";

        for (int i = 2; i < numberOfPeriods; i++)
        {
            beginIndex = (i-1) * MAX_OID_SEGMENT_LENGTH + 1;
            endIndex = i * MAX_OID_SEGMENT_LENGTH;
            segmentedOID +=  oid.substring( beginIndex, endIndex ) + ".";
        }
        int finalSegmentLength = oid.length() - (endIndex + 2);
        
        if ( finalSegmentLength > MAX_OID_SEGMENT_LENGTH )
        {
            segmentedOID += oid.substring( endIndex + 1,endIndex + 9  ) + ".";
            segmentedOID += oid.substring( endIndex + 10, oid.length()  );
        }
        else
        {
            segmentedOID += oid.substring( endIndex + 1, oid.length()  );            
        }
        return segmentedOID;
    }
}
