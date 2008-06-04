/**
 * $RCSFile$
 * $Revision: 18449 $
 * $Date: 2005-02-14 12:13:03 -0800 (Mon, 14 Feb 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.actions;

import org.jivesoftware.webchat.ChatSession;
import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.webchat.util.WebUtils;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.MessageEventManager;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.Iterator;

/**
 * Responsible for sending messages to all parties in a ChatRoom. The
 * ChatWriter sends and writes out messages sent from the client.
 */
public final class ChatWriter {
    private ChatSession chatSession;

     /**
     * ChatWriter constructor must accept a ChatSession to work from.
     * @param chatSession the owning ChatSession.
     */
    public ChatWriter(ChatSession chatSession){
         this.chatSession = chatSession;
     }

    /**
     * Updates the web transcript and sends the message.
     * @param message the message to send.
     */
    public void write(String message) {
        // If the user doesn't have a chat session, notify them.
        if (chatSession == null) {
            return;
        }

        // Notify user if the chat session has closed.
        if (chatSession.isClosed() || !chatSession.isInGroupChat()) {
            return;
        }

        // If the message isn't specified, do nothing.
        if (message != null) {
            try {
                final MultiUserChat chat = chatSession.getGroupChat();
                message = message.replaceAll("\r", " ");

                // update the transcript:
                String body = WebUtils.applyFilters(message);
                String nickname = chat.getNickname();
                chatSession.updateTranscript(nickname, body);

                if (chat != null) {
                    final Message chatMessage = new Message();
                    chatMessage.setType(Message.Type.groupchat);
                    chatMessage.setBody(message);

                    String room = chat.getRoom();
                    chatMessage.setTo(room);
                    chat.sendMessage(chatMessage);
                }
            }
            catch (XMPPException e) {
                WebLog.logError("Error sending message:", e);
            }
        }
    }

    /**
     * Notifies all MessageEventHandlers that the customer is typing a message.
     */
    public void customerIsTyping() {
        final MultiUserChat chat = chatSession.getGroupChat();
        final Iterator iter = chat.getOccupants();
        while (iter.hasNext()) {
            String from = (String) iter.next();
            String tFrom = StringUtils.parseResource(from);
            String nickname = chat.getNickname();
            if (tFrom != null && !tFrom.equals(nickname)) {
                MessageEventManager messageEventManager = chatSession.getMessageEventManager();
                messageEventManager.sendComposingNotification(from, "l0k1");
            }
        }
    }

    /**
     * Sets the current ChatSession.
     * @param chatSession the <code>ChatSession</code> associated with this Chat.
     */
    public void setChatSession(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    /**
     * Returns the <code>ChatSession</code> associated with this Chat.
     * @return the <code>ChatSession</code> associated with this Chat.
     */
    public ChatSession getChatSession() {
        return chatSession;
    }
}
