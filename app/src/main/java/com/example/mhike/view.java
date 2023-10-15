package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class view extends AppCompatActivity {

    ListView lst1;
    Button createButton;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        SQLiteDatabase db = openOrCreateDatabase("Trip", Context.MODE_PRIVATE, null);

        lst1 = findViewById(R.id.lst1);
        final Cursor c = db.rawQuery("select * from records", null);
        int id = c.getColumnIndex("id");
        int tripName = c.getColumnIndex("tripName");
        int location = c.getColumnIndex("location");
        int date = c.getColumnIndex("date");
        int parkingStatus = c.getColumnIndex("parkingStatus");
        int length = c.getColumnIndex("length");
        int level = c.getColumnIndex("level");
        int description = c.getColumnIndex("description");
        int time = c.getColumnIndex("time");
        titles.clear();

        arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titles);
        lst1.setAdapter(arrayAdapter);

        final ArrayList<trip> trips = new ArrayList<trip>();
        if (c.moveToFirst()) {
            do {
                trip Trip = new trip();
                Trip.id = c.getString(id);
                Trip.tripName = c.getString(tripName);
                Trip.location = c.getString(location);
                Trip.date = c.getString(date);
                Trip.parkingStatus = c.getString(parkingStatus);
                Trip.length = c.getString(length);
                Trip.level = c.getString(level);
                Trip.description = c.getString(description);
                Trip.time = c.getString(time);
                trips.add(Trip);

                titles.add("ID: " + c.getString(id) + " | " + "Name: " + c.getString(tripName) + " | " + "Location: " + c.getString(location) + " | " + "Date: " + c.getString(date));
            } while (c.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            lst1.invalidateViews();
        }

        lst1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aa = titles.get(position).toString();
                trip Trip = trips.get(position);
                Intent i = new Intent(getApplicationContext(), edit.class);
                i.putExtra("id", Trip.id);
                i.putExtra("tripName", Trip.tripName);
                i.putExtra("location", Trip.location);
                i.putExtra("date", Trip.date);
                i.putExtra("parkingStatus", Trip.parkingStatus);
                i.putExtra("length", Trip.length);
                i.putExtra("level", Trip.level);
                i.putExtra("description", Trip.description);
                i.putExtra("time", Trip.time);
                startActivity(i);
            }
        });
        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

