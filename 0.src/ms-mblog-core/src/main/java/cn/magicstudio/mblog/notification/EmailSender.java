package cn.magicstudio.mblog.notification;

import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final Boolean MAIL_SMTP_AUTH_TRUE = Boolean.valueOf(true);
	public static final Boolean MAIL_SMTP_AUTH_FALSE = Boolean.valueOf(false);
	public static final String MAIL_PROTOCOL = "smtp";
	public static final Boolean DEBUG_FLG = Boolean.valueOf(true);

	public void send() {
		String charset = "utf-8";
		String mailServer = "smtp.163.com";
		String mailUser = "";
		String mailPassword = "";
		String senderAddr = "jsjw18@163.com";
		String subject = "test";
		String content = "hello mail test!";
		String receiverAddr = "wang.w1@yougou.com";
		String[] allMainReceiversAddr = { "wang.w1@yougou.com",
				"zhang.tl@yougou.com", "song.x@yougou.com" };
		String[] allCarbonReceiversAddr = { "liu.ms@yougou.com",
				"zhang.lh@yougou.com" };
		try {
			Properties props = new Properties();

			props.put("mail.smtp.host", mailServer);

			props.put("mail.smtp.auth", MAIL_SMTP_AUTH_TRUE);

			Session mailSession = Session.getInstance(props);

			mailSession.setDebug(DEBUG_FLG.booleanValue());
			MimeMessage message = new MimeMessage(mailSession);

			Address address = new InternetAddress(senderAddr);

			message.addFrom(new Address[] { address });

			message.setSentDate(new Date(System.currentTimeMillis() + 120000L));

			Address[] addressArray = new InternetAddress[allMainReceiversAddr.length];
			for (int i = 0; i < allMainReceiversAddr.length; i++) {
				addressArray[i] = new InternetAddress(allMainReceiversAddr[i]);
			}
			message.addRecipients(Message.RecipientType.TO, addressArray);

			Address[] carbonAddressArray = new InternetAddress[allCarbonReceiversAddr.length];
			for (int i = 0; i < allCarbonReceiversAddr.length; i++) {
				carbonAddressArray[i] = new InternetAddress(
						allCarbonReceiversAddr[i]);
			}
			message.addRecipients(Message.RecipientType.CC, carbonAddressArray);

			message.setSubject(subject, charset);

			Multipart multiPart = new MimeMultipart();

			BodyPart bodyPart4Content = new MimeBodyPart();
			bodyPart4Content.setText(content);
			multiPart.addBodyPart(bodyPart4Content);

			message.setContent(multiPart);

			message.saveChanges();

			Transport transport = mailSession.getTransport("smtp");
			transport.connect(mailServer, mailUser, mailPassword);

			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.TO));

			transport.sendMessage(message,
					message.getRecipients(Message.RecipientType.CC));

			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
