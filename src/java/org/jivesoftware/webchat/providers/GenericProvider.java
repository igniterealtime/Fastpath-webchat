/**
 * $RCSfile$
 * $Revision: 19153 $
 * $Date: 2005-06-27 09:14:12 -0700 (Mon, 27 Jun 2005) $
 *
 * Copyright (C) 1999-2005 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */
package org.jivesoftware.webchat.providers;

import org.jivesoftware.webchat.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GenericProvider implements MetaDataProvider {
    private String nickname;
    private String userID;
    private String emailAddress;
    private String agent;
    private String question;
    private String product;
    private String company;
    private String state;
    private String country;

    public GenericProvider(){

    }

    public String getUsername() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String uniqueIdentifier) {
        this.userID = uniqueIdentifier;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCompany() {
        return company;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProduct() {
        return product;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean filterRequest(HttpServletRequest request, HttpServletResponse response) {
        // Retrieve reserved words
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String question = request.getParameter("question");
        String agent = request.getParameter("agent");
        product = getString(request.getParameter("product"));
        company = getString(request.getParameter("company"));
        state = getString(request.getParameter("state"));
        country = getString(request.getParameter("country"));

        // Handle Unique Identifier.
        String userID = request.getParameter("userID");
        if (userID == null) {
            userID = getUserCookie(request, response);
        }
        setUserID(userID);

        // Handle Email
        if (email == null) {
            email = "Not Specified";
        }
        setEmailAddress(email);

        // Handle the nickname to show in the WebClient and Agent.
        if (username == null) {
            username = "Visitor";
        }
        setNickname(username);


        if (question != null) {
            setQuestion(question);
        }

        if (agent != null) {
            setAgent(agent);
        }

        return false;
    }

    private String getUserCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String userid = null;
        final int no = cookies != null ? cookies.length : 0;
        for (int i = 0; i < no; i++) {
            Cookie foundCookie = cookies[i];
            String cookieName = foundCookie.getName();
            if ("uniqueid".equals(cookieName)) {
                userid = foundCookie.getValue();
            }
        }

        // If the cookie does not exist, create it and store it in the session.
        if (userid == null) {
            userid = StringUtils.randomString(10);
            Cookie usercookie = new Cookie("uniqueid", userid);
            usercookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(usercookie);
        }
        return userid;
    }

    private String getString(String s) {
        return s == null ? "" : s;
    }
}
