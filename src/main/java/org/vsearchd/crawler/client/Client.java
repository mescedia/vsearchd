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

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.backend.BackendSessionHandler;
import org.vsearchd.crawler.controller.ServerDocumentPoolManager;

public class Client {

	private Logger log = LoggerFactory.getLogger(getClass().getName());
	public static final long ThreadSleep = 100;
	private ClientMessagePool messagePool = new ClientMessagePool();
	private ServerDocumentPoolManager serverDocumentManager = null;
	private final String clientID = UUID.randomUUID().toString();
	private ClientState state = ClientState.None;
	public ClientTransformerCache TransformerCache = new ClientTransformerCache();
	private BackendSessionHandler backendHandler = null;
	private int projectDelay = 1;
	private int msgDelay = 0;

	private ClientRequest request = null;

	public Client(ClientRequest r) {
		this.request = r;
		this.backendHandler = new BackendSessionHandler(r);
		this.backendHandler.initSession();
		this.serverDocumentManager = new ServerDocumentPoolManager();
		this.projectDelay = this.request.getProjectDelay();
	}

	public synchronized void activate() {
		if (this.state == ClientState.Running) {
			log.error("client already activated !!!");
			return;
		}

		try {
			this.backendHandler.connect();
		} catch (Exception e1) {
			log.error(e1.getMessage());
			this.state = ClientState.Finished;
			return;
		}

		this.state = ClientState.Running;
	}

	public ClientRequest getClientRequest() {
		return this.request;
	}

	private synchronized int getMessageDelay() {
		return this.messagePool.size() * 2;
	}

	public synchronized int getHttpDelay() {
		msgDelay = getMessageDelay();
		if (msgDelay > this.projectDelay) {
			return msgDelay;
		}
		return this.projectDelay;
	}

	public String get_ClientID() {
		return this.clientID;
	}

	public ServerDocumentPoolManager get_ServerDocumentManger() {
		return this.serverDocumentManager;
	}

	public synchronized Boolean containsMessage() {
		return this.messagePool.containsMessage();
	}

	public synchronized ClientState getState() {
		return this.state;
	}

	public synchronized void setFinishedState() {
		this.state = ClientState.Finished;
	}

	public synchronized void shutdown() throws Exception {
		this.backendHandler.disconnect();
		this.messagePool.clear();
		this.messagePool = null;
		this.state = ClientState.Finished;
		Thread.sleep(Client.ThreadSleep + 100);
	}

	public synchronized ClientMessagePool getMessagePool() {
		return this.messagePool;
	}

	public synchronized void sendMessage() throws Exception {
		this.backendHandler.sendMessage(this.messagePool.getMessage());
	}

	public synchronized void sendMessage(String msg) throws Exception {
		this.backendHandler.sendMessage(msg);
	}
}
