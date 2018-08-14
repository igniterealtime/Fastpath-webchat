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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PresenceListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitation;
import org.jivesoftware.smackx.workgroup.WorkgroupInvitationListener;
import org.jivesoftware.smackx.workgroup.user.Workgroup;
import org.jivesoftware.smackx.xevent.MessageEventManager;
import org.jivesoftware.smackx.xevent.MessageEventNotificationListener;
import org.jivesoftware.webchat.history.Line;
import org.jivesoftware.webchat.history.Transcript;
import org.jivesoftware.webchat.personal.ChatMessage;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.WebLog;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

/**
 * The <code>ChatSession</code> class is responsible for handling all aspects of a chat. Connections, Joining
 * Queues and Joining Rooms all take place within <code>ChatSession</code>.
 *
 * @author Derek DeMoro
 */
public class ChatSession implements MessageEventNotificationListener, StanzaListener {
  
  private static Logger logger = Logger.getLogger(ChatSession.class.getName());
  
    private XMPPTCPConnection connection;
    private Workgroup workgroup;
    private MultiUserChat groupChat;

    private Resourcepart name;
    private List<String> presenceList = new ArrayList<>();

    private MessageEventManager messageEventManager;
    private boolean composingNotificationsReceived;
    private Map<String, Object> metadataMap;


    private Transcript transcript;

    private Resourcepart nickname;

    private String emailAddress;

    private Jid userid;

    private String sessionID;

    private List<ChatMessage> messageList = new ArrayList<>();

    /**
     * The time in milliseconds when the browser last checked for new messages.
     */
    private long lastCheck;

    /**
     * Flag that indicates that an inactivity warning has been send. This should be used to prevent multiple warnings
     * to be generated in the same period of inactivity. It is therefor reset whenever activity from the browser is
     * detected (OF-508).
     */
    private boolean inactivityWarningSent;

    final private long createdTimestamp;
    
    private EntityBareJid roomName;

    private Resourcepart lastAgent;

    private int port;
    private String host;

    /**
     * Creates a new <code>ChatSession</code>.`
     *
     * @param host         the host to connect to.
     * @param port         the port to connect through.
     * @param useSSL       true if we should SSL with the Connection.
     * @param userid 
     * @param nickname     the nickname to use during this chat session.
     * @param emailAddress the email address of the user.
     * @param sessionID 
     * @throws XMPPException
     */
    public ChatSession(String host, int port, boolean useSSL, Jid userid, Resourcepart nickname, String emailAddress, String sessionID) throws XMPPException {
        this.sessionID = sessionID;

        this.host = host;
        this.port = port;

        transcript = new Transcript();

        this.nickname = nickname;

        this.emailAddress = emailAddress;

        this.userid = userid;
        
        this.createdTimestamp = System.currentTimeMillis();
    }

