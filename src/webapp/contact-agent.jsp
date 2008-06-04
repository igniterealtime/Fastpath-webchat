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
                 org.jivesoftware.webchat.actions.WorkgroupStatus,
                 org.jivesoftware.smack.XMPPConnection,
                 org.jivesoftware.smack.Roster,
                 org.jivesoftware.smack.packet.Presence" errorPage="fatal.jsp" %>
<html>
<head>
 <script language="JavaScript" type="text/javascript" src="common.js"></script>
</head>
<body>

</body>
</html>
<%
   String email = request.getParameter("email");
   String agentName = request.getParameter("name");
   String jid = request.getParameter("jid");

   boolean isAgentOnline = WorkgroupStatus.isAgentOnline(jid);

   if(isAgentOnline){
       response.sendRedirect("agentinfo.jsp?agentName="+agentName+"&jid="+jid+"&email="+email);
       return;
   }
   else {
        %><a href="mailto:<%= email%>"><img src="images/personal_offline.gif" border="0"></a><%
   }
%>


