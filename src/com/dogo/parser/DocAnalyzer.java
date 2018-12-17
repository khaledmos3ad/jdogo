package com.dogo.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.dogo.model.DocObject;
import com.dogo.parser.enums.DocDelimiter;

public class DocAnalyzer {

	@SuppressWarnings("rawtypes")
	public List<Object> getObjectList(Class clazz, DocObject docObject) throws Exception {
		List<Object> objects = new ArrayList<>();
		List<String> dataList = docObject.getDataList();
		for (int index = 0; index < dataList.size(); index++) {
			DocObject newDoc = new DocObject.DocObjectBuilder().setHeader(docObject.getHeader())
					.setObjectData(dataList.get(index)).build();
			Object obj = getObject(clazz, newDoc);
			objects.add(obj);
		}
		return objects;
	}

	@SuppressWarnings("rawtypes")
	public Object getObject(Class clazz, DocObject docObject) throws Exception {
		String[] headers = getHeaderAttributes(docObject.getHeader());
		Method[] methods = getIncludedSetterMethods(clazz, headers);
		String data = checkMissingData(docObject.getData());
		String[] dataSections = tokenizeDataSection(data);
		Object object = null;
		object = clazz.newInstance();
		for (int i = 0; i < methods.length; i++) {
			System.out.println(methods[i].getName());
			Class<?>[] params = methods[i].getParameterTypes();

			String type = params[0].getSimpleName();
			if (!isCustomHeader(headers[i])) {
				executeFilling(object, methods[i], type, dataSections[i]);
			} else {
				Object customObject = getCustomObject(params[0], headers[i], dataSections[i]);
				object.getClass().getMethod(methods[i].getName(), params[0]);
				methods[i].invoke(object, customObject);
			}
		}

		return object;
	}

	private void executeFilling(Object object, Method method, String type, String dataSection) throws Exception {
		if (type.equals("int")) {
			object.getClass().getMethod(method.getName(), int.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Integer.parseInt(dataSection));
			}
		} else if (type.equals("Integer")) {
			//object.getClass().getMethod(method.getName(), Integer.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Integer.parseInt(dataSection));
			}
		} else if (type.equals("long")) {
			object.getClass().getMethod(method.getName(), long.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Long.parseLong(dataSection));
			}
		} else if (type.equals("Long")) {
			object.getClass().getMethod(method.getName(), Long.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Long.parseLong(dataSection));
			}
		} else if (type.equals("double")) {
			object.getClass().getMethod(method.getName(), double.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Double.parseDouble(dataSection));
			}
		} else if (type.equals("Double")) {
			object.getClass().getMethod(method.getName(), Double.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Double.parseDouble(dataSection));
			}
		} else if (type.equals("float")) {
			object.getClass().getMethod(method.getName(), float.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Float.parseFloat(dataSection));
			}
		} else if (type.equals("Float")) {
			object.getClass().getMethod(method.getName(), Float.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Float.parseFloat(dataSection));
			}
		} else if (type.equals("boolean")) {
			object.getClass().getMethod(method.getName(), boolean.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Boolean.valueOf(dataSection));
			}
		} else if (type.equals("Boolean")) {
			object.getClass().getMethod(method.getName(), Boolean.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, Boolean.valueOf(dataSection));
			}
		} else if (type.equals("String")) {
			object.getClass().getMethod(method.getName(), String.class);
			if (!dataSection.equals("-")) {
				method.invoke(object, dataSection);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public Object getCustomObject(Class clazz, String header, String dataTobeInserted) throws Exception {

		String customHeader = changeDelimeter(modulingCustomHeader(header));
		String customData = changeDelimeter(modulingCustomHeader(dataTobeInserted));

		DocObject docObject = new DocObject.DocObjectBuilder().setHeader(customHeader).setObjectData(customData)
				.build();
		return getObject(clazz, docObject);
	}

	@SuppressWarnings("rawtypes")
	private Method[] getIncludedSetterMethods(Class clazz, String[] headers) {
		Method[] allMethods = clazz.getDeclaredMethods();
		Method[] setterMethods = new Method[headers.length];
		for (int i = 0; i < headers.length; i++) {
			String currentHeader = headers[i];
			if (isCustomHeader(currentHeader)) {
				currentHeader = initCapString(getObjectNameFromHeader(currentHeader));
			}
			for (int j = 0; j < allMethods.length; j++) {
				if (allMethods[j].getName().equals("set" + currentHeader)) {
					setterMethods[i] = allMethods[j];
				}
			}
		}
		return setterMethods;
	}

	private String[] tokenizeDataSection(String dataSection) {
		StringTokenizer st = new StringTokenizer(dataSection, DocDelimiter.SPLIT_DELIMETER.getValue());
		String[] dataSections = new String[st.countTokens()];
		int index = 0;
		while (st.hasMoreTokens()) {
			String currentdataSection = st.nextToken();
			dataSections[index++] = currentdataSection;
		}
		return dataSections;
	}

	private String[] getHeaderAttributes(String header) {
		System.out.println(">> SPLITTED HEADER "+header);
		StringTokenizer st = new StringTokenizer(header, DocDelimiter.SPLIT_DELIMETER.getValue());
		String[] headers = new String[st.countTokens()];
		int index = 0;
		while (st.hasMoreTokens()) {
			String currentHeader = initCapString(st.nextToken());
			headers[index++] = currentHeader;
		}
		return headers;
	}

	public boolean isCustomHeader(String headerString) {
		boolean isCustom = false;
		if (headerString.charAt(0) == '#') {
			isCustom = true;
		}
		return isCustom;
	}

	public String modulingCustomHeader(String header) {
		return header.substring(header.indexOf('<') + 1, header.lastIndexOf('>'));
	}

	public String changeDelimeter(String customString) {
		StringBuilder stBuilder = new StringBuilder();
		for (int i = 0; i < customString.length(); i++) {
			if (customString.charAt(i) == '!') {
				if (customString.charAt(i + 1) == '#') {
					stBuilder.append(DocDelimiter.SPLIT_DELIMETER.getValue());
					int jumpPosition = customString.indexOf(">", i);
					stBuilder.append(customString.substring(i + 1, jumpPosition + 1));
					i = jumpPosition;
				} else {
					stBuilder.append(DocDelimiter.SPLIT_DELIMETER.getValue());
				}
			} else {
				stBuilder.append(customString.charAt(i));
			}
		}
		return stBuilder.toString();
	}

	public String checkMissingData(String rawData) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < rawData.length(); i++) {
			if (i == 0) {
				if (rawData.charAt(i) == DocDelimiter.SPLIT_DELIMETER.getValue().charAt(0)) {
					sb.append("-");
				}
			}
			sb.append(rawData.charAt(i));
			if (i == rawData.length() - 1) {
				if (rawData.charAt(i) == DocDelimiter.SPLIT_DELIMETER.getValue().charAt(0)) {
					sb.append("-");
				}
			} else {
				if ((rawData.charAt(i) == DocDelimiter.SPLIT_DELIMETER.getValue().charAt(0))
						&& (rawData.charAt(i + 1) == DocDelimiter.SPLIT_DELIMETER.getValue().charAt(0))) {
					sb.append("-");
				}
			}
		}
		return sb.toString();
	}

	private String initCapString(String word) {
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}

	private String getObjectNameFromHeader(String headerString) {
		System.out.println(headerString);
		char delimeter = '<';
		if (headerString.contains("$") && (!headerString.contains("<"))) {
			delimeter = '$';
		}
		return headerString.substring(1, headerString.indexOf(delimeter));
	}
}
