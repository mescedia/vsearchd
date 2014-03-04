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

package org.vsearchd.crawler.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jms.JMSException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.client.Client;
import org.vsearchd.crawler.client.ClientMessagePool;
import org.vsearchd.crawler.client.ClientQueue;
import org.vsearchd.crawler.client.ClientState;
import org.vsearchd.crawler.helper.ServerConfig;
import org.vsearchd.crawler.sourcefactory.HttpClient;
import org.vsearchd.crawler.sourcefactory.HttpClientFactory;

public class SessionController {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private ServerDocumentPoolManager serverDocumentManager = null;
	private Client client = null;
	private ServerDocumentReader serverDocumentReader = null;
	private HttpClient http = null;
	private String xmlSource = null;
	private TransformerFactory tFactory = null;
	private Transformer transformer = null;
	private String xmlResult = null;
	private StringWriter outWriter = new StringWriter();;
	private Pattern p = null;
	private Pattern pa = null;
	private Matcher mat = null;
	private Matcher mata = null;
	private StringBuffer sb = null;
	private ClientMessagePool q = null;
	private String msg = null;
	private String serverMsg = null;
	private ServerDocument sd = null;
	private String gOne = null;
	private Enumeration<String> e;
	private Hashtable<String, String> xsltParams = null;
	private int delayProject = 0;
	private int delayConfig = 0;
	private long delay = 0;
	private int statusCode = 0;

	private String name1 = "";
	private String value1 = "";
	private String name2 = "";

	public SessionController(Client c) throws JMSException {

		this.client = c;
		this.client.activate();
		this.tFactory = TransformerFactory.newInstance();
		this.serverDocumentManager = this.client.get_ServerDocumentManger();
		this.client.TransformerCache.setTransformerFactory(this.tFactory);
		this.serverDocumentReader = new ServerDocumentReader();

		this.pa = Pattern .compile("\\<server[\\s]+queue[\\s]*\\=[\\s]*\\\"(.*?)\\\"[\\s]*\\>(.*?)\\<\\/server\\>",
						Pattern.MULTILINE | Pattern.DOTALL | Pattern.UNIX_LINES);
		this.p = Pattern.compile("(\\<client.+?client\\>)", Pattern.MULTILINE
				| Pattern.DOTALL | Pattern.UNIX_LINES);
	}

	public void addDocument(String _msg) {
		// the initial server document will always be added to the primaryQueue
		// !!!
		this.serverDocumentManager.addPrimaryQueueMessage(_msg);
	}

