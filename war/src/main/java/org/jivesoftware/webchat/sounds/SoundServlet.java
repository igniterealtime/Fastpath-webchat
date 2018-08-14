package org.jivesoftware.webchat.sounds;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.workgroup.settings.SoundSettings;
import org.jivesoftware.smackx.workgroup.user.Workgroup;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.util.WebLog;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

public class SoundServlet extends HttpServlet {

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String workgroupName = request.getParameter("workgroup");
        String action = request.getParameter("action");
        Jid workgroupJid = JidCreate.from(workgroupName);

        Workgroup workgroup = new Workgroup(workgroupJid, ChatManager.getInstance().getGlobalConnection());
        try {
            SoundSettings soundSettings = workgroup.getSoundSettings();
            response.setContentType("audio/wav");
            if (action != null) {
                if ("incoming".equals(action.trim())) {
                    response.getOutputStream().write(soundSettings.getIncomingSoundBytes());
                } else if ("outgoing".equals(action.trim())) {
                    response.getOutputStream().write(soundSettings.getOutgoingSoundBytes());
                }
            }
        } catch (XMPPException | NoResponseException | NotConnectedException | InterruptedException e) {
            WebLog.log("Could not load sound settings for workgroup " + workgroupName);
        }
    }
}