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

package org.vsearchd.crawler.backend;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.outmessage.OutputField;
import org.vsearchd.crawler.outmessage.OutputMessage;
import org.vsearchd.crawler.outmessage.OutputMessageFactory;
import org.vsearchd.crawler.outmessage.OutputMessageReader;

public class BackendSessionHTTP extends BackendSessionBase implements
		IBackendSession {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private OutputMessageReader reader = new OutputMessageReader();
	private OutputMessage message = null;
	private HttpClient httpclient = null;
	private HttpPost httppost = null;
	private HttpParams params = new BasicHttpParams();
	private ArrayList<NameValuePair> para = new ArrayList<NameValuePair>();
	private UrlEncodedFormEntity formEntity = null;
	private HttpResponse response = null;
	private InputStream instr = null;
	private HttpEntity entity = null;

	public BackendSessionHTTP(BackendServer srv) {
		super(srv);
	}

	public synchronized void connect() {
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUseExpectContinue(params, false);
		HttpProtocolParams.setUserAgent(params, "vsearchd::backend-worker");
		HttpConnectionParams.setStaleCheckingEnabled(params, false);

		httppost.setParams(params);
	}

	public synchronized void disconnect() {
		httpclient.getConnectionManager().shutdown();
	}

	public synchronized void sendMessage(String msg) {

		try {
			this.reader.Read(msg);
		} catch (Exception e) {
			log.error(e.getMessage());
			return;
		}
		this.message = reader.getOutputMessage();
		this.handleOutputMessage();
		this.message.Reset();

		OutputMessageFactory.getInstance().Reset(this.message.getId());
	}

	private void handleOutputMessage() {

		try {
			para.clear();

			for (OutputField field : this.message.getFields()) {
				para.add(new BasicNameValuePair(field.getName(), URLEncoder
						.encode(field.getValue(), "UTF-8")));
			}

			formEntity = new UrlEncodedFormEntity(para);
			httppost.setURI(new URI("http://"
					+ this.getBackendServer().getHost() + ":"
					+ String.valueOf(this.getBackendServer().getPort()) + "/"
					+ this.getBackendServer().getFilePath()));

			httppost.setEntity(formEntity);
			response = httpclient.execute(httppost);

			entity = response.getEntity();
			instr = entity.getContent();

			while (instr.read() > -1)
				;
			instr.close();

		} catch (ClientProtocolException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}
	}
}
