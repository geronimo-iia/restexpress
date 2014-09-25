

RestExpress is a thin wrapper on the JBOSS Netty HTTP stack to provide a simple and easy way to
create RESTful services in Java that support massive Internet Scale and performance.

An extremely Lightweight, Fast, REST Engine and API for Java.  
Supports JSON and XML serialization automagically as well as ISO 8601 date formats.  
A thin wrapper on Netty IO HTTP handling, RestExpress lets you create performant, stand-alone REST web services rapidly.  
RestExpress supports the best practices proposed in the Best Practice document available at: http://www.restapitutorial.com


Born to be simple, only three things are required to wire up a service:
1) The main class which utilizes the RestExpress DSL to create a server instance.
2) A RouteDeclaration extender (much like routes.rb in a Rails app), which uses a DSL for the
   declaration of supported URLs and HTTP methods of the service(s) in its defineRoutes() method.
3) Service implementation(s), which is/are a simple POJO--no interface or super class
   implementation.

See: restexpress-sample/echo directory to get started.


Note:
 * Actually, this project is under heavy development, so be patient for the release.


See also:
* TechStack_Bootcamp_2013.pdf
* TechSummit_2012_Instant_REST_Services.pdf


Framework
=========


Current framework are used in order to run Restexpress server:
* Netty
* Guava
* Jackson Json and Xml
* Joda
* SLF4j
* Mvel

And, with Optional Plugins:
* Xstream
* Gson

Request Response Life Cycle
===========================


TODO


IO and Processing Model
=======================

TODO

Controller and REST
===================

* HTTP Methods, if not changed in the fluent (DSL) interface, map to the following:
	* GET --> read(Request, Response)
	* PUT --> update(Request, Response)
	* POST --> create(Request, Response)
	* DELETE --> delete(Request, Response)


* You can choose to return objects from the methods, if desired, which will be returned to the client in the body of the response.  
  The object will be marshaled into JSON or XML, depending on the default or based on the format in the request (e.g. '.xml' or '?format=xml').

* If you choose to not return a value from the method (void methods) and using raw responses, then call response.setResponseNoContent() 
  before returning to set the response HTTP status code to 204 (no content).  
  Wrapped responses (JSEND style) are the default.  
  So if you're using wrapped responses, there will always be a response returned to the client--therefore, you don't need to set the response.setResponseNoContent().  
  Just return your objects--or not.  RestExpress will handle things on your behalf!

* On successful creation, call response.setResponseCreated() to set the returning HTTP status code to 201.

* For more real-world examples, see the restexpress-sample directory which contains additional projects that setup RestExpress services.  
 Simply do '**mvn clean install**' to run them.  
 Then to see what's available perform a GET on the route: '/routes/metadata' to get a list of all the routes(or endpoints) available 
 (e.g. localhost:8000/routes/metadata in the browser).

Please see the echo application in examples/echo for a running example.


Example:

```java
public class EchoController {
	private static final String ECHO_PARAMETER_NOT_FOUND = "'echo' header or query-string parameter not found. Please set query-string parameter 'echo' (e.g. ?echo=value).";
	private static final String ECHO_HEADER = "echo";

	public ChannelBuffer create(Request request, Response response) {
		response.setResponseCreated();
		return request.getBody();
	}

	public String delete(Request request, Response response) {
		return request.getHeader(ECHO_HEADER, ECHO_PARAMETER_NOT_FOUND);
	}

	public String read(Request request, Response response) {
		String echo = request.getHeader(ECHO_HEADER);
		if (echo == null) {
			return "Please set query-string parameter 'echo' (e.g. ?echo=value)";
		}
		return echo;
	}

	public ChannelBuffer update(Request request, Response response) {
		return request.getBody();
	}
}
```


If a controller instance is mapped with "/", we have:
* GET, http://localhost:8000/ --> read, if echo parameter is present in query string, return it
* PUT, http://localhost:8000/ --> update, return the request body 
* POST, http://localhost:8000/ --> create, return an HTTP 204
* DELETE, http://localhost:8000/ --> delete, if echo parameter is present in query string, return it


Route Definition
================

TODO

Format and Negociation
======================

TODO


Plugin and Extension point
==========================

TODO

Settings
========

TODO


Serialization
=============

TODO


Processor
=========

We have three point when a "processor" can be applied:
* pre processor: applied before response processing
* post processor, applied after response processing
* finally processor: always applied after serialization process

Actually, your could find:
* HttpBasicAuthenticationPreprocessor
* CacheHeaderPostprocessor
* DateHeaderPostprocessor
* EtagHeaderPostprocessor
* LastModifiedHeaderPostprocessor
* ResponseHeaderPostProcessor


