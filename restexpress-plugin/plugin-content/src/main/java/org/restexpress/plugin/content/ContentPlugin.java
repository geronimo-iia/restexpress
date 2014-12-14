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
package org.restexpress.plugin.content;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractRoutePlugin;
import org.restexpress.plugin.content.adapter.CachedContextAdapter;
import org.restexpress.plugin.content.adapter.CompositeContextAdapter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

/**
 * {@link ContentPlugin} implements :
 * <ul>
 * <li>{@link ContentService}</li>
 * <li>{@link ContextAdapter}</li>
 * </ul>
 * in order to be able to change at runtime all content controller
 * configuration. By default:
 * <ul>
 * <li>cache is disabled</li>
 * <li>temporary directory is created under JVM temporary directory</li>
 * </ul>
 * 
 * <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class ContentPlugin extends AbstractRoutePlugin implements ContentService {

	/**
	 * Default value entry point.
	 */
	public final static String DEFAULT_ENTRYPOINT = "/static";

	/**
	 * Temporary directory.
	 */
	protected File tempDirectory;

	protected String entryPoint;

	private final Map<String, ContextAdapter> adapters = Maps.newHashMap();

	private boolean enableCache = false;
	private int initialCapacity;
	private int maximumSize;
	private int expireAfterWrite;

	public boolean binded = false;

	private ContextAdapter contextAdapter;

	/**
	 * Build a new instance.
	 */
	public ContentPlugin() {
		super();
	}

	/**
	 * Build a new instance.
	 * 
	 * @param priority
	 */
	public ContentPlugin(int priority) {
		super(priority);
	}

	@Override
	public void initialize(final RestExpress server) {
		entryPoint = DEFAULT_ENTRYPOINT;
		tempDirectory = Files.createTempDir();
		binded = true;
		build();
		final ContentController controller = new ContentController(this);
		server.uri(DEFAULT_ENTRYPOINT, controller).name("content.routes").noSerialization();
	}

	@Override
	public void destroy(final RestExpress server) {
		binded = false;
		enableCache = false;
		adapters.clear();
		contextAdapter = null;
		FileRemovals.cleanDirectory(tempDirectory);
	}

	@Override
	public String entryPoint() {
		return entryPoint;
	}

	@Override
	public void enableCache() {
		enableCache(300, 25000, 10);
	}

	@Override
	public void enableCache(final int initialCapacity, final int maximumSize, final int expireAfterWrite) {
		enableCache = true;
		this.initialCapacity = initialCapacity;
		this.maximumSize = maximumSize;
		this.expireAfterWrite = expireAfterWrite;
		build();
	}

	@Override
	public void disableCache() {
		enableCache = false;
		build();
	}

	@Override
	public boolean isCacheEnabled() {
		return enableCache;
	}

	@Override
	public int expireAfterWrite() {
		return enableCache ? expireAfterWrite : -1;
	}

	@Override
	public File temporaryDirectory() {
		return tempDirectory;
	}

	@Override
	public void temporaryDirectory(File temporaryDirectory) {
		Preconditions.checkNotNull(temporaryDirectory);
		Preconditions.checkArgument(temporaryDirectory.exists(), "temporary directory must exists");
		Preconditions.checkArgument(temporaryDirectory.isDirectory(), "temporary directory must be a directory");
		this.tempDirectory = temporaryDirectory;
		build();
	}

	@Override
	public void register(final ContextAdapter contextAdapter) {
		adapters.put(contextAdapter.name(), contextAdapter);
		build();
	}

	@Override
	public ContextAdapter find(final String name) {
		return adapters.get(name);
	}

	@Override
	public boolean remove(final String name) {
		final boolean result = adapters.remove(name) != null;
		if (result)
			build();
		return result;
	}

	/**
	 * Build and refresh controller if plugin is binded.
	 */
	protected void build() {
		if (binded) {
			ContextAdapter contextAdapter = new CompositeContextAdapter(adapters.values());
			if (enableCache)
				contextAdapter = new CachedContextAdapter(contextAdapter, tempDirectory, initialCapacity, maximumSize, expireAfterWrite);
			// swap configuration
			this.contextAdapter = contextAdapter;
		}
	}

	@Override
	public String name() {
		return contextAdapter.name();
	}

	@Override
	public Boolean match(final String name) {
		return contextAdapter.match(name);
	}

	@Override
	public File retrieve(final String name) throws IOException {
		return contextAdapter.retrieve(name);
	}

}
