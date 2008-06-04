/**
 * $RCSFile$
 * $Revision: 24159 $
 * $Date: 2005-11-28 13:49:07 -0800 (Mon, 28 Nov 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.settings;

import org.jivesoftware.webchat.util.WebLog;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles the loading and saving of the Settings associated with this webapp.
 */
public class ChatSettingsManager {
    private ConnectionSettings connectionSettings;
    private XStream xstream;
    private File settingsFile;

    /**
     * Initialize ChatSettingsManager.
     */
    public ChatSettingsManager() {
        xstream = new XStream();
        xstream.alias("chat-settings", ConnectionSettings.class);
    }

    /**
     * Loads the settings file.
     *
     * @param settingsFile the File wrapper of the settings file.
     */
    public ChatSettingsManager(File settingsFile) {
        this();
        this.settingsFile = settingsFile;
    }

    /**
     * Returns the <code>ChatSettings</code> of this webapp.
     *
     * @return the <code>ChatSettings</code> of this webapp, otherwise returns null if one
     *         does not exist.
     */
    public ConnectionSettings getSettings() {
        if (connectionSettings != null) {
            return connectionSettings;
        }

        // Otherwise load
        loadSettings();


        return connectionSettings;
    }

    /**
     * Load the chat-settings.xml file. The chat-settings.xml should be located under the WEB-INF
     * directory of the webapp.
     */
    public void loadSettings() {
        if (settingsFile.exists()) {
            try {
                FileReader reader = new FileReader(settingsFile);
                connectionSettings = (ConnectionSettings)xstream.fromXML(reader);
            }
            catch (Exception e) {
                WebLog.logError("Fastpath could not find chat-settings.xml in the WEB-INF directory of Webchat", e);
            }
        }
    }

    /**
     * Returns true if <code>ChatSettings</code> have already been set.
     *
     * @return true if <code>ChatSettings</code> have already been set.
     */
    public boolean hasSettings() {
        return getSettingsFile().exists();
    }

    /**
     * Persists the <code>ChatSettings</code>.
     *
     * @param settings the <code>ChatSettings</code> to persist.
     */
    public void save(ConnectionSettings settings) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(getSettingsFile());
        }
        catch (IOException e) {
            WebLog.logError("Error saving settings:", e);
        }
        xstream.toXML(settings, writer);
        connectionSettings = settings;
    }

    /**
     * Returns the ChatSettings file.
     *
     * @return the ChatSettings File.
     */
    private File getSettingsFile() {
        return settingsFile;
    }
}