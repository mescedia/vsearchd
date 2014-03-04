package org.vsearchd.crawler.backend;

import java.util.EventListener;

public interface BackendEventListener extends EventListener {

	public void backendEventOccurred(BackendEvent event);

}
