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
package com.kickstart.event;

import com.kickstart.domain.event.BlogEntryDeletedEvent;
import com.kickstart.persistence.CommentRepository;
import com.strategicgains.eventing.EventHandler;

public class BlogEntryCascadeDeleteHandler
implements EventHandler
{
	private CommentRepository comments;
	
	public BlogEntryCascadeDeleteHandler(CommentRepository service)
	{
		this.comments = service;
	}

	@Override
	public void handle(Object event)
	throws Exception
	{
		System.out.println("Cascade-deleting a blog entry...");
		String blogEntryId = ((BlogEntryDeletedEvent) event).blogEntryId;
		
		// "Cascade-delete" the comments for this blog entry.
		comments.deleteByBlogEntryId(blogEntryId);
	}

	@Override
	public boolean handles(Class<?> type)
	{
		return BlogEntryDeletedEvent.class.isAssignableFrom(type);
	}
}
