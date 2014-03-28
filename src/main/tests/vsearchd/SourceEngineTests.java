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


import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.vsearchd.crawler.sourceengine.SourceEngineJson;
import org.vsearchd.crawler.sourceengine.SourceEngineRobots;

public class SourceEngineTests {
	

	@Test
	public void robots_txt_test() throws IOException {
		
		FileInputStream fis = new FileInputStream("src/main/tests/resources/robots.txt"); 
		
		
		SourceEngineRobots r = new SourceEngineRobots() ;
		String result = r.getSource(fis) ;
		
		System.out.println(result);
		
		Assert.assertTrue(result.startsWith("<?xml"));
		
	}
	
	
	@Test
	public void json_2_xml_test() throws IOException {
		
		FileInputStream fis = new FileInputStream("src/main/tests/resources/test.json"); 
		
		SourceEngineJson r = new SourceEngineJson() ;
		String result = r.getSource(fis) ;
		
		System.out.println(result);
		
		Assert.assertTrue(result.startsWith("<?xml"));
		
	}

}
