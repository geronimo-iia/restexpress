HTTP COMMONS DEFINITION
=======================


The goal of this project is to provided common HTTP definition without dependencies on other project like servlet api, netty, apache http commons...

We provide:

* Standard HTTP Status Code
* Standard HTTP Response Status
* Http Methods
* Common Date Header format: RFC 1123, RFC 822, RFC 850, AINSI C ASCTIME, ISO 8601
* Exception class based on Http Response status Code
* Enum on request and response header


This code is based on different open source project like netty, jackson-databind, rest express, see Notice for more information.


Maven
-----

Add this dependency:


	<dependency>
		<groupId>org.intelligents-ia.restexpress</groupId>
		<artifactId>restexpress-http</artifactId>
		<version>X.X.X</version>
	</dependency>



Change Log
==========

* Add Header X_AUTHENTICATED_USER, X_AUTHENTICATED_PASSWORD, WWW_AUTHENTICATE
* Add standard Status, Header and HTTP Exception definition
* Date format Header definition


