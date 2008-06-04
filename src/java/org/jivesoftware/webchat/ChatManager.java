/**
 * $RCSfile$
 * $Revision: 29977 $
 * $Date: 2006-05-05 11:54:03 -0700 (Fri, 05 May 2006) $
 *
 * Copyright (C) 2003-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat;

import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.webchat.settings.ChatSettingsManager;
import org.jivesoftware.webchat.settings.ConnectionSettings;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;

/**
 * The ChatManager for the Web Chat Service. The ChatManager handles all ChatSessions,
 * connections, and settings handlers for the Web Chat Service.
 *
 * @author Derek DeMoro
 */
public final class ChatManager {
    private Map sessions;
    private XMPPConnection globalConnection;
    private ChatSettingsManager chatSettingsManager;

    private static final int MAXIMUM_STALE_SESSION_LENGTH = 30 * 60000;

    private static ChatManager singleton;
    private static final Object LOCK = new Object();

    /**
     * Returns the singleton instance of <CODE>ChatManager</CODE>,
     * creating it if necessary.
     * <p/>
     *
     * @return the singleton instance of <Code>ChatManager</CODE>
     */
    public static ChatManager getInstance() {
        // Synchronize on LOCK to ensure that we don't end up creating
        // two singletons.
        synchronized (LOCK) {
            if (null == singleton) {
                ChatManager controller = new ChatManager();
                singleton = controller;
                return controller;
            }
        }
        return singleton;
    }

    /**
     * Creates a new session manager.
     */
    private ChatManager() {
        sessions = Collections.synchronizedMap(new HashMap());

        // Setup timer to check for lingering sessions.
        Timer timer = new Timer();

        int delay = 10000;   // delay for 5 sec.
        int period = 10000;  // repeat every sec.

        final TimerTask closeSessionTask = new TimerTask() {
            public void run() {
                removeStaleChats();
            }
        };
        timer.scheduleAtFixedRate(closeSessionTask, delay, period);
    }

    private void removeStaleChats() {
        final Iterator chatSessions = new ArrayList(getChatSessions()).iterator();
        while (chatSessions.hasNext()) {
            ChatSession chatSession = (ChatSession)chatSessions.next();
            long lastCheck = chatSession.getLastCheck();
            if (chatSession.isClosed()) {
                if (lastCheck < new Date().getTime() - MAXIMUM_STALE_SESSION_LENGTH) {
                    removeChatSession(chatSession.getSessionID());
                }
            }
            else {
                // If the last time the user check for new messages is greater than one minute,
                // then we can assume the user has closed to window and has not explicitly closed the connection.
                if (((System.currentTimeMillis() - lastCheck > 60000) && lastCheck != 0)) {
                    //System.out.println("Closing Chat session due to no check for messages within the last 15 seconds. Timeout is for user " + chatSession.getNickname() + " on " + new Date());

                    // Close Chat Session
                    chatSession.close();

                    // Remove from cache
                    removeChatSession(chatSession.getSessionID());
                }
                // Handle case where the user never joins a conversation and leaves the queue.
                else if (lastCheck == 0 && !chatSession.isInQueue()) {
                    // Close Chat Session
                    chatSession.close();

                    // Remove from cache
                    removeChatSession(chatSession.getSessionID());
                }
            }
        }
    }

    /**
     * Adds a new <code>ChatSession</code> associated with a particular uniqueID.
     *
     * @param chatID      the uniqueID.
     * @param chatSession the <code>ChatSession</code> associated with the uniqueID.
     */
    public void addChatSession(String chatID, ChatSession chatSession) {
        sessions.put(chatID, chatSession);
    }

    /**
     * Gets the <code>ChatSession</code> associated with a unique ID. In general, each user is
     * assigned one ChatSession per transaction.
     *
     * @param chatID the uniqueID of the user.
     * @return the ChatSession associated with the unique ID.
     */
    public ChatSession getChatSession(String chatID) {
        return (ChatSession)sessions.get(chatID);
    }

    /**
     * Removes a <code>ChatSession</code> from the ChatManager.
     *
     * @param chatID the unique id associated with the ChatSession.
     * @return the ChatSession being removed.
     */
    public ChatSession removeChatSession(String chatID) {
        ChatSession sess = (ChatSession)sessions.remove(chatID);
        return sess;
    }

