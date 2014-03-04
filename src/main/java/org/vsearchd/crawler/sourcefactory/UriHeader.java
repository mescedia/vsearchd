package org.vsearchd.crawler.sourcefactory;

import org.vsearchd.crawler.helper.IResetHandler;


public class UriHeader implements IResetHandler {
	private int _id = 0;
	private int _uriid = 0;
	private String _name = null;
	private String _value = null;
	private int order = 0;

	public UriHeader() {

	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int get_uriid() {
		return _uriid;
	}

	public void set_uriid(int _uriid) {
		this._uriid = _uriid;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_value() {
		return _value;
	}

	public void set_value(String _value) {
		this._value = _value;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void Reset() {
		this._id = 0;
		this._uriid = 0;
		this._name = null;
		this._value = null;
		this.order = 0;
	}
}
