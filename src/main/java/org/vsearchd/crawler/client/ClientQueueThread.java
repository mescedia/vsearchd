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

package org.vsearchd.crawler.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.controller.SessionController;

public class ClientQueueThread implements Runnable {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private Client client = null;

	public ClientQueueThread() {
	}

	public void setClient(Client cli) {
		this.client = cli;
	}

	public synchronized void run() {
		BufferedReader in = null;
		SessionController controller = null;

		try {
			controller = new SessionController(this.client);
		} catch (JMSException e) {
			log.error(e.getMessage());

			// 
			try {
				client.shutdown();
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
			
			return;
		}

		try {
			in = new BufferedReader(new FileReader(this.client
					.getClientRequest().getServerRequestFile()));
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			
			// 
			try {
				client.shutdown();
			} catch (Exception e1) {
				log.error(e.getMessage());
			}
			
			return;
		}
		String line = "";
		String serverMsg = "";
		try {
			while ((line = in.readLine()) != null) {
				serverMsg += line;
			}
			in.close();
		} catch (IOException e) {
			log.error(e.getMessage());
			
			// 
			try {
				client.shutdown();
			} catch (Exception e1) {
				log.error(e.getMessage());
			}
			
			return;
		}

		controller.addDocument(serverMsg);
		boolean initial = true;
		try {
			while (controller.runController(initial)) {
				initial = false;
			}
		} catch (Exception e) {
			log.error("error executing project-controller: " + e.getMessage());
			try {
				client.shutdown();
			} catch (Exception e1) {				
				log.error(e1.getMessage());
			}
		}
	}	
}
