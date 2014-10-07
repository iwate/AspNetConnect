package com.aspnetconnect.sample;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.aspnetconnect.AspNetAuthStore;
import com.aspnetconnect.AspNetManager;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements AspNetManager.NetworkListener, OnClickListener, OnItemClickListener, ErrorListener {
	
	AspNetManager manager;
	EditText todoContent;
	Button todoAddition;
	ListView todoList;
	TodoListAdapter adapter;
	RequestQueue queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			manager = new AspNetManager(
					getString(R.string.api_url)
					, new AspNetAuthStore(this, getString(R.string.app_unique))
					, this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		queue = Volley.newRequestQueue(this);
		todoContent = (EditText)findViewById(R.id.todo_content);
		
		todoAddition = (Button)findViewById(R.id.todo_addition);
		todoAddition.setOnClickListener(this);
		
		todoList = (ListView)findViewById(R.id.todo_list);
		adapter = new TodoListAdapter(this, R.id.todo_list, new ArrayList<Todo>());
		todoList.setAdapter(adapter);
		todoList.setOnItemClickListener(this);
		
		reload();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_logout) {
			manager.logout();
			moveTo(LoginActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	private void reload(){
		manager.invoke(
				Request.Method.GET
				, "/api/todos"
				, new TypeToken<Collection<Todo>>(){}.getType()
				, null, null
				, new Listener<Collection<Todo>>(){
					@Override
					public void onResponse(Collection<Todo> response) {
						adapter.addAll(response);
					}}
				, this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Todo todo = adapter.getItem(position);
		todo.setIsChecked(!todo.getIsChecked());
		manager.invoke(
				Request.Method.PUT
				, "/api/todos"
				, Todo.class
				, null, todo
				, new Listener<Todo>(){
					@Override
					public void onResponse(Todo response) {
						adapter.notifyDataSetChanged();
					}}
				, this);
	}
	
	@Override
	public void onClick(View view) {
		Todo newTodo = new Todo();
		newTodo.setTitle(todoContent.getText().toString());
		if(view.getId() == R.id.todo_addition){
			manager.invoke(
					Request.Method.POST
					, "/api/todos"
					, Todo.class
					, null, newTodo
					, new Listener<Todo>(){
						@Override
						public void onResponse(Todo response) {
							adapter.addAll(response);
						}}
					, this);
		}
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		try {
			String message;
			if(error.networkResponse != null){
				message = new String(error.networkResponse.data, "UTF-8");
			} else if(error.getCause() != null){
				message = error.getCause().toString();
			} else {
				message = error.toString();
			}
			Toast.makeText(this, message, MODE_PRIVATE).show();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
