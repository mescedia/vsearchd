package org.vsearchd.crawler.sourceengine;

import java.io.IOException;
import java.io.InputStream;

import com.sun.syndication.io.impl.Base64;

public class SourceEngineBinary extends SourceEngineBase {

	public SourceEngineBinary() {
	}

	@Override
	public String getSource(InputStream inputStream) throws IOException {

		return "<?xml version=\"1.0\" encoding=\""
				+ this.getSourceEncoding()
				+ "\"?><html><head><title></title></head><body>"
				+ "<![CDATA["
				+ new String(Base64.encode(org.apache.tika.io.IOUtils
						.toByteArray(inputStream))) + "]]>" + "</body></html>";
	}

	@Override
	public void Reset() {
		super.Reset();
	}

}