package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.history.*;
import java.util.*;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.ChatSession;
import org.jivesoftware.webchat.util.ModelUtil;

public final class transcriptsrc_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n");

    String chatID = request.getParameter("chatID");
    ChatManager chatManager = ChatManager.getInstance();
    ChatSession chatSession = chatManager.getChatSession(chatID);
    if(chatSession == null){
        response.sendRedirect("chat-ended.jsp");
    }

    // Get the transcript from the session
    Transcript transcript = chatSession.getTranscript();
    chatManager.closeChatSession(chatID);

      out.write("\r\n\r\n    <html>\r\n    <head>\r\n        <title>Chat Transcripts</title>\r\n\r\n        <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n\r\n        <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\"/>\r\n    </head>\r\n\r\n    <body id=\"transcriptprintwindow\">\r\n        <div id=\"transcriptprintwindow-content\">\r\n            <div class=\"box\">\r\n                <br/>\r\n\r\n                <div id=\"ytext\">\r\n");

                    if (transcript != null) {

      out.write("\r\n\r\n");

                        String question = (String)session.getAttribute("Question");
                        if(ModelUtil.hasLength(question)){
                            
      out.write("<div class=\"chat-line\"><span class=\"chat-announcement\">Question: ");
      out.print( question);
      out.write(" </span>  </div>");

                        }


                        List transcriptLines = transcript.getTranscript();

                        for (int i = 0; i < transcriptLines.size(); i++) {
                            Line line = (Line) transcriptLines.get(i);

                            String from = line.getFrom();
                            String text = line.getText();
                            String cp = request.getContextPath();
                            String full = request.getRequestURL().toString();
                            int  index = full.indexOf(cp);
                            String base = full.substring(0, (index + cp.length()));

                            text = text.replaceAll("src=\"", "src=\"" + base + "/");
                            boolean isAnnouncement = !ModelUtil.hasLength(from);

      out.write("\r\n\r\n                            <div class=\"chat-line\">\r\n");

                                if (isAnnouncement) {

      out.write("\r\n\r\n                                    <span class=\"chat-announcement\"> ");
      out.print( text );
      out.write(" </span>\r\n\r\n");

                                }
                                else{

      out.write("\r\n\r\n                                    <span class=\"");
      out.print( "client-name" );
      out.write('"');
      out.write('>');
      out.write(' ');
      out.print( from );
      out.write(": </span>\r\n                                    <span class=\"text\"> ");
      out.print( text );
      out.write(" </span>\r\n\r\n");

                                }

      out.write("\r\n                            </div>\r\n\r\n");

                        }

      out.write("\r\n\r\n");

                    }

      out.write("\r\n                </div>\r\n            </div>\r\n        </div>\r\n    </body>\r\n    </html>\r\n");
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
