package org.vsearchd.crawler.outmessage;

import org.vsearchd.crawler.helper.IResetHandler;

public class OutputField implements IResetHandler {

	private String name = null;
	private String type = null;
	private String value = null;
	private boolean isUnique = false;
	private boolean update = false;

	public OutputField() {

	}

	public OutputField(String nam, String typ, String val, boolean uni,
			boolean upd) {
		this.name = nam;
		this.type = typ;
		this.value = val;
		this.isUnique = uni;
		this.update = upd;
	}

	public void setUpdate(boolean upd) {
		this.update = upd;
	}

	public boolean getUpdate() {
		return this.update;
	}

	public void setUnique(String uni) {
		if (uni.equalsIgnoreCase("true"))
			this.isUnique = true;
	}

	public boolean getUnique() {
		return this.isUnique;
	}

	public void setName(String nam) {
		this.name = nam;
	}

	public void setType(String typ) {
		this.type = typ;
	}

	public void setValue(String val) {
		this.value = val;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public String getValue() {
		return this.value;
	}

	public void Reset() {
		this.name = null;
		this.type = null;
		this.value = null;
		this.isUnique = false;
		this.update = false;
	}
}
