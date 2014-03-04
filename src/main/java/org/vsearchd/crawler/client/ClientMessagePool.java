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

import java.util.concurrent.*;

public class ClientMessagePool {

	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	public ClientMessagePool() {

	}

	public synchronized Boolean containsMessage() {
		if (this.queue.size() == 0)
			return false;
		return true;
	}

	public synchronized void clear() {
		this.queue.clear();
	}

	public synchronized int size() {
		return this.queue.size();
	}

	public synchronized String getMessage() {
		return this.queue.poll();
	}

	public synchronized void add(String str) {
		this.queue.add(str);
	}

}