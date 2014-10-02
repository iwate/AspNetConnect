package com.aspnetconnect;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonRequest<T> extends Request<T> { 
    private final Gson gson; 
    private final Class<T> clazz;
    private final Type type;
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Listener<T> listener; 

    /** 
     * Make a GET request and return a parsed object from JSON. 
     * 
     * @param url URL of the request to make 
     * @param clazz Relevant class object, for Gson's reflection 
     * @param headers Map of request headers 
     */ 
    public GsonRequest(Gson gson, int method, String url, Class<T> clazz, Map<String, String> headers, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) { 
        super(method, url, errorListener); 
        this.gson = gson;
        this.clazz = clazz;
        this.type = null;
        this.headers = headers; 
        this.params = params;
        this.listener = listener; 
    }
    public GsonRequest(Gson gson, int method, String url, Type type, Map<String, String> headers, Map<String, String> params, Listener<T> listener, ErrorListener errorListener) { 
        super(method, url, errorListener); 
        this.gson = gson;
        this.clazz = null; 
        this.type = type;
        this.headers = headers; 
        this.params = params;
        this.listener = listener; 
    } 
    
    @Override 
    public Map<String, String> getHeaders() throws AuthFailureError { 
        return headers != null ? headers : super.getHeaders();
    }
    
    @Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}

    @Override 
    protected void deliverResponse(T response) { 
        listener.onResponse(response); 
    }
    
    @SuppressWarnings("unchecked")
	@Override 
    protected Response<T> parseNetworkResponse(NetworkResponse response) { 
        try {
        	String json = "";
        	if(clazz != null){
        		json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        		return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        	}
        	if(type != null){
        		json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        		return Response.success((T)gson.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
        	}
        	return Response.error(new ParseError());
        } catch (UnsupportedEncodingException e) { 
            return Response.error(new ParseError(e)); 
        } catch (JsonSyntaxException e) { 
            return Response.error(new ParseError(e)); 
        } 
    }
} 

