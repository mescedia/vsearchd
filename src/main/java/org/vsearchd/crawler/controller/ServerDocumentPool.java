package org.vsearchd.crawler.controller;

import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerDocumentPool implements IServerDocumentPool {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private String name = null;
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

	public ServerDocumentPool(String _name) {
		this.name = _name;
	}

	public synchronized Boolean containsMessage() {
		return !this.queue.isEmpty();
	}

	public synchronized void clear() {
		this.queue.clear();
	}

	public synchronized long size() {
		return Long.valueOf(this.queue.size());
	}

	public synchronized String getMessage() {
		return this.queue.poll();
	}

	public synchronized void addMessage(String msg) {
		if (this.queue.size() % 200 == 0 && this.queue.size() > 0)
			log.warn(this.name + " is growing -> "
					+ String.valueOf(this.queue.size()) + " messages ...");
		this.queue.add(msg);
	}

	public synchronized void deleteQueue() {
		if (!this.containsMessage())
			this.removeQueue();
	}

	public synchronized void removeQueue() {
		this.queue.clear();
	}
}