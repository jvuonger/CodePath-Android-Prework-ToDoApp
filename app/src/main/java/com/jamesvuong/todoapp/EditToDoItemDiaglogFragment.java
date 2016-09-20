package com.jamesvuong.todoapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

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

    // Defines the listener interface with a method passing back data result
    public interface EditToDoItemDiaglogListener {
        void onFinishEditDialog(int itemPosition, ToDoItem item);
    }

    // Create a new instance of the DialogFragment
    public static EditToDoItemDiaglogFragment newInstance(int position, ToDoItem item) {
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
        // Initialize variables
        listener = (EditToDoItemDiaglogListener) getActivity();
        itemId = getArguments().getInt("id");
        itemPosition = getArguments().getInt("position");

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



        // Attach Events to buttons
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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
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
