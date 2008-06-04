package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Enumeration;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.ParamUtils;
import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.webchat.util.StringUtils;

public final class start_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n\r\n");

    String workgroup = ParamUtils.getParameter(request, "workgroup");

    // Get Agent and add to session
    String agent = ParamUtils.getParameter(request, "agent");

    // Get Page Location and add to session.
    String pageLocation = ParamUtils.getParameter(request, "location");

    // Does the user want to go directly to the Queue?
    boolean noUI = ParamUtils.getBooleanParameter(request, "noUI");

    StringBuffer paramString = new StringBuffer();

    // Generate a chatID. This id will be used to identify the user throughout the entire
    // chat process.
    String chatID = StringUtils.randomString(10);
    paramString.append("chatID="+chatID);

    if(ModelUtil.hasLength(workgroup)){
        paramString.append("&workgroup="+workgroup);
    }

    if(ModelUtil.hasLength(agent)){
        paramString.append("&agent="+agent);
    }

    if(ModelUtil.hasLength(pageLocation)){
        session.setAttribute("pageLocation", pageLocation);
    }

    if (WorkgroupStatus.isOnline(workgroup)) {
        if (noUI) {
            Enumeration requestEnum = request.getParameterNames();
            while (requestEnum.hasMoreElements()) {
                String name = (String) requestEnum.nextElement();
                String value = request.getParameter(name);
                if(name.equalsIgnoreCase("username")){
                    name = "username";
                }
                if(name.equalsIgnoreCase("email")){
                    name = "email";
                }
                paramString.append("&" + name + "=" + value);
            }

            String dest = paramString.toString();

            if(!ModelUtil.hasLength(workgroup)){
                out.println("A workgroup must be specified.");
                return;
            }

            String name = request.getParameter("username");
            if(!ModelUtil.hasLength(name)){
                out.println("A username must be specified.");
                return;
            }

            response.sendRedirect("queue.jsp?"+dest);
        }
        else{
            response.sendRedirect("userinfo.jsp?"+paramString.toString());
        }
    }
    else{
        response.sendRedirect("email/leave-a-message.jsp?"+paramString.toString());
    }

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
