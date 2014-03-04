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

package org.vsearchd.crawler.backend;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackendSessionJMS extends BackendSessionBase implements
		IBackendSession {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private ConnectionFactory connectionFactory = null;
	private Connection connection = null;
	private Session session = null;
	private MessageProducer producer = null;
	private Destination destination = null;
	private Message message = null;

	public BackendSessionJMS(BackendServer srv) {
		super(srv);
	}

	public synchronized void connect() {

		try {
			connectionFactory = new ActiveMQConnectionFactory(this
					.getBackendServer().getConnectionString());
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createQueue(this.getBackendServer()
					.getQueue());
			producer = session.createProducer(destination);
		} catch (JMSException ex) {
			log.error("could not connect server:" + ex.getMessage());
		}
	}

	public synchronized void sendMessage(String msg) {

		try {
			message = session.createTextMessage(msg);
			producer.send(message);

		} catch (JMSException e) {
			log.error("could not send message:" + e.getMessage());
		}
	}

	public synchronized void disconnect() {
		try {
			this.connection.close();
			log.info("backend disconnected gracefully ...");
		} catch (JMSException e) {
			log.error("could not disconnect:" + e.getMessage());
		}
	}
}
