package com.jdogo.response;

import java.util.List;

public class JDOCParser {

	private DOCModel docModel= null;
	
    public JDOCParser(){
    	
    }

    @SuppressWarnings("rawtypes")
	public Object getObject(Class clazz,String jsopCode) throws Exception{
    	Object object = null;
    	docModel = new DOCModel(jsopCode, "one"); 
		String data=docModel.getData();
		String header=docModel.getHeader();
		ObjectFactory parser =  new ObjectFactory();
		object = parser.getObject(clazz, header, data);
		return object;
    }
   
    @SuppressWarnings("rawtypes")
	public Object getObjectList(Class clazz,String jsopCode) throws Exception{
    	List<Object> objects = null;
    	docModel = new DOCModel(jsopCode, "list"); 
		List<String> data=docModel.getDataList();
		String header=docModel.getHeader();
		ObjectFactory parser =  new ObjectFactory();
		objects = parser.getObjectList(clazz, header, data);
		return objects;
    }
}
