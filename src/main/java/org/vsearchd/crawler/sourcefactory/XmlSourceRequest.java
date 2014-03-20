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

import java.util.Enumeration;

import org.vsearchd.crawler.client.ClientRequest;

public class XmlSourceRequest {

	public XmlSourceRequest() {
		
	}

	public static String getXmlSource(UriRequest request, ClientRequest creq)
			throws Exception {
		HttpClient http = HttpClientFactory.getInstance().getHttpClient();

		Enumeration<String> e = creq.getHttpParameter().keys();
		while (e.hasMoreElements()) {
			String n = e.nextElement();
			request.addUriParameter(n, creq.getHttpParameter().get(n));
		}

		http.setRequest(request);
		String xmlSource = http.GetSource();
		http.Reset();
		HttpClientFactory.getInstance().Reset(http.getClientID());
		return xmlSource;
	}
}