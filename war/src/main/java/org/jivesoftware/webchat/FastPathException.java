/*******************************************************************************
 *
 *      ___------------
 *    --  __-----------     NATO
 *    | --  __---------                  NCI AGENCY
 *    | | --          /\####    ###      P.O. box 174
 *    | | |          / #\   #    #       2501 CD The Hague
 *    | | |         <  # >       # 
 *     \ \ \         \ #/   #    # 
 *      \ \ \         \/####    ###
 *       \ \ \---------
 *        \ \---------     AGENCY        Project: JChat
 *         ----------     The Hague
 *
 * //******************************************************************************
 *
 *  * Copyright 2018 NCI Agency, Inc. All Rights Reserved.
 *  *
 *  * This software is proprietary to the NCI Agency and cannot be copied 
 *  * neither distributed to other parties.
 *  * Any other use of this software is subject to license terms, which
 *  * should be specified in a separate document, signed by NCIA and the
 *  * other party.
 *  *
 *  * This file is NATO UNCLASSIFIED
 *******************************************************************************/
package org.jivesoftware.webchat;

/**
 * Specific Fastpath exception.
 *
 * @author vliet
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
