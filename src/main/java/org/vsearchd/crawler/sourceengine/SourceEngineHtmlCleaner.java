package org.vsearchd.crawler.sourceengine;

import java.io.IOException;
import java.io.InputStream;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CompactXmlSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceEngineHtmlCleaner extends SourceEngineBase {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private HtmlCleaner cleaner = new HtmlCleaner();
	private TagNode tNode = null;
	private CompactXmlSerializer serializer = null;
	private String encoding = null;
	private String returnXml = "";

	public SourceEngineHtmlCleaner() {

		CleanerProperties props = cleaner.getProperties();

		props.setOmitComments(true);
		props.setAllowHtmlInsideAttributes(false);
		props.setOmitDoctypeDeclaration(true);
		props.setOmitUnknownTags(false);
		props.setOmitHtmlEnvelope(false);
		props.setOmitXmlDeclaration(true);

		props.setRecognizeUnicodeChars(true);

		props.setTreatUnknownTagsAsContent(false);
		props.setUseCdataForScriptAndStyle(true);
		props.setUseEmptyElementTags(false);

		serializer = new CompactXmlSerializer(props);
	}

	@Override
	public String getSource(InputStream inputStream) {
		returnXml = "";
		encoding = this.getSourceEncoding();

		try {

			tNode = cleaner.clean(inputStream, encoding);
			returnXml = serializer.getAsString(tNode, true);

		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return "<?xml  version=\"1.0\" encoding=\"" + encoding + "\"?> <html>"
				+ returnXml + "</html>";
	}

	@Override
	public void Reset() {
		if (tNode != null) {
			tNode.removeAllChildren();
			tNode.removeFromTree();
		}
		super.Reset();
	}
}
