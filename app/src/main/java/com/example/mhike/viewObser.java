package com.example.mhike;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class viewObser extends AppCompatActivity {
    ListView lstObser;
    Button btncreateObser;

    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obser_view);

        SQLiteDatabase db = openOrCreateDatabase("MHike", Context.MODE_PRIVATE, null);

        lstObser = findViewById(R.id.lstObser);

        final Cursor o = db.rawQuery("select * from observation", null);
        int id = o.getColumnIndex("id");
        int ObName = o.getColumnIndex("ObName");
        int ObTime = o.getColumnIndex("ObTime");
        int ObComment = o.getColumnIndex("ObComment");
        int hike_id = o.getColumnIndex("hike_id");
        titles.clear();

        arrayAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, titles);
        lstObser.setAdapter(arrayAdapter);
        final ArrayList<observation> obs = new ArrayList<observation>();
        if (o.moveToFirst()) {
            do {
                observation Obs = new observation();
                Obs.id = o.getString(id);
                Obs.ObName = o.getString(ObName);
                Obs.ObTime = o.getString(ObTime);
                Obs.ObComment = o.getString(ObComment);
                Obs.hike_id = o.getString(hike_id);
                obs.add(Obs);

                titles.add("ID: " + o.getString(id) + " | " + "Name: " + o.getString(ObName) + " | " + "Time: " + o.getString(ObTime) + " | " + "Comment: " + o.getString(ObComment) + "|" + "HikeID:" + o.getString(hike_id));

            } while (o.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            lstObser.invalidateViews();
        }
        lstObser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ob = titles.get(position).toString();
                observation Obs = obs.get(position);
                Intent i = new Intent(getApplicationContext(),editObser.class);
                i.putExtra("id",Obs.id);
                i.putExtra("ObName",Obs.ObName);
                i.putExtra("ObTime",Obs.ObTime);
                i.putExtra("ObComment",Obs.ObComment);
                i.putExtra("hike_id",Obs.hike_id);
                startActivity(i);
            }
        });
        btncreateObser = findViewById(R.id.btncreateObser);
        btncreateObser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), view.class);
                startActivity(i);
            }
        });
    }
}
