<%--
  - $RCSfile$
  - $Revision: 18450 $
  - $Date: 2005-02-14 12:16:43 -0800 (Mon, 14 Feb 2005) $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%@ page import = "java.util.*"
                   errorPage = "fatal.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
  <html>
    <head>
      <title>Transcript</title>

      <script language="JavaScript" type="text/javascript" src="common.js"></script>

      <link rel="stylesheet" type="text/css" href="style.jsp"/>
    </head>

    <body id="transcriptprintwindow">
      <div id="transcriptprintwindow-header">
        <p>
          Support Transcript</p>
      </div>

      <div id="transcriptprintwindow-content">
      </div>

      <div id="closebutton">
        <a href="#" onclick="handleClose('Are you sure you want to close this window?');return false;"
           title="Click to close this window."><img src="images/blank.gif" width="100%" height="100%" border="0"></a>
      </div>

      <div id="printbutton">
        <a href="#" onclick="window.print();return false;" title="Click to print the transcript.">
        <img src="images/blank.gif" width="100%" height="100%" border="0">
        </a>
      </div>

      <script language="JavaScript" type="text/javascript">

        // set the innerHTML of the content div to the value
        // of the transcriptsrc page div from the opener
        var transcriptDoc = opener.frames['transcriptsrc'].document;

        var divID = 'transcriptprintwindow-content';
        var srcDiv = getDivByDoc(divID, transcriptDoc);
        var targetDiv = getDiv(divID);
        targetDiv.innerHTML = srcDiv.innerHTML;
      </script>
    </body>
  </html>
