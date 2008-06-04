/**
 * $RCSFile$
 * $Revision: 19018 $
 * $Date: 2005-06-09 14:41:08 -0700 (Thu, 09 Jun 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.servlets;

import org.jivesoftware.webchat.util.SettingsManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Retrieves the images belonging to particular workgroup. This is used for the look and feel of
 * a particular workgroup.
 */
public class ImageServlet extends HttpServlet {
    private final String IMAGE = "image";
    private final String WORKGROUP = "workgroup";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Initialize adminManager
        final String imageName = request.getParameter(IMAGE);
        final String workgroupName = request.getParameter(WORKGROUP);

        final SettingsManager imageManager = SettingsManager.getInstance();

        byte[] imageBytes = imageManager.getImage(imageName, workgroupName, getServletContext());
        if (imageBytes == null) {
            getServletContext().log(imageName + " for " + workgroupName + " is null.");
            return;
        }

        imageManager.writeBytesToStream(imageBytes, response);
    }
}