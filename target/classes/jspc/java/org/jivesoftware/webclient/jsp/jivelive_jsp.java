package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class jivelive_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/common.js");
  }

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
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("//=====================\r\n// Copyright (c) 2004-2005\r\n// Fastpath\r\n// http://www.jivesoftware.com\r\n//=====================\r\n");
      out.write("/*\r\n * $RCSfile$\r\n * $Revision: 19342 $\r\n * $Date: 2005-07-20 09:30:31 -0700 (Wed, 20 Jul 2005) $\r\n *\r\n * Copyright (C) 2003-2008 Jive Software. All rights reserved.\r\n *\r\n * This software is the proprietary information of Jive Software. Use is subject to license terms.\r\n */\r\n\r\n// Override the default trim method of String.\r\nString.prototype.trim = function() {\r\n\r\n  var text = this;\r\n  var start = 0;\r\n  var end = text.length;\r\n  var display = \"\";\r\n  for (var i = 0; i < text.length; i++) {\r\n    display += text.charCodeAt(i) + \" \";\r\n  }\r\n  for (var i = 0; i < text.length; i++) {\r\n    var code = text.charCodeAt(i);\r\n\r\n    if (code >= 33) {\r\n      start = i;\r\n      break;\r\n    }\r\n    else{\r\n      start++;\r\n    }\r\n  }\r\n  for (var i = text.length; i > start; i--) {\r\n    var code = text.charCodeAt(i - 1);\r\n\r\n    if (code >= 33) {\r\n      end = i;\r\n      break;\r\n    }\r\n  }\r\n\r\n  return text.substring(start, end);\r\n}\r\n// variable to tell whether or not a form has been clicked\r\nvar clicked = false;\r\n\r\nfunction allowClick() {\r\n");
      out.write("  if (! clicked) {\r\n    clicked = true;\r\n    return true;\r\n  }\r\n  return false;\r\n}\r\n// Sequence for open window names\r\nvar windowNameSeq = 0;\r\n\r\n// Array of all open windows\r\nvar windows = new Array();\r\n\r\n// Checks to see if a window exists\r\nfunction windowExists(name) {\r\n\r\n  for (var i = 0; i < windows.length; i++) {\r\n\r\n    // IE needs a try/catch here for to avoid an access violation on windows[i].name\r\n    // in some cases.\r\n    try {\r\n      if (windows[i].name == name) {\r\n        return true;\r\n      }\r\n    }\r\n    catch (exception) {\r\n    }\r\n  }\r\n\r\n  return false;\r\n}\r\n// Returns the window object - returns nothing if not found.\r\nfunction getWindow(name) {\r\n\r\n  for (var i = 0; i < windows.length; i++) {\r\n    try {\r\n      if (windows[i].name == name) {\r\n        return windows[i];\r\n      }\r\n    }\r\n    catch (exception) {\r\n    }\r\n  }\r\n}\r\n\r\nfunction removeWindow(name) {\r\n\r\n  for (var i = 0; i < windows.length; i++) {\r\n    try {\r\n      if (windows[i].name == name) {\r\n        windows.splice(i, 1);\r\n        return;\r\n");
      out.write("      }\r\n    }\r\n    catch (exception) {\r\n    }\r\n  }\r\n}\r\n// Open a window given its unique name, url, width and height.\r\nfunction pushWin(name, url, width, height) {\r\n\r\n  var defaultOptions = \"location=yes,status=yes,toolbar=no,personalbar=no,menubar=no,directories=no,\";\r\n  defaultOptions += \"scrollbars=yes,resizable=yes,\";\r\n  defaultOptions += \"width=\" + width + \",height=\" + height;\r\n  launchWinWithOptions(name, url, defaultOptions);\r\n}\r\n// Open a window given its unique name, url, width and height.\r\nfunction launchWin(name, url, width, height) {\r\n\r\n  var defaultOptions = \"location=no,status=no,toolbar=no,personalbar=no,menubar=no,directories=no,\";\r\n  var winleft = (screen.width - width) / 2;\r\n  var winUp = (screen.height - height) / 2;\r\n\r\n  defaultOptions += \"scrollbars=no,resizable=yes,top=\" + winUp + \",left=\" + winleft + \",\";\r\n  defaultOptions += \"width=\" + width + \",height=\" + height;\r\n  launchWinWithOptions(name, url, defaultOptions);\r\n}\r\n// Open a window with given name, url, and options list\r\nfunction launchWinWithOptions(name, url, options) {\r\n");
      out.write("\r\n  if (! windowExists(name)) {\r\n    var winVar = window.open(url, name, options);\r\n    windows[windows.length] = winVar;\r\n    return winVar;\r\n  }\r\n  else{\r\n    var theWin = getWindow(name);\r\n    theWin.focus();\r\n  }\r\n}\r\n\r\nfunction getTopLevelWindow() {\r\n  var win = window;\r\n\r\n  if (win.parent == win) {\r\n    return win;\r\n  }\r\n\r\n  while (win.parent != win) {\r\n    win = window.parent.window;\r\n  }\r\n  return win;\r\n}\r\n// Close the current window object\r\nfunction closeWin(win) {\r\n  win.close();\r\n}\r\n// Handle closing of the current window\r\nfunction handleClose(message) {\r\n\r\n  if (confirm(message)) {\r\n    removeWindow(getTopLevelWindow().name);\r\n    closeWin(getTopLevelWindow());\r\n    return true;\r\n  }\r\n  else{\r\n    return false;\r\n  }\r\n}\r\n// Handle closing of the current window\r\nfunction confirmCancel(message) {\r\n\r\n  if (confirm(message)) {\r\n    getTopLevelWindow().location.href = 'userinfo.jsp';\r\n    return true;\r\n  }\r\n  else{\r\n    return false;\r\n  }\r\n}\r\n\r\nfunction cancelQueue(workgroup, chatID){\r\n    getTopLevelWindow().location.href = 'userinfo.jsp?workgroup=' + workgroup +'&chatID='+chatID;\r\n");
      out.write("    return true;\r\n}\r\n\r\nfunction confirmCancelAndClose(message) {\r\n\r\n  if (confirm(message)) {\r\n    getTopLevelWindow().location.href = 'userinfo.jsp';\r\n    getTopLevelWindow().close();\r\n    return true;\r\n  }\r\n  else{\r\n    return false;\r\n  }\r\n}\r\n// Handle closing of the current window\r\nfunction confirmCancel(message, workgroup, chatID) {\r\n\r\n  if (confirm(message)) {\r\n    getTopLevelWindow().location.href = 'userinfo.jsp?workgroup=' + workgroup +'&chatID='+chatID;\r\n    return true;\r\n  }\r\n  else{\r\n    return false;\r\n  }\r\n}\r\n\r\nfunction closeAll() {\r\n  removeWindow(getTopLevelWindow().name);\r\n  closeWin(getTopLevelWindow());\r\n}\r\n// Opens the help window:\r\nfunction launchHelpWin() {\r\n  var win = launchWin('helpwin', 'helpwin.jsp', 550, 350);\r\n}\r\n// Hide a DIV\r\nfunction hide(divId) {\r\n\r\n  if (document.layers) {\r\n    document.layers[divId].visibility = 'hide';\r\n  }\r\n  else if (document.all) {\r\n    document.all[divId].style.visibility = 'hidden';\r\n  }\r\n  else if (document.getElementById) {\r\n    document.getElementById(divId).style.visibility = 'hidden';\r\n");
      out.write("  }\r\n}\r\n// Show a DIV\r\nfunction show(divId) {\r\n\r\n  if (document.layers) {\r\n    document.layers[divId].visibility = 'show';\r\n  }\r\n  else if (document.all) {\r\n    document.all[divId].style.visibility = 'visible';\r\n  }\r\n  else if (document.getElementById) {\r\n    document.getElementById(divId).style.visibility = 'visible';\r\n  }\r\n}\r\n\r\nfunction getDiv(divID) {\r\n\r\n  if (document.layers) {\r\n    return document.layers[divID];\r\n  }\r\n  else if (document.all) {\r\n    return document.all[divID];\r\n  }\r\n  else if (document.getElementById) {\r\n    return document.getElementById(divID);\r\n  }\r\n}\r\n\r\nfunction getDivByDoc(divID, doc) {\r\n\r\n  if (doc.layers) {\r\n    return doc.layers[divID];\r\n  }\r\n  else if (doc.all) {\r\n    return doc.all[divID];\r\n  }\r\n  else if (doc.getElementById) {\r\n    return doc.getElementById(divID);\r\n  }\r\n}\r\n// TODO\r\nfunction showTypingIndicator(flag) {\r\n\r\n  if (flag) {\r\n\r\n  // put the text in the div\r\n  }\r\n  else{\r\n\r\n  // blank out the div\r\n  }\r\n}\r\n\r\nfunction informConnectionClosed() {\r\n  alert('Your support session has ended, you will be redirected to the transcript page.');\r\n");
      out.write("  parent.location.href = 'transcriptmain.jsp';\r\n}\r\n\r\nfunction addChatText(yakWin, from, text) {\r\n\r\n  // The div to write to:\r\n  var yakDiv = yakWin.document.getElementById('ytext');\r\n\r\n  // This will be an announcement if there is no from passed in\r\n  var isAnnouncement = (from == \"\");\r\n\r\n  // Create a new span node in the yakDiv. Record the num of nodes right now - used later\r\n  // to see if the node was really added:\r\n  var numChildren = yakDiv.childNodes.length;\r\n  var nameSpan = document.createElement(\"span\");\r\n  var textSpan = document.createElement(\"span\");\r\n  if (isAnnouncement) {\r\n    nameSpan.setAttribute(\"class\", \"chat-announcement\");\r\n    textSpan.setAttribute(\"class\", \"chat-announcement\");\r\n  }\r\n  else{\r\n    textSpan.setAttribute(\"class\", \"text\");\r\n  }\r\n  // add another span containing the username if this is not an announcement:\r\n  var fromIsCurrentUser = false;\r\n  if (! isAnnouncement) {\r\n\r\n    // is the from the same as the current user?\r\n    fromIsCurrentUser = (nickname == from);\r\n\r\n    if (fromIsCurrentUser) {\r\n");
      out.write("      nameSpan.setAttribute(\"class\", \"client-name\");\r\n    }\r\n    else{\r\n      nameSpan.setAttribute(\"class\", \"operator-name\");\r\n    }\r\n  }\r\n  var chatLineDiv = document.createElement(\"div\");\r\n  chatLineDiv.setAttribute(\"class\", \"chat-line\");\r\n\r\n  var appendFailed = false;\r\n  try {\r\n    if (! isAnnouncement) {\r\n      nameSpan.innerHTML = from + \": \";\r\n      chatLineDiv.appendChild(nameSpan);\r\n    }\r\n    textSpan.innerHTML = text;\r\n    chatLineDiv.appendChild(textSpan);\r\n\r\n    yakDiv.appendChild(chatLineDiv);\r\n  }\r\n  catch (exception) {\r\n    appendFailed = true;\r\n  }\r\n  if (! appendFailed) {\r\n\r\n    // Make sure the browser appended:\r\n    appendFailed = (numChildren == yakDiv.childNodes.length);\r\n  }\r\n\r\n  if (appendFailed) {\r\n    var inn = yakDiv.innerHTML;\r\n    inn += \"<div class=\\\"chat-line\\\">\";\r\n    if (! isAnnouncement) {\r\n      inn += \"<span class=\\\"\";\r\n      if (isAnnouncement) {\r\n        inn += \"chat-announcement\";\r\n      }\r\n      else if (fromIsCurrentUser) {\r\n        inn += \"client-name\";\r\n      }\r\n      else{\r\n");
      out.write("        inn += \"operator-name\";\r\n      }\r\n\r\n      inn += \"\\\">\" + from + \": </span>\";\r\n\r\n    // yakDiv.innerHTML = inn;\r\n    }\r\n    // var inn = yakDiv.innerHTML;\r\n\r\n    inn += \"<span class=\\\"\";\r\n    inn += (isAnnouncement ? \"chat-announcement\\\">\" : \"chat_text\\\">\");\r\n    inn += text + \"</span></div>\";\r\n\r\n    yakDiv.innerHTML = inn;\r\n  }\r\n  else{\r\n\r\n  // yakDiv.appendChild(document.createElement(\"br\"));\r\n  }\r\n}\r\n\r\nfunction scrollYakToEnd(yakWin) {\r\n\r\n  var endDiv = yakWin.document.getElementById('enddiv');\r\n  yakWin.scrollTo(0, endDiv.offsetTop);\r\n}\r\n");
      out.write("\r\n\r\n");

 String urls = request.getRequestURL().toString();
 int indexs = urls.lastIndexOf( "/" );

 if (indexs != -1) {
  urls = urls.substring( 0, indexs );
 }

      out.write("\r\n\r\n  function showChatButton(workgroup) {\r\n    var d = new Date();\r\n    var v1 = d.getSeconds() + '' + d.getDay();\r\n    var img = \"");
      out.print(urls );
      out.write("/live?action=isAvailable&workgroup=\" + workgroup;\r\n    var gotoURL = \"");
      out.print( urls );
      out.write("/start.jsp?workgroup=\" + workgroup + \"&location=\" + window.location.href;\r\n    document.write(\r\n        \"<a href=\\\"#\\\" onclick=\\\"launchWin(\\'framemain\\','\"+gotoURL+\"',500, 400);return false;\\\"><img border=\\\"0\\\" src=\\\"\"+img+\"\\\"></a>\");\r\n  }\r\n\r\n function displayWorkgroup(workgroup,online,offline) {\r\n    var d = new Date();\r\n    var v1 = d.getSeconds() + '' + d.getDay();\r\n    var img = \"");
      out.print(urls );
      out.write("/live?action=isAvailable&workgroup=\" + workgroup +\"&online=\"+ online + \"&offline=\"+offline;\r\n    var gotoURL = \"");
      out.print( urls );
      out.write("/start.jsp?workgroup=\" + workgroup + \"&location=\" + window.location.href;\r\n    document.write(\r\n        \"<a href=\\\"#\\\" onclick=\\\"launchWin(\\'framemain\\','\"+gotoURL+\"',500, 400);return false;\\\"><img border=\\\"0\\\" src=\\\"\"+img+\"\\\"></a>\");\r\n  }\r\n\r\n\r\n  function showChatButtonWithAgent(workgroup, agent) {\r\n    var d = new Date();\r\n    var v1 = d.getSeconds() + '' + d.getDay();\r\n    var img = \"");
      out.print(urls );
      out.write("/live?action=isAvailable&workgroup=\" + workgroup;\r\n    var gotoURL = \"");
      out.print( urls );
      out.write("/start.jsp?workgroup=\" + workgroup + \"&agent=\" + agent + \"&location=\" + window.location.href;\r\n    document.write(\"<a href=\\\"#\\\" onclick=\\\"launchWin(\\'framemain\\','\"+gotoURL+\"',500, 400);return false;\\\"><img border=\\\"0\\\" src=\\\"\"+img+\"\\\"></a>\");\r\n  }\r\n\r\n  function showButtonWithoutUI(workgroup, params){\r\n    var d = new Date();\r\n    var v1 = d.getSeconds() + '' + d.getDay();\r\n    var img = \"");
      out.print(urls );
      out.write("/live?action=isAvailable&workgroup=\" + workgroup;\r\n\r\n    var gotoURL = \"");
      out.print( urls );
      out.write("/start.jsp?workgroup=\" + workgroup + \"&location=\" + window.location.href + \"&noUI=true&\"+params;\r\n    document.write(\"<a href=\\\"#\\\" onclick=\\\"launchWin(\\'framemain\\','\"+gotoURL+\"',500, 400);return false;\\\"><img border=\\\"0\\\" src=\\\"\"+img+\"\\\"></a>\");\r\n  }\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n");
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
