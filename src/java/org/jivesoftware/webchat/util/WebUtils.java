/**
 * $RCSFile$
 * $Revision$
 * $Date: 2006-01-10 14:14:08 -0800 (Tue, 10 Jan 2006) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.util;

import org.jivesoftware.webchat.filter.EmoticonFilter;
import org.jivesoftware.webchat.filter.TextStyleFilter;
import org.jivesoftware.webchat.filter.URLFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.util.Date;

/**
 * The <code>WebUtils</code> class is a utility class for some of the most
 * mundane procedures in the WebChat client and in servlet programming. This
 * will be moved over to a more suitable class down the line. Probably be handled
 * in the org.jivesoftware.web.utils package to be used throughout Jive.
 */
public final class WebUtils {
    private WebUtils() {
    }

    /**
     * Check to see if string has been assigned a value. This is generally used
     * in web applications/applet when a user request a parameter from the parameter stack.
     *
     * @param str - the string to check.
     * @return true if String has been assigned a value, false otherwise.
     */
    public static final boolean isNotNull(String str) {
        if (str != null && str.trim().length() > 0) {
            return true;
        }

        return false;
    }

    /**
     * Checks to see if the String is boolean value and will return the appropriate
     * value
     *
     * @param str - the String to check
     * @return true if the string is not null and the value is equal to true, false
     *         otherwise.
     */
    public static final boolean isTrue(String str) {
        return (str != null && "true".equalsIgnoreCase(str));
    }


    public static final boolean isAvailable(Presence presence) {
        return (presence != null && presence.getType() == Presence.Type.available);
    }

    /**
     * Checks to see if the data is applicable to be added to metadata.
     *
     * @param data the data to check for validity.
     * @return true if the data is valid.
     */
    public static boolean isValidData(String data) {
        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isLetterOrDigit(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies all text filters to the given text.
     *
     * @param body the body of text to filter.
     * @return the given string with all filters applied.
     */
    public static String applyFilters(String body) {
        // Encode the HTML special characters
        body = replace(body, "&", "&amp;");
        body = replace(body, "<", "&lt;");
        body = replace(body, ">", "&gt;");
        body = replace(body, "\n", "<br>");
        body = replace(body, "\"", "&quot;");

        // Apply other filters.
        body = TextStyleFilter.applyFilter(body);
        body = URLFilter.applyFilter(body);
        body = EmoticonFilter.applyFilter(body);

        return body;
    }

    /**
     * Replaces all instances of oldString with newString in string.
     *
     * @param string    the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @return a String will all instances of oldString replaced by newString
     */
    public static final String replace(String string, String oldString, String newString) {
        if (string == null) {
            return null;
        }
        // If the newString is null or zero length, just return the string since there's nothing
        // to replace.
        if (newString == null) {
            return string;
        }
        int i = 0;
        // Make sure that oldString appears at least once before doing any processing.
        if ((i = string.indexOf(oldString, i)) >= 0) {
            // Use char []'s, as they are more efficient to deal with.
            char[] string2 = string.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(string2.length);
            buf.append(string2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            // Replace all remaining instances of oldString with newString.
            while ((i = string.indexOf(oldString, i)) > 0) {
                buf.append(string2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(string2, j, string2.length - j);
            return buf.toString();
        }
        return string;
    }

    /**
     * Validate the given text - to pass it must contain letters, digits, '@', '-', '_', '.', ','
     * or a space character.
     *
     * @param text the text to check
     * @return true if the given text is valid, false otherwise.
     */
    public boolean validateChars(String text) {
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (!Character.isLetterOrDigit(ch) && ch != '@' && ch != '-' && ch != '_'
                    && ch != '.' && ch != ',' && ch != ' ') {
                return false;
            }
        }
        return true;
    }


    public static String getPushedURL(String body) {
        String urlToPush = null;
        int index = body.indexOf("]");
        urlToPush = body.substring(index + 1);
        int index2 = urlToPush.indexOf("http://");
        int httpsIndex = urlToPush.indexOf("https");
        if (index2 == -1 && httpsIndex == -1) {
            urlToPush = "http://" + urlToPush;
        }

        return urlToPush;
    }

    /**
     * Returns the nickname of the user who sent the message.
     *
     * @param message the message sent.
     * @return the nickname of the user who sent the message.
     */
    public static String getNickname(Message message) {
        String from = org.jivesoftware.smack.util.StringUtils.parseResource(message.getFrom());
        return from;
    }

    /**
     * Returns better looking time String.
     * @param seconds the number of seconds to calculate.
     * @return the formatted time.
     */
    public static String getTimeFromLong(long seconds) {
        final String HOURS = "h";
        final String MINUTES = "min";
        final String SECONDS = "sec";

        final long MS_IN_A_DAY = 1000 * 60 * 60 * 24;
        final long MS_IN_AN_HOUR = 1000 * 60 * 60;
        final long MS_IN_A_MINUTE = 1000 * 60;
        final long MS_IN_A_SECOND = 1000;
        Date currentTime = new Date();
        long numDays = seconds / MS_IN_A_DAY;
        seconds = seconds % MS_IN_A_DAY;
        long numHours = seconds / MS_IN_AN_HOUR;
        seconds = seconds % MS_IN_AN_HOUR;
        long numMinutes = seconds / MS_IN_A_MINUTE;
        seconds = seconds % MS_IN_A_MINUTE;
        long numSeconds = seconds / MS_IN_A_SECOND;
        seconds = seconds % MS_IN_A_SECOND;
        long numMilliseconds = seconds;

        StringBuffer buf = new StringBuffer();
        if (numHours > 0) {
            buf.append(numHours + " " + HOURS + ", ");
        }

        if (numMinutes > 0) {
            buf.append(numMinutes + " " + MINUTES);
        }

        String result = buf.toString();

        if (numMinutes < 1) {
            result = "less than 1 minute";
        }

        return result;
    }


}