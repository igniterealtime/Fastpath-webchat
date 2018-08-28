/**
 * Copyright (C) 2018 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */
package org.jivesoftware.webchat;

/**
 * Specific Fastpath exception.
 *
 * @author Anno van Vliet
 *
 */
public class FastPathException extends Exception {

  /**
   * @param string
   */
  public FastPathException(String string) {
    super(string);
  }
}
