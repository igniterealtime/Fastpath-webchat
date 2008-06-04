/**
 * $RCSFile$
 * $Revision: 18542 $
 * $Date: 2005-03-03 15:01:36 -0800 (Thu, 03 Mar 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.history;

import java.util.ArrayList;
import java.util.List;

/**
 * A Transcript is a list of Line objects - each Line contains a from and message text.
 */
final public class Transcript {

    private List transcript;

    /**
     * Empty Constructor
     */
    public Transcript() {
        transcript = new ArrayList();
    }

    /**
     * Adds a new Line to the Transcript.
     * @param line the line to add to the transcript.
     */
    public void addLine(Line line) {
        transcript.add(line);
    }

    /**
     * Returns the current Chat Transcript.
     * @return the current Chat Transcript.
     */
    public List getTranscript() {
        return transcript;
    }
}