/*
 * $RCSfile$
 * $Revision: 19342 $
 * $Date: 2005-07-20 09:30:31 -0700 (Wed, 20 Jul 2005) $
 *
 * Copyright (C) 2003-2008 Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */

// Override the default trim method of String.
String.prototype.trim = function() {

  var text = this;
  var start = 0;
  var end = text.length;
  var display = "";
  for (var i = 0; i < text.length; i++) {
    display += text.charCodeAt(i) + " ";
  }
  for (var i = 0; i < text.length; i++) {
    var code = text.charCodeAt(i);

    if (code >= 33) {
      start = i;
      break;
    }
    else{
      start++;
    }
  }
  for (var i = text.length; i > start; i--) {
    var code = text.charCodeAt(i - 1);

    if (code >= 33) {
      end = i;
      break;
    }
  }

  return text.substring(start, end);
}
// variable to tell whether or not a form has been clicked
var clicked = false;

function allowClick() {
  if (! clicked) {
    clicked = true;
    return true;
  }
  return false;
}
// Sequence for open window names
var windowNameSeq = 0;

// Array of all open windows
var windows = new Array();

// Checks to see if a window exists
function windowExists(name) {

  for (var i = 0; i < windows.length; i++) {

    // IE needs a try/catch here for to avoid an access violation on windows[i].name
    // in some cases.
    try {
      if (windows[i].name == name) {
        return true;
      }
    }
    catch (exception) {
    }
  }

  return false;
}
// Returns the window object - returns nothing if not found.
function getWindow(name) {

  for (var i = 0; i < windows.length; i++) {
    try {
      if (windows[i].name == name) {
        return windows[i];
      }
    }
    catch (exception) {
    }
  }
}

function removeWindow(name) {

  for (var i = 0; i < windows.length; i++) {
    try {
      if (windows[i].name == name) {
        windows.splice(i, 1);
        return;
      }
    }
    catch (exception) {
    }
  }
}
// Open a window given its unique name, url, width and height.
function pushWin(name, url, width, height) {

  var defaultOptions = "location=yes,status=yes,toolbar=no,personalbar=no,menubar=no,directories=no,";
  defaultOptions += "scrollbars=yes,resizable=yes,";
  defaultOptions += "width=" + width + ",height=" + height;
  launchWinWithOptions(name, url, defaultOptions);
}
// Open a window given its unique name, url, width and height.
function launchWin(name, url, width, height) {

  var defaultOptions = "location=no,status=no,toolbar=no,personalbar=no,menubar=no,directories=no,";
  var winleft = (screen.width - width) / 2;
  var winUp = (screen.height - height) / 2;

  defaultOptions += "scrollbars=no,resizable=yes,top=" + winUp + ",left=" + winleft + ",";
  defaultOptions += "width=" + width + ",height=" + height;
  launchWinWithOptions(name, url, defaultOptions);
}
// Open a window with given name, url, and options list
function launchWinWithOptions(name, url, options) {

  if (! windowExists(name)) {
    var winVar = window.open(url, name, options);
    windows[windows.length] = winVar;
    return winVar;
  }
  else{
    var theWin = getWindow(name);
    theWin.focus();
  }
}

function getTopLevelWindow() {
  var win = window;

  if (win.parent == win) {
    return win;
  }

  while (win.parent != win) {
    win = window.parent.window;
  }
  return win;
}
// Close the current window object
function closeWin(win) {
  win.close();
}
// Handle closing of the current window
function handleClose(message) {

  if (confirm(message)) {
    removeWindow(getTopLevelWindow().name);
    closeWin(getTopLevelWindow());
    return true;
  }
  else{
    return false;
  }
}
// Handle closing of the current window
function confirmCancel(message) {

  if (confirm(message)) {
    getTopLevelWindow().location.href = 'userinfo.jsp';
    return true;
  }
  else{
    return false;
  }
}

function cancelQueue(workgroup, chatID){
    getTopLevelWindow().location.href = 'userinfo.jsp?workgroup=' + workgroup +'&chatID='+chatID;
    return true;
}

function confirmCancelAndClose(message) {

  if (confirm(message)) {
    getTopLevelWindow().location.href = 'userinfo.jsp';
    getTopLevelWindow().close();
    return true;
  }
  else{
    return false;
  }
}
// Handle closing of the current window
function confirmCancel(message, workgroup, chatID) {

  if (confirm(message)) {
    getTopLevelWindow().location.href = 'userinfo.jsp?workgroup=' + workgroup +'&chatID='+chatID;
    return true;
  }
  else{
    return false;
  }
}

