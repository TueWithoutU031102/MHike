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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class view extends AppCompatActivity {

    EditText search;
    ListView lstTrip;
    Button createButton,searchButton, deleteButton;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        SQLiteDatabase db = openOrCreateDatabase("MHike", Context.MODE_PRIVATE, null);

        lstTrip = findViewById(R.id.lstTrip);
        final Cursor c = db.rawQuery("select * from triphike", null);
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
        lstTrip.setAdapter(arrayAdapter);

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
            lstTrip.invalidateViews();
        }

        lstTrip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        search = findViewById(R.id.search);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = search.getText().toString();
                performSearch(searchText);
            }
        });

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete();
                Intent i = new Intent(getApplicationContext(), view.class);
                startActivity(i);
            }
        });
    }
    public void Delete()
    {
        SQLiteDatabase db = openOrCreateDatabase("MHike", Context.MODE_PRIVATE, null);
        db.execSQL("DELETE FROM triphike");
    }
    private void performSearch(String searchText) {
        ArrayList<String> searchTitles = new ArrayList<>();

        SQLiteDatabase db = openOrCreateDatabase("MHike", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM triphike WHERE tripName LIKE '%" + searchText + "%'", null);

        int id = c.getColumnIndex("id");
        int tripName = c.getColumnIndex("tripName");
        int location = c.getColumnIndex("location");
        int date = c.getColumnIndex("date");

        searchTitles.clear();

        if (c.moveToFirst()) {
            do {
                trip Trip = new trip();
                Trip.id = c.getString(id);
                Trip.tripName = c.getString(tripName);
                Trip.location = c.getString(location);
                Trip.date = c.getString(date);
                searchTitles.add("ID: " + c.getString(id) + " | " + "Name: " + c.getString(tripName) + " | " + "Location: " + c.getString(location) + " | " + "Date: " + c.getString(date));
            } while (c.moveToNext());
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchTitles);
        lstTrip.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        lstTrip.invalidateViews();
    }
}

