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

<%@ page errorPage = "fatal.jsp"
         import = "org.jivesoftware.webchat.*,
                   java.util.Map,
                   org.jivesoftware.webchat.util.FormText" %><%@ page import="com.jivesoftware.smack.workgroup.settings.SoundSettings"%><%@ page import="org.jivesoftware.smack.XMPPException"%><%@ page import="org.jivesoftware.webchat.util.WebUtils"%>
<%@ page import="org.jivesoftware.webchat.util.StringUtils" %>
<script type='text/javascript' src='<%= request.getContextPath()%>/dwr/interface/room.js'></script>
<script type='text/javascript' src='<%= request.getContextPath()%>/dwr/engine.js'></script>

<%
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

%>
  <html>
   <head>
    <title>Chat</title>

    <link rel="stylesheet" type="text/css" href="style.jsp"/>

    <script language="JavaScript" type="text/javascript">
     var nickname = '<%= StringUtils.escapeHTMLTags(userNickname) %>';
     var isRedirecting = false;

     var checker = 0;

     var lastChecked = 0;

     DWREngine.setErrorHandler(handleError);

     function handleError(error) {
         // check for connectivity.
         if (isRedirecting) {
             return;
         }
     }

     function connectionChecker(){
         var t = new Date().getTime();
         if(t > (lastChecked + 60000) && lastChecked != 0){
            chatHasEnded();
            alert("The connection to the conversatinicknamon has been lost. Please close the window and try again.");
            window.close();
         }

         setTimeout("connectionChecker()", 5000);
     }

     connectionChecker();

     function successful(b) {
         addText('', "Reconnection successful.");

         checkForNewMessages();
         checkIfAgentTyping();
         DWREngine.setErrorHandler(handleError);
         checker = 0;
     }
    </script>

    <script language="JavaScript" type="text/javascript" src="common.js"></script>

    <script language="JavaScript" type="text/javascript">
      var counter = 0;
      var cobrowseWin;

      function submitMessage() {
        var chatbox = document.f.chatbox;
        if (chatbox.value.trim() != "") {
            var val = chatbox.value;

            // submit the form to the servlet
            room.sendMessage(null, '<%= StringUtils.escapeHTMLTags(chatID) %>', val);

            // apply filters
            val = applyFilters(val);

            // put text in yak frame
            addChatText(window.frames['yak'],'<%= StringUtils.escapeHTMLTags(userNickname)%>', val);

            scrollYakToEnd(window.frames['yak']);

            // blink the window -- IE only
            if (document.all) {
                focus();
            }

            var sounds = document.getElementById("soundsEnabled");
            if (sounds != null && sounds.checked) {
                // play outgoing sound, if enabled
                document.getElementById("sounds").innerHTML=
                    "<embed src='<%= request.getContextPath()%>/sounds?workgroup=<%=StringUtils.URLEncode(workgroup, "utf-8")%>&action=outgoing' style=display:none; hidden=true autostart=true loop=false>";
            }
        }
        // reset the chatbox textarea
        chatbox.focus();
        chatbox.value = "";
    }

    function showCobrowser(url){
      var width = 800;
      var height = 700;
      var defaultOptions = "location=yes,status=no,toolbar=no,personalbar=no,menubar=no,directories=no,";
      var winleft = (screen.width - width) / 2;
      var winUp = (screen.height - height) / 2;

      defaultOptions += "scrollbars=yes,resizable=yes,top=" + winUp + ",left=" + winleft + ",";
      defaultOptions += "width=" + width + ",height=" + height;
      cobrowseWin = window.open(url, 'cobrowser', defaultOptions);

      room.sendMessage(null, '<%= StringUtils.escapeHTMLTags(chatID) %>', 'I have accepted the Cobrowse invitation for '+url);
    }

    function checkUnload(){
      if(!isRedirecting){
         window.location.href = 'exit-queue.jsp?workgroup=<%=StringUtils.URLEncode(workgroup, "utf-8")%>&chatID=<%=StringUtils.URLEncode(chatID, "utf-8")%>';
      }
    }

    function isTypingNotification() {
	   room.customerIsTyping(null, '<%= chatID%>');
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

    <script language="JavaScript" type="text/javascript">
     function showTranscriptWindow(message) {
        isRedirecting = true;
        location.href = 'transcriptmain.jsp?workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>&chatID=<%= StringUtils.URLEncode(chatID, "utf-8")%>';
     }

     function confirmExit(){
         var ok = confirm('Are you sure you want to end your session?');
         if(ok){
             showTranscriptWindow(null);
         }
     }

     function doit() {
    <%

      String initialAgent = chatSession.getInitialAgent();
      if(initialAgent == null){
         %>
           alert("We are unable to connect you to an agent. Please try back later.")
           window.location.href = "userinfo.jsp?workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>&chatID=<%= StringUtils.URLEncode(chatID, "utf-8") %>";
         <%
      }
    %>
       addChatText(window.frames['yak'], '', '<%= FormText.getChatRoomWelcomeMessage(initialAgent, workgroup) %>');
       checkForNewMessages();
       checkIfAgentTyping();
     }

     function addText(from, body) {
       addChatText(window.frames['yak'], from, body);
     }
    </script>
   </head>

   <body style="margin-top:0px;margin-right:20px;margin-left:20px;margin-bottom:10px;" id="defaultwindow" onload="doit();" onunload="checkUnload();">

    <span id=sounds></span>

    <%-- iframe for main chat transcript --%>
    <table height="100%" width="100%" cellpadding="3" cellspacing="0">
    <tr valign="top">
    <td> <img src="getimage?image=logo&workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>"/></td>
     <td align="right" colspan="3">
     <a href="#" onclick="confirmExit();return false;"
        title="Click to end session.">
     <img src="getimage?image=end&workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>" border="0">
     </a>
     </td>
    </tr>
    <tr>
     <td style="width:100%;height: 100%;" colspan="4">
     <iframe class="box"      name="yak"    src="yakframe.html" frameborder="0" marginheight="0" marginwidth="0"
             scrolling="auto" height="100%" width="100%">
     </iframe>
     </td>
    </tr>
    <%
        SoundSettings soundSettings = null;
        try {
            soundSettings = chatSession.getWorkgroup().getSoundSettings();
        }
        catch (XMPPException e) {
        }
        if(soundSettings != null && soundSettings.getIncomingSoundBytes() != null){
    %>
    <tr>
    <td width="100%" colspan="4">
     <input type="checkbox" name="soundsEnabled" id="soundsEnabled" CHECKED/> Play Sounds
    </td>
    </tr>
    <% } %>
    <tr><form name="f">
    <td width="100%" colspan="3">
      <textarea class="box" cols="40" rows="4" name="chatbox" wrap="virtual"  style="width:100%;height:50px;overflow:hidden;"></textarea>
    </td></form>
    <td align="right" width="1%" nowrap valign="top">
     <img src="getimage?image=sendmessage&workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>"
         alt="Send Message"
         title="Send Message"
         onclick="javascript:submitMessage();"/>
    </td>

    </tr>
     <tr valign="bottom">

                <td colspan="4">
         <table width="100%" cellpadding="0" cellspacing="0">
             <tr valign="bottom"><td height="30"> <div id="typingAgent"></div></td>
             <td>
             <% if (request.isSecure()) { %>
                                        <div id="isSecure">
                                            <img src="images/secure_button.gif" border="0"/>
                                        </div>
             <%}%>
                </td>
         </table>

 <div style="position:absolute;bottom:0px;right:5px"><img src="getimage?image=poweredby&workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>"/></div>


    <script>
    var timeOut;

    function checkForNewMessages() {
       room.getAllMessages(insertMessages, '<%= chatID %>');
       lastChecked = new Date().getTime();
    }

    function checkIfAgentTyping() {
        room.isTyping(isTyping, '<%= chatID %>');
   }

   function isTyping(agentIsTyping){
     if(agentIsTyping){
        document.getElementById('typingAgent').innerHTML = '<img src="getimage?image=agenttyping&workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>" />'
     }
     else {
        document.getElementById('typingAgent').innerHTML = ''
     }
     setTimeout("checkIfAgentTyping()", 2000);
   }

   function insertMessages(messages){
      if(messages == null){
        clearTimeout(timeOut);

        chatHasEnded();

        room.getEndMessage(showEndMessage, '<%= StringUtils.escapeHTMLTags(chatID)%>', '<%=StringUtils.escapeHTMLTags(workgroup)%>');
        return;
      }

      for(i=0; i<messages.length;i++){
         if(messages[i].cobrowsing){
           if(cobrowseWin && !cobrowseWin.closed){
             cobrowseWin.location.href= messages[i].urlToPush;
             cobrowseWin.focus();
           }
           else if(messages[i].body != null) {
              var urlToPush = messages[i].urlToPush;
              addText('', "<a href=javascript:parent.showCobrowser('"+urlToPush+"')>You have received a Cobrowsing invitation. Click to initiate a Cobrowser session.</a>");
           }
         }
         else {
           addChatText(window.frames['yak'], messages[i].from, messages[i].body);
         }
      }

      if(messages.length > 0 && !messages[0].cobrowsing){
        scrollYakToEnd(window.frames['yak']);
        document.getElementById('typingAgent').innerHTML = ''
        room.clearAgentTyping(null, '<%= StringUtils.escapeHTMLTags(chatID) %>');

        var hasSounds = document.getElementById("soundsEnabled");

        if (hasSounds != null && document.getElementById("soundsEnabled").checked) {
          // play incoming sound, if enabled
          document.getElementById("sounds").innerHTML=
              "<embed src='<%= request.getContextPath()%>/sounds?workgroup=<%=StringUtils.URLEncode(workgroup,"utf-8")%>&action=incoming' style=display:none; hidden=true autostart=true loop=false>";
        }

        // blink the window -- IE only
        if (document.all) {
            focus();
            document.f.chatbox.focus();
        }
        else {
           window.parent.focus();
        }
      }

      timeOut = setTimeout("checkForNewMessages()", 2000);
   }

      function showEndMessage(message){

        var ok = confirm(message);
        if(ok){
          isRedirecting = true;
          showTranscriptWindow(null);
        }
        else {
          window.location.href = "exit-window.jsp";
        }
      }
    </script>

   </body>
  </html>


