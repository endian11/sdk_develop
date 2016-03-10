package com.travelrely.model;

import com.travelrely.core.util.TimeUtil;

public class OrderModel extends Model {

	String name;

	long time;

	String date;

	String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		date = TimeUtil.getDateString(time, TimeUtil.dateFormat4);
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
