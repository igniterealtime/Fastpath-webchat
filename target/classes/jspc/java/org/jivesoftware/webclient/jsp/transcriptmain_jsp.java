package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.ChatSession;
import org.jivesoftware.smack.XMPPConnection;
import java.util.Map;
import org.jivesoftware.webchat.providers.Settings;
import com.jivesoftware.smack.workgroup.user.Workgroup;
import org.jivesoftware.webchat.util.FormText;
import org.jivesoftware.webchat.util.ParamUtils;
import org.jivesoftware.smack.util.StringUtils;

public final class transcriptmain_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      			"fatal.jsp", true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n\r\n\r\n\r\n");

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


      out.write("\r\n\r\n    <html>\r\n    <head>\r\n        <title>Transcript</title>\r\n\r\n        <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\"/>\r\n\r\n        <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n\r\n        <script language=\"javascript\">\r\n            function ValidateForm() {\r\n                if ((! Jtrim(document.emailForm.to.value).length)) {\r\n                    alert(\"Please specify an email address.\");\r\n                    document.emailForm.to.focus();\r\n                    return false;\r\n                }\r\n\r\n                if (! isValidEmail(document.emailForm.to.value)) {\r\n                    alert(\"Please enter a valid email address.\");\r\n                    document.emailForm.to.focus();\r\n                    return false;\r\n                }\r\n                return true;\r\n            }\r\n\r\n            function isValidEmail(str) {\r\n\r\n                var apos = str.indexOf(\"@\");\r\n                var dpos = str.indexOf(\".\");\r\n                var epos = str.indexOf(\"]\");\r\n                var fpos = str.indexOf(\"[\");\r\n");
      out.write("                if (apos <= 0 || epos > 0 || fpos > 0)\r\n                    return false;\r\n\r\n                return true;\r\n            }\r\n\r\n            function Jtrim(st) {\r\n\r\n                var len = st.length;\r\n                var begin = 0, end = len - 1;\r\n                while (st.charAt(begin) == \" \" && begin < len) {\r\n                    begin++;\r\n                }\r\n                while (st.charAt(end) == \" \" && end > begin) {\r\n                    end--;\r\n                }\r\n\r\n                return st.substring(begin, end + 1);\r\n            }\r\n        </script>\r\n    </head>\r\n\r\n    <body style=\"margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px\" id=\"defaultwindow\">\r\n      <table height=\"100%\" width=\"100%\" border=\"0\">\r\n        <tr><td colspan=\"2\" height=\"1%\">\r\n        <img src=\"getimage?image=logo&workgroup=");
      out.print( workgroup );
      out.write("\"/>\r\n        </td></tr>\r\n        ");
 if(!transcriptSent && !ModelUtil.hasLength(to)){ 
      out.write("\r\n            <form name=\"emailForm\" action=\"transcriptmain.jsp\" method=\"post\" onsubmit=\"return ValidateForm();\">\r\n                <!-- Define hidden variables -->\r\n                <input type=\"hidden\" name=\"chatID\" value=\"");
      out.print( chatID );
      out.write("\"/>\r\n\r\n                <input type=\"hidden\" name=\"workgroup\" value=\"");
      out.print( workgroup );
      out.write("\"/>\r\n\r\n                <!-- End of Hidden Variable definitions -->\r\n                 <tr><td><br/></tr>\r\n                    <tr>\r\n                        <td colspan=\"2\" height=\"1%\">\r\n                           ");
      out.print( FormText.getTranscriptText(workgroup));
      out.write("\r\n                        </td>\r\n                    </tr>\r\n\r\n                <tr><td><br/></tr>\r\n\r\n                    <tr>\r\n                        <td height=\"1%\" nowrap width=\"1%\">\r\n                            <b>Email:</b>\r\n                        </td>\r\n\r\n                        <td height=\"1%\">\r\n                            <input type=\"hidden\" name=\"message\" value=\"Here is the message\"/>\r\n\r\n                            <input type=\"text\" size=\"30\" name=\"to\" value=\"");
      out.print( chatSession.getEmailAddress() );
      out.write("\">\r\n                        </td>\r\n                    </tr>\r\n\r\n                    <tr>\r\n                        <td>\r\n                        </td>\r\n\r\n                        <td>\r\n                            <input type=\"image\" src=\"getimage?image=sendemail&workgroup=");
      out.print( workgroup );
      out.write("\" name=\"\"\r\n                                   value=\"\">\r\n                        </td>\r\n                    </tr>\r\n\r\n            </form>\r\n         ");
 } 
      out.write("\r\n\r\n         ");
 if(transcriptSent){ 
      out.write("\r\n             <tr><td height=\"1%\">");
      out.print( FormText.getTranscriptSent(workgroup));
      out.write("</td></tr>\r\n             <tr><td height=\"1%\" align=\"center\"><br/><br/><input type=\"button\" name=\"leave\" value=\"Close Window\"\r\n                           onclick=\"window.close();\"></td></tr>\r\n         ");
 } 
      out.write("\r\n\r\n         ");
 if(!transcriptSent && ModelUtil.hasLength(to)) { 
      out.write("\r\n            <tr><td height=\"1%\">");
      out.print( FormText.getTranscriptNotSent(workgroup) );
      out.write("</td></tr>\r\n             <tr><td height=\"1%\" align=\"center\"><br/><br/><input type=\"button\" name=\"leave\" value=\"Close Window\"\r\n                           onclick=\"window.close();\"></td></tr>\r\n         ");
 } 
      out.write("\r\n            <tr><td height=\"99%\" align=\"right\" valign=\"bottom\" colspan=\"2\">\r\n             \r\n            </td></tr>\r\n             </table>\r\n      <div style=\"position:absolute;bottom:0px;right:5px\"><img src=\"getimage?image=poweredby&workgroup=");
      out.print( workgroup );
      out.write("\"/></div>\r\n\r\n    </body>\r\n    </html>");
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
