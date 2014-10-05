package com.aspnetconnect.sample;

import com.aspnetconnect.AspNetRegisterRequest;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterFragment extends Fragment implements OnClickListener{
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
		((Button)view.findViewById(R.id.login)).setOnClickListener(this);
		((Button)view.findViewById(R.id.register)).setOnClickListener(this);
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
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.register:
			listener.onRegister(new AspNetRegisterRequest(
					email.getText().toString()
					, username.getText().toString()
					, password.getText().toString()
					, confirmPassword.getText().toString()));
			break;
		case R.id.login:
			listener.swapToLogin();
			break;
		}
	}
}
