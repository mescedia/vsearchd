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

package org.vsearchd.crawler.sourcefactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.sourceengine.ISourceEngine;
import org.mozilla.universalchardet.UniversalDetector;

public class HttpClient {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	public String url = null;
	public UriRequest uriRequest = null;	
	private DefaultHttpClient httpclient = null;
	private HttpParams params = new BasicHttpParams();
	private Header[] headers = null;
	private String contentType = null;
	private UriProxy uriProxy = null;
	private HttpGet httpget = new HttpGet();
	private HttpResponse response = null;
	private HttpEntity entity = null;
	private Header ceHeader = null;
	private HttpPost httppost = new HttpPost();
	private UrlEncodedFormEntity formEntity = null;
	private String returnXml = null;
	private String _returnXml = null;
	private String aName = null;
	private String aValue = null;
	private String gPara = null;
	private HeaderElement[] codecs = null;
	private Enumeration<String> e;
	private ArrayList<NameValuePair> para = new ArrayList<NameValuePair>();

	private InputStream instr = null;
	private InputStream instrEncoding = null;

	private int httpID = 0;
	private Pattern pattern = Pattern.compile("<([a-zA-Z]+)>");
	private Matcher tagmatch = null;
	private ISourceEngine currentEngine = null;

	private boolean releaseException = false;
	private int statusCode = 0;

	public HttpClient(int _id) {
		this.httpID = _id;
		this.initHttpClient();
	}

	private void initHttpClient() {

		this.httpclient = new DefaultHttpClient();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUseExpectContinue(params, false);
		HttpConnectionParams.setStaleCheckingEnabled(params, false);

		HttpConnectionParams.setConnectionTimeout(params, 30000);
		HttpConnectionParams.setSoTimeout(params, 30000);
		httpclient.setParams(params);
		
		httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,CookiePolicy.IGNORE_COOKIES);
		
		DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				if (executionCount >= 3) {
					log.error("skip request due to previous error - alread tried 3 times ...");
					return false;
				} else {
					if (exception instanceof UnknownHostException) {
						log.error("skip request due to unknown host ...");
						return false;
					}
					if (exception instanceof SSLException) {
						log.error("skip request due to SSL error ...");
						return false;
					}
				}
				HttpRequest request = (HttpRequest) context
						.getAttribute(ExecutionContext.HTTP_REQUEST);
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					return true;
				}
				return false;
			}
		};

		httpclient.setHttpRequestRetryHandler(retryHandler);
	}

	public void setRequest(UriRequest _r) {
		this.uriRequest = _r;
	}

	public void Reset() {

		statusCode = 0;
		url = null;
		uriRequest = null;
		headers = null;
		contentType = null;
		uriProxy = null;
		returnXml = null;
		this.currentEngine = null;
		para.clear();
		
		this.httpclient.getParams().removeParameter(
				ConnRoutePNames.DEFAULT_PROXY);
				
		httpclient.getCookieStore().clear();
		
		httpclient.getConnectionManager().closeExpiredConnections();		
		httpclient.getConnectionManager().closeIdleConnections(60,TimeUnit.SECONDS);
		
	}

	public HttpClient(UriRequest _uriRequest) {
		this.uriRequest = _uriRequest;
	}

	public String GetSource() throws Exception {

		if (this.uriRequest.get_uriMethod() == UriMethod.GET) {
			return this.buildGetRequest();
		} else if (this.uriRequest.get_uriMethod() == UriMethod.POST) {
			return this.buildPostRequest();
		} else {
			log.error("this Method is not implemented ... :( "
					+ this.uriRequest.get_uriMethod());
		}
		return null;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	protected TrustManager[] getTrustManager() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@SuppressWarnings("unused")
			public boolean isServerTrusted(
					java.security.cert.X509Certificate[] certs) {
				return true;
			}

			@SuppressWarnings("unused")
			public boolean isClientTrusted(
					java.security.cert.X509Certificate[] certs) {
				return true;
			}

			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		return trustAllCerts;
	}

	private String buildPostRequest() {

		try {
			url = this.uriRequest.get_host() + ":"
					+ String.valueOf(this.uriRequest.get_port())
					+ this.uriRequest.get_path();

			if (this.uriRequest.get_uriProtocol().equals(UriProtocol.HTTP)) {
				url = "http://" + url;
			} else if (this.uriRequest.get_uriProtocol().equals(
					UriProtocol.HTTPS)) {
				url = "https://" + url;
				this.httpclient.getConnectionManager().getSchemeRegistry()
						.register(this.getHttpSslTheme(url));
			} else {
				log.error("cannot handle this uriprotocol ...");
				return "";
			}

			uriProxy = this.uriRequest.get_proxy();
			if (uriProxy.get_host() != null) {
				this.setHttpProxy(uriProxy);
			}

			log.info("POST ["
					+ String.valueOf(this.httpID)
					+ "-"
					+ String.valueOf(this.uriRequest.getCurrnetClientID())
					+ "-"
					+ String.valueOf(this.uriRequest.getCurrentNumClients())
					+ "-"
					+ String.valueOf(this.uriRequest
							.getCurrentHttpRequestDelay())
					+ "-"
					+ String.valueOf(this.uriRequest
							.getCurrentClientMsgPoolSize()) + "] " + url);

			para.clear();
			e = this.uriRequest.get_uriParameter().keys();
			while (e.hasMoreElements()) {
				aName = e.nextElement();
				para.add(new BasicNameValuePair(aName, this.uriRequest
						.get_uriParameter().get(aName)));
			}

			httppost.setParams(this.params);

			formEntity = new UrlEncodedFormEntity(para);// , "UTF-8");
			httppost.setURI(new URI(url));
			e = this.uriRequest.get_uriHeader().keys();
			while (e.hasMoreElements()) {
				aName = e.nextElement();
				httppost.removeHeaders(aName);
				httppost.addHeader(aName,
						this.uriRequest.get_uriHeader().get(aName));
			}

			httppost.setEntity(formEntity);
			httppost.setParams(this.params);

			response = httpclient.execute(httppost);

			this.statusCode = response.getStatusLine().getStatusCode();

			entity = response.getEntity();
			ceHeader = entity.getContentEncoding();
			if (ceHeader != null) {
				codecs = ceHeader.getElements();
				for (int i = 0; i < codecs.length; i++) {
					if (codecs[i].getName().equalsIgnoreCase("gzip")) {
						response.setEntity(new GzipDecompressingEntity(entity));
						entity = response.getEntity();
					}
				}
			}

			this.headers = response.getAllHeaders();
			this.setContentType();
			log.debug("received content-type: " + this.contentType);

			instr = entity.getContent();

			if (this.contentType == null) {
				log.warn("no content-type given - treat as 'text/html' ...");
				this.contentType = "text/html";
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while ((len = instr.read(buf)) > -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			instr.close();

			instr = new ByteArrayInputStream(baos.toByteArray());
			instrEncoding = new ByteArrayInputStream(baos.toByteArray());

			returnXml = this.getContentTypeSource(instr, instrEncoding);
		} catch (Exception ex) {
			log.error("error in buildPostRequest: " + ex.getMessage());
			returnXml = "";
		}

		this.releaseConnection();
		return returnXml;
	}

	private String buildGetRequest() {

		try {

			url = this.uriRequest.get_host() + ":"
					+ String.valueOf(this.uriRequest.get_port())
					+ this.uriRequest.get_path();

			if (this.uriRequest.get_uriProtocol().equals(UriProtocol.HTTP)) {
				url = "http://" + url;
			} else if (this.uriRequest.get_uriProtocol().equals(
					UriProtocol.HTTPS)) {
				url = "https://" + url;
				httpclient.getConnectionManager().getSchemeRegistry()
						.register(this.getHttpSslTheme(url));
			} else {
				return "";
			}

			uriProxy = this.uriRequest.get_proxy();
			if (uriProxy.get_host() != null) {
				this.setHttpProxy(uriProxy);
			}

			gPara = "";
			e = this.uriRequest.get_uriParameter().keys();
			while (e.hasMoreElements()) {
				aName = e.nextElement();
				aValue = this.uriRequest.get_uriParameter().get(aName);
				if (gPara == "")
					gPara = "?" + aName + "=" + aValue;
				else
					gPara += "&" + aName + "=" + aValue;
			}

			log.info("GET ["
					+ String.valueOf(this.httpID)
					+ "-"
					+ String.valueOf(this.uriRequest.getCurrnetClientID())
					+ "-"
					+ String.valueOf(this.uriRequest.getCurrentNumClients())
					+ "-"
					+ String.valueOf(this.uriRequest
							.getCurrentHttpRequestDelay())
					+ "-"
					+ String.valueOf(this.uriRequest
							.getCurrentClientMsgPoolSize()) + "] " + url
					+ gPara);

			httpget.setURI(new URI(url + gPara));

			// timeout
			httpget.setParams(this.params);

			e = this.uriRequest.get_uriHeader().keys();
			while (e.hasMoreElements()) {
				aName = e.nextElement();
				httpget.removeHeaders(aName);
				httpget.addHeader(aName,
						this.uriRequest.get_uriHeader().get(aName));
			}

			response = httpclient.execute(httpget);
			this.statusCode = response.getStatusLine().getStatusCode();
			log.debug(String.valueOf(statusCode));

			entity = response.getEntity();
			ceHeader = entity.getContentEncoding();
			if (ceHeader != null) {
				codecs = ceHeader.getElements();
				for (int i = 0; i < codecs.length; i++) {
					if (codecs[i].getName().equalsIgnoreCase("gzip")) {
						response.setEntity(new GzipDecompressingEntity(entity));
						entity = response.getEntity();
					}
				}
			}

			this.headers = response.getAllHeaders();
			this.setContentType();

			instr = entity.getContent();
			if (this.contentType == null) {
				log.warn("no content-type given - treat as 'text/html' ...");
				this.contentType = "text/html";
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = instr.read(buf)) > -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			instr.close();

			instr = new ByteArrayInputStream(baos.toByteArray());
			instrEncoding = new ByteArrayInputStream(baos.toByteArray());

			returnXml = this.getContentTypeSource(instr, instrEncoding);
		} catch (Exception ex) {
			log.error("error in buildGetRequest: " + ex.getMessage());
			returnXml = "";
		}

		this.releaseConnection();
		return returnXml;
	}

	public void releaseConnection() {
		releaseException = false;

		if (response == null)
			log.error("response is null -> httpID:"
					+ String.valueOf(this.httpID) + "...");
		else {
			try {
				EntityUtils.consume(response.getEntity());
			} catch (IOException e) {
				releaseException = true;
				log.error("could not release response-entity -> httpID:"
						+ String.valueOf(this.httpID) + "...");
				log.error(e.getMessage());
			}
		}

		if (entity == null)
			log.error("entity is null -> httpID:" + String.valueOf(this.httpID)
					+ "...");
		else {
			try {
				EntityUtils.consume(entity);
			} catch (IOException e) {
				releaseException = true;
				log.error("could not release entitiy -> httpID:"
						+ String.valueOf(this.httpID) + "...");
				log.error(e.getMessage());
			}
		}

		try {
			instr.close();
		} catch (IOException e) {
			releaseException = true;
			log.error("could not release input stream -> httpID:"
					+ String.valueOf(this.httpID) + "...");
			log.error(e.getMessage());
		}

		if (releaseException) {
			try {
				this.reinitHttpClient();
			} catch (Exception ef) {
				log.error("could not reinit httpclient -> httpID:"
						+ String.valueOf(this.httpID) + "...");
				log.error(ef.getMessage());
			}
		}
	}

	/**/
	private void reinitHttpClient() {
		log.warn("reinit http client #" + String.valueOf(this.httpID));
		httpclient.getConnectionManager().shutdown();
		this.initHttpClient();
	}

	private String encoding = null;
	byte[] buf = new byte[1024];
	UniversalDetector detector = new UniversalDetector(null);
	int nread = 0, len = 0;

	private String getContentTypeSource(InputStream instr,
			InputStream instrEncoding) {
		encoding = null;
		_returnXml = "";

		this.setCurrentSourceEngine();
		if (this.currentEngine == null) {
			log.warn("using default (tika) engine ...");
			this.currentEngine = this.uriRequest.getTikaSourceEngine();
		}

		try {
			while ((nread = instrEncoding.read(buf)) > 0 && !detector.isDone()) {
				detector.handleData(buf, 0, nread);
			}
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}

		detector.dataEnd();
		encoding = detector.getDetectedCharset();
		detector.reset();

		try {
			instrEncoding.close();
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}

		if (encoding == null)
			encoding = "UTF-8";

		this.currentEngine.setSourceEncoding(encoding);

		log.debug("use engine: " + this.currentEngine.getName()
				+ " for content type: " + this.contentType);

		try {
			_returnXml = this.currentEngine.getSource(instr);
			_returnXml = this.currentEngine.handleRemoveContents(_returnXml);
			_returnXml = this.currentEngine.handleRegexReplace(_returnXml);
			_returnXml = this.addHttpHeader(_returnXml,
					this.getFirstTag(_returnXml));
			_returnXml = this.prepareReturnValue(_returnXml);
		} catch (IOException e) {
			log.error("source engine converter error: " + e.getMessage());
			_returnXml = "";
		}
		return _returnXml;
	}

	private void setCurrentSourceEngine() {

		if (this.uriRequest.getMandatorySourceEngine() != null) {
			if (this.uriRequest.getMandatorySourceEngine().equalsIgnoreCase("none"))
				this.currentEngine = this.uriRequest.getNoneSourceEngine();
			else if (this.uriRequest.getMandatorySourceEngine().equalsIgnoreCase("devnull"))
				this.currentEngine = this.uriRequest.getDevNullSourceEngine();
			else if (this.uriRequest.getMandatorySourceEngine().equalsIgnoreCase("tidy"))
				this.currentEngine = this.uriRequest.getTidySourceEngine();
			else if (this.uriRequest.getMandatorySourceEngine().equalsIgnoreCase("htmlcleaner"))
				this.currentEngine = this.uriRequest.getHtmlCleanerSourceEngine();
			else if (this.uriRequest.getMandatorySourceEngine().equalsIgnoreCase("tika"))
				this.currentEngine = this.uriRequest.getTikaSourceEngine();
			else if (this.uriRequest.getMandatorySourceEngine().equalsIgnoreCase("binary"))				
				this.currentEngine = this.uriRequest.getBinarySourceEngine();
			else if (this.uriRequest.getMandatorySourceEngine().equalsIgnoreCase("robots"))				
				this.currentEngine = this.uriRequest.getRobotsSourceEngine();
			else if (this.uriRequest.getMandatorySourceEngine().equalsIgnoreCase("json"))				
				this.currentEngine = this.uriRequest.getJsonSourceEngine();
			else {
				log.warn("unknown mandatory-source-engine:"
						+ this.uriRequest.getMandatorySourceEngine());
			}
			return;
		}

		for (String cntTyp : this.uriRequest.getTidySourceEngine()
				.getContentTypes()) {
			if (this.contentType.indexOf(cntTyp.trim()) > -1) {
				this.currentEngine = this.uriRequest.getTidySourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getDevNullSourceEngine()
				.getContentTypes()) {
			if (this.contentType.indexOf(cntTyp.trim()) > -1) {
				this.currentEngine = this.uriRequest.getDevNullSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getHtmlCleanerSourceEngine()
				.getContentTypes()) {
			if (this.contentType.indexOf(cntTyp.trim()) > -1) {
				this.currentEngine = this.uriRequest
						.getHtmlCleanerSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getTikaSourceEngine()
				.getContentTypes()) {
			if (this.contentType.indexOf(cntTyp.trim()) > -1) {
				this.currentEngine = this.uriRequest.getTikaSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getNoneSourceEngine()
				.getContentTypes()) {
			if (this.contentType.indexOf(cntTyp.trim()) > -1) {
				this.currentEngine = this.uriRequest.getNoneSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getBinarySourceEngine()
				.getContentTypes()) {
			if (this.contentType.indexOf(cntTyp.trim()) > -1) {
				this.currentEngine = this.uriRequest.getBinarySourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getRobotsSourceEngine()
				.getContentTypes()) {
			if (this.contentType.indexOf(cntTyp.trim()) > -1) {
				this.currentEngine = this.uriRequest.getRobotsSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getJsonSourceEngine()
				.getContentTypes()) {
			if (this.contentType.indexOf(cntTyp.trim()) > -1) {
				this.currentEngine = this.uriRequest.getJsonSourceEngine();
				return;
			}
		}
		
		

		for (String cntTyp : this.uriRequest.getTidySourceEngine()
				.getContentTypes()) {
			if (cntTyp.equalsIgnoreCase("*")) {
				this.currentEngine = this.uriRequest.getTidySourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getHtmlCleanerSourceEngine()
				.getContentTypes()) {
			if (cntTyp.equalsIgnoreCase("*")) {
				this.currentEngine = this.uriRequest
						.getHtmlCleanerSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getTikaSourceEngine()
				.getContentTypes()) {
			if (cntTyp.equalsIgnoreCase("*")) {
				this.currentEngine = this.uriRequest.getTikaSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getDevNullSourceEngine()
				.getContentTypes()) {
			if (cntTyp.equalsIgnoreCase("*")) {
				this.currentEngine = this.uriRequest.getDevNullSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getNoneSourceEngine()
				.getContentTypes()) {
			if (cntTyp.equalsIgnoreCase("*")) {
				this.currentEngine = this.uriRequest.getNoneSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getBinarySourceEngine()
				.getContentTypes()) {
			if (cntTyp.equalsIgnoreCase("*")) {
				this.currentEngine = this.uriRequest.getBinarySourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getRobotsSourceEngine()
				.getContentTypes()) {
			if (cntTyp.equalsIgnoreCase("*")) {
				this.currentEngine = this.uriRequest.getRobotsSourceEngine();
				return;
			}
		}
		for (String cntTyp : this.uriRequest.getJsonSourceEngine()
				.getContentTypes()) {
			if (cntTyp.equalsIgnoreCase("*")) {
				this.currentEngine = this.uriRequest.getJsonSourceEngine();
				return;
			}
		}
	}

	private Scheme getHttpSslTheme(String url) throws Exception {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, getTrustManager(), null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		SSLSocketFactory socketFactory = new SSLSocketFactory(sc,
				SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return new Scheme("https", Integer.valueOf(this.uriRequest.get_port()),
				socketFactory);
	}

	private String getFirstTag(String src) {
		tagmatch = pattern.matcher(src);
		tagmatch.find();
		return tagmatch.group().replaceFirst("<", "").replaceFirst(">", "");
	}

	private void setContentType() {
		for (Header h : this.headers) {
			if (h.getName().indexOf("Content-Type") > -1) {
				this.contentType = h.getValue();
				break;
			}
		}
	}

	private void setHttpProxy(UriProxy uriProxy) {

		this.httpclient.getParams().removeParameter(
				ConnRoutePNames.DEFAULT_PROXY);
		this.httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
				new HttpHost(uriProxy.get_host(), uriProxy.get_port()));
	}

	private String addHttpHeader(String src, String firstTag) {
		if (this.uriRequest.isShowHttpHeader()) {
			StringBuffer buffer = new StringBuffer(src);
			int startCut = buffer.indexOf("<" + firstTag);
			if (startCut != -1 && buffer.indexOf("</" + firstTag) != -1) {
				String hdr = "<http>";
				for (Header h : this.headers) {
					hdr += "<header name=\""
							+ h.getName()
							+ "\" value=\""
							+ h.getValue().replaceAll("\"", "")
									.replaceAll("&amp;", "&")
									.replaceAll("\\&", "&amp;") + "\" />";
				}
				hdr += "</http>";
				buffer.replace(startCut, ("<" + firstTag).length() + startCut,
						"<root>" + hdr + "<" + firstTag);
				startCut = buffer.indexOf("</" + firstTag + ">");
				buffer.replace(startCut, ("</" + firstTag + ">").length()
						+ startCut, "</" + firstTag + "></root>");
				src = buffer.toString();
			}
		}
		return src;
	}

	private String prepareReturnValue(String retVal) {
		retVal = retVal.replaceAll("\\n", " ");
		retVal = retVal.replaceAll("\\r", " ");
		retVal = retVal.replaceAll("\\t", " ");
		return retVal;
	}

	public int getClientID() {
		return this.httpID;
	}
}