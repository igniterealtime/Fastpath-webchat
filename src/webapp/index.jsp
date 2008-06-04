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
        import="org.jivesoftware.webchat.ChatManager,
                org.jivesoftware.webchat.actions.WorkgroupStatus,
                org.jivesoftware.webchat.settings.ConnectionSettings,
                org.jivesoftware.smack.XMPPConnection,
                java.util.Collection,
                java.util.Iterator" errorPage="fatal.jsp" %>

<%
    if (!Boolean.valueOf(System.getProperty("isdemo")).booleanValue()) {

    }
%>

<html>
<head>
	<title>Fastpath Web Chat</title>
	<link rel="stylesheet" type="text/css" href="setup-style.css">
    <script language="javascript" type="text/javascript" src="js/tooltips/domLib.js"></script>
    <script language="javascript" type="text/javascript" src="js/tooltips/domTT.js"></script>
    <script language="JavaScript" type="text/javascript" src="common.js"></script>
     <script language="JavaScript" type="text/javascript" src="jivelive.jsp"></script>
</head>

<body>

<!-- BEGIN jive-header -->
<div id="jive-header">
	<div id="jive-header-text">Fastpath Web Chat</div>
	<div id="sidebar-top"></div>
</div>
<!-- END jive-header -->

<!-- BEGIN jive-body -->
<div id="jive-body">


<table cellspacing="0" cellpadding="4" width="100%" border="0">
<tbody>
    <tr valign="top">

    <td width="99%">

        <b>List of available workgroups within Fastpath</b>
        <br/><br/>
        
        <p>
        Click on one of the following workgroups to join.
        </p>

        <div class="icons">
        <table cellspacing="0" cellpadding="4" border="0">
        <tbody>
            <% int count = 1;
                Collection workgroupList = null;
                Iterator workgroups = null;
                try {
                    workgroupList = WorkgroupStatus.getWorkgroupNames();
                    workgroups = workgroupList.iterator();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                while (workgroups != null && workgroups.hasNext()) {
                    String workgroupName = (String)workgroups.next();
            %>
                <tr>
                    <td><%= count++ %>. </td>
                    <td>Workgroup: <b><%= workgroupName %></b></td>
                    <td>
                        <script>
                        showChatButton('<%= workgroupName%>@workgroup.<%= WorkgroupStatus.getHost() %>');
                        </script>
                    </td>
                </tr>
            <% } %>

            <%
                if (workgroupList == null || workgroupList.size() == 0) {
                    XMPPConnection con = ChatManager.getInstance().getGlobalConnection();
                    ConnectionSettings settings = ChatManager.getInstance().getChatSettingsManager().getSettings();

                    if (con == null || !con.isConnected()) {
            %>
                            <tr><td><font color="red">Unable to connect to server using the following settings: </font></td></tr>
                            <tr><td><b>Server:</b> <%= settings.getServerDomain()%></td></tr>
                            <tr><td><b>Port:</b> <%= settings.isSSLEnabled() ? settings.getSSLPort() : settings.getPort()%></td></tr>
                             <tr><td><b>SSL Enabled:</b> <%= settings.isSSLEnabled() ? "true" : "false" %></td></tr>
                            <tr><td><a href="test-connection.jsp">Test Connection</a></td></tr>
                            <tr><td><br/>Please change your server settings in the setup tool.</td></tr>
                        <%
                                }

                            }
                        %>
        </tbody>
        </table>
        </div>

    </td>
    </tr>
</tbody>
</table>


<!-- END jive-body -->
</div>
<!-- BEGIN jive-footer -->
<div id="jive-footer"></div>
<!-- END jive-footer -->
</body>
</html>