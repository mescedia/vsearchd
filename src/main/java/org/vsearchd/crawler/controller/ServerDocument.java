/*
 * vsearchd - a focused crawler
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

package org.vsearchd.crawler.controller;

import java.util.Hashtable;

import org.vsearchd.crawler.helper.IResetHandler;
import org.vsearchd.crawler.sourcefactory.UriRequest;

public class ServerDocument implements IResetHandler {

	private String mapping = null;
	private Hashtable<String, String> xsltParameter = new Hashtable<String, String>();
	public UriRequest uriRequest = new UriRequest();

	private int Id = 0;

	public ServerDocument(int id) {
		this.Id = id;
	}

	public int getID() {
		return this.Id;
	}

	public Hashtable<String, String> get_xsltParameter() {
		return xsltParameter;
	}

	public void addXsltParamter(String name, String value) {
		this.xsltParameter.put(name, value);
	}

	public void setMapping(String map) {
		this.mapping = map;
	}

	public String getMapping() {
		return this.mapping;
	}

	public synchronized void Reset() {
		this.mapping = null;
		this.xsltParameter.clear();
		this.uriRequest.Reset();
	}

}
