package com.jamesvuong.todoapp;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity implements EditToDoItemDiaglogFragment.EditToDoItemDiaglogListener{
    final String TAG = "MainActivity";
    ArrayList<ToDoItem> toDoItems = new ArrayList<ToDoItem>();
    ToDoItemAdapter aToDoAdapter;
    ListView lvItems;
    EditText etEditText;
    ToDoItemDatabase db;

    @Override
    public void onFinishEditDialog(int itemPosition, ToDoItem item) {
        //update item name and save
        toDoItems.set(itemPosition, db.getToDoItemById(item.getToDoId()));
        aToDoAdapter.notifyDataSetChanged();
    }

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
                launchEditItemView(position, (ToDoItem) view.getTag());
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

    public void launchEditItemView(int itemPosition, ToDoItem item) {
        FragmentManager fm = getFragmentManager();
        EditToDoItemDiaglogFragment dialogFragment = EditToDoItemDiaglogFragment.newInstance(itemPosition, item);
        dialogFragment.show(fm, "Edit To Do Item Dialog Fragment");
    }
}
