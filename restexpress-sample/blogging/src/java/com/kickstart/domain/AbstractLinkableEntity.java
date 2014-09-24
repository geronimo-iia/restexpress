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
package com.kickstart.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.github.jmkgreen.morphia.annotations.NotSaved;
import com.strategicgains.hyperexpress.domain.Link;
import com.strategicgains.hyperexpress.domain.Linkable;
import com.strategicgains.repoexpress.mongodb.AbstractMongodbEntity;

public class AbstractLinkableEntity
extends AbstractMongodbEntity
implements Linkable
{
	@NotSaved
	private List<Link> links;

	@Override
	public List<Link> getLinks()
	{
		return Collections.unmodifiableList(links);
	}

	@Override
	public void setLinks(List<Link> links)
	{
		this.links = new ArrayList<Link>(links);
	}
	
	@Override
	public void addLink(Link link)
	{
		if (links == null)
		{
			links = new ArrayList<Link>();
		}
		
		links.add(link);
	}

	@Override
    public void addAllLinks(Collection<Link> links)
    {
		if (links == null) return;
		
		for (Link link : links)
		{
			addLink(link);
		}
    }
}
