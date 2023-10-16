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

public class editObser extends AppCompatActivity {
    EditText inputObName, inputObTime, inputObComment, inputObId, inputHikeId;
    Button btnEditObs, btnDeleteObs, btnBackObs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obser_edit);

        inputObId = findViewById(R.id.inputObId);
        inputObId.setVisibility(View.GONE);
        inputObName = findViewById(R.id.inputObName);
        inputObTime = findViewById(R.id.inputObTime);
        inputObComment = findViewById(R.id.inputObComment);
        inputHikeId = findViewById(R.id.inputHikeId);
        inputHikeId.setVisibility(View.GONE);

        btnEditObs = findViewById(R.id.btnEditObs);
        btnDeleteObs = findViewById(R.id.btnDeleteObs);
        btnBackObs = findViewById(R.id.btnBackObs);

        Intent i = getIntent();
        String o1 = i.getStringExtra("id").toString();
        String o2 = i.getStringExtra("ObName").toString();
        String o3 = i.getStringExtra("ObTime").toString();
        String o4 = i.getStringExtra("ObComment").toString();
        String o5 = i.getStringExtra("hike_id").toString();

        inputObId.setText(o1);
        inputObName.setText(o2);
        inputObTime.setText(o3);
        inputObComment.setText(o4);
        inputHikeId.setText(o5);

        btnEditObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit();
            }
        });
        btnDeleteObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
                Intent i = new Intent(getApplicationContext(), viewObser.class);
                startActivity(i);
            }
        });

        btnBackObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), viewObser.class);
                startActivity(i);
            }
        });
    }

    public void Delete() {
        try {
            String id = inputObId.getText().toString().trim();

            SQLiteDatabase db = openOrCreateDatabase("MHike", Context.MODE_PRIVATE, null);

            String sql = "delete from observation where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, id);
            statement.execute();
            Toast.makeText(this, "Record Deleted", Toast.LENGTH_LONG).show();

            inputObName.setText("");
            inputObTime.setText("");
            inputObComment.setText("");
            inputHikeId.setText("");
            inputObName.requestFocus();
        } catch (Exception ex) {
            Toast.makeText(this, "Record Fail", Toast.LENGTH_LONG).show();
        }
    }

    public void Edit() {
        try {
            String ObName = inputObName.getText().toString();
            String ObTime = inputObTime.getText().toString();
            String ObComment = inputObComment.getText().toString();

            if (ObName.isEmpty() || ObTime.isEmpty() || ObComment.isEmpty()) {
                Toast.makeText(this, "Please enter all required information", Toast.LENGTH_LONG).show();
                return;
            }

            if (!isValidTime(ObTime)) {
                Toast.makeText(this, "Please enter a valid time (HH:mm)", Toast.LENGTH_LONG).show();
                return;
            }

            SQLiteDatabase db = openOrCreateDatabase("MHike", Context.MODE_PRIVATE, null);
            String sql = "update observation set ObName =?,ObTime=?,ObComment=? where id=?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1, ObName);
            statement.bindString(2, ObTime);
            statement.bindString(3, ObComment);
            statement.bindString(4, inputObId.getText().toString());
            statement.execute();
            Toast.makeText(this, "Record edited", Toast.LENGTH_SHORT).show();

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
