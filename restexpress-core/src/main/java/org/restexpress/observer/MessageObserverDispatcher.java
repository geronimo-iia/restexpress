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