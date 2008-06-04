package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.ParamUtils;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.ChatSession;

public final class submitter_jsp extends org.apache.jasper.runtime.HttpJspBase
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

    final String chatID = request.getParameter("chatID");
    ChatManager chatManager = ChatManager.getInstance();
    ChatSession chatSession = chatManager.getChatSession(chatID);

    // Handle Parameters
    String message = ParamUtils.getParameter(request, "message");
    String isTyping = ParamUtils.getParameter(request, "isTyping");
    String leaving = ParamUtils.getParameter(request, "left");

      out.write("\r\n\r\n    <html>\r\n    <body>\r\n\r\n        <!-- form to submit chat text -->\r\n        <form name=\"chatform\" action=\"submitter.jsp\" method=\"post\">\r\n            <input type=\"hidden\" name=\"action\" value=\"write\">\r\n            <input type=\"hidden\" name=\"isTyping\">\r\n            <input type=\"hidden\" name=\"left\">\r\n            <input type=\"hidden\" name=\"chatID\">\r\n            <input type=\"hidden\" name=\"message\" value=\"\">\r\n        </form>\r\n\r\n        <form name=\"closeForm\" action=\"submitter.jsp\" method=\"post\">\r\n            <input type=\"hidden\" name=\"close\" value=\"close\">\r\n        </form>\r\n\r\n");

        if (ModelUtil.hasLength(message)) {

        }
        if (ModelUtil.hasLength(isTyping)) {
         
        }

        if (ModelUtil.hasLength(leaving)) {
          chatManager.closeChatSession(chatID);
        }

      out.write("\r\n    </body>\r\n    </html>\r\n");
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
