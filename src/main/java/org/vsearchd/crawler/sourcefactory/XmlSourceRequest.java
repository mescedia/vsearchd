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