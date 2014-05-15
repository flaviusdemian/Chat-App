package com.example.demoflavius.data;

public class Friend 
{
	private String id;
	private String name;
	private String picture;
	private String location;
	

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
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getLocation() {
		if( location == null)
		{
			return "Timisoara, Romania";
		}
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
