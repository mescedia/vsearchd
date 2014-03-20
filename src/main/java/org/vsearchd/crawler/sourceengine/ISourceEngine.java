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

package org.vsearchd.crawler.sourceengine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public interface ISourceEngine {
	String getSource(InputStream inputStream) throws IOException;

	void setRemoveContent(ArrayList<String> removeContent);

	ArrayList<String> getRemoveContent();

	void setRegexReplace(Hashtable<String, String> replace);

	Hashtable<String, String> getRegexReplace();

	String handleRemoveContents(String source);

	String handleRegexReplace(String source);

	void addContentType(String ct);

	ArrayList<String> getContentTypes();

	void setName(String name);

	String getName();

	void setSourceEncoding(String encoding);

	String getSourceEncoding();

}