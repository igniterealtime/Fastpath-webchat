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
import org.jivesoftware.smackx.provider.PrivateDataProvider;
import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;


/**
 * Parses all generic metadata for workgroup settings.
 */
public class SettingsDataProvider implements PrivateDataProvider {

    /**
     * Empty Constructor
     */
    public SettingsDataProvider() {
    }

    /**
     * Parses the xml stream for workgroup settings.
     * @param parser the XmlPullParse to use.
     * @return the PrivateData.
     * @throws Exception
     */
    public PrivateData parsePrivateData(XmlPullParser parser) throws Exception {
        final Map map = new HashMap();
        String t = parser.getText();

        int eventType = parser.getEventType();
        String privateSuperTagName = null;
        boolean isPersonal = true;

        if (eventType != XmlPullParser.START_TAG) {
            // throw exception
        }

        eventType = parser.nextTag();
        String text = parser.getName();
        while (true) {
            if ("entry".equals(text)) {
                eventType = parser.nextTag();
                String name = parser.getName();
                text = parser.nextText();

                map.put(name, text);
                eventType = parser.nextTag();
                eventType = parser.nextTag();
                text = parser.getName();
            }
            else {
                break;
            }
        }
        return new SettingsPrivateData("workgroup", map);
    }

}
