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
package org.restexpress.plugin.xstream;

import java.util.Date;

/**
 * @author toddf
 * @since Aug 4, 2011
 */
public class KnownObject
{
	public static final String CONSTANT = "i hope you don't see this";
	@SuppressWarnings("unused")
    private static final String INTERNAL = "or this";

	public int integer = 1;
	public String string = "string value";
	@SuppressWarnings("deprecation")
    public Date date = new Date(64, 11, 17, 16, 30);
	private String p = "something private";
	public String[] sa;
	
	public String getP()
	{
		return p;
	}
	
	public String getQ()
	{
		return "Q(" + p + ")";
	}
	
	@Override
	public String toString() {
		return string;
	}
}
