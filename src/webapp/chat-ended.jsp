<%--
  - $RCSfile$
  - $Revision: 28176 $
  - $Date: 2006-03-07 10:01:10 -0800 (Tue, 07 Mar 2006) $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%@ page import="org.jivesoftware.webchat.util.WebLog,
                 java.io.Writer,
                 java.io.StringWriter,
                 java.io.PrintWriter" %>

<%
    String workgroup = request.getParameter("workgroup");
%>

    <html>
    <head>
      <title>Chat Ended</title>

      <link rel="stylesheet" type="text/css" href="style.jsp"/>
    </head>

    <body style="margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px" id="defaultwindow">
      <table width="100%">
          <tr><td>
              <% if(workgroup != null){ %>
                <img src="getimage?image=logo&workgroup=<%= workgroup %>"/>
              <% } %>


              </td></tr>
          <tr><td><br/></td></tr>
          <tr><td><b>Chat Ended</b></td></tr>
          <tr><td>Thank you for using our chat service.</td></tr>
      </table>

    </body>
    </html>