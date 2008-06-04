/**
 * $RCSFile$
 * $Revision: 18401 $
 * $Date: 2005-02-05 10:18:52 -0800 (Sat, 05 Feb 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to peform common String manipulation algorithms.
 */
public class StringUtils {

    // Constants used by escapeHTMLTags
    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();
    private static final char[] LT_ENCODE = "&lt;".toCharArray();
    private static final char[] GT_ENCODE = "&gt;".toCharArray();

    // patterns for the email address checks
    private static Pattern basicAddressPattern;
    private static Pattern validUserPattern;
    private static Pattern domainPattern;
    private static Pattern ipDomainPattern;
    private static Pattern tldPattern;

    // prepare the patterns
    static {
        // constants used in the parsing of email addresses
        String basicAddress = "^([\\w\\.-]+)@([\\w\\.-]+)$";
        String specialChars = "\\(\\)><@,;:\\\\\\\"\\.\\[\\]";
        String validChars = "[^ \f\n\r\t" + specialChars + "]";
        String atom = validChars + "+";
        String quotedUser = "(\"[^\"]+\")";
        String word = "(" + atom + "|" + quotedUser + ")";
        String validUser = "^" + word + "(\\." + word + ")*$";
        String domain = "^" + atom + "(\\." + atom + ")+$";
        String ipDomain = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
        // from http://www.icann.org/tlds/
        String knownTLDs = "^\\.(com|net|org|edu|int|mil|gov|arpa|biz|aero|name|coop|info|pro|museum)$";


        basicAddressPattern = Pattern.compile(basicAddress, Pattern.CASE_INSENSITIVE);
        validUserPattern = Pattern.compile(validUser, Pattern.CASE_INSENSITIVE);
        domainPattern = Pattern.compile(domain, Pattern.CASE_INSENSITIVE);
        ipDomainPattern = Pattern.compile(ipDomain, Pattern.CASE_INSENSITIVE);
        tldPattern = Pattern.compile(knownTLDs, Pattern.CASE_INSENSITIVE);
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
     * Replaces all instances of oldString with newString in line with the
     * added feature that matches of newString in oldString ignore case.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @return a String will all instances of oldString replaced by newString
     */
    public static final String replaceIgnoreCase(String line, String oldString, String newString) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    /**
     * Replaces all instances of oldString with newString in line with the
     * added feature that matches of newString in oldString ignore case.
     * The count paramater is set to the number of replaces performed.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @param count     a value that will be updated with the number of replaces
     *                  performed.
     * @return a String will all instances of oldString replaced by newString
     */
    public static final String replaceIgnoreCase(String line, String oldString,
                                                 String newString, int[] count) {
        if (line == null) {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
            int counter = 1;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * Replaces all instances of oldString with newString in line.
     * The count Integer is updated with number of replaces.
     *
     * @param line      the String to search to perform replacements on
     * @param oldString the String that should be replaced by newString
     * @param newString the String that will replace all instances of oldString
     * @return a String will all instances of oldString replaced by newString
     */
    public static final String replace(String line, String oldString,
                                       String newString, int[] count) {
        if (line == null) {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0) {
            int counter = 1;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0) {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * This method takes a string and strips out all tags except <br> tags while still leaving
     * the tag body intact.
     *
     * @param in the text to be converted.
     * @return the input string with all tags removed.
     */
    public static final String stripTags(String in) {
        if (in == null) {
            return null;
        }

        return stripTags(in, false);
    }

    /**
     * This method takes a string and strips out all tags while still leaving
     * the tag body intact.
     *
     * @param in the text to be converted.
     * @return the input string with all tags removed.
     */
    public static final String stripTags(String in, boolean stripBRTag) {
        if (in == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = in.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
                continue;
            }
            else if (ch == '<') {
                if (!stripBRTag && i + 3 < len && input[i + 1] == 'b' && input[i + 2] == 'r' && input[i + 3] == '>') {
                    i += 3;
                    continue;
                }
                if (i > last) {
                    if (last > 0) {
                        out.append(" ");
                    }
                    out.append(input, last, i - last);
                }
                last = i + 1;
            }
            else if (ch == '>') {
                last = i + 1;
            }
        }
        if (last == 0) {
            return in;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
     * &lt;table&gt;, etc) and converts the '&lt'' and '&gt;' characters to
     * their HTML escape sequences.
     *
     * @param in the text to be converted.
     * @return the input string with the characters '&lt;' and '&gt;' replaced
     *         with their HTML escape sequences.
     */
    public static final String escapeHTMLTags(String in) {
        if (in == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = in.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];
            if (ch > '>') {
                continue;
            }
            else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            }
            else if (ch == '>') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            }
            else if (ch == '"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            }
        }
        if (last == 0) {
            return in;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * Used by the hash method.
     */
    private static MessageDigest digest = null;

    /**
     * Hashes a String using the Md5 algorithm and returns the result as a
     * String of hexadecimal numbers. This method is synchronized to avoid
     * excessive MessageDigest object creation. If calling this method becomes
     * a bottleneck in your code, you may wish to maintain a pool of
     * MessageDigest objects instead of using this method.
     * <p/>
     * A hash is a one-way function -- that is, given an
     * input, an output is easily computed. However, given the output, the
     * input is almost impossible to compute. This is useful for passwords
     * since we can store the hash and a hacker will then have a very hard time
     * determining the original password.
     * <p/>
     * In Jive, every time a user logs in, we simply
     * take their plain text password, compute the hash, and compare the
     * generated hash to the stored hash. Since it is almost impossible that
     * two passwords will generate the same hash, we know if the user gave us
     * the correct password or not. The only negative to this system is that
     * password recovery is basically impossible. Therefore, a reset password
     * method is used instead.
     *
     * @param data the String to compute the hash of.
     * @return a hashed version of the passed-in String
     */
    public synchronized static final String hash(String data) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            }
            catch (NoSuchAlgorithmException nsae) {
            }
        }
        // Now, compute hash.
        try {
            digest.update(data.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e) {
        }
        return encodeHex(digest.digest());
    }

    /**
     * Turns an array of bytes into a String representing each byte as an
     * unsigned hex number.
     * <p/>
     * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
     * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
     * Distributed under LGPL.
     *
     * @param bytes an array of bytes to convert to a hex-string
     * @return generated hex string
     */
    public static final String encodeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        int i;

        for (i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * Turns a hex encoded string into a byte array. It is specifically meant
     * to "reverse" the toHex(byte[]) method.
     *
     * @param hex a hex encoded String to transform into a byte array.
     * @return a byte array representing the hex String[
     */
    public static final byte[] decodeHex(String hex) {
        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += 2) {
            int newByte = 0x00;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = (byte) newByte;
            byteCount++;
        }
        return bytes;
    }

    /**
     * Returns the the byte value of a hexadecmical char (0-f). It's assumed
     * that the hexidecimal chars are lower case as appropriate.
     *
     * @param ch a hexedicmal character (0-f)
     * @return the byte value of the character (0x00-0x0F)
     */
    private static final byte hexCharToByte(char ch) {
        switch (ch) {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case 'a':
                return 0x0A;
            case 'b':
                return 0x0B;
            case 'c':
                return 0x0C;
            case 'd':
                return 0x0D;
            case 'e':
                return 0x0E;
            case 'f':
                return 0x0F;
        }
        return 0x00;
    }

    //*********************************************************************
    //* Base64 - a simple base64 encoder and decoder.
    //*
    //*     Copyright (c) 1999, Bob Withers - bwit@pobox.com
    //*
    //* This code may be freely used for any purpose, either personal
    //* or commercial, provided the authors copyright notice remains
    //* intact.
    //*********************************************************************

    /**
     * Encodes a String as a base64 String.
     *
     * @param data a String to encode.
     * @return a base64 encoded String.
     */
    public static String encodeBase64(String data) {
        byte[] bytes = null;
        try {
            bytes = data.getBytes("ISO-8859-1");
        }
        catch (UnsupportedEncodingException uee) {
        }
        return encodeBase64(bytes);
    }

    /**
     * Encodes a byte array into a base64 String.
     *
     * @param data a byte array to encode.
     * @return a base64 encode String.
     */
    public static String encodeBase64(byte[] data) {
        int c;
        int len = data.length;
        StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
        for (int i = 0; i < len; ++i) {
            c = (data[i] >> 2) & 0x3f;
            ret.append(cvt.charAt(c));
            c = (data[i] << 4) & 0x3f;
            if (++i < len)
                c |= (data[i] >> 4) & 0x0f;

            ret.append(cvt.charAt(c));
            if (i < len) {
                c = (data[i] << 2) & 0x3f;
                if (++i < len)
                    c |= (data[i] >> 6) & 0x03;

                ret.append(cvt.charAt(c));
            }
            else {
                ++i;
                ret.append((char) fillchar);
            }

            if (i < len) {
                c = data[i] & 0x3f;
                ret.append(cvt.charAt(c));
            }
            else {
                ret.append((char) fillchar);
            }
        }
        return ret.toString();
    }

    /**
     * Decodes a base64 String.
     *
     * @param data a base64 encoded String to decode.
     * @return the decoded String.
     */
    public static String decodeBase64(String data) {
        byte[] bytes = null;
        try {
            bytes = data.getBytes("ISO-8859-1");
        }
        catch (UnsupportedEncodingException uee) {
        }
        return decodeBase64(bytes);
    }

    /**
     * Decodes a base64 aray of bytes.
     *
     * @param data a base64 encode byte array to decode.
     * @return the decoded String.
     */
    public static String decodeBase64(byte[] data) {
        int c, c1;
        int len = data.length;
        StringBuffer ret = new StringBuffer((len * 3) / 4);
        for (int i = 0; i < len; ++i) {
            c = cvt.indexOf(data[i]);
            ++i;
            c1 = cvt.indexOf(data[i]);
            c = ((c << 2) | ((c1 >> 4) & 0x3));
            ret.append((char) c);
            if (++i < len) {
                c = data[i];
                if (fillchar == c)
                    break;

                c = cvt.indexOf(c);
                c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
                ret.append((char) c1);
            }

            if (++i < len) {
                c1 = data[i];
                if (fillchar == c1)
                    break;

                c1 = cvt.indexOf(c1);
                c = ((c << 6) & 0xc0) | c1;
                ret.append((char) c);
            }
        }
        return ret.toString();
    }

    /**
     * The method below is under the following license
     * <p/>
     * ====================================================================
     * <p/>
     * The Apache Software License, Version 1.1
     * <p/>
     * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
     * reserved.
     * <p/>
     * Redistribution and use in source and binary forms, with or without
     * modification, are permitted provided that the following conditions
     * are met:
     * <p/>
     * 1. Redistributions of source code must retain the above copyright
     * notice, this list of conditions and the following disclaimer.
     * <p/>
     * 2. Redistributions in binary form must reproduce the above copyright
     * notice, this list of conditions and the following disclaimer in
     * the documentation and/or other materials provided with the
     * distribution.
     * <p/>
     * 3. The end-user documentation included with the redistribution, if
     * any, must include the following acknowlegement:
     * "This product includes software developed by the
     * Apache Software Foundation (http://www.apache.org/)."
     * Alternately, this acknowlegement may appear in the software itself,
     * if and wherever such third-party acknowlegements normally appear.
     * <p/>
     * 4. The names "The Jakarta Project", "Commons", and "Apache Software
     * Foundation" must not be used to endorse or promote products derived
     * from this software without prior written permission. For written
     * permission, please contact apache@apache.org.
     * <p/>
     * 5. Products derived from this software may not be called "Apache"
     * nor may "Apache" appear in their names without prior written
     * permission of the Apache Group.
     * <p/>
     * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
     * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
     * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
     * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
     * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
     * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
     * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
     * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
     * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
     * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
     * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
     * SUCH DAMAGE.
     * ====================================================================
     * <p/>
     * This software consists of voluntary contributions made by many
     * individuals on behalf of the Apache Software Foundation.  For more
     * information on the Apache Software Foundation, please see
     * <http://www.apache.org/>.
     */
    private static final BitSet allowed_query = new BitSet(256);

    static {
        for (int i = '0'; i <= '9'; i++) {
            allowed_query.set(i);
        }

        for (int i = 'a'; i <= 'z'; i++) {
            allowed_query.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            allowed_query.set(i);
        }

        allowed_query.set('-');
        allowed_query.set('_');
        allowed_query.set('.');
        allowed_query.set('!');
        allowed_query.set('~');
        allowed_query.set('*');
        allowed_query.set('\'');
        allowed_query.set('(');
        allowed_query.set(')');
    }

    /**
     * Encodes URI string. This is a replacement for the java.net.URLEncode#encode(String, String)
     * class which is broken under JDK 1.3.
     * <p/>
     *
     * @param original the original character sequence
     * @param charset  the protocol charset
     * @return URI character sequence
     * @throws UnsupportedEncodingException unsupported character encoding
     */
    public static String URLEncode(String original, String charset)
            throws UnsupportedEncodingException {
        // encode original to uri characters.
        if (original == null) {
            return null;
        }

        // escape octet to uri characters.
        byte[] octets;

        try {
            octets = original.getBytes(charset);
        }
        catch (UnsupportedEncodingException error) {
            throw new UnsupportedEncodingException();
        }

        StringBuffer buf = new StringBuffer(octets.length);

        for (int i = 0; i < octets.length; i++) {
            char c = (char) octets[i];
            if (allowed_query.get(c)) {
                buf.append(c);
            }
            else {
                buf.append('%');
                byte b = octets[i]; // use the original byte value
                char hexadecimal = Character.forDigit((b >> 4) & 0xF, 16);
                buf.append(Character.toUpperCase(hexadecimal)); // high
                hexadecimal = Character.forDigit(b & 0xF, 16);
                buf.append(Character.toUpperCase(hexadecimal)); // low
            }
        }

        return buf.toString();
    }

    private static final int fillchar = '=';
    private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "0123456789+/";

    /**
     * Converts a line of text into an array of lower case words using a
     * BreakIterator.wordInstance(). <p>
     * <p/>
     * This method is under the Jive Open Source Software License and was
     * written by Mark Imbriaco.
     *
     * @param text a String of text to convert into an array of words
     * @return text broken up into an array of words.
     */
    public static final String[] toLowerCaseWordArray(String text) {
        if (text == null || text.length() == 0) {
            return new String[0];
        }

        ArrayList wordList = new ArrayList();
        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);
        int start = 0;

        for (int end = boundary.next(); end != BreakIterator.DONE;
             start = end, end = boundary.next()) {
            String tmp = text.substring(start, end).trim();
            // Remove characters that are not needed.
            tmp = replace(tmp, "+", "");
            tmp = replace(tmp, "/", "");
            tmp = replace(tmp, "\\", "");
            tmp = replace(tmp, "#", "");
            tmp = replace(tmp, "*", "");
            tmp = replace(tmp, ")", "");
            tmp = replace(tmp, "(", "");
            tmp = replace(tmp, "&", "");
            if (tmp.length() > 0) {
                wordList.add(tmp);
            }
        }
        return (String[]) wordList.toArray(new String[wordList.size()]);
    }

    /**
     * Pseudo-random number generator object for use with randomString().
     * The Random class is not considered to be cryptographically secure, so
     * only use these random Strings for low to medium security applications.
     */
    private static Random randGen = new Random();

    /**
     * Array of numbers and letters of mixed case. Numbers appear in the list
     * twice so that there is a more equal chance that a number will be picked.
     * We can use the array to get a random number or letter by picking a random
     * array index.
     */
    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    /**
     * Returns a random String of numbers and letters (lower and upper case)
     * of the specified length. The method uses the Random class that is
     * built-in to Java which is suitable for low to medium grade security uses.
     * This means that the output is only pseudo random, i.e., each number is
     * mathematically generated so is not truly random.<p>
     * <p/>
     * The specified length must be at least one. If not, the method will return
     * null.
     *
     * @param length the desired length of the random String to return.
     * @return a random String of numbers and letters of the specified length.
     */
    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    /**
     * Intelligently chops a String at a word boundary (whitespace) that occurs
     * at the specified index in the argument or before. However, if there is a
     * newline character before <code>length</code>, the String will be chopped
     * there. If no newline or whitespace is found in <code>string</code> up to
     * the index <code>length</code>, the String will chopped at <code>length</code>.
     * <p/>
     * For example, chopAtWord("This is a nice String", 10, -1) will return
     * "This is a" which is the first word boundary less than or equal to 10
     * characters into the original String.
     *
     * @param string    the String to chop.
     * @param length    the index in <code>string</code> to start looking for a
     *                  whitespace boundary at.
     * @param minLength the minimum length the word should be chopped at. This is helpful
     *                  for words with no natural boundaries, ie: "thisisareallylonglonglongword".
     *                  This must be smaller than length and can be -1 if no minLength is wanted
     * @return a substring of <code>string</code> whose length is less than or
     *         equal to <code>length</code>, and that is chopped at whitespace.
     */
    public static final String chopAtWord(String string, int length, int minLength) {
        // guard clauses
        if (length < 2) {
            throw new IllegalArgumentException("Length specified (" + length + ") must be > 2");
        }
        else if (minLength >= length) {
            throw new IllegalArgumentException("minLength must be smaller than length");
        }

        int sLength = (string == null) ? -1 : string.length();

        // shortcircuit clauses
        if (sLength < 1) {
            return string;
        }
        // minLength specified, string is smaller than the minLength, return the string
        else if (minLength != -1 && sLength < minLength) {
            return string;
        }
        // no minLength specified, string is smaller than length
        else if (minLength == -1 && sLength < length) {
            return string;
        }

        char[] charArray = string.toCharArray();

        // String is longer than the length specified, attempt to find a newline
        // or a space
        if (sLength > length) {
            sLength = length;

            // First check if there is a newline character before length; if so,
            // chop word there.
            for (int i = 0; i < sLength - 1; i++) {
                // Windows
                if (charArray[i] == '\r' && charArray[i + 1] == '\n') {
                    return string.substring(0, i + 1);
                }
                // Unix
                else if (charArray[i] == '\n') {
                    return string.substring(0, i);
                }
            }
            // Also check boundary case of Unix newline
            if (charArray[sLength - 1] == '\n') {
                return string.substring(0, sLength - 1);
            }

            // No newline, so chop at the first whitespace.
            for (int i = sLength - 1; i > 0; i--) {
                if (charArray[i] == ' ') {
                    return string.substring(0, i).trim();
                }
            }
        }
        // String is shorter than length but longer than minLength,
        // make sure there is a space in the string before minLength
        else if (minLength != -1 && sLength > minLength) {
            for (int i = 0; i < minLength; i++) {
                if (charArray[i] == ' ') {
                    return string;
                }
            }
        }

        // Did not find a word boundary, so return a string at the min length, if a min
        // length was specified:
        if (minLength > -1 && minLength <= string.length()) {
            return string.substring(0, minLength);
        }

        // Did not find word boundary or min length so return original String chopped at
        // specified length.
        return string.substring(0, length);
    }

    /**
     * Intelligently chops a String at a word boundary (whitespace) that occurs
     * at the specified index in the argument or before. However, if there is a
     * newline character before <code>length</code>, the String will be chopped
     * there. If no newline or whitespace is found in <code>string</code> up to
     * the index <code>length</code>, the String will chopped at <code>length</code>.
     * <p/>
     * For example, chopAtWord("This is a nice String", 10) will return
     * "This is a" which is the first word boundary less than or equal to 10
     * characters into the original String.
     *
     * @param string the String to chop.
     * @param length the index in <code>string</code> to start looking for a
     *               whitespace boundary at.
     * @return a substring of <code>string</code> whose length is less than or
     *         equal to <code>length</code>, and that is chopped at whitespace.
     */
    public static final String chopAtWord(String string, int length) {
        return chopAtWord(string, length, -1);
    }

    /**
     * Returns a substring of the given string which represents the words around the given word.
     * For example, passing in "This is a quick test a test", "{a,test}" and 5 would return a string
     * of "This is a quick" - that's 5 characters (or to the end of the word, whichever
     * is greater) on either side of "a". Also, since {a,test} is passed in a "a" is found
     * first in the string, we base the substring off of the position of "a". The wordList is
     * really just a list of strings to try - the first one found is used.<p>
     * <p/>
     * Note: The wordList passed in should be lowercase.
     *
     * @param input    The string to parse.
     * @param wordList The words to look for - the first one found in the string is used.
     * @param numChars The number of characters on either side to include in the chop.
     * @return a substring of the given string matching the criteria, otherwise "".
     */
    public static String chopAtWordsAround(String input, String[] wordList, int numChars) {
        if (input == null || "".equals(input.trim()) || wordList == null
                || wordList.length == 0 || numChars == 0) {
            return "";
        }
        String lc = input.toLowerCase();
        for (int i = 0; i < wordList.length; i++) {
            int pos = lc.indexOf(wordList[i]);
            if (pos > -1) {
                int beginIdx = pos - numChars;
                if (beginIdx < 0) {
                    beginIdx = 0;
                }
                int endIdx = pos + numChars;
                if (endIdx > input.length() - 1) {
                    endIdx = input.length() - 1;
                }
                char[] chars = input.toCharArray();
                while (beginIdx > 0 && chars[beginIdx] != ' ' && chars[beginIdx] != '\n'
                        && chars[beginIdx] != '\r') {
                    beginIdx--;
                }
                while (endIdx < input.length() && chars[endIdx] != ' '
                        && chars[endIdx] != '\n' && chars[endIdx] != '\r') {
                    endIdx++;
                }
                return input.substring(beginIdx, endIdx);
            }
        }
        return input.substring(0, (input.length() >= 200) ? 200 : input.length());
    }

    /**
     * Reformats a string where lines that are longer than <tt>width</tt>
     * are split apart at the earliest wordbreak or at maxLength, whichever is
     * sooner. If the width specified is less than 5 or greater than the input
     * Strings length the string will be returned as is.
     * <p/>
     * Please note that this method can be lossy - trailing spaces on wrapped
     * lines may be trimmed.
     *
     * @param input the String to reformat.
     * @param width the maximum length of any one line.
     * @return a new String with reformatted as needed.
     */
    public static String wordWrap(String input, int width, Locale locale) {
        // protect ourselves
        if (input == null) {
            return "";
        }
        else if (width < 5) {
            return input;
        }
        else if (width >= input.length()) {
            return input;
        }


        StringBuffer buf = new StringBuffer(input);
        boolean endOfLine = false;
        int lineStart = 0;

        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '\n') {
                lineStart = i + 1;
                endOfLine = true;
            }

            // handle splitting at width character
            if (i > lineStart + width - 1) {
                if (!endOfLine) {
                    int limit = i - lineStart - 1;
                    BreakIterator breaks = BreakIterator.getLineInstance(locale);
                    breaks.setText(buf.substring(lineStart, i));
                    int end = breaks.last();

                    // if the last character in the search string isn't a space,
                    // we can't split on it (looks bad). Search for a previous
                    // break character
                    if (end == limit + 1) {
                        if (!Character.isWhitespace(buf.charAt(lineStart + end))) {
                            end = breaks.preceding(end - 1);
                        }
                    }

                    // if the last character is a space, replace it with a \n
                    if (end != BreakIterator.DONE && end == limit + 1) {
                        buf.replace(lineStart + end, lineStart + end + 1, "\n");
                        lineStart = lineStart + end;
                    }
                    // otherwise, just insert a \n
                    else if (end != BreakIterator.DONE && end != 0) {
                        buf.insert(lineStart + end, '\n');
                        lineStart = lineStart + end + 1;
                    }
                    else {
                        buf.insert(i, '\n');
                        lineStart = i + 1;
                    }
                }
                else {
                    buf.insert(i, '\n');
                    lineStart = i + 1;
                    endOfLine = false;
                }
            }
        }

        return buf.toString();
    }

    /**
     * Highlights words in a string. Words matching ignores case. The actual
     * higlighting method is specified with the start and end higlight tags.
     * Those might be beginning and ending HTML bold tags, or anything else.<p>
     * <p/>
     * This method is under the Jive Open Source Software License and was
     * written by Mark Imbriaco.
     *
     * @param string         the String to highlight words in.
     * @param words          an array of words that should be highlighted in the string.
     * @param startHighlight the tag that should be inserted to start highlighting.
     * @param endHighlight   the tag that should be inserted to end highlighting.
     * @return a new String with the specified words highlighted.
     */
    public static final String highlightWords(String string, String[] words,
                                              String startHighlight, String endHighlight) {
        if (string == null || words == null || startHighlight == null || endHighlight == null) {
            return null;
        }

        StringBuffer regexp = new StringBuffer();
        regexp.append("(?i)\\b(");

        // Iterate through each word and generate a word list for the regexp.
        for (int x = 0; x < words.length; x++) {
            // Escape "$", "|", ".", "/", and "?" to keep us out of trouble in our regexp.
            words[x] = words[x].replaceAll("([\\$\\?\\|\\/\\.])", "\\\\$1");
            regexp.append(words[x]);

            if (x != words.length - 1) {
                regexp.append("|");
            }
        }
        regexp.append(")");

        return string.replaceAll(regexp.toString(), startHighlight + "$1" + endHighlight);
    }

    /**
     * Escapes all necessary characters in the String so that it can be used
     * in an XML doc.
     *
     * @param string the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static final String escapeForXML(String string) {
        if (string == null) {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++) {
            ch = input[i];

            if (ch > '>') {
                continue;
            }
            else if (ch == '<') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            }
            else if (ch == '&') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(AMP_ENCODE);
            }
            else if (ch == '"') {
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            }
            else if (ch == 10 || ch == 13 || ch == 9) {
                continue;
            }
            else if (ch < 32) {
                // Disallow all ASCII control characters, except space,
                // enter characters and tabs:
                if (i > last) {
                    out.append(input, last, i - last);
                }
                last = i + 1;
            }
        }
        if (last == 0) {
            return string;
        }
        if (i > last) {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * Unescapes the String by converting XML escape sequences back into normal
     * characters.
     *
     * @param string the string to unescape.
     * @return the string with appropriate characters unescaped.
     */
    public static final String unescapeFromXML(String string) {
        string = replace(string, "&lt;", "<");
        string = replace(string, "&gt;", ">");
        string = replace(string, "&quot;", "\"");
        return replace(string, "&amp;", "&");
    }

    private static final char[] zeroArray =
            "0000000000000000000000000000000000000000000000000000000000000000".toCharArray();

    /**
     * Pads the supplied String with 0's to the specified length and returns
     * the result as a new String. For example, if the initial String is
     * "9999" and the desired length is 8, the result would be "00009999".
     * This type of padding is useful for creating numerical values that need
     * to be stored and sorted as character data. Note: the current
     * implementation of this method allows for a maximum <tt>length</tt> of
     * 64.
     *
     * @param string the original String to pad.
     * @param length the desired length of the new padded String.
     * @return a new String padded with the required number of 0's.
     */
    public static final String zeroPadString(String string, int length) {
        if (string == null || string.length() > length) {
            return string;
        }
        StringBuffer buf = new StringBuffer(length);
        buf.append(zeroArray, 0, length - string.length()).append(string);
        return buf.toString();
    }

    /**
     * Formats a Date as a String. Depending on how Dates are defined in the database
     * (character data or numberic), the format return will either be a fifteen character long String
     * made up of the Date's padded millisecond value, or will simply be the Date's millesecond value.
     *
     * @return a Date encoded as a String.
     */
    public static final String dateToMillis(Date date) {
        return Long.toString(date.getTime());
    }

    /**
     * Validate an email address. This isn't 100% perfect but should handle just about everything
     * that is in common use.
     *
     * @param addr the email address to validate
     * @return true if the address is valid, false otherwise
     */
    public static boolean isValidEmailAddress(String addr) {
        if (addr == null) {
            return false;
        }

        addr = addr.trim();

        if (addr.length() == 0) {
            return false;
        }

        // basic address check
        Matcher matcher = basicAddressPattern.matcher(addr);
        if (!matcher.matches()) {
            return false;
        }
        String userPart = matcher.group(1);
        String domainPart = matcher.group(2);

        // user address check
        matcher = validUserPattern.matcher(userPart);
        if (!matcher.matches()) {
            return false;
        }

        // ip domain check
        matcher = ipDomainPattern.matcher(domainPart);
        if (matcher.matches()) {
            // if the pattern matched, check to make sure that the ip range is valid
            for (int i = 1; i < 5; i++) {
                String num = matcher.group(i);

                if (num == null) {
                    return false;
                }

                if (Integer.parseInt(num) > 254) {
                    return false;
                }
            }
            return true;
        }

        // symbolic domain check
        matcher = domainPattern.matcher(domainPart);
        if (matcher.matches()) {
            String tld = matcher.group(matcher.groupCount());

            // Permit top-level-domains of 3 (includes dot separator) because these could be
            // country codes which we are not going to check for.
            matcher = tldPattern.matcher(tld);
            if (tld.length() != 3 && !matcher.matches()) {
                return false;
            }
        }
        else {
            return false;
        }

        // all tests passed
        return true;
    }

    // Testing method
    /*
    public static void main(String[] args) {

        {
            String test1 = "The quick brown fox jumped";
            int chop11 = test1.length();
            int chop12 = test1.length() - 1;
            int chop13 = test1.length() - 3;
            int chop14 = test1.length() - "jumped".length();
            int chop15 = test1.length() - "ox jumped".length();
            // run test 1
            String result11 = chopAtWord(test1, chop11);
            String result12 = chopAtWord(test1, chop12);
            String result13 = chopAtWord(test1, chop13);
            String result14 = chopAtWord(test1, chop14);
            String result15 = chopAtWord(test1, chop15);
            // print results
            if (test1.equals(result11)) { System.err.println("Test 1.1 passed, result: " + result11); }
            else { System.err.println("Test 1.1 failed, result: " + result11); }

            if ("The quick brown fox".equals(result12)) { System.err.println("Test 1.2 passed, result: " + result12); }
            else { System.err.println("Test 1.2 failed, result: " + result12); }

            if ("The quick brown fox".equals(result13)) { System.err.println("Test 1.3 passed, result: " + result13); }
            else { System.err.println("Test 1.3 failed, result: " + result13); }

            if ("The quick brown fox".equals(result14)) { System.err.println("Test 1.4 passed, result: " + result14); }
            else { System.err.println("Test 1.4 failed, result: " + result14); }

            if ("The quick brown".equals(result15)) { System.err.println("Test 1.5 passed, result: " + result15); }
            else { System.err.println("Test 1.5 failed, result: " + result15); }
        }

        System.err.println("");

        {
            String test2 = "The quick brown fox jumped";
            int chop21 = test2.length();
            int chop22 = test2.length() - 1;
            int chop23 = test2.length() - 3;
            int chop24 = test2.length() - "jumped".length();
            int chop25 = test2.length() - "ox jumped".length();
            // run test 1
            String result21 = chopAtWord(test2, chop21, 0);
            String result22 = chopAtWord(test2, chop22, 0);
            String result23 = chopAtWord(test2, chop23, 0);
            String result24 = chopAtWord(test2, chop24, 0);
            String result25 = chopAtWord(test2, chop25, 0);
            // print results
            if (test2.equals(result21)) { System.err.println("Test 2.1 passed, result: " + result21); }
            else { System.err.println("Test 2.1 failed, result: " + result21); }

            if ("The quick brown fox".equals(result22)) { System.err.println("Test 2.2 passed, result: " + result22); }
            else { System.err.println("Test 2.2 failed, result: " + result22); }

            if ("The quick brown fox".equals(result23)) { System.err.println("Test 2.3 passed, result: " + result23); }
            else { System.err.println("Test 2.3 failed, result: " + result23); }

            if ("The quick brown fox".equals(result24)) { System.err.println("Test 2.4 passed, result: " + result24); }
            else { System.err.println("Test 2.4 failed, result: " + result24); }

            if ("The quick brown".equals(result25)) { System.err.println("Test 2.5 passed, result: " + result25); }
            else { System.err.println("Test 2.5 failed, result: " + result25); }
        }

        System.err.println("");

        {
            String test3 = "Thequickbrownfoxjumped";
            int chop31 = test3.length();
            int chop32 = test3.length() - 1;
            int chop33 = test3.length() - 3;
            int chop34 = test3.length() - "jumped".length();
            int chop35 = test3.length() - "ox jumped".length();
            // run test 1
            String result31 = chopAtWord(test3, chop31, "Thequickbrownfoxjumped".length());
            String result32 = chopAtWord(test3, chop32, "Thequick".length());
            String result33 = chopAtWord(test3, chop33, "Thequick".length());
            String result34 = chopAtWord(test3, chop34, "Thequick".length());
            String result35 = chopAtWord(test3, chop35, "Thequick".length());
            // print results
            if ("Thequick".equals(result31)) { System.err.println("Test 3.1 passed, result: " + result31); }
            else { System.err.println("Test 3.1 failed, result: " + result31); }

            if ("Thequick".equals(result32)) { System.err.println("Test 3.2 passed, result: " + result32); }
            else { System.err.println("Test 3.2 failed, result: " + result32); }

            if ("Thequick".equals(result33)) { System.err.println("Test 3.3 passed, result: " + result33); }
            else { System.err.println("Test 3.3 failed, result: " + result33); }

            if ("Thequick".equals(result34)) { System.err.println("Test 3.4 passed, result: " + result34); }
            else { System.err.println("Test 3.4 failed, result: " + result34); }

            if ("Thequick".equals(result35)) { System.err.println("Test 3.5 passed, result: " + result35); }
            else { System.err.println("Test 3.5 failed, result: " + result35); }
        }

        System.err.println("");

        {
            String test4 = "Java.Lang.ClassNotFoundException:com.Citrix";
            int length = test4.length()-3;
            int min = 20;
            String result = chopAtWord(test4, length, min);
            System.err.println("result: " + result);
        }
    }
    */
}
