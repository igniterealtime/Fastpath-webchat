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

package org.jivesoftware.webchat.history;

/**
 * A Line is a line in a chat - has a from and text. The from can be null if this is an
 * announcement.
 */
public class Line {
    private String from;
    private String text;

    /**
     * Creates a new line within a chat transcript.
     * @param from the nickname of the user who sent the message, or null if
     * it's a presence update.
     * @param text the body of the message.
     */
    public Line(String from, String text) {
        this.from = from;
        this.text = text;
    }

    /**
     * Returns the nickname of the user who sent the message.
     * @return the nickname of the user who sent the message.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the body of the message.
     * @return the body of the message.
     */
    public String getText() {
        return text;
    }
}