package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.ParamUtils;

public final class request_002dagent_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n<html>\r\n<head>\r\n  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">\r\n\r\n  <title>Contact Agent Form</title>\r\n\r\n");

  final String agentName = ParamUtils.getParameter(request, "agent", true);
  final String workgroupName = ParamUtils.getParameter(request, "workgroup", true);

      out.write("\r\n</head>\r\n\r\n<body onload=\"showDialog();\">\r\n  <script language=\"JavaScript\" type=\"text/javascript\"\r\n          src=\"http://liveassist.jivesoftware.com/liveassistant/common.js\"></script>\r\n\r\n  <script language=\"JavaScript\">\r\n    function showDialog() {\r\n      var laServer = 'http://liveassist.jivesoftware.com/liveassistant/LiveAssistantServlet?action=init&agent=");
      out.print( agentName );
      out.write("&workgroup=");
      out.print( workgroupName );
      out.write("@jivesoftware.com&location='\r\n              + window.location.href;\r\n      launchWin('framemain', laServer, 409, 376);\r\n    }\r\n  </script>\r\n\r\n  If you do not see a web chat window, <a href=\"javascript:showDialog();\">Click here to contact ");
      out.print( agentName );
      out.write("</a>\r\n</body>\r\n</html>\r\n");
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
