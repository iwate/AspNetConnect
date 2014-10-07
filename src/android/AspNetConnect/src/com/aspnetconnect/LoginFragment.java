package com.aspnetconnect;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {
	private static String KEY_INT_LAYOUT_ID = "key_layout_id";

	public static LoginFragment newInstance(){
		return newInstance(R.layout.fragment_login);
	}

	public static LoginFragment newInstance(int layout){
		LoginFragment fragment = new LoginFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_INT_LAYOUT_ID,layout);
		fragment.setArguments(args);
		return fragment;
	}

	public interface LoginFragmentListener{
		public void onLogin(AspNetLoginRequest model);
		public void onLoginWith(AspNetExternalLoginProvider provider);
		public void swapToRegister();
	}
	
	private LoginFragmentListener listener;
	private EditText username;
	private EditText password;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Bundle args = getArguments();
		try{
			View view = inflater.inflate(args.getInt(KEY_INT_LAYOUT_ID), container, false);
			username = (EditText) view.findViewById(R.id.username);
			password = (EditText) view.findViewById(R.id.password);
			((Button)view.findViewById(R.id.login)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					listener.onLogin(new AspNetLoginRequest(
							username.getText().toString()
							, password.getText().toString()));
				}});
			((Button)view.findViewById(R.id.register)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					listener.swapToRegister();
				}});
			((Button)view.findViewById(R.id.facebook)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					listener.onLoginWith(AspNetExternalLoginProvider.Facebook);
				}});
			((Button)view.findViewById(R.id.twitter)).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					listener.onLoginWith(AspNetExternalLoginProvider.Twitter);
				}});
			return view;
		}catch(InflateException e){
			throw new InflateException();
		}
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof LoginFragmentListener == false){
			throw new ClassCastException();
		}
		listener = (LoginFragmentListener) activity;
	}
}
