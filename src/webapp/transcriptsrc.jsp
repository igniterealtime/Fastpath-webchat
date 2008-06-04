<%--
  - $RCSfile$
  - $Revision: 18732 $
  - $Date: 2005-04-08 14:50:03 -0700 (Fri, 08 Apr 2005) $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%@ page
import   ="org.jivesoftware.webchat.history.*,
                   java.util.*,
                   org.jivesoftware.webchat.ChatManager,
                   org.jivesoftware.webchat.ChatSession,
                   org.jivesoftware.webchat.util.ModelUtil"
errorPage="fatal.jsp"%>

<%@ include file="include/global.jsp"%>

<%
    String chatID = request.getParameter("chatID");
    ChatManager chatManager = ChatManager.getInstance();
    ChatSession chatSession = chatManager.getChatSession(chatID);
    if(chatSession == null){
        response.sendRedirect("chat-ended.jsp");
    }

    // Get the transcript from the session
    Transcript transcript = chatSession.getTranscript();
    chatManager.closeChatSession(chatID);
%>

    <html>
    <head>
        <title>Chat Transcripts</title>

        <script language="JavaScript" type="text/javascript" src="common.js"></script>

        <link rel="stylesheet" type="text/css" href="style.jsp"/>
    </head>

    <body id="transcriptprintwindow">
        <div id="transcriptprintwindow-content">
            <div class="box">
                <br/>

                <div id="ytext">
<%
                    if (transcript != null) {
%>

<%
                        String question = (String)session.getAttribute("Question");
                        if(ModelUtil.hasLength(question)){
                            %><div class="chat-line"><span class="chat-announcement">Question: <%= question%> </span>  </div><%
                        }


                        List transcriptLines = transcript.getTranscript();

                        for (int i = 0; i < transcriptLines.size(); i++) {
                            Line line = (Line) transcriptLines.get(i);

                            String from = line.getFrom();
                            String text = line.getText();
                            String cp = request.getContextPath();
                            String full = request.getRequestURL().toString();
                            int  index = full.indexOf(cp);
                            String base = full.substring(0, (index + cp.length()));

                            text = text.replaceAll("src=\"", "src=\"" + base + "/");
                            boolean isAnnouncement = !ModelUtil.hasLength(from);
%>

                            <div class="chat-line">
<%
                                if (isAnnouncement) {
%>

                                    <span class="chat-announcement"> <%= text %> </span>

<%
                                }
                                else{
%>

                                    <span class="<%= "client-name" %>"> <%= from %>: </span>
                                    <span class="text"> <%= text %> </span>

<%
                                }
%>
                            </div>

<%
                        }
%>

<%
                    }
%>
                </div>
            </div>
        </div>
    </body>
    </html>
