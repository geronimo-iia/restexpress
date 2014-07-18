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

import com.github.jmkgreen.morphia.annotations.Entity;
import com.github.jmkgreen.morphia.annotations.Indexed;
import com.strategicgains.syntaxe.annotation.StringValidation;

@Entity("blog_entries")
public class BlogEntry
extends AbstractLinkableEntity
{
	@Indexed
	@StringValidation(name="Blog ID", required=true)
	private String blogId;

	@StringValidation(name="Title", required=true)
	private String title;
	
	@StringValidation(name="Entry Content", required=true)
	private String content;
	
	@Indexed
	@StringValidation(name="Author", required=true)
	private String author;

	public String getBlogId()
    {
    	return blogId;
    }

	public void setBlogId(String blogId)
    {
    	this.blogId = blogId;
    }

	public String getTitle()
    {
    	return title;
    }

	public void setTitle(String title)
    {
    	this.title = title;
    }

	public String getContent()
    {
    	return content;
    }

	public void setContent(String content)
    {
    	this.content = content;
    }

	public String getAuthor()
    {
    	return author;
    }

	public void setAuthor(String author)
    {
    	this.author = author;
    }
}
