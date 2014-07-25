package org.restexpress.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.restexpress.Request;
import org.restexpress.Response;

/**
 * {@link MessageObserverDispatcher} dispatch message on several
 * {@link MessageObserver}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class MessageObserverDispatcher implements MessageObserver {

	private final List<MessageObserver> messageObservers = new ArrayList<>();

	/**
	 * Build a new instance of {@link MessageObserverDispatcher}.
	 */
	public MessageObserverDispatcher() {
		super();
	}

	/**
	 * Build a new instance of {@link MessageObserverDispatcher}.
	 */
	public MessageObserverDispatcher(final List<MessageObserver> observers) {
		super();
		addMessageObserver(observers);
	}

	/**
	 * Add {@link MessageObserver} if they not ever in this dispatcher.
	 * 
	 * @param observers
	 */
	public void addMessageObserver(final MessageObserver... observers) {
		for (final MessageObserver observer : observers) {
			if (!messageObservers.contains(observer)) {
				messageObservers.add(observer);
			}
		}
	}

	/**
	 * Add {@link MessageObserver} if they not ever in this dispatcher.
	 * 
	 * @param observers
	 */
	public void addMessageObserver(final List<MessageObserver> observers) {
		for (final MessageObserver observer : observers) {
			if (!messageObservers.contains(observer)) {
				messageObservers.add(observer);
			}
		}
	}

	/**
	 * @return {@link List} of {@link MessageObserver}.
	 */
	public List<MessageObserver> messageObservers() {
		return messageObservers;
	}

	@Override
	public void onReceived(Request request, Response response) {
		for (final MessageObserver observer : messageObservers) {
			observer.onReceived(request, response);
		}
	}

	@Override
	public void onException(Throwable exception, Request request, Response response) {
		for (final MessageObserver observer : messageObservers) {
			observer.onException(exception, request, response);
		}
	}

	@Override
	public void onSuccess(Request request, Response response) {
		for (final MessageObserver observer : messageObservers) {
			observer.onSuccess(request, response);
		}
	}

	@Override
	public void onComplete(Request request, Response response) {
		for (final MessageObserver observer : messageObservers) {
			observer.onComplete(request, response);
		}
	}

	public void notifyReceived(MessageContext context) {
		onReceived(context.getRequest(), context.getResponse());
	}

	public void notifyException(final MessageContext context) {
		final Throwable exception = context.getException();
		onException(exception, context.getRequest(), context.getResponse());
	}

	public void notifyComplete(final MessageContext context) {
		onComplete(context.getRequest(), context.getResponse());

	}

	public void notifySuccess(final MessageContext context) {
		onSuccess(context.getRequest(), context.getResponse());
	}
}
