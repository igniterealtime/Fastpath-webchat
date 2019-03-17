/**
 * $RCSfile$
 * $Revision: 28143 $
 * $Date: 2006-03-06 20:42:43 -0800 (Mon, 06 Mar 2006) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */

package org.jivesoftware.webchat;

import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.webchat.settings.ChatSettingsManager;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.SettingsManager;
import org.jivesoftware.webchat.util.URLFileSystem;
import org.jivesoftware.webchat.util.WebLog;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Live Assistant servlet.
 *
 * @author Derek DeMoro
 */
public class FastpathServlet extends HttpServlet {
    private Timer timer = new Timer();

    private ChatManager chatManager;

    public static File SETTINGS_FILE;
    public static String BASE_LOCATION;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BASE_LOCATION = config.getServletContext().getRealPath("");

        // Initalize ChatManager
        chatManager = ChatManager.getInstance();

        // Handle init params
        String logging = getServletContext().getInitParameter("logging");
        String chatSettings = getServletContext().getInitParameter("settings");

        if (ModelUtil.hasLength(logging)) {
            try {
                URL url = new URL(logging);
                WebLog.changeLogFile(URLFileSystem.url2File(url));
            }
            catch (MalformedURLException e) {
                System.err.println("The log file location is not a proper URL. Defaulting to internal logging.");
            }
        }

        if (ModelUtil.hasLength(chatSettings)) {
            try {
                URL url = new URL(chatSettings);
                SETTINGS_FILE = URLFileSystem.url2File(url);
            }
            catch (MalformedURLException e) {
                System.err.println("The chat settings location is not a proper URL. Defaulting to WEB-INF dir of web application.");
            }

        }

        // Define settings file location
        if (SETTINGS_FILE == null) {
            String location = config.getServletContext().getRealPath("");
            SETTINGS_FILE = new File(location, "WEB-INF/chat-settings.xml");
        }

        final ChatSettingsManager chatSettingsManager = new ChatSettingsManager(SETTINGS_FILE);

        // Check if a debugger for the Smack connection is required
        String className = config.getInitParameter("smackDebugger");
        if (className != null) {
            try {
                Class.forName(className);
                System.setProperty("smack.debugEnabled", "true");
                System.setProperty("smack.debuggerClass", className);
            }
            catch (ClassNotFoundException e) {
                // Do nothing since the requested debugger class is incorrect or not available
            }
        }

        chatManager.setChatSettingsManager(chatSettingsManager);

        // Connect on Startup
        chatManager.createConnection(getServletContext());

