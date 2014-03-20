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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.tidy.Tidy;

public class SourceEngineTidy extends SourceEngineBase {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private Tidy tidy = new Tidy();
	private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	private String encoding = null;
	private String returnXml = "";

	public SourceEngineTidy() {
		tidy.setQuiet(true);
		tidy.setShowErrors(0);
		tidy.setShowWarnings(false);
		tidy.setTidyMark(false);
		tidy.setDocType("omit");
		tidy.setDropEmptyParas(false);
		tidy.setFixComments(true);
		tidy.setHideEndTags(false);
		tidy.setIndentAttributes(true);
		tidy.setMakeClean(true);
		tidy.setXmlOut(true);
		tidy.setXmlSpace(false);
		tidy.setXmlPi(false);
		tidy.setDropProprietaryAttributes(true);
		tidy.setForceOutput(true);
		tidy.setOutputEncoding("UTF-8");
	}

	@Override
	public String getSource(InputStream inputStream) {
		try {
			returnXml = "";
			encoding = this.getSourceEncoding();

			tidy.setOutputEncoding(encoding);
			outputStream.reset();
			tidy.parse(inputStream, outputStream);
			inputStream.close();

			returnXml = outputStream.toString();
			outputStream.close();

			return "<?xml  version=\"1.0\" encoding=\"" + encoding + "\"?> "
					+ returnXml;
		} catch (Exception ex) {
			log.error("tidy transformation error: " + ex.getMessage());

			try {
				inputStream.close();
			} catch (IOException e) {
				log.error("error closing inputStream: " + e.getMessage());
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				log.error("error closing outputStream: " + e.getMessage());
			}
		}
		return null;
	}

	@Override
	public void Reset() {
		super.Reset();
	}

}
