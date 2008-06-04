package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.util.ModelUtil;
import com.jivesoftware.smack.workgroup.user.Workgroup;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.webchat.util.FormUtils;
import org.jivesoftware.webchat.util.FormText;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.webchat.util.ParamUtils;
import java.util.*;
import org.jivesoftware.webchat.actions.WorkgroupStatus;

public final class userinfo_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write("\r\n\r\n\r\n\r\n\r\n\r\n<!-- Get and Set Workgroup -->\r\n");
      org.jivesoftware.webchat.ChatUser chatUser = null;
      synchronized (_jspx_page_context) {
        chatUser = (org.jivesoftware.webchat.ChatUser) _jspx_page_context.getAttribute("chatUser", PageContext.PAGE_SCOPE);
        if (chatUser == null){
          chatUser = new org.jivesoftware.webchat.ChatUser();
          _jspx_page_context.setAttribute("chatUser", chatUser, PageContext.PAGE_SCOPE);
        }
      }
      out.write('\r');
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.introspect(_jspx_page_context.findAttribute("chatUser"), request);
      out.write('\r');
      out.write('\n');

    boolean authFailed = ParamUtils.getParameter(request, "authFailed") != null;

    String location = (String)session.getAttribute("pageLocation");
    if (chatUser.hasSession()) {
        chatUser.removeSession();
    }

    String workgroup = chatUser.getWorkgroup();
    String chatID = chatUser.getChatID();
    if (chatID == null) {
        chatID = StringUtils.randomString(10);
    }

    Workgroup chatWorkgroup = WorkgroupStatus.getWorkgroup(workgroup);
    if (!chatWorkgroup.isAvailable()) {
        response.sendRedirect("email/leave-a-message.jsp?workgroup=" + workgroup);
        return;
    }

    Form workgroupForm = WorkgroupStatus.getWorkgroupForm(workgroup);
    String welcomeText = FormText.getWelcomeText(workgroup);
    String startButton = FormText.getStartButton(workgroup);

    // Add query string to session
    Enumeration params = request.getParameterNames();
    Map paramMap = new HashMap();
    while (params.hasMoreElements()) {
        String paramName = (String)params.nextElement();
        String paramValue = (String)request.getParameter(paramName);
        paramMap.put(paramName, paramValue);
    }
    session.setAttribute("params", paramMap);

    boolean hasElements = false;

      out.write("\r\n    <!-- DEFINE SCRIPTING VARIABLES -->\r\n    <script language=\"JavaScript\">\r\n        var isNav, isIE\r\n\r\n        if (parseInt(navigator.appVersion) >= 4) {\r\n            if (navigator.appName == \"Netscape\")\r\n                isNav = true\r\n            else\r\n                isIE = true\r\n        }\r\n\r\n        function checkForEnter(evt, f) {\r\n\r\n            var val = 0;\r\n            if (isNav) {\r\n                val = evt.which;\r\n            }\r\n            if (! isNav) {\r\n                val = window.event.keyCode;\r\n            }\r\n\r\n            if (val == 13) {\r\n            }\r\n        }\r\n    </script>\r\n\r\n   <script type=\"text/javascript\">\r\n    function popup(myform) {\r\n        var width = 500;\r\n        var height = 400;\r\n        if (!ValidateForm()) {\r\n            return false;\r\n        }\r\n        if (! window.focus)return true;\r\n        var d = new Date();\r\n\r\n        windowname = \"framemain\";\r\n        var winleft = (screen.width - width) / 2;\r\n        var winUp = (screen.height - height) / 2;\r\n\r\n\r\n        window.open('', windowname, 'top=' + winUp + ',left=' + winleft + ',height='+height+',width='+width+',location=no,resizable=no,scrollbars=no,status=no');\r\n");
      out.write("        myform.target = windowname;\r\n        return true;\r\n    }\r\n    </script>\r\n\r\n    <script>\r\n        function ValidateForm() {\r\n        ");

               Iterator iter = workgroupForm.getFields();
               while(iter.hasNext()){
                   FormField field = (FormField)iter.next();
                   boolean required = field.isRequired();
                   String variable = field.getVariable();
                   if(required && !"email".equals(variable)){

        
      out.write("\r\n            if ((! Jtrim(document.f.");
      out.print(variable);
      out.write(".value).length)) {\r\n                alert(\"You must specify all required fields.\");\r\n                document.f.");
      out.print( variable);
      out.write(".focus();\r\n                return false;\r\n            }\r\n        ");
 }else if(required){ 
      out.write("\r\n            if ((!isValidEmail(document.f.");
      out.print(variable);
      out.write(".value))) {\r\n                alert(\"A valid email address is required.\");\r\n                document.f.");
      out.print( variable);
      out.write(".focus();\r\n                return false;\r\n            }\r\n        ");
 }} 
      out.write("\r\n            document.f.submitForm.disabled = true;\r\n            return true;\r\n        }\r\n\r\n        function isBlank(val) {\r\n\r\n            if (val == null) {\r\n                return true;\r\n            }\r\n            for (var i = 0; i < val.length; i++) {\r\n                if ((val.charAt(i) != ' ') && (val.charAt(i) != \"\\t\") && (val.charAt(i) != \"\\n\") && (val.charAt(i) != \"\\r\")) {\r\n                    return false;\r\n                }\r\n            }\r\n\r\n            return true;\r\n        }\r\n\r\n        function isInteger(val) {\r\n\r\n            if (isBlank(val)) {\r\n                return false;\r\n            }\r\n            for (var i = 0; i < val.length; i++) {\r\n                if (! isDigit(val.charAt(i))) {\r\n                    return false;\r\n                }\r\n            }\r\n\r\n            return true;\r\n        }\r\n\r\n        function isValidEmail(str) {\r\n            if(!Jtrim(str).length){\r\n              return false;\r\n            }\r\n            var apos = str.indexOf(\"@\");\r\n            var dpos = str.indexOf(\".\");\r\n");
      out.write("            var epos = str.indexOf(\"]\");\r\n            var fpos = str.indexOf(\"[\");\r\n            if (apos <= 0 || epos > 0 || fpos > 0)\r\n                return false;\r\n\r\n            return true;\r\n        }\r\n\r\n        function Jtrim(st) {\r\n\r\n            var len = st.length;\r\n            var begin = 0, end = len - 1;\r\n            while (st.charAt(begin) == \" \" && begin < len) {\r\n                begin++;\r\n            }\r\n            while (st.charAt(end) == \" \" && end > begin) {\r\n                end--;\r\n            }\r\n\r\n            return st.substring(begin, end + 1);\r\n        }\r\n\r\n        function isDigit(num) {\r\n\r\n            if (num.length > 1) {\r\n                return false;\r\n            }\r\n            var string = \"1234567890\";\r\n            if (string.indexOf(num) != - 1) {\r\n                return true;\r\n            }\r\n\r\n            return false;\r\n        }\r\n        // -->\r\n    </script>\r\n\r\n    <!-- END OF SCRIPTING VARIABLES -->\r\n\r\n");

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

      out.write("\r\n\r\n    <html>\r\n    <head>\r\n        <title>Information </title>\r\n\r\n        <link rel=\"stylesheet\"\r\n              type=\"text/css\"\r\n              href=\"style.jsp?workgroup=");
      out.print( workgroup );
      out.write("\"/><script language=\"JavaScript\" type=\"text/javascript\" src=\"common.js\">//Ignore</script>\r\n  </head>\r\n  <body style=\"margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px\" id=\"defaultwindow\">\r\n    <table width=\"100%\" cellpadding=\"3\" cellspacing=\"2\">\r\n    <tr><td colspan=\"2\" height=\"1%\">\r\n    <img src=\"getimage?image=logo&workgroup=");
      out.print( workgroup );
      out.write("\"/>\r\n        </td>\r\n    </tr>\r\n      <form name=\"f\" id=\"f\" action=\"queue.jsp\" method=\"post\" onSubmit=\"return popup(this);\">\r\n       <!-- Identify all hidden variables. All variables will be passed to the metadata router.\r\n            You can do any name-value pairing you like. Such as product=Jive Live Assistant. Such\r\n            data can be used to effectivly route to a particular queue within a workgroup.\r\n       -->\r\n        <input type=\"hidden\" name=\"workgroup\" value=\"");
      out.print( workgroup );
      out.write("\"/>\r\n        <input type=\"hidden\" name=\"chatID\" value=\"");
      out.print( chatID );
      out.write("\" />\r\n       <!-- End of Hidden Variable identifiers -->\r\n          <tr>\r\n                <td colspan=\"2\" height=\"1%\">\r\n                <br/>");
      out.print(  welcomeText );
      out.write("\r\n                </td>\r\n            </tr>\r\n            <tr>\r\n              <td height=\"1%\">\r\n                <br/>\r\n              </td>\r\n            </tr>\r\n\r\n            ");
 if (authFailed) { 
      out.write("\r\n            <tr valign=\"top\">\r\n               <td class=\"formtext\" colspan=\"2\" height=\"1%\" nowrap><span class=\"error\">Authentication Failed</span></td>\r\n            </tr>\r\n            ");
 } 
      out.write("\r\n\r\n            ");

                       Iterator fields = workgroupForm.getFields();
                       while(fields.hasNext()){
                           hasElements = true;
                           FormField field = (FormField)fields.next();
                           String label = field.getLabel();
                           boolean required = field.isRequired();
                           String requiredStr = required ? "&nbsp;<span class=\"error\">*</span>" : "";
                           if(!field.getType().equals(FormField.TYPE_HIDDEN)){
                   
      out.write("\r\n                   <tr valign=\"top\">\r\n                     <td class=\"formtext\" height=\"1%\" width=\"1%\" nowrap>");
      out.print( label);
      out.print( requiredStr);
      out.write("</td><td>");
      out.print( FormUtils.createAnswers(field, request));
      out.write("</td>\r\n                   </tr>\r\n                   ");
 } } 
      out.write("\r\n\r\n            <tr valign=\"top\">\r\n              <td height=\"1%\">\r\n              <!-- All workgroup defined variables -->\r\n                ");

                       fields = workgroupForm.getFields();
                       while(fields.hasNext()){
                           FormField field = (FormField)fields.next();
                           if(field.getType().equals(FormField.TYPE_HIDDEN)){
                 
      out.write("\r\n                 ");
      out.print( FormUtils.createDynamicField(field, request));
      out.write("\r\n                 ");
 }} 
      out.write("\r\n              <!-- End of Defined Variables -->\r\n\r\n                 ");
 // Handle page location
                     if(location != null){ 
      out.write("\r\n                     <input type=\"hidden\" name=\"location\" value=\"");
      out.print( location);
      out.write("\" />\r\n                 ");
 } 
      out.write("\r\n              </td>\r\n                <td> <input type=\"submit\" name=\"submitForm\" value=\"");
      out.print( startButton);
      out.write("\"/></td>\r\n            </tr>\r\n            <tr>\r\n\r\n            </tr>\r\n           </form>\r\n          </table>\r\n <div style=\"position:absolute;bottom:0px;right:5px\"><img src=\"getimage?image=poweredby&workgroup=");
      out.print( workgroup );
      out.write("\"/></div>\r\n  </body>\r\n    ");
if(!hasElements){ 
      out.write("\r\n    <script type=\"text/javascript\">\r\n        document.f.submit();\r\n    </script>\r\n    ");
}
      out.write("\r\n</html>\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
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
