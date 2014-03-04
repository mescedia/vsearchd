package org.vsearchd.crawler.backend;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
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


public class BackendSessionHTTPS extends BackendSessionBase implements
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
	private String url = null;

	public BackendSessionHTTPS(BackendServer srv) {
		super(srv);

	}

	public synchronized void connect() {
		httpclient = new DefaultHttpClient();
		httppost = new HttpPost();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "UTF-8");
		HttpProtocolParams.setUseExpectContinue(params, false);
		HttpProtocolParams
			.setUserAgent(params, "vsearchd::www.vsearchd.org");
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

		try {
			this.handleOutputMessage();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		this.message.Reset();
		OutputMessageFactory.getInstance().Reset(this.message.getId());
	}

	private void handleOutputMessage() throws Exception {

		try {
			para.clear();

			for (OutputField field : this.message.getFields()) {
				para.add(new BasicNameValuePair(field.getName(), URLEncoder
						.encode(field.getValue(), "UTF-8")));
			}

			url = "https://" + this.getBackendServer().getHost() + ":"
					+ String.valueOf(this.getBackendServer().getPort()) + "/"
					+ this.getBackendServer().getFilePath();
			httpclient.getConnectionManager().getSchemeRegistry()
					.register(this.getHttpSslTheme(url));

			formEntity = new UrlEncodedFormEntity(para);
			httppost.setURI(new URI(url));

			httppost.setEntity(formEntity);
			response = httpclient.execute(httppost);

			entity = response.getEntity();
			log.debug(response.getStatusLine().getReasonPhrase());

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

	private Scheme getHttpSslTheme(String url) throws Exception {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, getTrustManager(), null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		SSLSocketFactory socketFactory = new SSLSocketFactory(sc,
				SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return new Scheme("https", Integer.valueOf(this.getBackendServer()
				.getPort()), socketFactory);
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
}