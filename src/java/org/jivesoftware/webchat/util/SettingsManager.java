/**
 * $RCSFile$
 * $Revision: 28143 $
 * $Date: 2006-03-06 20:42:43 -0800 (Mon, 06 Mar 2006) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.util;

import org.jivesoftware.smackx.workgroup.settings.ChatSetting;
import org.jivesoftware.smackx.workgroup.settings.ChatSettings;
import org.jivesoftware.smackx.workgroup.user.Workgroup;
import org.jivesoftware.webchat.ChatManager;
import org.jivesoftware.webchat.actions.WorkgroupChangeListener;
import org.jivesoftware.webchat.actions.WorkgroupStatus;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for retrieving and writing out images belong to a workgroup.
 */
public class SettingsManager implements WorkgroupChangeListener {
    private static final String CONTENT_TYPE = "image/jpeg";
    private ChatManager chatManager = ChatManager.getInstance();

    /**
     * Stores the ChatSettings of each workgroup, and will be updated
     * when packet date of workgroup changes.
     */
    private Map chatSettings = new HashMap();


    private static SettingsManager singleton;
    private static final Object LOCK = new Object();

    /**
     * Returns the singleton instance of <CODE>ImageManager</CODE>,
     * creating it if necessary.
     * <p/>
     *
     * @return the singleton instance of <Code>ImageManager</CODE>
     */
    public static SettingsManager getInstance() {
        // Synchronize on LOCK to ensure that we don't end up creating
        // two singletons.
        synchronized (LOCK) {
            if (null == singleton) {
                SettingsManager controller = new SettingsManager();
                singleton = controller;
                return controller;
            }
        }
        return singleton;
    }

    private SettingsManager() {
        WorkgroupStatus.addWorkgroupChangeListener(this);
    }

    /**
     * Writes out a <code>byte</code> to the ServletOuputStream.
     *
     * @param bytes the bytes to write to the <code>ServletOutputStream</code>.
     */
    public void writeBytesToStream(byte[] bytes, HttpServletResponse response) {
        if (bytes == null) {
            return;
        }

        response.setContentType(CONTENT_TYPE);

        // Send back image
        try {
            ServletOutputStream sos = response.getOutputStream();
            sos.write(bytes);
            sos.flush();
            sos.close();
        }
        catch (IOException e) {
            WebLog.logError("Failed writing out image on " + new Date(), e);
        }
    }

    public ChatSetting getChatSetting(String key, String workgroupName) {
        ChatSettings settings = null;
        if (chatSettings.containsKey(workgroupName)) {
            settings = (ChatSettings)chatSettings.get(workgroupName);
        }
        else {
            XMPPConnection connection = chatManager.getGlobalConnection();
            Workgroup workgroup = new Workgroup(workgroupName, connection);
            try {
                settings = workgroup.getChatSettings();
                chatSettings.put(workgroupName, settings);
            }
            catch (XMPPException e) {
                WebLog.logError("Error retrieving chat setting using key=" + key + " and workgroup=" + workgroupName, e);
            }
        }
        if (settings != null) {
            return settings.getChatSetting(key);
        }
        return null;
    }


    /**
     * Returns the BufferedImage associated with the Workgroup.
     *
     * @param imageName     the name of the image to retrieve.
     * @param workgroupName the name of the workgroup.
     * @return the BufferedImage
     */
    public byte[] getImage(String imageName, String workgroupName, ServletContext context) {
        if (!ModelUtil.hasLength(workgroupName) || "null".equals(workgroupName)) {
            WebLog.logError("Workgroup must be specified to retrieve image " + imageName);
            return getBlankImage(context);
        }
        // Handle already populated images.
        if (chatSettings.containsKey(workgroupName)) {
            byte[] bytes = getImageFromMap(imageName, workgroupName);
            if (bytes == null) {
                return getBlankImage(context);
            }
            return bytes;
        }

        // Otherwise, retrieve images from private data, store and send.
        XMPPConnection connection = chatManager.getGlobalConnection();
        try {
            Workgroup workgroup = new Workgroup(workgroupName, connection);
            ChatSettings chatSettings = workgroup.getChatSettings();
            this.chatSettings.put(workgroupName, chatSettings);
            byte[] imageBytes = getImageFromMap(imageName, workgroupName);
            if (imageBytes == null) {
                return getBlankImage(context);
            }
            return imageBytes;

        }
        catch (Exception e) {
            WebLog.logError("Could not retrieve image: " + imageName, e);
            return getBlankImage(context);
        }
    }

    public byte[] getImageFromMap(String imageName, String workgroupName) {
        ChatSettings chatSettings = (ChatSettings)this.chatSettings.get(workgroupName);
        ChatSetting imageSetting = chatSettings.getChatSetting(imageName);
        if (imageSetting == null || imageSetting.getValue() == null) {
            return null;
        }
        else {
            byte[] bytes = Base64.decode(imageSetting.getValue());
            return bytes;
        }
    }

    /**
     * Returns a blank image.
     *
     * @return a 1x1 blank BufferedImage
     */
    private byte[] getBlankImage(ServletContext context) {
        String blankImage = context.getRealPath("/images/blank.gif");
        final URL imageURL = URLFileSystem.newFileURL(blankImage);
        byte[] bytes = new byte[0];
        try {
            bytes = URLFileSystem.getBytes(imageURL);
        }
        catch (IOException e) {
            WebLog.logError("Error getting blank image bytes.", e);
        }
        return bytes;
    }

    /**
     * If the workgroup has been updated, remove from cache.
     *
     * @param workgroupName the name of the workgroup updated.
     */
    public void workgroupUpdated(String workgroupName) {
        chatSettings.remove(workgroupName);
    }
}