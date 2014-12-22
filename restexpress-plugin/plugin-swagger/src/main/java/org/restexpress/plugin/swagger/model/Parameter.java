package org.restexpress.plugin.swagger.model;

public class Parameter {
	private String name;
	private String description = null;
	private 	String defaultValue = null;
	private Boolean required;
	private Boolean allowMultiple;
	private String dataType;
	private AllowableValues allowableValues = new AnyAllowableValues();
	private String paramType;
	private String paramAccess;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}
	/**
	 * @return the allowMultiple
	 */
	public Boolean getAllowMultiple() {
		return allowMultiple;
	}
	/**
	 * @param allowMultiple the allowMultiple to set
	 */
	public void setAllowMultiple(Boolean allowMultiple) {
		this.allowMultiple = allowMultiple;
	}
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the allowableValues
	 */
	public AllowableValues getAllowableValues() {
		return allowableValues;
	}
	/**
	 * @param allowableValues the allowableValues to set
	 */
	public void setAllowableValues(AllowableValues allowableValues) {
		this.allowableValues = allowableValues;
	}
	/**
	 * @return the paramType
	 */
	public String getParamType() {
		return paramType;
	}
	/**
	 * @param paramType the paramType to set
	 */
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	/**
	 * @return the paramAccess
	 */
	public String getParamAccess() {
		return paramAccess;
	}
	/**
	 * @param paramAccess the paramAccess to set
	 */
	public void setParamAccess(String paramAccess) {
		this.paramAccess = paramAccess;
	}

}
