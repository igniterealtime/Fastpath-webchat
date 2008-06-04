package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.ModelUtil;

public final class agentinfo_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n\r\n");

    String jid = request.getParameter("jid");
    String email = request.getParameter("email");
    String agentName = request.getParameter("agentName");

      out.write("\r\n    <!-- DEFINE SCRIPTING VARIABLES -->\r\n\r\n<script type=\"text/javascript\" language=\"JavaScript\">\r\n      var isNav, isIE\r\n\r\n      if (parseInt(navigator.appVersion) >= 4) {\r\n        if (navigator.appName == \"Netscape\")\r\n          isNav = true\r\n        else\r\n          isIE = true\r\n      }\r\n\r\n      function checkForEnter(evt, f) {\r\n\r\n        var val = 0;\r\n        if (isNav) {\r\n          val = evt.which;\r\n        }\r\n        if (! isNav) {\r\n          val = window.event.keyCode;\r\n        }\r\n\r\n        if (val == 13) {\r\n        }\r\n      }\r\n    </script>\r\n\r\n    <script type=\"text/javascript\">\r\n      function changeScreenSize(w, h) {\r\n        window.resizeTo(w, h)\r\n      }\r\n\r\n      function ValidateForm() {\r\n        if ((! Jtrim(document.f.Name.value).length)) {\r\n          alert(\"Please specify a name to identify yourself.\");\r\n          document.f.Name.focus();\r\n          return false;\r\n        }\r\n\r\n        if ((! Jtrim(document.f.Email.value).length)) {\r\n          alert(\"Please specify a valid email address.\");\r\n          document.f.Email.focus();\r\n");
      out.write("          return false;\r\n        }\r\n\r\n        if ((Jtrim(document.f.Email.value).length)) {\r\n          if ((! isValidEmail(document.f.Email.value))) {\r\n            alert(\"The email address is not valid. Please specify a valid email address.\");\r\n            document.f.Email.focus();\r\n            return false;\r\n          }\r\n        }\r\n        return true;\r\n      }\r\n\r\n      function isBlank(val) {\r\n\r\n        if (val == null) {\r\n          return true;\r\n        }\r\n        for (var i = 0; i < val.length; i++) {\r\n          if ((val.charAt(i) != ' ') && (val.charAt(i) != \"\\t\") && (val.charAt(i) != \"\\n\") && (val.charAt(i) != \"\\r\")) {\r\n            return false;\r\n          }\r\n        }\r\n\r\n        return true;\r\n      }\r\n\r\n      function isInteger(val) {\r\n\r\n        if (isBlank(val)) {\r\n          return false;\r\n        }\r\n        for (var i = 0; i < val.length; i++) {\r\n          if (! isDigit(val.charAt(i))) {\r\n            return false;\r\n          }\r\n        }\r\n\r\n        return true;\r\n      }\r\n\r\n      function isValidEmail(str) {\r\n");
      out.write("\r\n        var apos = str.indexOf(\"@\");\r\n        var dpos = str.indexOf(\".\");\r\n        var epos = str.indexOf(\"]\");\r\n        var fpos = str.indexOf(\"[\");\r\n        if (apos <= 0 || epos > 0 || fpos > 0)\r\n          return false;\r\n\r\n        return true;\r\n      }\r\n\r\n      function Jtrim(st) {\r\n\r\n        var len = st.length;\r\n        var begin = 0, end = len - 1;\r\n        while (st.charAt(begin) == \" \" && begin < len) {\r\n          begin++;\r\n        }\r\n        while (st.charAt(end) == \" \" && end > begin) {\r\n          end--;\r\n        }\r\n\r\n        return st.substring(begin, end + 1);\r\n      }\r\n\r\n      function isDigit(num) {\r\n\r\n        if (num.length > 1) {\r\n          return false;\r\n        }\r\n        var string = \"1234567890\";\r\n        if (string.indexOf(num) != - 1) {\r\n          return true;\r\n        }\r\n\r\n        return false;\r\n      }\r\n      // -->\r\n    </script>\r\n    <!-- END OF SCRIPTING VARIABLES -->\r\n\r\n     <script type=\"text/javascript\">\r\n    function popup(myform) {\r\n        var width = 500;\r\n        var height = 400;\r\n");
      out.write("        if (!ValidateForm()) {\r\n            return false;\r\n        }\r\n        if (! window.focus)return true;\r\n        var d = new Date();\r\n\r\n        windowname = d.getTime();\r\n        var winleft = (screen.width - width) / 2;\r\n        var winUp = (screen.height - height) / 2;\r\n\r\n\r\n        window.open('', windowname, 'top=' + winUp + ',left=' + winleft + ',height='+height+',width='+width+',location=no,resizable=yes,scrollbars=no,status=no');\r\n        myform.target = windowname;\r\n        return true;\r\n    }\r\n    </script>\r\n");


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

      out.write("\r\n    <html>\r\n    <head>\r\n      <title>Live Assistant Personal Contact</title>\r\n\r\n      <link rel=\"stylesheet\" type=\"text/css\" href=\"style.jsp\">\r\n      <script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\">//Ignore</script>\r\n  </head>\r\n  ");

        String title = "Chat Live with "+agentName;
        String descriptiveTitle = "Enter a name to identify yourself to "+agentName+". You may supply additional information if you like.";
  
      out.write("\r\n  <body>\r\n  <div align=\"center\">\r\n  <table height=\"100%\" border=\"0\">\r\n  <tr><td>\r\n\r\n\r\n  <form name=\"f\" id=\"f\" action=\"chatroom.jsp\" method=\"post\" onSubmit=\"return popup(this);return false;\">\r\n  <table border=\"0\" class=\"box\">\r\n  <tr><td>\r\n  <img alt=\"title tag\" src=\"images/title.gif\"/>\r\n  </td></tr>\r\n  <tr>\r\n  <td>\r\n       <input type=\"hidden\" name=\"jid\" value=\"");
      out.print( jid );
      out.write("\" />\r\n       <input type=\"hidden\" name=\"agentName\" value=\"");
      out.print( agentName );
      out.write("\" />\r\n\r\n\r\n          <table cellpadding=\"0\" cellspacing=\"2\">\r\n            <tr>\r\n              <td colspan=\"2\" class=\"nicetext\">\r\n                <b>");
      out.print(  title );
      out.write("</b>\r\n              </td>\r\n            </tr>\r\n            <tr>\r\n              <td colspan=\"2\" class=\"nicetextsmall\">\r\n                ");
      out.print(  descriptiveTitle );
      out.write("\r\n              </td>\r\n            </tr>\r\n            <tr>\r\n              <td class=\"error\">*required field</td>\r\n            </tr>\r\n            <tr>\r\n              <td>\r\n                <br/>\r\n              </td>\r\n            </tr>\r\n            <tr>\r\n              <td class=\"formtext\">Name:\r\n                <span class=\"error\">&nbsp;*</span>\r\n              </td>\r\n              <td class=\"formtext\">Email:\r\n                <span class=\"error\">&nbsp;*</span>\r\n              </td>\r\n            </tr>\r\n            <tr>\r\n              <td class=\"formtext\">\r\n                <input type=\"text\" name=\"Name\" size=\"30\" value=\"");
      out.print( nameValue );
      out.write("\"/>\r\n              </td>\r\n              <td>\r\n                <input type=\"text\" name=\"Email\" value=\"");
      out.print( emailValue );
      out.write("\" size=\"30\"/>\r\n              </td>\r\n            </tr>\r\n            <tr valign=\"top\">\r\n              <td class=\"formtext\" colspan=\"2\">Question:</td>\r\n            </tr>\r\n            <tr valign=\"top\">\r\n              <td class=\"formtext\" colspan=\"2\">\r\n                <input type=\"text\" name=\"Question\" size=\"60\"/>\r\n              </td>\r\n            </tr>\r\n            <tr valign=\"top\">\r\n              <td>\r\n                <input type=\"submit\" name=\"submit\" value=\"Chat with ");
      out.print( agentName);
      out.write("\"/>\r\n              </td>\r\n            </tr>\r\n          </table>\r\n        </div>\r\n      </form>\r\n    </div>\r\n   </td>\r\n   </tr>\r\n   <tr>\r\n   <td align=\"right\">\r\n      <img src=\"images/poweredBy.gif\"/>\r\n   </td>\r\n   </tr>\r\n   </table>\r\n\r\n   </td></tr></table>\r\n<script language=\"JavaScript\" type=\"text/javascript\">\r\n      document.f.Name.focus();\r\n      </script>\r\n  </body>\r\n</html>\r\n\r\n\r\n\r\n\r\n\r\n");
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
