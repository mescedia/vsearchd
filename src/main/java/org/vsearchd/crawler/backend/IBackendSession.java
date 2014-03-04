package org.vsearchd.crawler.backend;

/*
 * @author Michael Kassnel
 *
 */
public interface IBackendSession {
	void connect();

	void disconnect();

	void sendMessage(String msg);
}
