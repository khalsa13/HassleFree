package com.example.hasslefree;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private TextView locationText;
    AutocompleteSupportFragment autocompleteFragment;
    private Button checkBestRoute;
    protected LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    private TabLayout tabLayout;
    private String globalDestination;
    private String selectedCity = null;
    double latitGlobal, longitGlobal;
    private RecyclerView recyclerViewDestination = null, recyclerViewCategories = null;
    private RecyclerViewDestinationAdapter recyclerViewDestinationAdapter = null;
    private RecyclerViewCategoriesAdapter recyclerViewCategoriesAdapter = null;
    private ArrayList<Destination>destinations;
    private int globalTabPosition;

    String API_KEY;
  //  private LottieAnimationView lottieAnimationView;

    // call this method from on click listner.
    // pass the data of the place that needs to be opened in the new tab.
    private void callSecondActivity(Destination data){
        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
        intent.putParcelableArrayListExtra("destinations", destinations);
        intent.putExtra("current", data);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = "YOUR API KEY";
        // lottieAnimationView = findViewById(R.id.loading);
        locationText = (TextView) findViewById(R.id.cityName);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation(locationText);
        }
        checkBestRoute = (Button) findViewById(R.id.completeDetailButton);
        checkBestRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity3.class);
                fetchApi(0, locationText.getText().toString());
                destinations.add(new Destination("Source", "Source", 0.0, 0.0, "dummy", "dummy", latitGlobal, longitGlobal, 0.0, 0.0));
                intent.putExtra("LIST", (Serializable) destinations);
                startActivity(intent);
            }
        });


        recyclerViewCategories = (RecyclerView) findViewById(R.id.recyclerViewCategories);
        recyclerViewCategoriesAdapter = new RecyclerViewCategoriesAdapter(createCategories(), new RecyclerViewCategoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d("position is : ", "pos " + position);
                destinations.clear();
                switch (position) {
                    case 0:
                        globalDestination = "best tourist attraction in";
                        break;
                    case 1:
                        globalDestination = "best food places in";
                        break;
                    case 2:
                        globalDestination = "best shopping places in";
                        break;
                    default:
                        globalDestination = "best hotels in";
                        break;
                }
                fetchApi(globalTabPosition, locationText.getText().toString());
            }
        }, getBaseContext());
        PopulateCategoriesRecyclerView(recyclerViewCategories, recyclerViewCategoriesAdapter);
        globalDestination = "best tourist attraction in";
        globalTabPosition = 0;

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.searchBar);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), API_KEY, Locale.US);
        }
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("SearchPlace", "Place: " + place.getName() + ", " + place.getId());
                latitGlobal = place.getLatLng().latitude;
                longitGlobal = place.getLatLng().longitude;
                selectedCity = place.getName();
                final TextView textView = (TextView) findViewById(R.id.cityName);
                textView.setText(selectedCity);
                fetchApi(globalTabPosition, selectedCity);
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("SearchPlace", "An error occurred: " + status);
            }
        });

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Popular"));//Recommendation
        tabLayout.addTab(tabLayout.newTab().setText("Nearest"));
        tabLayout.addTab(tabLayout.newTab().setText("All"));

        recyclerViewDestination = (RecyclerView) findViewById(R.id.recyclerViewDestinations);

        if (selectedCity == null) {
            final TextView textView = (TextView) findViewById(R.id.cityName);
            textView.setText(locationText.getText().toString());
            fetchApi(globalTabPosition, locationText.getText().toString());
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                destinations.clear();
                if (tab.getPosition() == 0) {
                    fetchApi(tab.getPosition(), locationText.getText().toString());
                } else if (tab.getPosition() == 1) {
                    fetchApi(tab.getPosition(), locationText.getText().toString());
                } else {
                    fetchApi(tab.getPosition(), locationText.getText().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    public void fetchApi(int filter, String city) {
        //for emulator testing hardcode lat, lng.
        /*latitGlobal = 18.516726;
        longitGlobal = 73.856255;*/
        globalTabPosition = filter;
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?keyword="+ globalDestination + city +"&location="+latitGlobal+"%2C"+longitGlobal+"&radius=50000&key="+API_KEY;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, uri,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("VolleyResponse", "response: " + response);
                int n = 0;
                try {

                    destinations = new ArrayList<>();
                    JSONArray cast = response.getJSONArray("results");
                    n = cast.length();
                    n = Math.min(n, 10);

                    for (int i = 0; i < n; i++) {
                    JSONObject actor = cast.getJSONObject(i);
                    JSONObject location = actor.getJSONObject("geometry").getJSONObject("location");
                    String photoReference;
                    try {
                        JSONObject photos = actor.getJSONArray("photos").getJSONObject(0);
                        photoReference = photos.getString("photo_reference");
                    }
                    catch (Exception e)
                    {
                        photoReference = "";
                    }

                    String photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&maxheight=200&photo_reference="+photoReference+"&key="+ API_KEY;
                    String name = actor.getString("name");
                    double rating = actor.getDouble("rating");
                    double distance = getKmFromLatLong(latitGlobal, longitGlobal, location.getDouble("lat"), location.getDouble("lng") );
                    String[] address = actor.getString("vicinity").split(",");
                    Log.i("SearchAddress", Arrays.toString(address));
                    String exactLocation = address[0];
                    String description = "";
                    JSONArray types = actor.getJSONArray("types");
                    for(int _p=0;_p<types.length();_p++){
                        description += types.getString(_p);
                        description += "\n";
                    }
                    Thread.sleep(50);
                    destinations.add(new Destination(name, exactLocation, distance, rating, photoUrl, description,location.getDouble("lat"), location.getDouble("lng"), latitGlobal, longitGlobal));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(filter == 0)
                {
                    Collections.sort(destinations, Destination.ratingComparator());
                }
                else if(filter == 1)
                {
                    Collections.sort(destinations, Destination.distanceComparator());
                }
                if(filter!=2 && destinations.size() > 5)
                {
                    destinations.subList(5,destinations.size()).clear();
                }
                recyclerViewDestinationAdapter = new RecyclerViewDestinationAdapter(destinations, getBaseContext());
                PopulateDestinationRecyclerView(recyclerViewDestination, recyclerViewDestinationAdapter);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        }) {
        };
        requestQueue.add(request);
    }

    public static double getKmFromLatLong(double lat1, double lng1, double lat2, double lng2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);
        double distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters/1000;
    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation(TextView locationText) {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if (locationGPS != null) {
                latitGlobal = locationGPS.getLatitude();
                longitGlobal = locationGPS.getLongitude();
                String city = getAddress(latitGlobal, longitGlobal);
                locationText.setText(city);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void PopulateCategoriesRecyclerView(RecyclerView recyclerViewCategories, RecyclerViewCategoriesAdapter recyclerViewCategoriesAdapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager);
        recyclerViewCategories.setAdapter(recyclerViewCategoriesAdapter);
    }

    private void PopulateDestinationRecyclerView(RecyclerView recyclerView, RecyclerViewDestinationAdapter recyclerViewDestinationAdapter){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewDestinationAdapter.setOnItemClickListener(new RecyclerViewDestinationAdapter.ClickListener<Destination>(){

            @Override
            public void onItemClick(Destination data) {
                callSecondActivity(data);
                // Toast.makeText(MainActivity.this, data.getDestinationName() + "$babnish", Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView.setAdapter(recyclerViewDestinationAdapter);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        //Toast.makeText(this,location.getLatitude()+"  "+ location.getLongitude(), Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(this, LocationService.class);
//        intent.putExtra("lastLat", latitGlobal);
//        intent.putExtra("lastLong", longitGlobal);
//        startService(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("Latitude","disable");
    }

    private List<Categories> createCategories() {
        List<Categories> categories = new ArrayList<>();
        categories.add(new Categories("https://youmatter.world/app/uploads/sites/2/2019/11/travel-world.jpg?w=100&h=100", "Travel"));
        categories.add(new Categories("https://d18mqtxkrsjgmh.cloudfront.net/public/2021-03/Eating%20More%20Ultraprocessed%20%E2%80%98Junk%E2%80%99%20Food%20Linked%20to%20Higher%20CVD%20Risk.jpeg?w=100&h=100", "Food"));
        categories.add(new Categories("https://img.freepik.com/free-photo/girl-holds-fashion-shopping-bag-beauty_1150-13673.jpg?w=100&h=100", "Shopping"));
        categories.add(new Categories("https://cdn.britannica.com/96/115096-050-5AFDAF5D/Bellagio-Hotel-Casino-Las-Vegas.jpg?w=100&h=100", "Hotels"));
        return categories;
    }
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String district = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);
            district = obj.getSubAdminArea();
            Toast.makeText(this,"You are at "+ district,Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return district;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra("lastLat", latitGlobal);
        intent.putExtra("lastLong", longitGlobal);
        startService(intent);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Intent intent = new Intent(this, LocationService.class);
//        intent.putExtra("lastLat", latitGlobal);
//        intent.putExtra("lastLong", longitGlobal);
//        startService(intent);
//    }
}