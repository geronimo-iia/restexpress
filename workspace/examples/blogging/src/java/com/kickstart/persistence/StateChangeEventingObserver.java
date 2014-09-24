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
package com.kickstart.persistence;

import com.kickstart.domain.Blog;
import com.kickstart.domain.BlogEntry;
import com.kickstart.domain.event.BlogDeletedEvent;
import com.kickstart.domain.event.BlogEntryDeletedEvent;
import com.kickstart.domain.event.ObjectCreatedEvent;
import com.kickstart.domain.event.ObjectDeletedEvent;
import com.kickstart.domain.event.ObjectUpdatedEvent;
import com.strategicgains.eventing.DomainEvents;
import com.strategicgains.repoexpress.Repository;
import com.strategicgains.repoexpress.domain.TimestampedIdentifiable;
import com.strategicgains.repoexpress.event.AbstractRepositoryObserver;

public class StateChangeEventingObserver<T extends TimestampedIdentifiable>
extends AbstractRepositoryObserver<T>
{
	private Repository<T> repo;

	public StateChangeEventingObserver(Repository<T> repo)
	{
		super();
		this.repo = repo;
	}

	@Override
	public void afterCreate(T object)
	{
		DomainEvents.publish(new ObjectCreatedEvent(object));
	}

	@Override
	public void beforeDelete(T object)
	{
		DomainEvents.publish(new ObjectDeletedEvent(object));
		
		if (Blog.class.isAssignableFrom(object.getClass()))
		{
			DomainEvents.publish(new BlogDeletedEvent((Blog) object));
		}
		else if (BlogEntry.class.isAssignableFrom(object.getClass()))
		{
			DomainEvents.publish(new BlogEntryDeletedEvent((BlogEntry) object));
		}
	}

	@Override
	public void beforeUpdate(T object)
	{
		T previous = repo.read(object.getId());
		DomainEvents.publish(new ObjectUpdatedEvent(previous, object));
	}
}