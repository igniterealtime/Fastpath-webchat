package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.WebUtils;

public final class chatroom_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n<script type='text/javascript' src='");
      out.print( request.getContextPath());
      out.write("/dwr/interface/chat.js'></script>\r\n<script type='text/javascript' src='");
      out.print( request.getContextPath());
      out.write("/dwr/engine.js'></script>\r\n");

    String jid = request.getParameter("jid");
    String userNickname = request.getParameter("Name");
    String email = request.getParameter("Email");
    String question = request.getParameter("Question");
    if (!ModelUtil.hasLength(question)) {
        question = "No question was asked.";
    }
    question = WebUtils.replace(question, "'", "\\\'");
    question = WebUtils.replace(question, "\n", "<br>");


      out.write("\r\n    <script type=\"text/javascript\">\r\n\r\n         DWREngine.setErrorHandler(handleError);\r\n\r\n        function handleError(error){\r\n        // Will ignore due to dwr bug.\r\n        alert(\"Unable to chat now. Please try back later.\");\r\n        }\r\n\r\n        function startChat() {\r\n            chat.startChat(null, '");
      out.print( jid );
      out.write("', '");
      out.print( userNickname );
      out.write("', '");
      out.print( email);
      out.write("', '");
      out.print( question );
      out.write("');\r\n            addChatText(window.frames['yak'], '', 'I\\'ll be with you in a moment. Please wait.');\r\n        }\r\n        function checkForNewMessages() {\r\n            var chatMessage = chat.getNextMessage(handleMessage);\r\n        }\r\n        function endChat() {\r\n            chat.endChat(closeWindow);\r\n        }\r\n        function closeWindow() {\r\n            window.location.href = \"exit-window.jsp\";\r\n        }\r\n        function checkAgentTyping(isTyping) {\r\n           var element = window.document.getElementById('typingAgent');\r\n           if(element == null){\r\n             return;\r\n           }\r\n            if (isTyping) {\r\n                window.document.getElementById('typingAgent').innerHTML = '<img src=\"images/typing_button.gif\" />'\r\n            }\r\n            else{\r\n                window.document.getElementById('typingAgent').innerHTML = ''\r\n            }\r\n        }\r\n        function handleMessage(message) {\r\n            if (message != null) {\r\n                var from = message.from;\r\n                var body = message.body;\r\n");
      out.write("                // put text in yak frame\r\n                addChatText(window.frames['yak'], from, body);\r\n                scrollYakToEnd(window.frames['yak']);\r\n\r\n                // blink the window -- IE only\r\n                if (document.all) {\r\n                  focus();\r\n                }\r\n                else {\r\n                  window.focus();\r\n                }\r\n            }\r\n            else{\r\n                chat.isAgentTyping(checkAgentTyping);\r\n            }\r\n            setTimeout(\"checkForNewMessages()\", 3000);\r\n        }\r\n        checkForNewMessages();\r\n    </script>\r\n    <html>\r\n    <head>\r\n        <title>Live Assistant Direct Chat</title>\r\n        <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\"/>\r\n        <script language=\"JavaScript\" type=\"text/javascript\">\r\n            var nickname = '");
      out.print( userNickname );
      out.write("';\r\n        </script>\r\n        <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n        <script language=\"JavaScript\" type=\"text/javascript\">\r\n        var counter = 0;\r\n      var cobrowseWin;\r\n\r\n      function submitMessage() {\r\n        var chatbox = document.f.chatbox;\r\n        if (chatbox.value.trim() != \"\") {\r\n            var val = chatbox.value;\r\n\r\n            chat.sendMessage(null, val);\r\n\r\n            // apply filters\r\n            val = applyFilters(val);\r\n\r\n\r\n            // put text in yak frame\r\n            addChatText(window.frames['yak'],'");
      out.print( userNickname);
      out.write("', val);\r\n\r\n            scrollYakToEnd(window.frames['yak']);\r\n\r\n            // blink the window -- IE only\r\n            if (document.all) {\r\n                focus();\r\n            }\r\n        }\r\n        // reset the chatbox textarea\r\n        chatbox.focus();\r\n        chatbox.value = \"\";\r\n    }\r\n\r\n    function isTypingNotification() {\r\n\t   chat.customerIsTyping(null);\r\n    }\r\n\r\n\r\n\r\n    function chatHasEnded(){\r\n      document.f.chatbox.disabled = true;\r\n    }\r\n\r\n    function handleKeyEvent(evt) {\r\n        var el = document.f.chatbox;\r\n        evt = (evt) ? evt : ((window.event) ? window.event : \"\");\r\n        var enterPressed = ((document.all) ? evt.keyCode==13 : evt.which==13);\r\n        if (enterPressed) {\r\n            submitMessage();\r\n        }\r\n        else {\r\n          if( counter == 5 ) {\r\n            isTypingNotification();\r\n            counter = 0;\r\n          }\r\n          else {\r\n            counter++;\r\n          }\r\n        }\r\n    }\r\n    if (!document.all && document.getElementById) {\r\n        document.addEventListener(\"keyup\", handleKeyEvent, true);\r\n");
      out.write("    }\r\n    else if (document.all) {\r\n        document.attachEvent(\"onkeyup\", handleKeyEvent);\r\n    }\r\n\r\n\r\n    // Function to handle text filter application\r\n    function applyFilters(body) {\r\n        // Replace ampersands\r\n        body = body.replace(/&/gi, \"&amp;\");\r\n        // Replace HTML\r\n        body = body.replace(/</gi, \"&lt;\");\r\n        body = body.replace(/>/gi, \"&gt;\");\r\n        // Replace newlines\r\n        body = body.replace(/\\n/gi, \"<br>\");\r\n        // text style\r\n        body = body.replace(/\\[b\\]/gi, \"<b>\");\r\n        body = body.replace(/\\[\\/b\\]/gi, \"</b>\");\r\n        body = body.replace(/\\[i\\]/gi, \"<i>\");\r\n        body = body.replace(/\\[\\/i\\]/gi, \"</i>\");\r\n        body = body.replace(/\\[u\\]/gi, \"<u>\");\r\n        body = body.replace(/\\[\\/u\\]/gi, \"</u>\");\r\n        // Emoticons\r\n        /* full list -> :) :-) :( :-( :D :x ;\\ B-) ]:) :p X-( :^O ;) ;-) :8} :_| ?:| :O :| */\r\n        body = body.replace(/\\]:\\)/gi, \"<img src=\\\"images/emoticons/devil.gif\\\" border='0'>\");\r\n        body = body.replace(/:\\)/gi, \"<img src=\\\"images/emoticons/happy.gif\\\" border='0'>\");\r\n");
      out.write("        body = body.replace(/:-\\)/gi, \"<img src='images/emoticons/happy.gif' border='0'>\");\r\n        body = body.replace(/:\\(/gi, \"<img src='images/emoticons/sad.gif' border='0'>\");\r\n        body = body.replace(/:-\\(/gi, \"<img src='images/emoticons/sad.gif' border='0'>\");\r\n        body = body.replace(/:D/gi, \"<img src='images/emoticons/grin.gif' border='0'>\");\r\n        body = body.replace(/:x/gi, \"<img src='images/emoticons/love.gif' border='0'>\");\r\n        body = body.replace(/;\\\\/gi, \"<img src='images/emoticons/mischief.gif' border='0'>\");\r\n        body = body.replace(/B-\\)/gi, \"<img src='images/emoticons/cool.gif' border='0'>\");\r\n        body = body.replace(/:p/gi, \"<img src='images/emoticons/silly.gif' border='0'>\");\r\n        body = body.replace(/X-\\(/gi, \"<img src='images/emoticons/angry.gif' border='0'>\");\r\n        body = body.replace(/:\\^O/gi, \"<img src='images/emoticons/laugh.gif' border='0'>\");\r\n        body = body.replace(/:\\^0/gi, \"<img src='images/emoticons/laugh.gif' border='0'>\");\r\n        body = body.replace(/;\\)/gi, \"<img src='images/emoticons/wink.gif' border='0'>\");\r\n");
      out.write("        body = body.replace(/;-\\)/gi, \"<img src='images/emoticons/wink.gif' border='0'>\");\r\n        body = body.replace(/:8\\}/gi, \"<img src='images/emoticons/blush.gif' border='0'>\");\r\n        body = body.replace(/:_\\|/gi, \"<img src='images/emoticons/cry.gif' border='0'>\");\r\n        body = body.replace(/\\?:\\|/gi, \"<img src='images/emoticons/confused.gif' border='0'>\");\r\n        body = body.replace(/:O/gi, \"<img src='images/emoticons/shocked.gif' border='0'>\");\r\n        body = body.replace(/:0/gi, \"<img src='images/emoticons/shocked.gif' border='0'>\");\r\n        body = body.replace(/:\\|/gi, \"<img src='images/emoticons/plain.gif' border='0'>\");\r\n        // done!\r\n        return body;\r\n    }\r\n        </script>\r\n    </head>\r\n    <body onload=\"startChat();\">\r\n    <div align=\"center\">\r\n      <table height=\"100%\" width=\"100%\" border=\"1\"><tr align=\"center\" ><td valign=\"middle\">\r\n        <table height=\"100%\" width=\"100%\" class=\"box\" border=\"0\">\r\n            <tr>\r\n                <td>\r\n                    <img src=\"images/title.gif\">\r\n");
      out.write("                </td>\r\n                <td align=\"right\">\r\n                   <input type=\"button\" value=\"End Session\" onclick=\"endChat();\" />\r\n                </td>\r\n            </tr>\r\n            <tr>\r\n                <td style=\"width:100%;height: 100%;\">\r\n                    <iframe class=\"box\"     name=\"yak\"       src=\"yakframe.html\" frameborder=\"0\" marginheight=\"0\"\r\n                            marginwidth=\"0\" scrolling=\"auto\" height=\"100%\"       width=\"100%\" >\r\n                    </iframe>\r\n                </td>\r\n            </tr>\r\n             <form name=\"f\">\r\n            <tr>\r\n                <td>\r\n                        <textarea class=\"box\" name=\"chatbox\" wrap=\"virtual\" style=\"width:100%;height:50px;\"></textarea>\r\n                </td>\r\n                <td valign=\"middle\">\r\n                   <input type=\"button\" value=\"Send\" onclick=\"javascript:submitMessage();\" />\r\n                </td>\r\n            </tr>\r\n              </form>\r\n            <tr valign=\"top\" style=\"height:40px;\">\r\n                <td>\r\n");
      out.write("                    <div id=\"typingAgent\"></div>\r\n                </td>\r\n                <td>\r\n");

                    if (request.isSecure()) {

      out.write("\r\n                                        <div id=\"isSecure\">\r\n                                            <img src=\"images/secure_button.gif\" border=\"0\"/>\r\n                                        </div>\r\n                </td>\r\n");

                    }

      out.write("\r\n            </tr>\r\n            <tr>\r\n                <td align=\"right\" colspan=\"2\">\r\n                    <img src=\"images/poweredBy.gif\"/>\r\n                </td>\r\n            </tr>\r\n            </table>\r\n            </td></tr></table>\r\n            </div>\r\n    </body>\r\n    </html>\r\n");
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
