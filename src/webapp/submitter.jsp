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
                 org.jivesoftware.webchat.util.ModelUtil,
                 org.jivesoftware.webchat.ChatManager,
                 org.jivesoftware.webchat.ChatSession"%>

<%
    final String chatID = request.getParameter("chatID");
    ChatManager chatManager = ChatManager.getInstance();
    ChatSession chatSession = chatManager.getChatSession(chatID);

    // Handle Parameters
    String message = ParamUtils.getParameter(request, "message");
    String isTyping = ParamUtils.getParameter(request, "isTyping");
    String leaving = ParamUtils.getParameter(request, "left");
%>

    <html>
    <body>

        <!-- form to submit chat text -->
        <form name="chatform" action="submitter.jsp" method="post">
            <input type="hidden" name="action" value="write">
            <input type="hidden" name="isTyping">
            <input type="hidden" name="left">
            <input type="hidden" name="chatID">
            <input type="hidden" name="message" value="">
        </form>

        <form name="closeForm" action="submitter.jsp" method="post">
            <input type="hidden" name="close" value="close">
        </form>

<%
        if (ModelUtil.hasLength(message)) {

        }
        if (ModelUtil.hasLength(isTyping)) {
         
        }

        if (ModelUtil.hasLength(leaving)) {
          chatManager.closeChatSession(chatID);
        }
%>
    </body>
    </html>