function closeAll() {
  removeWindow(getTopLevelWindow().name);
  closeWin(getTopLevelWindow());
}
// Opens the help window:
function launchHelpWin() {
  var win = launchWin('helpwin', 'helpwin.jsp', 550, 350);
}
// Hide a DIV
function hide(divId) {

  if (document.layers) {
    document.layers[divId].visibility = 'hide';
  }
  else if (document.all) {
    document.all[divId].style.visibility = 'hidden';
  }
  else if (document.getElementById) {
    document.getElementById(divId).style.visibility = 'hidden';
  }
}
// Show a DIV
function show(divId) {

  if (document.layers) {
    document.layers[divId].visibility = 'show';
  }
  else if (document.all) {
    document.all[divId].style.visibility = 'visible';
  }
  else if (document.getElementById) {
    document.getElementById(divId).style.visibility = 'visible';
  }
}

function getDiv(divID) {

  if (document.layers) {
    return document.layers[divID];
  }
  else if (document.all) {
    return document.all[divID];
  }
  else if (document.getElementById) {
    return document.getElementById(divID);
  }
}

function getDivByDoc(divID, doc) {

  if (doc.layers) {
    return doc.layers[divID];
  }
  else if (doc.all) {
    return doc.all[divID];
  }
  else if (doc.getElementById) {
    return doc.getElementById(divID);
  }
}
// TODO
function showTypingIndicator(flag) {

  if (flag) {

  // put the text in the div
  }
  else{

  // blank out the div
  }
}

function informConnectionClosed() {
  alert('Your support session has ended, you will be redirected to the transcript page.');
  parent.location.href = 'transcriptmain.jsp';
}

function addChatText(yakWin, from, text) {

  // The div to write to:
  var yakDiv = yakWin.document.getElementById('ytext');

  // This will be an announcement if there is no from passed in
  var isAnnouncement = (from == "");

  // Create a new span node in the yakDiv. Record the num of nodes right now - used later
  // to see if the node was really added:
  var numChildren = yakDiv.childNodes.length;
  var nameSpan = document.createElement("span");
  var textSpan = document.createElement("span");
  if (isAnnouncement) {
    nameSpan.setAttribute("class", "chat-announcement");
    textSpan.setAttribute("class", "chat-announcement");
  }
  else{
    textSpan.setAttribute("class", "text");
  }
  // add another span containing the username if this is not an announcement:
  var fromIsCurrentUser = false;
  if (! isAnnouncement) {

    // is the from the same as the current user?
    fromIsCurrentUser = (nickname == from);

    if (fromIsCurrentUser) {
      nameSpan.setAttribute("class", "client-name");
    }
    else{
      nameSpan.setAttribute("class", "operator-name");
    }
  }
  var chatLineDiv = document.createElement("div");
  chatLineDiv.setAttribute("class", "chat-line");

  var appendFailed = false;
  try {
    if (! isAnnouncement) {
      nameSpan.innerHTML = from + ": ";
      chatLineDiv.appendChild(nameSpan);
    }
    textSpan.innerHTML = text;
    chatLineDiv.appendChild(textSpan);

    yakDiv.appendChild(chatLineDiv);
  }
  catch (exception) {
    appendFailed = true;
  }
  if (! appendFailed) {

    // Make sure the browser appended:
    appendFailed = (numChildren == yakDiv.childNodes.length);
  }

  if (appendFailed) {
    var inn = yakDiv.innerHTML;
    inn += "<div class=\"chat-line\">";
    if (! isAnnouncement) {
      inn += "<span class=\"";
      if (isAnnouncement) {
        inn += "chat-announcement";
      }
      else if (fromIsCurrentUser) {
        inn += "client-name";
      }
      else{
        inn += "operator-name";
      }

      inn += "\">" + from + ": </span>";

    // yakDiv.innerHTML = inn;
    }
    // var inn = yakDiv.innerHTML;

    inn += "<span class=\"";
    inn += (isAnnouncement ? "chat-announcement\">" : "chat_text\">");
    inn += text + "</span></div>";

    yakDiv.innerHTML = inn;
  }
  else{

  // yakDiv.appendChild(document.createElement("br"));
  }
}

function scrollYakToEnd(yakWin) {

  var endDiv = yakWin.document.getElementById('enddiv');
  yakWin.scrollTo(0, endDiv.offsetTop);
}
