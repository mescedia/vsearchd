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

package org.vsearchd.crawler.client;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageQueueHandler implements Runnable {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	public ClientMessageQueueHandler() {
	}

	public void run() {
		ClientQueue clientQueue = ClientQueue.Instance();
		Iterator<Client> it = null;
		Client cCli = null;

		while (true) {
			try {
				Thread.sleep(50);
				if (clientQueue.size() > 0) {
					it = clientQueue.getIterator();
					while (it.hasNext()) {
						cCli = (Client) it.next();
						while (cCli.containsMessage()) {
							cCli.sendMessage();
						}
					}
				}
				Thread.sleep(50);
			} catch (Exception e) {
				if (cCli != null) {
					cCli.setFinishedState();
					log.debug("client is going to leave: {"	+ cCli.get_ClientID() + "} => " + e.getMessage());
				} else {
					log.error("********* THIS SHOULD NOT HAPPEN: ERROR IN CLIENT-MESSAGE-THREAD {2} {null} ********* => "
							+ e.getMessage());
				}
			}
		}
	}
}