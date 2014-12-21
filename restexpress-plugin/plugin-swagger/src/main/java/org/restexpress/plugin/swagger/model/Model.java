package org.restexpress.plugin.swagger.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Model {

	String id;
	String name;
	String qualifiedType;
	Map<String, ModelProperty> properties = Maps.newLinkedHashMap();
	String description = null;
	String baseModel = null;
	String discriminator = null;
	List<String> subTypes = Lists.newArrayList();

}
