package com.ctapweb.api.servlets.users;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ctapweb.api.servlets.utils.PropertiesManager;
import com.ctapweb.api.servlets.utils.PropertyKeys;


public class SendMail {
	Properties mailProps;
	
	private String smtpHost;
	private String smtpPort;
	private String smtpAccount;
	private String smtpPasswd;
	private String fromEmail;
	
	
	public SendMail() throws IOException {
		//load properties from config 
		Properties ctapProps = PropertiesManager.getProperties();
		smtpHost = ctapProps.getProperty(PropertyKeys.MAIL_SMTP_HOST);
		smtpPort = ctapProps.getProperty(PropertyKeys.MAIL_SMTP_PORT);
		smtpAccount = ctapProps.getProperty(PropertyKeys.MAIL_SMTP_ACCOUNT);
		smtpPasswd = ctapProps.getProperty(PropertyKeys.MAIL_SMTP_PASSWD);
		fromEmail = ctapProps.getProperty(PropertyKeys.MAIL_FROM);

		mailProps = new Properties();
		mailProps.put("mail.smtp.host", smtpHost); 
		mailProps.put("mail.smtp.port", smtpPort);
        mailProps.put("mail.smtp.auth", "true");
//        mailProps.put("mail.debug", "true"); 
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.socketFactory.port", "465");
        mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProps.put("mail.smtp.socketFactory.fallback", "false");

		
	}

	public void sendMail(String recipientEmail, String subject, String text) throws AddressException, MessagingException {
		Session mailSession = Session.getInstance(mailProps, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpAccount, smtpPasswd);
            }
        });

        Message msg = new MimeMessage(mailSession);

        //set the FROM, TO, DATE and SUBJECT fields
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipientEmail));
        msg.setSentDate(new Date());
        msg.setSubject(subject);
        msg.setText(text);

        Transport.send( msg );

		
	}
}