	/**
	 * 
	 * @param initial
	 * @return
	 */
	public synchronized boolean runController(boolean initial) {

		try {

			if (!this.serverDocumentManager.containsMessage()) {
				log.error("no message in serverdocument pool ...");
				return false;
			}

			serverMsg = this.serverDocumentManager.getMessage();
			this.serverDocumentReader.ReadFromString(serverMsg);

			sd = this.serverDocumentReader.getServerDocument();

			sd.uriRequest
					.setCurrnetHttpRequestDelay(this.client.getHttpDelay());
			sd.uriRequest.setCurrnetNumClients(ClientQueue.Instance().size());
			sd.uriRequest.setCurrentClientMsgPoolSize(this.client
					.getMessagePool().size());
			sd.uriRequest.setCurrentClientID(this.client.getClientRequest()
					.getUser()
					+ "-"
					+ this.client.getClientRequest().getProject());

			// delay calc   
			
			delay = delayProject = sd.uriRequest.getCurrentHttpRequestDelay();
			delayConfig = (int) (1000 / ServerConfig.getInstance().getMaxRequestsPerSecond());

			if (delayProject < delayConfig) {
				delay = delayConfig;
			}
			
			delay =  sd.uriRequest.getCurrentHttpRequestDelay() ;			
			delay = ((HttpClientFactory.getInstance().getLastRequest() + delay ) - System.currentTimeMillis()) ;
			
			if(delay > 0)	{				
				Thread.sleep( delay );
			}

			// 
			if (initial) {
				e = this.client.getClientRequest().getHttpParameter().keys();
				while (e.hasMoreElements()) {
					name1 = e.nextElement();
					sd.uriRequest.addUriParameter(name1, this.client
							.getClientRequest().getHttpParameter().get(name1));
				}

				xsltParams = this.client.getClientRequest().getXsltParameter();
				e = xsltParams.keys();
				while (e.hasMoreElements()) {
					name2 = e.nextElement();
					sd.addXsltParamter(name2, xsltParams.get(name2));
				}
				e = null;
			}

			//
			http = HttpClientFactory.getInstance().getHttpClient();
			http.setRequest(sd.uriRequest);

			try {
				xmlSource = http.GetSource();
				statusCode = http.getStatusCode();
			} catch (Exception ex) {
				http.releaseConnection();
				log.error("could not retrieve source:" + ex.getMessage());
				log.error(ex.getStackTrace().toString());
				log.error("try to release client connection due to previous error ...");
			}

			HttpClientFactory.getInstance().Reset(http.getClientID());
			http = null;

			try {
				transformer = this.client.TransformerCache.getTransformer(sd
						.getMapping());
				e = sd.get_xsltParameter().keys();
				while (e.hasMoreElements()) {
					name1 = e.nextElement();
					value1 = sd.get_xsltParameter().get(name1);
					transformer.setParameter(name1, value1);
					log.debug("setting xslt parameter: " + name1 + "=" + value1);
				}

				// set  default xslt parameter
				transformer.setParameter("XmlSource", xmlSource);
				transformer.setParameter("SessionID", this.client.get_ClientID());
				transformer.setParameter("HttpStatusCode", String.valueOf(statusCode));
			} catch (TransformerConfigurationException e) {
				log.error("transformation error: " + e.getMessage());
				this.prepareClientExit();
				return false;
			}

			outWriter.getBuffer().setLength(0);

			try {
				transformer.transform(new StreamSource(new StringReader(
						xmlSource)), new StreamResult(outWriter));
				sb = outWriter.getBuffer();
				xmlResult = sb.toString();
				sb.setLength(0);

				msg = null;
				q = client.getMessagePool();
				mat = p.matcher(xmlResult);
				while (mat.find()) {
					msg = mat.group().replaceAll("\r", "");
					msg = mat.group().replaceAll("\n", "");
					q.add(msg);
				}
				mat.reset();

				mata = pa.matcher(xmlResult);
				while (mata.find()) {
					gOne = mata.group(1).trim();
					if ((gOne.toLowerCase().equalsIgnoreCase("primary")
							|| gOne.equalsIgnoreCase("1") || gOne.toLowerCase()
							.equalsIgnoreCase("one"))
							&& !this.client.getState().equals(
									ClientState.Finished))
						this.serverDocumentManager.addPrimaryQueueMessage(mata
								.group(2).trim());
					else if ((gOne.toLowerCase().equalsIgnoreCase("secondary")
							|| gOne.equalsIgnoreCase("2") || gOne.toLowerCase()
							.equalsIgnoreCase("two"))
							&& !this.client.getState().equals(
									ClientState.Finished))
						this.serverDocumentManager
								.addSecondaryQueueMessage(mata.group(2).trim());
					else {
						log.warn("server document not added to queue -> client-state: "
								+ this.client.getState().toString()
								+ "-> "
								+ gOne);
						log.warn(xmlResult);
						this.prepareClientExit();
						return false;
					}
				}
				mata.reset();
			} catch (TransformerException e) {
				log.error(e.getMessage());
			}

			transformer.clearParameters();
			transformer.reset();
			if (sd != null)
				ServerDocumentProvider.getInstance().resetServerDocument(sd);

			if (this.serverDocumentManager.containsMessage()
					&& !this.client.getState().equals(ClientState.Finished)) {
				return true;
			}
			
			this.prepareClientExit();

			log.info("session finished ...");
		} catch (Exception ex) {
			log.error(ex.getMessage());
			log.error(" ***** ERROR in runController - this should not happen ****** ");
		}
		return false;
	}

	/**
	 * client exit preparation
	 */
	private void prepareClientExit() {

		try {
			if (client != null) {
				int c = 0;
				while (client.containsMessage()) {
					log.debug("waiting for all clientmessages to be send ... ");
					Thread.sleep(500);
					c++;
					if (c > 20) {
						log.warn("not all messages sent - force client release ... ");
						break;
					}
				}
				client.shutdown();
			} else {
				log.error("client already null - cannot prepare for exit ...");
			}
		} catch (Exception ex) {
			log.error("could not prepare client for exit ...");
			log.error(ex.getMessage());
		}
	}
}