package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.FormText;
import org.jivesoftware.webchat.util.ParamUtils;

public final class view_002dqueue_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\n\n\n\n<script type='text/javascript' src='");
      out.print( request.getContextPath());
      out.write("/dwr/interface/room.js'></script>\n<script type='text/javascript' src='");
      out.print( request.getContextPath());
      out.write("/dwr/engine.js'></script>\n");

    final String workgroup = ParamUtils.getParameter(request, "workgroup");
    final String chatID = ParamUtils.getParameter(request, "chatID");
    String descriptiveText = FormText.getQueueDescriptionText(workgroup);
    String titleText = FormText.getQueueTitleText(workgroup);

      out.write("\n    <html>\n    <head>\n        <title>Queue Information</title>\n        <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp?workgroup=");
      out.print( workgroup );
      out.write("\"/>\n        <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\n        <script type=\"text/javascript\" language=\"Javascript\">\n         DWREngine.setErrorHandler(handleError);\n\n\n         function handleError(error) {\n             // Will ignore due to dwr bug.\n         }\n\n         function showLeaveAMessage() {\n                isClosing = false;\n                window.location.href = 'email/leave-a-message.jsp?workgroup=");
      out.print( workgroup );
      out.write("&chatID=");
      out.print( chatID);
      out.write("';\n         }\n        </script>\n    </head>\n    <body style=\"margin-top:0px; margin-bottom:20px; margin-right:20px; margin-left:20px\" id=\"defaultwindow\" onunload=\"isWindowClosing();\">\n        <table width=\"100%\" border=\"0\">\n            <tr>\n                <td height=\"1%\">\n                    <img src=\"getimage?image=logo&workgroup=");
      out.print( workgroup );
      out.write("\"/>\n                </td>\n            </tr>\n            <tr>\n                <td height=\"1%\">\n                  <br/>  <span  id=\"queue_info_header\">");
      out.print( titleText );
      out.write(" </span>\n                </td>\n            </tr>\n            <tr>\n                <td height=\"1%\">\n                    <span id=\"queue_info\" style=\"visibility:hidden;\">\n                    ");
      out.print( descriptiveText );
      out.write(" </span>\n                </td>\n            </tr>\n            <tr>\n                <td height=\"1%\" >\n                    <br/>\n                    <br/>\n                    <span id=\"queue_info_footer\"> ");
      out.print( FormText.getQueueFooter(workgroup));
      out.write("\n                   </span>\n                    <br/>\n                    <br/>\n                </td>\n            </tr>\n            <form name=\"queueform\" action=\"exit-queue.jsp\" method=\"post\">\n                <input type=\"hidden\" name=\"workgroup\" value=\"");
      out.print( workgroup );
      out.write("\" />\n                <input type=\"hidden\" name=\"chatID\" value=\"");
      out.print( chatID);
      out.write("\" />\n            <tr>\n                <td height=\"1%\">\n                            <input type=\"submit\"\n                                   name=\"leave\"\n                                   onclick=\"return checkLeaving();return false\"\n                                   value=\"Close Window\"/>\n                </td>\n            </tr>\n            </form>\n              <tr>\n                        <td colspan=\"2\" align=\"right\" valign=\"bottom\" height=\"99%\">\n\n                        </td>\n                    </tr>\n        </table>\n        <div style=\"position:absolute;bottom:0px;right:5px\"><img src=\"getimage?image=poweredby&workgroup=");
      out.print( workgroup );
      out.write("\"/></div>\n        <script>\n            var isClosing = true;\n\n            function checkQueue() {\n                room.getChatQueue(handleQueue, '");
      out.print( chatID );
      out.write("');\n            }\n            function handleQueue(queue) {\n                if (queue == null) {\n                    return;\n                }\n                if (queue.connectionDropped) {\n                    showNoAnswer();\n                    return;\n                }\n                else if (queue.routed) {\n                    redirectForChat();\n                    return;\n                }\n                else if (queue.queueTime > 0 && queue.queuePosition > 0) {\n                    updateQueuePosition(queue.queuePosition);\n                    updateQueueTime(queue.queueTimeForHtml);\n                }\n                setTimeout(\"checkQueue()\", 5000);\n            }\n            // Redirect For Chat\n            function redirectForChat() {\n                isClosing = false;\n                window.location.href = 'chatmain.jsp?workgroup=");
      out.print( workgroup );
      out.write("&chatID=");
      out.print( chatID);
      out.write("';\n            }\n            // Updates the queue position text on the queue page\n            function updateQueuePosition(position) {\n                var posSpan = window.document.getElementById('queue_position');\n                if (posSpan) {\n                    posSpan.innerHTML = position;\n                    window.document.getElementById('queue_info').style.visibility = 'visible';\n                }\n            }\n            // Updates the queue wait time. Time should be in seconds.\n            function updateQueueTime(time) {\n                var timeSpan = window.document.getElementById('queue_time');\n                if (timeSpan) {\n                    timeSpan.innerHTML = time;\n                    window.document.getElementById('queue_info').style.visibility = 'visible';\n                }\n            }\n\n            function isWindowClosing() {\n                if (isClosing) {\n                    window.location.href = 'exit-queue.jsp?workgroup=");
      out.print( workgroup);
      out.write("&chatID=");
      out.print( chatID );
      out.write("';\n                }\n            }\n\n            function checkLeaving(){\n                var ok = confirm('Are you sure you want to leave the queue?');\n                if(ok){\n                    isClosing = false;\n                }\n                return ok;\n            }\n\n\n\n            function showNoAnswer() {\n                var queueInfoHeader = window.document.getElementById('queue_info_header');\n                if (queueInfoHeader != null) {\n                    queueInfoHeader.innerHTML  = '");
      out.print( FormText.getNoAgentText(workgroup));
      out.write("';\n                    var queueInfoText = window.document.getElementById('queue_info');\n                    queueInfoText.innerHTML = \"\";\n                    var queueInfoFooter = window.document.getElementById('queue_info_footer');\n                    queueInfoFooter.innerHTML = \"\";\n                }\n            }\n            checkQueue();\n        </script>\n\n    </body>\n    </html>\n");
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
