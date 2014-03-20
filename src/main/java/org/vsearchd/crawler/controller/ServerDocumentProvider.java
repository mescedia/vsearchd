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

package org.vsearchd.crawler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerDocumentProvider {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private final int numInst = 20;
	private final int numAddInst = 10;

	private boolean[] inUse = null;
	private ServerDocument[] serverDocuments = null;

	private static ServerDocumentProvider instance = null;

	private ServerDocumentProvider() {
		this.initProvider();
	}

	public static ServerDocumentProvider getInstance() {
		if (instance == null)
			instance = new ServerDocumentProvider();
		return instance;
	}

	private void initProvider() {
		this.serverDocuments = new ServerDocument[numInst];
		this.inUse = new boolean[numInst];
		for (int i = this.serverDocuments.length - 1; i >= 0; i--) {
			this.serverDocuments[i] = new ServerDocument(i);
			this.inUse[i] = false;
		}
	}

	public synchronized ServerDocument getServerDocument() {
		for (int i = inUse.length - 1; i >= 0; i--) {
			if (!inUse[i]) {
				log.debug("return ServerDocument from pool ["
						+ String.valueOf(i) + "] ... ");
				inUse[i] = true;
				return this.serverDocuments[i];
			}
		}

		log.warn("need to create additonal objects ...");

		boolean[] old_inUse = inUse;
		inUse = new boolean[old_inUse.length + this.numAddInst];
		System.arraycopy(old_inUse, 0, inUse, 0, old_inUse.length);

		ServerDocument[] old_serverDocs = this.serverDocuments;
		this.serverDocuments = new ServerDocument[old_serverDocs.length
				+ this.numAddInst];
		System.arraycopy(old_serverDocs, 0, this.serverDocuments, 0,
				old_serverDocs.length);

		for (int i = old_serverDocs.length; i < this.serverDocuments.length; i++) {
			this.serverDocuments[i] = new ServerDocument(i);
			this.inUse[i] = false;
		}

		inUse[this.serverDocuments.length - 1] = true;
		return this.serverDocuments[this.serverDocuments.length - 1];
	}

	public synchronized void resetServerDocument(ServerDocument sDoc) {
		this.serverDocuments[sDoc.getID()].Reset();
		inUse[sDoc.getID()] = false;		
	}
}