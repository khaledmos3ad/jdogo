package com.dogo.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dogo.enums.DocSection;
import com.dogo.enums.DocType;

public class DocObject {

	private String header;
	private List<String> dataList = new ArrayList<String>();
	private String data;

	public DocObject(DocObjectBuilder docObjectBuilder) {
		this.header = docObjectBuilder.header;
		this.data = docObjectBuilder.data;
		this.dataList = docObjectBuilder.dataList;
	}

	public String getHeader() {
		return this.header;
	}

	public List<String> getDataList() {
		return this.dataList;
	}

	public String getData() {
		return this.data;
	}

	public String getDOCObject(String objectData) {
		return getDocStructure(objectData);
	}

	public String getDOCObjectList(List<String> objectsData) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int iterate = 0;
		for (String objectData : objectsData) {
			builder.append("\"" + objectData + "\"");
			if (iterate++ != objectsData.size() - 1) {
				builder.append(",");
			}
		}
		builder.append("]");
		return getDocStructure(builder.toString());
	}

	private String getDocStructure(String data) {
		String doc = "{\"Header\":\"" + this.header + "\",\"Data\":\"" + data + "\"}";
		return doc;
	}
	
	@Override
	public String toString() {
		return "DocObject [header=" + header + ", dataList=" + dataList + ", data=" + data + "]";
	}



	public static class DocObjectBuilder {

		private String header;
		private String data;
		private List<String> dataList;
		private DocType docType;

		/*
		 * Constructors*
		 */

		public DocObjectBuilder() {
		}

		public DocObjectBuilder(DocType docType) {
			this.docType = docType;
		}

		/*
		 * Setters *
		 */
		public DocObjectBuilder setHeader(String header) {
			this.header = header;
			return this;
		}

		public DocObjectBuilder setObjectData(String objectData) {
			this.data = objectData;
			return this;
		}

		public DocObjectBuilder setObjectDataList(List<String> objectDataList) {
			this.dataList = objectDataList;
			return this;
		}

		public DocObjectBuilder setDocJsonObject(JSONObject jsonObject) {
			initDocFromJson(jsonObject);
			return this;
		}

		public DocObjectBuilder setDocJsonString(String jsonCode) {
			JSONObject jsonObject = new JSONObject(jsonCode);
			initDocFromJson(jsonObject);
			return this;
		}

		public DocObject build() {
			return new DocObject(this);
		}

		private void initDocFromJson(JSONObject jsonObject) {
			this.header = jsonObject.get(DocSection.HEADER.getValue()).toString();
			if (docType.name().equalsIgnoreCase(DocType.ONE.name())) {
				this.data = jsonObject.get(DocSection.DATA.getValue()).toString();
			}
			if (docType.name().equalsIgnoreCase(DocType.LIST.name())) {
				JSONArray jsonArray = jsonObject.getJSONArray(DocSection.DATA.getValue());
				for (int i = 0; i < jsonArray.length(); i++) {
					this.dataList.add(jsonArray.getString(i));
				}
			}
		}

	}

}
