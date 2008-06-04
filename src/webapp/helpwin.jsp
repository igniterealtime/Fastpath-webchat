<%--
  - $RCSfile$
  - $Revision: 18400 $
  - $Date: 2005-02-05 00:40:39 -0800 (Sat, 05 Feb 2005) $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%@ page errorPage = "fatal.jsp"%>

<%@ include file = "include/global.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
 <html>
  <head>
   <title><%= brandingTitle %> - Help</title>

   <link rel="stylesheet" type="text/css" href="style.jsp"/>

   <script language="JavaScript" type="text/javascript" src="common.js"></script>
  </head>

  <body id="helpwindow">
   <div id="helpwindow-header">
    <p>
     <%= brandingTitle %> - Help</p>
   </div>

   <div id="helpwindow-content">
    <iframe name="yak" src="help/content.html" frameborder="0" scrolling="auto" height="100%" width="100%">
    </iframe>
   </div>

   <div id="closebutton">
    <a href="#" onclick="handleClose('Are you sure you want to close this window?');return false;"
       title="Click to close this window.">

    <img src="images/blank.gif" width="100%" height="100%" border="0">

    </a>
   </div>
  </body>
 </html>
