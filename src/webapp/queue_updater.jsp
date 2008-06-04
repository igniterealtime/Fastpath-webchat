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

<%@ page import="org.jivesoftware.webchat.ChatManager,
                 org.jivesoftware.webchat.ChatSession,
                 org.jivesoftware.webchat.actions.ChatQueue,
                 org.jivesoftware.webchat.util.WebUtils"%>

<%@ page errorPage = "fatal.jsp" %>
<%
    String workgroup = request.getParameter("workgroup");
    String chatID = request.getParameter("chatID");

    ChatManager chatManager = ChatManager.getInstance();
    ChatSession chatSession = chatManager.getChatSession(chatID);
    ChatQueue queue = null;
%>
  <html>
   <head>
    <meta http-equiv="cache-control" content="no-cache">
      <meta http-equiv="expires" content="0">
      <script language="JavaScript" type="text/javascript">

       // Updates the queue position text on the queue page
       function updateQueuePosition(position) {
         var posSpan = window.parent.document.getElementById('queue_position');

         if (posSpan) {
           posSpan.innerHTML = position;
           window.parent.document.getElementById('queue_info').style.visibility = 'visible';
         }
       }
       // Updates the queue wait time. Time should be in seconds.
       function updateQueueTime(time) {
         var timeSpan = window.parent.document.getElementById('queue_time');

         if (timeSpan) {
           timeSpan.innerHTML = time;
           window.parent.document.getElementById('queue_info').style.visibility = 'visible';
         }
       }

       function showNoAnswer() {
         var queueInfoHeader = window.parent.document.getElementById('queue_info_header');
         queueInfoHeader.innerHTML
             = "We are unable to route your request at this time. To leave a message or request a call back <a href='email/leave-a-message.jsp?workgroup=<%= workgroup %>'>click here</a>.";

         var queueInfoText = window.parent.document.getElementById('queue_info_text');
         queueInfoText.innerHTML = "";

         var queueInfoFooter = window.parent.document.getElementById('queue_info_footer');
         queueInfoFooter.innerHTML = "Please try again later...";
       }

       function redirectForChat() {
         window.parent.location.href = 'chatmain.jsp?workgroup=<%= workgroup %>&chatID=<%= chatID%>';
       }
      </script>
   </head>

   <body>
   </body>
  </html>

  <% if(queue.isConnectionDropped()){%>
   <script>
    showNoAnswer();
   </script>
   <% } %>

   <% if(queue.isRouted()){ %>
   <script>
    redirectForChat();
   </script>
    <% } %>

  <% if(queue.getQueueTime() > 0 && queue.getQueuePosition() > 0){ %>
   <script>
   updateQueuePosition(<%= queue.getQueuePosition() %>);
   updateQueueTime('<%= WebUtils.getTimeFromLong(queue.getQueueTime()) %>');
   </script>
  <% } %>

  <% if(!queue.isConnectionDropped()) { %>
  <!-- Will reload every 3 seconds -->
  <script>
   function CRCheckMsgs() {
     setTimeout("getMsgs()", 3000);
   }

   function getMsgs() {
     var yakWin = window;
     yakWin.location.reload(true);
   }

   CRCheckMsgs();
  </script>
  <% } %>
