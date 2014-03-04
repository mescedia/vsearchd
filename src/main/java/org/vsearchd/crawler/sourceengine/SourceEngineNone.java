package org.vsearchd.crawler.sourceengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SourceEngineNone extends SourceEngineBase {

	private String line = null;
	private String returnSource = null;
	private StringBuilder stringBuilder = new StringBuilder();
	private BufferedReader bufferedReader = null;

	public SourceEngineNone() {

	}

	@Override
	public String getSource(InputStream inputStream) throws IOException {
		line = returnSource = null;

		bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
				this.getSourceEncoding()));

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferedReader.close();
		returnSource = stringBuilder.toString();
		stringBuilder.setLength(0);
		return returnSource;
	}

	@Override
	public void Reset() {
		super.Reset();
	}

}
