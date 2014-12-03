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
package org.restexpress.domain.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * {@link UriMetadata} expose information on URI route.
 * 
 * @author toddf
 * @since Feb 14, 2011
 */
public class UriMetadata implements Serializable {
	private static final long serialVersionUID = 7363930531607327246L;

	/**
	 * URI pattern.
	 */
	private String pattern;
	/**
	 * list of parameter from URI.
	 */
	private List<String> parameters;

	protected UriMetadata() {
		super();
	}

	public UriMetadata(final String uri) {
		super();
		this.pattern = uri;
	}

	public String getPattern() {
		return pattern;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void addParameter(final String parameter) {
		if (parameters == null) {
			parameters = new ArrayList<String>();
		}

		if (!parameters.contains(parameter)) {
			parameters.add(parameter);
		}
	}

	public void addAllParameters(final Collection<String> parameters) {
		if (parameters == null) {
			return;
		}

		for (final String parameter : parameters) {
			addParameter(parameter);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UriMetadata other = (UriMetadata) obj;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UriMetadata {pattern=\"" + pattern + "\", parameters=\"" + parameters + " \"}";
	}

}
