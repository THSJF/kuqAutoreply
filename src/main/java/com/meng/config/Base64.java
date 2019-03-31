package com.meng.config;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64 {

	  public static byte[] decryptBASE64(String key) throws Exception {  
	        return (new BASE64Decoder()).decodeBuffer(key);  
	    }   
	    public static String encryptBASE64(byte[] key) throws Exception {  
	        return (new BASE64Encoder()).encodeBuffer(key);  
	    }  
}