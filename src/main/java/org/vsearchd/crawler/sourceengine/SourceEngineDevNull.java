package org.vsearchd.crawler.sourceengine;

import java.io.InputStream;

public class SourceEngineDevNull extends SourceEngineBase {

	public SourceEngineDevNull() {
	}

	@Override
	public String getSource(InputStream inputStream) {
		return "<?xml  version=\"1.0\" encoding=\"" + this.getSourceEncoding()
				+ "\"?><root>ccraw-dev-null-source-engine</root>";
	}

	@Override
	public void Reset() {
		super.Reset();
	}

}
