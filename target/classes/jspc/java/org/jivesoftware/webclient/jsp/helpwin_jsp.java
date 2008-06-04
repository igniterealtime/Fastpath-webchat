package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class helpwin_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/include/global.jsp");
  }

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
      out.write("\r\n\r\n");
  // All global vars defined here

    // Get the branding scheme for this app. This will serve as a suffix for all
    // image names.
    String branding = application.getInitParameter( "branding" );
    if (branding == null || "".equals(branding.trim())) {
        branding = "default";
    }
    String brandingTitle = application.getInitParameter("branding-title");

      out.write("\r\n\r\n<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\r\n <html>\r\n  <head>\r\n   <title>");
      out.print( brandingTitle );
      out.write(" - Help</title>\r\n\r\n   <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\"/>\r\n\r\n   <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n  </head>\r\n\r\n  <body id=\"helpwindow\">\r\n   <div id=\"helpwindow-header\">\r\n    <p>\r\n     ");
      out.print( brandingTitle );
      out.write(" - Help</p>\r\n   </div>\r\n\r\n   <div id=\"helpwindow-content\">\r\n    <iframe name=\"yak\" src=\"help/content.html\" frameborder=\"0\" scrolling=\"auto\" height=\"100%\" width=\"100%\">\r\n    </iframe>\r\n   </div>\r\n\r\n   <div id=\"closebutton\">\r\n    <a href=\"#\" onclick=\"handleClose('Are you sure you want to close this window?');return false;\"\r\n       title=\"Click to close this window.\">\r\n\r\n    <img src=\"images/blank.gif\" width=\"100%\" height=\"100%\" border=\"0\">\r\n\r\n    </a>\r\n   </div>\r\n  </body>\r\n </html>\r\n");
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
