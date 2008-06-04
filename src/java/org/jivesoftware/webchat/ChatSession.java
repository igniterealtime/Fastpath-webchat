/**
 * $RCSFile$
 * $Revision: 28803 $
 * $Date: 2006-03-22 08:53:32 -0800 (Wed, 22 Mar 2006) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat;

import com.jivesoftware.smack.workgroup.WorkgroupInvitation;
import com.jivesoftware.smack.workgroup.WorkgroupInvitationListener;
import com.jivesoftware.smack.workgroup.user.Workgroup;
import org.jivesoftware.webchat.history.Line;
import org.jivesoftware.webchat.history.Transcript;
import org.jivesoftware.webchat.personal.ChatMessage;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.MessageEventManager;
import org.jivesoftware.smackx.MessageEventNotificationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.DelayInformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The <code>ChatSession</code> class is responsible for handling all aspects of a chat. Connections, Joining
 * Queues and Joining Rooms all take place within <code>ChatSession</code>.
 *
 * @author Derek DeMoro
 */
public class ChatSession implements MessageEventNotificationListener, PacketListener {
    private XMPPConnection connection;
    private Workgroup workgroup;
    private MultiUserChat groupChat;

    private String name;
    private List presenceList = new ArrayList();

    private MessageEventManager messageEventManager;
    private boolean composingNotificationsReceived;
    private Map metadataMap;


    private Transcript transcript;

    private String nickname;

    private String emailAddress;

    private String userid;

    private String sessionID;

    private List messageList = new ArrayList();

    private long lastCheck;

    private String roomName;

    private String lastAgent;

    private int port;
    private String host;

    /**
     * Creates a new <code>ChatSession</code>.
     *
     * @param host         the host to connect to.
     * @param port         the port to connect through.
     * @param useSSL       true if we should SSL with the Connection.
     * @param nickname     the nickname to use during this chat session.
     * @param emailAddress the email address of the user.
     * @throws XMPPException
     */
    public ChatSession(String host, int port, boolean useSSL, String userid, String nickname, String emailAddress, String sessionID) throws XMPPException {
        this.sessionID = sessionID;

        this.host = host;
        this.port = port;

        transcript = new Transcript();

        this.nickname = nickname;

        this.emailAddress = emailAddress;

        this.userid = userid;
    }

    private boolean connect() throws Exception {
        if (port == -1 || port == 5222) {
            connection = new XMPPConnection(host);
        }
        else {
            ConnectionConfiguration config = new ConnectionConfiguration(host, port);
            connection = new XMPPConnection(config);
        }

        connection.connect();

        if (!connection.isConnected()) {
            return false;
        }

        composingNotificationsReceived = false;

        // Add a connection listener.
        connection.addConnectionListener(new ConnectionListener() {
            public void connectionClosed() {
                workgroup = null;
                groupChat = null;
                connection = null;
                messageEventManager = null;
                presenceList.add("The connection has been closed.");
            }

            public void connectionClosedOnError(Exception e) {
                workgroup = null;
                groupChat = null;
                connection = null;
                messageEventManager = null;
                //e.printStackTrace();
                presenceList.add("The connection has been closed.");
            }


            public void reconnectingIn(int i) {
            }

            public void reconnectionSuccessful() {
            }

            public void reconnectionFailed(Exception exception) {
            }
        });

        return true;
    }

    /**
     * Returns the current connection.
     *
     * @return the current connection used by the user.
     */
    public XMPPConnection getConnection() {
        return connection;
    }

    /**
     * Returns true if the user has logged in anonymous or with a username and password.
     *
     * @return true if the user has logged in.
     */
    public boolean isAuthenticated() {
        return connection.isAuthenticated();
    }

    /**
     * Logs the user in as an anonymous user.
     *
     * @throws XMPPException
     */
    public void loginAnonymously() throws Exception {
        boolean connected = connect();
        if (connected) {
            connection.loginAnonymously();
        }
        else {
            throw new Exception("Unable to connect to the server at this time.");
        }
    }

