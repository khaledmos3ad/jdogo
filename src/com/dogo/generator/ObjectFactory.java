package com.dogo.generator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectFactory {

	@SuppressWarnings("rawtypes")
	private Class rootClass;
	private List<String> excludedClasses;
	private Map<String, List<String>> excludedFields;

	@SuppressWarnings("rawtypes")
	public ObjectFactory(Class clazz) {
		this.rootClass = clazz;
		excludedClasses = new ArrayList<String>();
		excludedFields = new HashMap<String, List<String>>();
	}

	@SuppressWarnings("rawtypes")
	public String buildHeader(Class clazz) throws Exception {
		StringBuilder builder = new StringBuilder();
		Field[] fieldList = clazz.getDeclaredFields();
		int iterate = 0;
		for (Field field : fieldList) {
			boolean customField = isCustomField(field);
			if (customField) {
				Class currentClass = Class.forName(getPackagePath(field.getGenericType().toString()));
				String generatedHeader = generateCustomClassHeader(currentClass);
				if (generatedHeader.isEmpty()) {
					builder.deleteCharAt(builder.length() - 1);
				}
				builder.append(generatedHeader);
			} else {
				builder.append(field.getName());
			}
			if (iterate++ != fieldList.length - 1) {
				builder.append("|");
			}
		}
		return builder.toString();
	}

	@SuppressWarnings("rawtypes")
	private String generateCustomClassHeader(Class clazz) throws Exception {
		StringBuilder builder = new StringBuilder();
		Field[] fieldList = getIncludedAttributes(clazz);
		if (!excludedClasses.contains(clazz.getSimpleName().toString())) {
			int iterate = 0;
			builder.append("#" + removeInitCap(clazz.getSimpleName()) + "<");
			for (Field field : fieldList) {
				boolean customField = isCustomField(field);
				if (customField) {
					Class currentClass = Class.forName(getPackagePath(field.getGenericType().toString()));
					String generatedHeader = generateCustomClassHeader(currentClass);
					if (generatedHeader.isEmpty()) {
						builder.deleteCharAt(builder.length() - 1);
					}
					builder.append(generatedHeader);
				} else {
					builder.append(field.getName());
				}
				if (iterate++ != fieldList.length - 1) {
					builder.append("!");
				}
			}
			builder.append(">");
		} else {
		}
		return builder.toString();
	}

	@SuppressWarnings("rawtypes")
	private Field[] getIncludedAttributes(Class clazz) {
		Field[] fieldList = clazz.getDeclaredFields();
		List<String> excludes = excludedFields.get(clazz.getSimpleName());
		Field[] includedAttributes = null;
		if (excludes != null) {
			Field[] included = new Field[(fieldList.length - excludes.size())];
			int iterate = 0;
			for (Field field : fieldList) {
				if (!excludes.contains(field.getName())) {
					included[iterate++] = field;
				}
			}
			includedAttributes = included;
		} else {
			includedAttributes = fieldList;
		}
		return includedAttributes;
	}

	public String fillObject(Object obj) throws Exception {
		StringBuilder builder = new StringBuilder();
		Field[] fieldList = this.rootClass.getDeclaredFields();
		int iterate = 0;
		for (Field field : fieldList) {
			String getterMethod = "get" + initCap(field.getName());
			Class<?> fieldType = field.getType();
			boolean customField = isCustomField(field);
			if (customField) {
				Method method = obj.getClass().getMethod(getterMethod, new Class<?>[0]);
				Object theObject = (Object) method.invoke(obj);
				String generatedHeader = fillCustomObject(theObject);
				if (generatedHeader.isEmpty()) {
					builder.deleteCharAt(builder.length() - 1);
				}
				builder.append(generatedHeader);
			} else {
				builder.append(getNativeValueFromObject(obj, fieldType.getSimpleName(), field.getName()));
			}
			if (iterate++ != fieldList.length - 1) {
				builder.append("|");
			}
		}
		return builder.toString();
	}

	private String fillCustomObject(Object object) throws Exception {
		StringBuilder builder = new StringBuilder();
		Field[] fieldList = getIncludedAttributes(object.getClass());
		if (!excludedClasses.contains(object.getClass().getSimpleName().toString())) {
			builder.append("#<");
			int iterate = 0;
			for (Field field : fieldList) {
				String getterMethod = "get" + initCap(field.getName());
				Class<?> fieldType = field.getType();
				boolean customField = isCustomField(field);
				if (customField) {
					Method method = object.getClass().getMethod(getterMethod, new Class<?>[0]);
					Object currentObject = (Object) method.invoke(object);
					String generatedHeader = fillCustomObject(currentObject);
					if (generatedHeader.isEmpty()) {
						builder.deleteCharAt(builder.length() - 1);
					}
					builder.append(generatedHeader);
				} else {
					builder.append(getNativeValueFromObject(object, fieldType.getSimpleName(), field.getName()));
				}
				if (iterate++ != fieldList.length - 1) {
					builder.append("!");
				}
			}
			builder.append(">");
		}
		return builder.toString();
	}

	public String getNativeValueFromObject(Object object, String fieldType, String fieldName) throws Exception {
		if ("int".equals(fieldType) || "Integer".equals(fieldType)) {			
			int value = 0;
			String getterMethod = "get" + initCap(fieldName);
			Method m = object.getClass().getMethod(getterMethod, new Class<?>[0]);
			value = (int) m.invoke(object);
			return value + "";
		} else if ("long".equals(fieldType) || "Long".equals(fieldType)) {
			long value = 0;
			String getterMethod = "get" + initCap(fieldName);
			Method m = object.getClass().getMethod(getterMethod, new Class<?>[0]);
			value = (long) m.invoke(object);
			return value+"";
		} else if ("String".equals(fieldType)) {
			String value = "";
			String getterMethod = "get" + initCap(fieldName);
			Method m = object.getClass().getMethod(getterMethod, new Class<?>[0]);
			value = (String) m.invoke(object);
			return value;
		} else if ("double".equals(fieldType) || "Double".equals(fieldType)) {
			double value = 0.0;
			String getterMethod = "get" + initCap(fieldName);
			Method m = object.getClass().getMethod(getterMethod, new Class<?>[0]);
			value = (double) m.invoke(object);
			return value + "";
		} else if ("float".equals(fieldType) || "Float".equals(fieldType)) {
			float value = 0f;
			String getterMethod = "get" + initCap(fieldName);
			Method m = object.getClass().getMethod(getterMethod, new Class<?>[0]);
			value = (float) m.invoke(object);
			return value + "";
		} else if ("boolean".equals(fieldType) || "Boolean".equals(fieldType)) {
			boolean value = false;
			String getterMethod = "get" + initCap(fieldName);
			if("boolean".equals(fieldType)){
				getterMethod = "is" + initCap(fieldName);
			}
			Method m = object.getClass().getMethod(getterMethod, new Class<?>[0]);
			value = (boolean) m.invoke(object);
			return value + "";
		}
		return "";
	}

	public void excludeObject(String className) {
		excludedClasses.add(className);
	}

	public void excludeFieldsFromObject(String className, List<String> fields) {
		excludedFields.put(className, fields);
	}

	private boolean isCustomField(Field field) {
		List<String> dataTypeClasses = Arrays.asList("int", "double", "long", "float", "boolean", "String", "Integer",
				"Double", "Long", "Float", "Boolean", "Date");
		String typeString = field.getGenericType().toString();
		if (typeString.contains(" ")) {
			String className = getPackagePath(typeString).substring((getPackagePath(typeString).lastIndexOf('.') + 1));
			if (!dataTypeClasses.contains(className)) {
				return true;
			}
		}
		return false;
	}

	private String getPackagePath(String classType) {
		String packageName = classType.substring(classType.indexOf(' '));
		return packageName.trim();
	}

	private String removeInitCap(String word) {
		return Character.toLowerCase(word.charAt(0)) + word.substring(1);
	}

	private String initCap(String word) {
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}

}