HttpBasicAuthenticationPreprocessor
-----------------------------------

This preprocessor implements HTTP Basic authentication. It simply parses the
Authorization header, putting the username and password in request headers.
To use it, simply add it to your RestExpress server as follows:
 

	server.addPreprocessor(new HttpBasicAuthenticationPreprocessor("my realm"));


Once this preprocessor completes successfully, it places the username and password in the request as headers, X-AuthenticatedUser and
 X-AuthenticatedPassword, respectively.

If the preprocessor fails, it throws an UnauthorizedException, which results in an HTTP status of 401 to the caller. 
It also sets the WWW-Authenticate header to 'Basic realm=<provided realm>' where <provided realm> is the arbitrary realm name passed in on instantiation.
 
Use of this preprocessor assumes you'll implement an authorization preprocessor that validates the username and password.

CacheHeaderPostprocessor
------------------------

For GET and HEAD requests, adds caching control headers. May be used in conjunction with DateHeaderPostprocessor to add Date header 
for GET and HEAD requests.
 
If the route has a Parameters.Cache.MAX_AGE parameter, whose value is the max-age in seconds then the following are added:
* Cache-Control: max-age=<seconds>
* Expires: now + max-age
 
If the route has a Flags.Cache.NO_CACHE flag, then the following headers are set on the response: 
* Cache-Control: no-cache
* Pragma: no-cache
 
The MAX_AGE parameter takes precidence, in that, if present, the NO_CACHE flag is ignored.

To use: simply add server.addPostprocessor(new CacheHeaderPostprocessor()); in your main() method.


DateHeaderPostprocessor
-----------------------

For GET and HEAD requests, adds a Date: <timestamp> header, if not already present.
This enables clients to determine age of a representation for caching purposes. 
<timestamp> is in RFC1123 full date format.

To use: simply add server.addPostprocessor(new DateHeaderPostprocessor()); in your main() method.

Note that HEAD requests are not provided with a Date header via this postprocessor. This is due to the fact that most external
caches forward HEAD requests to the origin server as a GET request and cache the result.


EtagHeaderPostprocessor
-----------------------

If the response body is non-null, adds an ETag header. ETag is computed from the body object's hash code .


LastModifiedHeaderPostprocessor
-------------------------------

Add header "LAST_MODIFIED" for GET requests if not present.
 Timestamp came from response body, if the object implement TimeStamped interface.
 
	public interface TimeStamped {
	
	    public Date updateAt();
	}
 

ResponseHeaderPostProcessor
---------------------------

Add response header if they're not present.
Headers are arbitrary define by pair (name, value).

To use: add server.addPostprocessor(new ResponseHeaderPostProcessor(name, value)); in your main() method.
	
	

RestExpress Response
====================

RestExpress supports:
* JSEND-style
* raw response 
* a formated error result

Meaning that it can wrap responses so AJAX clients can always process the responses easily.  
Or it can simply marshal the service return value directly into JSON or XML.

JSend is a specification that lays down some rules for how JSON responses from web servers should be formatted.
This specification should only be used in JSON format. 
For more information on JSEND-style responses, see: http://labs.omniti.com/labs/jsend


Date Format Support
===================

Restexpress support this following standard and no standard date format:
* ISO 8601
* RFC 1123
* RFC 822
* RFC 850
* ANSI C ASCTIME
* Other: yyyy-MM-dd

The standard output header is RFC 1123, and for body is ISO 8601 (like 'yyyy-MM-ddThh:mm:ss[.sss]Z')


Build
===== 


Maven Usage
-----------

A usual, simply do a 'mvn clean install"



Project Tree and Module
-----------------------

* restexpress-http: Http standard Definition without external dependencies (See readme file in the module).
* restexpress-common: Common Definition which can be used by Server and a Java Client
* restexpress-core: server implementation
* restexpress-server: server distribution, with cache and route plugin activated
* restexpress-plugin:
	* plugin-gson: Gson serializer
	* plugin-xstream: Xstream serializer
* restexpress-sample
	* echo: A simple example



Dependency
----------

If you would integrate Restexpress server in your project, you have to use this dependency:

```xml
		<dependency>
			<groupId>org.intelligents-ia.restexpress</groupId>
			<artifactId>restexpress-server</artifactId>
			<version>X.X.X</version>
		</dependency>
```



Plugins
=======


TODO




Cache Control Plugin
--------------------


This plugin adds caching-related headers to GET and HEAD responses.