    /**
     * Login using the specified account information.
     *
     * @param username the username
     * @param password the password
     * @throws XMPPException
     */
    public void login(String username, String password) throws Exception {
        boolean connected = connect();
        if (connected) {
            connection.login(username, password, "Live Assistant Web Client");
            name = username;
        }
        else {
            throw new Exception("Unable to connect to the server at this time.");
        }
    }

    /**
     * Joins a queue of the specified workgroup with the associated metadata.
     *
     * @param workgroupName the name of the workgroup to join.
     * @param metaData      the metadata associated with this request.
     * @throws XMPPException if an error occurs.
     */
    public void joinQueue(String workgroupName, Map metaData) throws XMPPException {
        // Never have null values in metadata
        if (metaData.containsValue(null)) {
            WebLog.logError("You cannot have null values in the Metadata.");
            return;
        }
        workgroup = new Workgroup(workgroupName, connection);

        workgroup.addInvitationListener(new WorkgroupInvitationListener() {
            public void invitationReceived(WorkgroupInvitation workgroupInvitation) {
                String room = workgroupInvitation.getGroupChatName();
                joinRoom(room);
            }
        });


        if (workgroup != null) {
            try {
                workgroup.joinQueue(metaData, userid);
            }
            catch (XMPPException e) {
                WebLog.logError("Unable to join chat queue.", e);
            }
        }

        // If metadata about the users name is present, use it to set the name.
        if (metaData.containsKey("username")) {
            name = (String)metaData.get("username");
        }

        if (name == null) {
            name = "Visitor";
        }

        metadataMap = metaData;
    }

    /**
     * Checks to see if the user is in a queue.
     *
     * @return true if the user is in a queue, otherwise false.
     */
    public boolean isInQueue() {
        return workgroup != null && workgroup.isInQueue();
    }

    /**
     * Rejoins a Queue. This can be used if the sue
     */
    public void rejoinQueue() {
        try {
            //workgroup.joinQueue(metadataMap);
        }
        catch (Exception ex) {
            WebLog.logError("Error joining queue:", ex);
        }
    }

    /**
     * Checks to see if this <code>ChatSession</code> is a Chat Room.
     *
     * @return true if the user is active in a chat room.
     */
    public boolean isInGroupChat() {
        return groupChat != null && groupChat.isJoined();
    }

    /**
     * Returns the workgroup associated with this <code>ChatSession</code>.
     *
     * @return the workgroup associated with this ChatSession.
     */
    public Workgroup getWorkgroup() {
        return workgroup;
    }

    /**
     * Returns the <code>MultiUserChat</code> room the user is in.
     *
     * @return the MultiUserChat room the user is in.
     */
    public MultiUserChat getGroupChat() {
        return groupChat;
    }

    /**
     * Check to see if the <code>XMPPConnection</code> has been closed.
     *
     * @return true if the <code>XMPPConnection</code> has been closed.
     */
    public boolean isClosed() {
        return connection == null || !connection.isConnected();
    }

    /**
     * Returns whether composing notifications were received; regardless, the flag describing this
     * condition is set to false after this query.
     *
     * @return true if a message is being composed.
     */
    public boolean composingNotificationsWereReceived() {
        boolean flag = composingNotificationsReceived;
        composingNotificationsReceived = false;
        return flag;
    }

    public void clearNotificationReceived() {
        composingNotificationsReceived = false;
    }

    /**
     * Closed the current ChatSession and all available resources.
     */
    public void close() {
        // If we're currently waiting in the queue to be routed, leave the queue.
        if (workgroup != null && workgroup.isInQueue()) {
            try {
                workgroup.departQueue();
                workgroup = null;
            }
            catch (XMPPException xe) {
                WebLog.logError("Error closing ChatSession:", xe);
            }
        }
        // If we've already been routed and are in a chat, leave it.
        if (groupChat != null) {
            groupChat.leave();
            groupChat = null;

            if (messageEventManager != null) {
                messageEventManager.removeMessageEventNotificationListener(this);
                messageEventManager = null;
            }
        }
        // Close the connection to the server.
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }


