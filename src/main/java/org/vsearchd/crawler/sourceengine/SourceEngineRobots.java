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

package org.vsearchd.crawler.sourceengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SourceEngineRobots extends SourceEngineBase {

	private String line = null;
	private String returnSource = null;
	private StringBuilder stringBuilder = new StringBuilder();
	private BufferedReader bufferedReader = null;
	
	private String key = null ;
	private String value = null;

	public SourceEngineRobots() {

	}

	@Override
	public String getSource(InputStream inputStream) throws IOException {
		line = null;
		
		stringBuilder.append("<?xml  version=\"1.0\" encoding=\"" + this.getSourceEncoding() + "\"?> ") ;
		stringBuilder.append( "<robots>") ;
					

		bufferedReader = new BufferedReader(new InputStreamReader(inputStream, this.getSourceEncoding()));
		
		boolean uaOpen = false ;
		
		while ((line = bufferedReader.readLine()) != null) {
			
			if (line.trim().startsWith("#") || line.trim().equals("") || line.indexOf(':') == -1)
				continue ;
			
			line = line.replaceAll("#.*$", "") ;
			key = line.substring(0,line.indexOf(':')) ;
			value = line.substring(line.indexOf(':')+1).trim() ;
			
			if (key.toLowerCase().equals("user-agent")) {
				if(uaOpen)	
					stringBuilder.append("</user-agent>") ;
				
				stringBuilder.append("<user-agent name=\"" + value + "\" >") ;
				uaOpen = true;				
				continue ;
			}
						
			stringBuilder.append( "<rule " +
					 "key=\""+ key  +"\" " +
					 "value=\""+ value  +"\" />") ; 
		}
		
		if(uaOpen)	{
			stringBuilder.append("</user-agent>") ;
		}
		
		stringBuilder.append( "</robots>") ;
		bufferedReader.close();
		returnSource = stringBuilder.toString();
		stringBuilder.setLength(0);
		return returnSource;
	}

	@Override
	public void Reset() {
		super.Reset();
	}
}
