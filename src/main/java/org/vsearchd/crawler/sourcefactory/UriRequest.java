/*
 * vsearchd - a vertical search engine (crawler)
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
import java.util.*;

import org.vsearchd.crawler.helper.IResetHandler;
import org.vsearchd.crawler.sourceengine.ISourceEngine;
import org.vsearchd.crawler.sourceengine.SourceEngineBinary;
import org.vsearchd.crawler.sourceengine.SourceEngineDevNull;
import org.vsearchd.crawler.sourceengine.SourceEngineHtmlCleaner;
import org.vsearchd.crawler.sourceengine.SourceEngineNone;
import org.vsearchd.crawler.sourceengine.SourceEngineTidy;
import org.vsearchd.crawler.sourceengine.SourceEngineTika;

public class UriRequest implements IResetHandler, Serializable {

	private static final long serialVersionUID = 1L;
	private String _host = null;
	private int _port = 0;
	private String _path = null;
	private String name = null;
	private UriMethod _uriMethod = UriMethod.NONE;
	private UriProtocol _uriProtocol = UriProtocol.NONE;
	private UriAuthentication _auth = new UriAuthentication();
	private UriProxy proxy = new UriProxy();

	private Hashtable<String, String> _uriHeader = new Hashtable<String, String>();
	private Hashtable<String, String> _uriParameter = new Hashtable<String, String>();
	private int timeout = 30000;
	private int retry = 6;
	private int delay = 6000; 
								
	private String dtd = "strict";
	private boolean showRootTag = true;
	private boolean showHttpHeader = true;
	private int currentHttpRequestDelay = 0; 
	private int currentClientMsgPoolSize = 0;
	private String currentClientID = null;
	private int currentNumClients = 0;

	private String mandatorySourceEngine = null;

	private SourceEngineNone noneSourceEngine = new SourceEngineNone();
	private SourceEngineBinary binarySourceEngine = new SourceEngineBinary();
	private SourceEngineTika tikaSourceEngine = new SourceEngineTika();
	private SourceEngineTidy tidySourceEngine = new SourceEngineTidy();
	private SourceEngineHtmlCleaner htmlCleanerSourceEngine = new SourceEngineHtmlCleaner();
	private SourceEngineDevNull devNullSourceEngine = new SourceEngineDevNull();

	public UriRequest() {

	}

	public ISourceEngine getDevNullSourceEngine() {
		return this.devNullSourceEngine;
	}

	public ISourceEngine getHtmlCleanerSourceEngine() {
		return this.htmlCleanerSourceEngine;
	}

	public ISourceEngine getNoneSourceEngine() {
		return this.noneSourceEngine;
	}

	public ISourceEngine getTidySourceEngine() {
		return this.tidySourceEngine;
	}

	public ISourceEngine getTikaSourceEngine() {
		return this.tikaSourceEngine;
	}

	public ISourceEngine getBinarySourceEngine() {
		return this.binarySourceEngine;
	}

	public void setDevNullSourceEngine(SourceEngineDevNull ie) {
		this.devNullSourceEngine = ie;
	}

	public void setNoneSourceEngine(SourceEngineNone ie) {
		this.noneSourceEngine = ie;
	}

	public void setBinarySourceEngine(SourceEngineBinary ie) {
		this.binarySourceEngine = ie;
	}

	public void setTidySourceEngine(SourceEngineTidy ie) {
		this.tidySourceEngine = ie;
	}

	public void setTikaSourceEngine(SourceEngineTika ie) {
		this.tikaSourceEngine = ie;
	}

	public void setHtmlCleanerSourceEngine(SourceEngineHtmlCleaner ie) {
		this.htmlCleanerSourceEngine = ie;
	}

	public void setMandatorySourceEngine(String mse) {
		this.mandatorySourceEngine = mse;
	}

	public String getMandatorySourceEngine() {
		return this.mandatorySourceEngine;
	}

	public int getCurrentClientMsgPoolSize() {
		return this.currentClientMsgPoolSize;
	}

	public void setCurrentClientMsgPoolSize(int sze) {
		this.currentClientMsgPoolSize = sze;
	}

	public int getCurrentHttpRequestDelay() {
		return this.currentHttpRequestDelay;
	}

	public void setCurrnetHttpRequestDelay(int dly) {
		this.currentHttpRequestDelay = dly;
	}

	public void setCurrentClientID(String cID) {
		this.currentClientID = cID;
	}

	public void setCurrnetNumClients(int numCli) {
		this.currentNumClients = numCli;
	}

	public int getCurrentNumClients() {
		return this.currentNumClients;
	}

	public String getCurrnetClientID() {
		return this.currentClientID;
	}

	public String get_host() {
		return this._host;
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

	public String get_path() {
		return _path;
	}

	public void set_path(String _path) {
		this._path = _path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UriMethod get_uriMethod() {
		return _uriMethod;
	}

	public void set_uriMethod(UriMethod _uriMethod) {
		this._uriMethod = _uriMethod;
	}

	public UriProtocol get_uriProtocol() {
		return _uriProtocol;
	}

	public void set_uriProtocol(UriProtocol _uriProtocol) {
		this._uriProtocol = _uriProtocol;
	}

	public UriAuthentication get_auth() {
		return _auth;
	}

	public void set_auth(UriAuthentication _auth) {
		this._auth = _auth;
	}

	public Hashtable<String, String> get_uriHeader() throws Exception {
		return _uriHeader;
	}

	//
	public Hashtable<String, String> get_uriParameter() throws Exception {
		return _uriParameter;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public String getDtd() {
		return dtd;
	}

	public void setDtd(String dtd) {
		this.dtd = dtd;
	}

	public boolean isShowRootTag() {
		return showRootTag;
	}

	public void setShowRootTag(boolean showRootTag) {
		this.showRootTag = showRootTag;
	}

	public boolean isShowHttpHeader() {
		return showHttpHeader;
	}

	public void setShowHttpHeader(boolean showHttpHeader) {
		this.showHttpHeader = showHttpHeader;
	}

	public void addHeader(String name, String value) {
		this._uriHeader.put(name, value);
	}

	public void addUriParameter(String name, String value) {
		this._uriParameter.put(name, value);
	}

	public UriProxy get_proxy() {
		return this.proxy;
	}

	public void set_proxy(String host, int port) {
		this.proxy.set_host(host);
		this.proxy.set_port(port);
	}

	public synchronized void Reset() {
		this._host = null;
		this._port = 0;
		this._path = null;
		this.name = null;
		this._uriMethod = UriMethod.NONE;
		this._uriProtocol = UriProtocol.NONE;
		this._auth.Reset();
		this.proxy.Reset();

		this._uriHeader.clear();
		this._uriParameter.clear();

		this.tidySourceEngine.Reset();
		this.noneSourceEngine.Reset();
		this.binarySourceEngine.Reset();
		this.tikaSourceEngine.Reset();
		this.htmlCleanerSourceEngine.Reset();
		this.devNullSourceEngine.Reset();

		this.mandatorySourceEngine = null;

		this.timeout = 30000;
		this.retry = 6;
		this.delay = 6000;

		this.dtd = "strict";
		this.showRootTag = true;
		this.showHttpHeader = true;

		this.currentHttpRequestDelay = 0;
		this.currentClientMsgPoolSize = 0;
		this.currentClientID = null;
	}
}
