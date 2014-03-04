package org.vsearchd.crawler.backend;

public abstract class BackendSessionBase {

	private BackendServer backendServer = null;

	public BackendSessionBase(BackendServer srv) {
		this.backendServer = srv;
	}

	public BackendServer getBackendServer() {
		return this.backendServer;
	}

}