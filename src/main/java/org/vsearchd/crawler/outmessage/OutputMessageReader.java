package org.vsearchd.crawler.outmessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class OutputMessageReader {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private OutputMessage outputMessage = null;
	private XMLReader xmlReader = null;
	private OutputMessageReaderHandler handler = null;

	public OutputMessageReader() {
		handler = new OutputMessageReaderHandler();
		try {
			xmlReader = XMLReaderFactory.createXMLReader();
		} catch (SAXException e) {
			log.error(e.getMessage());
		}
	}

	public OutputMessage getOutputMessage() {
		return this.outputMessage;
	}

	public void Read(String msg) throws Exception {
		this.Read(new InputSource(new StringReader(msg)));
	}

	private void Read(InputSource inputSource) throws Exception {
		try {
			xmlReader.setContentHandler(handler);
			xmlReader.parse(inputSource);
			this.outputMessage = handler.getOutputMessage();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (SAXException e) {
			log.error(e.getMessage());
		}
	}
}
