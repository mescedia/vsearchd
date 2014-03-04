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