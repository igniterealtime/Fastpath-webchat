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

<%@ page import="org.jivesoftware.webchat.actions.ChatStarter,
                 org.jivesoftware.webchat.actions.WorkgroupStatus,
                 org.jivesoftware.webchat.util.ModelUtil,
                 org.jivesoftware.webchat.util.ParamUtils" %>
<%@ page import="org.jivesoftware.webchat.ChatSession"%><%@ page import="org.jivesoftware.webchat.ChatManager"%>
<%@ page errorPage="fatal.jsp" %>

<%
    ChatStarter chatStarter = new ChatStarter();
%>
<%
    final String workgroup = ParamUtils.getParameter(request, "workgroup");
    // Add workgroup to session
    session.setAttribute("workgroup", workgroup);
    final String chatID = ParamUtils.getParameter(request, "chatID");
    final String question = ParamUtils.getParameter(request, "question");
    session.setAttribute("chatID", chatID);
    if (ModelUtil.hasLength(question)) {
        session.setAttribute("Question", question);
    }
    if (!WorkgroupStatus.isOnline(workgroup)) {
        response.sendRedirect("email/leave-a-message.jsp?workgroup=" + workgroup);
        return;
    }
    chatStarter.init(pageContext);
    // Define the starting paramters for this chat session

    /**
     * @param workgroup the name of the workgroup to join.
     * @param chatID the unique id for this session.
     */
    chatStarter.startSession(workgroup, chatID);
%>