package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.actions.ChatStarter;
import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.ParamUtils;
import org.jivesoftware.webchat.ChatSession;
import org.jivesoftware.webchat.ChatManager;

public final class queue_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n\r\n\r\n\r\n");

    ChatStarter chatStarter = new ChatStarter();

      out.write('\r');
      out.write('\n');

    final String workgroup = ParamUtils.getParameter(request, "workgroup");
    // Add workgroup to session
    session.setAttribute("workgroup", workgroup);
    final String chatID = ParamUtils.getParameter(request, "chatID");
    final String question = ParamUtils.getParameter(request, "question");
    session.setAttribute("chatID", chatID);
    if (ModelUtil.hasLength(question)) {
        session.setAttribute("Question", question);
    }
    if (!WorkgroupStatus.isOnline(workgroup)) {
        response.sendRedirect("email/leave-a-message.jsp?workgroup=" + workgroup);
        return;
    }
    chatStarter.init(pageContext);
    // Define the starting paramters for this chat session

    /**
     * @param workgroup the name of the workgroup to join.
     * @param chatID the unique id for this session.
     */
    chatStarter.startSession(workgroup, chatID);

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
