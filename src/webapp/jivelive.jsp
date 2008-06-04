//=====================
// Copyright (c) 2004-2005
// Fastpath
// http://www.jivesoftware.com
//=====================
<%@ include file="common.js" %>

<%
 String urls = request.getRequestURL().toString();
 int indexs = urls.lastIndexOf( "/" );

 if (indexs != -1) {
  urls = urls.substring( 0, indexs );
 }
%>

  function showChatButton(workgroup) {
    var d = new Date();
    var v1 = d.getSeconds() + '' + d.getDay();
    var img = "<%=urls %>/live?action=isAvailable&workgroup=" + workgroup;
    var gotoURL = "<%= urls %>/start.jsp?workgroup=" + workgroup + "&location=" + window.location.href;
    document.write(
        "<a href=\"#\" onclick=\"launchWin(\'framemain\','"+gotoURL+"',500, 400);return false;\"><img border=\"0\" src=\""+img+"\"></a>");
  }

 function displayWorkgroup(workgroup,online,offline) {
    var d = new Date();
    var v1 = d.getSeconds() + '' + d.getDay();
    var img = "<%=urls %>/live?action=isAvailable&workgroup=" + workgroup +"&online="+ online + "&offline="+offline;
    var gotoURL = "<%= urls %>/start.jsp?workgroup=" + workgroup + "&location=" + window.location.href;
    document.write(
        "<a href=\"#\" onclick=\"launchWin(\'framemain\','"+gotoURL+"',500, 400);return false;\"><img border=\"0\" src=\""+img+"\"></a>");
  }


  function showChatButtonWithAgent(workgroup, agent) {
    var d = new Date();
    var v1 = d.getSeconds() + '' + d.getDay();
    var img = "<%=urls %>/live?action=isAvailable&workgroup=" + workgroup;
    var gotoURL = "<%= urls %>/start.jsp?workgroup=" + workgroup + "&agent=" + agent + "&location=" + window.location.href;
    document.write("<a href=\"#\" onclick=\"launchWin(\'framemain\','"+gotoURL+"',500, 400);return false;\"><img border=\"0\" src=\""+img+"\"></a>");
  }

  function showButtonWithoutUI(workgroup, params){
    var d = new Date();
    var v1 = d.getSeconds() + '' + d.getDay();
    var img = "<%=urls %>/live?action=isAvailable&workgroup=" + workgroup;

    var gotoURL = "<%= urls %>/start.jsp?workgroup=" + workgroup + "&location=" + window.location.href + "&noUI=true&"+params;
    document.write("<a href=\"#\" onclick=\"launchWin(\'framemain\','"+gotoURL+"',500, 400);return false;\"><img border=\"0\" src=\""+img+"\"></a>");
  }















