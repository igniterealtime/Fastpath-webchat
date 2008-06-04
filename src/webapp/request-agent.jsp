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

<%@ page import="org.jivesoftware.webchat.util.ParamUtils"%><html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

  <title>Contact Agent Form</title>

<%
  final String agentName = ParamUtils.getParameter(request, "agent", true);
  final String workgroupName = ParamUtils.getParameter(request, "workgroup", true);
%>
</head>

<body onload="showDialog();">
  <script language="JavaScript" type="text/javascript"
          src="http://liveassist.jivesoftware.com/liveassistant/common.js"></script>

  <script language="JavaScript">
    function showDialog() {
      var laServer = 'http://liveassist.jivesoftware.com/liveassistant/LiveAssistantServlet?action=init&agent=<%= agentName %>&workgroup=<%= workgroupName %>@jivesoftware.com&location='
              + window.location.href;
      launchWin('framemain', laServer, 409, 376);
    }
  </script>

  If you do not see a web chat window, <a href="javascript:showDialog();">Click here to contact <%= agentName %></a>
</body>
</html>
