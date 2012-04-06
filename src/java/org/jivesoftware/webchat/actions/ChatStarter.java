/**
 * $RCSFile$
 * $Revision: 30207 $
 * $Date: 2006-05-15 08:56:15 -0700 (Mon, 15 May 2006) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.actions;

import org.jivesoftware.smackx.workgroup.settings.WorkgroupProperties;
import org.jivesoftware.smackx.workgroup.user.Workgroup;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.ChatSession;
import org.jivesoftware.webchat.providers.MetaDataProvider;
import org.jivesoftware.webchat.settings.ChatSettingsManager;
import org.jivesoftware.webchat.settings.ConnectionSettings;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.StringUtils;
import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

/**
 * Puts users into the ChatQueue to await assistance. The ChatStarter is used
 * to join a queue using the specified metadata.
 */
public class ChatStarter extends WebBean {
    private final ChatManager chatManager = ChatManager.getInstance();

    /**
     * Empty constructor for JavaBean.
     */
    public ChatStarter() {
    }

    /**
     * Joins a queue with associated metadata.
     */
    public void startSession(String workgroup, String chatID) {
        ChatSession chatSession = chatManager.getChatSession(chatID);

        // If the user already has a session, close it down.
        if (chatSession != null) {
            return;
        }

        try {
            final List searchedList = new ArrayList();

            // Create a new session.
            ChatSettingsManager settingsManager = chatManager.getChatSettingsManager();
            ConnectionSettings settings = settingsManager.getSettings();

            final String host = settings.getServerDomain();
            boolean sslEnabled = settings.isSSLEnabled();
            int port = settings.getPort();
            if (sslEnabled) {
                port = settings.getSSLPort();
            }

            // Gather all meta-data.
            final Map metadata = new HashMap();

            // Add standard Request Properties
            //addRequestProperties(metadata);

            // Look for cookies to set
            Enumeration setCookieEnum = request.getParameterNames();
            while (setCookieEnum.hasMoreElements()) {
                String name = (String)setCookieEnum.nextElement();
                if (name.startsWith("setCookie_")) {
                    String variableToSet = name.substring(10);
                    String parameter = request.getParameter(variableToSet);
                    if (parameter != null) {
                        try {
                            parameter = URLEncoder.encode(parameter, "UTF-8");
                        }
                        catch (UnsupportedEncodingException e) {
                            WebLog.logError("Error setting cookies.", e);
                        }
                        Cookie cookie = new Cookie("la_" + variableToSet, parameter);
                        cookie.setValue(parameter);
                        cookie.setMaxAge(60 * 60 * 24 * 30);
                        cookie.setPath(request.getContextPath());
                        response.addCookie(cookie);
                    }
                    searchedList.add(name);
                }
            }

            // Add all Parameters to Metadata.
            final String[] exclusionList = {"submit", "refresh", "location", "noUI"};
            final Enumeration nameEnum = request.getParameterNames();
            while (nameEnum.hasMoreElements()) {
                String key = (String)nameEnum.nextElement();
                String value = request.getParameter(key);
                if (!metadata.containsKey(key) && !searchedList.contains(key)) {
                    boolean excludeIt = false;
                    for (int i = 0; i < exclusionList.length; i++) {
                        String exclude = exclusionList[i];
                        if (key.equalsIgnoreCase(exclude)) {
                            excludeIt = true;
                        }
                    }
                    if (!excludeIt && ModelUtil.hasLength(value)) {
                        String escapedValue = StringUtils.escapeHTMLTags(value);
                        metadata.put(key, escapedValue);
                    }
                }
            }


            String mdProvider = application.getInitParameter("metadataProvider");
            if (mdProvider == null) {
                mdProvider = "org.jivesoftware.webchat.providers.GenericProvider";
            }

            MetaDataProvider provider = null;
            try {
                provider = (MetaDataProvider)getClass().getClassLoader().loadClass(mdProvider).newInstance();
            }
            catch (Exception e) {
                WebLog.logError("Error loading metadataprovider.", e);
            }

            // Pass of request and response to Provider Handler
            provider.filterRequest(request, response);

            String uniqueID = provider.getUserID();
            String nickname = provider.getUsername();
            String email = provider.getEmailAddress();
            String question = provider.getQuestion();
            String agent = provider.getAgent();

            if (question != null) {
                question = question.trim();
                metadata.put("question", question);
            }

            if (agent != null) {
                agent = agent.trim();
                metadata.put("agent", agent);
            }

            if (nickname == null) {
                nickname = "Visitor";
            }
            else {
                nickname = nickname.trim();
            }

            if (uniqueID != null) {
                metadata.put("userID", uniqueID);
            }

            metadata.put("username", nickname);

            if (email == null) {
                email = "Not specified.";
            }

            metadata.put("email", email);

            // Handle Location if Specified.
            if (request.getParameter("location") != null) {
                metadata.put("Location", request.getParameter("location"));
            }


            chatSession = new ChatSession(host, port, sslEnabled, uniqueID, nickname, email, chatID);

            // load workgroup properties and check of authentication is required for this workgroup
            Workgroup wGroup = new Workgroup(workgroup, chatManager.getGlobalConnection());
            WorkgroupProperties properties = null;
            try {
                properties = wGroup.getWorkgroupProperties();
            }
            catch (XMPPException e) {
            }

            if (properties != null && properties.isAuthRequired()) {
                // authentication is required, all users must login
                String username = (String)metadata.get("username");
                String password = (String)metadata.get("password");
                boolean chatloginok=false;
                try {
                    chatSession.login(username, password);
                    chatloginok=true;
                }
                catch (Exception e) {
                    try {
                        WebLog.logError("Authentication failed - ", e);
                        response.sendRedirect("userinfo.jsp?authFailed=true&workgroup=" + workgroup);
                    }
                    catch (IOException redirectException) {
                        WebLog.logError("Error during redirection - ", redirectException);
                    }
                }
                // load user metadata
                if (chatloginok) {
                    try {
                        properties = wGroup.getWorkgroupProperties(username + "@" + settings.getServerDomain());
                        metadata.put("name", properties.getFullName());
                        metadata.put("email", properties.getEmail());
                        chatSession.setEmailAddress(properties.getEmail());
                        metadata.remove("password");
                    }
                    catch (Exception e) {
                        WebLog.logError("Error setting up workgroup properties - ", e );
                    }
                }
            }
            else {
                for (int i = 0; i < 5; i++) {
                    // All users login as anoynoumous
                    try {
                        chatSession.loginAnonymously();
                        break;
                    }
                    catch (Exception e) {
                        WebLog.logError("Unable to login anonymously", e);
                    }
                }
            }


            if (!chatSession.getConnection().isConnected()) {
                try {
                    response.sendRedirect("fatal.jsp");
                }
                catch (IOException e) {
                    WebLog.logError("Unable to redirect.", e);
                }
                return;
            }

            // Filter metadata
            Map filteredData = filterMetadata(metadata);

            // Join the Workgroup Queue
            filteredData.remove("chatID");

            // Handle referer
            String referer = request.getHeader("referer");
            if (ModelUtil.hasLength(referer)) {
                filteredData.put("referer", referer);
            }

            chatManager.addChatSession(chatID, chatSession);
            chatSession.joinQueue(workgroup, filteredData);

            try {
                request.getRequestDispatcher("/view-queue.jsp").forward(request, response);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        catch (XMPPException e) {
            try {
                WebLog.logError("Could not join queue - ", e);
                response.sendRedirect("email/leave-a-message.jsp?workgroup=" + workgroup);
            }
            catch (IOException redirectException) {
                WebLog.logError("Error during redirection - ", redirectException);
            }
        }
    }

    private void addRequestProperties(Map metadata) {
        // Retrieve the users IP Address
        String ipAddress = request.getRemoteAddr();
        if (ipAddress != null) {
            metadata.put("IP Address", ipAddress);
        }

        String remoteUser = request.getRemoteUser();
        if (remoteUser != null) {
            metadata.put("Remote User", remoteUser);
        }

        String requestURL = request.getRequestURL().toString();
        if (requestURL != null) {
            metadata.put("Request URL", requestURL);
        }

        String authType = request.getAuthType();
        if (authType != null) {
            metadata.put("Auth Type", authType);
        }
    }

    private Map filterMetadata(Map map) {
        Map newMap = new HashMap(map);
        final Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String)iter.next();
            int indexOf = key.indexOf("0");
            if (key.endsWith("0")) {
                key = key.substring(0, indexOf);
                consolidateMetadata(key, map, newMap);
            }
            else {
                newMap.put(key, map.get(key));
            }
        }


        return newMap;
    }

    private void consolidateMetadata(String key, Map map, Map newMap) {
        List values = new ArrayList();
        final Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String k = (String)iter.next();
            if (k.startsWith(key)) {
                String value = (String)map.get(k);
                values.add(value);
                newMap.remove(k);
            }
        }
        newMap.put(key, values);
    }


}