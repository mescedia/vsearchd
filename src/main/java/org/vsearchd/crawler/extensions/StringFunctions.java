/*
 * vsearchd - a focused web-crawler
 *
 * Copyright (C) 2012-2014  Michael Kassnel 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License (Version 3) as published
 * by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU Lesser General Public License for more details:
 * http://www.gnu.org/licenses/lgpl.txt
 *
 */

package org.vsearchd.crawler.extensions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;

public class StringFunctions {

	public static String base64Encode(String str) throws IOException {
		return Base64.encodeBase64String(str.getBytes("UTF-8"));
	}

	public static String base64Decode(String str) throws IOException {
		return new String(Base64.decodeBase64(str));
	}

	public static String calcMD5(String inStr) throws NoSuchAlgorithmException {
		MessageDigest msgDgst = MessageDigest.getInstance("MD5");
		msgDgst.update(inStr.getBytes(), 0, inStr.length());
		return new BigInteger(1, msgDgst.digest()).toString(16);
	}

	public static String calcSHA1(String inStr) throws NoSuchAlgorithmException {
		MessageDigest msgDgst = MessageDigest.getInstance("SHA1");
		msgDgst.update(inStr.getBytes(), 0, inStr.length());
		return new String(Hex.encodeHex(msgDgst.digest()));
	}

	public static String replaceString(String str, String searchRegex,
			String replace) {
		return str.replaceAll(searchRegex, replace);
	}

	public static String replaceQuotes(String str)
			throws UnsupportedEncodingException {
		return str.replaceAll("\"", "&quot;");
	}

	public static String replaceApos(String str)
			throws UnsupportedEncodingException {
		return str.replaceAll("\'", "&apos;");
	}
	
	public static String replaceAmp(String str)
			throws UnsupportedEncodingException {
		return str.replaceAll("&", "&amp;");
	}

	public static String replaceLtGt(String str)
			throws UnsupportedEncodingException {

		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");

		return str;
	}

	public static String getMysqlDateTime() {
		Date dt = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(dt);
	}
	
	public static String removeTags(String str) {
		String oldStr = "";
		String newStr = Jsoup.parse(str).text();		
	    while(!oldStr.equals(newStr))	{
	    	oldStr = newStr ;
	    	newStr = Jsoup.parse(oldStr).text();	    	
	    }
	    return newStr;
	}
	
	public static String detoxifyText(String str) throws UnsupportedEncodingException	{
		
		str = StringFunctions.removeTags(str);
		str = StringFunctions.replaceAmp(str);
		str = StringFunctions.replaceApos(str);
		str = StringFunctions.replaceQuotes(str);
		
		return str ;
	}

}
