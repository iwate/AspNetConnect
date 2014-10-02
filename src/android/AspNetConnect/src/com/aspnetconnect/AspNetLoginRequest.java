package com.aspnetconnect;

import com.google.gson.annotations.SerializedName;

public class AspNetLoginRequest {
	private static final String GRANT_TYPE = "password";
	@SerializedName("grant_type")
	private String grantType;
	@SerializedName("username")
	private String username;
	@SerializedName("password")
	private String password;
	public AspNetLoginRequest(String username, String password) {
		this.grantType = GRANT_TYPE;
		this.username = username;
		this.password = password;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
