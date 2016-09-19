package com.jamesvuong.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    ArrayList<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
    ToDoItemAdapter aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    ToDoItemDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = ToDoItemDatabase.getInstance(this);

        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);

        // Edit item on click
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchEditItemView(position);
            }
        });

        // Delete item on Long Click
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.deleteToDoItem((ToDoItem) view.getTag());
                toDoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            int itemId = data.getExtras().getInt("updatedItemId");
            int itemPosition = data.getExtras().getInt("updatedItemPosition");

            //update item name and save
            toDoItems.set(itemPosition, db.getToDoItemById(itemId));
            aToDoAdapter.notifyDataSetChanged();
        }
    }

    public void populateArrayItems() {
        toDoItems = db.getAllToDoItems();
        aToDoAdapter = new ToDoItemAdapter(this,toDoItems);
    }

    public void onAddItem(View view) {
        etEditText = (EditText) findViewById(R.id.etEditText);

        ToDoItem newToDoItem = new ToDoItem();
        newToDoItem.setToDoItem(etEditText.getText().toString());
        newToDoItem.setToDoId((int) db.addToDoItem(newToDoItem));
        aToDoAdapter.add(newToDoItem);
        aToDoAdapter.notifyDataSetChanged();

        etEditText.setText("");
    }

    public void launchEditItemView(int itemPosition) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);

        ToDoItem item = (ToDoItem) lvItems.getItemAtPosition(itemPosition);

        i.putExtra("item", item.getToDoItem());
        i.putExtra("itemId", item.getToDoId());
        i.putExtra("itemPosition", itemPosition);

        startActivityForResult(i, REQUEST_CODE);
    }
}
