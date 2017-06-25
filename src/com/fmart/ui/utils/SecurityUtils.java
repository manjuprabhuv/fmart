package com.fmart.ui.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
	
	
	public static String encode(String value) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(value.getBytes("UTF-8"));
        StringBuilder stringBuilder=  new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            stringBuilder.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuilder.toString(); 
	}

}
