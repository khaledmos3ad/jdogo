package com.jdogo.response;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class DOCModel {

	private String header; 
	private List<String> dataList;
	private String data;
	private String status;
	
	public DOCModel(String jsonCode,String type){
		
		this.dataList = new ArrayList<String>();
		
		JSONObject jsonObject = new JSONObject(jsonCode);
		this.status=jsonObject.get("Status").toString();
		this.header=jsonObject.get("Header").toString();
		if(type.equalsIgnoreCase("one")){
			this.data = jsonObject.get("Data").toString();
		}
		if(type.equalsIgnoreCase("list")){
			JSONArray jsonArray=jsonObject.getJSONArray("Data");
			for(int i=0;i<jsonArray.length();i++){
				this.dataList.add(jsonArray.getString(i));
			}
		}
	}
	
    public String getHeader(){
        return this.header; 
    }
	
    public List<String> getDataList(){
    	return this.dataList;
    }
    
    public String getData(){
    	return this.data;
    }
    
    public String getStatus(){
    	return this.status;
    }
   
}
