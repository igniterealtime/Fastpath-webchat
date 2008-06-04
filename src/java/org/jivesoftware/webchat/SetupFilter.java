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

package org.jivesoftware.webchat;

import org.jivesoftware.webchat.settings.ConnectionSettings;
import org.jivesoftware.webchat.settings.ChatSettingsManager;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.URLFileSystem;
import org.jivesoftware.webchat.util.WebLog;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Handles validation of settings file. The SetupFilter will redirect a user to the setup wizard if no
 * settings were found.
 */
public class SetupFilter implements Filter {
    private ChatManager chatManager;

    public void init(FilterConfig filterConfig) throws ServletException {
        chatManager = ChatManager.getInstance();
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ChatSettingsManager settingsManager = chatManager.getChatSettingsManager();
        String path = ((HttpServletRequest)request).getContextPath();
        if (settingsManager == null) {
            try {
                ((HttpServletResponse) response).sendRedirect(path+"/setup-index.jsp");
                return;
            }
            catch (IOException e) {
                WebLog.logError("Error in SetupFilter.", e);
            }
        }

        // Add page info.
        HttpServletRequest re = (HttpServletRequest) request;
        StringBuffer requestURL = re.getRequestURL();
        String page = requestURL.toString();

        if (page != null && page.indexOf("setup-") == -1) {
            if (isValidPage(page)) {
                ConnectionSettings settings = settingsManager.getSettings();
                if (settings == null || !ModelUtil.hasLength(settings.getServerDomain())) {
                    try {
                        ((HttpServletResponse) response).sendRedirect(path+"/setup-index.jsp");
                        return;
                    }
                    catch (IOException e) {
                        WebLog.logError("Error in SetupFilter.", e);
                    }
                }
            }
        }
        else if (page != null && page.indexOf("setup-") != -1 && page.indexOf("setup-completed") == -1) {
            ConnectionSettings settings = settingsManager.getSettings();
            if (settings != null &&  isValidPage(page) && isConnected()) {
                try {
                    ((HttpServletResponse) response).sendRedirect("setup-completed.jsp");
                    return;
                }
                catch (IOException e) {
                    WebLog.logError("Error in SetupFilter.", e);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private static boolean isValidPage(String pageURL) {
        URL url = null;
        try {
            url = new URL(pageURL);
        }
        catch (MalformedURLException e) {
            WebLog.logError("Error in SetupFilter.", e);
            return false;
        }

        boolean ok = false;

        final String[] acceptSuffixList = {".html", ".jsp", ".htm"};
        final String suffix = URLFileSystem.getSuffix(url);

        // Handle url's without page names.
        if (!ModelUtil.hasLength(suffix)) {
            return true;
        }

        for (int i = 0; i < acceptSuffixList.length; i++) {
            if (suffix.equals(acceptSuffixList[i])) {
                ok = true;
            }
        }

        return ok;
    }

    private boolean isConnected(){
        ChatSettingsManager settingsManager = chatManager.getChatSettingsManager();
        if(settingsManager != null || settingsManager.getSettings() != null){
            return chatManager.getGlobalConnection() != null && chatManager.getGlobalConnection().isConnected();
        }
        return false;
    }
}
