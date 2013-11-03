package com.medisanaspace.web.library;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Helper class to send email via a configured JNDI Java mail entry in the
 * server.
 * 
 * @author Clemens Lode, <clemens.lode@medisanaspace.com>
 */
public final class EmailHelper {
	private final static String SERVER_EMAIL = "Jan.krause@medisanaspace.com";
	private final static String SUPPORT_EMAIL = "Jan.krause@medisanaspace.com";
	
	private EmailHelper() {
	}

	private static Session getMailvitadock() throws NamingException {
		Context c = new InitialContext();
		//java:comp/env/mail/vitadock
		return (Session) c.lookup("mail/vitadock");
	}

	public static void sendMail(final String email, final String subject,
			final String body) throws NamingException, MessagingException,
			UnsupportedEncodingException {
		Session mailvitadock = getMailvitadock();
		MimeMessage message = new MimeMessage(mailvitadock);
		message.setFrom(new InternetAddress(SERVER_EMAIL,
				"VitaDock Online Support"));
		InternetAddress[] replyToAddress = new InternetAddress[1];
		replyToAddress[0] = new InternetAddress(SUPPORT_EMAIL,
				"VitaDock Online Support");
		message.setReplyTo(replyToAddress);
		message.setRecipients(RecipientType.TO,
				InternetAddress.parse(email, false));
		message.setSentDate(Calendar.getInstance().getTime());

		message.setSubject(subject, "UTF-8");
		message.setText(body, "UTF-8");
		Transport.send(message);
	}

	public static void sendMail(final String email, final String subject,
			final String body, final String filepath, final String filename)
			throws NamingException, MessagingException,
			UnsupportedEncodingException {
		Session mailvitadock = getMailvitadock();
		MimeMessage message = new MimeMessage(mailvitadock);
		message.setFrom(new InternetAddress(SERVER_EMAIL,
				"VitaDock Online Support"));
		InternetAddress[] replyToAddress = new InternetAddress[1];
		replyToAddress[0] = new InternetAddress(SUPPORT_EMAIL,
				"VitaDock Online Support");
		message.setReplyTo(replyToAddress);
		message.setRecipients(RecipientType.TO,
				InternetAddress.parse(email, false));
		message.setSentDate(Calendar.getInstance().getTime());

		message.setSubject(subject, "UTF-8");

		MimeBodyPart mbp1 = new MimeBodyPart();
		mbp1.setText(body, "UTF-8");

		MimeBodyPart mbp2 = new MimeBodyPart();

		DataSource source = new FileDataSource(filepath);
		mbp2.setDataHandler(new DataHandler(source));
		mbp2.setFileName(filename);

		// create the Multipart and its parts to it
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(mbp1);
		mp.addBodyPart(mbp2);

		// add the Multipart to the message
		message.setContent(mp);

		Transport.send(message);
	}
}
