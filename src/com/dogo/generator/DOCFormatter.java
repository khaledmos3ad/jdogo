package com.dogo.generator;

import java.util.List;
//////////////////////////////////////////////////////////////////////// 
/* 
 *  Class         : JDocBuilder 
 *  Description   : JDocBuilder used to build the JDOC Request Format
 *  Dev Date      : 
 *  Last Modified : (Optional) 
 *  Reason        : (Optional)  
 */
////////////////////////////////////////////////////////////////////////

public class DOCFormatter {

	private String header ;   

	public DOCFormatter(){}
	
	public DOCFormatter(String header){
		 this.header = header;
	}
	
	public void setHeader(String header){
		 this.header = header;
	}

	public String buildJDOCObject(String objectData){
		String jdoc = "{\"Header\":\""+this.header
				 +"\",\"Data\":\""+objectData+"\",\"Status\":"+"\"200|Ok\"}";
		return jdoc;
	}
	
	public String buildJDOCObjectList(List<String> objectsData){
		StringBuilder builder = new StringBuilder();
		builder.append("{\"Header\":\""+this.header+"\",\"Data\":[");
		        int iterate=0;
				for(String objectData : objectsData){
					builder.append("\""+objectData+"\"");
					if(iterate++ != objectsData.size()-1){
						builder.append(",");
					}
				}
				builder.append("],\"Status\":"+"\"200|Ok\"}");
		return builder.toString();
	}
}