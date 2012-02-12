package org.jivesoftware.smackx.workgroup.ext.email;

import org.jivesoftware.smack.packet.IQ;

public class EmailIQ extends IQ {
    public static final String ELEMENT_NAME = "send-email";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private String fromAddress;
    private String toAddress;
    private String subject;
    private String message;
    private boolean html;
    private String sessionID;
    
    public String getChildElementXML() {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("<").append("send-email").append(" xmlns=\"").append("http://jivesoftware.com/protocol/workgroup").append("\">");
        localStringBuilder.append("<fromAddress>").append(getFromAddress()).append("</fromAddress>");
        localStringBuilder.append("<toAddress>").append(getToAddress()).append("</toAddress>");
        localStringBuilder.append("<subject>").append(getSubject()).append("</subject>");
        localStringBuilder.append("<message>").append(getMessage()).append("</message>");
        localStringBuilder.append("<useHTML>").append(Boolean.toString(isHtml())).append("</useHTML>");
        if (getSessionID() != null)
            localStringBuilder.append("<sessionID>").append(getSessionID()).append("</sessionID>");
        localStringBuilder.append("</").append("send-email").append("> ");
        return localStringBuilder.toString();
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
}