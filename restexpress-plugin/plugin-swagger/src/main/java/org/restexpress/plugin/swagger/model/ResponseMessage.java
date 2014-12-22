package org.restexpress.plugin.swagger.model;

public class ResponseMessage {
	private int code;
	private String message;
	private String responseModel = null;

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the responseModel
	 */
	public String getResponseModel() {
		return responseModel;
	}

	/**
	 * @param responseModel
	 *            the responseModel to set
	 */
	public void setResponseModel(String responseModel) {
		this.responseModel = responseModel;
	}

}
