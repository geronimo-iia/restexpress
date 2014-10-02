

RestExpress is a thin wrapper on the JBOSS Netty HTTP stack to provide a simple and easy way to
create RESTful services in Java that support massive Internet Scale and performance.

An extremely Lightweight, Fast, REST Engine and API for Java.  
Supports JSON and XML serialization automagically as well as ISO 8601 date formats.  
A thin wrapper on Netty IO HTTP handling, RestExpress lets you create performant, stand-alone REST web services rapidly.  
RestExpress supports the best practices proposed in the [Best Practice document](http://www.restapitutorial.com).


Born to be simple, only three things are required to wire up a service:
1) The main class which utilizes the RestExpress DSL to create a server instance.
2) A RouteDeclaration extender (much like routes.rb in a Rails app), which uses a DSL for the
   declaration of supported URLs and HTTP methods of the service(s) in its defineRoutes() method.
3) Service implementation(s), which is/are a simple POJO--no interface or super class
   implementation.


Read Wiki page on https://github.com/geronimo-iia/RestExpress/wiki for more information.
