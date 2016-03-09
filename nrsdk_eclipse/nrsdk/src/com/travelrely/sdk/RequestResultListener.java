package com.travelrely.sdk;

import com.travelrely.net.response.ResponseInfo;

public interface RequestResultListener {
	public void success(ResponseInfo responseInfo);
	public void fail(ResponseInfo responseInfo);
}