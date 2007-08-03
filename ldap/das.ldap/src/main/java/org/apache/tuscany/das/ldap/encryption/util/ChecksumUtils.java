
package org.apache.tuscany.das.ldap.encryption.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.tuscany.das.ldap.encryption.constants.EncryptionConstants;

public class ChecksumUtils
implements EncryptionConstants
{
    public static String computeMD5Hash(String string) 
    throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest = MessageDigest.getInstance(MD5);
        
        byte[] digest               = 
            messageDigest.digest(string.getBytes());
    
        StringBuffer hexString = new StringBuffer();
        for (int i=0;i<digest.length;i++) {
          hexString.append (
            Integer.toHexString(0xFF & digest[i]));
        }
    
        char[] hexStringCharacters = hexString.toString().toCharArray();
        String checksum = "";
        
        for( char i : hexStringCharacters)
        {
            int ascii = (int) i;
            checksum = checksum + Integer.toString(ascii);
        }
 
        return checksum;
    }

}
