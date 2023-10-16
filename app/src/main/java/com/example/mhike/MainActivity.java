package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText inputTName, inputLocation, inputLength, inputDescription, inputTime, inputDate;
    CheckBox cbParking;
    RadioGroup radioGroup;
    RadioButton btnEasy, btnMedium, btnHard;

    Button confirmButton, viewButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        confirmButton = findViewById(R.id.confirmButton);
        viewButton = findViewById(R.id.viewButton);


        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), view.class);
                startActivity(i);
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
    }

    public void insert() {
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

            if(tripName.isEmpty()||location.isEmpty()||date.isEmpty()||length.isEmpty()||level.isEmpty()||time.isEmpty())
            {
                Toast.makeText(this, "Please enter all required information", Toast.LENGTH_LONG).show();
                return;
            }
            if (!isValidDate(date)) {
                Toast.makeText(this, "Please enter a valid date (dd/MM/yyyy)", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidTime(time)) {
                Toast.makeText(this, "Please enter a valid time (HH:mm)", Toast.LENGTH_LONG).show();
                return;
            }

            SQLiteDatabase db = openOrCreateDatabase("MHike", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS triphike (id INTEGER PRIMARY KEY AUTOINCREMENT, tripName VARCHAR,location VARCHAR, length VARCHAR, level VARCHAR,description VARCHAR,date VARCHAR, parkingStatus VARCHAR, time VARCHAR)");
            String sql = "insert into triphike(tripName,location,date,parkingStatus,length,level,description,time)values(?,?,?,?,?,?,?,?)";

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
            Toast.makeText(this, "Record added", Toast.LENGTH_SHORT).show();

            inputTName.setText("");
            inputLocation.setText("");
            inputDate.setText("");
            inputLength.setText("");
            inputDescription.setText("");
            inputTime.setText("");
            inputTName.requestFocus();

        } catch (Exception ex) {
            Log.e("InsertError", ex.toString());
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }
    private boolean isValidDate(String inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inputDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    private boolean isValidTime(String inputTime) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeFormat.setLenient(false);
        try {
            timeFormat.parse(inputTime.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}