package org.jivesoftware.smackx.workgroup.user;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.StanzaIdFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.workgroup.ext.email.EmailIQ;


public class WorkgroupExt extends Workgroup {

    private XMPPTCPConnection connection;

    /**
     * @param workgroupJID
     * @param connection
     */
    public WorkgroupExt(String workgroupJID, XMPPTCPConnection connection) {
        super(workgroupJID, connection);
        this.connection = connection;
    }
    
    public boolean sendMail(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) throws XMPPErrorException, NotConnectedException {
        EmailIQ localEmailIQ = new EmailIQ();
        localEmailIQ.setToAddress(paramString1);
        localEmailIQ.setFromAddress(paramString2);
        localEmailIQ.setSubject(paramString3);
        localEmailIQ.setMessage(paramString4);
        localEmailIQ.setHtml(paramBoolean);
        localEmailIQ.setType(IQ.Type.set);
        localEmailIQ.setTo(getWorkgroupJID());
        PacketCollector localPacketCollector = connection.createPacketCollector(new StanzaIdFilter(localEmailIQ.getStanzaId()));
        connection.sendStanza(localEmailIQ);
        Stanza localPacket = localPacketCollector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());
        localPacketCollector.cancel();
        if (localPacket == null)
            throw new XMPPErrorException("No response from server.",null);
        if (localPacket.getError() != null)
            throw new XMPPErrorException(localPacket.getError());
        return true;
    }
    
    public boolean sendTranscript(String paramString1, String paramString2) throws XMPPErrorException, NotConnectedException {
        EmailIQ localEmailIQ = new EmailIQ();
        localEmailIQ.setToAddress(paramString1);
        localEmailIQ.setSessionID(paramString2);
        localEmailIQ.setType(IQ.Type.set);
        localEmailIQ.setTo(getWorkgroupJID());
        PacketCollector localPacketCollector = this.connection.createPacketCollector(new StanzaIdFilter(localEmailIQ.getStanzaId()));
        this.connection.sendStanza(localEmailIQ);
        Stanza localPacket = localPacketCollector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());
        localPacketCollector.cancel();
        if (localPacket == null)
            throw new XMPPErrorException("No response from server." , null);
        if (localPacket.getError() != null)
            throw new XMPPErrorException(localPacket.getError());
        return true;
    }
    
}
