package com.dogo.generator;

import java.util.ArrayList;
import java.util.List;

public class JDOCGenerator {

	private ObjectFactory objectFactory;
	@SuppressWarnings("rawtypes")
	private Class clazz;

	public final int EXCLUDE_ALL = 1;
	public final int INCLUDE_ALL = 2;

	@SuppressWarnings("rawtypes")
	public JDOCGenerator(Class clazz) {
		this.clazz = clazz;
		objectFactory = new ObjectFactory(clazz);
	}

	public void excludeObject(String className) {
		objectFactory.excludeObject(className);
	}

	public void excludeFieldsFromObject(String className, String... args) {
		List<String> fields = new ArrayList<String>();
		for (String field : args) {
			fields.add(field);
		}
		objectFactory.excludeFieldsFromObject(className, fields);
	}

	public String generateObject(Object object) throws Exception{
		String header = objectFactory.buildHeader(this.clazz);
		String data = objectFactory.fillObject(object);
		DOCFormatter docFormatter = new DOCFormatter(header);
		String JDOC = docFormatter.buildJDOCObject(data);
		return JDOC;
	}

	public String generateObjectList(List<? extends Object> objects) throws Exception{
		List<String> dataList = new ArrayList<String>();
		String header = objectFactory.buildHeader(this.clazz);
		for (Object object : objects) {
			String data = objectFactory.fillObject(object);
			dataList.add(data);
		}
		DOCFormatter docFormatter = new DOCFormatter(header);
		String JDOC = docFormatter.buildJDOCObjectList(dataList);
		return JDOC;
	}
}
