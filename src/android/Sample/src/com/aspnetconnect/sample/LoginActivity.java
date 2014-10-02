package com.aspnetconnect.sample;

import java.net.MalformedURLException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aspnetconnect.AspNetAuthStore;
import com.aspnetconnect.AspNetManager;

import android.app.Activity;
import android.os.Bundle;

public class LoginActivity extends Activity implements AspNetManager.NetworkListener{
	private static final String API_URL = "http://localhost:9000/";
	private static final String APP_UNIQUE = "com:aspnetconnect:sample";
	RequestQueue queue;
	AspNetManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		queue = Volley.newRequestQueue(this);
		try {
			manager = new AspNetManager(API_URL, new AspNetAuthStore(this, APP_UNIQUE), this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> void enqueue(Request<T> request) {
		queue.add(request);
	}
}
