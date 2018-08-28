package org.jivesoftware.smackx.workgroup.user;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.workgroup.ext.email.EmailIQ;
import org.jxmpp.jid.Jid;


public class WorkgroupExt extends Workgroup {

    private XMPPConnection connection;

    /**
     * @param workgroupJID
     * @param connection
     */
    public WorkgroupExt(Jid workgroupJID, XMPPConnection connection) {
        super(workgroupJID, connection);
        this.connection = connection;
    }
    
    public boolean sendMail(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) throws XMPPErrorException, NotConnectedException, InterruptedException {
        EmailIQ localEmailIQ = new EmailIQ();
        localEmailIQ.setToAddress(paramString1);
        localEmailIQ.setFromAddress(paramString2);
        localEmailIQ.setSubject(paramString3);
        localEmailIQ.setMessage(paramString4);
        localEmailIQ.setHtml(paramBoolean);
        localEmailIQ.setType(IQ.Type.set);
        localEmailIQ.setTo(getWorkgroupJID());
        
        this.connection.createStanzaCollectorAndSend(localEmailIQ);
        
        return true;
    }
    
    public boolean sendTranscript(String paramString1, String paramString2) throws XMPPErrorException, NotConnectedException, InterruptedException {
        EmailIQ localEmailIQ = new EmailIQ();
        localEmailIQ.setToAddress(paramString1);
        localEmailIQ.setSessionID(paramString2);
        localEmailIQ.setType(IQ.Type.set);
        localEmailIQ.setTo(getWorkgroupJID());
        
        this.connection.createStanzaCollectorAndSend(localEmailIQ);
        
        return true;
    }
    
}
