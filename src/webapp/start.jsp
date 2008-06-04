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

<%@ page import="java.util.Enumeration,
                 org.jivesoftware.webchat.util.ModelUtil,
                 org.jivesoftware.webchat.util.ParamUtils,
                 org.jivesoftware.webchat.actions.WorkgroupStatus,
                 org.jivesoftware.webchat.util.StringUtils" errorPage="fatal.jsp" %>

<%
    String workgroup = ParamUtils.getParameter(request, "workgroup");

    // Get Agent and add to session
    String agent = ParamUtils.getParameter(request, "agent");

    // Get Page Location and add to session.
    String pageLocation = ParamUtils.getParameter(request, "location");

    // Does the user want to go directly to the Queue?
    boolean noUI = ParamUtils.getBooleanParameter(request, "noUI");

    StringBuffer paramString = new StringBuffer();

    // Generate a chatID. This id will be used to identify the user throughout the entire
    // chat process.
    String chatID = StringUtils.randomString(10);
    paramString.append("chatID="+chatID);

    if(ModelUtil.hasLength(workgroup)){
        paramString.append("&workgroup="+workgroup);
    }

    if(ModelUtil.hasLength(agent)){
        paramString.append("&agent="+agent);
    }

    if(ModelUtil.hasLength(pageLocation)){
        session.setAttribute("pageLocation", pageLocation);
    }

    if (WorkgroupStatus.isOnline(workgroup)) {
        if (noUI) {
            Enumeration requestEnum = request.getParameterNames();
            while (requestEnum.hasMoreElements()) {
                String name = (String) requestEnum.nextElement();
                String value = request.getParameter(name);
                if(name.equalsIgnoreCase("username")){
                    name = "username";
                }
                if(name.equalsIgnoreCase("email")){
                    name = "email";
                }
                paramString.append("&" + name + "=" + value);
            }

            String dest = paramString.toString();

            if(!ModelUtil.hasLength(workgroup)){
                out.println("A workgroup must be specified.");
                return;
            }

            String name = request.getParameter("username");
            if(!ModelUtil.hasLength(name)){
                out.println("A username must be specified.");
                return;
            }

            response.sendRedirect("queue.jsp?"+dest);
        }
        else{
            response.sendRedirect("userinfo.jsp?"+paramString.toString());
        }
    }
    else{
        response.sendRedirect("email/leave-a-message.jsp?"+paramString.toString());
    }
%>
