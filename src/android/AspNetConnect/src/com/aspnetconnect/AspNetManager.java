package com.aspnetconnect;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AspNetManager {
    public interface LoginListener{
        public void onLogin(AspNetUser user);
    }
    public interface ErrorListener{
        public void onError(VolleyError error);
    }
    public interface NetworkListener{
        public <T> void enqueue(Request<T> request);
    }
    public interface ExternalLoginListener{
        public void onLoadRequest(String url);
    }
    private interface ExternalLoginsListener{
    	public void onGotExternalLogins();
    }
    private static final String EXTERNAL_LOGINS = "api/Account/ExternalLogins?returnUrl=%s&generateState=true";
    private static final String REGISTER = "api/Account/register";
    private static final String LOGIN = "token";

    URL url;
    String callback;
    IAspNetAuthStore store;
    AspNetExternalLogins externalLogins;
    NetworkListener networkListener;
    GsonBuilder gsonBuilder;

    public AspNetManager(String url, IAspNetAuthStore store, NetworkListener listener) throws MalformedURLException{
        this.url = new URL(url);
        this.store = store;
        networkListener = listener;
        gsonBuilder = createGsonBuilder();
    }
    public AspNetManager(String url, String callback, IAspNetAuthStore store, NetworkListener listener) throws MalformedURLException, UnsupportedEncodingException {
        this.url = new URL(url);
        this.callback = URLEncoder.encode(callback, "utf-8");
        this.store = store;
        networkListener = listener;
        gsonBuilder = createGsonBuilder();
    }
    private GsonBuilder createGsonBuilder(){
    	GsonBuilder builder = new GsonBuilder();

		// Register custom date serializer/deserializer
    	builder.registerTypeAdapter(Date.class, new DateSerializer());
		LongSerializer longSerializer = new LongSerializer();
		builder.registerTypeAdapter(Long.class, longSerializer);
		builder.registerTypeAdapter(long.class, longSerializer);

		return builder;
    }
    private Map<String,String> gsonToMap(Object obj){
    	Gson gson = gsonBuilder.create();
    	String json = gson.toJson(obj);
    	Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
    	return gson.fromJson(json, stringStringMap);
    }
    public boolean hasAuthorization(){
    	return store.getAuthorization() != null;
    }
    public static void setAuthorizationFrom(Uri uri, String callback, IAspNetAuthStore store){
    	Uri params = Uri.parse(uri.toString().replaceFirst("#", "?"));
    	if(params != null && params.toString().startsWith(callback)){
            String token = params.getQueryParameter("access_token");
            String type = params.getQueryParameter("token_type");
            long expiresIn = Long.parseLong(params.getQueryParameter("expires_in"));
            store.setAuthorization(type, token, new Date().getTime() + expiresIn);
        }
    }
    public void logout(){
    	store.clearAuthorization();
    }
    public void register(final AspNetRegisterRequest model, final LoginListener loginListener, final ErrorListener errorListener){
    	String endpoint = url.toString() + REGISTER;
    	networkListener.enqueue(new GsonRequest<AspNetRegisterResponse>(gsonBuilder.create()
    			, Request.Method.POST
    			, endpoint
    			, AspNetRegisterResponse.class
    			, null
    			, gsonToMap(model)
    			, new Response.Listener<AspNetRegisterResponse>() {
					@Override
					public void onResponse(AspNetRegisterResponse response) {
						login(new AspNetLoginRequest(model.getUsername(), model.getPassword()), loginListener, errorListener);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						errorListener.onError(error);
					}
				}));
    }
    public void login(AspNetLoginRequest model, final LoginListener loginListener, final ErrorListener errorListener){
    	String endpoint = url.toString() + LOGIN;
    	networkListener.enqueue(new GsonRequest<AspNetLoginResponse>(gsonBuilder.create()
    			, Request.Method.POST
    			, endpoint
    			, AspNetLoginResponse.class
    			, null
    			, gsonToMap(model)
    			, new Response.Listener<AspNetLoginResponse>() {
					@Override
					public void onResponse(AspNetLoginResponse response) {
						store.setAuthorization(
								response.getTokenType()
								, response.getToken()
								, new Date().getTime() + response.getExpiresIn());
						loginListener.onLogin(new AspNetUser(response.getUsername()));
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						errorListener.onError(error);
					}
				}));
    }
    public void loginWith(final AspNetExternalLoginProvider provider, final ExternalLoginListener externalListener, final LoginListener loginListener, final ErrorListener errorListener){
    	if(callback == null){
    		throw new IllegalArgumentException("Callback is null.");
    	}
    	if(externalLogins == null){
    		getExternalLogins(new ExternalLoginsListener(){
				@Override
				public void onGotExternalLogins() {
					_loginWith(provider, externalListener, loginListener, errorListener);
				}}, errorListener);
        }
    	else{
    		_loginWith(provider, externalListener, loginListener, errorListener);
    	}
    }
    private void _loginWith(AspNetExternalLoginProvider provider, ExternalLoginListener externalListener, LoginListener loginListener, ErrorListener errorListener){
    	externalListener.onLoadRequest(externalLogins.getExternalLoginLonk(provider));
    }
    private void getExternalLogins(final ExternalLoginsListener successListener, final ErrorListener errorListener){
    	String endpoint = url.toString() + String.format(EXTERNAL_LOGINS, callback );
    	networkListener.enqueue(new GsonRequest<Collection<AspNetExternalLoginResponse>>(gsonBuilder.create()
    			, Request.Method.GET
    			, endpoint
    			, new TypeToken<Collection<AspNetExternalLoginResponse>>(){}.getType()
    			, null
    			, null
    			, new Response.Listener<Collection<AspNetExternalLoginResponse>>() {
					@Override
					public void onResponse(Collection<AspNetExternalLoginResponse> response) {
						externalLogins = new AspNetExternalLogins();
						Iterator<AspNetExternalLoginResponse> iterator = response.iterator();
						try {
							while(iterator.hasNext()){
								AspNetExternalLoginResponse externalLogin = iterator.next();
								AspNetExternalLoginProvider provider = AspNetExternalLoginProvider.valueOf(externalLogin.getName());
								String endpoint = url.toURI().resolve(externalLogin.getUrl()).toString();
								externalLogins.setExternalLoginLink(provider, endpoint);
							}
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
						successListener.onGotExternalLogins();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError err) {
						errorListener.onError(err);
					}
				}));
    }
    public <T> Request<T> invoke(int method, String route, Class<T> clazz, Map<String,String> headers, Object params, Listener<T> success, Response.ErrorListener error){
    	String endpoint = "";
		try {
			endpoint = url.toURI().resolve(route).toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	if(headers == null){
    		headers = new HashMap<String, String>();
    	}
    	headers.put("Authorization", store.getAuthorization());
    	Request<T> request = new GsonRequest<T>(gsonBuilder.create()
    			, method
    			, endpoint
    			, clazz
    			, headers
    			, gsonToMap(params)
    			, success
    			, error);
    	networkListener.enqueue(request);
    	return request;
    }
    public <T> Request<T> invoke(int method, String route, Type type, Map<String,String> headers, Object params, Listener<T> success, Response.ErrorListener error){
    	String endpoint = "";
		try {
			endpoint = url.toURI().resolve(route).toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	if(headers == null){
    		headers = new HashMap<String, String>();
    	}
    	headers.put("Authorization", store.getAuthorization());
    	Request<T> request = new GsonRequest<T>(gsonBuilder.create()
    			, method
    			, endpoint
    			, type
    			, headers
    			, gsonToMap(params)
    			, success
    			, error);
    	networkListener.enqueue(request);
    	return request;
    }
}
