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

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.vsearchd.crawler.client.Client;
import org.vsearchd.crawler.client.ClientQueue;
import org.vsearchd.crawler.client.ClientRequest;
import org.vsearchd.crawler.client.ClientRequestReader;
import org.vsearchd.crawler.controller.ServerDocumentReader;
import org.vsearchd.crawler.sourcefactory.XmlSourceRequest;

public class ServerHandler extends IoHandlerAdapter {
	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private Iterator<Client> it = null;
	private Client client = null;

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.error(cause.getMessage());
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		String str = message.toString().trim();
		if (str.isEmpty())
			return;

		if (str.startsWith("<?xml")) {
			ClientRequestReader reader = new ClientRequestReader();
			ClientRequest request = null;
			reader.ReadFromString(str);
			request = reader.getClientRequest();

			if (request.getRequestType().equalsIgnoreCase("run_project")) {
				it = ClientQueue.Instance().getIterator();
				while (it.hasNext()) {
					client = (Client) it.next();
					if (client.getClientRequest().getProject()
							.equalsIgnoreCase(request.getProject())
							&& client.getClientRequest().getUser()
									.equalsIgnoreCase(request.getUser())) {
						log.error("project already running - bye {"
								+ request.getProject() + "}");
						session.write("project already running - bye {"
								+ request.getProject() + "}");
						session.close(false);
						return;
					}
				}

				session.write("start running project {" + request.getProject() + "}");
				session.close(false);

				ClientQueue.Instance().add(new Client(request));
			} else if (request.getRequestType()
					.equalsIgnoreCase("stop_project")) {
				it = ClientQueue.Instance().getIterator();
				while (it.hasNext()) {
					client = (Client) it.next();
					if (client.getClientRequest().getProject()
							.equalsIgnoreCase(request.getProject())
							&& client.getClientRequest().getUser()
									.equalsIgnoreCase(request.getUser())) {
						client.setFinishedState();

						session.write("stopping project - bye {"
								+ request.getProject() + "}");
						session.close(false);
						return;
					}
				}
				log.error("could not set client (finish) state - client already offline {"
						+ request.getProject() + "}");
			} else if (request.getRequestType().equalsIgnoreCase("set_source")) {

				ServerDocumentReader srvReader = new ServerDocumentReader();
				srvReader.ReadFromFilePath(request.getServerRequestFile());

				String xml = XmlSourceRequest.getXmlSource(
						srvReader.getServerDocument().uriRequest, request);

				try {

					File f = new File(request.getOutputFilename());
					if (f.exists())
						f.delete();

					FileWriter fstream = new FileWriter(
							request.getOutputFilename());
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(xml);

					out.close();
				} catch (Exception e) {
					log.error(e.getMessage());
					session.write("could not create/remove file ...");
					session.close(true);
					return;
				}
				session.write("file created ...");
				session.close(true);
			} else {
				session.write("unknown request ...");
				session.close(true);
			}
		} else {
			log.error("received invalid clinet-request:" + str);
		}

		if (session.isConnected())
			session.close(false);
		log.debug("client-request terminated ...");
	}

	@Override
	public void messageSent(IoSession session, Object message) {
		log.debug("message sent sessionid : " + session.getId());
		log.debug("message sent message : " + String.valueOf(message));
	}

	@Override
	public void sessionCreated(IoSession session) {
		log.debug("session created : " + session.getId());
	}

	@Override
	public void sessionClosed(IoSession session) {
		log.debug("session closed : " + session.getId());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		log.warn("idle state reached - disconnecting client ... ;)");
		session.close(false);
	}

	@Override
	public void sessionOpened(IoSession session) {
		log.debug("session opened : " + session.getId());
	}
}
