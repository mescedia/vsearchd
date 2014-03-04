package org.vsearchd.crawler.sourceengine;
//package com.ccraw.server.uri.request;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.ccraw.server.helper.IResetHandler; 
//
//public class SourceEngine implements IResetHandler {
//	
//	private Logger log = LoggerFactory.getLogger(SourceEngine.class);
//
//	private String name = null ;
//	private ArrayList<String> contentTypes = new ArrayList<String>();
//	private ArrayList<String> removeContent = new ArrayList<String>();
//	private Hashtable<String, String> regexReplace = new Hashtable<String, String>(); 
//	
//	public SourceEngine() {
//		
//	}
//	
//	public synchronized void addRegexReplace(String rex, String replace) {
//		// expects at least one group
//		if(rex.trim().indexOf("(") > -1 && rex.trim().indexOf(")") > -1) {
//			this.regexReplace.put(rex.trim(),replace) ;	
//		}
//		else {
//			log.warn("invalid regex (expected at least one group to be replaced):" + rex.trim()) ;
//		}
//	}
//	
//	public synchronized Hashtable<String, String> getRegexReplace() {
//		return this.regexReplace;
//	}
//	
//	public synchronized void addRemoveContent(String val) {
//		this.removeContent.add(val) ;
// 	}
//	
//	public synchronized ArrayList<String> getRemoveContent() {
//		return this.removeContent ;
//	}
//	private String name = null ;
//	public synchronized String getName() {
//		return this.name;
//	}
//	
//	public synchronized void setName(String _name) {
//		this.name = _name.trim() ;
//	}
//	
//	public synchronized ArrayList<String> getContentTypes() {
//		return this.contentTypes ;
//	}
//	
//	public synchronized void addContentType(String ct) {
//		this.contentTypes.add(ct.trim());
//	}
//
//	@Override
//	public synchronized void Reset() {
//		this.name = null;
//		this.removeContent.clear();
//		this.regexReplace.clear();
//		this.contentTypes.clear();
//	}
// }