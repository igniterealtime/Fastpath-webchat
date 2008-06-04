/**
 * $RCSFile$
 * $Revision: 18449 $
 * $Date: 2005-02-14 12:13:03 -0800 (Mon, 14 Feb 2005) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.actions;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * Used to create JavaBeans that are container aware. Subclass the WebBean class if
 * to get a handle on the implicit objects within a servlet or jsp. From there you
  * can access each object just as you would within a whatever corresponding object.
 * For example, assuming MyBean subclasses WebBean and you are using MyBean within
 * a jsp page.:<p>
 *
 * <pre>
 * MyBean myBean = new MyBean();
 * myBean.init(pageContext);
 * </pre>
 * </p>
 *
 * You can then access all implicit objects directly within the bean.
 */
public abstract class WebBean {
    /**
     * The HttpSession used with this WebBean.
     */
    public HttpSession session;

     /**
     * The HttpRequest used with this WebBean.
     */
    public HttpServletRequest request;

     /**
     * The HttpResponse used with this WebBean.
     */
    public HttpServletResponse response;

     /**
     * The ServletContextHttp used with this WebBean.
     */
    public ServletContext application;

     /**
     * The Serlvet Writer used with this WebBean.
     */
    public JspWriter out;

    /**
     * Empty Constructor
     */
    public WebBean(){

    }

    /**
     * Initialize WebBean from within a servlet.
     * @param request the request to use.
     * @param response the response to use.
     * @param session the session to use.
     * @param app the servlet context to use.
     * @param out the PrintWriter to use.
     */
    public void init(HttpServletRequest request, HttpServletResponse response,
                     HttpSession session, ServletContext app, JspWriter out) {

        this.request = request;
        this.response = response;
        this.session = session;
        application = app;
        this.out = out;
    }

    /**
     * Initialize the WebBean from within a JSP page.
     * @param context the pageContext of the JSP page.
     */
    public void init(PageContext context) {
        request = (HttpServletRequest) context.getRequest();
        response = (HttpServletResponse) context.getResponse();
        session = context.getSession();
        application = context.getServletContext();
        out = context.getOut();
    }
}
