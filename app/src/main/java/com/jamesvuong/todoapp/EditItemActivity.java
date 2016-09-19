package com.jamesvuong.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
    ToDoItemDatabase db;
    ToDoItem itemToEdit;
    EditText etEditItemText;
    DatePicker dpDueDate;
    int itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        itemId = getIntent().getIntExtra("itemId",-1);

        // Get Item from SQL Lite database
        db = ToDoItemDatabase.getInstance(this);
        itemToEdit = db.getToDoItemById(itemId);

        // Edit To Do Item Name
        etEditItemText = (EditText) findViewById(R.id.etEditItemText);
        etEditItemText.setText(itemToEdit.getToDoItem());
        etEditItemText.setSelection(etEditItemText.getText().length());

        // Edit Due Date
        dpDueDate = (DatePicker) findViewById(R.id.dpDueDate);
        final Calendar c = itemToEdit.getDueDateForDatePicker();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dpDueDate.updateDate(year, month, day);

    }

    public void onSubmit(View v) {
        etEditItemText = (EditText) findViewById(R.id.etEditItemText);

        itemToEdit.setToDoItem(etEditItemText.getText().toString());

        // Get date object from date picker
        int day = dpDueDate.getDayOfMonth();
        int month = dpDueDate.getMonth();
        int year =  dpDueDate.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        itemToEdit.setDueDate(calendar.getTime());

        db.updateToDoItem(itemToEdit);

        Intent data = new Intent();
        data.putExtra("updatedItemId", itemId);
        data.putExtra("updatedItemPosition", getIntent().getIntExtra("itemPosition",-1));
        setResult(RESULT_OK, data); // set result code and bundle data for response
        this.finish();
    }
}
