package com.allinpay.mis.aip.model;

import java.util.HashMap;
import java.util.Map;

public class ReqData {
	
	private Map<String, String> request = new HashMap();

	public Map<String, String> getRequest() {
		return this.request;
	}

	public String GetValue(String key) {
		key = key.toLowerCase().trim();
		if (this.request.containsKey(key)) {
			return (String) this.request.get(key);
		}
		return "";
	}

	public void PutValue(String key, String value) {
		key = key.toLowerCase().trim();
		this.request.put(key, value.trim());
	}
}
