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
import com.aspnetconnect.LoginFragment;
import com.aspnetconnect.LoginFragment.LoginFragmentListener;
import com.aspnetconnect.RegisterFragment;
import com.aspnetconnect.RegisterFragment.RegisterFragmentListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class LoginActivity extends Activity implements AspNetManager.NetworkListener
													, AspNetManager.LoginListener
													, AspNetManager.ErrorListener
													, AspNetManager.ExternalLoginListener
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
					, getString(R.string.callback)
					, new AspNetAuthStore(this, getString(R.string.app_unique))
					, this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(manager.hasAuthorization()){
			moveTo(MainActivity.class);
		}
		else {
			if (savedInstanceState == null) {
				fragmentManager = this.getFragmentManager();
				fragmentManager.beginTransaction()
				 		.add(R.id.container, RegisterFragment.newInstance(R.layout.fragment_register)).commit();
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
		manager.loginWith(provider, this, this, this);
	}

	@Override
	public void swapToRegister() {
		fragmentManager.beginTransaction()
			.replace(R.id.container, RegisterFragment.newInstance(R.layout.fragment_register)).commit();
	}

	@Override
	public void onRegister(AspNetRegisterRequest model) {
		manager.register(model, this, this);
	}

	@Override
	public void swapToLogin() {
		fragmentManager.beginTransaction()
		.replace(R.id.container, LoginFragment.newInstance(R.layout.fragment_login)).commit();
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

	@Override
	public void onLoadRequest(String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
}
