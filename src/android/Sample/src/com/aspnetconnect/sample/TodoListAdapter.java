package com.aspnetconnect.sample;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoListAdapter extends ArrayAdapter<Todo> {
	
	private Context context;
	public TodoListAdapter(Context context, int resource, List<Todo> objects) {
		super(context, resource, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Todo todo = getItem(position);
		if(view == null){
			view = new TextView(context);
		}
		((TextView)view).setTextColor(todo.getIsChecked()? Color.GRAY : Color.BLACK);
		((TextView)view).setText(todo.getTitle());
		return view;
	}

}
