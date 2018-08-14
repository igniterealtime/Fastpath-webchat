/**
 * $RCSFile$
 * $Revision: 19018 $
 * $Date: 2005-06-09 14:41:08 -0700 (Thu, 09 Jun 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.webchat.util.SettingsManager;
import org.jivesoftware.webchat.util.WebLog;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

/**
 * Used to retrieve images from within an email account. This allows to bypass
 * the issues we have had with retrieving dynamic images in email accounts.
 */
public class DynamicImageServlet extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * Process an image request.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Initialize adminManager
        final String url = request.getRequestURI();
        String string = url.substring(0, url.indexOf("/image.gif"));
        String requestInfo = string.substring(string.lastIndexOf("/") + 1);

        int indexOfUnderline = requestInfo.indexOf("_");
        final String agentName = requestInfo.substring(0, indexOfUnderline);
        final String workgroupName = requestInfo.substring(indexOfUnderline + 1);
        BareJid agentJid = JidCreate.bareFrom(agentName);
        Jid workgroupJid = JidCreate.from(workgroupName);
        getImage(agentJid, workgroupJid, request, response);
    }

    private void getImage(BareJid requestAgent, Jid workgroup, HttpServletRequest request, HttpServletResponse response) {
        ChatManager chatManager = ChatManager.getInstance();

        try {

          boolean isOnline = WorkgroupStatus.isOnline(workgroup.toString());
          final SettingsManager imageManager = SettingsManager.getInstance();
  
          final Roster roster = Roster.getInstanceFor(chatManager.getGlobalConnection());
          final Presence presence = roster.getPresence(requestAgent);
  
          isOnline = isOnline && presence != null && presence.getType() == Presence.Type.available;
  
          if (!isOnline) {
              Object o = roster.getEntry(requestAgent);
              if (o == null) {
                  roster.createEntry(requestAgent, requestAgent.toString(), null);
            }
          }

          byte[] image;
          if (isOnline) {
              image = imageManager.getImage("personalonline", workgroup, getServletContext());
          }
          else {
              image = imageManager.getImage("personaloffline", workgroup, getServletContext());
          }
  
          imageManager.writeBytesToStream(image, response);
        }
        catch (XMPPException | NotLoggedInException | NoResponseException | NotConnectedException | InterruptedException e) {
          //TODO maybe better to throw
           WebLog.logError("Error creating new roster entry:", e);
        }

    }


}