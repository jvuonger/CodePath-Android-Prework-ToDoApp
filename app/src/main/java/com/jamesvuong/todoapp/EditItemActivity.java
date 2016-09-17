package com.jamesvuong.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    EditText etEditItemText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String item = getIntent().getStringExtra("item");
        etEditItemText = (EditText) findViewById(R.id.etEditItemText);
        etEditItemText.setText(item);
        etEditItemText.setSelection(etEditItemText.getText().length());
    }

    public void onSubmit(View v) {
        etEditItemText = (EditText) findViewById(R.id.etEditItemText);

        Intent data = new Intent();
        data.putExtra("updatedItem", etEditItemText.getText().toString());
        data.putExtra("updatedItemPosition", getIntent().getIntExtra("itemPosition",-1));
        setResult(RESULT_OK, data); // set result code and bundle data for response
        this.finish();
    }
}
