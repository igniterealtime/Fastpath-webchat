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
import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverItems;

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
    public static final Map CHANGE_MAP = new HashMap();

    public static final Set listeners = new HashSet();

    // Stores the latest workgroup forms.
    private static Map workgroupForms = new HashMap();

    // Stores all the workgroups.
    private static Map workgroups = new HashMap();

    private static Map<String, Presence> workgroupPresence = new HashMap<String, Presence>();


    /**
     * Empty Private Constructor.
     */
    private WorkgroupStatus() {

    }


    public static void initStatusListener() {
        ChatManager chatManager = ChatManager.getInstance();

        final PacketListener presenceListener = new PacketListener() {
            public void processPacket(Packet packet) {
                if (packet instanceof Presence) {
                    Presence p = (Presence)packet;
                    DefaultPacketExtension ext = (DefaultPacketExtension)p.getExtension("workgroup", "http://jivesoftware.com/protocol/workgroup");
                    if (ext != null) {
                        String lastModified = ext.getValue("lastModified");
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
            chatManager.getGlobalConnection().addPacketListener(presenceListener, new PacketTypeFilter(Presence.class));
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
     */
    public static boolean isOnline(final String workgroupName) {
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
            boolean isAvailable = workgroup.isAvailable();
            presence = new Presence(isAvailable ? Presence.Type.available : Presence.Type.unavailable);
            workgroupPresence.put(workgroupName, presence);

            // Otherwise
            PacketFilter fromFilter = new FromContainsFilter(workgroupName);
            PacketFilter presenceFilter = new PacketTypeFilter(Presence.class);
            PacketFilter andFilter = new AndFilter(fromFilter, presenceFilter);

            globalConnection.addPacketListener(new PacketListener() {
                public void processPacket(Packet packet) {
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
     */
    public static boolean isAgentOnline(String agentJID) {
        ChatManager chatManager = ChatManager.getInstance();
        XMPPConnection globalConnection = chatManager.getGlobalConnection();

        Presence directedPresence = new Presence(Presence.Type.available);
        directedPresence.setProperty("anonymous", true);
        directedPresence.setTo(agentJID);
        PacketFilter typeFilter = new PacketTypeFilter(Presence.class);
        PacketFilter fromFilter = new FromContainsFilter(agentJID);
        PacketCollector collector = globalConnection.createPacketCollector(new AndFilter(fromFilter,
                typeFilter));

        globalConnection.sendPacket(directedPresence);

        Presence response = (Presence)collector.nextResult(SmackConfiguration.getPacketReplyTimeout());

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
        String from = org.jivesoftware.smack.util.StringUtils.parseResource(message.getFrom());
        return from;
    }

    public static Collection getWorkgroupNames() {
        final List workgroupNames = new ArrayList();

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
        }
        catch (XMPPException e) {
            return workgroupNames;
        }
        Iterator iter = result.getItems();
        while (iter.hasNext()) {
            DiscoverItems.Item item = (DiscoverItems.Item)iter.next();
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

    public static Form getWorkgroupForm(String workgroupName) {
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