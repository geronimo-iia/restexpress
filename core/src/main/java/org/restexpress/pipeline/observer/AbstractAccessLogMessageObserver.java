package org.restexpress.pipeline.observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restexpress.Request;
import org.restexpress.Response;

/**
 * {@link AbstractAccessLogMessageObserver} is base class to build "access log".
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public abstract class AbstractAccessLogMessageObserver extends BaseMessageObserver {

	private final Map<String, Long> timers = new ConcurrentHashMap<String, Long>();

	public AbstractAccessLogMessageObserver() {
	}

	@Override
	public final void onReceived(final Request request, final Response response) {
		timers.put(request.getCorrelationId(), System.currentTimeMillis());
	}

	@Override
	public final void onComplete(Request request, Response response) {
		final Long stop = System.currentTimeMillis();
		final Long start = timers.remove(request.getCorrelationId());
		access(request.getEffectiveHttpMethod().toString(), request.getUrl(), response.getResponseStatus().toString(), start != null ? stop - start : -1);
	}

	protected abstract void access(String method, String url, String status, long duration);
}
