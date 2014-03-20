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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SourceEngineNone extends SourceEngineBase {

	private String line = null;
	private String returnSource = null;
	private StringBuilder stringBuilder = new StringBuilder();
	private BufferedReader bufferedReader = null;

	public SourceEngineNone() {

	}

	@Override
	public String getSource(InputStream inputStream) throws IOException {
		line = returnSource = null;

		bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
				this.getSourceEncoding()));

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
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
