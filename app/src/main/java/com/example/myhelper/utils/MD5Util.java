package com.example.myhelper.utils;

import java.security.MessageDigest;

public class MD5Util {
	/**
	 * 将msg加密为32位的密文
	 */
	public static String md5(String msg){
		StringBuffer sBuffer = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] digestBytes = digest.digest(msg.getBytes());
			for (int i = 0; i < digestBytes.length; i++)   
            {  
				String string = Integer.toHexString(0xFF & digestBytes[i]);
                if (string.length() == 1)  {
                	sBuffer.append("0");
                }
                sBuffer.append(string);  
            }  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sBuffer.toString();
	}
}
