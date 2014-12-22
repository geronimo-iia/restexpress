package org.restexpress.plugin.swagger.model;

import java.util.List;

import com.google.common.collect.Lists;


public class Operation {
	private String method;
	private String summary;
	private String notes;
	private String responseClass;
	private String nickname;
	private int position;
	private List<String> produces = Lists.newArrayList();
	private List<String> consumes = Lists.newArrayList();
	private List<String> protocols = Lists.newArrayList();

	private List<Authorization> authorizations = Lists.newArrayList();
	private List<Parameter> parameters = Lists.newArrayList();
	private List<ResponseMessage> responseMessages = Lists.newArrayList();
	private String deprecated = null;
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}
	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}
	/**
	 * @return the responseClass
	 */
	public String getResponseClass() {
		return responseClass;
	}
	/**
	 * @param responseClass the responseClass to set
	 */
	public void setResponseClass(String responseClass) {
		this.responseClass = responseClass;
	}
	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	/**
	 * @return the produces
	 */
	public List<String> getProduces() {
		return produces;
	}
	/**
	 * @param produces the produces to set
	 */
	public void setProduces(List<String> produces) {
		this.produces = produces;
	}
	/**
	 * @return the consumes
	 */
	public List<String> getConsumes() {
		return consumes;
	}
	/**
	 * @param consumes the consumes to set
	 */
	public void setConsumes(List<String> consumes) {
		this.consumes = consumes;
	}
	/**
	 * @return the protocols
	 */
	public List<String> getProtocols() {
		return protocols;
	}
	/**
	 * @param protocols the protocols to set
	 */
	public void setProtocols(List<String> protocols) {
		this.protocols = protocols;
	}
	/**
	 * @return the authorizations
	 */
	public List<Authorization> getAuthorizations() {
		return authorizations;
	}
	/**
	 * @param authorizations the authorizations to set
	 */
	public void setAuthorizations(List<Authorization> authorizations) {
		this.authorizations = authorizations;
	}
	/**
	 * @return the parameters
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	/**
	 * @return the responseMessages
	 */
	public List<ResponseMessage> getResponseMessages() {
		return responseMessages;
	}
	/**
	 * @param responseMessages the responseMessages to set
	 */
	public void setResponseMessages(List<ResponseMessage> responseMessages) {
		this.responseMessages = responseMessages;
	}
	/**
	 * @return the deprecated
	 */
	public String getDeprecated() {
		return deprecated;
	}
	/**
	 * @param deprecated the deprecated to set
	 */
	public void setDeprecated(String deprecated) {
		this.deprecated = deprecated;
	}

	
}
