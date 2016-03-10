package com.travelrely.model;

import java.io.Serializable;

public class Area extends Model implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int _ID;
	public String id;
	public String name;
	public int get_ID() {
		return _ID;
	}
	public void set_ID(int _ID) {
		this._ID = _ID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFather() {
		return father;
	}
	public void setFather(String father) {
		this.father = father;
	}
	public String father;
}
