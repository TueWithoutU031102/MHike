package com.example.mhike;

import static android.content.Context.*;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class edit extends AppCompatActivity {
    EditText inputId,inputTName, inputLocation, inputLength, inputDescription, inputTime, inputDate;
    CheckBox cbParking;
    RadioGroup radioGroup;
    RadioButton btnEasy, btnMedium, btnHard;
    Button editButton, deleteButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        inputId = findViewById(R.id.inputId);
        inputTName = findViewById(R.id.inputTName);
        inputLocation = findViewById(R.id.inputLocation);
        inputDate = findViewById(R.id.inputDate);
        cbParking = findViewById(R.id.cbParking);
        inputLength = findViewById(R.id.inputLength);
        radioGroup = findViewById(R.id.radioGroup);

        btnEasy = findViewById(R.id.radioButtonEasy);
        btnMedium = findViewById(R.id.radioButtonMedium);
        btnHard = findViewById(R.id.radioButtonHard);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                String radioStatus;
                if (isChecked) {
                    if (checkedId == R.id.radioButtonEasy) {
                        radioStatus = "Easy";
                    } else if (checkedId == R.id.radioButtonMedium) {
                        radioStatus = "Medium";
                    } else if (checkedId == R.id.radioButtonHard) {
                        radioStatus = "Hard";
                    }
                }
            }
        });
        inputDescription = findViewById(R.id.inputDescription);
        inputTime = findViewById(R.id.inputTime);

        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        Intent i = getIntent();
        String t1 = i.getStringExtra("id").toString();
        String t2 = i.getStringExtra("tripName").toString();
        String t3 = i.getStringExtra("location").toString();
        String t4 = i.getStringExtra("date").toString();
        String t5 = i.getStringExtra("parkingStatus").toString();
        String t6 = i.getStringExtra("length").toString();
        String t7 = i.getStringExtra("level").toString();
        String t8 = i.getStringExtra("description").toString();
        String t9 = i.getStringExtra("time").toString();

        inputId.setText(t1);
        inputTName.setText(t2);
        inputLocation.setText(t3);
        inputDate.setText(t4);
        if (t5.equals("Yes")) {
            cbParking.setChecked(true);
        } else {
            cbParking.setChecked(false);
        }
        inputLength.setText(t6);
        if (t7.equals("Easy")) {
            btnEasy.setChecked(true);
        } else if (t7.equals("Medium")) {
            btnMedium.setChecked(true);
        } else if (t7.equals("Hard")) {
            btnHard.setChecked(true);
        }
        inputDescription.setText(t8);
        inputTime.setText(t9);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), view.class);
                startActivity(i);
            }
        });
    }

    public void Delete() {
        try {
            String id = inputId.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("Trip",Context.MODE_PRIVATE,null);

            String sql = "delete from records where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1,id);
            statement.execute();
            Toast.makeText(this,"Record Deleted",Toast.LENGTH_LONG).show();

            inputTName.setText("");
            inputLocation.setText("");
            inputDate.setText("");
            inputLength.setText("");
            inputDescription.setText("");
            inputTime.setText("");
            inputTName.requestFocus();

        } catch (Exception ex) {
            Toast.makeText(this,"Record Fail",Toast.LENGTH_LONG).show();
        }
    }

    public void Edit() {
        try {
            String tripName = inputTName.getText().toString();
            String location = inputLocation.getText().toString();
            String date = inputDate.getText().toString();
            String parkingStatus;
            if (cbParking.isChecked()) parkingStatus = "Yes";
            else parkingStatus = "No";
            String length = inputLength.getText().toString();
            String level = "";
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton radioButton = findViewById(selectedId);
                level = radioButton.getText().toString();
            }
            String description = inputDescription.getText().toString();
            String time = inputTime.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("Trip", Context.MODE_PRIVATE, null);
            String sql = "update records set tripName = ?,location = ?,date = ?,parkingStatus = ?,length = ?,level = ?,description = ?,time = ? where id= ?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, tripName);
            statement.bindString(2, location);
            statement.bindString(3, date);
            statement.bindString(4, parkingStatus);
            statement.bindString(5, length);
            statement.bindString(6, level);
            statement.bindString(7, description);
            statement.bindString(8, time);
            statement.execute();
            Toast.makeText(this, "Record edited", Toast.LENGTH_SHORT).show();

            inputTName.setText("");
            inputLocation.setText("");
            inputDate.setText("");
            inputLength.setText("");
            inputDescription.setText("");
            inputTime.setText("");
            inputTName.requestFocus();
        } catch (Exception ex) {
            Toast.makeText(this,"Record Fail",Toast.LENGTH_LONG).show();
        }
    }
}

