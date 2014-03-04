package org.vsearchd.crawler.backend;

import java.sql.*;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.outmessage.OutputField;
import org.vsearchd.crawler.outmessage.OutputMessage;
import org.vsearchd.crawler.outmessage.OutputMessageAction;
import org.vsearchd.crawler.outmessage.OutputMessageFactory;
import org.vsearchd.crawler.outmessage.OutputMessageReader;

public class BackendSessionJDBC extends BackendSessionBase implements
		IBackendSession {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private Connection connection = null;

	private OutputMessageReader reader = new OutputMessageReader();
	private OutputMessage message = null;
	private String sql = null;
	private String sqlSelect = null;
	private ResultSet result = null;
	private PreparedStatement pStatement = null;
	private int count = 0;
	private String values = null;
	private String where = null;

	public BackendSessionJDBC(BackendServer srv) {
		super(srv);
	}

	public synchronized void connect() {
		try {
			Class.forName(this.getBackendServer().getDriver()).newInstance();
			this.connection = DriverManager.getConnection(this
					.getBackendServer().getConnectionString(), this
					.getBackendServer().getUser(), this.getBackendServer()
					.getPassword());
		} catch (Exception e) {
			log.error("could not connect database: " + e.getMessage());
		}
	}

	public synchronized void disconnect() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			log.error("could not disconnect database: " + e.getMessage());
		}
	}

	public synchronized void sendMessage(String msg) {
		try {
			this.handleMessage(msg);
		} catch (Exception e1) {
			log.error("could not handle message:" + msg);
			log.error(e1.getMessage());
			return;
		}
	}

	private synchronized void handleMessage(String msg) throws Exception {

		this.reader.Read(msg);
		this.message = reader.getOutputMessage();

		this.handleOutputMessage();

		this.message.Reset();

		OutputMessageFactory.getInstance().Reset(this.message.getId());
	}

	private synchronized void handleOutputMessage() {
		if (this.message.getAction().equals(OutputMessageAction.Insert)) {
			this.handleInsert();
		} else if (this.message.getAction().equals(
				OutputMessageAction.CheckInsert)) {
			this.handleCheckInsert();
		} else if (this.message.getAction().equals(
				OutputMessageAction.InsertOrUpdate)) {
			this.handleUpdateOrInsertWithSelect();
		} else if (this.message.getAction().equals(OutputMessageAction.Update)) {
			this.handleUpdate();
		} else if (this.message.getAction().equals(OutputMessageAction.Delete)) {
			this.handleDelete();
		}
	}

	private synchronized void handleInsert() {

		values = " values ( ";
		sql = "insert into " + this.message.getTable() + " ( ";

		for (OutputField field : this.message.getFields()) {
			if (field.getName() != null) {
				sql += field.getName() + ", ";
				values += "?, ";
			}
		}

		sql = sql.substring(0, sql.length() - 2) + ") ";
		values = values.substring(0, values.length() - 2) + ") ;";

		try {
			count = 1;
			this.pStatement = this.connection.prepareStatement(sql + values);
			for (OutputField field : this.message.getFields()) {
				if (field.getName() != null) {
					this.pStatement.setObject(count, field.getValue());
					count++;
				}
			}
			this.pStatement.executeUpdate();
			this.pStatement.close();
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	private void handleUpdate() {

		sql = "update " + this.message.getTable() + " set ";
		where = " where ";
		for (OutputField field : this.message.getFields()) {

			if (field.getName() != null && field.getUpdate()) {
				sql += field.getName() + "= ?, ";
			} else if (field.getName() != null && field.getUnique()) {
				where += field.getName() + "= ? and ";
			}
		}

		sql = sql.substring(0, sql.length() - 2) + " ";
		where = where.substring(0, where.length() - 4) + "; ";

		log.debug("update sql:" + sql + where);

		try {
			count = 1;
			this.pStatement = this.connection.prepareStatement(sql + where);
			for (OutputField field : this.message.getFields()) {
				if (field.getName() != null && field.getUpdate()) {
					this.pStatement.setObject(count, field.getValue());
					count++;
				} else if (field.getName() != null && field.getUnique()) {
					this.pStatement.setObject(count, field.getValue());
					count++;
				}
			}
			this.pStatement.executeUpdate();
			this.pStatement.close();
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	private synchronized void handleCheckInsert() {

		sqlSelect = "select count(*) from " + this.message.getTable()
				+ " where ";

		for (OutputField field : this.message.getFields()) {
			if (field.getName() != null && field.getUnique() == true) {
				sqlSelect += field.getName() + "= ? and ";
			}
		}

		sqlSelect = sqlSelect.substring(0, sqlSelect.length() - 4) + "; ";

		try {

			this.pStatement = this.connection.prepareStatement(sqlSelect);

			count = 1;
			for (OutputField field : this.message.getFields()) {
				if (field.getName() != null && field.getUnique() == true) {
					this.pStatement.setObject(count, field.getValue());
					count++;
				}
			}

			result = this.pStatement.executeQuery();

			log.debug("excuted SQL: " + sqlSelect);
			while (result.next()) {
				if (result.getInt(1) > 0) {
					log.debug("record exists - skip ...");
					this.pStatement.close();
					result.close();
					return;
				}
			}
			this.pStatement.close();
			result.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		this.handleInsert();
	}

	private void handleUpdateOrInsertWithSelect() {
		
		sqlSelect = "select count(*) from " + this.message.getTable()
				+ " where ";

		for (OutputField field : this.message.getFields()) {
			if (field.getName() != null && field.getUnique() == true) {
				sqlSelect += field.getName() + "= ? and ";
			}
		}

		sqlSelect = sqlSelect.substring(0, sqlSelect.length() - 4) + "; ";

		try {

			this.pStatement = this.connection.prepareStatement(sqlSelect);

			count = 1;
			for (OutputField field : this.message.getFields()) {
				if (field.getName() != null && field.getUnique() == true) {
					this.pStatement.setObject(count, field.getValue());
					count++;
				}
			}

			result = this.pStatement.executeQuery();

			log.debug("excuted SQL: " + sqlSelect);
			while (result.next()) {
				if (result.getInt(1) > 0) {
					log.debug("record exists - do update ...");

					this.pStatement.close();
					result.close();
					this.handleUpdate();
					return;
				}
			}
			this.pStatement.close();
			result.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		log.debug("record does not exist - do insert ...");
		this.handleInsert();
	}

	private void handleDelete() {
		throw new NotImplementedException("delete handler not implemented yet ...");
	}

}
