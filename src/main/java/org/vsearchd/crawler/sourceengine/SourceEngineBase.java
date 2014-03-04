package org.vsearchd.crawler.sourceengine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.helper.IResetHandler;

public abstract class SourceEngineBase implements ISourceEngine, IResetHandler {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private String name = null;
	private StringBuffer buffer = null;
	private ArrayList<String> removeContent = new ArrayList<String>();
	private Hashtable<String, String> regexReplace = new Hashtable<String, String>();

	private ArrayList<String> contentTypes = new ArrayList<String>();

	public SourceEngineBase() {
	}

	private String sourceEncoding = "UTF-8";

	public synchronized void setSourceEncoding(String enc) {
		this.sourceEncoding = enc;
	}

	public synchronized String getSourceEncoding() {
		return this.sourceEncoding;
	}

	public synchronized String getName() {
		return this.name;
	}

	public synchronized void setName(String _name) {
		this.name = _name.trim();
	}

	public synchronized String getSource(InputStream inputStream)
			throws IOException {
		return null;
	}

	public synchronized Hashtable<String, String> getRegexReplace() {
		return this.regexReplace;
	}

	public synchronized ArrayList<String> getRemoveContent() {
		return this.removeContent;
	}

	public synchronized void setRemoveContent(ArrayList<String> remCont) {
		this.removeContent = remCont;
	}

	public synchronized void setRegexReplace(Hashtable<String, String> replace) {
		// make sure that every item must contain one regex group to be replaced
		this.regexReplace = replace;
	}

	public synchronized String handleRemoveContents(String src) {
		for (String searchStr : this.removeContent) {
			buffer = new StringBuffer(src);
			int startCut = buffer.indexOf(searchStr);
			if (startCut != -1) {
				buffer.replace(startCut, startCut + searchStr.length(), "");
				src = buffer.toString();
			}
			buffer.setLength(0);
		}
		return src;
	}

	public synchronized String handleRegexReplace(String src) {
		
		String pattern = "" ;
		String replace = "" ;
				
		try {
		
			Iterator<Map.Entry<String, String>> it = this.regexReplace.entrySet().iterator();
	
			while (it.hasNext()) {
			  Map.Entry<String, String> entry = it.next();
			  
			  pattern = entry.getKey() ;
			  replace = entry.getValue() ;
			  
			  src = src.replaceAll(pattern, replace) ;
			}
		}
		catch (Exception ex)	{		
			log.error("regex rule failed: pattern='" + pattern + "' replace='"+replace+"'" );
			log.error(ex.getMessage());			
		}
		return src;
	}

	public synchronized ArrayList<String> getContentTypes() {
		return this.contentTypes;
	}

	public synchronized void addContentType(String ct) {
		this.contentTypes.add(ct.trim());
	}

	public synchronized void Reset() {
		name = null;
		buffer = null;
		removeContent.clear();
		regexReplace.clear();
		contentTypes.clear();
	}

}
