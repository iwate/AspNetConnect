package com.aspnetconnect.sample;

import com.aspnetconnect.AspNetExternalLoginProvider;
import com.aspnetconnect.AspNetLoginRequest;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;

public class LoginFragment extends Fragment implements OnClickListener {
	public interface LoginFragmentListener{
		public void onLogin(AspNetLoginRequest model);
		public void onLoginWith(AspNetExternalLoginProvider provider);
		public void swapToRegister();
	}
	private LoginFragmentListener listener;
	private EditText username;
	private EditText password;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		username = (EditText) view.findViewById(R.id.username);
		password = (EditText) view.findViewById(R.id.password);
		((Button)view.findViewById(R.id.login)).setOnClickListener(this);
		((Button)view.findViewById(R.id.register)).setOnClickListener(this);
		((Button)view.findViewById(R.id.facebook)).setOnClickListener(this);
		((Button)view.findViewById(R.id.twitter)).setOnClickListener(this);
		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof LoginFragmentListener == false){
			throw new ClassCastException();
		}
		listener = (LoginFragmentListener) activity;
	}
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.login:
			listener.onLogin(new AspNetLoginRequest(
					username.getText().toString()
					, password.getText().toString()));
			break;
		case R.id.register:
			listener.swapToRegister();
			break;
		case R.id.facebook:
			listener.onLoginWith(AspNetExternalLoginProvider.Facebook);
			break;
		case R.id.twitter:
			listener.onLoginWith(AspNetExternalLoginProvider.Twitter);
			break;
		}
		
	}
}
