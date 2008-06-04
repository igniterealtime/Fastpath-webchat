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

<%@ page import="org.jivesoftware.webchat.util.FormText,
                 org.jivesoftware.webchat.util.ParamUtils" %>
<%@ page errorPage="fatal.jsp" %>
<script type='text/javascript' src='<%= request.getContextPath()%>/dwr/interface/room.js'></script>
<script type='text/javascript' src='<%= request.getContextPath()%>/dwr/engine.js'></script>
<%
    final String workgroup = ParamUtils.getParameter(request, "workgroup");
    final String chatID = ParamUtils.getParameter(request, "chatID");
    String descriptiveText = FormText.getQueueDescriptionText(workgroup);
    String titleText = FormText.getQueueTitleText(workgroup);
%>
    <html>
    <head>
        <title>Queue Information</title>
        <link rel="stylesheet" type="text/css" href="style.jsp?workgroup=<%= workgroup %>"/>
        <script language="JavaScript" type="text/javascript" src="common.js"></script>
        <script type="text/javascript" language="Javascript">
         DWREngine.setErrorHandler(handleError);


         function handleError(error) {
             // Will ignore due to dwr bug.
         }

         function showLeaveAMessage() {
                isClosing = false;
                window.location.href = 'email/leave-a-message.jsp?workgroup=<%= workgroup %>&chatID=<%= chatID%>';
         }
        </script>
    </head>
    <body style="margin-top:0px; margin-bottom:20px; margin-right:20px; margin-left:20px" id="defaultwindow" onunload="isWindowClosing();">
        <table width="100%" border="0">
            <tr>
                <td height="1%">
                    <img src="getimage?image=logo&workgroup=<%= workgroup %>"/>
                </td>
            </tr>
            <tr>
                <td height="1%">
                  <br/>  <span  id="queue_info_header"><%= titleText %> </span>
                </td>
            </tr>
            <tr>
                <td height="1%">
                    <span id="queue_info" style="visibility:hidden;">
                    <%= descriptiveText %> </span>
                </td>
            </tr>
            <tr>
                <td height="1%" >
                    <br/>
                    <br/>
                    <span id="queue_info_footer"> <%= FormText.getQueueFooter(workgroup)%>
                   </span>
                    <br/>
                    <br/>
                </td>
            </tr>
            <form name="queueform" action="exit-queue.jsp" method="post">
                <input type="hidden" name="workgroup" value="<%= workgroup %>" />
                <input type="hidden" name="chatID" value="<%= chatID%>" />
            <tr>
                <td height="1%">
                            <input type="submit"
                                   name="leave"
                                   onclick="return checkLeaving();return false"
                                   value="Close Window"/>
                </td>
            </tr>
            </form>
              <tr>
                        <td colspan="2" align="right" valign="bottom" height="99%">

                        </td>
                    </tr>
        </table>
        <div style="position:absolute;bottom:0px;right:5px"><img src="getimage?image=poweredby&workgroup=<%= workgroup %>"/></div>
        <script>
            var isClosing = true;

            function checkQueue() {
                room.getChatQueue(handleQueue, '<%= chatID %>');
            }
            function handleQueue(queue) {
                if (queue == null) {
                    return;
                }
                if (queue.connectionDropped) {
                    showNoAnswer();
                    return;
                }
                else if (queue.routed) {
                    redirectForChat();
                    return;
                }
                else if (queue.queueTime > 0 && queue.queuePosition > 0) {
                    updateQueuePosition(queue.queuePosition);
                    updateQueueTime(queue.queueTimeForHtml);
                }
                setTimeout("checkQueue()", 5000);
            }
            // Redirect For Chat
            function redirectForChat() {
                isClosing = false;
                window.location.href = 'chatmain.jsp?workgroup=<%= workgroup %>&chatID=<%= chatID%>';
            }
            // Updates the queue position text on the queue page
            function updateQueuePosition(position) {
                var posSpan = window.document.getElementById('queue_position');
                if (posSpan) {
                    posSpan.innerHTML = position;
                    window.document.getElementById('queue_info').style.visibility = 'visible';
                }
            }
            // Updates the queue wait time. Time should be in seconds.
            function updateQueueTime(time) {
                var timeSpan = window.document.getElementById('queue_time');
                if (timeSpan) {
                    timeSpan.innerHTML = time;
                    window.document.getElementById('queue_info').style.visibility = 'visible';
                }
            }

            function isWindowClosing() {
                if (isClosing) {
                    window.location.href = 'exit-queue.jsp?workgroup=<%= workgroup%>&chatID=<%= chatID %>';
                }
            }

            function checkLeaving(){
                var ok = confirm('Are you sure you want to leave the queue?');
                if(ok){
                    isClosing = false;
                }
                return ok;
            }



            function showNoAnswer() {
                var queueInfoHeader = window.document.getElementById('queue_info_header');
                if (queueInfoHeader != null) {
                    queueInfoHeader.innerHTML  = '<%= FormText.getNoAgentText(workgroup)%>';
                    var queueInfoText = window.document.getElementById('queue_info');
                    queueInfoText.innerHTML = "";
                    var queueInfoFooter = window.document.getElementById('queue_info_footer');
                    queueInfoFooter.innerHTML = "";
                }
            }
            checkQueue();
        </script>

    </body>
    </html>
