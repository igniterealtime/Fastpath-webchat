package org.jivesoftware.webchat.sounds;

import org.jivesoftware.smackx.workgroup.user.Workgroup;
import org.jivesoftware.smackx.workgroup.settings.SoundSettings;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.util.WebLog;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;

import org.jivesoftware.smack.XMPPException;

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
        Workgroup workgroup = new Workgroup(workgroupName, ChatManager.getInstance().getGlobalConnection());
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
        } catch (XMPPException e) {
            WebLog.log("Could not load sound settings for workgroup " + workgroupName);
        }
    }
}