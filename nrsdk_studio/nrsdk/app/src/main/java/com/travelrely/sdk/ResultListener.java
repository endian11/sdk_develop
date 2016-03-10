package com.travelrely.sdk;

import org.json.JSONObject;

public interface ResultListener {
	public void success(JSONObject SuccObj);
	public void fail(JSONObject ErrorObj);
}