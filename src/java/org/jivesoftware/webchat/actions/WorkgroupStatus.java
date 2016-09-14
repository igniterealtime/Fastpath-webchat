/**
 * $RCSFile$
 * $Revision: 24191 $
 * $Date: 2005-11-28 20:16:08 -0800 (Mon, 28 Nov 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.actions;

import org.jivesoftware.smackx.workgroup.user.Workgroup;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.settings.ConnectionSettings;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.StringUtils;
import org.jivesoftware.webchat.util.WebLog;
import org.jxmpp.util.XmppStringUtils;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.workgroup.ext.forms.WorkgroupForm;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.disco.packet.DiscoverItems.Item;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Checks availability of Workgroups and Agents. Use WorkgroupStatus if you wish
 * to discover the presence of a workgroup or an agent within the Workgroup.
 */
public final class WorkgroupStatus {
    public static final Map<String , String> CHANGE_MAP = new HashMap<String , String>();

    public static final Set<WorkgroupChangeListener> listeners = new HashSet<WorkgroupChangeListener>();

    // Stores the latest workgroup forms.
    private static Map<String , Form> workgroupForms = new HashMap<String , Form>();

    // Stores all the workgroups.
    private static Map<String , Workgroup> workgroups = new HashMap<String , Workgroup>();

    private static Map<String, Presence> workgroupPresence = new HashMap<String, Presence>();


    /**
     * Empty Private Constructor.
     */
    private WorkgroupStatus() {

    }


    public static void initStatusListener() {
        ChatManager chatManager = ChatManager.getInstance();

        final StanzaListener presenceListener = new StanzaListener() {
			
			@Override
			public void processPacket(Stanza packet) throws NotConnectedException {
				// TODO Auto-generated method stub
				if (packet instanceof Presence) {
                    Presence p = (Presence)packet;
                    WebLog.log("Presence packets : "+p.toXML().toString());
                    ExtensionElement ext = p.getExtension("workgroup", "http://jivesoftware.com/protocol/workgroup");
                    if (ext != null && ext instanceof DefaultExtensionElement) {
                        String lastModified = ((DefaultExtensionElement)ext).getValue("lastModified");
                        if (lastModified != null) {
                            String workgroupName = StringUtils.parseName(p.getFrom());
                            String previousDate = (String)CHANGE_MAP.get(workgroupName);

                            final SimpleDateFormat UTC_FORMAT = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
                            UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+0"));

                            try {
                                Date newDate = UTC_FORMAT.parse(lastModified);

                                if (ModelUtil.hasLength(previousDate)) {
                                    Date oldDate = UTC_FORMAT.parse(previousDate);
                                    if (newDate.getTime() > oldDate.getTime()) {
                                        CHANGE_MAP.put(workgroupName, lastModified);
                                        fireWorkgroupChanged(p.getFrom());
                                        workgroupForms.remove(p.getFrom());
                                    }
                                }
                                else {
                                    CHANGE_MAP.put(workgroupName, lastModified);
                                }
                            }
                            catch (ParseException e) {
                                WebLog.logError("Error processing workgroup packet.", e);
                            }
                        }
                    }
                }
			}
		};

        if (chatManager.getGlobalConnection() == null || !chatManager.getGlobalConnection().isConnected()) {
            ConnectionSettings settings = chatManager.getChatSettingsManager().getSettings();
            WebLog.logError("A connection to the server could not be made when attempting connection to " + settings.getServerDomain());
        }
        else {
        	chatManager.getGlobalConnection().addAsyncStanzaListener(presenceListener, new StanzaTypeFilter(Presence.class));
            workgroups.clear();
            workgroupForms.clear();
            workgroupPresence.clear();
        }
    }

    /**
     * Adds a WorkgroupChangeListener. WorkgroupChangeListener is used to allow notification of workgroup
     * updates.
     *
     * @param listener the WorkgroupChangeListener to add.
     */
    public static void addWorkgroupChangeListener(WorkgroupChangeListener listener) {
    	listeners.add(listener);
    }

    /**
     * Notify the WorkgroupChangeListeners that a workgroup has been updated.
     *
     * @param nameOfWorkgroup the name of the workgroup that has been updated.
     */
    public static void fireWorkgroupChanged(String nameOfWorkgroup) {
        Iterator iter = new HashSet(listeners).iterator();
        while (iter.hasNext()) {
            ((WorkgroupChangeListener)iter.next()).workgroupUpdated(nameOfWorkgroup);
        }
    }

