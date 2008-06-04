package org.jivesoftware.webclient.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.jivesoftware.webchat.util.ModelUtil;

public final class style_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  static {
    _jspx_dependants = new java.util.Vector(1);
    _jspx_dependants.add("/include/global.jsp");
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

      out.write('\r');
      out.write('\n');
      out.write(' ');
      out.write("\r\n\r\n");
      out.write("\r\n\r\n");
  // All global vars defined here

    // Get the branding scheme for this app. This will serve as a suffix for all
    // image names.
    String branding = application.getInitParameter( "branding" );
    if (branding == null || "".equals(branding.trim())) {
        branding = "default";
    }
    String brandingTitle = application.getInitParameter("branding-title");

      out.write("\r\n\r\n");
  response.setContentType("text/css"); 
      out.write("\r\n\r\n");

    String workgroup = request.getParameter("workgroup");
    if(!ModelUtil.hasLength(workgroup)){
      workgroup = (String)session.getAttribute("workgroup");
    }

      out.write("\r\n\r\n/* Global fonts and colors */\r\n\r\nBODY {\r\n    background : #fff;\r\n    color : #000;\r\n    font: 11px verdana, arial, helvetica, sans-serif;\r\n    margin: 0;\r\n    padding: 0;\r\n    border-width: 0;\r\n}\r\n\r\n* {\r\n    font-size: 1em;\r\n}\r\n\r\nPRE, CODE, TT {\r\n    font: 100% \"Courier New\", Courier, \"Andale Mono\", monospace;\r\n}\r\n\r\n/* Error styles */\r\n\r\n.error-text {\r\n    color : #f00;\r\n}\r\n\r\n/* Styles for main chat window DIVs */\r\n\r\n#mainwindow {\r\n    background-image: url(getimage?image=chatwindow&workgroup=");
      out.print( workgroup );
      out.write(");\r\n    background-repeat : no-repeat;\r\n}\r\n\r\n#inputdialog {\r\n  background-image: url(getimage?image=infopage&workgroup=");
      out.print( workgroup );
      out.write(");\r\n  background-repeat: no-repeat;\r\n}\r\n\r\n#transcriptdialog {\r\n  background-image: url(images/transcript_window.gif);\r\n  background-repeat: no-repeat;\r\n}\r\n\r\n#queuedialog {\r\n  background-image: url(getimage?image=queue&workgroup=");
      out.print( workgroup );
      out.write(");\r\n  background-repeat: no-repeat;\r\n}\r\n\r\n#messagesentdialog {\r\n  background-image: url(images/default_window.gif);\r\n  background-repeat: no-repeat;\r\n}\r\n\r\n#defaultwindow {\r\n  background-image: url(getimage?image=main&workgroup=");
      out.print( workgroup );
      out.write(");\r\n}\r\n\r\n#conversation {\r\n    width : 398px;\r\n    height : 228px;\r\n\tposition : absolute;\r\n\ttop : 11;\r\n\tleft : 11;\r\n}\r\n\r\n#inputbuttons {\r\n    width : 400px;\r\n    height : 25px;\r\n    position : absolute;\r\n    top : 245px;\r\n    left : 10px;\r\n    text-align : center;\r\n}\r\n\r\n#inputbuttons FORM, #inputbuttons DIV {\r\n    display : inline;\r\n}\r\n\r\n#inputbuttons DIV {\r\n    margin : 1px 2px 1px 2px;\r\n    padding : 0px;\r\n}\r\n\r\n#inputbuttons #filebutton {\r\n    margin-left : 10px;\r\n}\r\n\r\n#inputbox {\r\n    width : 310px;\r\n    height : 60px;\r\n    position : absolute;\r\n    left : 10;\r\n    top : 280;\r\n}\r\n\r\n#questionbox {\r\n    width : 300px;\r\n    height : 60px;\r\n    position : absolute;\r\n    left : 10;\r\n    top : 280;\r\n}\r\n\r\n#questionbox TEXTAREA {\r\n   width : 100%;\r\n   height : 50px;\r\n   margin : 0px;\r\n}\r\n\r\n#questionbox SELECT {\r\n   width : 100%;\r\n   height : 50px;\r\n   margin : 0px;\r\n}\r\n\r\n#questionbox FORM {\r\n  display:inline;\r\n}\r\n\r\n\r\n\r\n\r\n#inputbox TEXTAREA {\r\n    width : 100%;\r\n    height : 60px;\r\n    margin : 0px;\r\n}\r\n#inputbox FORM {\r\n");
      out.write("    display : inline;\r\n}\r\n\r\n#sendbutton {\r\n    width : 100px;\r\n    height : 30px;\r\n    position : absolute;\r\n    top : 295px;\r\n    left : 430px;\r\n}\r\n\r\n#infobox {\r\n    width : 120px;\r\n    height : 230px;\r\n    position : absolute;\r\n    top : 40px;\r\n    left : 420px;\r\n}\r\n\r\n#closebutton {\r\n    background-image: url(images/end_button.gif);\r\n    background-repeat : no-repeat;\r\n    position : absolute;\r\n}\r\n\r\n#helpbutton {\r\n    background-image: url(images/helpicon-");
      out.print( branding );
      out.write(".gif);\r\n    background-repeat : no-repeat;\r\n    position : absolute;\r\n    width : 16;\r\n    height : 16;\r\n    top : 15;\r\n    left : 460px;\r\n}\r\n\r\n#printbutton {\r\n    background-image: url(images/printicon-");
      out.print( branding );
      out.write(".gif);\r\n    background-repeat : no-repeat;\r\n    position : absolute;\r\n    width : 16;\r\n    height : 16;\r\n    top : 15;\r\n    left : 460px;\r\n}\r\n\r\n#userinfowindow #closebutton, #queuewindow #closebutton, #helpwindow #closebutton,\r\n        #transcriptwindow #closebutton, #standardwindow #closebutton\r\n{\r\n    top : 15px;\r\n    left : 519px;\r\n}\r\n\r\n#transcriptprintwindow #printbutton {\r\n    top : 15px;\r\n    left : 475px;\r\n}\r\n\r\n#transcriptprintwindow #closebutton {\r\n    top : 15px;\r\n    left : 500px;\r\n}\r\n\r\n#userinfowindow #helpbutton, #queuewindow #helpbutton, #transcriptwindow #helpbutton,\r\n        #standardwindow #helpbutton\r\n{\r\n    top : 15px;\r\n    left : 495px;\r\n}\r\n\r\n/* Styles for the text of the conversation */\r\n\r\n#conversation-text {\r\n    padding : 5px;\r\n}\r\n.chat-line {\r\n    padding : 0px 0px 4px 0px;\r\n}\r\n.operator-name {\r\n    font-weight : bold;\r\n    color : #00f;\r\n}\r\n.client-name {\r\n    font-weight : bold;\r\n    color : #f00;\r\n}\r\n.chat-announcement {\r\n    font-style : italic;\r\n    color : #060;\r\n}\r\n\r\n/* Styles for the other smaller popups and popup pages */\r\n");
      out.write("\r\n#fileuploadbox {\r\n    visibility : hidden;\r\n    width : 400px;\r\n    height : 250px;\r\n    position : absolute;\r\n    left : 50;\r\n    top : 50;\r\n    border : 2px #ccc groove;\r\n}\r\n\r\n\r\n\r\n#fileuploadbox, #userinfowindow, #queuewindow, #endsessionwindow, #fatalerrorwindow, #helpwindow,\r\n        #transcriptwindow, #transcriptprintwindow, #standardwindow\r\n{\r\n    background-image: url(images/branding-helperwin-");
      out.print( branding );
      out.write(".gif);\r\n    background-repeat : repeat-x;\r\n}\r\n\r\n#upload-header, #userinfo-header, #queue-header, #endsession-header, #fatalerrorwindow-header,\r\n        #helpwindow-header, #transcriptwindow-header, #transcriptprintwindow-header\r\n        #standard-header\r\n{\r\n    width : 550px;\r\n    height : 50px;\r\n    position : absolute;\r\n    top : 0px;\r\n    left : 0px;\r\n}\r\n\r\n#upload-header P, #userinfo-header P, #queue-header P, #endsession-header P, #fatalerrorwindow P,\r\n        #helpwindow-header P, #transcriptwindow-header P, #transcriptprintwindow-header P,\r\n        #standard-header P\r\n{\r\n    font-weight : bold;\r\n    margin : 0px;\r\n    padding : 16px 16px 8px 16px;\r\n}\r\n\r\n#upload-content {\r\n    width : 400px;\r\n    position : absolute;\r\n    top : 50px;\r\n    left : 0px;\r\n}\r\n\r\n#userinfo-content, #queue-content, #endsession-content, #fatalerrorwindow-content,\r\n        #helpwindow-content, #transcriptwindow-content, #transcriptprintwindow-content\r\n        #standard-content\r\n{\r\n    width : 550px;\r\n    position : absolute;\r\n    top : 50px;\r\n");
      out.write("    left : 0px;\r\n}\r\n\r\n#helpwindow-content, #transcriptwindow-content, #transcriptprintwindow-content {\r\n    height : 300px;\r\n}\r\n\r\n#upload-content .box, #userinfo-content .box, #queue-content .box, #endsession-content .box,\r\n        #fatalerrorwindow-content .box, #transcriptwindow-content .box,\r\n        #transcriptprintwindow-content .box,\r\n        #standard-content .box\r\n{\r\n    padding : 16px 16px 5px 16px;\r\n}\r\n\r\n.inputField {\r\n  font-weight: normal;\r\n  font-size: 8pt;\r\n  font-family: Verdana;\r\n  border:solid #FFFFFF 2px;\r\n  border-style:none;\r\n  border-width:0px;\r\n  border-color:white;\r\n}\r\n.queue_info_text {font-family: Arial, Helvetica, sans-serif; font-size: 11px; margin-top: 6; color: white;}\r\n.queue_info_small_text {font-family: Arial, Helvetica, sans-serif; font-size: 11px; margin-top: 2; color: white;}\r\n\r\n.testField\r\n{\r\nfont-family: Verdana, Arial, Helvetica, sans-serif;\r\nfont-size: 10px;\r\ncolor: #000000;\r\nborder-top-width: 1px;\r\nborder-right-width: 1px;\r\nborder-bottom-width: 1px;\r\nborder-left-width: 1px;\r\n");
      out.write("border-top-style: solid;\r\nborder-right-style: solid;\r\nborder-bottom-style: solid;\r\nborder-left-style: solid;\r\nborder-top-color: white;\r\nborder-right-color: white;\r\nborder-bottom-color: white;\r\nborder-left-color: white;\r\n}\r\n\r\n#userinfo-content INPUT {\r\n    margin : 1px;\r\n}\r\n\r\n/* Styles for the help page */\r\n\r\n#helpcontent {\r\n    padding : 15px;\r\n}\r\n\r\n/* FOLLOWING FOR DEBUGGING ONLY */\r\n\r\n/* Borders on all DIVs */\r\n/*\r\nDIV, P {\r\n    border : 1px #ccc solid;\r\n}\r\n*/\r\n\r\n/* User Defined Variable */\r\n.info_text {font-family: Arial, Helvetica, sans-serif; font-size: 11px; margin-top: 6; color: black;}\r\n.info_small_text {font-family: Arial, Helvetica, sans-serif; font-size: 11px; margin-top: 2; color: black;}\r\n\r\n.header {font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: bold; color: black;}\r\n.input_title {font-family: Arial, Helvetica, sans-serif; font-size: 11px; font-weight: bold; color: black;}\r\n\r\n.box { BORDER: #cccccc 1px solid; }\r\n.boxLayout {\r\n\tBORDER-RIGHT: #003399 1px solid; PADDING-RIGHT: 0px; BORDER-TOP: #003399 1px solid; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; BORDER-LEFT: #003399 1px solid; PADDING-TOP: 0px; BORDER-BOTTOM: #003399 1px solid\r\n");
      out.write("}\r\nboxLayoutGrey {\r\n\tBORDER-RIGHT: #cccccc 1px solid; PADDING-RIGHT: 0px; BORDER-TOP:#cccccc 1px solid; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; BORDER-LEFT: #cccccc 1px solid; PADDING-TOP: 0px; BORDER-BOTTOM: #cccccc 1px solid\r\n}\r\n\r\n.error_text {font-family: Arial, Helvetica, sans-serif; font-size: 9px; color : #f00; }\r\n.error {\r\n\tFONT-WEIGHT: bold; FONT-SIZE: 10px; COLOR: #cc0000; LINE-HEIGHT: 16px; FONT-FAMILY: helvetica,arial,sans-serif,verdana; TEXT-ALIGN: justify; TEXT-DECORATION: none\r\n}\r\n.descriptive-live {\r\n\tfont-family: Arial;\r\n\tcolor: #336699;\r\n\tfont-size: 10pt;\r\n\tfont-weight: bold;\r\n\ttext-decoration: underline;\r\n}\r\n\r\n.content {  font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt; color: #000000}\r\n.nicetext {  font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt; color: #000000}\r\n.nicetextsmall {  font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; color: #000000}\r\n.subhead { font-family: Arial, Helvetica, sans-serif; font-size: 12pt; color: #000066}\r\n");
      out.write(".subtitle{\r\n\t\tfont-size: 14px;\r\n        font-family:Verdana, Arial, Helvetica, sans-serif;\r\n        color: #000000;\r\n\t\tmargin-bottom:10px;\r\n\t\tfont-weight:bold;\r\n}\r\n\r\n.highlight {\r\n\tcolor: #990000;\r\n}\r\n\r\n.formtitle {  font-family: Arial, Helvetica, sans-serif; font-size: 20px; color: #FB7015; font-weight: bolder; font-style: italic; letter-spacing: -1px}\r\n.otherbox {  font-family: Arial, Helvetica, sans-serif; font-size: 12px; height: 18px; width: 205px; background-color: #EFEFE7; padding-left: 3px; color: #333333; border-top-width: 0px; border-right-width: 0px; border-bottom-width: 0px; border-left-width: 0px; background-image: url(../img/otherbox.gif)}\r\n.formtext {  font-family: Arial, Helvetica, sans-serif; font-size: 12px; font-weight: normal; color: #224162}\r\n.text {  font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #333333}\r\n\r\n");
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
