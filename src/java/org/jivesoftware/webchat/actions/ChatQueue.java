/**
 * $RCSFile$
 * $Revision: 18903 $
 * $Date: 2005-05-11 18:03:06 -0700 (Wed, 11 May 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.actions;

import org.jivesoftware.webchat.util.WebUtils;

/**
 * The ChatQueue for a particular user. This is the model implementation of the current state in a queue.
 * Updates such information as position, average wait time, and total wait time.
 */
public class ChatQueue {
    private boolean connectionDropped;
    private boolean inQueue;
    private boolean routed;
    private int queuePosition;
    private int queueTime;
    private String nickname;


    public boolean isConnectionDropped() {
        return connectionDropped;
    }

    public void setConnectionDropped(boolean connectionDropped) {
        this.connectionDropped = connectionDropped;
    }

    public boolean isInQueue() {
        return inQueue;
    }

    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }

    public boolean isRouted() {
        return routed;
    }

    public void setRouted(boolean routed) {
        this.routed = routed;
    }

    public int getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(int queuePosition) {
        this.queuePosition = queuePosition;
    }

    public int getQueueTime() {
        return queueTime;
    }

    public String getQueueTimeForHtml() {
        return WebUtils.getTimeFromLong(getQueueTime());
    }

    public void setQueueTime(int queueTime) {
        this.queueTime = queueTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
