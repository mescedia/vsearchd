/*
 * vsearchd - a focused crawler
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

package org.vsearchd.crawler.outmessage;

import java.util.ArrayList;

import org.vsearchd.crawler.backend.BackendType;
import org.vsearchd.crawler.helper.IResetHandler;

public class OutputMessage implements IResetHandler {

	private String table = null;
	private BackendType backendType = BackendType.Any;
	private OutputMessageAction action = OutputMessageAction.None;
	private ArrayList<OutputField> fields = new ArrayList<OutputField>();
	private int id = 0;

	public OutputMessage(int _id) {
		this.id = _id;
	}

	public int getId() {
		return this.id;
	}

	public String getTable() {
		return this.table;
	}

	public void setTable(String tab) {
		this.table = tab;
	}

	public void setBackendType(BackendType bt) {
		this.backendType = bt;
	}

	public BackendType getBackendType() {
		return this.backendType;
	}

	public void setAction(String a) {
		if (a.equalsIgnoreCase("insert"))
			this.action = OutputMessageAction.Insert;
		else if (a.equalsIgnoreCase("insertorupdate"))
			this.action = OutputMessageAction.InsertOrUpdate;
		else if (a.equalsIgnoreCase("checkinsert")) // unique attribute required
			this.action = OutputMessageAction.CheckInsert;
		else if (a.equalsIgnoreCase("update"))
			this.action = OutputMessageAction.Update;
		else if (a.equalsIgnoreCase("delete"))
			this.action = OutputMessageAction.Delete;
	}

	public OutputMessageAction getAction() {
		return this.action;
	}

	public void addField(String nam, String typ, String val, boolean uni,
			boolean upd) {
		this.fields.add(new OutputField(nam, typ, val, uni, upd));
	}

	public ArrayList<OutputField> getFields() {
		return this.fields;
	}

	public void Reset() {
		this.table = null;
		this.backendType = BackendType.Any;
		this.action = OutputMessageAction.None;
		this.fields.clear();
	}
}
