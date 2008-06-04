/**
 * $RCSfile$
 * $Revision: 18903 $
 * $Date: 2005-05-11 18:03:06 -0700 (Wed, 11 May 2005) $
 *
 * Copyright (C) 2003-2008 Jive Software. All rights reserved.
 *
 * This software is the proprietary information of Jive Software. Use is subject to license terms.
 */

package org.jivesoftware.webchat.filter;

import org.jivesoftware.webchat.util.ModelUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * A Filter that converts ASCII emoticons into image equivalents.
 * This filter should only be run after any HTML stripping filters.<p>
 *
 * The filter must be configured with information about where the image files
 * are located. A table containing all the supported emoticons with their
 * ASCII representations and image file names is as follows:<p>
 *
 * <table border=1>
 * <tr><td><b>Emotion</b></td><td><b>ASCII</b></td><td><b>Image</b></td></tr>
 * <p/>
 * <tr><td>Happy</td><td>:) or :-)</td><td>happy.gif</td></tr>
 * <tr><td>Sad</td><td>:( or :-(</td><td>sad.gif</td></tr>
 * <tr><td>Grin</td><td>:D</td><td>grin.gif</td></tr>
 * <tr><td>Love</td><td>:x</td><td>love.gif</td></tr>
 * <tr><td>Mischief</td><td>;\</td><td>mischief.gif</td></tr>
 * <tr><td>Cool</td><td>B-)</td><td>cool.gif</td></tr>
 * <tr><td>Devil</td><td>]:)</td><td>devil.gif</td></tr>
 * <tr><td>Silly</td><td>:p</td><td>silly.gif</td></tr>
 * <tr><td>Angry</td><td>X-(</td><td>angry.gif</td></tr>
 * <tr><td>Laugh</td><td>:^O</td><td>laugh.gif</td></tr>
 * <tr><td>Wink</td><td>;) or ;-)</td><td>wink.gif</td></tr>
 * <tr><td>Blush</td><td>:8}</td><td>blush.gif</td></tr>
 * <tr><td>Cry</td><td>:_|</td><td>cry.gif</td></tr>
 * <tr><td>Confused</td><td>?:|</td><td>confused.gif</td></tr>
 * <tr><td>Shocked</td><td>:O</td><td>shocked.gif</td></tr>
 * <tr><td>Plain</td><td>:|</td><td>plain.gif</td></tr>
 * </table>
 */
public class EmoticonFilter {
    private static final Map EMOTICON_MAP = new HashMap();

    private EmoticonFilter() {
        // Not instantiable.
    }

    /**
     * Applys the emoticon filter to a string. For example, if you wanted the
     * actual graphic for :) :<p>
     * <pre>
     * String graphic = EmoticonFilter.applyFilter(":)");
     * </pre>
     * </p>
     *
     * You would receive images/emoticons/happy.gif.
     * @param string the string to parse for emoticon images.
     * @return the emoticon image link.
     */
    public static String applyFilter(String string) {
        if (!ModelUtil.hasLength(string)) {
            return string;
        }

        final StringBuffer buf = new StringBuffer();
        final StringTokenizer tkn = new StringTokenizer(string, " ", false);
        while (tkn.hasMoreTokens()) {
            String str = tkn.nextToken();
            String found = (String) EMOTICON_MAP.get(str);
            if (found != null) {
                str = buildURL(found);
            }
            buf.append(str + " ");
        }
        return buf.toString();
    }

    /**
     * Build image tags
     */
    static {
        EMOTICON_MAP.put(":)", "images/emoticons/happy.gif");
        EMOTICON_MAP.put(":-)", "images/emoticons/happy.gif");
        EMOTICON_MAP.put(":(", "images/emoticons/sad.gif");
        EMOTICON_MAP.put(":-(", "images/emoticons/sad.gif");
        EMOTICON_MAP.put(":D", "images/emoticons/grin.gif");
        EMOTICON_MAP.put(":x", "images/emoticons/love.gif");
        EMOTICON_MAP.put(";\\", "images/emoticons/mischief.gif");
        EMOTICON_MAP.put("B-)", "images/emoticons/cool.gif");
        EMOTICON_MAP.put("]:)", "images/emoticons/devil.gif");
        EMOTICON_MAP.put(":p", "images/emoticons/silly.gif");
        EMOTICON_MAP.put("X-(", "images/emoticons/angry.gif");
        EMOTICON_MAP.put(":^0", "images/emoticons/laugh.gif");
        EMOTICON_MAP.put(";)", "images/emoticons/wink.gif");
        EMOTICON_MAP.put(";-)", "images/emoticons/wink.gif");
        EMOTICON_MAP.put(":8}", "images/emoticons/blush.gif");
        EMOTICON_MAP.put(":_|", "images/emoticons/cry.gif");
        EMOTICON_MAP.put("?:|", "images/emoticons/confused.gif");
        EMOTICON_MAP.put(":0", "images/emoticons/shocked.gif");
        EMOTICON_MAP.put(":|", "images/emoticons/plain.gif");
    }

    /**
     * Returns an HTML image tag using the base image URL and image name.
     * @param imageName the relative url of the image to build.
     * @return the new img tag to use.
     */
    private static String buildURL(String imageName) {
        return "<img border=\"0\" src=\"" + imageName + "\">";
    }
}
