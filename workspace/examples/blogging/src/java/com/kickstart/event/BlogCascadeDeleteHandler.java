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

import com.kickstart.domain.event.BlogDeletedEvent;
import com.kickstart.persistence.BlogEntryRepository;
import com.kickstart.persistence.CommentRepository;
import com.strategicgains.eventing.EventHandler;

public class BlogCascadeDeleteHandler
implements EventHandler
{
	private BlogEntryRepository blogEntries;
	private CommentRepository comments;

	public BlogCascadeDeleteHandler(BlogEntryRepository blogEntryRepo, CommentRepository commentRepo)
	{
		this.blogEntries = blogEntryRepo;
		this.comments = commentRepo;
	}

	@Override
	public void handle(Object event)
	throws Exception
	{
		System.out.println("Cascade-deleting blog...");
		String blogId = ((BlogDeletedEvent) event).blogId;

		// Delete the comments for every blog entry within this blog.
		comments.deleteByBlogEntryIds(blogEntries.findIdsByBlogId(blogId));

		// Now delete all the blog entries in this blog.
		blogEntries.deleteByBlogId(blogId);
	}

	@Override
	public boolean handles(Class<?> type)
	{
		return BlogDeletedEvent.class.isAssignableFrom(type);
	}
}
