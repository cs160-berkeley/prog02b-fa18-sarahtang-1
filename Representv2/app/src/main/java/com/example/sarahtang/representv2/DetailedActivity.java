package com.example.sarahtang.representv2;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class DetailedActivity extends AppCompatActivity {
    private Representative representative;
    private TextView name, rep_type, party, contact, website, bioguide;
    private Button contactButton, websiteButton;
    private ImageView photoView;
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        name = (TextView) findViewById(R.id.name);
        rep_type = (TextView) findViewById(R.id.repView);
        party = (TextView) findViewById(R.id.party);
        contact = (TextView) findViewById(R.id.contact);
        website = (TextView) findViewById(R.id.website);
//        contactButton = (Button) findViewById(R.id.contactButton);
//        websiteButton = (Button) findViewById(R.id.websiteButton);
        photoView = (ImageView) findViewById(R.id.photoView);

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        representative = (Representative) bundle.getSerializable("rep");

        String n = representative.getFirst_name() + " " + representative.getLast_name();
        String type = representative.getRepresentative_type();
        type = type.substring(0,1).toUpperCase() + type.substring(1);

        name.setText(n);
        String upperType = type.substring(0,1).toUpperCase() + type.substring(1);
        rep_type.setText(upperType);
        party.setText(representative.getParty());

        if (representative.getContact_page() == null) {
            contact.setText("Didn't find any contact information :(");
        } else {
            contact.setText(representative.getContact_page());
            Linkify.addLinks(contact, Linkify.WEB_URLS);
        }

        website.setText(representative.getWebsite());
        Linkify.addLinks(website, Linkify.WEB_URLS);

//        bioguide.setText(test); //TODO check to make sure can get picture

//        contactButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String urlContact = representative.getContact_page();
//                Intent intent1 = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent1.putExtra(SearchManager.QUERY, urlContact);
//                startActivity(intent1);
//            }
//        });
//
//        websiteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String urlWebsite = representative.getWebsite();
//                Intent intent2 = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent2.putExtra(SearchManager.QUERY, urlWebsite);
//                startActivity(intent2);
//            }
//        });

        final String bioguide_id = representative.getBioguide_id();
        final String first_bio = bioguide_id.substring(0, 1);
        String photo = "http://bioguide.congress.gov/bioguide/photo/" + first_bio + "/"
                + bioguide_id + ".jpg";
//        bioguide.setText(photo); //TODO what do when no photo found
        //Photo is getting correct website
        //Get photo!
        Picasso.get().load(photo).
                into(photoView); //TOdo if picasso fails




        //TODO propublica
//        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//        String url = "https://api.propublica.org/congress/v1/members/" + bioguide_id
//                + ".json&api_key=W9mgP0QOnz5VmuBmb4j5x6IHrGdoOHDlTaQyagK7";


    }

}
