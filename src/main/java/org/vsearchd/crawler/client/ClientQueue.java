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

import java.util.Iterator;
import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientQueue {

	private Logger log = LoggerFactory.getLogger(getClass().getName());
	private static ClientQueue instance = null;
	private BlockingQueue<Client> queue = new LinkedBlockingQueue<Client>();

	private ClientQueue() {

	}

	public synchronized static ClientQueue Instance() {
		if (instance == null)
			instance = new ClientQueue();
		return instance;
	}

	public synchronized void clear() {
		this.queue.clear();
	}

	public synchronized int size() {
		return this.queue.size();
	}

	public synchronized Iterator<Client> getIterator() {
		return this.queue.iterator();
	}

	public synchronized void add(Client cli) {

		try {
			this.queue.add(cli);
			log.info("client added to client-queue ...");
		} catch (Exception ex) {
			log.error("***** THIS SHOULD NOT HAPPEN: HUNG IN ClientQueue add (CLIENT) {"
					+ String.valueOf(queue.size())
					+ " - clients in queue} ******");
			log.error(ex.getMessage());
		}
	}

	public synchronized void remove(Client cli) {

		try {
			if (this.queue.contains(cli)) {
				this.queue.remove(cli);
			} else {
				log.error("client not in queue - cannot remove: "
						+ cli.get_ClientID());
			}
		} catch (Exception ex) {
			log.error("***** THIS SHOULD NOT HAPPEN: HUNG IN ClientQueue remove (CLIENT) {"
					+ String.valueOf(queue.size())
					+ " - clients in queue} ******");
			log.error(ex.getMessage());
		}
	}
}