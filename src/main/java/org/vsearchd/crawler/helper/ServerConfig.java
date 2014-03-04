package org.vsearchd.crawler.helper;

public class ServerConfig {

	private static ServerConfig instance = null;

	private int tcpPort = 0;
	private int numHttpClients = 0;
	private int maxClients = 0;
	private String ipv4Addr = null;
	private int maxRequestsPerSecond = 10;
	private int numberOutputMessages = 10;

	private ServerConfig() {

	}

	public void setNumberOutputMessages(int num) {
		this.numberOutputMessages = num;
	}

	public int getNumberOutputMessages() {
		return this.numberOutputMessages;
	}

	public static ServerConfig getInstance() {
		if (instance == null)
			instance = new ServerConfig();
		return instance;
	}

	public void setMaxRequestsPerSecond(int max) {
		this.maxRequestsPerSecond = max;
	}

	public int getMaxRequestsPerSecond() {
		return this.maxRequestsPerSecond;
	}

	public void setIPV4Address(String addr) {
		this.ipv4Addr = addr;
	}

	public String getIPV4Address() {
		return this.ipv4Addr;
	}

	public void setTcpPort(int port) {
		this.tcpPort = port;
	}

	public int getTcpPort() {
		return this.tcpPort;
	}

	public void setNumHttpClient(int num) {
		this.numHttpClients = num;
	}

	public int getNumHttpClients() {
		return this.numHttpClients;
	}

	public void setMaxClient(int max) {
		this.maxClients = max;
	}

	public int getMaxClients() {
		return this.maxClients;
	}

}
