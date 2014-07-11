/*
    Copyright 2013, Strategic Gains, Inc.

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 */
package org.restexpress;

import java.util.List;

/**
 * @author toddf
 * @since Jul 19, 2013
 */
public class LittleO
{
	private String name = "name";
	private int integer = 42;
	private boolean isBoolean = true;
	private String[] array =
	{
	    "array1", "array2", "array3"
	};
	private List<LittleO> children;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getInteger()
	{
		return integer;
	}

	public void setInteger(int integer)
	{
		this.integer = integer;
	}

	public boolean isBoolean()
	{
		return isBoolean;
	}

	public void setBoolean(boolean isBoolean)
	{
		this.isBoolean = isBoolean;
	}

	public String[] getArray()
	{
		return array;
	}

	public void setArray(String[] values)
	{
		this.array = values;
	}
	
	public List<LittleO> getChildren()
	{
		return children;
	}
	
	public void setChildren(List<LittleO> values)
	{
		this.children = values;
	}
}
