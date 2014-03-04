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
