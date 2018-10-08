package com.example.sarahtang.midtermelections;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.TextView;

import java.util.ArrayList;

public class CongressionalActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Representative> listRepresentatives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Initialize dataset
        //Get Array list of representatives from intent
        listRepresentatives = (ArrayList<Representative>) getIntent().getSerializableExtra("representativeList");

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CongressAdapter(getApplicationContext(), listRepresentatives);
        mRecyclerView.setAdapter(mAdapter);

        TextView locationView = (TextView) findViewById(R.id.locationView);
        String full_location = (String) getIntent().getSerializableExtra("full_location");
        locationView.setText(full_location);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
}
