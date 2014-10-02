package com.aspnetconnect;

public interface IAspNetAuthStore {
    public String getAuthorization();
    public void setAuthorization(String type, String token, long expire);
    public boolean isExpiration();
    public boolean isNearExpiration(long interval);
}