    /**
     * Joins the specified room and removes from queue.
     *
     * @param roomName the name of the room to join.
     */
    public void joinRoom(String roomName) {
        // Set the last check now
        lastCheck = System.currentTimeMillis();

        try {
            groupChat = new MultiUserChat(connection, roomName);
            if (name != null) {
                try {
                    AndFilter presenceFilter = new AndFilter(new PacketTypeFilter(Presence.class), new FromContainsFilter(groupChat.getRoom()));
                    connection.addPacketListener(this, presenceFilter);
                    groupChat.join(name);
                }
                // Catch any join exceptions and attempt to join again
                // using an altered name (since exception is most likely
                // due to a conflict of names).
                catch (XMPPException xe) {
                    String conflictName = name + " (Visitor)";
                    groupChat.join(conflictName);
                    nickname = conflictName;
                }
            }
            else {
                nickname = "Visitor";
                groupChat.join(nickname);
            }
            groupChat.addParticipantListener(new PacketListener() {
                public void processPacket(Packet packet) {
                    final Presence p = (Presence)packet;
                    final String from = p.getFrom();
                    String user = StringUtils.parseResource(from);

                    if (p.getType() != Presence.Type.available) {
                        lastAgent = user;
                    }

                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            checkForEmptyRoom();
                        }
                    }, 5000);


                }
            });

            messageEventManager = new MessageEventManager(connection);
            messageEventManager.addMessageEventNotificationListener(this);
        }
        catch (Exception e) {
            WebLog.logError("Error joining room:", e);
        }

        this.roomName = roomName;
        listenForMessages(connection, groupChat);
    }

    private void checkForEmptyRoom() {
        // See if we are the last one in the chat room.
        if (groupChat.getOccupantsCount() == 1) {
            // Leave the room.
            groupChat.leave();
            groupChat = null;
            // Cancel this listener.
            connection.removePacketListener(this);
            // Close the connection.
            connection.disconnect();
            connection = null;
        }
    }

    /**
     * Returns all messages since last request.
     *
     * @return messageList
     */
    public List getMessageList() {
        lastCheck = System.currentTimeMillis();
        return messageList;
    }

    public long getLastCheck() {
        return lastCheck;
    }

    /**
     * MessageEventNotificationListener implementation.
     *
     * @param from     who is delievering the message.
     * @param packetID the id of the packet.
     */
    public void deliveredNotification(String from, String packetID) {
    }

    /**
     * MessageEventNotificationListener implementation.
     *
     * @param from     who is delievering the message.
     * @param packetID the id of the packet.
     */
    public void displayedNotification(String from, String packetID) {
    }

    /**
     * Since only the agent app broadcasts this type of notification, we don't need to worry about
     * who specifically this notification is <em>from</em>.
     *
     * @param from     who is composing the message.
     * @param packetID the id of the packet.
     */
    public void composingNotification(String from, String packetID) {
        composingNotificationsReceived = true;
    }

    public void offlineNotification(String from, String packetID) {
    }

    public void cancelledNotification(String from, String packetID) {
    }

    public void processPacket(Packet packet) {
        final Presence p = (Presence)packet;
        final String from = p.getFrom();
        String user = StringUtils.parseResource(from);

        if (p.getType() == Presence.Type.available) {
            int count = groupChat.getOccupantsCount();
            if (!user.equals(name)) {
                if (count > 2) {
                    ChatMessage message = new ChatMessage(packet);
                    messageList.add(message);
                }
            }
        }
        else {
            if (!user.equals(name)) {
                ChatMessage message = new ChatMessage(packet);
                message.setBody(user + " has left the conversation.");
                messageList.add(message);
            }
        }
    }

    /**
     * Returns a <code>List</code> of all Presence updates within the conversation.
     *
     * @return the <code>List</code> of all Presence updates within the conversation.
     */
    public List getPresenceList() {
        return presenceList;
    }

    /**
     * Notifies the user that an agent has now joined the room. This agent will
     * be the agent who received that Chat Request from the Workgroup.
     *
     * @return the nickname of the agent who has joined the room.
     */
    public String getInitialAgent() {
        String agent = null;

        for (int i = 0; i < 10; i++) {
            if (groupChat != null) {
                Iterator iter = groupChat.getOccupants();
                while (iter.hasNext()) {
                    String occupant = (String)iter.next();
                    String user = StringUtils.parseResource(occupant);
                    if (!user.equals(name)) {
                        agent = user;
                        break;
                    }
                }
                if (agent != null) {
                    break;
                }
                try {
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    WebLog.logError("Problem sleeping thread", e);
                }
            }
        }
        return agent;
    }

    /**
     * Gets the MessageEventManager.
     *
     * @return the MessageEventManager used in this ChatSession.
     */
    public MessageEventManager getMessageEventManager() {
        return messageEventManager;
    }

    /**
     * Returns the current transcript of the current ChatSession.
     *
     * @return the current transcript of the current ChatSession.
     * @see Line
     */
    public Transcript getTranscript() {
        return transcript;
    }

    /**
     * Updates the current transcript.
     *
     * @param from who the message is from.
     * @param body the body of the message.
     */
    public void updateTranscript(String from, String body) {
        transcript.addLine(new Line(from, body));
    }

    /**
     * Returns the nickname used in this chat.
     *
     * @return the nickname used in this chat.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Returns the metadata associated with this chat. The metadata will
     * contain os properties, question, name, email address and other dynamic
     * information.
     *
     * @return the metadata associated with this chat.
     */
    public Map getMetaData() {
        return metadataMap;
    }

    /**
     * Returns the users email address.
     *
     * @return the users email address.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the users email address.
     *
     * @param emailAddress the email address of the user
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Returns the session id for this chat session.
     *
     * @return the session id for this chat session.
     */
    public String getSessionID() {
        return sessionID;
    }


    public void listenForMessages(final XMPPConnection con, MultiUserChat chat) {
        PacketListener packetListener = new PacketListener() {
            public void processPacket(Packet packet) {
                Message message = (Message)packet;
                if (ModelUtil.hasLength(message.getBody())) {
                    ChatMessage chatMessage = new ChatMessage(message);
                    String from = StringUtils.parseResource(message.getFrom());
                    if (from.equalsIgnoreCase(nickname)) {
                        return;
                    }
                    String body = message.getBody();
                    chatMessage.setFrom(from);
                    chatMessage.setBody(body);


                    DelayInformation inf = (DelayInformation)message.getExtension("x", "jabber:x:delay");
                    Date sentDate;
                    if (inf != null) {
                        sentDate = inf.getStamp();
                    }
                    else {
                        sentDate = new Date();
                    }

                    SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yy h:mm");
                    String dateToInsert = "[" + DATE_FORMATTER.format(sentDate) + "] ";
                    chatMessage.setDate(dateToInsert);

                    messageList.add(chatMessage);
                    updateTranscript(chatMessage.getFrom(), chatMessage.getBody());
                }
                else {
                    // Check if cobrowsing
                    ChatMessage me = new ChatMessage(message);
                    messageList.add(me);
                }
            }
        };

        groupChat.addMessageListener(packetListener);
    }

    /**
     * Returns the associated question, if asked.
     *
     * @return the question asked by the visitor.
     */
    public String getQuestion() {
        final Map map = getMetaData();
        String question = "";
        if (map.containsKey(("Question"))) {
            question = "Question: " + (String)map.get("Question");
        }
        return question;
    }

    /**
     * Returns the name of the MultiUserChat room.
     *
     * @return the name of the MultiUserChat room.
     */
    public String getRoomName() {
        return roomName;
    }

    public String getLastAgentInRoom() {
        return lastAgent;
    }
}
