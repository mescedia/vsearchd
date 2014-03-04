/*
 * vsearchd - a vertical search engine (crawler)
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

import com.sun.syndication.io.impl.Base64;

public class SourceEngineBinary extends SourceEngineBase {

	public SourceEngineBinary() {
	}

	@Override
	public String getSource(InputStream inputStream) throws IOException {

		return "<?xml version=\"1.0\" encoding=\""
				+ this.getSourceEncoding()
				+ "\"?><html><head><title></title></head><body>"
				+ "<![CDATA["
				+ new String(Base64.encode(org.apache.tika.io.IOUtils
						.toByteArray(inputStream))) + "]]>" + "</body></html>";
	}

	@Override
	public void Reset() {
		super.Reset();
	}

}