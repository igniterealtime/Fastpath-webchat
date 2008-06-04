<%--
  - $RCSfile$
  - $Revision: $
  - $Date: $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%@ page errorPage="fatal.jsp"
        import="org.jivesoftware.webchat.util.ModelUtil"%><%@ page import="org.jivesoftware.webchat.util.WebUtils"%>
<script type='text/javascript' src='<%= request.getContextPath()%>/dwr/interface/chat.js'></script>
<script type='text/javascript' src='<%= request.getContextPath()%>/dwr/engine.js'></script>
<%
    String jid = request.getParameter("jid");
    String userNickname = request.getParameter("Name");
    String email = request.getParameter("Email");
    String question = request.getParameter("Question");
    if (!ModelUtil.hasLength(question)) {
        question = "No question was asked.";
    }
    question = WebUtils.replace(question, "'", "\\\'");
    question = WebUtils.replace(question, "\n", "<br>");

%>
    <script type="text/javascript">

         DWREngine.setErrorHandler(handleError);

        function handleError(error){
        // Will ignore due to dwr bug.
        alert("Unable to chat now. Please try back later.");
        }

        function startChat() {
            chat.startChat(null, '<%= jid %>', '<%= userNickname %>', '<%= email%>', '<%= question %>');
            addChatText(window.frames['yak'], '', 'I\'ll be with you in a moment. Please wait.');
        }
        function checkForNewMessages() {
            var chatMessage = chat.getNextMessage(handleMessage);
        }
        function endChat() {
            chat.endChat(closeWindow);
        }
        function closeWindow() {
            window.location.href = "exit-window.jsp";
        }
        function checkAgentTyping(isTyping) {
           var element = window.document.getElementById('typingAgent');
           if(element == null){
             return;
           }
            if (isTyping) {
                window.document.getElementById('typingAgent').innerHTML = '<img src="images/typing_button.gif" />'
            }
            else{
                window.document.getElementById('typingAgent').innerHTML = ''
            }
        }
        function handleMessage(message) {
            if (message != null) {
                var from = message.from;
                var body = message.body;
                // put text in yak frame
                addChatText(window.frames['yak'], from, body);
                scrollYakToEnd(window.frames['yak']);

                // blink the window -- IE only
                if (document.all) {
                  focus();
                }
                else {
                  window.focus();
                }
            }
            else{
                chat.isAgentTyping(checkAgentTyping);
            }
            setTimeout("checkForNewMessages()", 3000);
        }
        checkForNewMessages();
    </script>
    <html>
    <head>
        <title>Live Assistant Direct Chat</title>
        <link rel="stylesheet" type="text/css" href="style.jsp"/>
        <script language="JavaScript" type="text/javascript">
            var nickname = '<%= userNickname %>';
        </script>
        <script language="JavaScript" type="text/javascript" src="common.js"></script>
        <script language="JavaScript" type="text/javascript">
        var counter = 0;
      var cobrowseWin;

      function submitMessage() {
        var chatbox = document.f.chatbox;
        if (chatbox.value.trim() != "") {
            var val = chatbox.value;

            chat.sendMessage(null, val);

            // apply filters
            val = applyFilters(val);


            // put text in yak frame
            addChatText(window.frames['yak'],'<%= userNickname%>', val);

            scrollYakToEnd(window.frames['yak']);

            // blink the window -- IE only
            if (document.all) {
                focus();
            }
        }
        // reset the chatbox textarea
        chatbox.focus();
        chatbox.value = "";
    }

    function isTypingNotification() {
	   chat.customerIsTyping(null);
    }



    function chatHasEnded(){
      document.f.chatbox.disabled = true;
    }

    function handleKeyEvent(evt) {
        var el = document.f.chatbox;
        evt = (evt) ? evt : ((window.event) ? window.event : "");
        var enterPressed = ((document.all) ? evt.keyCode==13 : evt.which==13);
        if (enterPressed) {
            submitMessage();
        }
        else {
          if( counter == 5 ) {
            isTypingNotification();
            counter = 0;
          }
          else {
            counter++;
          }
        }
    }
    if (!document.all && document.getElementById) {
        document.addEventListener("keyup", handleKeyEvent, true);
    }
    else if (document.all) {
        document.attachEvent("onkeyup", handleKeyEvent);
    }


    // Function to handle text filter application
    function applyFilters(body) {
        // Replace ampersands
        body = body.replace(/&/gi, "&amp;");
        // Replace HTML
        body = body.replace(/</gi, "&lt;");
        body = body.replace(/>/gi, "&gt;");
        // Replace newlines
        body = body.replace(/\n/gi, "<br>");
        // text style
        body = body.replace(/\[b\]/gi, "<b>");
        body = body.replace(/\[\/b\]/gi, "</b>");
        body = body.replace(/\[i\]/gi, "<i>");
        body = body.replace(/\[\/i\]/gi, "</i>");
        body = body.replace(/\[u\]/gi, "<u>");
        body = body.replace(/\[\/u\]/gi, "</u>");
        // Emoticons
        /* full list -> :) :-) :( :-( :D :x ;\ B-) ]:) :p X-( :^O ;) ;-) :8} :_| ?:| :O :| */
        body = body.replace(/\]:\)/gi, "<img src=\"images/emoticons/devil.gif\" border='0'>");
        body = body.replace(/:\)/gi, "<img src=\"images/emoticons/happy.gif\" border='0'>");
        body = body.replace(/:-\)/gi, "<img src='images/emoticons/happy.gif' border='0'>");
        body = body.replace(/:\(/gi, "<img src='images/emoticons/sad.gif' border='0'>");
        body = body.replace(/:-\(/gi, "<img src='images/emoticons/sad.gif' border='0'>");
        body = body.replace(/:D/gi, "<img src='images/emoticons/grin.gif' border='0'>");
        body = body.replace(/:x/gi, "<img src='images/emoticons/love.gif' border='0'>");
        body = body.replace(/;\\/gi, "<img src='images/emoticons/mischief.gif' border='0'>");
        body = body.replace(/B-\)/gi, "<img src='images/emoticons/cool.gif' border='0'>");
        body = body.replace(/:p/gi, "<img src='images/emoticons/silly.gif' border='0'>");
        body = body.replace(/X-\(/gi, "<img src='images/emoticons/angry.gif' border='0'>");
        body = body.replace(/:\^O/gi, "<img src='images/emoticons/laugh.gif' border='0'>");
        body = body.replace(/:\^0/gi, "<img src='images/emoticons/laugh.gif' border='0'>");
        body = body.replace(/;\)/gi, "<img src='images/emoticons/wink.gif' border='0'>");
        body = body.replace(/;-\)/gi, "<img src='images/emoticons/wink.gif' border='0'>");
        body = body.replace(/:8\}/gi, "<img src='images/emoticons/blush.gif' border='0'>");
        body = body.replace(/:_\|/gi, "<img src='images/emoticons/cry.gif' border='0'>");
        body = body.replace(/\?:\|/gi, "<img src='images/emoticons/confused.gif' border='0'>");
        body = body.replace(/:O/gi, "<img src='images/emoticons/shocked.gif' border='0'>");
        body = body.replace(/:0/gi, "<img src='images/emoticons/shocked.gif' border='0'>");
        body = body.replace(/:\|/gi, "<img src='images/emoticons/plain.gif' border='0'>");
        // done!
        return body;
    }
        </script>
    </head>
    <body onload="startChat();">
    <div align="center">
      <table height="100%" width="100%" border="1"><tr align="center" ><td valign="middle">
        <table height="100%" width="100%" class="box" border="0">
            <tr>
                <td>
                    <img src="images/title.gif">
                </td>
                <td align="right">
                   <input type="button" value="End Session" onclick="endChat();" />
                </td>
            </tr>
            <tr>
                <td style="width:100%;height: 100%;">
                    <iframe class="box"     name="yak"       src="yakframe.html" frameborder="0" marginheight="0"
                            marginwidth="0" scrolling="auto" height="100%"       width="100%" >
                    </iframe>
                </td>
            </tr>
             <form name="f">
            <tr>
                <td>
                        <textarea class="box" name="chatbox" wrap="virtual" style="width:100%;height:50px;"></textarea>
                </td>
                <td valign="middle">
                   <input type="button" value="Send" onclick="javascript:submitMessage();" />
                </td>
            </tr>
              </form>
            <tr valign="top" style="height:40px;">
                <td>
                    <div id="typingAgent"></div>
                </td>
                <td>
<%
                    if (request.isSecure()) {
%>
                                        <div id="isSecure">
                                            <img src="images/secure_button.gif" border="0"/>
                                        </div>
                </td>
<%
                    }
%>
            </tr>
            <tr>
                <td align="right" colspan="2">
                    <img src="images/poweredBy.gif"/>
                </td>
            </tr>
            </table>
            </td></tr></table>
            </div>
    </body>
    </html>
