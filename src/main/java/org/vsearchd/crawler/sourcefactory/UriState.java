/*
 * vsearchd - a vertical search engine (crawler)
 *
 * Copyright (C) 2012-2014  Michael Kassnel 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License (Version 3) as published
 * by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.
 * 
 * See the GNU Lesser General Public License for more details:
 * http://www.gnu.org/licenses/lgpl.txt
 *
 */

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
