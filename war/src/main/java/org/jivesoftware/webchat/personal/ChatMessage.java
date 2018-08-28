/**
 * $RCSfile$
 * $Revision: 19490 $
 * $Date: 2005-08-12 10:52:02 -0700 (Fri, 12 Aug 2005) $
 *
 * Copyright (C) 1999-2005 Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is
 subject to license terms.
 */
package org.jivesoftware.webchat.personal;

import org.jivesoftware.webchat.util.FormText;
import org.jivesoftware.webchat.util.ModelUtil;
import org.jivesoftware.webchat.util.WebUtils;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smackx.jiveproperties.JivePropertiesManager;
import org.jxmpp.jid.parts.Localpart;

/* RCSFile: $
 * Revision: $
 * Date: $
 *
 * Copyright (C) 2004-2008 JiveSoftware, Inc. All rights reserved.
 *
 * This software is the proprietary information of CoolServlets, Inc.
 * Use is subject to license terms.
 */

public class ChatMessage {
    private String from;
    private String body;
    private Stanza packet;
    private String urlToPush;
    private String date;

    public ChatMessage(Stanza packet) {
        if (packet instanceof Presence) {
            Presence presence = (Presence)packet;
            from = "";

            String usersNickname = presence.getFrom().getResourceOrEmpty().toString();
            body = usersNickname + " has joined the conversation.";
        }
        else if (packet instanceof Message) {
            Message message = (Message)packet;
            if (JivePropertiesManager.getProperty(message, "PUSH_URL") != null) {
                urlToPush = (String)JivePropertiesManager.getProperty(message, "PUSH_URL");
                urlToPush = WebUtils.getPushedURL(urlToPush);
            }
            else if (JivePropertiesManager.getProperty(message, "transfer")!= null) {
                from = "";
                boolean transfer = ((Boolean)JivePropertiesManager.getProperty(message, "transfer")).booleanValue();
                String workgroup = (String)JivePropertiesManager.getProperty(message, "workgroup");
                if (transfer) {
                    body = FormText.getTransferToAgentText(workgroup);
                }
                else {
                    body = FormText.getInvitingAgentText(workgroup);
                }
            }
            else {
                String from = message.getFrom().getResourceOrEmpty().toString();
                setFrom(from);
                setBody("");
            }
        }
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setFrom(Localpart from) {
       this.from = from.toString();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        body = WebUtils.applyFilters(body);
        body = body.replaceAll("\n", "<br>");

        this.body = body;
    }

    public Stanza getStanza() {
        return packet;
    }

    public void setStanza(Stanza packet) {
        this.packet = packet;
    }

    public String getUrlToPush() {
        return urlToPush;
    }

    public void setUrlToPush(String urlToPush) {
        this.urlToPush = urlToPush;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCobrowsing() {
        return ModelUtil.hasLength(urlToPush);
    }
}
