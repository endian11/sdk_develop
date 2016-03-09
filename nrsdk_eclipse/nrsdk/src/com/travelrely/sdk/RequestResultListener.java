package com.travelrely.sdk;

import com.travelrely.core.glms.response.ResponseInfo;

public interface RequestResultListener {
	public void success(ResponseInfo responseInfo);
	public void fail(ResponseInfo responseInfo);
}