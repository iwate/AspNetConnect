package com.aspnetconnect;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class AspNetAuthStore implements IAspNetAuthStore {
    private static final String TOKEN = "token";
    private static final String TOKEN_TYPE = "token_type";
    private static final String EXPIRE = "expire";
    private static final long DEFAULT_EXPIRE = 0;

    private String token;
    private String tokenType;
    private long expire = DEFAULT_EXPIRE;
    private SharedPreferences preferences;

    public AspNetAuthStore(Context context, String prefKey){
        preferences = context.getSharedPreferences(prefKey, Activity.MODE_PRIVATE);
    }

    @Override
    public String getAuthorization() {
        String tokenType = this.getTokenType();
        String token = this.getToken();

        if(tokenType == null){
            return null;
        }
        if(token == null){
            return null;
        }
        return tokenType + " " + token;
    }

    @Override
    public void setAuthorization(String type, String token, long expire) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(TOKEN_TYPE, tokenType);
        editor.putString(TOKEN, token);
        editor.putLong(EXPIRE, expire);
        editor.commit();
        this.tokenType = type;
        this.token = token;
        this.expire = expire;
    }

    @Override
    public boolean isExpiration() {
        return getExpire() < new Date().getTime();
    }

    @Override
    public boolean isNearExpiration(long interval) {
        return getExpire() - new Date().getTime() < interval;
    }

    private String getToken(){
        if(token == null) {
            token = preferences.getString(TOKEN, null);
        }
        return token;
    }
    private String getTokenType(){
        if(tokenType == null){
            tokenType = preferences.getString(TOKEN_TYPE, null);
        }
        return tokenType;
    }
    private long getExpire(){
        if(expire == DEFAULT_EXPIRE){
            expire = preferences.getLong(EXPIRE, DEFAULT_EXPIRE);
        }
        return expire;
    }
}
