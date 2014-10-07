package com.aspnetconnect.sample;

import java.util.Date;

public class Todo {
	public Todo(){}
	public Todo(String id, String userId, String title, boolean isChecked,
			Date createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.isChecked = isChecked;
		this.createdAt = createdAt;
	}
	private String id;
	private String userId;
	private String title;
	private boolean isChecked;
	private Date createdAt;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean getIsChecked() {
		return isChecked;
	}
	public void setIsChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
