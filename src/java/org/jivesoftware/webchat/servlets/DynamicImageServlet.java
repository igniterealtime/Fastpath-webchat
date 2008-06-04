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

import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.webchat.util.SettingsManager;
import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

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
        getImage(agentName, workgroupName, request, response);
    }

    private void getImage(String requestAgent, String workgroup, HttpServletRequest request, HttpServletResponse response) {
        ChatManager chatManager = ChatManager.getInstance();

        boolean isOnline = WorkgroupStatus.isOnline(workgroup);
        final SettingsManager imageManager = SettingsManager.getInstance();

        final Roster roster = chatManager.getGlobalConnection().getRoster();
        final Presence presence = roster.getPresence(requestAgent);

        isOnline = isOnline && presence != null && presence.getType() == Presence.Type.available;

        if (!isOnline) {
            Object o = roster.getEntry(requestAgent);
            if (o == null) {
                try {
                    roster.createEntry(requestAgent, requestAgent, null);
                }
                catch (XMPPException e) {
                   WebLog.logError("Error creating new roster entry:", e);
                }
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


}