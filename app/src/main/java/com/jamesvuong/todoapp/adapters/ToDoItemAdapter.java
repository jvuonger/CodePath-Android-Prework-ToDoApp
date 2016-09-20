package com.jamesvuong.todoapp.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jamesvuong.todoapp.R;
import com.jamesvuong.todoapp.models.ToDoItem;

import java.text.SimpleDateFormat;
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
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        // Populate the data into the template view using the data object
        tvToDoItem.setText(toDoItem.getToDoItem());
        tvDueDate.setText(getDateForView(toDoItem.getDueDateTime()));
        tvPriority.setText(toDoItem.getPriority());

        // Style Priority Text
        stylePriorityText(convertView.getContext(), tvPriority);

        // Return the completed view to render on screen
        return convertView;
    }

    private String getDateForView(long time) {
        if (time == -1 ) return "";

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);

        SimpleDateFormat fmtOut = new SimpleDateFormat("MMM dd, yyyy");
        String date = "Due: " + fmtOut.format(cal.getTime());

        return date;
    }

    private void stylePriorityText(Context context, TextView tvPriority) {
        int textColor = R.color.lowPriority;
        switch(tvPriority.getText().toString().toLowerCase()) {
            case "medium":
                textColor = R.color.mediumPriority;
                break;
            case "high":
                textColor = R.color.highPriority;
                break;
            default:
                break;
        }

        tvPriority.setTextColor(ContextCompat.getColor(context, textColor));
    }

}
