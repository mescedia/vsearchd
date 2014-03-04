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

package org.vsearchd.crawler.sourcefactory;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.helper.ServerConfig;

public class HttpClientFactory {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private static int numClientsDefault = 5; 
	private static int numClients = 0; 
	private static HttpClientFactory instance = null;

	private ArrayList<HttpClient> clientList = null;
	private ArrayList<Boolean> inUseList = null;

	private long lastRequest = 0;

	private HttpClientFactory() {
		//
		numClients = ServerConfig.getInstance().getNumHttpClients();
		if (numClients == 0) {
			log.warn("number of http-clients not stated in server-config - use default:"
					+ String.valueOf(numClientsDefault));
			numClients = numClientsDefault;
		}

		this.clientList = new ArrayList<HttpClient>(numClients);
		this.inUseList = new ArrayList<Boolean>(numClients);
		for (int i = 0; i < numClients; i++) {
			this.clientList.add(new HttpClient(i));
			this.inUseList.add(false);
		}
	}

	public long getLastRequest() {
		return this.lastRequest;
	}

	public static HttpClientFactory getInstance() {
		if (instance == null)
			instance = new HttpClientFactory();
		return instance;
	}

	private int cnt = 0;

	public synchronized HttpClient getHttpClient() throws InterruptedException {
		while (true) {
			cnt = 0;
			for (Boolean b : this.inUseList) {
				if (!b) {
					this.inUseList.set(cnt, true);
					lastRequest = System.currentTimeMillis();
					return this.clientList.get(cnt);
				}
				cnt++;
			}
			Thread.sleep(50);
		}
	}

	public void Reset(int c) {
		this.inUseList.set(c, false);
		this.clientList.get(c).Reset();
	}
}
