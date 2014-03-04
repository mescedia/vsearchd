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