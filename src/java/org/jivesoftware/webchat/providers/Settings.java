/**
 * $RCSFile$
 * $Revision: 18449 $
 * $Date: 2005-02-14 12:13:03 -0800 (Mon, 14 Feb 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.providers;

import org.jivesoftware.webchat.util.WebLog;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.PrivateDataManager;

import java.util.Map;

/**
 * Handles all generic metadata settings for a particular workgroup. For example, to retrieve emails settings for
 * demo@workgroup.localhost:<p>
 * <pre>
 * Settings settings = new Settings();
 * Map settingsMap = settings.getSettings(con, "demo@workgroup.localhost", "email");
 *
 */
public class Settings {
    /**
     * Used to retrieve offline settings. Offline settings specify what to do if
     * a workgroup is not accepting requests.
     */
    public static final String OFFLINE_SETTINGS = "offline";

    /**
     * Used to retrieve email account information.
     */
    public static final String EMAIL_SETTINGS = "email";

    /**
     * Used to retrieve images specified for a workgroup.
     */
    public static final String IMAGE_SETTINGS = "images";

    private Settings() {
    }

    /**
     * Returns the specified settings for a Workgroup.
     * @param con the XMPPConnection to use.
     * @param workgroup the name of the workgroup.
     * @param setting the setting to retrieve.
     * @return a map of found settings.  If no settings have been found, it will
     * return null.
     */
    public static Map getSettings(XMPPConnection con, String workgroup, String setting) {

        try {
            PrivateDataManager personalPDM = new PrivateDataManager(con, workgroup);

            String namespace = "workgroup:" + workgroup + ":settings:" + setting;
            String elementName = "workgroup_settings";

            PrivateDataManager.addPrivateDataProvider(elementName, namespace, new SettingsDataProvider());
            SettingsPrivateData data = (SettingsPrivateData) personalPDM.getPrivateData(elementName, namespace);
            Map map = data.getMap();
            return map;
        }
        catch (XMPPException e) {
            WebLog.logError("Could not load private data:", e);
        }
        return null;
    }
}