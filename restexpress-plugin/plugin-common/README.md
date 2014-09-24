Common Plugin
=============





X-Security Plugin 
=================


This plugin qdds owasp recomended x-security headers to responses. Based on the 
recommendations at https://www.owasp.org/index.php/List_of_useful_HTTP_headers

Currently, this plugin only adds the X-Content-Type-Options header to all responses leaving 
the RestExpress server. The only currently defined option is "nosniff",
therefore each header leaving the system will have a header that looks like
```X-Content-Type-Options: nosniff```


Example Usage:
```Java
    RestExpress server = new RestExpress();
    ...
    new XSecurityPlugin()
        .register(server);
```




Cache Control Plugin
====================


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
