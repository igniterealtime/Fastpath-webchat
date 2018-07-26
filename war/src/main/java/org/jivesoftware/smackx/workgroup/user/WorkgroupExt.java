package org.jivesoftware.smackx.workgroup.user;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smackx.workgroup.ext.email.EmailIQ;


public class WorkgroupExt extends Workgroup {

    private Connection connection;

    /**
     * @param workgroupJID
     * @param connection
     */
    public WorkgroupExt(String workgroupJID, Connection connection) {
        super(workgroupJID, connection);
        this.connection = connection;
    }
    
    public boolean sendMail(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) throws XMPPException {
        EmailIQ localEmailIQ = new EmailIQ();
        localEmailIQ.setToAddress(paramString1);
        localEmailIQ.setFromAddress(paramString2);
        localEmailIQ.setSubject(paramString3);
        localEmailIQ.setMessage(paramString4);
        localEmailIQ.setHtml(paramBoolean);
        localEmailIQ.setType(IQ.Type.SET);
        localEmailIQ.setTo(getWorkgroupJID());
        PacketCollector localPacketCollector = connection.createPacketCollector(new PacketIDFilter(localEmailIQ.getPacketID()));
        connection.sendPacket(localEmailIQ);
        Packet localPacket = localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        localPacketCollector.cancel();
        if (localPacket == null)
            throw new XMPPException("No response from server.");
        if (localPacket.getError() != null)
            throw new XMPPException(localPacket.getError());
        return true;
    }
    
    public boolean sendTranscript(String paramString1, String paramString2) throws XMPPException {
        EmailIQ localEmailIQ = new EmailIQ();
        localEmailIQ.setToAddress(paramString1);
        localEmailIQ.setSessionID(paramString2);
        localEmailIQ.setType(IQ.Type.SET);
        localEmailIQ.setTo(getWorkgroupJID());
        PacketCollector localPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(localEmailIQ.getPacketID()));
        this.connection.sendPacket(localEmailIQ);
        Packet localPacket = localPacketCollector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        localPacketCollector.cancel();
        if (localPacket == null)
            throw new XMPPException("No response from server.");
        if (localPacket.getError() != null)
            throw new XMPPException(localPacket.getError());
        return true;
    }
    
}
