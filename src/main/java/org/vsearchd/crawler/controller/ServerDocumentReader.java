package org.vsearchd.crawler.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


// make sure this is created only once per client-session !!!
public class ServerDocumentReader {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private ServerDocument serverDocument = null;
	private XMLReader xmlReader = null;
	private ServerDocumentContentHandler handler = null;

	public ServerDocumentReader() {
		handler = new ServerDocumentContentHandler();
		try {
			xmlReader = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			log.error(e.getMessage());
		}
	}

	public ServerDocument getServerDocument() {
		return this.serverDocument;
	}

	public void ReadFromString(String request) throws Exception {
		this.Read(new InputSource(new StringReader(request)));
	}

	public void ReadFromFilePath(final String filePath) throws Exception {
		this.Read(new InputSource(new FileReader(filePath)));
	}

	private void Read(InputSource inputSource) throws Exception {

		try {
			xmlReader.setContentHandler(handler);
			xmlReader.parse(inputSource);
			this.serverDocument = handler.getServerDocument();

		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (SAXException e) {
			log.error(e.getMessage());
		}
	}
}
