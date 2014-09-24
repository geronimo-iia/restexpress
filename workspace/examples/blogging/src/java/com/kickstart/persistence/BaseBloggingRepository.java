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

import com.mongodb.Mongo;
import com.strategicgains.repoexpress.mongodb.AbstractMongodbEntity;
import com.strategicgains.repoexpress.mongodb.MongodbEntityRepository;

public class BaseBloggingRepository<T extends AbstractMongodbEntity>
extends MongodbEntityRepository<T>
{
	public BaseBloggingRepository(Mongo mongo, String databaseName, Class<T>... types)
	{
		super(mongo, databaseName, types);
	}
	
	@Override
	protected void initializeObservers()
	{
		super.initializeObservers();
		addObserver(new StateChangeEventingObserver<T>(this));
	}
}
