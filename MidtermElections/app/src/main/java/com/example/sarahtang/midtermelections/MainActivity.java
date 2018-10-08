package com.example.sarahtang.midtermelections;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;
    public ArrayList<Representative> listRepresentatives;
    private RequestQueue queue;
    private TextView mTextView;
    private String getLocationZipcode;
    private String locationZip;
    private String locationState;
    private String locationCity;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.textView);
//        Button congressionalButton = (Button) findViewById(R.id.congressionalButton);
        listRepresentatives = new ArrayList<>();
        //Instantiate new request queue
        queue = Volley.newRequestQueue(getApplicationContext());
        Button currLocationButton = (Button) findViewById(R.id.currLocationButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //TODO hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Button goes to Recycler view and Congressional Acitivity
//        congressionalButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CongressionalActivity.class);
//                intent.putExtra("representativeList", listRepresentatives);
//                String full_location = locationCity + ", " + locationState + ", " + locationZip;
//                intent.putExtra("full_location", full_location);
//                startActivity(intent);
//            }
//        });

        //GOOGLE LOCATION API - getting current location
        //Create location services client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Check whether or not user has given permission to access location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            createLocationRequest();
            //ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    //MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }

        //Get last known location
        mFusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    String urlGeocode = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                            latitude + "," + longitude +
                            "&key=AIzaSyAjQIEWTn7JqieAs_wOqcYnfH_dCOzayAA";

                    //Create JSON Object
                    final JsonObjectRequest jsonGeocode = new JsonObjectRequest(Request.Method.GET,
                            urlGeocode, null,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject response) {
                                    //Gets zipcode and populates getLocationZipcode
                                    String zipcode;
                                    String city; //3
                                    String state; //5
                                    JSONArray address_components;
                                    try {
                                        address_components = response.getJSONArray("results").getJSONObject(0)
                                                //get first elem
                                                .getJSONArray("address_components");
                                        zipcode = address_components.getJSONObject(7).getString("long_name");
                                        city = address_components.getJSONObject(3).getString("long_name");
                                        state = address_components.getJSONObject(5).getString("short_name");
                                        getLocationZipcode = zipcode;
                                        textView.setText(city + ", " + state + ", " + zipcode);
                                        locationState = state;
                                        locationCity = city;

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            textView.setText("That didn't work!");
                        }
                    });
                    //Add the request to the RequestQueue.
                    queue.add(jsonGeocode);
                } else {
//                    textView.setText("No location found");
                }
            }
        });

        //Android Volley Request using manual entry zipcode
        mTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        //Add to listRepresentatives
        //Manual Zipcode button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mTextView.setText("");
                EditText zipcodeText = (EditText) findViewById(R.id.zipCodeEdit);
                String zipcode = zipcodeText.getText().toString();
                //Call Geocodio with zipcode and populate representativesList
                callGeocodio(zipcode);
                progressBar.setVisibility(View.VISIBLE);

                //MAKE THIS WORK WITH ONE BUTTON
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 2s = 2000ms
                        Intent intent = new Intent(MainActivity.this, CongressionalActivity.class);
                        intent.putExtra("representativeList", listRepresentatives);
                        String full_location = locationCity + ", " + locationState + ", " + locationZip;
                        intent.putExtra("full_location", full_location);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 2000);

            }
        });

        //Current Location button; call geocodio on given zipcode
        currLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRepresentatives.clear();

                if (getLocationZipcode == null) {
                    //If null set to Berkeley
                    callGeocodio("94709");
                } else {
                    callGeocodio(getLocationZipcode);
                }
                progressBar.setVisibility(View.VISIBLE);

                //ONE BUTTON
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 2s = 2000ms
                        Intent intent = new Intent(MainActivity.this, CongressionalActivity.class);
                        intent.putExtra("representativeList", listRepresentatives);
                        String full_location = locationCity + ", " + locationState + ", " + locationZip;
                        intent.putExtra("full_location", full_location);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 2000);

            }
        });

        //Random button
        Button randomButton = (Button) findViewById(R.id.randomButton);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting random zipcodes
                //zipcodes.xml, name = zip_codes
                progressBar.setVisibility(View.VISIBLE);
                Resources res = getResources();
                String[] allZipcodes = res.getStringArray(R.array.zip_codes);
                //Random int generator between 0 and 41466
                Random random = new Random();
                int num = random.nextInt(41466);
                String randomZipcode = allZipcodes[num];
