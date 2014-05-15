package com.example.demoflavius.data;

public class Channel {

	private String id;
	private Enums.DeviceType deviceType;
	private String channelUri;
	private String registrationId;
	private String userId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Enums.DeviceType getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Enums.DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	public String getChannelUri() {
		return channelUri;
	}
	public void setChannelUri(String channelUri) {
		this.channelUri = channelUri;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
