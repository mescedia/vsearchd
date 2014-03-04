package org.vsearchd.crawler.backend;

public class BackendServer {

	private BackendType type = BackendType.None;

	private String connectionString = null;
	private String queue = null;
	private String user = null;
	private String password = null;
	private String driver = null;
	private int port = 0;
	private String host = null;
	private String filePath = null;

	public BackendServer() {

	}

	public BackendType getBackendType() {
		return this.type;
	}

	public void setBackendType(BackendType bt) {
		this.type = bt;
	}

	public void setConnectionString(String cs) {
		this.connectionString = cs;
	}

	public String getConnectionString() {
		return this.connectionString;
	}

	public void setQueue(String q) {
		this.queue = q;
	}

	public String getQueue() {
		return this.queue;
	}

	public void setUser(String usr) {
		this.user = usr;
	}

	public String getUser() {
		return this.user;
	}

	public void setPassword(String ps) {
		this.password = ps;
	}

	public String getPassword() {
		return this.password;
	}

	public void setDriver(String drv) {
		this.driver = drv;
	}

	public String getDriver() {
		return this.driver;
	}

	public void setPort(int prt) {
		this.port = prt;
	}

	public void setHost(String hst) {
		this.host = hst;
	}

	public void setFilePath(String fp) {
		this.filePath = fp;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public String getFilePath() {
		return this.filePath;
	}
}