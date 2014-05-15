package com.example.demoflavius.data;

import com.example.demoflavius.data.Enums.IdentityProvider;

public class UserEntity {
	private String id;
	private String name;
	private String picture;
	private String location;
	private String token;
	private String providerIdLong;
	private String providerIdShort;
	private IdentityProvider identityProvider;
	private String accessToken;
	private String accessTokenSecret;
	
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
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getProviderIdLong() {
		return providerIdLong;
	}
	public void setProviderIdLong(String providerIdLong) {
		this.providerIdLong = providerIdLong;
	}
	public String getProviderIdShort() {
		return providerIdShort;
	}
	public void setProviderIdShort(String providerIdShort) {
		this.providerIdShort = providerIdShort;
	}
	public IdentityProvider getIdentityProvider() {
		return identityProvider;
	}
	public void setIdentityProvider(IdentityProvider identityProvider) {
		this.identityProvider = identityProvider;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
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