    /**
     * Checks the availability of the workgroupName.
     *
     * @param workgroupName the workgroupName to check for availability.
     * @return true if the workgroupName is available to accept requests, otherwise returns false.
     * @throws NotConnectedException 
     * @throws XMPPErrorException 
     * @throws NoResponseException 
     */
    public static boolean isOnline(final String workgroupName){
        // Error check param, return false if there's a problem
        if (!ModelUtil.hasLength(workgroupName) || workgroupName.indexOf('@') == -1) {
            WebLog.logError("Workgroup not specified or invalid: \"" + workgroupName + "\"");
            return false;
        }
        ChatManager chatManager = ChatManager.getInstance();
        XMPPConnection globalConnection = chatManager.getGlobalConnection();
        
        Presence presence = workgroupPresence.get(workgroupName);
        if (presence == null) {
            Workgroup workgroup  = getWorkgroup(workgroupName);
            boolean isAvailable = false;
			try {
				isAvailable = workgroup.isAvailable();
			} catch (NoResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XMPPErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            presence = new Presence(isAvailable ? Presence.Type.available : Presence.Type.unavailable);
            workgroupPresence.put(workgroupName, presence);

            // Otherwise
            StanzaFilter fromFilter = FromMatchesFilter.create(workgroupName);
            StanzaFilter presenceFilter = new StanzaTypeFilter(Presence.class);
            StanzaFilter andFilter = new AndFilter(fromFilter, presenceFilter);

            globalConnection.addAsyncStanzaListener(new StanzaListener() {
                public void processPacket(Stanza packet) {
                    Presence presence = (Presence)packet;
                    workgroupPresence.put(workgroupName, presence);
                }
            }, andFilter);

            return isAvailable;
        }


        return presence != null && presence.getType() == Presence.Type.available;
    }

    /**
     * Checks the availability of the specified agent.
     *
     * @param agentJID the jid of the agent to check.
     * @return true if the agent is available to accept a request.
     * @throws NotConnectedException 
     */
    public static boolean isAgentOnline(String agentJID) throws NotConnectedException {
        ChatManager chatManager = ChatManager.getInstance();
        XMPPConnection globalConnection = chatManager.getGlobalConnection();

        Presence directedPresence = new Presence(Presence.Type.available);
//      directedPresence.setProperty("anonymous", true);
        JivePropertiesManager.addProperty(directedPresence, "anonymous", true);
        directedPresence.setTo(agentJID);
        StanzaFilter typeFilter = new StanzaTypeFilter(Presence.class);
//      PacketFilter fromFilter = new FromContainsFilter(agentJID);
        StanzaFilter fromFilter = FromMatchesFilter.create(agentJID);
        PacketCollector collector = globalConnection.createPacketCollector(new AndFilter(fromFilter , typeFilter));

        globalConnection.sendStanza(directedPresence);

        Presence response = (Presence)collector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());

        // Cancel the collector.
        collector.cancel();
        if (response == null) {
            return false;
        }
        if (response.getError() != null) {
            return false;
        }
        return Presence.Type.available == response.getType();
    }

    /**
     * Returns the nickname of the user who sent the message.
     *
     * @param message the message sent.
     * @return the nickname of the user who sent the message.
     */
    public static String getNickname(Message message) {
        String from = XmppStringUtils.parseResource(message.getFrom());
        return from;
    }

    public static Collection<String> getWorkgroupNames() throws NoResponseException, NotConnectedException {
        final List<String> workgroupNames = new ArrayList<String>();

        ChatManager chatManager = ChatManager.getInstance();
        ConnectionSettings connectionSettings = chatManager.getChatSettingsManager().getSettings();
        if (connectionSettings == null) {
            return workgroupNames;
        }
        String host = connectionSettings.getServerDomain();
        XMPPConnection con = chatManager.getGlobalConnection();
        if (con == null) {
            return workgroupNames;
        }
        ServiceDiscoveryManager discoManager = ServiceDiscoveryManager.getInstanceFor(con);
        DiscoverItems result = null;
        
        try {
			result = discoManager.discoverItems("workgroup." + host);
		} catch (XMPPErrorException e) {
			// TODO Auto-generated catch block
			return workgroupNames;
		}
        
        List<Item> items =  result.getItems();
        for (Item item : items) {
            String workgroupName = item.getName();
            workgroupNames.add(workgroupName);
        }
        return workgroupNames;
    }

    public static String getHost() {
        ChatManager chatManager = ChatManager.getInstance();
        ConnectionSettings connectionSettings = chatManager.getChatSettingsManager().getSettings();
        if (connectionSettings == null) {
            return null;
        }
        return connectionSettings.getServerDomain();
    }

    public static Form getWorkgroupForm(String workgroupName) throws NoResponseException, NotConnectedException {
    	ProviderManager.addIQProvider(WorkgroupForm.ELEMENT_NAME, WorkgroupForm.NAMESPACE, new WorkgroupForm.InternalProvider());
    	Form form = (Form)workgroupForms.get(workgroupName);
        if (form == null) {
            try {
                form = getWorkgroup(workgroupName).getWorkgroupForm();
                workgroupForms.put(workgroupName, form);
            }
            catch (XMPPException e) {
                e.printStackTrace();
            }
        }
        return form;
    }

    public static Workgroup getWorkgroup(String workgroupName) {
        Workgroup workgroup = (Workgroup)workgroups.get(workgroupName);
        if (workgroup == null) {
            workgroup = new Workgroup(workgroupName, ChatManager.getInstance().getGlobalConnection());
            workgroups.put(workgroupName, workgroup);
        }
        
        return workgroup;
    }

}