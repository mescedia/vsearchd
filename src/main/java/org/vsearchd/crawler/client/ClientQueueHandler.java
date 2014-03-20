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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientQueueHandler implements Runnable {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	public ClientQueueHandler() {
		log.info("client-manager initialized ... ");
	}

	public void run() {

		ClientQueue clientQueue = ClientQueue.Instance();
		Iterator<Client> it = null;
		Client cCli = null;

		while (true) {
			try {
				it = clientQueue.getIterator();
				while (it.hasNext()) {
					cCli = (Client) it.next();

					if (cCli.getState().equals(ClientState.Finished)) {

						ClientQueue.Instance().remove(cCli);
						log.info("project shutdown succeeded ...");
					} else if (cCli.getState().equals(ClientState.None)
							&& cCli.getClientRequest().getRequestType()
									.equalsIgnoreCase("run_project")) {
						ClientQueueThread ctd = new ClientQueueThread();
						ctd.setClient(cCli);
						Thread t0 = new Thread(ctd);
						t0.setName(cCli.get_ClientID());
						t0.start();
						log.info("client thread started : "
								+ String.valueOf(cCli.get_ClientID()));
					}
					Thread.sleep(100);
				}
				Thread.sleep(50);
			} catch (InterruptedException e) {
				log.error(e.getMessage());
				log.error("********* THIS SHOULD NOT HAPPEN: ERROR IN CLIENT-QUEUE-HANDLER {1} *********");
			} catch (Exception e) {
				log.error(e.getMessage());
				log.error("********* THIS SHOULD NOT HAPPEN: ERROR IN CLIENT-QUEUE-HANDLER {2} *********");
			}
		}
	}
}
