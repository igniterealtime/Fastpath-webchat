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

<%@ page  import = "org.jivesoftware.webchat.ChatManager,
                    com.jivesoftware.smack.workgroup.user.Workgroup,
                    org.jivesoftware.smackx.Form,
                    org.jivesoftware.smackx.FormField"
                    errorPage="fatal.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="org.jivesoftware.webchat.actions.WorkgroupStatus" %>
<%@ page import="org.jivesoftware.webchat.util.*" %>
<!-- Get and Set Workgroup -->
<jsp:useBean class="org.jivesoftware.webchat.ChatUser" id="chatUser" />
<jsp:setProperty name="chatUser" property="*" />
<%
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
        response.sendRedirect("email/leave-a-message.jsp?workgroup=" + StringUtils.URLEncode(workgroup, "utf-8"));
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
%>
    <!-- DEFINE SCRIPTING VARIABLES -->
    <script language="JavaScript">
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
    function popup(myform) {
        var width = 500;
        var height = 400;
        if (!ValidateForm()) {
            return false;
        }
        if (! window.focus)return true;
        var d = new Date();

        windowname = "framemain";
        var winleft = (screen.width - width) / 2;
        var winUp = (screen.height - height) / 2;


        window.open('', windowname, 'top=' + winUp + ',left=' + winleft + ',height='+height+',width='+width+',location=no,resizable=no,scrollbars=no,status=no');
        myform.target = windowname;
        return true;
    }
    </script>

    <script>
        function ValidateForm() {
        <%
               Iterator iter = workgroupForm.getFields();
               while(iter.hasNext()){
                   FormField field = (FormField)iter.next();
                   boolean required = field.isRequired();
                   String variable = field.getVariable();
                   if(required && !"email".equals(variable)){

        %>
            if ((! Jtrim(document.f.<%=variable%>.value).length)) {
                alert("You must specify all required fields.");
                document.f.<%= variable%>.focus();
                return false;
            }
        <% }else if(required){ %>
            if ((!isValidEmail(document.f.<%=variable%>.value))) {
                alert("A valid email address is required.");
                document.f.<%= variable%>.focus();
                return false;
            }
        <% }} %>
            document.f.submitForm.disabled = true;
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
            if(!Jtrim(str).length){
              return false;
            }
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
        <title>Information </title>

        <link rel="stylesheet"
              type="text/css"
              href="style.jsp?workgroup=<%= workgroup %>"/><script language="JavaScript" type="text/javascript" src="common.js">//Ignore</script>
  </head>
  <body style="margin-top:0px; margin-bottom:20px; margin-right:20px;margin-left:20px" id="defaultwindow">
    <table width="100%" cellpadding="3" cellspacing="2">
    <tr><td colspan="2" height="1%">
    <img src="getimage?image=logo&workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>"/>
        </td>
    </tr>
      <form name="f" id="f" action="queue.jsp" method="post" onSubmit="return popup(this);">
       <!-- Identify all hidden variables. All variables will be passed to the metadata router.
            You can do any name-value pairing you like. Such as product=Jive Live Assistant. Such
            data can be used to effectivly route to a particular queue within a workgroup.
       -->
        <input type="hidden" name="workgroup" value="<%= StringUtils.escapeHTMLTags(workgroup) %>"/>
        <input type="hidden" name="chatID" value="<%= StringUtils.escapeHTMLTags(chatID) %>" />
       <!-- End of Hidden Variable identifiers -->
          <tr>
                <td colspan="2" height="1%">
                <br/><%=  welcomeText %>
                </td>
            </tr>
            <tr>
              <td height="1%">
                <br/>
              </td>
            </tr>

            <% if (authFailed) { %>
            <tr valign="top">
               <td class="formtext" colspan="2" height="1%" nowrap><span class="error">Authentication Failed</span></td>
            </tr>
            <% } %>

            <%
                       Iterator fields = workgroupForm.getFields();
                       while(fields.hasNext()){
                           hasElements = true;
                           FormField field = (FormField)fields.next();
                           String label = field.getLabel();
                           boolean required = field.isRequired();
                           String requiredStr = required ? "&nbsp;<span class=\"error\">*</span>" : "";
                           if(!field.getType().equals(FormField.TYPE_HIDDEN)){
                   %>
                   <tr valign="top">
                     <td class="formtext" height="1%" width="1%" nowrap><%= label%><%= requiredStr%></td><td><%= FormUtils.createAnswers(field, request)%></td>
                   </tr>
                   <% } } %>

            <tr valign="top">
              <td height="1%">
              <!-- All workgroup defined variables -->
                <%
                       fields = workgroupForm.getFields();
                       while(fields.hasNext()){
                           FormField field = (FormField)fields.next();
                           if(field.getType().equals(FormField.TYPE_HIDDEN)){
                 %>
                 <%= FormUtils.createDynamicField(field, request)%>
                 <% }} %>
              <!-- End of Defined Variables -->

                 <% // Handle page location
                     if(location != null){ %>
                     <input type="hidden" name="location" value="<%= StringUtils.escapeHTMLTags(location)%>" />
                 <% } %>
              </td>
                <td> <input type="submit" name="submitForm" value="<%= startButton%>"/></td>
            </tr>
            <tr>

            </tr>
           </form>
          </table>
 <div style="position:absolute;bottom:0px;right:5px"><img src="getimage?image=poweredby&workgroup=<%= StringUtils.URLEncode(workgroup, "utf-8") %>"/></div>
  </body>
    <%if(!hasElements){ %>
    <script type="text/javascript">
        document.f.submit();
    </script>
    <%}%>
</html>







