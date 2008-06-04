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

<%@ page import="org.jivesoftware.webchat.util.ModelUtil,
                 org.jivesoftware.webchat.ChatManager,
                 org.jivesoftware.webchat.ChatSession,
                 org.jivesoftware.smack.XMPPConnection,
                 java.util.Map,
                 org.jivesoftware.webchat.providers.Settings,
                 com.jivesoftware.smack.workgroup.user.Workgroup,
                 org.jivesoftware.webchat.util.FormText,
                 org.jivesoftware.webchat.util.ParamUtils,
                 org.jivesoftware.smack.util.StringUtils" errorPage="fatal.jsp" %>

<%
    String workgroup = request.getParameter("workgroup");
    String chatID = request.getParameter("chatID");

    ChatManager chatManager = ChatManager.getInstance();
    ChatSession chatSession = chatManager.getChatSession(chatID);
    if (!ModelUtil.hasLength(workgroup) || chatSession == null) {
        response.sendRedirect("chat-ended.jsp");
        return;
    }

    // Close Chat Session
    chatSession.close();

    XMPPConnection con = chatManager.getGlobalConnection();
    if(!con.isConnected()){
        response.sendRedirect("chat-ended.jsp");
        return;
    }

    Workgroup wgroup = new Workgroup(workgroup, con);
    boolean isEmailConfigured = wgroup.isEmailAvailable();

    if(!isEmailConfigured){
        response.sendRedirect("chat-ended.jsp?workgroup="+workgroup);
        return;
    }

    String to = ParamUtils.getParameter(request, "to");
    String sessionID = StringUtils.parseName(chatSession.getRoomName());

    boolean transcriptSent = false;
    if(ModelUtil.hasLength(to)){
        transcriptSent = wgroup.sendTranscript(to, sessionID);
    }

%>

    <html>
    <head>
        <title>Transcript</title>

        <link rel="stylesheet" type="text/css" href="style.jsp"/>

        <script language="JavaScript" type="text/javascript" src="common.js"></script>

        <script language="javascript">
            function ValidateForm() {
                if ((! Jtrim(document.emailForm.to.value).length)) {
                    alert("Please specify an email address.");
                    document.emailForm.to.focus();
                    return false;
                }

                if (! isValidEmail(document.emailForm.to.value)) {
                    alert("Please enter a valid email address.");
                    document.emailForm.to.focus();
                    return false;
                }
                return true;
            }

            function isValidEmail(str) {

                var apos = str.indexOf("@");
                var dpos = str.indexOf(".");
                var epos = str.indexOf("]");
                var fpos = str.indexOf("[");
                if (apos <= 0 || epos > 0 || fpos > 0)
                    return false;

                return true;
            }

            function Jtrim(st) {

                var len = st.length;
                var begin = 0, end = len - 1;
                while (st.charAt(begin) == " " && begin < len) {
                    begin++;
                }
                while (st.charAt(end) == " " && end > begin) {
                    end--;
                }

                return st.substring(begin, end + 1);
            }
        </script>
    </head>

    <body style="margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px" id="defaultwindow">
      <table height="100%" width="100%" border="0">
        <tr><td colspan="2" height="1%">
        <img src="getimage?image=logo&workgroup=<%= workgroup %>"/>
        </td></tr>
        <% if(!transcriptSent && !ModelUtil.hasLength(to)){ %>
            <form name="emailForm" action="transcriptmain.jsp" method="post" onsubmit="return ValidateForm();">
                <!-- Define hidden variables -->
                <input type="hidden" name="chatID" value="<%= chatID %>"/>

                <input type="hidden" name="workgroup" value="<%= workgroup %>"/>

                <!-- End of Hidden Variable definitions -->
                 <tr><td><br/></tr>
                    <tr>
                        <td colspan="2" height="1%">
                           <%= FormText.getTranscriptText(workgroup)%>
                        </td>
                    </tr>

                <tr><td><br/></tr>

                    <tr>
                        <td height="1%" nowrap width="1%">
                            <b>Email:</b>
                        </td>

                        <td height="1%">
                            <input type="hidden" name="message" value="Here is the message"/>

                            <input type="text" size="30" name="to" value="<%= chatSession.getEmailAddress() %>">
                        </td>
                    </tr>

                    <tr>
                        <td>
                        </td>

                        <td>
                            <input type="image" src="getimage?image=sendemail&workgroup=<%= workgroup %>" name=""
                                   value="">
                        </td>
                    </tr>

            </form>
         <% } %>

         <% if(transcriptSent){ %>
             <tr><td height="1%"><%= FormText.getTranscriptSent(workgroup)%></td></tr>
             <tr><td height="1%" align="center"><br/><br/><input type="button" name="leave" value="Close Window"
                           onclick="window.close();"></td></tr>
         <% } %>

         <% if(!transcriptSent && ModelUtil.hasLength(to)) { %>
            <tr><td height="1%"><%= FormText.getTranscriptNotSent(workgroup) %></td></tr>
             <tr><td height="1%" align="center"><br/><br/><input type="button" name="leave" value="Close Window"
                           onclick="window.close();"></td></tr>
         <% } %>
            <tr><td height="99%" align="right" valign="bottom" colspan="2">
             
            </td></tr>
             </table>
      <div style="position:absolute;bottom:0px;right:5px"><img src="getimage?image=poweredby&workgroup=<%= workgroup %>"/></div>

    </body>
    </html>