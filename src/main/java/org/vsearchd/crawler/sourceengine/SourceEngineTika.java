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

import java.io.InputStream;
import java.io.Reader;
import org.apache.tika.io.IOUtils;
import org.apache.tika.parser.ParsingReader;

public class SourceEngineTika extends SourceEngineBase {

	private Reader reader = null;
	private String content = "";

	@Override
	public String getSource(InputStream inputStream) {

		try {
			reader = new ParsingReader(inputStream);
			content = IOUtils.toString(reader);
		} catch (Exception ex) {
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return "<?xml version=\"1.0\" encoding=\"" + this.getSourceEncoding()
				+ "\"?><html><head><title></title></head><body>" + "<![CDATA["
				+ content + "]]>" + "</body></html>";
	}

	@Override
	public void Reset() {
		super.Reset();
		IOUtils.closeQuietly(reader);
		content = "";
	}

}
