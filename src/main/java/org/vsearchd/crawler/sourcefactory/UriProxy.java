/*
 * vsearchd - a focused web-crawler
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
