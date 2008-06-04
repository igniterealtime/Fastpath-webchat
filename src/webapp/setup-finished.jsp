<%--
  - $RCSfile$
  - $Revision: $
  - $Date: $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%@ page
import="java.util.Map,
                 java.util.Iterator,
                 org.jivesoftware.webchat.settings.ConnectionSettings,
                 java.beans.XMLEncoder,
                 java.io.File,
                 org.jivesoftware.webchat.settings.ChatSettingsManager,
        org.jivesoftware.webchat.util.Base64,
        org.jivesoftware.webchat.FastpathServlet"%><%@ page import="org.jivesoftware.webchat.ChatManager" %>

<%
    // update the sidebar status
    session.setAttribute("jive.setup.sidebar.1", "done");
    session.setAttribute("jive.setup.sidebar.2", "done");
    session.setAttribute("jive.setup.sidebar.3", "done");
    session.setAttribute("jive.setup.sidebar.4", "in_progress");

    boolean showSidebar = false;
    // Define settings file location

    File settingsFile = FastpathServlet.SETTINGS_FILE;

    ChatManager chatManager = ChatManager.getInstance();
    ChatSettingsManager settingsManager = new ChatSettingsManager(settingsFile);
    ConnectionSettings settings = settingsManager.getSettings();
    if (settings == null) {
        settings = new ConnectionSettings();
    }
    chatManager.setChatSettingsManager(settingsManager);

    // First, update with XMPPSettings
    Map xmppSettings = (Map)session.getAttribute("xmppSettings");
    String domain = (String)xmppSettings.get("xmpp.domain");
    String port = (String)xmppSettings.get("xmpp.socket.plain.port");
    String sslEnabled = (String)xmppSettings.get("xmpp.socket.ssl.active");
    String ssl = (String)xmppSettings.get("xmpp.socket.ssl.port");
    String password = (String)xmppSettings.get("presencebot");

    int sslPort = -1;
    boolean isSSLEnabled = Boolean.valueOf(sslEnabled).booleanValue();
    if (isSSLEnabled) {
        sslPort = Integer.parseInt(ssl);
        settings.setSSLEnabled(true);
        settings.setSSLPort(sslPort);
    }
    settings.setServerDomain(domain);
    settings.setPort(Integer.parseInt(port));

    settingsManager.save(settings);
%>


<html>
<head>
	<title>Webchat Setup</title>
	<link rel="stylesheet" type="text/css" href="setup-style.css">
    <script language="javascript" type="text/javascript" src="js/tooltips/domLib.js"></script>
    <script language="javascript" type="text/javascript" src="js/tooltips/domTT.js"></script>
    <script language="JavaScript" type="text/javascript" src="common.js"></script>
</head>

<body>

<!-- BEGIN jive-header -->
<div id="jive-header">
	<div id="jive-header-text">Webchat Setup</div>
	<div id="sidebar-top"></div>
</div>
<!-- END jive-header -->

<!-- BEGIN jive-body -->
<div id="jive-body">

<p>
    Webchat installation is now complete. To test your installation, click on the following image:
</p>

    <script language="JavaScript" type="text/javascript" src="jivelive.jsp"></script>
    <script>
        showChatButton('demo@workgroup.<%= settings.getServerDomain()%>');
    </script>

</div>

<!-- END jive-body -->

<!-- BEGIN jive-footer -->
<div id="jive-footer"></div>
<!-- END jive-footer -->

</body>
</html>



