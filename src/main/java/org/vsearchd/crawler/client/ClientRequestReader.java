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

/*
 * @author Michael Kassnel
 *
 */
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
