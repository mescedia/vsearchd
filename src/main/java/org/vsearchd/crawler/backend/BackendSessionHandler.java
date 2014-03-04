package org.vsearchd.crawler.backend;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.client.ClientRequest;

public class BackendSessionHandler implements IBackendSession {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private ClientRequest clientRequest = null;
	private ArrayList<IBackendSession> backendSession = new ArrayList<IBackendSession>();

	public BackendSessionHandler(ClientRequest req) {
		this.clientRequest = req;
	}

	public synchronized void initSession() {

		for (BackendServer bSrv : this.clientRequest.getBackendServer()) {

			if (bSrv.getBackendType() == BackendType.Jms) {
				backendSession.add(new BackendSessionJMS(bSrv));
				log.info("initialize jms backend session ...");
			} else if (bSrv.getBackendType() == BackendType.Http) {
				backendSession.add(new BackendSessionHTTP(bSrv));
				log.info("initialize http backend session ...");
			} else if (bSrv.getBackendType() == BackendType.Https) {
				backendSession.add(new BackendSessionHTTPS(bSrv));
				log.info("initialize https backend session ...");
			} else if (bSrv.getBackendType() == BackendType.JDBC) {
				backendSession.add(new BackendSessionJDBC(bSrv));
				log.info("initialize jdbc backend session ...");
			} else if (bSrv.getBackendType() == BackendType.DevNull) {
				backendSession.add(new BackendSessionDevNull(bSrv));
				log.info("initialize dev-null backend session ...");
			} else {
				log.warn("backend not defined or does not exist -> using dev-null ...");
				backendSession.add(new BackendSessionDevNull(bSrv));
			}
		}
	}

	public synchronized void connect() {
		for (IBackendSession backend : this.backendSession) {
			if (backend != null)
				backend.connect();
			else
				log.error("could not connect to backend - got no session ... ");
		}
	}

	public synchronized void disconnect() {
		for (IBackendSession backend : this.backendSession) {
			if (backend != null)
				backend.disconnect();
			else
				log.error("could not connect to backend - got no session ... ");
		}
	}

	public synchronized void sendMessage(String msg) {

		if (msg == null) {
			log.debug("empty message received - ignore ...");
			return;
		}

		for (IBackendSession backend : this.backendSession) {
			if (backend != null) {
				backend.sendMessage(msg);
			} else
				log.error("could not send message to backend - got no session ... ");
		}
	}
}
