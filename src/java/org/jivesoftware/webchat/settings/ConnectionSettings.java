/**
 * $RCSFile$
 * $Revision: 19018 $
 * $Date: 2005-06-09 14:41:08 -0700 (Thu, 09 Jun 2005) $
 *
 * Copyright (C) 2004-2008 JiveSoftware, Inc. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.webchat.settings;

/**
 * Container for server connection settings. The ChatSettings are responsible for handling
 * all connectivty settings to connect the Web Client service to a particular XMPPServer.
  */
public class ConnectionSettings {
    private String serverDomain;
    private int port;
    private boolean sslEnabled;
    private int sslPort = -1;

    /**
     * Empty Constructor
     */
    public ConnectionSettings(){

    }

    /**
     * Returns the XMPPServer domain to connect to.
     * @return the XMPPServer domain.
     */
    public String getServerDomain() {
        return serverDomain;
    }

    /**
     * Sets the XMPPServer domain to connect to.
     * @param serverDomain the XMPPServer to connect to.
     */
    public void setServerDomain(String serverDomain) {
        this.serverDomain = serverDomain;
    }

    /**
     * Returns true if SSL Connections are enabled.
     * @return true if SLL Connections are enabled, otherwise false.
     */
    public boolean isSSLEnabled() {
        return sslEnabled;
    }

    /**
     * Enables or disables SSL Connections.
     * @param sslEnabled true to enable SSL Connections, default is false.
     */
    public void setSSLEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    /**
     * Returns the SSLPort to connect to. This is only called if SSL is enabled.
     * @return the port to connect to using SSL.
     */
    public int getSSLPort() {
        return sslPort;
    }

    /**
     * Sets the SLL port.
     * @param sslPort the SSL port to use.
     */
    public void setSSLPort(int sslPort) {
        this.sslPort = sslPort;
    }

    /**
     * Returns the non-secure port to connect to.
     * @return the non-secure port to connect to.
     */
    public int getPort() {
        return port;
    }

    /**
     * Sets the non-secure port to connect to. Default is port 5222.
     * @param port the non-secure port to connect to.
     */
    public void setPort(int port) {
        this.port = port;
    }
}
