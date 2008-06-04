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

import org.jivesoftware.smackx.packet.PrivateData;

import java.util.Map;

/**
 * Represents the private data for the Workgroup Settings.
 */
public class SettingsPrivateData implements PrivateData {
    private String elementName;
    private String namespace;
    private Map settingsMap;

    /**
     * Paramter constructor to create the PrivateData to use.
     * @param workgroup the name of the workgroup this private data belongs to.
     * @param map the private data settings.
     */
    public SettingsPrivateData(String workgroup, Map map) {
        super();
        namespace = "workgroup:" + workgroup + ":settings:email";
        elementName = "workgroup_settings";
        this.settingsMap = map;
    }

    /**
     * Returns the private data settings.
     * @return the settings.
     */
    public Map getMap() {
        return settingsMap;
    }

    /**
     * PrivateData implementation
     */
    public String getElementName() {
        return elementName;
    }

    /**
     * Returns the namespace.
     * @return the namespace.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Creates the xml representation of the data.
     * @return
     */
    public String toXML() {
        final StringBuffer buf = new StringBuffer();
        return buf.toString();
    }

}
