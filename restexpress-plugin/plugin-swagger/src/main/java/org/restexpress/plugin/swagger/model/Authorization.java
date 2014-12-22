package org.restexpress.plugin.swagger.model;

public class Authorization {
	private String type;
	private AuthorizationScope[] scopes;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the scopes
	 */
	public AuthorizationScope[] getScopes() {
		return scopes;
	}

	/**
	 * @param scopes
	 *            the scopes to set
	 */
	public void setScopes(AuthorizationScope[] scopes) {
		this.scopes = scopes;
	}

}