    private boolean connect( boolean anonymous ) throws SmackException, IOException, XMPPException, InterruptedException {
      
      XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration.builder()
           .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
           .setXmppDomain(host)
           .setHost(host)
           .setPort(port);
      
      if ( anonymous ) {
        config.performSaslAnonymousAuthentication();
      }
      
      
        connection = new XMPPTCPConnection(config.build());
        
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

            @Override
            public void connected(XMPPConnection connection) {
            }

            @Override
            public void authenticated(XMPPConnection connection, boolean resumed) {
            }
        });

        return true;
    }

    /**
     * Returns the current connection.
     *
     * @return the current connection used by the user.
     */
    public XMPPTCPConnection getConnection() {
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
     * @throws InterruptedException 
     * @throws IOException 
     * @throws SmackException 
     * @throws XMPPException
     * @throws FastPathException 
     */
    public void loginAnonymously() throws XMPPException, SmackException, IOException, InterruptedException, FastPathException  {
        boolean connected = connect(true);
        if (connected) {
            connection.login();
        }
        else {
            throw new FastPathException("Unable to connect to the server at this time.");
        }
    }

    /**
     * Login using the specified account information.
     *
     * @param username the username
     * @param password the password
     * 
     * @throws FastPathException 
     * @throws InterruptedException 
     * @throws IOException 
     * @throws SmackException 
     * @throws XmppStringprepException 
     * @throws XMPPException
     */
    public void login(String username, String password) throws FastPathException, XmppStringprepException, XMPPException, SmackException, IOException, InterruptedException {
        boolean connected = connect(false);
        if (connected) {
            connection.login(username, password, Resourcepart.from("Live Assistant Web Client"));
            name = Resourcepart.from(username);
        }
        else {
            throw new FastPathException("Unable to connect to the server at this time.");
        }
    }

    /**
     * Joins a queue of the specified workgroup with the associated metadata.
     *
     * @param workgroupName the name of the workgroup to join.
     * @param metaData      the metadata associated with this request.
     * @throws XMPPException if an error occurs.
     * @throws XmppStringprepException 
     * @throws InterruptedException 
     * @throws SmackException 
     */
    public void joinQueue(Jid workgroupName, Map<String, Object> metaData) throws XMPPException, XmppStringprepException, SmackException, InterruptedException {
        // Never have null values in metadata
        if (metaData.containsValue(null)) {
            WebLog.logError("You cannot have null values in the Metadata.");
            return;
        }
        workgroup = new Workgroup(workgroupName, connection);

        workgroup.addInvitationListener(new WorkgroupInvitationListener() {
            public void invitationReceived(WorkgroupInvitation workgroupInvitation) {
                EntityBareJid room = workgroupInvitation.getGroupChatName().asEntityBareJidIfPossible();
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
            name = Resourcepart.from((String)metaData.get("username"));
        }

        if (name == null) {
            name = Resourcepart.from("Visitor");
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
            workgroup.joinQueue(getMetaData(), userid);
        } catch (Exception ex) {
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
     * Check to see if the <code>XMPPTCPConnection</code> has been closed.
     *
     * @return true if the <code>XMPPTCPConnection</code> has been closed.
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
            catch (XMPPException | NoResponseException | NotConnectedException | InterruptedException xe) {
                WebLog.logError("Error closing ChatSession:", xe);
            }
        }
        // If we've already been routed and are in a chat, leave it.
        if (groupChat != null) {
            try {
              groupChat.leave();
            } catch (NotConnectedException | InterruptedException e) {
              WebLog.logError("Error closing ChatSession:", e);
              
            }
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
    public void joinRoom(EntityBareJid roomName) {
        // Set the last check now
        lastCheck = System.currentTimeMillis();

        try {
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
            groupChat = manager.getMultiUserChat(roomName);
            if (name != null) {
                try {
                    AndFilter presenceFilter = new AndFilter(new StanzaTypeFilter(Presence.class), FromMatchesFilter.create(groupChat.getRoom()));
                    connection.addAsyncStanzaListener(this, presenceFilter);
                    groupChat.join(name);
                }
                // Catch any join exceptions and attempt to join again
                // using an altered name (since exception is most likely
                // due to a conflict of names).
                catch (XMPPException xe) {
                    Resourcepart conflictName = Resourcepart.from(name + " (Visitor)");
                    groupChat.join(conflictName);
                    nickname = conflictName;
                }
            }
            else {
                nickname = Resourcepart.from("Visitor");
                groupChat.join(nickname);
            }
            groupChat.addParticipantListener(new PresenceListener() {
              
              @Override
              public void processPresence(Presence presence) {

                final Jid from = presence.getFrom();
                Resourcepart user = from.getResourceOrEmpty();
                
                if (presence.getType() != Presence.Type.available) {
                  lastAgent = user;
                }

                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            try {
                              checkForEmptyRoom();
                            } catch (NotConnectedException | InterruptedException e) {
                              logger.fine("checkForEmptyRoom:" + e.getMessage());
                              
                            }
                        }
                    }, 5000);


                }
            });

            messageEventManager = MessageEventManager.getInstanceFor(connection);
            messageEventManager.addMessageEventNotificationListener(this);
        }
        catch (Exception e) {
            WebLog.logError("Error joining room:", e);
        }

        this.roomName = roomName;
        listenForMessages(connection, groupChat);
    }

    private void checkForEmptyRoom() throws NotConnectedException, InterruptedException {
        // See if we are the last one in the chat room.
        if (groupChat.getOccupantsCount() == 1) {
            // Leave the room.
            groupChat.leave();
            groupChat = null;
            // Cancel this listener.
            connection.removeAsyncStanzaListener(this);
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
    public List<ChatMessage> getMessageList() {
        lastCheck = System.currentTimeMillis();
        inactivityWarningSent = false; // OF-508: reset the flag that determines if inactivity warnings are to be send.
        return messageList;
    }

    /**
     * Returns The time in milliseconds when the browser last checked for new messages.
     *
     * @return timestamp of last message retrieval by browser.
     */
    public long getLastCheck() {
        return lastCheck;
    }

    /**
     * Returns if an inactivity warning has been send for the current period of inactivity (if any). This should be used
     * to prevent multiple warnings to be generated in the same period of inactivity. It is therefor reset whenever
     * activity from the browser is detected (OF-508).
     *
     * @return <tt>true</tt> if an inactivity warning flag has been set, <tt>false</tt> otherwise.
     */
    public boolean isInactivityWarningSent() {
        return inactivityWarningSent;
    }

    public void setInactivityWarningSent(boolean inactivityWarningSent) {
        this.inactivityWarningSent = inactivityWarningSent;
    }

    /**
     * MessageEventNotificationListener implementation.
     *
     * @param from     who is delievering the message.
     * @param packetID the id of the packet.
     */
    @Override
    public void deliveredNotification(Jid from, String packetID) {
    }

    /**
     * MessageEventNotificationListener implementation.
     *
     * @param from     who is delievering the message.
     * @param packetID the id of the packet.
     */
    @Override
    public void displayedNotification(Jid from, String packetID) {
    }

    /**
     * Since only the agent app broadcasts this type of notification, we don't need to worry about
     * who specifically this notification is <em>from</em>.
     *
     * @param from     who is composing the message.
     * @param packetID the id of the packet.
     */
    @Override
    public void composingNotification(Jid from, String packetID) {
        composingNotificationsReceived = true;
    }

    @Override
    public void offlineNotification(Jid from, String packetID) {
    }

    @Override
    public void cancelledNotification(Jid from, String packetID) {
    }

    @Override
    public void processStanza(Stanza packet) {
        final Presence p = (Presence)packet;
        final Jid from = p.getFrom();
        Resourcepart user = from.getResourceOrEmpty();

        if (p.getType() == Presence.Type.available) {
            int count = groupChat==null?0:groupChat.getOccupantsCount();
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
    public List<String> getPresenceList() {
        return presenceList;
    }

    /**
     * Notifies the user that an agent has now joined the room. This agent will
     * be the agent who received that Chat Request from the Workgroup.
     *
     * @return the nickname of the agent who has joined the room.
     */
    public Resourcepart getInitialAgent() {
      Resourcepart agent = null;

        for (int i = 0; i < 10; i++) {
            if (groupChat != null) {
                for( EntityFullJid occupant : groupChat.getOccupants() ) {
                    Resourcepart user = occupant.getResourceOrEmpty();
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
    public Resourcepart getNickname() {
        return nickname;
    }

    /**
     * Returns the metadata associated with this chat. The metadata will
     * contain os properties, question, name, email address and other dynamic
     * information.
     *
     * @return the metadata associated with this chat.
     */
    public Map<String, Object> getMetaData() {
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


    public void listenForMessages(final XMPPTCPConnection con, MultiUserChat chat) {
        MessageListener packetListener = new MessageListener() {
            public void processMessage(Message message) {
                if (ModelUtil.hasLength(message.getBody())) {
                    ChatMessage chatMessage = new ChatMessage(message);
                    Resourcepart from = message.getFrom().getResourceOrEmpty();
                    
                    if (from.equals(nickname)) {
                        return;
                    }
                    String body = message.getBody();
                    chatMessage.setFrom(from.toString());
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
        final Map<String, Object> map = getMetaData();
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
    public EntityBareJid getRoomName() {
        return roomName;
    }

    public Resourcepart getLastAgentInRoom() {
        return lastAgent;
    }

	public long getCreatedTimestamp() {
        return createdTimestamp;
    }
}
