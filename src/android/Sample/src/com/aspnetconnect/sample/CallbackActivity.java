package com.aspnetconnect.sample;

import com.aspnetconnect.AspNetAuthStore;
import com.aspnetconnect.AspNetManager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class CallbackActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = this.getIntent().getData();
        AspNetManager.setAuthorizationFrom(
        		uri
        		, getString(R.string.callback)
        		, new AspNetAuthStore(this, getString(R.string.app_unique)));
		moveTo(MainActivity.class);
	}
	private <T> void moveTo(Class<T> clazz){
		Intent intent = new Intent(getApplicationContext(),clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		finish();
	}
}
