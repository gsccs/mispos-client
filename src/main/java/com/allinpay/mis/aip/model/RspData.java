package com.allinpay.mis.aip.model;

import java.util.HashMap;
import java.util.Map;

public class RspData {
	private Map<String, String> response = new HashMap();

	public Map<String, String> getResponse() {
		return this.response;
	}

	public String GetValue(String key) {
		key = key.toLowerCase().trim();
		if (this.response.containsKey(key)) {
			return (String) this.response.get(key);
		}
		return "";
	}

	public void PutValue(String key, String value) {
		key = key.toLowerCase().trim();
		this.response.put(key, null == value ? "" : value.trim());
	}
}
