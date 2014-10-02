package com.aspnetconnect;

import com.google.gson.annotations.SerializedName;

public class AspNetLoginResponse {
	@SerializedName("access_token")
	private String token;
	@SerializedName("token_type")
	private String tokenType;
	@SerializedName("expires_in")
	private long expiresIn;
	@SerializedName("userName")
	private String username;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
