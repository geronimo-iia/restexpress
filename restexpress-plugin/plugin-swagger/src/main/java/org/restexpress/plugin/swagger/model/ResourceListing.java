package org.restexpress.plugin.swagger.model;

import scala.collection.immutable.List;

public class ResourceListing {
	private String apiVersion;
	private String swaggerVersion;
	private List<ApiListingReference> apis;
	private List<AuthorizationType> authorizations;
	private ApiInfo info;

	/**
	 * @return the apiVersion
	 */
	public String getApiVersion() {
		return apiVersion;
	}

	/**
	 * @param apiVersion
	 *            the apiVersion to set
	 */
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	/**
	 * @return the swaggerVersion
	 */
	public String getSwaggerVersion() {
		return swaggerVersion;
	}

	/**
	 * @param swaggerVersion
	 *            the swaggerVersion to set
	 */
	public void setSwaggerVersion(String swaggerVersion) {
		this.swaggerVersion = swaggerVersion;
	}

	/**
	 * @return the apis
	 */
	public List<ApiListingReference> getApis() {
		return apis;
	}

	/**
	 * @param apis
	 *            the apis to set
	 */
	public void setApis(List<ApiListingReference> apis) {
		this.apis = apis;
	}

	/**
	 * @return the authorizations
	 */
	public List<AuthorizationType> getAuthorizations() {
		return authorizations;
	}

	/**
	 * @param authorizations
	 *            the authorizations to set
	 */
	public void setAuthorizations(List<AuthorizationType> authorizations) {
		this.authorizations = authorizations;
	}

	/**
	 * @return the info
	 */
	public ApiInfo getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(ApiInfo info) {
		this.info = info;
	}

}
