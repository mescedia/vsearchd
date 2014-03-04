package org.vsearchd.crawler.controller;

/*
 * @author Michael Kassnel
 *
 */
public interface IServerDocumentPool {

	public Boolean containsMessage();

	public void clear();

	public String getMessage();

	public long size();

	public void addMessage(String msg);

	public void deleteQueue();

	public void removeQueue();

}
