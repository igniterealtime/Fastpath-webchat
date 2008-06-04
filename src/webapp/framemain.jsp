<%@ taglib uri = "http://java.sun.com/jstl/core" prefix = "c"%>

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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
  <html>
    <head>
      <title><c:out value="${initParam.brandingTitle}"/></title>

      <link rel="stylesheet" type="text/css" href="style.jsp"/>

      <script language="JavaScript" type="text/javascript" href="common.js"></script>

      <script language="JavaScript" type="text/javascript">

        // Data for the entire chat:
        var transcript = new Array();

        var startDate = new Date(0);
        var endDate = new Date(0);

        // Function to add a line in the chat:
        function addChatLine(from, data) {
          transcript[transcript.length] = new Array(from, data);
        }

        function printTranscript(yakWin) {

          for (var i = 0; i < transcript.length; i++) {
            var from = transcript[i][0];
            var text = transcript[i][1];
            addChatText(yakWin, from, text);
          }
        }

        function handleWindowClose() {
          location.href = 'LiveAssistantServlet?action=close';
        }
      </script>
    </head>

    <frameset rows="*" border="0" frameborder="0" framespacing="0" onunload="handleWindowClose();">
      <frame name="main"      src="userinfo-default.jsp?workgroup=<c:out value="${workgroup}" />" marginwidth="0"
             marginheight="0" scrolling="no"                                                      frameborder="0"
             noresize>
    </frameset>
  </html>