        long minute = 1000 * 20;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!chatManager.isConnected()) {
                    chatManager.createConnection(getServletContext());
                }
            }
        }, minute, minute);
    }

    /**
     * Shut down the servlet by destroying all chat sessions.
     */
    public void destroy() {
        timer.cancel();

        // The servlet is shutting down, so close down all sessions.
        chatManager.destroyAllSessions();

        if (chatManager.isConnected()) {
            // Close XMPPConnection
            chatManager.getGlobalConnection().disconnect();
        }

        super.destroy();
    }

    /**
     * Handle requests from the HTML client. Handles commands for data reading, queue status,
     * data writing, leaving the queue or chat, starting the chat and creating an account.
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!chatManager.isConnected()) {
            chatManager.createConnection(getServletContext());
            if (!chatManager.isConnected()) {
                return;
            }
        }

        SettingsManager imageManager = SettingsManager.getInstance();

        final String action = request.getParameter("action");

        // Check the availability of agents in a particular workgroup.
        if (action.equals("isAvailable")) {
            String workgroup = request.getParameter("workgroup");
            String offline = request.getParameter("offline");
            String online = request.getParameter("online");

            Jid workgroupJid = JidCreate.from(workgroup);
            
            boolean isOnline = WorkgroupStatus.isOnline(workgroup);
            if (ModelUtil.hasLength(offline) && ModelUtil.hasLength(online)) {
                response.sendRedirect(isOnline ? online : offline);
                return;
            }


            byte[] imageBytes = null;
            if (isOnline) {
                imageBytes = imageManager.getImage("online", workgroupJid, getServletContext());
            }
            else {
                imageBytes = imageManager.getImage("offline", workgroupJid, getServletContext());
            }

            imageManager.writeBytesToStream(imageBytes, response);
            return;
        }

        if (action.equals("agentAvailable")) {
            String requestAgent = request.getParameter("requestAgent");
            String workgroup = request.getParameter("workgroup");
            Jid workgroupJid = JidCreate.from(workgroup);
            BareJid requestAgentJid = JidCreate.bareFrom(requestAgent);
            
            boolean isOnline = WorkgroupStatus.isOnline(workgroup);

            final Roster roster =  Roster.getInstanceFor( chatManager.getGlobalConnection());
            final Presence presence = roster.getPresence(requestAgentJid);

            if (isOnline && presence != null && presence.getType() == Presence.Type.available) {
                isOnline = true;
            }
            else {
                isOnline = false;
            }

            byte[] image = null;
            if (isOnline) {
                image = imageManager.getImage("online", workgroupJid, getServletContext());
            }
            else {
                image = imageManager.getImage("offline", workgroupJid, getServletContext());
            }

            imageManager.writeBytesToStream(image, response);
            return;
        }

        if (action.equals("agentCard")) {
            String requestAgent = request.getParameter("requestAgent");
            String workgroup = request.getParameter("workgroup");
            Jid workgroupJid = JidCreate.from(workgroup);
            BareJid requestAgentJid = JidCreate.bareFrom(requestAgent);
            
            boolean isOnline = WorkgroupStatus.isOnline(workgroup);

            final Roster roster =  Roster.getInstanceFor( chatManager.getGlobalConnection());
            final Presence presence = roster.getPresence(requestAgentJid);

            if (isOnline && presence != null && presence.getType() == Presence.Type.available) {
                isOnline = true;
            }
            else {
                isOnline = false;
            }

            if (!isOnline) {
                Object o = roster.getEntry(requestAgentJid);
                if (o == null) {
                    try {
                        roster.createEntry(requestAgentJid, requestAgent, null);
                    }
                    catch (XMPPException | NotLoggedInException | NoResponseException | NotConnectedException | InterruptedException e) {
                        WebLog.logError("Error checking roster", e);
                    }
                }
            }

            byte[] image = null;
            if (isOnline) {
                image = imageManager.getImage("personalonline", workgroupJid, getServletContext());
            }
            else {
                image = imageManager.getImage("personaloffline", workgroupJid, getServletContext());
            }

            imageManager.writeBytesToStream(image, response);
            return;
        }

        // User is leaving the chat or the queue
        if ("close".equals(action) || "depart_queue".equals(action) || "lost_connection".equals(action)) {
            final String sessionID = request.getSession().getId();
            final ChatSession chatSession = chatManager.getChatSession(sessionID);

            if (chatSession != null) {
                chatSession.close();
            }

            chatManager.removeChatSession(sessionID);

            if ("lost_connection".equals(action)) {
                final String path = makePath(request);
                RequestDispatcher rd = request.getRequestDispatcher(path + "lostconnection.jsp");
                rd.forward(request, response);
                return;
            }

            request.getSession().removeAttribute("TRANSCRIPT");
            return;
        }

        // Default - print error message and reflect parameters.
        else {
            StringBuffer msg = new StringBuffer("<b>Fastpath Servlet</b>");
            msg.append("<hr>");
            msg.append("<br>Content Type: ").append(request.getContentType());
            msg.append("<br>Content Encoding: ");
            msg.append(request.getCharacterEncoding()).append("<p>");

            // Write out params
            Enumeration<String> paramEnums = request.getParameterNames();
            msg.append("<b>Parameters:</b><ul>");
            if (paramEnums.hasMoreElements()) {
                while (paramEnums.hasMoreElements()) {
                    String name = paramEnums.nextElement();
                    String value = request.getParameter(name);
                    msg.append("<li>").append(name).append("=").append(value);
                }
                msg.append("</ul>");
            }

            writeData(msg.toString(), response);
        }
    }

    /**
     * convenience method, used ultimately in constructing packet filters.
     * @param p 
     * @return 
     */
    protected String getPacketIDRoot(Stanza p) {
        if (p == null) {
            return null;
        }
        return p.getStanzaId().substring(0, 5);
    }

    // Utility methods //

    /**
     * Writes the given data to the servlet output stream.
     *
     * @param data     the text to write out
     * @param response the servlet output stream.
     */
    protected void writeData(String data, HttpServletResponse response) {
        try {
            PrintWriter responseWriter = response.getWriter();
            response.setContentType("text/html");
            responseWriter.println(data);
            responseWriter.close();
        }
        catch (IOException ioe) {
            // PENDING
        }
    }


    private String makePath(ServletRequest request) {
        String path = ((HttpServletRequest)request).getServletPath();
        path = path.substring(0, path.lastIndexOf('/')) + "/";
        return path;
    }
}
