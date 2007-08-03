package org.apache.tuscany.das.ldap.util;

public class IDGenerator {

	static long currentID= System.currentTimeMillis();
	
	static public synchronized long generate(){
	    return currentID++;
	}
}
