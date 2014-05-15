package com.example.demoflavius.data;

public class Message 
{
	String id;
	String content;
	String fromId;
	String fromName;
	String fromPicture;
	String toId;
	Enums.DeviceType deviceType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getToId() {
		return toId;
	}
	public void setToId(String toId) {
		this.toId = toId;
	}
	public Enums.DeviceType getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Enums.DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	public String getFromPicture() {
		return fromPicture;
	}
	public void setFromPicture(String fromPicture) {
		this.fromPicture = fromPicture;
	}
}
