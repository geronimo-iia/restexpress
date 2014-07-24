package org.restexpress.response;

import org.intelligentsia.commons.http.exception.BadRequestException;
import org.intelligentsia.commons.http.exception.NotAcceptableException;
import org.restexpress.Request;
import org.restexpress.Response;

/**
 * {@link ResponseProcessorSettingResolver} define methods to resolve:
 * <ul>
 * <li>Which {@link ResponseProcessorSetting} to use for deserialize
 * {@link Request}</li>
 * <li>Which {@link ResponseProcessorSetting} to use for serialize
 * {@link Response}</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public interface ResponseProcessorSettingResolver {

	/**
	 * Resolve {@link Request} and find which {@link ResponseProcessorSetting}
	 * to use for deserialize {@link Request}.
	 * 
	 * @param request
	 * @return a {@link ResponseProcessorSetting}
	 * @throws {@link BadRequestException} if format is not supported
	 */
	public ResponseProcessorSetting resolve(Request request) throws BadRequestException;

	/**
	 * Resolve {@link Response} and find which {@link ResponseProcessorSetting}
	 * to use for serialize {@link Response}.
	 * 
	 * @param request
	 *            {@link Request}
	 * @param response
	 *            {@link Response}
	 * @param shouldForce
	 * @return a {@link ResponseProcessorSetting}.
	 * @throws {@link BadRequestException} if format is not supported
	 * @throws {@link NotAcceptableException} if media type is not supported
	 */
	public ResponseProcessorSetting resolve(Request request, Response response, boolean shouldForce) throws BadRequestException, NotAcceptableException;
}
