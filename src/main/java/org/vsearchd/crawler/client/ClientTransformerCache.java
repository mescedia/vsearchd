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

package org.vsearchd.crawler.client;

import java.util.Hashtable;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

public class ClientTransformerCache {

	private Transformer transformer = null;
	private TransformerFactory tFactory = null;
	private Hashtable<String, Transformer> tCache = new Hashtable<String, Transformer>();

	public ClientTransformerCache() {

	}

	public void setTransformerFactory(TransformerFactory _tFactory) {
		this.tFactory = _tFactory;
	}

	public Transformer getTransformer(String filePath)
			throws TransformerConfigurationException {

		if (transformer != null) {
			transformer.clearParameters();
			transformer.reset();
		}

		if (!this.tCache.containsKey(filePath)) {
			transformer = this.tFactory
					.newTemplates(new StreamSource(filePath)).newTransformer();
						
			this.addTransformer(filePath, transformer);
		} else {
			transformer = this.tCache.get(filePath);
		}

		return transformer;
	}

	private void addTransformer(String filePath, Transformer transformer) {
		this.tCache.put(filePath, transformer);
	}
}
