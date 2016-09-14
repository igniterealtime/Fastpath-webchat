/**
 * $RCSfile$
 * $Revision: 24191 $
 * $Date: 2005-11-28 20:16:08 -0800 (Mon, 28 Nov 2005) $
 *
 * Copyright (C) 1999-2005 Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is
 subject to license terms.
 */
package org.jivesoftware.webchat.personal;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jivesoftware.smackx.xevent.MessageEventManager;
import org.jivesoftware.smackx.xevent.MessageEventNotificationListener;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.settings.ConnectionSettings;
import org.jivesoftware.webchat.util.WebLog;

/* RCSFile: $
 * Revision: $
 * Date: $
 *
 * Copyright (C) 2004-2008 JiveSoftware, Inc. All rights reserved.
 *
 * This software is the proprietary information of CoolServlets, Inc.
 * Use is subject to license terms.
 */

public class PersonalChat implements MessageEventNotificationListener {
    private Chat chat;
    private XMPPTCPConnection con;
    private ChatPoller chatPoller;

    // User information
    private String jid;
    private String nickname;
    private MessageEventManager messageEventManager;
    private boolean agentTyping = false;

    private long lastCheck;
    private Timer timer;

    private String email;

    public PersonalChat() {

    }

    public void startChat(String jid, String nickname, String email, String question) {
        ChatManager chatManager = ChatManager.getInstance();
        ConnectionSettings settings = chatManager.getChatSettingsManager().getSettings();
        String host = settings.getServerDomain();
        int port = settings.getPort();
        try {
        	XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder()
        			.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
        			.setServiceName(host)
        			.setHost(host)
        			.setPort(port);
            con = new XMPPTCPConnection(config.build());
            con.connect();
            con.loginAnonymously();
            chatPoller = new ChatPoller();
        }
        catch (XMPPException e) {
            WebLog.logError("Error starting chat.", e);
        } catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        this.jid = jid;
        this.nickname = nickname;
        this.email = email;

        // TODO: this is broken!! Must refactor.
        //chat = new Chat(con, jid);

        chatPoller.listenForMessages(con, chat);

        messageEventManager = MessageEventManager.getInstanceFor(con);
        messageEventManager.addMessageEventNotificationListener(this);

        Message newMessage = new Message();
        newMessage.setTo(jid);
//      newMessage.setProperty("nickname", nickname);
//      newMessage.setProperty("anonymous", true);
//      newMessage.setProperty("email", email);
//      newMessage.setProperty("question", question);
        JivePropertiesManager.addProperty(newMessage , "nickname", nickname);
        JivePropertiesManager.addProperty(newMessage , "anonymous", true);
        JivePropertiesManager.addProperty(newMessage , "email", email);
        JivePropertiesManager.addProperty(newMessage , "question", question);
        newMessage.setBody( "I would like to chat with you.");
        try {
			chat.sendMessage(newMessage);
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        timer = new Timer();

        int delay = 10000;   // delay for 5 sec.
        int period = 10000;  // repeat every sec.

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                long diff = System.currentTimeMillis() - lastCheck;

                // If nothing has been read for the last 5 seconds, then the session
                // has expired.
                if (diff > 30000) {
                    if (con != null) {
                        System.out.println("Closing dwr connect.");
                        con.disconnect();
                    }
                    timer.cancel();
                    this.cancel();
                }
            }
        }, delay, period);

    }

    public void endChat() {
        Message newMessage = new Message();
        newMessage.setTo(jid);
//      newMessage.setProperty("nickname", nickname);
//      newMessage.setProperty("anonymous", true);
//      newMessage.setProperty("email", email);
//      newMessage.setProperty("left", true);
        JivePropertiesManager.addProperty(newMessage , "nickname", nickname);
        JivePropertiesManager.addProperty(newMessage , "anonymous", true);
        JivePropertiesManager.addProperty(newMessage , "email", email);
        JivePropertiesManager.addProperty(newMessage , "left", true);

        newMessage.setBody(nickname+ " has left the conversation.");
        try {
            chat.sendMessage(newMessage);
        }
        catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (con != null) {
            con.disconnect();
        }
    }

    public boolean isClosed() {
        return con == null || !con.isConnected();
    }

    public ChatMessage getNextMessage() {
        lastCheck = System.currentTimeMillis();

        if (chatPoller != null) {
            return chatPoller.getNextMessage();
        }
        return null;
    }

    public void sendMessage(String message) {
        Message newMessage = new Message();
        newMessage.setTo(jid);
//        newMessage.setProperty("nickname", nickname);
//        newMessage.setProperty("anonymous", true);
//        newMessage.setProperty("email", email);
        JivePropertiesManager.addProperty(newMessage , "nickname", nickname);
        JivePropertiesManager.addProperty(newMessage , "anonymous", true);
        JivePropertiesManager.addProperty(newMessage , "email", email);
        newMessage.setBody(message);
        try {
            chat.sendMessage(newMessage);
        }
        catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void customerIsTyping() throws NotConnectedException {
        messageEventManager.sendComposingNotification(jid, "l0k1");
    }

    public boolean isAgentTyping() {
        boolean isAgentTyping = agentTyping;
        agentTyping = false;
        return isAgentTyping;
    }


    public void deliveredNotification(String chatID, String chatID1) {
    }

    public void displayedNotification(String chatID, String chatID1) {
    }

    public void composingNotification(String chatID, String chatID1) {
        agentTyping = true;
    }

    public void offlineNotification(String chatID, String chatID1) {
    }

    public void cancelledNotification(String chatID, String chatID1) {
    }
}
