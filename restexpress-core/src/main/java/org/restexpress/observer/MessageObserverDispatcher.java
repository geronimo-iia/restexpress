package org.restexpress.observer;

import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageObserver;

/**
 * {@link MessageObserverDispatcher} dispatch message on several
 * {@link MessageObserver}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class MessageObserverDispatcher implements MessageObserver {

	private final List<MessageObserver> observers;

	/**
	 * Build a new instance of {@link MessageObserverDispatcher}.
	 */
	public MessageObserverDispatcher(final List<MessageObserver> observers) {
		super();
		this.observers = observers;
	}

	/**
	 * @return {@link List} of {@link MessageObserver}.
	 */
	public List<MessageObserver> messageObservers() {
		return observers;
	}

	@Override
	public void onReceived(Request request, Response response) {
		for (final MessageObserver observer : observers) {
			observer.onReceived(request, response);
		}
	}

	@Override
	public void onException(Throwable exception, Request request, Response response) {
		for (final MessageObserver observer : observers) {
			observer.onException(exception, request, response);
		}
	}

	@Override
	public void onSuccess(Request request, Response response) {
		for (final MessageObserver observer : observers) {
			observer.onSuccess(request, response);
		}
	}

	@Override
	public void onComplete(Request request, Response response) {
		for (final MessageObserver observer : observers) {
			observer.onComplete(request, response);
		}
	}

}