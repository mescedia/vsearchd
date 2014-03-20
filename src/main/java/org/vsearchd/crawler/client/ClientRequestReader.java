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

package org.vsearchd.crawler.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ClientRequestReader {

	private Logger log = LoggerFactory.getLogger(getClass().getName());
	private ClientRequestHandler handler = new ClientRequestHandler();
	private ClientRequest request = null;
	private XMLReader xmlReader = null;

	public ClientRequestReader() {
		try {
			xmlReader = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			log.error(e.getMessage());
			return;
		}
	}

	public ClientRequest getClientRequest() {
		return this.request;
	}

	public void ReadFromString(String request) throws Exception {
		this.Read(new InputSource(new StringReader(request)));
	}

	private void Read(InputSource inputSource) throws Exception {
		try {
			xmlReader.setContentHandler(handler);
			xmlReader.parse(inputSource);
			this.request = handler.getClientRequest();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
