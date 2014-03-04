//former FollowLink
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
