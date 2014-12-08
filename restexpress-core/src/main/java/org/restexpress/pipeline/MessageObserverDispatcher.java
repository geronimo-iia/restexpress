/**
 *        Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 *
 */
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
public final class MessageObserverDispatcher implements MessageObserver {

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
	public MessageObserverDispatcher addMessageObserver(final MessageObserver... observers) {
		for (final MessageObserver observer : observers) {
			if (!messageObservers.contains(observer)) {
				messageObservers.add(observer);
			}
		}
		return this;
	}

	/**
	 * Add {@link MessageObserver} if they not ever in this dispatcher.
	 * 
	 * @param observers
	 */
	public MessageObserverDispatcher addMessageObserver(final List<MessageObserver> observers) {
		for (final MessageObserver observer : observers) {
			if (!messageObservers.contains(observer)) {
				messageObservers.add(observer);
			}
		}
		return this;
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
