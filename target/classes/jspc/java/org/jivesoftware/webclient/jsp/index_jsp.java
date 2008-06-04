package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.webchat.settings.ConnectionSettings;
import org.jivesoftware.smack.XMPPConnection;
import java.util.Collection;
import java.util.Iterator;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
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

    if (!Boolean.valueOf(System.getProperty("isdemo")).booleanValue()) {

    }

      out.write("\r\n\r\n<html>\r\n<head>\r\n\t<title>Fastpath Web Chat</title>\r\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"setup-style.css\">\r\n    <script language=\"javascript\" type=\"text/javascript\" src=\"js/tooltips/domLib.js\"></script>\r\n    <script language=\"javascript\" type=\"text/javascript\" src=\"js/tooltips/domTT.js\"></script>\r\n    <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n     <script language=\"JavaScript\" type=\"text/javascript\" src=\"jivelive.jsp\"></script>\r\n</head>\r\n\r\n<body>\r\n\r\n<!-- BEGIN jive-header -->\r\n<div id=\"jive-header\">\r\n\t<div id=\"jive-header-text\">Fastpath Web Chat</div>\r\n\t<div id=\"sidebar-top\"></div>\r\n</div>\r\n<!-- END jive-header -->\r\n\r\n<!-- BEGIN jive-body -->\r\n<div id=\"jive-body\">\r\n\r\n\r\n<table cellspacing=\"0\" cellpadding=\"4\" width=\"100%\" border=\"0\">\r\n<tbody>\r\n    <tr valign=\"top\">\r\n\r\n    <td width=\"99%\">\r\n\r\n        <b>List of available workgroups within Fastpath</b>\r\n        <br/><br/>\r\n        \r\n        <p>\r\n        Click on one of the following workgroups to join.\r\n        </p>\r\n");
      out.write("\r\n        <div class=\"icons\">\r\n        <table cellspacing=\"0\" cellpadding=\"4\" border=\"0\">\r\n        <tbody>\r\n            ");
 int count = 1;
                Collection workgroupList = null;
                Iterator workgroups = null;
                try {
                    workgroupList = WorkgroupStatus.getWorkgroupNames();
                    workgroups = workgroupList.iterator();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                while (workgroups != null && workgroups.hasNext()) {
                    String workgroupName = (String)workgroups.next();
            
      out.write("\r\n                <tr>\r\n                    <td>");
      out.print( count++ );
      out.write(". </td>\r\n                    <td>Workgroup: <b>");
      out.print( workgroupName );
      out.write("</b></td>\r\n                    <td>\r\n                        <script>\r\n                        showChatButton('");
      out.print( workgroupName);
      out.write("@workgroup.");
      out.print( WorkgroupStatus.getHost() );
      out.write("');\r\n                        </script>\r\n                    </td>\r\n                </tr>\r\n            ");
 } 
      out.write("\r\n\r\n            ");

                if (workgroupList == null || workgroupList.size() == 0) {
                    XMPPConnection con = ChatManager.getInstance().getGlobalConnection();
                    ConnectionSettings settings = ChatManager.getInstance().getChatSettingsManager().getSettings();

                    if (con == null || !con.isConnected()) {
            
      out.write("\r\n                            <tr><td><font color=\"red\">Unable to connect to server using the following settings: </font></td></tr>\r\n                            <tr><td><b>Server:</b> ");
      out.print( settings.getServerDomain());
      out.write("</td></tr>\r\n                            <tr><td><b>Port:</b> ");
      out.print( settings.isSSLEnabled() ? settings.getSSLPort() : settings.getPort());
      out.write("</td></tr>\r\n                             <tr><td><b>SSL Enabled:</b> ");
      out.print( settings.isSSLEnabled() ? "true" : "false" );
      out.write("</td></tr>\r\n                            <tr><td><a href=\"test-connection.jsp\">Test Connection</a></td></tr>\r\n                            <tr><td><br/>Please change your server settings in the setup tool.</td></tr>\r\n                        ");

                                }

                            }
                        
      out.write("\r\n        </tbody>\r\n        </table>\r\n        </div>\r\n\r\n    </td>\r\n    </tr>\r\n</tbody>\r\n</table>\r\n\r\n\r\n<!-- END jive-body -->\r\n</div>\r\n<!-- BEGIN jive-footer -->\r\n<div id=\"jive-footer\"></div>\r\n<!-- END jive-footer -->\r\n</body>\r\n</html>");
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
