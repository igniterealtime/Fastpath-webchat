<%@ page import = "org.jivesoftware.smack.XMPPConnection,
                   org.jivesoftware.webchat.ChatManager,
                   org.jivesoftware.webchat.util.ModelUtil,
                   org.jivesoftware.webchat.util.ParamUtils,
                   org.jivesoftware.webchat.ChatSession,
                   org.jivesoftware.smack.util.StringUtils,
                   com.jivesoftware.smack.workgroup.user.Workgroup,
                   com.jivesoftware.smack.workgroup.settings.OfflineSettings,
                   org.jivesoftware.smack.XMPPException"
                   errorPage="../fatal.jsp"%><%@ page import="java.util.Map"%><%@ page import="java.util.Iterator"%>

<%
    boolean success = false;
    ChatManager chatManager = ChatManager.getInstance();
    String chatID = request.getParameter("chatID");
    if(chatID == null){
        chatID = (String)session.getAttribute("chatID");
    }

    Map metadata = null;

    if(ModelUtil.hasLength(chatID)){
       ChatSession chatSession = chatManager.getChatSession(chatID);
       if(chatSession != null){
         metadata = chatSession.getMetaData();
         if(metadata != null){
             session.setAttribute("metadata", metadata);
         }
       }
       chatManager.closeChatSession(chatID);
    }

    // Get and Set Workgroup
    String  workgroup = request.getParameter("workgroup");
    if(workgroup == null){
        workgroup = (String)session.getAttribute("workgroup");
    }

    XMPPConnection con = chatManager.getGlobalConnection();

    Workgroup offlineWorkgroup = new Workgroup(workgroup, con);
    boolean isEmailConfigured = offlineWorkgroup.isEmailAvailable();
    OfflineSettings offlineSettings = null;
    try {
        offlineSettings = offlineWorkgroup.getOfflineSettings();
    }
    catch (XMPPException e) {
       response.sendRedirect("offline-mail.jsp?workgroup="+workgroup);
       return;
    }
%>

    <html>
    <head>
        <link rel="stylesheet" type="text/css" href="../style.jsp?workgroup=<%= workgroup %>"/>

        <script language="JavaScript" type="text/javascript" src="../common.js"></script>

        <title>Leave A Message</title>
<%
    if (offlineSettings.redirects()){
%>
        <script>
            window.opener.location.href = "<%= offlineSettings.getRedirectURL() %>";
            window.close();
        </script>

<%  return;
    }
%>

<%


    String offlineText = (String) offlineSettings.getOfflineText();
    if (!ModelUtil.hasLength(offlineText)) {
        offlineText = "Leave A Message";
    }

    // Handle if no email account is configured.
    if(!isEmailConfigured || !offlineSettings.isConfigured()){
        response.sendRedirect("offline-mail.jsp?workgroup="+workgroup);
        return;
    }

    boolean hasSubmitted = ModelUtil.hasLength(request.getParameter("submit"));

    // Specify subject to use.
    String subject = (String) offlineSettings.getSubject();
    if (!ModelUtil.hasLength(subject)) {
        subject = "No subject.";
    }

    // Specify body of email.
    String body = ParamUtils.getParameter(request, "message");
    String from = ParamUtils.getParameter(request, "fromEmail");
    String visitorsName = ParamUtils.getParameter(request, "name");
    String company = ParamUtils.getParameter(request, "company");
    if(!ModelUtil.hasLength(visitorsName)){
        visitorsName = "Not specified.";
    }

    if(!ModelUtil.hasLength(company)){
        company = "Not specified.";
    }
%>



