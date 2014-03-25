package org.vsearchd.crawler.sourceengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;
import org.json.XML;

public class SourceEngineJson extends SourceEngineBase {
	
	private String line = null;
	private String jsonStr = null;
	private StringBuilder stringBuilder = new StringBuilder();
	private BufferedReader bufferedReader = null;

	public SourceEngineJson() {

	}

	@Override
	public String getSource(InputStream inputStream) throws IOException {
		line = null;

		bufferedReader = new BufferedReader(new InputStreamReader(inputStream, this.getSourceEncoding()));
		
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		bufferedReader.close();
		jsonStr = stringBuilder.toString();
		stringBuilder.setLength(0);
		
		JSONObject json = new JSONObject(jsonStr);
		return "<?xml  version=\"1.0\" encoding=\"" + this.getSourceEncoding() + "\"?> " + XML.toString(json) ;	
	}

	@Override
	public void Reset() {
		super.Reset();
	}

}
