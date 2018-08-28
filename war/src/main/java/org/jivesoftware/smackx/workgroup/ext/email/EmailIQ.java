package org.jivesoftware.smackx.workgroup.ext.email;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.util.StringUtils;

public class EmailIQ extends IQ {
  
    public static final String ELEMENT_NAME = "send-email";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String fromAddress;
    private String toAddress;
    private String subject;
    private String message;
    private boolean html;
    private String sessionID;
    
    public EmailIQ() {
         super(ELEMENT_NAME , NAMESPACE);
    }
    
    public String getFromAddress() {
        return this.fromAddress;
    }
    
    public void setFromAddress(String paramString) {
        this.fromAddress = paramString;
    }
    
    public String getToAddress() {
        return this.toAddress;
    }
    
    public void setToAddress(String paramString) {
        this.toAddress = paramString;
    }
    
    public String getSubject() {
        return this.subject;
    }
    
    public void setSubject(String paramString) {
        this.subject = paramString;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String paramString) {
        this.message = paramString;
    }
    
    public boolean isHtml() {
        return this.html;
    }
    
    public void setHtml(boolean paramBoolean) {
        this.html = paramBoolean;
    }
    
    public String getSessionID() {
        return this.sessionID;
    }
    
    public void setSessionID(String paramString) {
        this.sessionID = paramString;
    }
    
    @Override
    protected IQChildElementXmlStringBuilder getIQChildElementBuilder(IQChildElementXmlStringBuilder xml) {

      xml.append("<").append("send-email").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
      xml.append("<fromAddress>").append(StringUtils.escapeForXml(getFromAddress())).append("</fromAddress>");
      xml.append("<toAddress>").append(StringUtils.escapeForXml(getToAddress())).append("</toAddress>");
      xml.append("<subject>").append(StringUtils.escapeForXml(getSubject())).append("</subject>");
      xml.append("<message>").append(StringUtils.escapeForXml(getMessage())).append("</message>");
      xml.append("<useHTML>").append(Boolean.toString(isHtml())).append("</useHTML>");
           if (getSessionID() != null)
            xml.append("<sessionID>").append(getSessionID()).append("</sessionID>");
           xml.append("</").append("send-email").append("> ");
           return xml;
    }
    
}
