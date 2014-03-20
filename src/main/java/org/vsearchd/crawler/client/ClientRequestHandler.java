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

package org.vsearchd.crawler.client;

import org.vsearchd.crawler.backend.BackendServer;
import org.vsearchd.crawler.backend.BackendType;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class ClientRequestHandler implements ContentHandler {

	private ClientRequest request = new ClientRequest();

	public ClientRequestHandler() {
	}

	public ClientRequest getClientRequest() {
		return this.request;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		if (localName.equalsIgnoreCase("request")) {
			this.request.setRequestType(atts.getValue("type"));
		}

		if (localName.equalsIgnoreCase("options")) {
			this.request.setServerRequestFile(atts.getValue("server-request"));

			// if available !!!
			this.request.setOutputFilename(atts.getValue("outfile"));
		}

		if (localName.equalsIgnoreCase("auth-key")) {
			this.request.setAuthKey((atts.getValue("value")));
		}

		if (localName.equalsIgnoreCase("user")) {
			this.request.setUser((atts.getValue("value")));
		}

		if (localName.equalsIgnoreCase("project")) {
			this.request.setProject((atts.getValue("name")));
			this.request
					.setProjectDelay(Integer.valueOf(atts.getValue("delay")));
		}

		if (localName.equalsIgnoreCase("backend")) {

			if (atts.getValue("type").equalsIgnoreCase("jms")) {

				BackendServer backendServer = new BackendServer();
				backendServer.setBackendType(BackendType.Jms);
				backendServer.setConnectionString(atts
						.getValue("connection-string"));
				backendServer.setQueue(atts.getValue("queue"));
				backendServer.setUser(atts.getValue("user"));
				backendServer.setPassword(atts.getValue("passwd"));

				this.request.addBackendServer(backendServer);
			} else if (atts.getValue("type").equalsIgnoreCase("jdbc")) {
				BackendServer backendServer = new BackendServer();

				backendServer.setBackendType(BackendType.JDBC);
				backendServer.setConnectionString(atts
						.getValue("connection-string"));
				backendServer.setDriver(atts.getValue("driver"));
				backendServer.setUser(atts.getValue("user"));
				backendServer.setPassword(atts.getValue("passwd"));

				this.request.addBackendServer(backendServer);
			} else if (atts.getValue("type").equalsIgnoreCase("http")) {

				BackendServer backendServer = new BackendServer();
				backendServer.setBackendType(BackendType.Http);
				backendServer.setHost(atts.getValue("host"));
				backendServer.setPort(Integer.valueOf(atts.getValue("port")));
				backendServer.setFilePath(atts.getValue("filepath"));

				this.request.addBackendServer(backendServer);
			} else if (atts.getValue("type").equalsIgnoreCase("https")) {

				BackendServer backendServer = new BackendServer();
				backendServer.setBackendType(BackendType.Https);
				backendServer.setHost(atts.getValue("host"));
				backendServer.setPort(Integer.valueOf(atts.getValue("port")));
				backendServer.setFilePath(atts.getValue("filepath"));

				this.request.addBackendServer(backendServer);
			} else if (atts.getValue("type").equalsIgnoreCase("dev-null")) {
				BackendServer backendServer = new BackendServer();
				backendServer.setBackendType(BackendType.DevNull);

				this.request.addBackendServer(backendServer);
			}
		}

		if (localName.equalsIgnoreCase("http-parameter")) {
			this.request.addHttpParameter(atts.getValue("name"),
					atts.getValue("value"));
		}

		if (localName.equalsIgnoreCase("xslt-parameter")) {
			this.request.addXsltParameter(atts.getValue("name"),
					atts.getValue("value"));
		}
	}

	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {

	}

	public void endDocument() throws SAXException {

	}

	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
	}

	public void endPrefixMapping(String arg0) throws SAXException {
	}

	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
	}

	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
	}

	public void setDocumentLocator(Locator arg0) {

	}

	public void skippedEntity(String arg0) throws SAXException {
	}

	public void startDocument() throws SAXException {

	}

	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
	}
}
