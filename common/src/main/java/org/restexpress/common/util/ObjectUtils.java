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
package org.restexpress.common.util;

/**
 * @author toddf
 * @since Aug 30, 2013
 */
public class ObjectUtils
{
	/**
	 * Determines is two objects are comparable to each other, in that
	 * they implement Comparable and are of the same type.  If either
	 * object is null, returns false.
	 * 
	 * @param o1 an instance
	 * @param o2 an instance
	 * @return true if the instances can be compared to each other.
	 */
	public static boolean areComparable(Object o1, Object o2)
    {
		if (o1 == null || o2 == null)
		{
			return false;
		}
		
		if ((isComparable(o1) && isComparable(o2)) &&
			(o1.getClass().isAssignableFrom(o2.getClass()) ||
			o2.getClass().isAssignableFrom(o1.getClass())))
		{
			return true;
		}
		
		return false;
    }

	/**
	 * Returns true if the object implements Comparable.
	 * Otherwise, false.
	 * 
	 * @param object an instance
	 * @return true if the instance implements Comparable.
	 */
	public static boolean isComparable(Object object)
    {
	    return (object instanceof Comparable);
    }

	private ObjectUtils()
	{
		// prevents instantiation.
	}
}
