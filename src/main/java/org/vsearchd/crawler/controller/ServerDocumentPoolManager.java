package org.vsearchd.crawler.controller;

import org.apache.poi.ss.formula.eval.NotImplementedException;

public class ServerDocumentPoolManager implements IServerDocumentPool {

	private IServerDocumentPool currentQueue = null;
	private ServerDocumentPool primaryQueue = null;
	private ServerDocumentPool secondaryQueue = null;

	public ServerDocumentPoolManager() {
		primaryQueue = new ServerDocumentPool("primaryQueue");
		secondaryQueue = new ServerDocumentPool("secondaryQueue");
	}

	private synchronized void setCurrentQueue() {
		if (this.primaryQueue.containsMessage())
			this.currentQueue = this.primaryQueue;
		else
			this.currentQueue = this.secondaryQueue;
	}

	public synchronized void addPrimaryQueueMessage(String msg) {
		this.primaryQueue.addMessage(msg);
	}

	public synchronized void addSecondaryQueueMessage(String msg) {
		this.secondaryQueue.addMessage(msg);
	}

	public synchronized Boolean containsMessage() {
		return (this.primaryQueue.containsMessage() || this.secondaryQueue
				.containsMessage());
	}

	public synchronized void clear() {
		throw new NotImplementedException("don't use this ;)");
	}

	public synchronized String getMessage() {
		this.setCurrentQueue();
		return this.currentQueue.getMessage();
	}

	public synchronized long size() {
		throw new NotImplementedException("don't use this ;)");
	}

	public synchronized void addMessage(String msg) {
		throw new NotImplementedException("don't use this ;)");
	}

	public synchronized void deleteQueue() {
		this.setCurrentQueue();
		this.currentQueue.deleteQueue();
	}

	public synchronized void removeQueue() {
		this.setCurrentQueue();
		this.currentQueue.removeQueue();
	}
}
