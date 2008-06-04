package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.WebLog;
import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;

public final class chat_002dended_jsp extends org.apache.jasper.runtime.HttpJspBase
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

    String workgroup = request.getParameter("workgroup");

      out.write("\r\n\r\n    <html>\r\n    <head>\r\n      <title>Chat Ended</title>\r\n\r\n      <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\"/>\r\n    </head>\r\n\r\n    <body style=\"margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px\" id=\"defaultwindow\">\r\n      <table width=\"100%\">\r\n          <tr><td>\r\n              ");
 if(workgroup != null){ 
      out.write("\r\n                <img src=\"getimage?image=logo&workgroup=");
      out.print( workgroup );
      out.write("\"/>\r\n              ");
 } 
      out.write("\r\n\r\n\r\n              </td></tr>\r\n          <tr><td><br/></td></tr>\r\n          <tr><td><b>Chat Ended</b></td></tr>\r\n          <tr><td>Thank you for using our chat service.</td></tr>\r\n      </table>\r\n\r\n    </body>\r\n    </html>");
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
