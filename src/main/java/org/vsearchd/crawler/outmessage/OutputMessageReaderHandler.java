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

package org.vsearchd.crawler.outmessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.backend.BackendType;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class OutputMessageReaderHandler implements ContentHandler {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private OutputMessage outputMessage = null;
	private OutputField currentField = new OutputField();
	private boolean readFieldValue = false;
	private String backend = null;

	public OutputMessageReaderHandler() {

	}

	public OutputMessage getOutputMessage() throws Exception {
		return this.outputMessage;
	}

	public void startDocument() throws SAXException {

	}

	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {

		if (localName.equalsIgnoreCase("client"))
			return;

		if (localName.equalsIgnoreCase("output")) {
			try {
				this.outputMessage = OutputMessageFactory.getInstance()
						.getOutputMessage();
			} catch (InterruptedException e) {
				log.error(e.getMessage());
				return;
			}

			this.outputMessage.setTable(atts.getValue("table"));
			this.outputMessage.setAction(atts.getValue("action"));

			backend = atts.getValue("backend");

			if (backend.equalsIgnoreCase("any"))
				this.outputMessage.setBackendType(BackendType.Any);
			else if (backend.equalsIgnoreCase("devnull"))
				this.outputMessage.setBackendType(BackendType.DevNull);
			else if (backend.equalsIgnoreCase("http")) {
				this.outputMessage.setBackendType(BackendType.Http);
			} else if (backend.equalsIgnoreCase("https"))
				this.outputMessage.setBackendType(BackendType.Https);
			else if (backend.equalsIgnoreCase("jdbc"))
				this.outputMessage.setBackendType(BackendType.JDBC);
			else if (backend.equalsIgnoreCase("jms"))
				this.outputMessage.setBackendType(BackendType.Jms);

			return;
		}

		if (localName.equalsIgnoreCase("field")) {
			currentField.Reset();
			this.currentField.setName(atts.getValue("name"));
			this.currentField.setType(atts.getValue("type"));
			if (atts.getValue("unique") != null)
				this.currentField.setUnique(atts.getValue("unique"));
			if (atts.getValue("update") != null) {
				if (atts.getValue("update").equalsIgnoreCase("true"))
					this.currentField.setUpdate(true);
				else
					this.currentField.setUpdate(false);
			}

			this.readFieldValue = true;
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (this.readFieldValue
				&& (this.currentField.getName() != null || this.currentField
						.getType() != null)) {

			this.outputMessage.addField(this.currentField.getName(),
					this.currentField.getType(), new String(ch, start, length),
					this.currentField.getUnique(),
					this.currentField.getUpdate());
			this.currentField.Reset();
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
