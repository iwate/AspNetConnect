package com.aspnetconnect;

public class AspNetUser {
    private String id;
    private String username;
    
    public AspNetUser(String username){
    	this.id = "";
    	this.username = username;
    }
    public AspNetUser(String id, String username){
    	this.id = id;
    	this.username = username;
    }
    public String getId(){
        return id;
    }
    public String getUsername(){
        return username;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setUserName(String userName){
        this.username = userName;
    }
}
