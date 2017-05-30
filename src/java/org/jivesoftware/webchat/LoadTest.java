package org.jivesoftware.webchat;

import org.jivesoftware.smackx.workgroup.MetaData;
import org.jivesoftware.smackx.workgroup.agent.AgentSession;
import org.jivesoftware.smackx.workgroup.agent.Offer;
import org.jivesoftware.smackx.workgroup.agent.OfferListener;
import org.jivesoftware.smackx.workgroup.agent.RevokedOffer;
import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import javax.swing.JFrame;

import java.io.IOException;
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
                    	if (session.getGroupChat() != null){
                    		MultiUserChat chat = session.getGroupChat();
                    		chat.sendMessage("Howdy");
                    	}
                        
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
        	
        	XMPPTCPConnectionConfiguration.Builder xmppConfig =  XMPPTCPConnectionConfiguration.builder()
            		.setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)
            		.setCompressionEnabled(true)
            		.setServiceName(SERVER)
            		.setHost(SERVER)
            		.setPort(5222);
            XMPPTCPConnection con = new XMPPTCPConnection(xmppConfig.build());
            try {
				con.login("jive", "user", "demo");
			} catch (SmackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            // Notify workgroup that the agent is here
            AgentSession agentSession = new AgentSession("demo@workgroup."+SERVER, con);
            try {
				agentSession.setOnline(true);
			} catch (SmackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            try {
				agentSession.setStatus(Presence.Mode.available, 10, "available");
			} catch (NoResponseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NotConnectedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}


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

				@Override
				public void connected(XMPPConnection connection) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void authenticated(XMPPConnection connection, boolean resumed) {
					// TODO Auto-generated method stub
					
				}
            });

            // Add Offer Listener
            agentSession.addOfferListener(new OfferListener() {
                public void offerReceived(Offer offer) {
                    try {
						offer.accept();
					} catch (NotConnectedException e) {
						WebLog.logError("Error accepting offer", e);
					}
                    System.out.println("offer accepted " + offer.getSessionID());
                }

                public void offerRevoked(RevokedOffer revokedOffer) {
                    System.out.println("Offer was revoked: " + revokedOffer.getReason());
                }
            });
            
            // Listen for Room Invitation from Server
            MultiUserChatManager.getInstanceFor(con).addInvitationListener(new InvitationListener() {
				
				@Override
				public void invitationReceived(XMPPConnection conn, MultiUserChat room, String inviter, String reason,
						String password, Message message) {
					Map metaData = new HashMap();
                    MetaData metaDataExt = (MetaData)message.getExtension(MetaData.ELEMENT_NAME, MetaData.NAMESPACE);
                    if (metaDataExt != null) {
                        metaData = metaDataExt.getMetaData();
                    }
                    System.out.println("Creating room for sessionID " + reason);
                    metaData.put("inviter", inviter);

                    room.addMessageListener(new MessageListener() {
						
						@Override
						public void processMessage(Message message) {
							// TODO Auto-generated method stub
							System.out.println(message.getBody());
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
