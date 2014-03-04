/* 	  CLIENT REQUEST (sampels) :

 <?xml version="1.0" encoding="UTF-8" ?>
 <request type="run_project">
 <auth-key value="mickas-develop-0123456789" />
 <user value="mickas" />                 
 <project name="www.heise.de" delay="300" />               

 <jms-server connection-string="failover:tcp://localhost:61616" queue="develop-delay-2" user="jmsUser" passwd="jmsPasswd" />
 <!-- jdbc jdbc:mysql://192.168.178.32:3306/MeineDb?useUnicode=true&characterEncoding=UTF-8 -->
 <jdbc-server connection-string="jdbc:mysql://127.0.0.1:3306/testmk?useUnicode=true&characterEncoding=UTF-8&characterSetResults=UTF-8" 
 driver="com.mysql.jdbc.Driver" user="lisa" passwd="passwd" />

 <!-- http|https -->
 <http-server  host="127.0.0.1" port="80" filepath="/file/input.do"  method="post" />
 <https-server  host="127.0.0.1" port="443" filepath="/file/input.do"  method="post" />

 <http-parameter name="name1" value="value1" />
 <http-parameter name="name2" value="value2" />
 <xslt-parameter name="name1" value="value1" />
 <xslt-parameter name="name2" value="value2" />
 </request>

 <?xml version="1.0" encoding="UTF-8" ?>
 <request type="set_source">
 <auth-key value="mickas-develop-0123456789" />
 <user value="develop" /> <!-- part of path -->     
 <command value="set_source" />      

 <options server-request="/ccraw/projects/develop/localhost_test_1/index_request.xml" 		
 outfile="wwwlocalhosttest1" />

 <project name="localhost_test_1" delay="100" />  

 <http-parameter name="name1" value="value1" />
 <http-parameter name="name2" value="value2" />
 <xslt-parameter name="name1" value="value1" />
 <xslt-parameter name="name2" value="value2" />
 </request>
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