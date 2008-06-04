<%@ page import="org.jivesoftware.webchat.util.ModelUtil"%>
 <%--
  - $RCSfile$
  - $Revision$
  - $Date: 2005-12-13 11:52:27 -0800 (Tue, 13 Dec 2005) $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%@ include file="include/global.jsp" %>

<%  response.setContentType("text/css"); %>

<%
    String workgroup = request.getParameter("workgroup");
    if(!ModelUtil.hasLength(workgroup)){
      workgroup = (String)session.getAttribute("workgroup");
    }
%>

/* Global fonts and colors */

BODY {
    background : #fff;
    color : #000;
    font: 11px verdana, arial, helvetica, sans-serif;
    margin: 0;
    padding: 0;
    border-width: 0;
}

* {
    font-size: 1em;
}

PRE, CODE, TT {
    font: 100% "Courier New", Courier, "Andale Mono", monospace;
}

/* Error styles */

.error-text {
    color : #f00;
}

/* Styles for main chat window DIVs */

#mainwindow {
    background-image: url(getimage?image=chatwindow&workgroup=<%= workgroup %>);
    background-repeat : no-repeat;
}

#inputdialog {
  background-image: url(getimage?image=infopage&workgroup=<%= workgroup %>);
  background-repeat: no-repeat;
}

#transcriptdialog {
  background-image: url(images/transcript_window.gif);
  background-repeat: no-repeat;
}

#queuedialog {
  background-image: url(getimage?image=queue&workgroup=<%= workgroup %>);
  background-repeat: no-repeat;
}

#messagesentdialog {
  background-image: url(images/default_window.gif);
  background-repeat: no-repeat;
}

#defaultwindow {
  background-image: url(getimage?image=main&workgroup=<%= workgroup %>);
}

#conversation {
    width : 398px;
    height : 228px;
	position : absolute;
	top : 11;
	left : 11;
}

#inputbuttons {
    width : 400px;
    height : 25px;
    position : absolute;
    top : 245px;
    left : 10px;
    text-align : center;
}

#inputbuttons FORM, #inputbuttons DIV {
    display : inline;
}

#inputbuttons DIV {
    margin : 1px 2px 1px 2px;
    padding : 0px;
}

#inputbuttons #filebutton {
    margin-left : 10px;
}

#inputbox {
    width : 310px;
    height : 60px;
    position : absolute;
    left : 10;
    top : 280;
}

#questionbox {
    width : 300px;
    height : 60px;
    position : absolute;
    left : 10;
    top : 280;
}

#questionbox TEXTAREA {
   width : 100%;
   height : 50px;
   margin : 0px;
}

#questionbox SELECT {
   width : 100%;
   height : 50px;
   margin : 0px;
}

#questionbox FORM {
  display:inline;
}




#inputbox TEXTAREA {
    width : 100%;
    height : 60px;
    margin : 0px;
}
#inputbox FORM {
    display : inline;
}

#sendbutton {
    width : 100px;
    height : 30px;
    position : absolute;
    top : 295px;
    left : 430px;
}

#infobox {
    width : 120px;
    height : 230px;
    position : absolute;
    top : 40px;
    left : 420px;
}

#closebutton {
    background-image: url(images/end_button.gif);
    background-repeat : no-repeat;
    position : absolute;
}

#helpbutton {
    background-image: url(images/helpicon-<%= branding %>.gif);
    background-repeat : no-repeat;
    position : absolute;
    width : 16;
    height : 16;
    top : 15;
    left : 460px;
}

#printbutton {
    background-image: url(images/printicon-<%= branding %>.gif);
    background-repeat : no-repeat;
    position : absolute;
    width : 16;
    height : 16;
    top : 15;
    left : 460px;
}

#userinfowindow #closebutton, #queuewindow #closebutton, #helpwindow #closebutton,
        #transcriptwindow #closebutton, #standardwindow #closebutton
{
    top : 15px;
    left : 519px;
}

#transcriptprintwindow #printbutton {
    top : 15px;
    left : 475px;
}

#transcriptprintwindow #closebutton {
    top : 15px;
    left : 500px;
}

#userinfowindow #helpbutton, #queuewindow #helpbutton, #transcriptwindow #helpbutton,
        #standardwindow #helpbutton
{
    top : 15px;
    left : 495px;
}

/* Styles for the text of the conversation */

#conversation-text {
    padding : 5px;
}
.chat-line {
    padding : 0px 0px 4px 0px;
}
.operator-name {
    font-weight : bold;
    color : #00f;
}
.client-name {
    font-weight : bold;
    color : #f00;
}
.chat-announcement {
    font-style : italic;
    color : #060;
}

/* Styles for the other smaller popups and popup pages */

#fileuploadbox {
    visibility : hidden;
    width : 400px;
    height : 250px;
    position : absolute;
    left : 50;
    top : 50;
    border : 2px #ccc groove;
}



#fileuploadbox, #userinfowindow, #queuewindow, #endsessionwindow, #fatalerrorwindow, #helpwindow,
        #transcriptwindow, #transcriptprintwindow, #standardwindow
{
    background-image: url(images/branding-helperwin-<%= branding %>.gif);
    background-repeat : repeat-x;
}

