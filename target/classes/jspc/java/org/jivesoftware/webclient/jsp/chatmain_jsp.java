package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.*;
import java.util.Map;
import org.jivesoftware.webchat.util.FormText;
import com.jivesoftware.smack.workgroup.settings.SoundSettings;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.webchat.util.WebUtils;

public final class chatmain_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("/dwr/interface/room.js'></script>\r\n<script type='text/javascript' src='");
      out.print( request.getContextPath());
      out.write("/dwr/engine.js'></script>\r\n\r\n");

   final String chatID = request.getParameter("chatID");
   final String workgroup = request.getParameter("workgroup");

   ChatManager chatManager = ChatManager.getInstance();
   ChatSession chatSession = chatManager.getChatSession(chatID);

   final Map map = chatSession.getMetaData();
   String question = "";
   if (map.containsKey( ("question") )) {
     question = "Question: " + (String) map.get( "question" );
   }

   String userNickname = chatSession.getNickname();
   userNickname = WebUtils.replace(userNickname, "'", "&apos;");
   userNickname = WebUtils.replace(userNickname, "\"", "&quot;");


      out.write("\r\n  <html>\r\n   <head>\r\n    <title>Chat</title>\r\n\r\n    <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\"/>\r\n\r\n    <script language=\"JavaScript\" type=\"text/javascript\">\r\n     var nickname = '");
      out.print( userNickname );
      out.write("';\r\n     var isRedirecting = false;\r\n\r\n     var checker = 0;\r\n\r\n     var lastChecked = 0;\r\n\r\n     DWREngine.setErrorHandler(handleError);\r\n\r\n     function handleError(error) {\r\n         // check for connectivity.\r\n         if (isRedirecting) {\r\n             return;\r\n         }\r\n     }\r\n\r\n     function connectionChecker(){\r\n         var t = new Date().getTime();\r\n         if(t > (lastChecked + 60000) && lastChecked != 0){\r\n            chatHasEnded();\r\n            alert(\"The connection to the conversation has been lost. Please close the window and try again.\");\r\n            window.close();\r\n         }\r\n\r\n         setTimeout(\"connectionChecker()\", 5000);\r\n     }\r\n\r\n     connectionChecker();\r\n\r\n     function successful(b) {\r\n         addText('', \"Reconnection successful.\");\r\n\r\n         checkForNewMessages();\r\n         checkIfAgentTyping();\r\n         DWREngine.setErrorHandler(handleError);\r\n         checker = 0;\r\n     }\r\n    </script>\r\n\r\n    <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\"></script>\r\n");
      out.write("\r\n    <script language=\"JavaScript\" type=\"text/javascript\">\r\n      var counter = 0;\r\n      var cobrowseWin;\r\n\r\n      function submitMessage() {\r\n        var chatbox = document.f.chatbox;\r\n        if (chatbox.value.trim() != \"\") {\r\n            var val = chatbox.value;\r\n\r\n            // submit the form to the servlet\r\n            room.sendMessage(null, '");
      out.print( chatID );
      out.write("', val);\r\n\r\n            // apply filters\r\n            val = applyFilters(val);\r\n\r\n            // put text in yak frame\r\n            addChatText(window.frames['yak'],'");
      out.print( userNickname);
      out.write("', val);\r\n\r\n            scrollYakToEnd(window.frames['yak']);\r\n\r\n            // blink the window -- IE only\r\n            if (document.all) {\r\n                focus();\r\n            }\r\n\r\n            var sounds = document.getElementById(\"soundsEnabled\");\r\n            if (sounds != null && sounds.checked) {\r\n                // play outgoing sound, if enabled\r\n                document.getElementById(\"sounds\").innerHTML=\r\n                    \"<embed src='");
      out.print( request.getContextPath());
      out.write("/sounds?workgroup=");
      out.print(workgroup);
      out.write("&action=outgoing' style=display:none; hidden=true autostart=true loop=false>\";\r\n            }\r\n        }\r\n        // reset the chatbox textarea\r\n        chatbox.focus();\r\n        chatbox.value = \"\";\r\n    }\r\n\r\n    function showCobrowser(url){\r\n      var width = 800;\r\n      var height = 700;\r\n      var defaultOptions = \"location=yes,status=no,toolbar=no,personalbar=no,menubar=no,directories=no,\";\r\n      var winleft = (screen.width - width) / 2;\r\n      var winUp = (screen.height - height) / 2;\r\n\r\n      defaultOptions += \"scrollbars=yes,resizable=yes,top=\" + winUp + \",left=\" + winleft + \",\";\r\n      defaultOptions += \"width=\" + width + \",height=\" + height;\r\n      cobrowseWin = window.open(url, 'cobrowser', defaultOptions);\r\n\r\n      room.sendMessage(null, '");
      out.print( chatID );
      out.write("', 'I have accepted the Cobrowse invitation for '+url);\r\n    }\r\n\r\n    function checkUnload(){\r\n      if(!isRedirecting){\r\n         window.location.href = 'exit-queue.jsp?workgroup=");
      out.print(workgroup);
      out.write("&chatID=");
      out.print(chatID);
      out.write("';\r\n      }\r\n    }\r\n\r\n    function isTypingNotification() {\r\n\t   room.customerIsTyping(null, '");
      out.print( chatID);
      out.write("');\r\n    }\r\n\r\n    function chatHasEnded(){\r\n      document.f.chatbox.disabled = true;\r\n    }\r\n\r\n    function handleKeyEvent(evt) {\r\n        var el = document.f.chatbox;\r\n        evt = (evt) ? evt : ((window.event) ? window.event : \"\");\r\n        var enterPressed = ((document.all) ? evt.keyCode==13 : evt.which==13);\r\n        if (enterPressed) {\r\n            submitMessage();\r\n        }\r\n        else {\r\n          if( counter == 5 ) {\r\n            isTypingNotification();\r\n            counter = 0;\r\n          }\r\n          else {\r\n            counter++;\r\n          }\r\n        }\r\n    }\r\n    if (!document.all && document.getElementById) {\r\n        document.addEventListener(\"keyup\", handleKeyEvent, true);\r\n    }\r\n    else if (document.all) {\r\n        document.attachEvent(\"onkeyup\", handleKeyEvent);\r\n    }\r\n\r\n\r\n    // Function to handle text filter application\r\n    function applyFilters(body) {\r\n        // Replace ampersands\r\n        body = body.replace(/&/gi, \"&amp;\");\r\n        // Replace HTML\r\n        body = body.replace(/</gi, \"&lt;\");\r\n");
      out.write("        body = body.replace(/>/gi, \"&gt;\");\r\n        // Replace newlines\r\n        body = body.replace(/\\n/gi, \"<br>\");\r\n        // text style\r\n        body = body.replace(/\\[b\\]/gi, \"<b>\");\r\n        body = body.replace(/\\[\\/b\\]/gi, \"</b>\");\r\n        body = body.replace(/\\[i\\]/gi, \"<i>\");\r\n        body = body.replace(/\\[\\/i\\]/gi, \"</i>\");\r\n        body = body.replace(/\\[u\\]/gi, \"<u>\");\r\n        body = body.replace(/\\[\\/u\\]/gi, \"</u>\");\r\n        // Emoticons\r\n        /* full list -> :) :-) :( :-( :D :x ;\\ B-) ]:) :p X-( :^O ;) ;-) :8} :_| ?:| :O :| */\r\n        body = body.replace(/\\]:\\)/gi, \"<img src=\\\"images/emoticons/devil.gif\\\" border='0'>\");\r\n        body = body.replace(/:\\)/gi, \"<img src=\\\"images/emoticons/happy.gif\\\" border='0'>\");\r\n        body = body.replace(/:-\\)/gi, \"<img src='images/emoticons/happy.gif' border='0'>\");\r\n        body = body.replace(/:\\(/gi, \"<img src='images/emoticons/sad.gif' border='0'>\");\r\n        body = body.replace(/:-\\(/gi, \"<img src='images/emoticons/sad.gif' border='0'>\");\r\n        body = body.replace(/:D/gi, \"<img src='images/emoticons/grin.gif' border='0'>\");\r\n");
      out.write("        body = body.replace(/:x/gi, \"<img src='images/emoticons/love.gif' border='0'>\");\r\n        body = body.replace(/;\\\\/gi, \"<img src='images/emoticons/mischief.gif' border='0'>\");\r\n        body = body.replace(/B-\\)/gi, \"<img src='images/emoticons/cool.gif' border='0'>\");\r\n        body = body.replace(/:p/gi, \"<img src='images/emoticons/silly.gif' border='0'>\");\r\n        body = body.replace(/X-\\(/gi, \"<img src='images/emoticons/angry.gif' border='0'>\");\r\n        body = body.replace(/:\\^O/gi, \"<img src='images/emoticons/laugh.gif' border='0'>\");\r\n        body = body.replace(/:\\^0/gi, \"<img src='images/emoticons/laugh.gif' border='0'>\");\r\n        body = body.replace(/;\\)/gi, \"<img src='images/emoticons/wink.gif' border='0'>\");\r\n        body = body.replace(/;-\\)/gi, \"<img src='images/emoticons/wink.gif' border='0'>\");\r\n        body = body.replace(/:8\\}/gi, \"<img src='images/emoticons/blush.gif' border='0'>\");\r\n        body = body.replace(/:_\\|/gi, \"<img src='images/emoticons/cry.gif' border='0'>\");\r\n        body = body.replace(/\\?:\\|/gi, \"<img src='images/emoticons/confused.gif' border='0'>\");\r\n");
      out.write("        body = body.replace(/:O/gi, \"<img src='images/emoticons/shocked.gif' border='0'>\");\r\n        body = body.replace(/:0/gi, \"<img src='images/emoticons/shocked.gif' border='0'>\");\r\n        body = body.replace(/:\\|/gi, \"<img src='images/emoticons/plain.gif' border='0'>\");\r\n        // done!\r\n        return body;\r\n    }\r\n    </script>\r\n\r\n    <script language=\"JavaScript\" type=\"text/javascript\">\r\n     function showTranscriptWindow(message) {\r\n        isRedirecting = true;\r\n        location.href = 'transcriptmain.jsp?workgroup=");
      out.print( workgroup );
      out.write("&chatID=");
      out.print( chatID);
      out.write("';\r\n     }\r\n\r\n     function confirmExit(){\r\n         var ok = confirm('Are you sure you want to end your session?');\r\n         if(ok){\r\n             showTranscriptWindow(null);\r\n         }\r\n     }\r\n\r\n     function doit() {\r\n    ");


      String initialAgent = chatSession.getInitialAgent();
      if(initialAgent == null){
         
      out.write("\r\n           alert(\"We are unable to connect you to an agent. Please try back later.\")\r\n           window.location.href = \"userinfo.jsp?workgroup=");
      out.print( workgroup );
      out.write("&chatID=");
      out.print( chatID );
      out.write("\";\r\n         ");

      }
    
      out.write("\r\n       addChatText(window.frames['yak'], '', '");
      out.print( FormText.getChatRoomWelcomeMessage(initialAgent, workgroup) );
      out.write("');\r\n       checkForNewMessages();\r\n       checkIfAgentTyping();\r\n     }\r\n\r\n     function addText(from, body) {\r\n       addChatText(window.frames['yak'], from, body);\r\n     }\r\n    </script>\r\n   </head>\r\n\r\n   <body style=\"margin-top:0px;margin-right:20px;margin-left:20px;margin-bottom:10px;\" id=\"defaultwindow\" onload=\"doit();\" onunload=\"checkUnload();\">\r\n\r\n    <span id=sounds></span>\r\n\r\n    ");
      out.write("\r\n    <table height=\"100%\" width=\"100%\" cellpadding=\"3\" cellspacing=\"0\">\r\n    <tr valign=\"top\">\r\n    <td> <img src=\"getimage?image=logo&workgroup=");
      out.print( workgroup );
      out.write("\"/></td>\r\n     <td align=\"right\" colspan=\"3\">\r\n     <a href=\"#\" onclick=\"confirmExit();return false;\"\r\n        title=\"Click to end session.\">\r\n     <img src=\"getimage?image=end&workgroup=");
      out.print( workgroup );
      out.write("\" border=\"0\">\r\n     </a>\r\n     </td>\r\n    </tr>\r\n    <tr>\r\n     <td style=\"width:100%;height: 100%;\" colspan=\"4\">\r\n     <iframe class=\"box\"      name=\"yak\"    src=\"yakframe.html\" frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\"\r\n             scrolling=\"auto\" height=\"100%\" width=\"100%\">\r\n     </iframe>\r\n     </td>\r\n    </tr>\r\n    ");

        SoundSettings soundSettings = null;
        try {
            soundSettings = chatSession.getWorkgroup().getSoundSettings();
        }
        catch (XMPPException e) {
        }
        if(soundSettings != null && soundSettings.getIncomingSoundBytes() != null){
    
      out.write("\r\n    <tr>\r\n    <td width=\"100%\" colspan=\"4\">\r\n     <input type=\"checkbox\" name=\"soundsEnabled\" id=\"soundsEnabled\" CHECKED/> Play Sounds\r\n    </td>\r\n    </tr>\r\n    ");
 } 
      out.write("\r\n    <tr><form name=\"f\">\r\n    <td width=\"100%\" colspan=\"3\">\r\n      <textarea class=\"box\" cols=\"40\" rows=\"4\" name=\"chatbox\" wrap=\"virtual\"  style=\"width:100%;height:50px;overflow:hidden;\"></textarea>\r\n    </td></form>\r\n    <td align=\"right\" width=\"1%\" nowrap valign=\"top\">\r\n     <img src=\"getimage?image=sendmessage&workgroup=");
      out.print( workgroup );
      out.write("\"\r\n         alt=\"Send Message\"\r\n         title=\"Send Message\"\r\n         onclick=\"javascript:submitMessage();\"/>\r\n    </td>\r\n\r\n    </tr>\r\n     <tr valign=\"bottom\">\r\n\r\n                <td colspan=\"4\">\r\n         <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n             <tr valign=\"bottom\"><td height=\"30\"> <div id=\"typingAgent\"></div></td>\r\n             <td></td>\r\n             ");
 if (request.isSecure()) { 
      out.write("\r\n                                        <div id=\"isSecure\">\r\n                                            <img src=\"images/secure_button.gif\" border=\"0\"/>\r\n                                        </div>\r\n             ");
}
      out.write("\r\n                </td>\r\n         </table>\r\n\r\n <div style=\"position:absolute;bottom:0px;right:5px\"><img src=\"getimage?image=poweredby&workgroup=");
      out.print( workgroup );
      out.write("\"/></div>\r\n\r\n\r\n    <script>\r\n    var timeOut;\r\n\r\n    function checkForNewMessages() {\r\n       room.getAllMessages(insertMessages, '");
      out.print( chatID );
      out.write("');\r\n       lastChecked = new Date().getTime();\r\n    }\r\n\r\n    function checkIfAgentTyping() {\r\n        room.isTyping(isTyping, '");
      out.print( chatID );
      out.write("');\r\n   }\r\n\r\n   function isTyping(agentIsTyping){\r\n     if(agentIsTyping){\r\n        document.getElementById('typingAgent').innerHTML = '<img src=\"getimage?image=agenttyping&workgroup=");
      out.print( workgroup );
      out.write("\" />'\r\n     }\r\n     else {\r\n        document.getElementById('typingAgent').innerHTML = ''\r\n     }\r\n     setTimeout(\"checkIfAgentTyping()\", 2000);\r\n   }\r\n\r\n   function insertMessages(messages){\r\n      if(messages == null){\r\n        clearTimeout(timeOut);\r\n\r\n        chatHasEnded();\r\n\r\n        room.getEndMessage(showEndMessage, '");
      out.print( chatID);
      out.write("', '");
      out.print(workgroup);
      out.write("');\r\n        return;\r\n      }\r\n\r\n      for(i=0; i<messages.length;i++){\r\n         if(messages[i].cobrowsing){\r\n           if(cobrowseWin && !cobrowseWin.closed){\r\n             cobrowseWin.location.href= messages[i].urlToPush;\r\n             cobrowseWin.focus();\r\n           }\r\n           else if(messages[i].body != null) {\r\n              var urlToPush = messages[i].urlToPush;\r\n              addText('', \"<a href=javascript:parent.showCobrowser('\"+urlToPush+\"')>You have received a Cobrowsing invitation. Click to initiate a Cobrowser session.</a>\");\r\n           }\r\n         }\r\n         else {\r\n           addChatText(window.frames['yak'], messages[i].from, messages[i].body);\r\n         }\r\n      }\r\n\r\n      if(messages.length > 0 && !messages[0].cobrowsing){\r\n        scrollYakToEnd(window.frames['yak']);\r\n        document.getElementById('typingAgent').innerHTML = ''\r\n        room.clearAgentTyping(null, '");
      out.print( chatID );
      out.write("');\r\n\r\n        var hasSounds = document.getElementById(\"soundsEnabled\");\r\n\r\n        if (hasSounds != null && document.getElementById(\"soundsEnabled\").checked) {\r\n          // play incoming sound, if enabled\r\n          document.getElementById(\"sounds\").innerHTML=\r\n              \"<embed src='");
      out.print( request.getContextPath());
      out.write("/sounds?workgroup=");
      out.print(workgroup);
      out.write("&action=incoming' style=display:none; hidden=true autostart=true loop=false>\";\r\n        }\r\n\r\n        // blink the window -- IE only\r\n        if (document.all) {\r\n            focus();\r\n            document.f.chatbox.focus();\r\n        }\r\n        else {\r\n           window.parent.focus();\r\n        }\r\n      }\r\n\r\n      timeOut = setTimeout(\"checkForNewMessages()\", 2000);\r\n   }\r\n\r\n      function showEndMessage(message){\r\n\r\n        var ok = confirm(message);\r\n        if(ok){\r\n          isRedirecting = true;\r\n          showTranscriptWindow(null);\r\n        }\r\n        else {\r\n          window.location.href = \"exit-window.jsp\";\r\n        }\r\n      }\r\n    </script>\r\n\r\n   </body>\r\n  </html>\r\n\r\n\r\n");
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
