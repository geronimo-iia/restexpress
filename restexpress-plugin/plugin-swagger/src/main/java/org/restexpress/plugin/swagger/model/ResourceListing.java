package org.restexpress.plugin.swagger.model;

import scala.collection.immutable.List;

public class ResourceListing {
	 String apiVersion;
	 
	   String swaggerVersion;
	   List<ApiListingReference> apis;
	   List<AuthorizationType> authorizations;
	   ApiInfo info;
	   
//	  apis: List[ApiListingReference] = List(),
//	  authorizations: List[AuthorizationType] = List(),
//	  info: Option[ApiInfo] = None)
}