<%
    // Modify body for this message.
    StringBuffer buf = new StringBuffer();
    buf.append("From: "+visitorsName);
    buf.append("\\n");
    buf.append("Email Address: "+from);
     buf.append("\\n\\nMessage:"+body);

    Map data = (Map)session.getAttribute("metadata");
    if(data != null){
        buf.append("\\n\\nAssociated Data\\n");
        Iterator iter = data.keySet().iterator();
        while(iter.hasNext()){
            String key = (String)iter.next();
            String value = (String)data.get(key);
            buf.append("\\n\\n"+key + " = "+value);
        }
    }

    String sendValue = StringUtils.escapeForXML(buf.toString());

%>

<%
    if (hasSubmitted) {
        success = offlineWorkgroup.sendMail(offlineSettings.getEmailAddress(),
                        from,
                        subject,
                        sendValue,
                        false);
    }
%>


        <script language="javascript">
            function ValidateForm() {
                if (! isValidEmail(document.f.fromEmail.value)) {
                    alert("Please enter a valid email address.");
                    document.f.fromEmail.focus();
                    return false;
                }


                if ((! Jtrim(document.f.message.value).length)) {
                    alert("Please enter a question.");
                    document.f.message.focus();
                    return false;
                }

                return true;
            }

            function isValidEmail(str) {

                var apos = str.indexOf("@");
                var dpos = str.indexOf(".");
                var epos = str.indexOf("]");
                var fpos = str.indexOf("[");
                if (apos <= 0 || dpos <= 0 || epos > 0 || fpos > 0)
                    return false;
                if (dpos + 1 >= str.length)
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
         <tr>
         <td colspan="2"> <img src="../getimage?image=logo&workgroup=<%= workgroup %>"/></td>
         </tr>
<%
        if (!hasSubmitted) {
%>

                    <FORM name="f" action="leave-a-message.jsp" method="post" onSubmit="return ValidateForm();">
                        <input type="hidden" name="workgroup" value="<%= workgroup %>"/>
                            <tr><td><br/></td></tr>
                            <tr>
                                <td  colspan="2">
                                    <%= offlineText %>
                                    <br>
                                    <br>
                            </tr>

                            <!-- Name Field -->
                             <tr>
                                <td  width="1%" nowrap>
                                    Name:
                                </td>
                                 <td>
                                 <input type="text" maxLength="100" style="width:75%" name="name"/>
                              </td>
                             </tr>

                             <tr>
                           <!-- Email Field -->
                           <td  nowrap>
                             Email: <span class="error">*</span>
                           </td>
                           <td>
                                <INPUT maxLength="100" style="width:75%" name="fromEmail"/>
                            </td>

                           </tr>

                           <tr valign="top">
                                <td  nowrap>
                                    Question: <span class="error">*</span>
                                </td>
                                  <td>
                                    <textarea cols="40" rows="4" style="width:75%" name="message"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td >
                                    <input type="submit" name="submit" value="Send Mail"/>&nbsp;
                                     <input type="button" name="leave" value="Close Window"
                           onclick="window.close();">
                                </td>
                            </tr>

                </FORM>

<%
        }
%>

<%
        if (success) {
%>
                    <tr>
                        <td height="1%" align="center">
                            Your message has been sent. Thank you for contacting us.
                        </td>
                    </tr>
                    <tr>
                    <td height="1%" align="center">
                      <form>
                         <input type="submit" name="leave" value="Close Window"
                           onclick="window.close();">
                      </form>
                    </td>
                    </tr>

<%
        }
        else if (!success && hasSubmitted) {
%>

                    <tr>
                        <td>
                            The message could not be sent.
                        </td>
                    </tr>

                    <tr>
                        <td>
                            Please try back later. Thank you.
                        </td>
                    </tr>
                </table>

                <form>
                    <input type="submit" name="leave" value="Close Window"
                           onclick="window.close();">
                </form>

<%
        }
%>
  <tr>
    <td colspan="2" height="99%" align="right" valign="bottom">

     </td>
   </tr>

      </table>
    <div style="position:absolute;bottom:0px;right:5px"><img src="../getimage?image=poweredby&workgroup=<%= workgroup %>"/></div>
 
    </body>
    </html>