For GET and HEAD requests, adds a Date: \<timestamp\> header, if not already present. This enables clients to determine age of a representation for caching purposes.  Where \<timestamp\> is in RFC1123 full date format.

Note that while HEAD requests are provided with a Date header via this plugin. Most external caches forward HEAD requests to the origin server as a GET request and cache the result.

If the route has a Parameters.Cache.MAX_AGE parameter, whose value is the max-age in seconds then the following are added:
* Cache-Control: max-age=\<seconds\>
* Expires: now + max-age

If the route has a Flags.Cache.NO_CACHE flag, then the following headers are set on the response:
* Cache-Control: no-cache
* Pragma: no-cache

The MAX_AGE parameter takes precidence, in that, if present, the NO_CACHE flag is ignored.

If the response body is non-null, adds an ETag header.  ETag is computed from the body object's hash code combined with the hash code of the resulting format (content-type).

**NOTE:** To fully support basic caching capability, also implement a LastModifiedHeaderPostprocessor() that inspects the date on your domain or presentation model and sets the 'Last-Modified' header.

Example Usage:
```Java
    RestExpress server = new RestExpress();
    ...
    new CacheControlPlugin()
        .register(server);
    server.addPostprocessor(new LastModifiedHeaderProcessor());
```

An example LastModifiedHeaderPostprocessor:
```Java
public class LastModifiedHeaderPostprocessor
implements Postprocessor
{
	DateAdapter fmt = new HttpHeaderTimestampAdapter();

	@Override
	public void process(Request request, Response response)
	{
		if (!request.isMethodGet()) return;
		if (!response.hasBody()) return;

		Object body = response.getBody();

		if (!response.hasHeader(LAST_MODIFIED) && body instanceof Timestamped)
		{
			response.addHeader(LAST_MODIFIED, fmt.format(((Timestamped) body).getUpdatedAt()));
		}
	}
}
```




Routes Plugin
-------------

Adds several routes within your service suite to facilitate auto-discovery of what's available. 

Routes added are:
* /routes/metadata.{format} - to retrieve all metadata for all routes.
* /routes/{routeName}/metadata.{format} - to retrieve metadata for a named route.
* /routes - placeholder in HTML format to see all information in a classical browser

The plugin allows flags and parameters, just like the regular Route DSL to set flags and parameters on the routes created
by the plugin so appropriate values are available in preprocessors, etc.  For instance, if you want to turn off 
authentication or authorization for the metadata routes.

This plugins is present by default in restexpress-server.

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





Change History/Release Notes:
===================================================================================================

Current Developpement Version 0.10.4
------------------------------------

