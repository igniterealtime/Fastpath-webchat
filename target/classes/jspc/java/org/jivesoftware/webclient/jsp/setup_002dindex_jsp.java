package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.ParamUtils;
import java.util.Map;
import java.util.HashMap;
import java.net.InetAddress;
import org.jivesoftware.smack.XMPPConnection;
import java.util.Iterator;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPException;

public final class setup_002dindex_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n\r\n\r\n \r\n \r\n \r\n\r\n");
  // Get parameters
    String domain = ParamUtils.getParameter(request,"domain");
    int port = ParamUtils.getIntParameter(request,"port",-1);

    boolean doSave = request.getParameter("save") != null;

    // handle a continue request:
    Map errors = new HashMap();
    if (doSave) {
        // Validate parameters
        if (domain == null) {
            errors.put("domain","The server host is invalid.");
        }
        if (port < 0) {
            errors.put("port","Please specify a valid port.");
        }

        try {
            ConnectionConfiguration xmppConfig = new ConnectionConfiguration(domain, port);
            XMPPConnection con = new XMPPConnection(xmppConfig);
            con.connect();
            con.loginAnonymously();
        }
        catch (XMPPException xe) {
            // If anonymous login disabled.
            if (xe.getXMPPError().getCode() == 403) {
                errors.put("connect", "Anonymous login test failed. Ensure that anonymous logins are enabled on the server.");
            }
            else {
                errors.put("connect", "Could not connect to server. Please check that the domain and port are valid.");
            }
        }
        catch (Exception ex) {
            errors.put("connect", "Could not connect to server. Please check that the domain and port are valid.");
        }

        // Continue if there were no errors
        if (errors.size() == 0) {
            Map xmppSettings = new HashMap();

            xmppSettings.put("xmpp.domain",domain);
            xmppSettings.put("xmpp.socket.plain.port",Integer.toString(port));
            session.setAttribute("xmppSettings", xmppSettings);

            // successful, so redirect
            response.sendRedirect("setup-finished.jsp");
            return;
        }
    }

    // Load the current values:
    if (!doSave) {
        // If the domain is still blank, guess at the value:
        if (domain == null) {
            domain = InetAddress.getLocalHost().getHostName().toLowerCase();
        }
    }

      out.write("\r\n\r\n<html>\r\n<head>\r\n\t<title>Webchat Setup</title>\r\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"setup-style.css\">\r\n    <script language=\"javascript\" type=\"text/javascript\" src=\"js/tooltips/domLib.js\"></script>\r\n    <script language=\"javascript\" type=\"text/javascript\" src=\"js/tooltips/domTT.js\"></script>\r\n</head>\r\n\r\n<body>\r\n\r\n<!-- BEGIN jive-header -->\r\n<div id=\"jive-header\">\r\n\t<div id=\"jive-header-text\">Webchat Setup</div>\r\n\t<div id=\"sidebar-top\"></div>\r\n</div>\r\n<!-- END jive-header -->\r\n\r\n<!-- BEGIN jive-body -->\r\n<div id=\"jive-body\">\r\n\r\n<p>\r\nWelcome to the Webchat setup tool. Before the webchat application can be used, you must connect it\r\nto a Openfire Enterprise server.\r\n</p>\r\n\r\n");
 if (errors.get("connect") != null) { 
      out.write("\r\n<div id=\"errorMessage\" class=\"error\">\r\n    ");
      out.print( errors.get("connect") );
      out.write("\r\n</div>\r\n");
 } 
      out.write("\r\n\r\n<div class=\"jive-contentBox\">\r\n\r\n<form action=\"setup-index.jsp\" name=\"f\" method=\"post\">\r\n\r\n<script langauge=\"JavaScript\" type=\"text/javascript\">\r\nfunction toggle(form,disabled) {\r\n    form.sslPort.disabled = disabled;\r\n}\r\n</script>\r\n\r\n<table cellpadding=\"3\" cellspacing=\"0\" border=\"0\" width=\"100%\">\r\n\r\n<tr valign=\"top\">\r\n    <td width=\"1%\" nowrap>\r\n        Server Host:\r\n        ");
  if (errors.get("domain") != null) { 
      out.write("\r\n\r\n            <span class=\"jive-error-text\"><br>\r\n            ");
      out.print( errors.get("domain") );
      out.write("\r\n            </span>\r\n\r\n        ");
  } 
      out.write("\r\n    </td>\r\n    <td width=\"99%\">\r\n        <input type=\"text\" size=\"30\" maxlength=\"150\" name=\"domain\"\r\n         value=\"");
      out.print( ((domain != null) ? domain : "") );
      out.write("\">\r\n        <span class=\"jive-setup-helpicon\" onmouseover=\"domTT_activate(this, event, 'content', 'Hostname or IP address of the IM server.', 'styleClass', 'jiveTooltip', 'trail', true, 'delay', 300, 'lifetime', 8000);\"></span>\r\n    </td>\r\n</tr>\r\n<tr valign=\"top\">\r\n    <td width=\"1%\" nowrap>\r\n        Server Port:\r\n        ");
  if (errors.get("port") != null) { 
      out.write("\r\n\r\n            <span class=\"jive-error-text\"><br>\r\n            Invalid port number.\r\n            </span>\r\n\r\n        ");
  } 
      out.write("\r\n    </td>\r\n    <td width=\"99%\">\r\n        <input type=\"text\" size=\"6\" maxlength=\"6\" name=\"port\"\r\n         value=\"");
      out.print( ((port != -1) ? ""+port : "5222") );
      out.write("\">\r\n        <span class=\"jive-setup-helpicon\" onmouseover=\"domTT_activate(this, event, 'content', 'Port number the XMPP server listens on. Default XMPP port is 5222.', 'styleClass', 'jiveTooltip', 'trail', true, 'delay', 300, 'lifetime', 8000);\"></span>\r\n    </td>\r\n</tr>\r\n\r\n</table>\r\n\r\n<br><br>\r\n\r\n<div align=\"right\">\r\n    <input type=\"submit\" name=\"save\" value=\"Save Settings \" id=\"jive-setup-save\" border=\"0\">\r\n</div>\r\n</form>\r\n\r\n<script language=\"JavaScript\" type=\"text/javascript\">\r\n// give focus to domain field\r\ndocument.f.domain.focus();\r\n</script>\r\n\r\n</div>\r\n\r\n</div>\r\n<!-- END jive-body -->\r\n\r\n<!-- BEGIN jive-footer -->\r\n<div id=\"jive-footer\"></div>\r\n<!-- END jive-footer -->\r\n\r\n</body>\r\n</html>");
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
