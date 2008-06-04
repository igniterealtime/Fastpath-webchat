package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.ParamUtils;
import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.packet.Presence;

public final class contact_002dagent_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n<html>\r\n<head>\r\n <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n</head>\r\n<body>\r\n\r\n</body>\r\n</html>\r\n");

   String email = request.getParameter("email");
   String agentName = request.getParameter("name");
   String jid = request.getParameter("jid");

   boolean isAgentOnline = WorkgroupStatus.isAgentOnline(jid);

   if(isAgentOnline){
       response.sendRedirect("agentinfo.jsp?agentName="+agentName+"&jid="+jid+"&email="+email);
       return;
   }
   else {
        
      out.write("<a href=\"mailto:");
      out.print( email);
      out.write("\"><img src=\"images/personal_offline.gif\" border=\"0\"></a>");

   }

      out.write("\r\n\r\n\r\n");
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
