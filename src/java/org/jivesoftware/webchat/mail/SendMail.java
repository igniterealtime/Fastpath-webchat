/**
 * $RCSFile$
 * $Revision: 24191 $
 * $Date: 2005-11-28 20:16:08 -0800 (Mon, 28 Nov 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.mail;

import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.actions.WebBean;
import org.jivesoftware.webchat.providers.Settings;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.smack.XMPPConnection;

import java.rmi.server.UID;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Sends an email using simple SMTP to the specified addresse(s).
 */
public class SendMail extends WebBean {
    private ChatManager chatManager = ChatManager.getInstance();
    private String to;
    private String subject;
    private String body;
    private String from;
    private String attachmentFile;
    private boolean isHTML = false;

    public boolean sendMessage(String workgroup) {
        boolean ok = false;

        String uidString = "";
        try {
            // Set settings for this session
            XMPPConnection con = chatManager.getGlobalConnection();
            Map map = Settings.getSettings(con, workgroup, Settings.EMAIL_SETTINGS);
            String host = (String)map.get("host");
            String port = (String)map.get("port");
            String password = (String)map.get("password");
            String username = (String)map.get("username");

            // Set the email properties necessary to send email
            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.server", host);
            if (ModelUtil.hasLength(port)) {
                props.put("mail.smtp.port", port);
            }

            if (ModelUtil.hasLength(username)) {
                props.put("mail.smtp.auth", "true");
            }

            Session sess;

            if (ModelUtil.hasLength(password) && ModelUtil.hasLength(username)) {
                sess = Session.getInstance(props, new MailAuthentication(username, password));
            }
            else {
                sess = Session.getDefaultInstance(props, null);
            }


            Message msg = new MimeMessage(sess);

            StringTokenizer toST = new StringTokenizer(to, ",");
            if (toST.countTokens() > 1) {
                InternetAddress[] address = new InternetAddress[toST.countTokens()];
                int addrIndex = 0;
                String addrString = "";
                while (toST.hasMoreTokens()) {
                    addrString = toST.nextToken();
                    address[addrIndex] = (new InternetAddress(addrString));
                    addrIndex = addrIndex + 1;
                }
                msg.setRecipients(Message.RecipientType.TO, address);
            }
            else {
                InternetAddress[] address = {new InternetAddress(to)};
                msg.setRecipients(Message.RecipientType.TO, address);
            }

            InternetAddress from = new InternetAddress(this.from);

            msg.setFrom(from);
            msg.setSubject(subject);

            UID msgUID = new UID();
            uidString = msgUID.toString();
            msg.setHeader("X-Mailer", uidString);
            msg.setSentDate(new Date());
            MimeMultipart mp = new MimeMultipart();

            // create body part for textarea
            MimeBodyPart mbp1 = new MimeBodyPart();

            if (isHTML) {
                mbp1.setContent(body, "text/html");
            }
            else {
                mbp1.setContent(body, "text/plain");
            }
            mp.addBodyPart(mbp1);

            try {
                if (!isHTML) {
                    msg.setContent(body, "text/plain");
                }
                else {
                    msg.setContent(body, "text/html");
                }
                Transport.send(msg);
                ok = true;
            }
            catch (SendFailedException sfe) {
                WebLog.logError("Error sending mail.", sfe);
            }

        }
        catch (Exception eq) {
            WebLog.logError("Error sending mail.", eq);
        }
        return ok;
    }

    class MailAuthentication extends Authenticator {
        String smtpUsername = null;
        String smtpPassword = null;

        public MailAuthentication(String username, String password) {
            smtpUsername = username;
            smtpPassword = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUsername, smtpPassword);
        }
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void sendAsHTML(boolean isHTML) {
        this.isHTML = isHTML;
    }

    public boolean sendingAsHTML() {
        return isHTML;
    }
}