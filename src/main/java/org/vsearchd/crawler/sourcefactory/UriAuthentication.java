package org.vsearchd.crawler.sourcefactory;

import java.io.Serializable;

import org.vsearchd.crawler.helper.IResetHandler;

public class UriAuthentication implements IResetHandler, Serializable {

	private static final long serialVersionUID = 1L;
	private String _uName = null;
	private String _passwd = null;
	private UriAuthType _authType = UriAuthType.BASIC;

	public UriAuthentication() {

	}

	public String get_uName() {
		return _uName;
	}

	public void set_uName(String _uName) {
		this._uName = _uName;
	}

	public String get_passwd() {
		return _passwd;
	}

	public void set_passwd(String _passwd) {
		this._passwd = _passwd;
	}

	public UriAuthType get_authType() {
		return _authType;
	}

	public void set_authType(UriAuthType _authType) {
		this._authType = _authType;
	}

	public synchronized void Reset() {
		this._uName = null;
		this._passwd = null;
		this._authType = UriAuthType.BASIC;
	}
}