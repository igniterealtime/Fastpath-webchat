package org.jivesoftware.webchat;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.swing.JFrame;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.MUCUser.Invite;
import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.agent.AgentSession;
import org.jivesoftware.smackx.workgroup.agent.Offer;
import org.jivesoftware.smackx.workgroup.agent.OfferListener;
import org.jivesoftware.smackx.workgroup.agent.RevokedOffer;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;

public class LoadTest {

    private final ChatManager sm = ChatManager.getInstance();
    private static String SERVER = "thor";

    public LoadTest() {
        connectAsAgent();

        int i = 0;
        for (int j = 0; j < 1000; j++) {
            try {
                Iterator<ChatSession> iter = sm.getChatSessions().iterator();
                while (iter.hasNext()) {
                    ChatSession session = iter.next();
                    if (session.isInGroupChat()) {
                        session.getGroupChat().sendMessage("Howdy");
                    }
                    //session.close();
                }

                Thread.sleep(500);

                i++;

                Jid jid = JidCreate.from("dd");
                Resourcepart res = Resourcepart.from("DON JUAN");
                ChatSession chatSession = new ChatSession(SERVER, -1, false, jid , res , "don@juan.com", "ddfdfd");
                // Gather all meta-data.
                final Map<String, Object> metaData = new HashMap<>();
                metaData.put("Name", "DON");

                metaData.put("User Agent", "Mozilla");

                // Check to see if there is a referer.
                metaData.put("Referer", "http://www.google.com");
                metaData.put("IP Address", "127.0.0.1");
                metaData.put("Location", "unknown");
                metaData.put("Question", "How do i do it?");

                metaData.put("Product", "Jive Live Assistant");
                // Hard coded for demo
                TimeZone GMT = TimeZone.getTimeZone("GMT");
                Calendar c_GMT = Calendar.getInstance(GMT);
                Date userDate = c_GMT.getTime();

                metaData.put("Time Entered Queue", userDate);
                metaData.put("Email Addresss", "derek@thor");
                metaData.put("la_userid", "user");

                sm.addChatSession("" + i, chatSession);
                chatSession.loginAnonymously();
                Jid agentJid = JidCreate.from("demo@workgroup."+SERVER);
                chatSession.joinQueue(agentJid, metaData);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        if(args.length == 0){
            System.err.println("You must specify the name of the server to connect to.");
            System.exit(1);
        }
        SERVER = args[0];

        new LoadTest();
        
        new JFrame().setVisible(true);
    }

    private void connectAsAgent() {
        try {
            final XMPPTCPConnection con = new XMPPTCPConnection("jive", "user",SERVER);
            con.login("jive", "user", Resourcepart.from("demo"));

            Jid agentJid = JidCreate.from("demo@workgroup."+SERVER);
            // Notify workgroup that the agent is here
            AgentSession agentSession = new AgentSession(agentJid, con);
            agentSession.setOnline(true);
            agentSession.setStatus(Presence.Mode.available, 10, "available");


            con.addConnectionListener(new ConnectionListener() {
              @Override
              public void connectionClosed() {
                System.out.println("Connection is closed");
              }
      
              @Override
              public void connectionClosedOnError(Exception e) {
                System.out.println("Connection closed with an error: " + e);
              }
      
              @Override
              public void reconnectingIn(int i) {
              }
      
              @Override
              public void reconnectionSuccessful() {
              }
      
              @Override
              public void reconnectionFailed(Exception exception) {
              }
      
              @Override
              public void connected(XMPPConnection connection) {
                System.out.println("connected");
      
              }
      
              @Override
              public void authenticated(XMPPConnection connection, boolean resumed) {
                System.out.println("authenticated");
      
              }
            });

            // Add Offer Listener
            agentSession.addOfferListener(new OfferListener() {
                public void offerReceived(Offer offer) {
                    try {
                      offer.accept();
                      System.out.println("offer accepted " + offer.getSessionID());
                    } catch (NotConnectedException | InterruptedException e) {
                      System.out.println("offer not accepted " + e.getLocalizedMessage());
                      
                    }
                }

                public void offerRevoked(RevokedOffer revokedOffer) {
                    System.out.println("Offer was revoked: " + revokedOffer.getReason());
                }
            });

            // Listen for Room Invitation from Server
            MultiUserChatManager.getInstanceFor(con).addInvitationListener(new InvitationListener() {
              @Override
              public void invitationReceived(XMPPConnection conn, MultiUserChat groupChat, EntityJid inviter, String reason, String password,
                  Message message, Invite invitation) {

                    Map<String, List<String>>  metaData = new HashMap<>();
                    MetaData metaDataExt = (MetaData)message.getExtension(MetaData.ELEMENT_NAME, MetaData.NAMESPACE);
                    if (metaDataExt != null) {
                        metaData = metaDataExt.getMetaData();
                    }
                    System.out.println("Creating room for sessionID " + reason);
                    metaData.put("inviter", Collections.singletonList(inviter.toString()));

                    //MultiUserChat groupChat = MultiUserChatManager.getInstanceFor(connection) new MultiUserChat(con, room);
                    groupChat.addMessageListener(new MessageListener() {
                        public void processMessage(Message packet) {
                            System.out.println(packet.getBody());
                        }
                    });
                }

//                @Override
//                public void invitationReceived(XMPPConnection conn, String room, String inviter, String reason, String password, Message message) {
//                    throw new UnsupportedOperationException();
//                }

            });

        }
        catch (XMPPException | SmackException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
