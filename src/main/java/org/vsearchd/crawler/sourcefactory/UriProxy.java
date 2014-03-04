package org.vsearchd.crawler.sourcefactory;

import java.io.Serializable;

import org.vsearchd.crawler.helper.IResetHandler;

public class UriProxy implements IResetHandler, Serializable {
	
	private static final long serialVersionUID = 1L;
	private String _host = null;
	private int _port = 0;

	public UriProxy() {

	}

	public String get_host() {
		return _host;
	}

	public void set_host(String _host) {
		this._host = _host;
	}

	public int get_port() {
		return _port;
	}

	public void set_port(int _port) {
		this._port = _port;
	}

	public synchronized void Reset() {
		this._host = null;
		this._port = 0;
	}

}
