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

<%@ page import="org.jivesoftware.webchat.util.ParamUtils,
                 java.util.Map,
                 java.util.HashMap,
                 java.net.InetAddress,
                 org.jivesoftware.smack.XMPPConnection"%>
 <%@ page import="java.util.Iterator" %>
 <%@ page import="org.jivesoftware.smack.ConnectionConfiguration" %>
 <%@ page import="org.jivesoftware.smack.XMPPException" %>

<%  // Get parameters
    String domain = ParamUtils.getParameter(request,"domain");
    int port = ParamUtils.getIntParameter(request,"port",-1);

    boolean doSave = request.getParameter("save") != null;

    // handle a continue request:
    Map errors = new HashMap();
    if (doSave) {
        // Validate parameters
        if (domain == null) {
            errors.put("domain","The server host is invalid.");
        }
        if (port < 0) {
            errors.put("port","Please specify a valid port.");
        }

        try {
            ConnectionConfiguration xmppConfig = new ConnectionConfiguration(domain, port);
            XMPPConnection con = new XMPPConnection(xmppConfig);
            con.connect();
            con.loginAnonymously();
        }
        catch (XMPPException xe) {
            // If anonymous login disabled.
            if (xe.getXMPPError().getCode() == 403) {
                errors.put("connect", "Anonymous login test failed. Ensure that anonymous logins are enabled on the server.");
            }
            else {
                errors.put("connect", "Could not connect to server. Please check that the domain and port are valid.");
            }
        }
        catch (Exception ex) {
            errors.put("connect", "Could not connect to server. Please check that the domain and port are valid.");
        }

        // Continue if there were no errors
        if (errors.size() == 0) {
            Map xmppSettings = new HashMap();

            xmppSettings.put("xmpp.domain",domain);
            xmppSettings.put("xmpp.socket.plain.port",Integer.toString(port));
            session.setAttribute("xmppSettings", xmppSettings);

            // successful, so redirect
            response.sendRedirect("setup-finished.jsp");
            return;
        }
    }

    // Load the current values:
    if (!doSave) {
        // If the domain is still blank, guess at the value:
        if (domain == null) {
            domain = InetAddress.getLocalHost().getHostName().toLowerCase();
        }
    }
%>

<html>
<head>
	<title>Webchat Setup</title>
	<link rel="stylesheet" type="text/css" href="setup-style.css">
    <script language="javascript" type="text/javascript" src="js/tooltips/domLib.js"></script>
    <script language="javascript" type="text/javascript" src="js/tooltips/domTT.js"></script>
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
Welcome to the Webchat setup tool. Before the webchat application can be used, you must connect it
to a Openfire Enterprise server.
</p>

<% if (errors.get("connect") != null) { %>
<div id="errorMessage" class="error">
    <%= errors.get("connect") %>
</div>
<% } %>

<div class="jive-contentBox">

<form action="setup-index.jsp" name="f" method="post">

<script langauge="JavaScript" type="text/javascript">
function toggle(form,disabled) {
    form.sslPort.disabled = disabled;
}
</script>

<table cellpadding="3" cellspacing="0" border="0" width="100%">

<tr valign="top">
    <td width="1%" nowrap>
        Server Host:
        <%  if (errors.get("domain") != null) { %>

            <span class="jive-error-text"><br>
            <%= errors.get("domain") %>
            </span>

        <%  } %>
    </td>
    <td width="99%">
        <input type="text" size="30" maxlength="150" name="domain"
         value="<%= ((domain != null) ? domain : "") %>">
        <span class="jive-setup-helpicon" onmouseover="domTT_activate(this, event, 'content', 'Hostname or IP address of the IM server.', 'styleClass', 'jiveTooltip', 'trail', true, 'delay', 300, 'lifetime', 8000);"></span>
    </td>
</tr>
<tr valign="top">
    <td width="1%" nowrap>
        Server Port:
        <%  if (errors.get("port") != null) { %>

            <span class="jive-error-text"><br>
            Invalid port number.
            </span>

        <%  } %>
    </td>
    <td width="99%">
        <input type="text" size="6" maxlength="6" name="port"
         value="<%= ((port != -1) ? ""+port : "5222") %>">
        <span class="jive-setup-helpicon" onmouseover="domTT_activate(this, event, 'content', 'Port number the XMPP server listens on. Default XMPP port is 5222.', 'styleClass', 'jiveTooltip', 'trail', true, 'delay', 300, 'lifetime', 8000);"></span>
    </td>
</tr>

</table>

<br><br>

<div align="right">
    <input type="submit" name="save" value="Save Settings " id="jive-setup-save" border="0">
</div>
</form>

<script language="JavaScript" type="text/javascript">
// give focus to domain field
document.f.domain.focus();
</script>

</div>

</div>
<!-- END jive-body -->

<!-- BEGIN jive-footer -->
<div id="jive-footer"></div>
<!-- END jive-footer -->

</body>
</html>