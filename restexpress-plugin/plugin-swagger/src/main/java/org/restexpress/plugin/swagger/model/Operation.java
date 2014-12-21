package org.restexpress.plugin.swagger.model;

import java.util.List;

import com.google.common.collect.Lists;


public class Operation {
	String method;
	String summary;
	String notes;
	String responseClass;
	String nickname;
	int position;
	List<String> produces = Lists.newArrayList();
	List<String> consumes = Lists.newArrayList();
	List<String> protocols = Lists.newArrayList();

	List<Authorization> authorizations = Lists.newArrayList();
	List<Parameter> parameters = Lists.newArrayList();
	List<ResponseMessage> responseMessages = Lists.newArrayList();
	String deprecated = null;

}
