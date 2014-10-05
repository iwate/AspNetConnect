package com.aspnetconnect.sample;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.aspnetconnect.AspNetAuthStore;
import com.aspnetconnect.AspNetExternalLoginProvider;
import com.aspnetconnect.AspNetLoginRequest;
import com.aspnetconnect.AspNetManager;
import com.aspnetconnect.AspNetRegisterRequest;
import com.aspnetconnect.AspNetUser;
import com.aspnetconnect.sample.LoginFragment.LoginFragmentListener;
import com.aspnetconnect.sample.RegisterFragment.RegisterFragmentListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class LoginActivity extends Activity implements AspNetManager.NetworkListener
													, AspNetManager.LoginListener
													, AspNetManager.ErrorListener
													, LoginFragmentListener
													, RegisterFragmentListener{
	RequestQueue queue;
	AspNetManager manager;
	FragmentManager fragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		queue = Volley.newRequestQueue(this);
		try {
			manager = new AspNetManager(
					getString(R.string.api_url)
					, new AspNetAuthStore(this, getString(R.string.app_unique))
					, this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(manager.hasAuthorization()){
			moveTo(MainActivity.class);
		}
		else {
			if (savedInstanceState == null) {
				fragmentManager = this.getFragmentManager();
				fragmentManager.beginTransaction()
						.add(R.id.container, new RegisterFragment()).commit();
			}
		}
	}

	@Override
	public <T> void enqueue(Request<T> request) {
		queue.add(request);
	}
	
	private <T> void moveTo(Class<T> clazz){
		Intent intent = new Intent(getApplicationContext(),clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		finish();
	}

	@Override
	public void onLogin(AspNetLoginRequest model) {
		manager.login(model, this, this);
	}

	@Override
	public void onLoginWith(AspNetExternalLoginProvider provider) {
		manager.loginWith(provider, this, this);
	}

	@Override
	public void swapToRegister() {
		fragmentManager.beginTransaction()
			.replace(R.id.container, new RegisterFragment()).commit();
	}

	@Override
	public void onRegister(AspNetRegisterRequest model) {
		manager.register(model, this, this);
	}

	@Override
	public void swapToLogin() {
		fragmentManager.beginTransaction()
		.replace(R.id.container, new LoginFragment()).commit();
	}

	@Override
	public void onError(VolleyError error) {
		try {
			String message = new String(error.networkResponse.data, "UTF-8");
			Toast.makeText(this, message, MODE_PRIVATE).show();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onLogin(AspNetUser user) {
		Toast.makeText(this, user.getUsername(), MODE_PRIVATE).show();
		moveTo(MainActivity.class);
	}
}
