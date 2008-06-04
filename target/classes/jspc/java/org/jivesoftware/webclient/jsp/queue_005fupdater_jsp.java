package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.ChatSession;
import org.jivesoftware.webchat.actions.ChatQueue;
import org.jivesoftware.webchat.util.WebUtils;

public final class queue_005fupdater_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n\r\n\r\n");

    String workgroup = request.getParameter("workgroup");
    String chatID = request.getParameter("chatID");

    ChatManager chatManager = ChatManager.getInstance();
    ChatSession chatSession = chatManager.getChatSession(chatID);
    ChatQueue queue = null;

      out.write("\r\n  <html>\r\n   <head>\r\n    <meta http-equiv=\"cache-control\" content=\"no-cache\">\r\n      <meta http-equiv=\"expires\" content=\"0\">\r\n      <script language=\"JavaScript\" type=\"text/javascript\">\r\n\r\n       // Updates the queue position text on the queue page\r\n       function updateQueuePosition(position) {\r\n         var posSpan = window.parent.document.getElementById('queue_position');\r\n\r\n         if (posSpan) {\r\n           posSpan.innerHTML = position;\r\n           window.parent.document.getElementById('queue_info').style.visibility = 'visible';\r\n         }\r\n       }\r\n       // Updates the queue wait time. Time should be in seconds.\r\n       function updateQueueTime(time) {\r\n         var timeSpan = window.parent.document.getElementById('queue_time');\r\n\r\n         if (timeSpan) {\r\n           timeSpan.innerHTML = time;\r\n           window.parent.document.getElementById('queue_info').style.visibility = 'visible';\r\n         }\r\n       }\r\n\r\n       function showNoAnswer() {\r\n         var queueInfoHeader = window.parent.document.getElementById('queue_info_header');\r\n");
      out.write("         queueInfoHeader.innerHTML\r\n             = \"We are unable to route your request at this time. To leave a message or request a call back <a href='email/leave-a-message.jsp?workgroup=");
      out.print( workgroup );
      out.write("'>click here</a>.\";\r\n\r\n         var queueInfoText = window.parent.document.getElementById('queue_info_text');\r\n         queueInfoText.innerHTML = \"\";\r\n\r\n         var queueInfoFooter = window.parent.document.getElementById('queue_info_footer');\r\n         queueInfoFooter.innerHTML = \"Please try again later...\";\r\n       }\r\n\r\n       function redirectForChat() {\r\n         window.parent.location.href = 'chatmain.jsp?workgroup=");
      out.print( workgroup );
      out.write("&chatID=");
      out.print( chatID);
      out.write("';\r\n       }\r\n      </script>\r\n   </head>\r\n\r\n   <body>\r\n   </body>\r\n  </html>\r\n\r\n  ");
 if(queue.isConnectionDropped()){
      out.write("\r\n   <script>\r\n    showNoAnswer();\r\n   </script>\r\n   ");
 } 
      out.write("\r\n\r\n   ");
 if(queue.isRouted()){ 
      out.write("\r\n   <script>\r\n    redirectForChat();\r\n   </script>\r\n    ");
 } 
      out.write("\r\n\r\n  ");
 if(queue.getQueueTime() > 0 && queue.getQueuePosition() > 0){ 
      out.write("\r\n   <script>\r\n   updateQueuePosition(");
      out.print( queue.getQueuePosition() );
      out.write(");\r\n   updateQueueTime('");
      out.print( WebUtils.getTimeFromLong(queue.getQueueTime()) );
      out.write("');\r\n   </script>\r\n  ");
 } 
      out.write("\r\n\r\n  ");
 if(!queue.isConnectionDropped()) { 
      out.write("\r\n  <!-- Will reload every 3 seconds -->\r\n  <script>\r\n   function CRCheckMsgs() {\r\n     setTimeout(\"getMsgs()\", 3000);\r\n   }\r\n\r\n   function getMsgs() {\r\n     var yakWin = window;\r\n     yakWin.location.reload(true);\r\n   }\r\n\r\n   CRCheckMsgs();\r\n  </script>\r\n  ");
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