    /**
     * Returns a <code>Collection</code> of <code>ChatSessions</code>.
     *
     * @return <code>Collection</code> of <code>ChatSessions</code>
     */
    public Collection getChatSessions() {
        return sessions.values();
    }

    /**
     * Returns the number of ChatSessions.
     *
     * @return the number of ChatSessions.
     */
    public int getNumberOfSessions() {
        return sessions.size();
    }

    /**
     * Close a particular <code>ChatSession</code>.
     *
     * @param chatID the uniqueID identifying the ChatSession.
     */
    public void closeChatSession(String chatID) {
        final ChatSession chatSession = getChatSession(chatID);
        if (chatSession != null) {
            chatSession.close();
            removeChatSession(chatID);
        }
    }

    /**
     * Removes all ChatSessions from the Manager.
     */
    public void destroyAllSessions() {
        Collection chatSessions = getChatSessions();
        Iterator iter = chatSessions.iterator();
        while (iter.hasNext()) {
            ChatSession chatSession = (ChatSession)iter.next();
            chatSession.close();
        }
        sessions = new HashMap();
    }

    /**
     * Sets the Global Connection for the Web Client service.
     *
     * @param con the <code>XMPPConnection</code> for the Web Client Service.
     */
    public void setGlobalConnection(XMPPConnection con) {
        globalConnection = con;

        WorkgroupStatus.initStatusListener();
    }

    /**
     * Gets the Global <code>XMPPConnection</code> for the Web Client Service.
     *
     * @return the <code>XMPPConnection</code> for the Web Client Service.
     */
    public XMPPConnection getGlobalConnection() {
        return globalConnection;
    }

    /**
     * Sets the <code>ChatSettingsManager</code> for the Web Client Service. The
     * ChatSettingsManager allows for connection details such as host, port, and password
     * s needed to connect to the XMPP Server.
     *
     * @param chatSettingsManager the ChatSettingsManager.
     */
    public void setChatSettingsManager(ChatSettingsManager chatSettingsManager) {
        this.chatSettingsManager = chatSettingsManager;
    }

    /**
     * Gets the <code>ChatSettingsManager</code> for the Web Client Service. You would
     * call this if you needed information on how to connect to XMPP Server.
     *
     * @return the ChatSettingsManager.
     */
    public ChatSettingsManager getChatSettingsManager() {
        return chatSettingsManager;
    }


    /**
     * Connection Handling.
     */
    public synchronized XMPPConnection createConnection(final ServletContext context) {
        XMPPConnection xmppConn = null;

        ConnectionSettings settings = chatSettingsManager.getSettings();
        if (settings == null) {
            return null;
        }

        boolean sslEnabled = settings.isSSLEnabled();
        String host = settings.getServerDomain();
        int port = settings.getPort();
        if (sslEnabled) {
            port = settings.getSSLPort();
        }

        // Initialize the XMPP connection
        try {

            if (port == -1) {
                xmppConn = new XMPPConnection(host);
            }
            else {
                ConnectionConfiguration config = new ConnectionConfiguration(host, port);
                xmppConn = new XMPPConnection(config);
            }
            xmppConn.connect();

            // Login the presence bot user
            xmppConn.loginAnonymously();

            // Add Connection to Application Object
            setGlobalConnection(xmppConn);

            // Add a connection listener.
            xmppConn.addConnectionListener(new ConnectionListener() {
                public void connectionClosed() {
                    context.log("Main Connection closed for some reason");
                }

                public void connectionClosedOnError(Exception e) {
                    context.log("Connection closed on Error", e);
                }


                public void reconnectingIn(int i) {

                }

                public void reconnectionSuccessful() {

                }

                public void reconnectionFailed(Exception exception) {

                }
            });
        }
        catch (Exception e) {
            context.log("Error creating connection to server.", e);
            return null;
        }
        finally {
            // If creating the connection failed this time around, make sure it is cleaned up.
            if (xmppConn != null && !xmppConn.isAuthenticated() && xmppConn.isConnected()) {
                try {
                    xmppConn.disconnect();
                }
                catch (Exception e) {
                    context.log("Error disconnecting from server.", e);
                }
            }
        }
        return xmppConn;
    }

    public boolean isConnected() {
        XMPPConnection con = getGlobalConnection();
        return con != null && (con.isConnected() && con.isAuthenticated());
    }
}
