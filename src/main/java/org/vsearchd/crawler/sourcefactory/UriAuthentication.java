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