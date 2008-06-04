package org.jivesoftware.webchat;

import com.jivesoftware.smack.workgroup.MetaData;
import com.jivesoftware.smack.workgroup.agent.AgentSession;
import com.jivesoftware.smack.workgroup.agent.Offer;
import com.jivesoftware.smack.workgroup.agent.OfferListener;
import com.jivesoftware.smack.workgroup.agent.RevokedOffer;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import javax.swing.JFrame;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

public class LoadTest {

    private final ChatManager sm = ChatManager.getInstance();
    private static String SERVER = "thor";

    public LoadTest() {
        connectAsAgent();

        int i = 0;
        for (int j = 0; j < 1000; j++) {
            try {
                Iterator iter = sm.getChatSessions().iterator();
                while (iter.hasNext()) {
                    ChatSession session = (ChatSession)iter.next();
                    if (session.isInGroupChat()) {
                        session.getGroupChat().sendMessage("Howdy");
                    }
                    //session.close();
                }

                Thread.sleep(500);

                i++;


                ChatSession chatSession = new ChatSession(SERVER, -1, false, "dd", "DON JUAN", "don@juan.com", "ddfdfd");
                // Gather all meta-data.
                final Map metaData = new HashMap();
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
                chatSession.joinQueue("demo@workgroup."+SERVER, metaData);
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
            final XMPPConnection con = new XMPPConnection(SERVER);
            con.login("jive", "user", "demo");

            // Notify workgroup that the agent is here
            AgentSession agentSession = new AgentSession("demo@workgroup."+SERVER, con);
            agentSession.setOnline(true);
            agentSession.setStatus(Presence.Mode.available, 10, "available");


            con.addConnectionListener(new ConnectionListener() {
                public void connectionClosed() {
                    System.out.println("Connection is closed");
                }

                public void connectionClosedOnError(Exception e) {
                    System.out.println("Connection closed with an error: " + e);
                }


                public void reconnectingIn(int i) {
                }

                public void reconnectionSuccessful() {
                }

                public void reconnectionFailed(Exception exception) {
                }
            });

            // Add Offer Listener
            agentSession.addOfferListener(new OfferListener() {
                public void offerReceived(Offer offer) {
                    offer.accept();
                    System.out.println("offer accepted " + offer.getSessionID());
                }

                public void offerRevoked(RevokedOffer revokedOffer) {
                    System.out.println("Offer was revoked: " + revokedOffer.getReason());
                }
            });

            // Listen for Room Invitation from Server
            MultiUserChat.addInvitationListener(con, new InvitationListener() {
                public void invitationReceived(XMPPConnection conn, String room, String inviter, String reason, String password, Message message) {

                    Map metaData = new HashMap();
                    MetaData metaDataExt = (MetaData)message.getExtension(MetaData.ELEMENT_NAME, MetaData.NAMESPACE);
                    if (metaDataExt != null) {
                        metaData = metaDataExt.getMetaData();
                    }
                    System.out.println("Creating room for sessionID " + reason);
                    metaData.put("inviter", inviter);

                    MultiUserChat groupChat = new MultiUserChat(con, room);
                    groupChat.addMessageListener(new PacketListener() {
                        public void processPacket(Packet packet) {
                            System.out.println(((Message)packet).getBody());
                        }
                    });
                }
            });

        }
        catch (XMPPException e) {
            e.printStackTrace();
        }
    }

}
