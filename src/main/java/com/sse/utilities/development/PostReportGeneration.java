package com.sse.utilities.development;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sse.utilities.configuration.PropertiesHolder;


/**
 * This is a test utility called after report generation. Not part of the real suite (probably)
 * @author atul
 *
 */
public class PostReportGeneration {

	private static final String smtpfromemail = PropertiesHolder.configurationProperties.getProperty("smtpfromemail","autotest@reebusiness.com");
	private static final String smtpport = PropertiesHolder.configurationProperties.getProperty("smtpport","25");
	private static final String smtpserver = PropertiesHolder.configurationProperties.getProperty("smtpserver","rbnsmtp.b2b.regn.net");

	private String user;
	private File f;

	@SuppressWarnings("javadoc")
	public PostReportGeneration(File f, String user) {
		this.f = f;
		this.user = user;
	}

	@SuppressWarnings("javadoc")
	public void run() {

		Properties props = new Properties();
		props.put("mail.smtp.host", smtpserver);
		props.put("mail.smtp.port", smtpport);
		Session session = Session.getInstance(props, null);

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(smtpfromemail));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("andy.mitchell@reedbusiness.com"));
			message.setSubject("L2I automation report " + user + " : " + new Date());
			Multipart multiPart = new MimeMultipart();
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("File :\n" + f.getAbsolutePath() + " attached");
			multiPart.addBodyPart(messageBodyPart);
			BodyPart attachmentMessageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(f);
			attachmentMessageBodyPart.setDataHandler(new DataHandler(source));
			attachmentMessageBodyPart.setFileName(f.getName());
			multiPart.addBodyPart(attachmentMessageBodyPart);
			message.setContent(multiPart);
			Transport.send(message);
		} catch (@SuppressWarnings("unused") MessagingException e) {
		}
	}

}
