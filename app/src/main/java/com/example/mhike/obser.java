package com.example.mhike;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class obser extends AppCompatActivity {
    EditText inputObName, inputObTime, inputObComment;
    Button btnAddOb, btnViewOb, btnBackHike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obser_create);

        inputObName = findViewById(R.id.inputObName);
        inputObTime = findViewById(R.id.inputObTime);
        inputObComment = findViewById(R.id.inputObComment);

        btnBackHike = findViewById(R.id.btnBackHike);
        btnAddOb = findViewById(R.id.btnAddOb);
        btnViewOb = findViewById(R.id.btnViewOb);

        btnViewOb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), viewObser.class);
                startActivity(i);
            }
        });
        btnBackHike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), view.class);
                startActivity(i);
            }
        });
        btnAddOb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
    }

    public void insert() {
        try {
            String ObName = inputObName.getText().toString();
            String ObTime = inputObTime.getText().toString();
            String ObComment = inputObComment.getText().toString();
            Intent intent=getIntent();
            String hike_id = intent.getStringExtra("hike_id") ;

            if (ObName.isEmpty() || ObTime.isEmpty() || ObComment.isEmpty()) {
                Toast.makeText(this, "Please enter all required information", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidTime(ObTime)) {
                Toast.makeText(this, "Please enter a valid time (HH:mm)", Toast.LENGTH_LONG).show();
                return;
            }

            SQLiteDatabase db = openOrCreateDatabase("MHike", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS observation (id INTEGER PRIMARY KEY AUTOINCREMENT, ObName TEXT, ObTime TEXT, ObComment TEXT, hike_id INTEGER, FOREIGN KEY(hike_id) REFERENCES triphike(id))");
            String sql = "insert into observation(ObName,ObTime,ObComment,hike_id)values(?,?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, ObName);
            statement.bindString(2, ObTime);
            statement.bindString(3, ObComment);
            statement.bindString(4, hike_id);
            statement.execute();
            Toast.makeText(this, "Record added", Toast.LENGTH_SHORT).show();

            inputObName.setText("");
            inputObTime.setText("");
            inputObComment.setText("");

        } catch (Exception ex) {
            Log.e("InsertError", ex.toString());
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
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
