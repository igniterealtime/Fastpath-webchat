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

import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.settings.ConnectionSettings;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.WebLog;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaCollector;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.StandardExtensionElement;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverItems;
import org.jivesoftware.smackx.workgroup.user.Workgroup;
import org.jivesoftware.smackx.xdata.Form;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

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
    public static final Map<String , String> CHANGE_MAP = new HashMap<>();

    public static final Set<WorkgroupChangeListener> listeners = new HashSet<>();

    // Stores the latest workgroup forms.
    private static Map<Jid , Form> workgroupForms = new HashMap<>();

    // Stores all the workgroups.
    private static Map<Jid , Workgroup> workgroups = new HashMap<>();

    private static Map<Jid, Presence> workgroupPresence = new HashMap<>();


    /**
     * Empty Private Constructor.
     */
    private WorkgroupStatus() {

    }


    public static void initStatusListener() {
        ChatManager chatManager = ChatManager.getInstance();

        final StanzaListener presenceListener = new StanzaListener() {
          @Override
          public void processStanza(Stanza packet) {
            if (packet instanceof Presence) {
              Presence p = (Presence) packet;
              WebLog.log("Presence packets : " + p.toXML().toString());
              // TODO not completle clear
              ExtensionElement ext = p.getExtension("workgroup", "http://jivesoftware.com/protocol/workgroup");
              if (ext != null && ext instanceof StandardExtensionElement) {
                String lastModified = ((StandardExtensionElement) ext).getAttributeValue("lastModified");
                if (lastModified != null) {
                  String workgroupName = p.getFrom().getLocalpartOrThrow().toString();
                  String previousDate = (String) CHANGE_MAP.get(workgroupName);
    
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
                    } else {
                      CHANGE_MAP.put(workgroupName, lastModified);
                    }
                  } catch (ParseException e) {
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
     * @param jid the name of the workgroup that has been updated.
     */
    public static void fireWorkgroupChanged(Jid jid) {
        Iterator<WorkgroupChangeListener> iter = new HashSet<>(listeners).iterator();
        while (iter.hasNext()) {
            iter.next().workgroupUpdated(jid);
        }
    }

    /**
     * Checks the availability of the workgroupJid.
     *
     * @param workgroupName the workgroupJid to check for availability.
     * @return true if the workgroupJid is available to accept requests, otherwise returns false.
     */
    public static boolean isOnline(final String workgroupName) {
        // Error check param, return false if there's a problem
      
      Jid workgroupJid;
      try {
        workgroupJid = JidCreate.from(workgroupName);
      } catch (XmppStringprepException e1) {
        WebLog.logError("Workgroup not specified or invalid: \"" + workgroupName + "\"");
        return false;
      }
      
        if (workgroupJid == null) {
            WebLog.logError("Workgroup not specified or invalid: \"" + workgroupJid + "\"");
            return false;
        }
        ChatManager chatManager = ChatManager.getInstance();
        XMPPConnection globalConnection = chatManager.getGlobalConnection();

        Presence presence = workgroupPresence.get(workgroupJid);
        if (presence == null) {
            Workgroup workgroup  = getWorkgroup(workgroupJid);
            boolean isAvailable = false;
            try {
              isAvailable = workgroup.isAvailable();
            } catch (NoResponseException | XMPPErrorException | NotConnectedException | InterruptedException e) {
              WebLog.logError("Workgroup is not available: \"" + workgroupJid + "\" : " + e.getMessage());
              
            }
            presence = new Presence(isAvailable ? Presence.Type.available : Presence.Type.unavailable);
            workgroupPresence.put(workgroupJid, presence);

            // Otherwise
            StanzaFilter fromFilter = FromMatchesFilter.create(workgroupJid);
            StanzaFilter presenceFilter = new StanzaTypeFilter(Presence.class);
            StanzaFilter andFilter = new AndFilter(fromFilter, presenceFilter);

            globalConnection.addAsyncStanzaListener(new StanzaListener() {
                public void processStanza(Stanza packet) {
                    Presence presence = (Presence)packet;
                    workgroupPresence.put(workgroupJid, presence);
                }
            }, andFilter);

            return isAvailable;
        }


        return presence != null && presence.getType() == Presence.Type.available;
    }

    /**
     * Checks the availability of the specified agent.
     *
     * @param agentName the jid of the agent to check.
     * @return true if the agent is available to accept a request.
     */
    public static boolean isAgentOnline(String agentName) {
        ChatManager chatManager = ChatManager.getInstance();
        XMPPConnection globalConnection = chatManager.getGlobalConnection();

        try {
          Jid agentJID = JidCreate.from(agentName);
          Presence directedPresence = new Presence(Presence.Type.available);
          //TODO directedPresence.setProperty("anonymous", true);
          directedPresence.setTo(agentJID);
          StanzaFilter typeFilter = new StanzaTypeFilter(Presence.class);
          StanzaFilter fromFilter = FromMatchesFilter.create(agentJID);
          StanzaCollector collector = globalConnection.createStanzaCollector(new AndFilter(fromFilter,
                  typeFilter));

          globalConnection.sendStanza(directedPresence);

          Presence response = (Presence)collector.nextResult(globalConnection.getReplyTimeout());

          // Cancel the collector.
          collector.cancel();
          if (response == null) {
              return false;
          }
          if (response.getError() != null) {
              return false;
          }
          return Presence.Type.available == response.getType();
        } catch (NotConnectedException | InterruptedException | XmppStringprepException e) {
          WebLog.logError("Agent is not available: \"" + agentName + "\" : " + e.getMessage());
          return false;
        }
    }

    /**
     * Returns the nickname of the user who sent the message.
     *
     * @param message the message sent.
     * @return the nickname of the user who sent the message.
     */
    public static Resourcepart getNickname(Message message) {
        return message.getFrom().getResourceOrNull();
    }

    public static Collection<String> getWorkgroupNames() {
        final List<String> workgroupNames = new ArrayList<>();

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
          Jid jid = JidCreate.from("workgroup." + host);
          
            result = discoManager.discoverItems(jid);
        }
        catch (XMPPException | NoResponseException | NotConnectedException | InterruptedException | XmppStringprepException e) {
          //TODO better throw exception
            return workgroupNames;
        }
        
        for (DiscoverItems.Item item : result.getItems()) {
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

    public static Form getWorkgroupForm(Jid workgroupJid) throws NoResponseException, NotConnectedException, InterruptedException {
        Form form = (Form)workgroupForms.get(workgroupJid);
        if (form == null) {
            try {
                form = getWorkgroup(workgroupJid).getWorkgroupForm();
                workgroupForms.put(workgroupJid, form);
            }
            catch (XMPPException e) {
                e.printStackTrace();
            }
        }
        return form;
    }

    public static Workgroup getWorkgroup(Jid workgroupJid) {
        Workgroup workgroup = (Workgroup)workgroups.get(workgroupJid);
        if (workgroup == null) {
            workgroup = new Workgroup(workgroupJid, ChatManager.getInstance().getGlobalConnection());
            workgroups.put(workgroupJid, workgroup);
        }

        return workgroup;
    }

}
