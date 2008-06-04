package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Map;
import java.util.Iterator;
import org.jivesoftware.webchat.settings.ConnectionSettings;
import java.beans.XMLEncoder;
import java.io.File;
import org.jivesoftware.webchat.settings.ChatSettingsManager;
import org.jivesoftware.webchat.util.Base64;
import org.jivesoftware.webchat.FastpathServlet;
import org.jivesoftware.webchat.ChatManager;

public final class setup_002dfinished_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n\r\n");

    // update the sidebar status
    session.setAttribute("jive.setup.sidebar.1", "done");
    session.setAttribute("jive.setup.sidebar.2", "done");
    session.setAttribute("jive.setup.sidebar.3", "done");
    session.setAttribute("jive.setup.sidebar.4", "in_progress");

    boolean showSidebar = false;
    // Define settings file location

    File settingsFile = FastpathServlet.SETTINGS_FILE;

    ChatManager chatManager = ChatManager.getInstance();
    ChatSettingsManager settingsManager = new ChatSettingsManager(settingsFile);
    ConnectionSettings settings = settingsManager.getSettings();
    if (settings == null) {
        settings = new ConnectionSettings();
    }
    chatManager.setChatSettingsManager(settingsManager);

    // First, update with XMPPSettings
    Map xmppSettings = (Map)session.getAttribute("xmppSettings");
    String domain = (String)xmppSettings.get("xmpp.domain");
    String port = (String)xmppSettings.get("xmpp.socket.plain.port");
    String sslEnabled = (String)xmppSettings.get("xmpp.socket.ssl.active");
    String ssl = (String)xmppSettings.get("xmpp.socket.ssl.port");
    String password = (String)xmppSettings.get("presencebot");

    int sslPort = -1;
    boolean isSSLEnabled = Boolean.valueOf(sslEnabled).booleanValue();
    if (isSSLEnabled) {
        sslPort = Integer.parseInt(ssl);
        settings.setSSLEnabled(true);
        settings.setSSLPort(sslPort);
    }
    settings.setServerDomain(domain);
    settings.setPort(Integer.parseInt(port));

    settingsManager.save(settings);

      out.write("\r\n\r\n\r\n<html>\r\n<head>\r\n\t<title>Webchat Setup</title>\r\n\t<link rel=\"stylesheet\" type=\"text/css\" href=\"setup-style.css\">\r\n    <script language=\"javascript\" type=\"text/javascript\" src=\"js/tooltips/domLib.js\"></script>\r\n    <script language=\"javascript\" type=\"text/javascript\" src=\"js/tooltips/domTT.js\"></script>\r\n    <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n</head>\r\n\r\n<body>\r\n\r\n<!-- BEGIN jive-header -->\r\n<div id=\"jive-header\">\r\n\t<div id=\"jive-header-text\">Webchat Setup</div>\r\n\t<div id=\"sidebar-top\"></div>\r\n</div>\r\n<!-- END jive-header -->\r\n\r\n<!-- BEGIN jive-body -->\r\n<div id=\"jive-body\">\r\n\r\n<p>\r\n    Webchat installation is now complete. To test your installation, click on the following image:\r\n</p>\r\n\r\n    <script language=\"JavaScript\" type=\"text/javascript\" src=\"jivelive.jsp\"></script>\r\n    <script>\r\n        showChatButton('demo@workgroup.");
      out.print( settings.getServerDomain());
      out.write("');\r\n    </script>\r\n\r\n</div>\r\n\r\n<!-- END jive-body -->\r\n\r\n<!-- BEGIN jive-footer -->\r\n<div id=\"jive-footer\"></div>\r\n<!-- END jive-footer -->\r\n\r\n</body>\r\n</html>\r\n\r\n\r\n\r\n");
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
