/**
 * $RCSfile$
 * $Revision: 18739 $
 * $Date: 2005-04-10 23:51:24 -0700 (Sun, 10 Apr 2005) $
 *
 * Copyright (C) 1999-2005 Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is
 subject to license terms.
 */
package org.jivesoftware.webchat.personal;

import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.StringUtils;
import org.jxmpp.util.XmppStringUtils;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* RCSFile: $
 * Revision: $
 * Date: $
 *
 * Copyright (C) 2004-2008 JiveSoftware, Inc. All rights reserved.
 *
 * This software is the proprietary information of CoolServlets, Inc.
 * Use is subject to license terms.
 */

public class ChatPoller {
    private List<ChatMessage> messageList;
    private Chat chat;

    public ChatPoller() {
        messageList = Collections.synchronizedList(new ArrayList<ChatMessage>());
    }

    public void listenForMessages(final XMPPTCPConnection con, Chat chat) {
        this.chat = chat;

        StanzaListener packetListener = new StanzaListener() {
			
			@Override
			public void processPacket(Stanza packet) throws NotConnectedException {
				// TODO Auto-generated method stub
				Message message = (Message) packet;
                if (ModelUtil.hasLength(message.getBody())) {
                    ChatMessage chatMessage = new ChatMessage(message);
                    String from = StringUtils.parseName(message.getFrom());
                    String body = message.getBody();

                    if(body.equals("/kill")){
                        con.disconnect();
                        return;
                    }
                    chatMessage.setFrom(from);
                    chatMessage.setBody(body);
                    messageList.add(chatMessage);
                }
			}
		};
        con.addAsyncStanzaListener(packetListener, new StanzaTypeFilter(Message.class));
    }

    public ChatMessage getNextMessage() {
        if (messageList.size() > 0) {
            ChatMessage message = (ChatMessage)messageList.get(0);
            messageList.remove(message);
            return message;
        }
        return null;
    }
}
