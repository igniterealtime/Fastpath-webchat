package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;

public final class lostconnection_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n\r\n<html>\r\n<head>\r\n  <title>Live Assistant - Connection Lost</title>\r\n\r\n  <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n\r\n  <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\"/>\r\n</head>\r\n\r\n<body id=\"transcriptprintwindow\">\r\n  <div id=\"transcriptprintwindow-header\">\r\n    <p>\r\n        Live Assistant</p>\r\n  </div>\r\n\r\n  <div id=\"transcriptprintwindow-content\">\r\n  </div>\r\n\r\n  <div id=\"closebutton\">\r\n    <a href=\"#\" onclick=\"handleClose('Are you sure you want to close this window?');return false;\"\r\n       title=\"Click to close this window.\">\r\n\r\n    <img src=\"images/blank.gif\" width=\"100%\" height=\"100%\" border=\"0\">\r\n\r\n    </a>\r\n  </div>\r\n\r\n  <div id=\"printbutton\">\r\n    <a href=\"#\" onclick=\"window.print();return false;\" title=\"Click to print the transcript.\">\r\n\r\n    <img src=\"images/blank.gif\" width=\"100%\" height=\"100%\" border=\"0\">\r\n\r\n    </a>\r\n  </div>\r\n\r\n  <script language=\"JavaScript\" type=\"text/javascript\">\r\n\r\n    // set the innerHTML of the content div to the value\r\n    // of the transcriptsrc page div from the opener\r\n");
      out.write("    var transcriptDoc = opener.frames['transcriptsrc'].document;\r\n\r\n    var divID = 'transcriptprintwindow-content';\r\n    var srcDiv = getDivByDoc(divID, transcriptDoc);\r\n    var targetDiv = getDiv(divID);\r\n    targetDiv.innerHTML = srcDiv.innerHTML;\r\n  </script>\r\n</body>\r\n</html>\r\n");
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
