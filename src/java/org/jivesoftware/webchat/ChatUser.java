/**
 * $RCSfile$
 * $Revision: 18645 $
 * $Date: 2005-03-22 21:34:24 -0800 (Tue, 22 Mar 2005) $
 *
 * Copyright (C) 1999-2005 Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is
 subject to license terms.
 */
package org.jivesoftware.webchat;

/**
 * Represents one user in a chat. This user information is retrieved via their defined
 * chat id within the system.
 */
public class ChatUser {

    private String workgroup;
    private String chatID;

    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public ChatSession getChatSession() {
        ChatManager chatManager = ChatManager.getInstance();
        return chatManager.getChatSession(chatID);
    }

    public boolean hasSession(){
        return getChatSession() != null;
    }

    public void closeSession(){
        if(hasSession()){
            getChatSession().close();
        }
    }

    public void removeSession(){
        ChatManager chatManager = ChatManager.getInstance();
        chatManager.closeChatSession(chatID);
    }
}
