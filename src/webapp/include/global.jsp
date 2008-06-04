<%--
  - $RCSfile$
  - $Revision: 18400 $
  - $Date: 2005-02-05 00:40:39 -0800 (Sat, 05 Feb 2005) $
  -
  - Copyright (C) 2003-2008 Jive Software. All rights reserved.
  -
  - This software is published under the terms of the GNU Public License (GPL),
  - a copy of which is included in this distribution, or a commercial license
  - agreement with Jive.
--%>

<%  // All global vars defined here

    // Get the branding scheme for this app. This will serve as a suffix for all
    // image names.
    String branding = application.getInitParameter( "branding" );
    if (branding == null || "".equals(branding.trim())) {
        branding = "default";
    }
    String brandingTitle = application.getInitParameter("branding-title");
%>