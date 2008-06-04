package org.jivesoftware.webclient.jsp.email;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.ParamUtils;
import org.jivesoftware.webchat.ChatSession;
import org.jivesoftware.smack.util.StringUtils;
import com.jivesoftware.smack.workgroup.user.Workgroup;
import com.jivesoftware.smack.workgroup.settings.OfflineSettings;
import org.jivesoftware.smack.XMPPException;
import java.util.Map;
import java.util.Iterator;

public final class leave_002da_002dmessage_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			"../fatal.jsp", true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n\r\n");

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

      out.write("\r\n\r\n    <html>\r\n    <head>\r\n        <link rel=\"stylesheet\" type=\"text/css\" href=\"../style.jsp?workgroup=");
      out.print( workgroup );
      out.write("\"/>\r\n\r\n        <script language=\"JavaScript\" type=\"text/javascript\" src=\"../common.js\"></script>\r\n\r\n        <title>Leave A Message</title>\r\n");

    if (offlineSettings.redirects()){

      out.write("\r\n        <script>\r\n            window.opener.location.href = \"");
      out.print( offlineSettings.getRedirectURL() );
      out.write("\";\r\n            window.close();\r\n        </script>\r\n\r\n");
  return;
    }

      out.write("\r\n\r\n");



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

      out.write("\r\n\r\n\r\n\r\n");

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


      out.write("\r\n\r\n");

    if (hasSubmitted) {
        success = offlineWorkgroup.sendMail(offlineSettings.getEmailAddress(),
                        from,
                        subject,
                        sendValue,
                        false);
    }

      out.write("\r\n\r\n\r\n        <script language=\"javascript\">\r\n            function ValidateForm() {\r\n                if (! isValidEmail(document.f.fromEmail.value)) {\r\n                    alert(\"Please enter a valid email address.\");\r\n                    document.f.fromEmail.focus();\r\n                    return false;\r\n                }\r\n\r\n\r\n                if ((! Jtrim(document.f.message.value).length)) {\r\n                    alert(\"Please enter a question.\");\r\n                    document.f.message.focus();\r\n                    return false;\r\n                }\r\n\r\n                return true;\r\n            }\r\n\r\n            function isValidEmail(str) {\r\n\r\n                var apos = str.indexOf(\"@\");\r\n                var dpos = str.indexOf(\".\");\r\n                var epos = str.indexOf(\"]\");\r\n                var fpos = str.indexOf(\"[\");\r\n                if (apos <= 0 || dpos <= 0 || epos > 0 || fpos > 0)\r\n                    return false;\r\n                if (dpos + 1 >= str.length)\r\n                    return false;\r\n\r\n                return true;\r\n");
      out.write("            }\r\n\r\n            function Jtrim(st) {\r\n\r\n                var len = st.length;\r\n                var begin = 0, end = len - 1;\r\n                while (st.charAt(begin) == \" \" && begin < len) {\r\n                    begin++;\r\n                }\r\n                while (st.charAt(end) == \" \" && end > begin) {\r\n                    end--;\r\n                }\r\n\r\n                return st.substring(begin, end + 1);\r\n            }\r\n        </script>\r\n    </head>\r\n\r\n   <body style=\"margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px\" id=\"defaultwindow\">\r\n       <table height=\"100%\" width=\"100%\" border=\"0\">\r\n         <tr>\r\n         <td colspan=\"2\"> <img src=\"../getimage?image=logo&workgroup=");
      out.print( workgroup );
      out.write("\"/></td>\r\n         </tr>\r\n");

        if (!hasSubmitted) {

      out.write("\r\n\r\n                    <FORM name=\"f\" action=\"leave-a-message.jsp\" method=\"post\" onSubmit=\"return ValidateForm();\">\r\n                        <input type=\"hidden\" name=\"workgroup\" value=\"");
      out.print( workgroup );
      out.write("\"/>\r\n                            <tr><td><br/></td></tr>\r\n                            <tr>\r\n                                <td  colspan=\"2\">\r\n                                    ");
      out.print( offlineText );
      out.write("\r\n                                    <br>\r\n                                    <br>\r\n                            </tr>\r\n\r\n                            <!-- Name Field -->\r\n                             <tr>\r\n                                <td  width=\"1%\" nowrap>\r\n                                    Name:\r\n                                </td>\r\n                                 <td>\r\n                                 <input type=\"text\" maxLength=\"100\" style=\"width:75%\" name=\"name\"/>\r\n                              </td>\r\n                             </tr>\r\n\r\n                             <tr>\r\n                           <!-- Email Field -->\r\n                           <td  nowrap>\r\n                             Email: <span class=\"error\">*</span>\r\n                           </td>\r\n                           <td>\r\n                                <INPUT maxLength=\"100\" style=\"width:75%\" name=\"fromEmail\"/>\r\n                            </td>\r\n\r\n                           </tr>\r\n\r\n                           <tr valign=\"top\">\r\n");
      out.write("                                <td  nowrap>\r\n                                    Question: <span class=\"error\">*</span>\r\n                                </td>\r\n                                  <td>\r\n                                    <textarea cols=\"40\" rows=\"4\" style=\"width:75%\" name=\"message\"></textarea>\r\n                                </td>\r\n                            </tr>\r\n                            <tr>\r\n                                <td></td>\r\n                                <td >\r\n                                    <input type=\"submit\" name=\"submit\" value=\"Send Mail\"/>&nbsp;\r\n                                     <input type=\"button\" name=\"leave\" value=\"Close Window\"\r\n                           onclick=\"window.close();\">\r\n                                </td>\r\n                            </tr>\r\n\r\n                </FORM>\r\n\r\n");

        }

      out.write("\r\n\r\n");

        if (success) {

      out.write("\r\n                    <tr>\r\n                        <td height=\"1%\" align=\"center\">\r\n                            Your message has been sent. Thank you for contacting us.\r\n                        </td>\r\n                    </tr>\r\n                    <tr>\r\n                    <td height=\"1%\" align=\"center\">\r\n                      <form>\r\n                         <input type=\"submit\" name=\"leave\" value=\"Close Window\"\r\n                           onclick=\"window.close();\">\r\n                      </form>\r\n                    </td>\r\n                    </tr>\r\n\r\n");

        }
        else if (!success && hasSubmitted) {

      out.write("\r\n\r\n                    <tr>\r\n                        <td>\r\n                            The message could not be sent.\r\n                        </td>\r\n                    </tr>\r\n\r\n                    <tr>\r\n                        <td>\r\n                            Please try back later. Thank you.\r\n                        </td>\r\n                    </tr>\r\n                </table>\r\n\r\n                <form>\r\n                    <input type=\"submit\" name=\"leave\" value=\"Close Window\"\r\n                           onclick=\"window.close();\">\r\n                </form>\r\n\r\n");

        }

      out.write("\r\n  <tr>\r\n    <td colspan=\"2\" height=\"99%\" align=\"right\" valign=\"bottom\">\r\n\r\n     </td>\r\n   </tr>\r\n\r\n      </table>\r\n    <div style=\"position:absolute;bottom:0px;right:5px\"><img src=\"../getimage?image=poweredby&workgroup=");
      out.print( workgroup );
      out.write("\"/></div>\r\n \r\n    </body>\r\n    </html>\r\n\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
