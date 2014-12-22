package org.restexpress.plugin.swagger.model;

public class AllowableRangeValues  implements AllowableValues {
	private String min, max;

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}
	
	
}
