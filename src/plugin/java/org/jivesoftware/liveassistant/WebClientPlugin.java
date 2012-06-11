/**
 * $RCSfile$
 * $Revision$
 * $Date: 2006-03-10 16:43:12 -0800 (Fri, 10 Mar 2006) $
 *
 * Copyright (C) 2003-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.liveassistant;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jivesoftware.util.JiveGlobals;
import org.jivesoftware.util.Log;
import org.jivesoftware.openfire.container.AdminConsolePlugin;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.XMPPServer;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plugin implementation to allow the web client to operate as a plugin in Jive Messenger.
 * It does so, by installing a "liveassistant" web application into the embedded Jetty
 * server of Jive Messenger.
 *
 * @author Matt Tucker
 */
public class WebClientPlugin implements Plugin {

    private ServletContextHandler context;

    public void initializePlugin(PluginManager pluginManager, File pluginDirectory) {
        // Specify it's a demo.
        String port = JiveGlobals.getProperty("xmpp.socket.plain.port", "5222");
        String domain = JiveGlobals.getProperty("xmpp.domain");

        if (port != null && domain != null) {
            // Set System properties to use.
            System.setProperty("isdemo", "true");
            System.setProperty("domain", domain);
            System.setProperty("port", port);
        }

        // Add web-app.

        ContextHandlerCollection contexts = ((AdminConsolePlugin)pluginManager.getPlugin("admin")).getContexts();

        context = new WebAppContext(pluginDirectory.getPath(),
                "/" + pluginDirectory.getName());
        contexts.addHandler(context);

        context.setWelcomeFiles(new String[]{"index.jsp"});

        // The embedded web server doesn't know how to compile JSPs. Therefore, we have
        // to manually parse a generated web.xml file and add the entries as servlets
        // to the webapp.
        try {
            // Make the reader non-validating so that it doesn't try to resolve external
            // DTD's. Trying to resolve external DTD's can break on some firewall configurations.
            SAXReader saxReader = new SAXReader(false);
            saxReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
                    false);
            Document doc = saxReader.read(new File(pluginDirectory, "WEB-INF" +
                    File.separator + "web.xml.generated"));
            // Find all <servlet> entries to discover name to class mapping.
            List classes = doc.selectNodes("//servlet");
            Map classMap = new HashMap();
            for (int i = 0; i < classes.size(); i++) {
                Element servletElement = (Element)classes.get(i);
                String name = servletElement.element("servlet-name").getTextTrim();
                String className = servletElement.element("servlet-class").getTextTrim();
                classMap.put(name, className);
            }
            // Find all <servelt-mapping> entries to discover name to URL mapping.
            List names = doc.selectNodes("//servlet-mapping");
            for (int i = 0; i < names.size(); i++) {
                Element nameElement = (Element)names.get(i);
                String name = nameElement.element("servlet-name").getTextTrim();
                String url = nameElement.element("url-pattern").getTextTrim();
                context.addServlet((String)classMap.get(name), url);
            }
        }
        catch (Exception e) {
            Log.error(e);
        }

        try {
            context.start();
        }
        catch (Exception e) {
            Log.error(e);
        }
    }

    public void destroyPlugin() {
        PluginManager pluginManager = XMPPServer.getInstance().getPluginManager();
        ((AdminConsolePlugin) pluginManager.getPlugin("admin")
                ).getContexts().removeHandler(context);
        try {
            context.stop();
        }
        catch (Exception e) {
            Log.error(e);
        }
        context = null;
    }
}