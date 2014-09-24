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
/*
 Copyright 2011, Strategic Gains, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.restexpress.observer;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.MessageObserver;

/**
 * {@link CounterMessageObserver} count message.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class CounterMessageObserver implements MessageObserver {

	private int receivedCount = 0;
	private int exceptionCount = 0;
	private int successCount = 0;
	private int completeCount = 0;

	public CounterMessageObserver() {
		super();
	}

	/**
	 * Reset counter.
	 */
	public void reset() {
		receivedCount = 0;
		exceptionCount = 0;
		successCount = 0;
		completeCount = 0;
	}

	@Override
	public void onReceived(Request request, Response response) {
		++receivedCount;
	}

	@Override
	public void onException(Throwable exception, Request request, Response response) {
		++exceptionCount;
		exception.printStackTrace();
	}

	@Override
	public void onSuccess(Request request, Response response) {
		++successCount;
	}

	@Override
	public void onComplete(Request request, Response response) {
		++completeCount;
	}

	public int getReceivedCount() {
		return receivedCount;
	}

	public int getExceptionCount() {
		return exceptionCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public int getCompleteCount() {
		return completeCount;
	}
}
