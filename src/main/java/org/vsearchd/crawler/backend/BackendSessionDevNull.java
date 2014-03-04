package org.vsearchd.crawler.backend;

public class BackendSessionDevNull implements IBackendSession {

	public BackendSessionDevNull(BackendServer srv) {

	}

	public synchronized void connect() {
	}

	public synchronized void disconnect() {
	}

	public synchronized void sendMessage(String msg) {
	}

}
