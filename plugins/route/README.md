Routes Plugin
=============

Adds several routes within your service suite to facilitate auto-discovery of what's available. 

Routes added are:
* /routes/metadata.{format} - to retrieve all metadata for all routes.
* /routes/{routeName}/metadata.{format} - to retrieve metadata for a named route.
* /routes - placeholder in HTML format to see all information in a classical browser

The plugin allows flags and parameters, just like the regular Route DSL to set flags and parameters on the routes created
by the plugin so appropriate values are available in preprocessors, etc.  For instance, if you want to turn off 
authentication or authorization for the metadata routes.

maven: com.strategicgains:restexpress-plugins-route

Usage
=====

Simply create a new plugin and register it with the RestExpress server, setting options
as necessary, using method chaining if desired.

For example:
```java
RestExpress server = new RestExpress()...

new RoutesPlugin()
	.flag("public-route")					// optional. Set a flag on the request for this route.
	.parameter("name", "value")				// optional. Set a parameter on the request for this route.
	.register(server);
```

Todo
====


* Use content type negotiation to display in JSON, XML or HTML format.
* Autoregister plugin when this artifact is loaded.


