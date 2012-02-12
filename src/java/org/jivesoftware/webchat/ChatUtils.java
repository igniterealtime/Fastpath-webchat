/**
 * $RCSfile$
 * $Revision: 29138 $
 * $Date: 2006-04-05 16:04:50 -0700 (Wed, 05 Apr 2006) $
 * <p/>
 * Copyright (C) 1999-2005 Jive Software. All rights reserved.
 * <p/>
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */
package org.jivesoftware.webchat;

import org.jivesoftware.smackx.workgroup.user.Workgroup;
import org.jivesoftware.webchat.actions.ChatQueue;
import org.jivesoftware.webchat.personal.ChatMessage;
import org.jivesoftware.webchat.util.FormText;
import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.webchat.util.WebUtils;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.MessageEventManager;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.Iterator;
import java.util.List;

/**
 * ChatUtils handles basic messaging functionallity, including
 * the sending and queueing of messages.
 *
 * @author Derek DeMoro
 */
public class ChatUtils {

    /**
     * Returns all messages in a queue associated with the given
     * chatID
     *
     * @param chatID the chatID.
     * @return an Array of <code>ChatMessages</code>
     */
    public static ChatMessage[] getAllMessages(String chatID) {
        ChatSession chatSession = getChatSession(chatID);
        if (chatSession == null || chatSession.isClosed()) {
            return null;
        }

        List messages = chatSession.getMessageList();
        ChatMessage[] m = (ChatMessage[])messages.toArray(new ChatMessage[messages.size()]);
        chatSession.getMessageList().clear();
        return m;
    }

    /**
     * Sends a message from a given <code>ChatSession</code> that is associated with
     * a given chat id.
     *
     * @param chatID  the chat id.
     * @param message the message to send.
     */
    public static void sendMessage(String chatID, String message) {
        ChatSession chatSession = getChatSession(chatID);

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

                // Handle odd case of double spacing
                if (message.endsWith("\n")) {
                    message = message.substring(0, message.length() - 1);
                }

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
     * Returns true if the user is typing.
     * @param chatID the associated chat id.
     * @return true if the agent is typing.
     */
    public static boolean isTyping(String chatID) {
        ChatSession chatSession = getChatSession(chatID);
        if (chatSession == null) {
            return false;
        }

        return chatSession.composingNotificationsWereReceived();
    }

    /**
     * Clear agent is typing.
     * @param chatID the associated chat id.
     */
    public static void clearAgentTyping(String chatID) {
        ChatSession chatSession = getChatSession(chatID);
        if (chatSession == null) {
            return;
        }

        chatSession.clearNotificationReceived();
    }

    public static void customerIsTyping(String chatID) {
        ChatSession chatSession = getChatSession(chatID);
        if (chatSession == null) {
            return;
        }


        final MultiUserChat chat = chatSession.getGroupChat();
        final Iterator iter = chat.getOccupants();
        while (iter.hasNext()) {
            String from = (String)iter.next();
            String tFrom = StringUtils.parseResource(from);
            String nickname = chat.getNickname();
            if (tFrom != null && !tFrom.equals(nickname)) {
                MessageEventManager messageEventManager = chatSession.getMessageEventManager();
                messageEventManager.sendComposingNotification(from, "l0k1");
            }
        }
    }


    public static ChatQueue getChatQueue(String chatID) {
        ChatSession chatSession = getChatSession(chatID);
        if (chatSession == null) {
            return null;
        }

        ChatQueue queue = new ChatQueue();
        if (chatSession.isClosed()) {
            queue.setConnectionDropped(true);
            return queue;
        }

        // We've been routed to an agent and received a  MUC Invite.
        if (chatSession.isInGroupChat()) {
            queue.setRouted(true);
            queue.setInQueue(false);
            return queue;
        }
        else if (chatSession.isInQueue()) {
            final Workgroup workgroup = chatSession.getWorkgroup();
            if (workgroup != null) {
                queue.setQueuePosition(workgroup.getQueuePosition());
                queue.setQueueTime(workgroup.getQueueRemainingTime());
                queue.setInQueue(true);
            }
            return queue;
        }
        else if (!chatSession.isInQueue() && !chatSession.isInGroupChat()) {
            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            queue.setConnectionDropped(!chatSession.isInQueue() && !chatSession.isInGroupChat());
            return queue;
        }

        if (!chatSession.getWorkgroup().isAvailable()) {
            queue.setConnectionDropped(true);
            chatSession.close();
        }

        return queue;
    }

    public static ChatSession getChatSession(String chatID) {
        ChatManager chatManager = ChatManager.getInstance();
        return chatManager.getChatSession(chatID);
    }


    public static String getEndMessage(String chatID, String workgroup) {
        ChatManager chatManager = ChatManager.getInstance();
        ChatSession chatSession = chatManager.getChatSession(chatID);
        if (chatSession != null) {
            String lastAgent = chatSession.getLastAgentInRoom();
            if (lastAgent == null) {
                lastAgent = "Agent";
            }
            return FormText.agentHasEndedConversation(lastAgent, workgroup);
        }

        return "Your chat has ended.";
    }
}
