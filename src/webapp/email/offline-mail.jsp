<%@ page import="org.jivesoftware.webchat.util.FormText"%>
<%
     String workgroup = request.getParameter("workgroup");
     String offlineText = FormText.getNoHelpText(workgroup);

%>
    <html>
    <head>
        <title>Offline</title>
        <link rel="stylesheet"
                     type="text/css"
                     href="../style.jsp?workgroup=<%= workgroup %>"/><script language="JavaScript" type="text/javascript" src="common.js">//Ignore</script>

  </head>
   <body style="margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px" id="defaultwindow">
      <table width="100%" border="0" height="100%">
      <tr>
      <td height="1%" >
       <img alt="workgroup image" src="../getimage?image=logo&workgroup=<%= workgroup %>"/>
      </td>
      </tr>
      <tr>
        <td height="1%"><br/><%= offlineText%><br/><br/><br/></td>
      </tr>
      <tr>
      <td align="center">
        <form action="offline-mail.jsp">
                        <input type="submit" name="leave" value="Close Window"  onclick="window.close();">
        </form>
      </td>
      </tr>
      <tr valign="bottom" height="99%">
      <td align="right"></td>
      </tr>
      </table>
    <div style="position:absolute;bottom:0px;right:5px"><img src="../getimage?image=poweredby&workgroup=<%= workgroup %>"/></div>
  </body>
</html>







