package org.jivesoftware.webclient.jsp.email;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.FormText;

public final class offline_002dmail_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write('\r');
      out.write('\n');

     String workgroup = request.getParameter("workgroup");
     String offlineText = FormText.getNoHelpText(workgroup);


      out.write("\r\n    <html>\r\n    <head>\r\n        <title>Offline</title>\r\n        <link rel=\"stylesheet\"\r\n                     type=\"text/css\"\r\n                     href=\"../style.jsp?workgroup=");
      out.print( workgroup );
      out.write("\"/><script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\">//Ignore</script>\r\n\r\n  </head>\r\n   <body style=\"margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px\" id=\"defaultwindow\">\r\n      <table width=\"100%\" border=\"0\" height=\"100%\">\r\n      <tr>\r\n      <td height=\"1%\" >\r\n       <img alt=\"workgroup image\" src=\"../getimage?image=logo&workgroup=");
      out.print( workgroup );
      out.write("\"/>\r\n      </td>\r\n      </tr>\r\n      <tr>\r\n        <td height=\"1%\"><br/>");
      out.print( offlineText);
      out.write("<br/><br/><br/></td>\r\n      </tr>\r\n      <tr>\r\n      <td align=\"center\">\r\n        <form action=\"offline-mail.jsp\">\r\n                        <input type=\"submit\" name=\"leave\" value=\"Close Window\"  onclick=\"window.close();\">\r\n        </form>\r\n      </td>\r\n      </tr>\r\n      <tr valign=\"bottom\" height=\"99%\">\r\n      <td align=\"right\"></td>\r\n      </tr>\r\n      </table>\r\n    <div style=\"position:absolute;bottom:0px;right:5px\"><img src=\"../getimage?image=poweredby&workgroup=");
      out.print( workgroup );
      out.write("\"/></div>\r\n  </body>\r\n</html>\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
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
