package com.aspnetconnect;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class RegisterFragment extends Fragment {
	private static String KEY_INT_LAYOUT_ID = "key_layout_id";

	public static RegisterFragment newInstance(){
		return newInstance(R.layout.fragment_register);
	}
	public static RegisterFragment newInstance(int layout){
		RegisterFragment fragment = new RegisterFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_INT_LAYOUT_ID,layout);
		fragment.setArguments(args);
		return fragment;
	}
	public interface RegisterFragmentListener{
		public void onRegister(AspNetRegisterRequest model);
		public void swapToLogin();
	}
	private RegisterFragmentListener listener;
	private EditText username;
	private EditText email;
	private EditText password;
	private EditText confirmPassword;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_register, container, false);
		username = (EditText) view.findViewById(R.id.username);
		email = (EditText) view.findViewById(R.id.email);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirm_password);
		((Button)view.findViewById(R.id.login)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				listener.swapToLogin();
			}});
		((Button)view.findViewById(R.id.register)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				listener.onRegister(new AspNetRegisterRequest(
						email.getText().toString()
						, username.getText().toString()
						, password.getText().toString()
						, confirmPassword.getText().toString()));
			}});
		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof RegisterFragmentListener == false){
			throw new ClassCastException();
		}
		listener = (RegisterFragmentListener) activity;
	}
}
