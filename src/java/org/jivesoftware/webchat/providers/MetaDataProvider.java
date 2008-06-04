package org.jivesoftware.webchat.providers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * $RCSfile$
 * $Revision: 19153 $
 * $Date: 2005-06-27 09:14:12 -0700 (Mon, 27 Jun 2005) $
 * <p/>
 * Copyright (C) 1999-2005 Jive Software. All rights reserved.
 * <p/>
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */
public interface MetaDataProvider {

    String getUsername();
    String getUserID();
    String getEmailAddress();
    String getCompany();
    String getAgent();
    String getQuestion();
    String getProduct();
    String getState();
    String getCountry();

    boolean filterRequest(HttpServletRequest request, HttpServletResponse response);
}
