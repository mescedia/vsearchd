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

package org.vsearchd.crawler.client;

import java.util.ArrayList;
import java.util.Hashtable;

import org.vsearchd.crawler.backend.BackendServer;

public class ClientRequest {

	private String authKey = null;
	private String user = null;
	private String project = null;
	private int projectDelay = 0;

	private ArrayList<BackendServer> backendServer = new ArrayList<BackendServer>();

	private String serverRequestFile = null;
	private String outputFileName = null;

	private String type = null;

	private Hashtable<String, String> httpParameter = new Hashtable<String, String>();
	private Hashtable<String, String> xsltParameter = new Hashtable<String, String>();

	public ClientRequest() {

	}

	public void setOutputFilename(String fn) {
		this.outputFileName = fn;
	}

	public String getOutputFilename() {
		return this.outputFileName;
	}

	public void setAuthKey(String a) {
		this.authKey = a;
	}

	public String getAuthKey() {
		return this.authKey;
	}

	public void setServerRequestFile(String sr) {
		this.serverRequestFile = sr;
	}

	public String getServerRequestFile() {
		return this.serverRequestFile;
	}

	public void setRequestType(String t) {
		this.type = t;
	}

	public String getRequestType() {
		return this.type;
	}

	public void setProjectDelay(int d) {
		this.projectDelay = d;
	}

	public int getProjectDelay() {
		return this.projectDelay;
	}

	public void addBackendServer(BackendServer srv) {
		this.backendServer.add(srv);
	}

	public ArrayList<BackendServer> getBackendServer() {
		return this.backendServer;
	}

	public void setUser(String uname) {
		this.user = uname;
	}

	public String getUser() {
		return this.user;
	}

	public void setProject(String id) {
		this.project = id;
	}

	public String getProject() {
		return this.project;
	}

	public void addHttpParameter(String _name, String _value) {
		this.httpParameter.put(_name, _value);
	}

	public void addXsltParameter(String _name, String _value) {
		this.xsltParameter.put(_name, _value);
	}

	public Hashtable<String, String> getHttpParameter() {
		return this.httpParameter;
	}

	public Hashtable<String, String> getXsltParameter() {
		return this.xsltParameter;
	}
}