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

package org.vsearchd.crawler.controller;

import org.vsearchd.crawler.sourceengine.ISourceEngine;
import org.vsearchd.crawler.sourcefactory.UriMethod;
import org.vsearchd.crawler.sourcefactory.UriProtocol;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class ServerDocumentContentHandler implements ContentHandler {

	private ServerDocument serverDocument = null;
	private ISourceEngine currentSourceEngine = null;

	public ServerDocumentContentHandler() {

	}

	public ServerDocument getServerDocument() throws Exception {
		return this.serverDocument;
	}

	public void startDocument() throws SAXException {

	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		if (localName.equalsIgnoreCase("followlink")
				|| localName.equalsIgnoreCase("server-document")) {
			this.serverDocument = ServerDocumentProvider.getInstance()
					.getServerDocument();
			return;
		}

		if (localName.equalsIgnoreCase("xslt-parameter")) {
			this.serverDocument.addXsltParamter(atts.getValue("name"),
					atts.getValue("value"));
			return;
		}

		if (localName.equalsIgnoreCase("source-engine")) {

			if (atts.getValue("name").equalsIgnoreCase("none"))
				this.currentSourceEngine = this.serverDocument.uriRequest
						.getNoneSourceEngine();
			else if (atts.getValue("name").equalsIgnoreCase("tidy"))
				this.currentSourceEngine = this.serverDocument.uriRequest
						.getTidySourceEngine();
			else if (atts.getValue("name").equalsIgnoreCase("htmlcleaner"))
				this.currentSourceEngine = this.serverDocument.uriRequest
						.getHtmlCleanerSourceEngine();
			else if (atts.getValue("name").equalsIgnoreCase("binary"))
				this.currentSourceEngine = this.serverDocument.uriRequest
						.getBinarySourceEngine();
			else if (atts.getValue("name").equalsIgnoreCase("devnull"))
				this.currentSourceEngine = this.serverDocument.uriRequest
						.getDevNullSourceEngine();
			else if (atts.getValue("name").equalsIgnoreCase("robots"))
				this.currentSourceEngine = this.serverDocument.uriRequest
						.getRobotsSourceEngine();
			else if (atts.getValue("name").equalsIgnoreCase("json"))
				this.currentSourceEngine = this.serverDocument.uriRequest
						.getJsonSourceEngine();
			else { // tika is the default
				this.currentSourceEngine = this.serverDocument.uriRequest
						.getTikaSourceEngine();
			}
			this.currentSourceEngine.setName(atts.getValue("name"));
			return;
		}

		if (localName.equalsIgnoreCase("content-type")) {
			this.currentSourceEngine.addContentType(atts.getValue("value"));
			return;
		}

		if (localName.equalsIgnoreCase("remove-content")) {
			this.currentSourceEngine.getRemoveContent().add(
					atts.getValue("value"));
			return;
		}

		if (localName.equalsIgnoreCase("regex-replace")) {
			this.currentSourceEngine.getRegexReplace().put(
					atts.getValue("pattern"), atts.getValue("replace"));
			return;
		}

		if (localName.equalsIgnoreCase("urirequest")) {

			for (int i = 0; i < atts.getLength(); i++) {
				if (atts.getLocalName(i).equalsIgnoreCase("root-tag")) {
					this.serverDocument.uriRequest.setShowRootTag(Boolean
							.parseBoolean(atts.getValue("root-tag")));
				} else if (atts.getLocalName(i).equalsIgnoreCase("http-header")) {
					this.serverDocument.uriRequest.setShowHttpHeader(Boolean
							.parseBoolean(atts.getValue("http-header")));
				} else if (atts.getLocalName(i).equalsIgnoreCase("dtd")) {
					this.serverDocument.uriRequest.setDtd(atts.getValue("dtd"));
				} else if (atts.getLocalName(i).equalsIgnoreCase("delay")) {
					this.serverDocument.uriRequest.setDelay(Integer
							.parseInt(atts.getValue("delay")));
				} else if (atts.getLocalName(i).equalsIgnoreCase("timeout")) {
					this.serverDocument.uriRequest.setTimeout(Integer
							.parseInt(atts.getValue("timeout")));
				} else if (atts.getLocalName(i).equalsIgnoreCase("retry")) {
					this.serverDocument.uriRequest.setRetry(Integer
							.parseInt(atts.getValue("retry")));
				} else if (atts.getLocalName(i).equalsIgnoreCase("name")) {
					this.serverDocument.uriRequest.setName(atts
							.getValue("name"));
				} else if (atts.getLocalName(i).equalsIgnoreCase(
						"mandatory-source-engine")) {
					this.serverDocument.uriRequest
							.setMandatorySourceEngine(atts
									.getValue("mandatory-source-engine"));
				}
			}
			return;
		}

		if (localName.equalsIgnoreCase("method")) {
			if (atts.getValue("value").equalsIgnoreCase("GET"))
				this.serverDocument.uriRequest.set_uriMethod(UriMethod.GET);
			else if (atts.getValue("value").equalsIgnoreCase("POST"))
				this.serverDocument.uriRequest.set_uriMethod(UriMethod.POST);
			else
				try {
					throw new Exception("cCraw: Method not implemented !!!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			return;
		}

		if (localName.equalsIgnoreCase("field")) {
			if (atts.getValue("name").equalsIgnoreCase("host"))
				this.serverDocument.uriRequest.set_host(atts.getValue("value"));
			else if (atts.getValue("name").equalsIgnoreCase("port"))
				this.serverDocument.uriRequest.set_port(Integer.parseInt(atts
						.getValue("value")));
			else if (atts.getValue("name").equalsIgnoreCase("path"))
				this.serverDocument.uriRequest.set_path(atts.getValue("value"));
			else if (atts.getValue("name").equalsIgnoreCase("protocol")) {
				if (atts.getValue("value").equalsIgnoreCase("http"))
					this.serverDocument.uriRequest
							.set_uriProtocol(UriProtocol.HTTP);
				else if (atts.getValue("value").equalsIgnoreCase("https"))
					this.serverDocument.uriRequest
							.set_uriProtocol(UriProtocol.HTTPS);
				else {
					try {
						throw new Exception("cCraw: unknown Protocol !!!");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return;
		}

		if (localName.equalsIgnoreCase("header")) {
			this.serverDocument.uriRequest.addHeader(atts.getValue("name"),
					atts.getValue("value"));
			return;
		}

		if (localName.equalsIgnoreCase("http-parameter")) {
			this.serverDocument.uriRequest.addUriParameter(
					atts.getValue("name"), atts.getValue("value"));
			return;
		}

		if (localName.equalsIgnoreCase("proxy")) {
			this.serverDocument.uriRequest.set_proxy(atts.getValue("host"),
					Integer.parseInt(atts.getValue("port")));
			return;
		}

		if (localName.equalsIgnoreCase("mapping")) {
			this.serverDocument.setMapping(atts.getValue("file"));
			return;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
	}

	public void endDocument() throws SAXException {
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void skippedEntity(String name) throws SAXException {
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}
}