#upload-header, #userinfo-header, #queue-header, #endsession-header, #fatalerrorwindow-header,
        #helpwindow-header, #transcriptwindow-header, #transcriptprintwindow-header
        #standard-header
{
    width : 550px;
    height : 50px;
    position : absolute;
    top : 0px;
    left : 0px;
}

#upload-header P, #userinfo-header P, #queue-header P, #endsession-header P, #fatalerrorwindow P,
        #helpwindow-header P, #transcriptwindow-header P, #transcriptprintwindow-header P,
        #standard-header P
{
    font-weight : bold;
    margin : 0px;
    padding : 16px 16px 8px 16px;
}

#upload-content {
    width : 400px;
    position : absolute;
    top : 50px;
    left : 0px;
}

#userinfo-content, #queue-content, #endsession-content, #fatalerrorwindow-content,
        #helpwindow-content, #transcriptwindow-content, #transcriptprintwindow-content
        #standard-content
{
    width : 550px;
    position : absolute;
    top : 50px;
    left : 0px;
}

#helpwindow-content, #transcriptwindow-content, #transcriptprintwindow-content {
    height : 300px;
}

#upload-content .box, #userinfo-content .box, #queue-content .box, #endsession-content .box,
        #fatalerrorwindow-content .box, #transcriptwindow-content .box,
        #transcriptprintwindow-content .box,
        #standard-content .box
{
    padding : 16px 16px 5px 16px;
}

.inputField {
  font-weight: normal;
  font-size: 8pt;
  font-family: Verdana;
  border:solid #FFFFFF 2px;
  border-style:none;
  border-width:0px;
  border-color:white;
}
.queue_info_text {font-family: Arial, Helvetica, sans-serif; font-size: 11px; margin-top: 6; color: white;}
.queue_info_small_text {font-family: Arial, Helvetica, sans-serif; font-size: 11px; margin-top: 2; color: white;}

.testField
{
font-family: Verdana, Arial, Helvetica, sans-serif;
font-size: 10px;
color: #000000;
border-top-width: 1px;
border-right-width: 1px;
border-bottom-width: 1px;
border-left-width: 1px;
border-top-style: solid;
border-right-style: solid;
border-bottom-style: solid;
border-left-style: solid;
border-top-color: white;
border-right-color: white;
border-bottom-color: white;
border-left-color: white;
}

#userinfo-content INPUT {
    margin : 1px;
}

/* Styles for the help page */

#helpcontent {
    padding : 15px;
}

/* FOLLOWING FOR DEBUGGING ONLY */

/* Borders on all DIVs */
/*
DIV, P {
    border : 1px #ccc solid;
}
*/

/* User Defined Variable */
.info_text {font-family: Arial, Helvetica, sans-serif; font-size: 11px; margin-top: 6; color: black;}
.info_small_text {font-family: Arial, Helvetica, sans-serif; font-size: 11px; margin-top: 2; color: black;}

.header {font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; color: black;}
.input_title {font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold; color: black;}

.box { BORDER: #cccccc 1px solid; }
.boxLayout {
	BORDER-RIGHT: #003399 1px solid; PADDING-RIGHT: 0px; BORDER-TOP: #003399 1px solid; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; BORDER-LEFT: #003399 1px solid; PADDING-TOP: 0px; BORDER-BOTTOM: #003399 1px solid
}
boxLayoutGrey {
	BORDER-RIGHT: #cccccc 1px solid; PADDING-RIGHT: 0px; BORDER-TOP:#cccccc 1px solid; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; BORDER-LEFT: #cccccc 1px solid; PADDING-TOP: 0px; BORDER-BOTTOM: #cccccc 1px solid
}

.error_text {font-family: Arial, Helvetica, sans-serif; font-size: 9px; color : #f00; }
.error {
	FONT-WEIGHT: bold; FONT-SIZE: 10px; COLOR: #cc0000; LINE-HEIGHT: 16px; FONT-FAMILY: helvetica,arial,sans-serif,verdana; TEXT-ALIGN: justify; TEXT-DECORATION: none
}
.descriptive-live {
	font-family: Arial;
	color: #336699;
	font-size: 10pt;
	font-weight: bold;
	text-decoration: underline;
}

.content {  font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt; color: #000000}
.nicetext {  font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt; color: #000000}
.nicetextsmall {  font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; color: #000000}
.subhead { font-family: Arial, Helvetica, sans-serif; font-size: 12pt; color: #000066}
.subtitle{
		font-size: 14px;
        font-family:Verdana, Arial, Helvetica, sans-serif;
        color: #000000;
		margin-bottom:10px;
		font-weight:bold;
}

.highlight {
	color: #990000;
}

.formtitle {  font-family: Arial, Helvetica, sans-serif; font-size: 20px; color: #FB7015; font-weight: bolder; font-style: italic; letter-spacing: -1px}
.otherbox {  font-family: Arial, Helvetica, sans-serif; font-size: 12px; height: 18px; width: 205px; background-color: #EFEFE7; padding-left: 3px; color: #333333; border-top-width: 0px; border-right-width: 0px; border-bottom-width: 0px; border-left-width: 0px; background-image: url(../img/otherbox.gif)}
.formtext {  font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: normal; color: #224162}
.text {  font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #333333}

