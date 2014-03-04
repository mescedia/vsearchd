package org.vsearchd.crawler.sourceengine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

public interface ISourceEngine {
	String getSource(InputStream inputStream) throws IOException;

	void setRemoveContent(ArrayList<String> removeContent);

	ArrayList<String> getRemoveContent();

	void setRegexReplace(Hashtable<String, String> replace);

	Hashtable<String, String> getRegexReplace();

	String handleRemoveContents(String source);

	String handleRegexReplace(String source);

	void addContentType(String ct);

	ArrayList<String> getContentTypes();

	void setName(String name);

	String getName();

	void setSourceEncoding(String encoding);

	String getSourceEncoding();

}