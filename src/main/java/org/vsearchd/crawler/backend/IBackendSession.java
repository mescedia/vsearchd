package org.vsearchd.crawler.backend;

public interface IBackendSession {
	void connect();

	void disconnect();

	void sendMessage(String msg);
}
