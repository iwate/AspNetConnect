package com.aspnetconnect;

import com.google.gson.annotations.SerializedName;

public class AspNetExternalLoginResponse {
	@SerializedName("name")
	private String name;
	@SerializedName("url")
	private String url;
	@SerializedName("state")
	private String state;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
