package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.Writer;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.jivesoftware.webchat.util.WebLog;

public final class fatal_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


         public static String getStackTrace(Throwable aThrowable) {
            if(aThrowable == null){
                return "";
            }
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            aThrowable.printStackTrace(printWriter);
            return result.toString();
         }
    
  private static java.util.Vector _jspx_dependants;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    Throwable exception = org.apache.jasper.runtime.JspRuntimeLibrary.getThrowable(request);
    if (exception != null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
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

      out.write("\r\n\r\n\r\n\r\n\r\n\r\n");

    if (exception != null) {
        try {
            application.log("Error in Web Client", exception);
            WebLog.logError("Error in web client", exception);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


      out.write("\r\n\r\n    <html>\r\n    <head>\r\n      <title>Chat Service Not Available</title>\r\n\r\n      <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\"/>\r\n    </head>\r\n\r\n    <body id=\"defaultwindow\">\r\n\r\n<table cellspacing=\"0\" cellpadding=\"4\" width=\"100%\" border=\"0\">\r\n<tbody>\r\n    <tr valign=\"top\">\r\n    <td>\r\n        <h3>Online Chat Service</h3>\r\n    </td>\r\n    </tr>\r\n    <tr>\r\n    <td width=\"99%\">\r\n        Our chat service is unavailable at this time. Please check back soon.\r\n      </td>\r\n      </tr>\r\n      <tr>\r\n      <td width=\"1%\" colspan=\"2\">\r\n      ");
      out.print( getStackTrace(exception) );
      out.write("\r\n      </td>\r\n      </tr>\r\n      </table>\r\n    </body>\r\n    </html>\r\n\r\n    ");
      out.write('\r');
      out.write('\n');
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
