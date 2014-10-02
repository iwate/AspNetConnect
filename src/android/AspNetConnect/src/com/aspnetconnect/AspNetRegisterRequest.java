package com.aspnetconnect;

import com.google.gson.annotations.SerializedName;

public class AspNetRegisterRequest {
	@SerializedName("Email")
	private String email;
	@SerializedName("UserName")
	private String username;
	@SerializedName("Password")
	private String password;
	@SerializedName("ConfirmPassword")
	private String confirmPassword;
	public AspNetRegisterRequest(String email, String username, String password, String confirmPassword){
		this.email = email;
		this.username = username;
		this.password = password;
		this.confirmPassword = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
