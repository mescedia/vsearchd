package org.vsearchd.crawler.sourceengine;

import java.io.InputStream;
import java.io.Reader;
import org.apache.tika.io.IOUtils;
import org.apache.tika.parser.ParsingReader;

public class SourceEngineTika extends SourceEngineBase {

	private Reader reader = null;
	private String content = "";

	@Override
	public String getSource(InputStream inputStream) {

		try {
			reader = new ParsingReader(inputStream);
			content = IOUtils.toString(reader);
		} catch (Exception ex) {
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return "<?xml version=\"1.0\" encoding=\"" + this.getSourceEncoding()
				+ "\"?><html><head><title></title></head><body>" + "<![CDATA["
				+ content + "]]>" + "</body></html>";
	}

	@Override
	public void Reset() {
		super.Reset();
		IOUtils.closeQuietly(reader);
		content = "";
	}

}
