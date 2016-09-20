package com.jamesvuong.todoapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.jamesvuong.todoapp.R.id.dismiss;
import static com.jamesvuong.todoapp.R.id.dpDueDate;
import static com.jamesvuong.todoapp.R.id.etEditItemText;
import static com.jamesvuong.todoapp.R.string.save;

/**
 * Created by jvuonger on 9/19/16.
 */

public class EditToDoItemDiaglogFragment extends DialogFragment {

    ToDoItemDatabase db;
    ToDoItem itemToEdit;
    EditText etEditItemText;
    DatePicker dpDueDate;
    int itemId;
    int itemPosition;
    EditToDoItemDiaglogListener listener;

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditToDoItemDiaglogListener {
        void onFinishEditDialog(int itemPosition, ToDoItem item);
    }

    static EditToDoItemDiaglogFragment newInstance(int position, ToDoItem item) {
        EditToDoItemDiaglogFragment f = new EditToDoItemDiaglogFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("id", item.getToDoId());
        args.putInt("dateTime", (int)item.getDueDateTime());
        args.putString("name", item.getToDoItem());
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listener = (EditToDoItemDiaglogListener) getActivity();
        itemId = getArguments().getInt("id");
        itemPosition = getArguments().getInt("position");

        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);

        etEditItemText = (EditText) rootView.findViewById(R.id.etEditItemText);

        // Get Item from SQL Lite database
        db = ToDoItemDatabase.getInstance(rootView.getContext());
        itemToEdit = db.getToDoItemById(itemId);

        // Edit To Do Item Name
        etEditItemText = (EditText) rootView.findViewById(R.id.etEditItemText);
        etEditItemText.setText(itemToEdit.getToDoItem());
        etEditItemText.setSelection(etEditItemText.getText().length());

        // Edit Due Date
        dpDueDate = (DatePicker) rootView.findViewById(R.id.dpDueDate);
        final Calendar c = itemToEdit.getDueDateForDatePicker();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dpDueDate.updateDate(year, month, day);


        getDialog().setTitle("Edit To Do Item");

        Button btnSaveButton = (Button) rootView.findViewById(R.id.btnSaveEditItem);
        btnSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDoItem();
                listener.onFinishEditDialog(itemPosition, itemToEdit);
                dismiss();
            }
        });

        Button dismiss = (Button) rootView.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    private void saveToDoItem() {
        itemToEdit.setToDoItem(etEditItemText.getText().toString());

        // Get date object from date picker
        int day = dpDueDate.getDayOfMonth();
        int month = dpDueDate.getMonth();
        int year =  dpDueDate.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        itemToEdit.setDueDate(calendar.getTime());

        db.updateToDoItem(itemToEdit);

    }
}