* create restexpress-XXX module
* move default serialization tool in restexpress-common
* integrate http specification module
* Add RestExpressSettings definition and json I/O loader utilities (see Settings)
* Remove route default class (default format could be use with a Format class)
* Merge (Nul/Default/Abstract)SerializationProvider, into one class.
* Extract gson serialization processor in a child module
* Move JsendResult, ErrorResult in common (useful for java client)
* Remove Error id and http code status (duplicated with response.status)
* Refactor JsendResult to be compliant with JSend http://labs.omniti.com/labs/jsend
* Use Http standard definition shared between rest express server and java client
* Use MessageContext in pre and post processor (more flexibility to handle response)
* Use HttpRuntimeException from org.intelligents-ia.common:http-specification
* Remove ExceptionMapping: server implementation must throw standard Http Exception.
* Refactor Resolver in order to user MessageContext (more flexibility to handle response)
* Add javadoc on methods, classes
* Refactor XML alias definition. All alias must be declared either on dedicated processor, or after "add" with SerializationProvider
* Remove SerializationProvider static member on RestExpress.
* Add RestExpressLauncher (main class) to quickly launch a RestExpress server instance
* Add plugins module
* refactor Jackson Object mapper instantiation in order to be able to customize it without extends JacksonJsonProcessor
* add RestExpressEntryPoint (for runtime discovery of plugins)
* default serialization configuration can be controlled by ServerSettings.
* update maven parent pom (version in properties, license plugin, ...)
* Add a Server Context
* Add a ConsoleAccesLogMessageObserver, CounterMessageObserver and MessageObserverDispatcher
* Simplify SerializationProvider: support only one format(one content type) per instance.
* Add ResponseHeaderPostProcessor (add predefined list of header in response, if they're not present)
* Add common plugin (from PluginExpress): cache utility, and x-security (not finalized).
* Add MediaType, Format, Encoding enumeration
* Extract restexpress-api, restexpress-core, restexpress restexpress-test modules
* refactor SerializationProcessor in order to deal only with media type
* user format enumeration to resolve to a mime type
* refactor Request Handler
* add priority on Plugin, in order to manage their order on initialization
* Refactor Route and base URL usage
* add TextProcessor for all text/* media 
* add Slf4fErrorLogMessageObserver to log error using SLF4j
* add Slf4jAccessLogMessageObserver to build an access log using SLF4j
* change default wrapper to ErrorResult
* add Joda and Guava module for Jackson


Release 0.10.3 - 27 May 2014
---------------------------
* Change URL Pattern matcher to allow URLs with '?' at the end, but no query-string parameters following it.
* Changed compiler output version to 1.7 (from 1.6).
* Added SSL support (from Clark Hobbie).
* Fixed error message in QueryRange.setLimit(int) from 'limit must be >= 0' to 'limit must be > 0' (from GCL).
* Changed Request.getQueryStringMap() to never return null (from Kevin Williams).
* Expose the creation of the DefaultRequestHandler (via RestExpress.buildRequestHandler()) to rest engines not using "main()" or "bind()" (from Ryan Dietrich).
* Fixed issue with QueryOrders.enforceAllowedProperties() threw erroneous exceptions.
* Added JacksonEncodingStringSerializer (including it in JacksonJsonProcessor) to outbound HTML Entity encode for possible XSS attacks.
* Updated to Jackson-Databind 2.3.3 (from 2.1.4).
* refactor Flag as enum

Release 0.10.2 - 3 Apr 2014
---------------------------
* Refactored ExceptionMapping into an interface, extracting previous implementation into DefaultExceptionMapper.
* Added new convenience methods on Request: getBodyAsStream(), getBodyAsBytes(), getBodyAsByteBuffer().
* Added new method Request.getNamedPath() that returns only the route path pattern instead of the
  entire URL, which Request.getNamedUrl() does.
* Request.getProtocol() now returns the protocol from the underlying HttpRequest instance.
* Fixed issue in QueryOrders where only a single valid order parameters is supported. Caused IndexOutOfBoundsException.
* Fixed an issue in RestExpress.java where finally processors were assigned as post processors. This could affect some finally processor implementations, such as timers, etc. since they are now run later in the process.
* Fixed an issue with finally processors, they are now called before the message is returned to the client (after serialization), or in the finally block (if an error occurs, response is in indeterminite state).
* Introduced another factory method in ErrorResult, allowing elimination of exceptionType in output.
* Introduced RequestContext, a Map of name/value pairs much like the Log4j mapped diagnostic contexts (MDC), as an instrument for passing augmentation data from different sources to lower levels in the framework.
* Can now add supported MediaRange(s) dynamically at startup, such as application/hal+json.
* ** Breaking Change ** Repackaged org.serialization.xml to org.restexpress.serialization.xml
* Added ContentType.HAL_JSON and ContentType.HAL_XML plus new RestExpressServerTest tests.
* Introduced RoutePlugin to simplify plugins that create internal routes.

Release 0.10.1 - 24 Jan 2014
---------------------------------------------------------------------------------------------------
* Fixed NPE issue when RestExpress.setSerializationProvider() is not called.
* Fixed misspelling in JsonSerializationProcessor.java for SUPPORTED_MEDIA_TYPES.
* Enhanced QueryFilters and QueryOrders to support enforcement of appropriate filter/order
  properties—enabling the verification of appropriate orders and filters. Throws
  BadRequestException on failure.
* Removed core StringUtils in favor of common StringUtils.
* Fixed issue where default serialization processor was not used if setSerializationProcessor() was not called.
* Changed RestExpress server startup message from “Starting <name> Server on port <port>” to “<name> server listening on port <port>”

Release 0.10.0 - 3 Jan 2014
---------------------------------------------------------------------------------------------------
* ** Breaking Change ** Repackaged to 'org.restexpress...' from 'com.strategicgains.restexpress...'
* ** Breaking Change ** Re-added GSON capability from version 0.8.2, making things a little
  more pluggable with RestExpress.setSerializationProvider(SerializationProvider).
  DefaultSerializationProvider is the default. GsonSerializationProvider is also available,
  but requires adding GSON to your pom file.  Must refactor your own custom ResponseProcessor
  class into a SerializationProvider implementor.
* ** Breaking Change ** Removed RestExpress.putResponseProcessor(), .supportJson(), .supportXml(),
  .supportTxt(), .noJson(), .noXml(), noTxt() as this is all implemented in the SerializationProvider.
* Implemented content-type negotiation using Content-Type header for serialization (e.g. 
  Request.getBodyAs(type)) and Accept header for deserialization. Implementation still
  favors .{format}, but uses content-type negotiation if format not supplied.
* Added RestExpress.enforceHttpSpec() and .setEnforceHttpSpec(boolean) to enable setting
  the HTTP specification enforcement.  Previously, enforcement was always turned on. Now
  default is OFF. With it off, RestExpress allows you to create non-standard (per the HTTP
  specification) responses.
* Removed com.strategicgains.restexpress.common.util.Callback interface since it wasn't being used.
* Upgraded Netty to 3.9.0 Final

Release 0.9.4.2 - 16 Oct 2013
----------------------------------------------------------------------------------------------------
* Added ErrorResultWrapper and ErrorResult to facilitate only wrapping error responses vs.
  not wrapping or JSEND-style always-wrapped responses.

Release 0.9.4 - 17 Jul 2013
---------------------------------------------------------------------------------------------------
* Fixed issue for plugins that are dependent on RouteMetadata. Fixed issue with routes
  that depend on GET, PUT, POST, DELETE as the default--wasn't generating metadata
  correctly for that corner case.
* Fixed issue with RouteBuilder metadata generation where it wouldn't include the defaults if none set on route.
* Updated javadoc for getFullPattern() and getPattern().
* Changed Route.getBaseUrl() to perform null check to avoid getting 'null' string in value.
* Combined RestExpress-Common as a sub-module and moved core RestExpress functionality
  to the 'core' sub-module.

Release 0.9.3 - 14 Jun 2013
---------------------------------------------------------------------------------------------------
* Fixed issue with setter getting called in deserialization instead of Jackson deserializer.
* Removed LogLevel enumeration due to lack of use.
* Fixed issue #61 - Large Chunked Request Causes Errors.  Added HttpChunkAggregator to the
  pipeline.
* Added RestExpress.setMaxContentSize(int) to allow limiting of total content length of requests
  even if chunked.  Default max content size is 25K.
* Added RestExpress.iterateRouteBuilders(Callback<RouteBuilder> callback) to facilitate
  plugins, etc. augmenting or extracting information from the declared routes.

Release 0.9.2 - 27 Mar 2013
---------------------------------------------------------------------------------------------------
* **DEPRECATED:** Request.getUrlDecodedHeader() and Request.getRawHeader() in favor of getHeader(). Since
  all HTTP headers and query-string parameters are URL decoded before being put on the Request
  object, these methods are redundant and cause problems.  Their functionality was also changed
  to simply call getHeader()--so no URL decoding is done in getUrlDecodedHeader().
* Ensured that parameters extracted from the URL are decoded before setting them as headers
  on the Request.  Now all headers are URL decoded before any call to Request.getHeader(String).
* Added Request.getRemoteAddress(), which returns the remote address of the request originator.
* Merged pull request (Issue #58) from amitkarmakar13: add List&lt;String&gt; getHeaders(String)
* Removed '?' and '#' as valid path segment characters in UrlPattern to conform better with
  IETF RFC 3986, section 3.3-path. Made '{format}' a first-class element for matching URL route
  patterns (by using '{format}' instead of a regex to match).

Release 0.9.1 - 4 Mar 2013
---------------------------------------------------------------------------------------------------
* **BREAKING CHANGE:** eliminated GSON. RestExpress now uses Jackson for JSON processing.
  The changes are localized to the 'serialization' package.  Simply copy the ObjectIdDeserializer,
  ObjectIdSerializer and JsonSerializationProcessor from https://github.com/RestExpress/RestExpress-Scaffold/tree/master/mongodb/src/main/java/com/strategicgains/restexpress/scaffold/mongodb/serialization
  for MongoDB-based projects.  Or just the JsonSerializationProcessor from https://github.com/RestExpress/RestExpress-Scaffold/tree/master/minimal/src/main/java/com/strategicgains/restexpress/scaffold/minimal/serialization
  for a minimal project.
* **BREAKING CHANGE:** Removed Chunking and compression settings. RestExpress does not support
  chunking/streaming uploads.  So the setting were superfluous.  The facility is still there
  to support streaming downloads, however, and these will be chunked as necessary. As compression
  is based on the Accept header, support is always provided--settings are superfluous.
  NOTE: streaming downloads are not fully implemented yet.
* **BREAKING CHANGE:** Removed LoggingHandler from the Netty pipeline and related setter methods.
* Added HttpBasicAuthenticationPreprocessor to facilitate HTTP Basic Authentication. Added
  Flags.Auth.PUBLIC_ROUTE, NO_AUTHENTICATION, and NO_AUTHORIZATION to support configuration
  of HttpBasicAuthenticationPreprocessor (and other authentication/authorization 
  related routes).

Release 0.8.2 - 19 Feb 2013
---------------------------------------------------------------------------------------------------
* Fixed issue in Request.parseQueryString() to URL decode query-string parameters before putting
  them in the Request header.

Release 0.8.1 - 16 Jan 2013
---------------------------------------------------------------------------------------------------
* Removed Ant-build artifacts.
* Extracted Query-related classes into RestExpress-Common.
* Fixed maven compile plugin to generate Java target and source for 1.6
* Updated Netty dependency to 3.6.2.Final
* Removed dependency on HyperExpress.

Release 0.8.0 - 09 Jan 2013
---------------------------------------------------------------------------------------------------
* Pushed to Maven Central repository.
* Introduced maven build.
* Merged pull request #49 - Added method to get all headers from a HttpRequest.
* Fixed issue #40 (https://github.com/RestExpress/RestExpress/issues/40).
* Introduced route 'aliases' where there are multiple URLs for a given service.
* Introduced concept of "finally" processors, which are executed in the finally block of
  DefaultRequestHandler and all of them are executed even if an exception is thrown within one
  of them.  This enable the CorsHeaderPlugin to set the appropriate header even on not found
  errors, etc.
* Changed to support multiple response types with wrapping or not, etc. Now can support wrapped
  JSON (.wjson) and XML (.wxml) as well as un-wrapped JSON (.json) and XML (.xml) depending on the
  format specifier.
* Now throws BadRequestException (400) if the specified format (e.f. .json) isn't supported by the 
  service suite.
* Now throws MethodNotAllowedException (405) if the requested URL matches a route but not for the
  requested HTTP method.  Sets the HTTP Allow header to a comma-delimited list of accepted methods.
* Removed StringUtils.parseQueryString() as it was previously deprecated--use QueryStringParser.
* Introduced String.join() methods (2).
* Removed JSONP handling, favoring use of CORS instead, introducing CorsHeaderPlugin and corresponding post-processor.
* Wraps ETAG header in quotes.
* Renamed QueryRange.stop to QueryRange.limit.
* Removed need for RouteDefinition class, moving that functionality into the RestExpress builder.
* Changed example apps to reflect above elimination of RouteDefinition class.

Release 0.7.4 - 30 Nov 2012 (branch 'v0.7.4')
---------------------------------------------------------------------------------------------------
* Patch release to allow period ('.') as a valid character within URL parameters. Note that this
  now allows a period to be the last character on the URL whether there is a format-specifier
  parameter declared for that route, or not. Also, if the route supports the format specifier and
  there is a period in the last parameter of the URL, anything after the last period will be used
  as the format for the request--which may NOT be what you want.
  -- /foo/todd.fredrich --> /foo/{p1}.{format} will use 'fredrich' as the format
  -- /foo/todd.fredrich.json --> /foo/{p1}.{format} will use 'json' as the format
  -- /foo/todd. --> /foo/{p1}.{format} will contain 'todd.' for the value of p1
  -- /foo/todd. --> /foo/{p1} will contain 'todd.' for the value of p1

Release 0.7.3 - 12 July 2012 (branch 'v0.7.3')
---------------------------------------------------------------------------------------------------
* Patch release to fix an issue with i18n. Fixed issue with
  DefaultJsonProcessor.deserialize(ChannelBuffer, Class) where underlying InputStreamReader was
  not UTF-8.

Release 0.7.2 - 14 May 2012
---------------------------------------------------------------------------------------------------
* Introduced ExecutionHandler with configuration via RestExpress.setExecutorThreadCount(int)
  to off-load long-running requests from the NIO workers into a separate thread pool.
* Introduced CacheControlPlugin which leverages CacheHeaderPostprocessor, DateHeaderPostprocessor
  and EtagHeaderPostprocessor to respond to GET requests.
* Introduced EtagHeaderPostprocessor which adds ETag header in response to GET requests.
* Introduced DateHeaderPostprocessor which adds a Date header to responses to GET requests.
* Introduced CacheHeaderPostprocessor which support Cache-Control and other caching-related
  response header best-practices by setting Parameters.Cache.MAX_AGE or Flags.Cache.DONT_CACHE on
  a route.
* Changed to use QueryStringParser over StringUtils for query-string and QueryStringDecoder for 
  body parsing. This mitigates HashDoS attacks, since the query-string is parsed before a request
  is accepted.
* Deprecated StringUtils in favor of using Netty's QueryStringDecoder or 
  RestExpress's QueryStringParser.
* Refactored so SerializationProcessor.resolve(Request) is only called once at the end of the
  request cycle (performance enhancement).

Release 0.7.1 - 20 Sep 2011
---------------------------------------------------------------------------------------------------
* Added rootCause to ResultWrapper data area.
* Exposed the XStream object from DefaultXmlProcessor.
* Renamed Link to XLink.
* Renamed LinkUtils to XLinkUtils, adding asXLinks() method that utilizes XLinkFactory callback
  to create the XLink instances.
* Changed URL Matching to support additional characters: '[', ']', '&' which more closely follows
  W3C the specification.
* Added ability to return query string parameters as a Map from the Request.
* Introduced Request.getBaseUrl() which returns protocol and host, without URL path information.
* Introduced query criteria capability: filter, order, range (for pagination).
* Introduced the concept of Plugins.
* Refactored the console routes to use the new plugin concept.
* Updated Netty jars (to 3.2.5).
* Added ability to set the number of worker threads via call to RestExpress.setWorkerThreadCount()
  before calling bind().

Release 0.7.0
---------------------------------------------------------------------------------------------------
* Added gzip request/response handling. On by default. Disable it via call to
  RestExpress.noCompression() and supportCompression().
* Added chunked message handling. On by default. Chunking settings are managed via
  RestExpress.noChunkingSupport(), supportChunking(), and setMaxChunkSize(int).

Release 0.6.1.1 - 31 Mar 2011
---------------------------------------------------------------------------------------------------
* Bug fix to patch erroneously writing to already closed channel in
  DefaultRequestHandler.exceptionCaught().

Release 0.6.1 - 30 Mar 2011
---------------------------------------------------------------------------------------------------
* Stability release.
* Fixed issue when unable to URL Decode query string parameters or URL.
* Introduced SerializationResolver that defines a getDefault() method. Implemented
  SerializationResolver in DefaultSerializationResolver.
* Changed UrlPattern to match format based on non-whitespace vs. word characters.
* Refactored Request.getHeader() into getRawHeader(String) and getUrlDecodedHeader(String), along
  with corresponding getRawHeader(String,String) and getUrlDecodedHeader(String,String).
* Renamed realMethod property to effectiveHttpMethod, along with appropriate accessor/mutator.
* Removed Request from Response constructor signature.
* Added FieldNamingPolicy to DefaultJsonProcessor (using LOWER_CASE_WITH_UNDERSCORES).
* getUrlDecodedHeader(String) throws BadRequestException if URL decoding fails.

Release 0.6.0.2 - 21 Mar 2011
---------------------------------------------------------------------------------------------------
* Fixed issue with 'connection reset by peer' causing unresponsive behavior.
* Utilized Netty logging behavior to add logging capabilities to RestExpress.
* Made socket-level settings externally configurable:  tcpNoDelay, KeepAlive, reuseAddress,
  soLinger, connectTimeoutMillis, receiveBufferSize.
* Merged in 0.5.6.1 path release changes.
* Added enforcement of some HTTP 1.1 specification rules: Content-Type, Content-Length and body
  content for 1xx, 204, 304 are not allow.  Now throws HttpSpecificationException if spec. is
  not honored.
* Added ability to add 'flags' and 'parameters' to routes, in that, uri().flag("name") on Route
  makes test request.isFlagged("name") return true. Also, uri().parameter("name", "value")
  makes request.getParamater("name") return "value". Not returned/marshaled in the response.
  Useful for setting internal values/flags for preprocessors, controllers, etc.
* Added .useRawResponse() and .useWrappedResponse() to fluent route DSL.  Causes that particular
  route to wrap the response or not, independent of global response wrap settings.
* Parameters parsed from the URL and query string arguments are URL decoded before being placed
  as Request headers.

Release 0.6.0.1
---------------------------------------------------------------------------------------------------
* Issue #7 - Fixed issue with invalid URL requested where serialization always occurred to the
             default (JSON).  Now serializes to the requested format, if applicable.
* Issue #11 - Feature enhancement for Kickstart.  Now utilizes Rails-inspired configuration
              environements (e.g. dev, testing, prod, etc.).
* Issue #12 - Parse URL parameter names out of the URL pattern and include them in the route
              metadata output.

Release 0.6.0
---------------------------------------------------------------------------------------------------
* Routes now defined in descendant of RouteDeclaration.
* Refactored everything into RestExpress object, using builder pattern for configuration.
* Implemented RestExpress DSL to declare REST server in main().
* Added supported formats and default format to RouteBuilder.
* Added JSEND-style response wrapping (now default).  Call RestExpress.useWrappedResponses() to use.
* Add ability to support raw response return.  Call RestExpress.useRawResponses() to use.
* Implemented /console/routes.{format} route which return metadata about the routes in this
  service suite. To use, call RestExpress.supportConsoleRoutes().
* Exceptions occurring now return in the requested format with the message wrapped and using the
  appropriate mime type (e.g. application/json or application/xml).
* Kickstart application has complete build with 'dist' target that builds a runnable jar file and
  'run' target that will run the services from the command line.
* Kickstart application now handles JVM shutdown correctly using JVM shutdown hooks.  The method
  RestExpress.awaitShutdown() uses the DefaultShutdownHook class.  RestExpress.shutdown() allows
  programs to use their own shutdown hooks, calling RestExpress.shutdown() upon shudown to release
  all resource.

Release 0.5.6.1 - 11 Mar 2011
---------------------------------------------------------------------------------------------------
* Patch release to fix issue with HTTP response status of 204 (No Content) and 304 (Not Modified)
  where they would return a body of an empty string and content length of 2 ('\r\n').  No longer
  serializes for 204 or 304.  Also no longer serializes for null body response unless a JSONP header
  is passed in on the query string.

Release 0.5.6 - 18 Jan 2011
---------------------------------------------------------------------------------------------------
* Upgraded to Netty 3.2.3 final.
* Added getProtocol(), getHost(), getPath() to Request
* Functionality of getUrl() is now getPath() and getUrl() now returns the entire URL string,
  including protocol, host and port, and path.

Release 0.5.5
---------------------------------------------------------------------------------------------------
* Added regex URL matching with RouteMapping.regex(String) method.
* Refactored Route into an abstract class, moving previous functionality into ParameterizedRoute.
* Added KickStart release artifact to get projects going quickly--simply unzip the kickstart file.
* Added SimpleMessageObserver which performs simple timings and outputs to System.out.

Release 0.5.4
---------------------------------------------------------------------------------------------------
* Added alias() capability to DefaultTxtProcessor to facilitate custom text serialization.
* Updated kickstart application to illustrate latest features.
* Minor refactoring of constants and their locations (moved to RestExpress.java).

Release 0.5.3
---------------------------------------------------------------------------------------------------
* Fixed issue with JSON date/timestamp parsing.
* Fixed issues with XML date/timestamp parsing.
* Upgraded to GSON 1.6 release.
* Added correlation ID to Request to facilitate timing, etc. in pipeline.
* Added alias(String, Class) to DefaultXmlProcessor.
* By default, alias List and Link in DefaultXmlProcessor.

Release 0.5.2
---------------------------------------------------------------------------------------------------
* Introduced DateJsonProcessor (sibling to DefaultJsonProcessor) which parses dates vs. time points.
* Refactored ExceptionMapping.getExceptionFor() signature from Exception to Throwable.
* Introduced MessageObserver, which accepts notifications of onReceived(), onSuccess(), onException(), onComplete() to facilitate logging, auditing, timing, etc.
* Changed RouteResolver.resolve() to throw NotFoundException instead of BadRequestException for unresolvable URI.

Release 0.5.1
---------------------------------------------------------------------------------------------------
* Enhanced support for mark, unreserved and some reserved characters in URL. Specifically, added
  $-+*()~:!' and %.  Still doesn't parse URLs with '.' within the string itself--because of the
  support for .{format} URL section.

Release 0.5
---------------------------------------------------------------------------------------------------
* Renamed repository from RestX to RestExpress.
* Repackaged everything from com.strategicgains.restx... to com.strategicgains.restexpress...
* Changed DefaultHttpResponseWriter to output resonse headers correctly.
* Updated javadoc on RouteBuilder to provide some documentation on route DSL.

Release 0.4
---------------------------------------------------------------------------------------------------
* Fixed error in "Connection: keep-alive" processing during normal and error response writing.
* Can now create route mappings for OPTIONS and HEAD http methods.
* Added decoding to URL when Request is constructed.
* Improved pre-processor implementation, including access to resolved route in request.
* Better null handling here and there to avoid NullPointerException, including serialization resolver.
* Improved UT coverage.
* KickStart application builds and is a more accurate template.

Release 0.3
---------------------------------------------------------------------------------------------------
* Added support for "method tunneling" in POST via query string parameter (e.g. _method=PUT or _method=DELETE)
* Added JSONP support. Use jsonp=<method_name> in query string.
* Utilized Builder pattern in DefaultPipelineFactory, which is now PipelineBuilder.
* Externalized DefaultRequestHandler in PipelineBuilder and now supports pre/post processors (with associated interfaces).














