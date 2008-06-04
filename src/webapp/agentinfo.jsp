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

<%@ page import = "org.jivesoftware.webchat.util.ModelUtil"
                   errorPage="fatal.jsp"%>

<%
    String jid = request.getParameter("jid");
    String email = request.getParameter("email");
    String agentName = request.getParameter("agentName");
%>
    <!-- DEFINE SCRIPTING VARIABLES -->

<script type="text/javascript" language="JavaScript">
      var isNav, isIE

      if (parseInt(navigator.appVersion) >= 4) {
        if (navigator.appName == "Netscape")
          isNav = true
        else
          isIE = true
      }

      function checkForEnter(evt, f) {

        var val = 0;
        if (isNav) {
          val = evt.which;
        }
        if (! isNav) {
          val = window.event.keyCode;
        }

        if (val == 13) {
        }
      }
    </script>

    <script type="text/javascript">
      function changeScreenSize(w, h) {
        window.resizeTo(w, h)
      }

      function ValidateForm() {
        if ((! Jtrim(document.f.Name.value).length)) {
          alert("Please specify a name to identify yourself.");
          document.f.Name.focus();
          return false;
        }

        if ((! Jtrim(document.f.Email.value).length)) {
          alert("Please specify a valid email address.");
          document.f.Email.focus();
          return false;
        }

        if ((Jtrim(document.f.Email.value).length)) {
          if ((! isValidEmail(document.f.Email.value))) {
            alert("The email address is not valid. Please specify a valid email address.");
            document.f.Email.focus();
            return false;
          }
        }
        return true;
      }

      function isBlank(val) {

        if (val == null) {
          return true;
        }
        for (var i = 0; i < val.length; i++) {
          if ((val.charAt(i) != ' ') && (val.charAt(i) != "\t") && (val.charAt(i) != "\n") && (val.charAt(i) != "\r")) {
            return false;
          }
        }

        return true;
      }

      function isInteger(val) {

        if (isBlank(val)) {
          return false;
        }
        for (var i = 0; i < val.length; i++) {
          if (! isDigit(val.charAt(i))) {
            return false;
          }
        }

        return true;
      }

      function isValidEmail(str) {

        var apos = str.indexOf("@");
        var dpos = str.indexOf(".");
        var epos = str.indexOf("]");
        var fpos = str.indexOf("[");
        if (apos <= 0 || epos > 0 || fpos > 0)
          return false;

        return true;
      }

      function Jtrim(st) {

        var len = st.length;
        var begin = 0, end = len - 1;
        while (st.charAt(begin) == " " && begin < len) {
          begin++;
        }
        while (st.charAt(end) == " " && end > begin) {
          end--;
        }

        return st.substring(begin, end + 1);
      }

      function isDigit(num) {

        if (num.length > 1) {
          return false;
        }
        var string = "1234567890";
        if (string.indexOf(num) != - 1) {
          return true;
        }

        return false;
      }
      // -->
    </script>
    <!-- END OF SCRIPTING VARIABLES -->

     <script type="text/javascript">
    function popup(myform) {
        var width = 500;
        var height = 400;
        if (!ValidateForm()) {
            return false;
        }
        if (! window.focus)return true;
        var d = new Date();

        windowname = d.getTime();
        var winleft = (screen.width - width) / 2;
        var winUp = (screen.height - height) / 2;


        window.open('', windowname, 'top=' + winUp + ',left=' + winleft + ',height='+height+',width='+width+',location=no,resizable=yes,scrollbars=no,status=no');
        myform.target = windowname;
        return true;
    }
    </script>
<%

    Cookie [] cookies = request.getCookies();
    String nameValue = null;
    String emailValue = null;

    if (cookies != null) {
        for (int i = 0; i < cookies.length; i++) {
            String name = cookies[i].getName();

            if (name.equals("jive_name")) {
                nameValue = cookies[i].getValue();
            }
            else if (name.equals("jive_email")) {
                emailValue = cookies[i].getValue();
            }
            else if (name.equals("uniqueid")) {
                session.setAttribute("uniqueid", cookies[i].getValue());
            }
        }
        nameValue = ModelUtil.emptyStringIfNull(nameValue);
        emailValue = ModelUtil.emptyStringIfNull(emailValue);

        nameValue = nameValue.replaceAll("_", " ");
    }
%>
    <html>
    <head>
      <title>Live Assistant Personal Contact</title>

      <link rel="stylesheet" type="text/css" href="style.jsp">
      <script language="JavaScript" type="text/javascript" src="common.js">//Ignore</script>
  </head>
  <%
        String title = "Chat Live with "+agentName;
        String descriptiveTitle = "Enter a name to identify yourself to "+agentName+". You may supply additional information if you like.";
  %>
  <body>
  <div align="center">
  <table height="100%" border="0">
  <tr><td>


  <form name="f" id="f" action="chatroom.jsp" method="post" onSubmit="return popup(this);return false;">
  <table border="0" class="box">
  <tr><td>
  <img alt="title tag" src="images/title.gif"/>
  </td></tr>
  <tr>
  <td>
       <input type="hidden" name="jid" value="<%= jid %>" />
       <input type="hidden" name="agentName" value="<%= agentName %>" />


          <table cellpadding="0" cellspacing="2">
            <tr>
              <td colspan="2" class="nicetext">
                <b><%=  title %></b>
              </td>
            </tr>
            <tr>
              <td colspan="2" class="nicetextsmall">
                <%=  descriptiveTitle %>
              </td>
            </tr>
            <tr>
              <td class="error">*required field</td>
            </tr>
            <tr>
              <td>
                <br/>
              </td>
            </tr>
            <tr>
              <td class="formtext">Name:
                <span class="error">&nbsp;*</span>
              </td>
              <td class="formtext">Email:
                <span class="error">&nbsp;*</span>
              </td>
            </tr>
            <tr>
              <td class="formtext">
                <input type="text" name="Name" size="30" value="<%= nameValue %>"/>
              </td>
              <td>
                <input type="text" name="Email" value="<%= emailValue %>" size="30"/>
              </td>
            </tr>
            <tr valign="top">
              <td class="formtext" colspan="2">Question:</td>
            </tr>
            <tr valign="top">
              <td class="formtext" colspan="2">
                <input type="text" name="Question" size="60"/>
              </td>
            </tr>
            <tr valign="top">
              <td>
                <input type="submit" name="submit" value="Chat with <%= agentName%>"/>
              </td>
            </tr>
          </table>
        </div>
      </form>
    </div>
   </td>
   </tr>
   <tr>
   <td align="right">
      <img src="images/poweredBy.gif"/>
   </td>
   </tr>
   </table>

   </td></tr></table>
<script language="JavaScript" type="text/javascript">
      document.f.Name.focus();
      </script>
  </body>
</html>





