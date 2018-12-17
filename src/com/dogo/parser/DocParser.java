package com.dogo.parser;

import java.util.List;

import com.dogo.enums.DocType;
import com.dogo.model.DocObject;
import com.dogo.parser.DocAnalyzer;

public class DocParser {

	@SuppressWarnings("rawtypes")
	public Object getObject(Class clazz, String jsonCode) throws Exception {
		Object object = null;
		
		DocObject docObject = new DocObject.DocObjectBuilder(DocType.ONE)
				.setDocJsonString(jsonCode).build();
		
		DocAnalyzer parser = new DocAnalyzer();
		object = parser.getObject(clazz, docObject);
		return object;
	}

	@SuppressWarnings("rawtypes")
	public Object getObjectList(Class clazz, String jsonCode) throws Exception {
		List<Object> objects = null;
		
		DocObject docObject = new DocObject.DocObjectBuilder(DocType.LIST)
				.setDocJsonString(jsonCode).build();
		
		DocAnalyzer parser = new DocAnalyzer();
		objects = parser.getObjectList(clazz, docObject);
		return objects;
	}

}
