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

/**
 * Defines the interface for processing that occurs after the request is
 * created, but before any other processing of the route occurs. The
 * preprocessing chain is terminated if an exception is thrown. In fact, if a
 * Processor throws an exception, the rest of the Preprocessors in the chain
 * are skipped along with the entire route. If an exception occurs in a
 * Processor, Postprocessors are not called either. However, MessageObservers
 * are.
 * 
 * @author toddf
 * @since Aug 31, 2010
 */
public interface Preprocessor {
	public void process(MessageContext context);
}
