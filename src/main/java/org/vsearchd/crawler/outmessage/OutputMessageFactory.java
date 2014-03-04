package org.vsearchd.crawler.outmessage;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vsearchd.crawler.helper.ServerConfig;

public class OutputMessageFactory {

	private Logger log = LoggerFactory.getLogger(getClass().getName());

	private static int numMessagesDefault = 30; // default
	private static int numMessages = 0; // default
	private static OutputMessageFactory instance = null;

	private ArrayList<OutputMessage> messageList = null;
	private ArrayList<Boolean> inUseList = null;

	private OutputMessageFactory() {
		numMessages = ServerConfig.getInstance().getNumberOutputMessages();
		if (numMessages == 0) {
			log.warn("number of outputMessages not stated in server-config - use default:"
					+ String.valueOf(numMessagesDefault));
			numMessages = numMessagesDefault;
		}

		this.messageList = new ArrayList<OutputMessage>(numMessages);
		this.inUseList = new ArrayList<Boolean>(numMessages);
		for (int i = 0; i < numMessages; i++) {
			this.messageList.add(new OutputMessage(i));
			this.inUseList.add(false);
		}
	}

	public static OutputMessageFactory getInstance() {
		if (instance == null)
			instance = new OutputMessageFactory();
		return instance;
	}

	private int cnt = 0;

	public synchronized OutputMessage getOutputMessage()
			throws InterruptedException {
		while (true) {
			cnt = 0;
			for (Boolean b : this.inUseList) {
				if (!b) {
					this.inUseList.set(cnt, true);
					return this.messageList.get(cnt);
				}
				cnt++;
			}
			Thread.sleep(50);
		}
	}

	public void Reset(int c) {
		this.inUseList.set(c, false);
	}
}
