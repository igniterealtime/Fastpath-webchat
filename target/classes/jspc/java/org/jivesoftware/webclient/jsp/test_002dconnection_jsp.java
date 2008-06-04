package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.settings.ConnectionSettings;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import org.jivesoftware.smack.ConnectionConfiguration;

public final class test_002dconnection_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n\n\n\n\n\n\n\n\n\n\n\n<html>\n<head><title>Test Server Connection</title></head>\n\n<body>\n<table width=\"600\">\n    <tr><td colspan=\"2\"><h4>Server Connection Test</h4></td></tr>\n    <tr valign=\"top\"><td><b>Results:</b></td>\n\n        <td>\n            ");

                ConnectionSettings settings = ChatManager.getInstance().getChatSettingsManager().getSettings();
                boolean ok = false;
                XMPPConnection con = null;
                try {
                    ConnectionConfiguration xmppConfig = new ConnectionConfiguration(
                            settings.getServerDomain(), settings.getPort());
                    con = new XMPPConnection(xmppConfig);
                    con.connect();
                    con.loginAnonymously();
                    ok = true;
                }
                catch (XMPPException e) {
                    final Writer result = new StringWriter();
                    final PrintWriter printWriter = new PrintWriter(result);
                    e.printStackTrace(printWriter);
                    out.println("<p>" + result.toString() + "</p>");
                }
                finally {
                    if (con != null && con.isConnected()) {
                        con.disconnect();
                    }
                }
            
      out.write("\n            ");
 if (ok) { 
      out.write("\n            Connected!\n            ");
 } 
      out.write("\n        </td>\n    </tr>\n</table>\n\n\n</body>\n</html>");
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
