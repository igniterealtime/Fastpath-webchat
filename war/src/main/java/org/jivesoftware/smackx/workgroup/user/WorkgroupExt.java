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
        
//        StanzaCollector localStanzaCollector = connection.createStanzaCollector(new StanzaIdFilter(localEmailIQ.getStanzaId()));
//        connection.sendStanza(localEmailIQ);
//        Stanza localStanza = localStanzaCollector.nextResult(connection.getReplyTimeout());
//        localStanzaCollector.cancel();
//        if (localStanza == null)
//            throw new XMPPErrorException("No response from server.");
//        if (localStanza.getError() != null)
//            throw new XMPPErrorException(localStanza.getError());
        return true;
    }
    
    public boolean sendTranscript(String paramString1, String paramString2) throws XMPPErrorException, NotConnectedException, InterruptedException {
        EmailIQ localEmailIQ = new EmailIQ();
        localEmailIQ.setToAddress(paramString1);
        localEmailIQ.setSessionID(paramString2);
        localEmailIQ.setType(IQ.Type.set);
        localEmailIQ.setTo(getWorkgroupJID());
        
        this.connection.createStanzaCollectorAndSend(localEmailIQ);
        
//        StanzaCollector localStanzaCollector = this.connection.createStanzaCollector(new StanzaIdFilter(localEmailIQ.getStanzaId()));
//        this.connection.sendStanza(localEmailIQ);
//        Stanza localStanza = localStanzaCollector.nextResult(connection.getReplyTimeout());
//        localStanzaCollector.cancel();
//        if (localStanza == null)
//            throw new XMPPErrorException("No response from server.");
//        if (localStanza.getError() != null)
//            throw new XMPPErrorException(localStanza.getError());
        return true;
    }
    
}
