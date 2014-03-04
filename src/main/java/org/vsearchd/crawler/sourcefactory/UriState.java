package org.vsearchd.crawler.sourcefactory;

public enum UriState {
	None(0x0000), System(0x0001), UnChanged(0x0002), Changed(0x0004), New(
			0x0008), Deleted(0x0010), Saved(0x0020), Added(0x0040), All(0x00FF);

	private int state = 0;

	private UriState(int c) {
		this.state = c;
	}

	public int GetCode() {
		return this.state;
	}
}
