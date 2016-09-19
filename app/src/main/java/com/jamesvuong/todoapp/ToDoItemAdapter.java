package com.jamesvuong.todoapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by jvuonger on 9/17/16.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {

    public ToDoItemAdapter(Context context, ArrayList<ToDoItem> toDoItems) {
        super(context, 0, toDoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ToDoItem toDoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.to_do_item, parent, false);
        }

        convertView.setTag(toDoItem);

        // Lookup view for data population
        TextView tvToDoItem = (TextView) convertView.findViewById(R.id.tvToDoItemText);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        // Populate the data into the template view using the data object
        tvToDoItem.setText(toDoItem.getToDoItem());
        tvDueDate.setText(getDateForView(toDoItem.getDueDateTime()));
        // Return the completed view to render on screen
        return convertView;
    }

    public String getDateForView(long time) {
        if (time == -1 ) return "";

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("MM-dd-yyyy", cal).toString();

        return date;
    }

}