//                mTextView.setText(randomZipcode);
                locationZip = randomZipcode;
                callGeocodio(randomZipcode);

                //Made it one button by adding a delay
                //TODO this is a jank fix

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 2s = 2000ms
                        Intent intent = new Intent(MainActivity.this, CongressionalActivity.class);
                        intent.putExtra("representativeList", listRepresentatives);
                        String full_location = locationCity + ", " + locationState + ", " + locationZip;
                        intent.putExtra("full_location", full_location);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, 2000);
            }
        });

    } //On create close

    //Make API call to Geocodio helper function
    protected void callGeocodio(String zipcode) {
        locationZip = zipcode;
        listRepresentatives.clear();
        String url = "https://api.geocod.io/v1.3/geocode?q="
                + zipcode + "&fields=cd,stateleg&api_key=1696e0bfe56b6b7011be886e88e567b45e0760b";

        //Create JSON Object
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                public void onResponse(JSONObject response) {
                    JSONArray congressional_districts;
                    JSONArray current_legislators;
                    String state;
                    String city;
                    try {
                        state = response.getJSONArray("results").getJSONObject(0)
                                .getJSONObject("address_components").getString("state");
                        city = response.getJSONArray("results").getJSONObject(0)
                                .getJSONObject("address_components").getString("city");
                        locationState = state;
                        locationCity = city;
                        congressional_districts = response.getJSONArray("results").getJSONObject(0)
                                .getJSONObject("fields").getJSONArray("congressional_districts");
                        for (int i = 0; i < congressional_districts.length(); i++) {
                            //For each legislator
                            current_legislators =
                                    congressional_districts.getJSONObject(i).
                                            getJSONArray("current_legislators");
                            for (int j = 0; j < current_legislators.length(); j++) {
                                String type =
                                        current_legislators.getJSONObject(j).getString("type");
                                String first_name =
                                        current_legislators.getJSONObject(j).
                                                getJSONObject("bio").getString("first_name");
                                String last_name =
                                        current_legislators.getJSONObject(j).
                                                getJSONObject("bio").getString("last_name");
                                String party =
                                        current_legislators.getJSONObject(j).
                                                getJSONObject("bio").getString("party");
                                String contact_page =
                                        current_legislators.getJSONObject(j).
                                                getJSONObject("contact").getString("contact_form");
                                String website =
                                        current_legislators.getJSONObject(j).
                                                getJSONObject("contact").getString("url");
                                String phone =
                                        current_legislators.getJSONObject(j).
                                                getJSONObject("contact").getString("phone");
                                String bioguide_id =
                                        current_legislators.getJSONObject(j).
                                                getJSONObject("references").getString("bioguide_id");

                                if (listRepresentatives.size() < 4) {
                                    //Don't add repeat Senators
                                    Representative rep = new Representative(type, first_name, last_name,
                                            party, contact_page, website, phone, bioguide_id);
                                    listRepresentatives.add(rep);
                                } else {
                                    continue;
                                }

                            }
                        }
//                        mTextView.setText("Got representatives!");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mTextView.setText("That didn't work!");
                }
            });
        // Add the request to the RequestQueue.
        queue.add(jsonObjReq);

    }

    //Google Location API
    //Prompt user to change location settings
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                RESULT_CANCELED); //REQUEST_CHECK_SETTINGS
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

}