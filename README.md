# Fastpath-webchat

Fastpath is a helpdesk product for Openfire and Spark. It allows users of your website to ask a question and get immediate 
feedback from somone in a given queue. 

There are three components to Fastpath: The Openfire plugin, the Spark plugin, and this webchat component. 
All three are required for this product to function. 

The Webchat app deploys to a servlet container (such as Jetty or Tomcat) as a WAR file. On the first run, you must configure 
it by giving it the hostname and port to your Openfire server. It then writes this to a settings file and it is retained
until the next time you deploy the app.

The Webchat app does not use web sockets or BOSH, but rather HTTP polling. This can be beneficial in certain situations
where having a thread tied up continually for each connection can quickly lock up a web server such as Apache.

Another benefit to the Webchat component is that it acts as a reverse proxy to your internal network, where Openfire 
can reside. For secure architectures, allowing direct external access to Openfire may be out of the question, so Fastpath 
provides an alternate method.
