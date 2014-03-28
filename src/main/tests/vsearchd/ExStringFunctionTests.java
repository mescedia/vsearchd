/*
 * vsearchd - a focused crawler
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

package vsearchd;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.vsearchd.crawler.extensions.StringFunctions;

public class ExStringFunctionTests {
	
	@Test
	public void RemoveTagsTest() {
 
		String src = "abc &amp; def <br> &amp; <p>asöl öasdkl</p>" ;
		String dst = "abc & def & asöl öasdkl" ;
		
		assertEquals(StringFunctions.removeTags(src),dst) ;
	}
	
	@Test
	public void H4xTest() {
 
		String src = "abc &lt;script&gt;alert('hello world');&lt;/script&gt; def <br> &amp; <p>asöl öasdkl</p>" ;
		// String dst = "abc <script>alert('hello world');</script> def & asöl öasdkl" ;
		String dst = "abc def & asöl öasdkl" ;
		
		assertEquals(StringFunctions.removeTags(src),dst) ;
	}
	
	@Test
	public void QuoteTest() {
 
		String src = "abc &lt;script&gt;alert('hello world');&lt;/script&gt; def <br> &amp; <p>asöl öasdkl&quot;\"</p>" ;
		// String dst = "abc <script>alert('hello world');</script> def & asöl öasdkl" ;
		String dst = "abc def & asöl öasdkl\"\"" ;
		
		System.out.println(StringFunctions.removeTags(src));
		
		assertEquals(StringFunctions.removeTags(src),dst) ;
	}
	
	@Test
	public void detoxifyTextTest() throws UnsupportedEncodingException {
 
		String src = "abc &lt;script&gt;alert('hello world');&lt;/script&gt; def <br> &amp; <p>asöl öasdkl&quot;\"</p>" ;
		String dst = "abc def &amp; asöl öasdkl&quot;&quot;" ;
		
		System.out.println(StringFunctions.detoxifyText(src));
		
		assertEquals(StringFunctions.detoxifyText(src),dst) ;
	}

}
