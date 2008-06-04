<%--
  - $RCSfile$
  - $Revision: 29022 $
  - $Date: 2006-04-01 21:58:02 -0800 (Sat, 01 Apr 2006) $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%@ page import="java.io.Writer,
                 java.io.StringWriter,
                 java.io.PrintWriter,
                 org.jivesoftware.webchat.util.WebLog" %>

<%@ page isErrorPage="true"%>

<%
    if (exception != null) {
        try {
            application.log("Error in Web Client", exception);
            WebLog.logError("Error in web client", exception);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

%>

    <html>
    <head>
      <title>Chat Service Not Available</title>

      <link rel="stylesheet" type="text/css" href="style.jsp"/>
    </head>

    <body id="defaultwindow">

<table cellspacing="0" cellpadding="4" width="100%" border="0">
<tbody>
    <tr valign="top">
    <td>
        <h3>Online Chat Service</h3>
    </td>
    </tr>
    <tr>
    <td width="99%">
        Our chat service is unavailable at this time. Please check back soon.
      </td>
      </tr>
      <tr>
      <td width="1%" colspan="2">
      <%= getStackTrace(exception) %>
      </td>
      </tr>
      </table>
    </body>
    </html>

    <%!
         public static String getStackTrace(Throwable aThrowable) {
            if(aThrowable == null){
                return "";
            }
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            aThrowable.printStackTrace(printWriter);
            return result.toString();
         }
    %>
