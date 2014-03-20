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

package org.vsearchd.crawler.extensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Notifications {

	private static Logger log = LoggerFactory.getLogger(Notifications.class);

	public static void log(String level, String msg) {

		if (level.equalsIgnoreCase("debug"))
			log.debug(msg);
		else if (level.equalsIgnoreCase("info"))
			log.info(msg);
		else if (level.equalsIgnoreCase("warn"))
			log.warn(msg);
		else
			log.error(msg);
	}

	public static void mail(String mailFrom, String mailTo, final String user,
			final String pass, String subject, String msg, String smtpServer,
			String port) {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.port", port);

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(user, pass);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailFrom));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(mailTo));
			message.setSubject(subject);
			message.setText(msg);

			Transport.send(message);

		} catch (MessagingException e) {
			log.error(e.getMessage());
		}
	}
}
