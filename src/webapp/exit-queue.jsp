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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean class="org.jivesoftware.webchat.ChatUser" id="chatUser"/>
<jsp:setProperty name="chatUser" property="*"/>
<%
    if (chatUser.hasSession()) {
        chatUser.removeSession();
    }
%>
<html>
<head>
    <script type="text/javascript">
        window.close();
    </script>
</head>
</html>