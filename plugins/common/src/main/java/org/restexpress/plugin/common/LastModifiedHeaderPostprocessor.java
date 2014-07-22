package org.restexpress.plugin.common;

import org.intelligentsia.commons.http.HttpMethods;
import org.intelligentsia.commons.http.ResponseHeader;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.HttpHeaderTimestampAdapter;

/**
 * {@link LastModifiedHeaderPostprocessor} add header
 * {@link ResponseHeader#LAST_MODIFIED} for {@link HttpMethods#GET} if not
 * present. Time come from {@link Response#getBody()} if the {@link Object}
 * implement Timestamped.
 * 
 * TODO define Timestamped.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class LastModifiedHeaderPostprocessor implements Postprocessor {
	DateAdapter fmt = new HttpHeaderTimestampAdapter();

	@Override
	public void process(MessageContext context) {
		if (!context.getRequest().isMethodGet())
			return;
		Response response = context.getResponse();
		if (!context.getResponse().hasBody())
			return;
		Object body = response.getBody();

		// if (!response.hasHeader(ResponseHeader.LAST_MODIFIED.getHeader()) &&
		// body instanceof Timestamped) {
		// response.addHeader(ResponseHeader.LAST_MODIFIED.getHeader(),
		// fmt.format(((Timestamped) body).getUpdatedAt()));
		// }
	}
}
