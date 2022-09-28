package com.example.hasslefree;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private Destination defaultDestination;
    private String errorImage = "<default_image_path>";
    private ArrayList<Destination> destinations;
    private Button bookCab;
    private  EditText description;
    private String queryText;
    // get the fields using R.ID thing and set the data from the destinations.
    private void populateData(Destination destination) {
        TextView destinationName = findViewById(R.id.textView5);
        TextView rating = findViewById(R.id.textView6);
        description = findViewById(R.id.description);
        TextView travelCost = findViewById(R.id.travelCost);
        ImageView imageView = findViewById(R.id.imageView4);
        bookCab = findViewById(R.id.buttonBookCab);
        queryText = destination.getDestinationName();
        new doIT().execute();

        destinationName.setText(destination.getDestinationName());
        rating.setText(Double.toString(destination.getRating()));
        String _description = destination.getDescription().replace("&lt;br&gt", "\n").replace("_", " ");
        Log.d("desc", _description);
        description.setTextColor(Color.BLACK);
        description.setAllCaps(true);
        travelCost.setText(CalculateTravelCost(destination));
        Glide.with(getApplicationContext()).load(destination.getImage()).into(imageView);
        //fetchEstimates(destination.getLat(), destination.getLng(), destination.getDropLat(), destination.getDropLng());

        bookCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(destination.getLat(), destination.getLng(), destination.getDropLat(), destination.getDropLng());
            }
        });
    }

    private String CalculateTravelCost(Destination destination) {
        double cost = 50.0 + destination.getDistance() * 7.5;
        int _cost = (int) cost;
        return "~ INR " + Integer.toString(_cost) + "/person";
    }

    private Destination getDestinationByName(String current) {
        for (Destination x : destinations) {
            if (x.getDestinationName() == current) {
                return x;
            }
        }
        return defaultDestination;
    }
    //TODO: Add swipe listener to change the current to next or prev.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        defaultDestination = new Destination("default_name", "default_destination", 0, 0, errorImage, "Dummy Description", 0.0, 0.0, 0.0, 0.0);
        destinations = getIntent().getParcelableArrayListExtra("destinations");
        // String current = getIntent().getStringExtra("current");
        Destination currentDestination = getIntent().getParcelableExtra("current");
        // Toast.makeText(getApplicationContext(),currentDestination.toString(), Toast.LENGTH_SHORT).show();
        /*String _destinations = "";
        for(Destination d : destinations){
            _destinations += d.toString();
            _destinations += " ";
        }
        Log.i("2ndActivity", _destinations);
        */
        populateData(currentDestination);
    }

    public void startNewActivity(Double latPickup, Double lngPickup, Double latDrop, Double lngDrop) {

        try {
            PackageManager pm = this.getPackageManager();
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String clientId = "jhx1vb3A_OmLbYe9V41UUxOP9nDKJNlW";
            String uri = "https://m.uber.com/ul/?action=setPickup&client_id=" + clientId + "&pickup[latitude]=" + latPickup + "&pickup[longitude]=" + lngPickup + "&dropoff[latitude]=" + latDrop + "&dropoff[longitude]=" + lngDrop;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);

        } catch (PackageManager.NameNotFoundException e) {
            Log.d("log", "package not found : uber");

        }
    }


    public void fetchEstimates(Double latPickup, Double lngPickup, Double latDrop, Double lngDrop) {
        String clientId = "jhx1vb3A_OmLbYe9V41UUxOP9nDKJNlW";
        String url = "https://api.uber.com/v1.2/estimates/price?start_latitude="+latPickup+"&start_longitude="+lngPickup+"&end_latitude="+latDrop+"&end_longitude="+ lngDrop;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String uri = Uri.parse(url)
                .buildUpon()
                .build().toString();
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, uri,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("VolleyResponse", "response: " + response);

                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", error.toString());
            }
        }) { @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Content-Type", "application/json");
            params.put("Authorization", "Token <token>");
            return params;
            }
        };
        requestQueue.add(request);
    }

    public class doIT extends AsyncTask<Void,Void,Void> {
        String words;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                String request = "https://search.yahoo.com/search?p="+queryText;
                Document doc = Jsoup
                        .connect(request).get();
                Elements links = doc.getElementsByClass("compText mt-16 mb-16 cl-b fc-falcon pl-15 pr-15 fz-13 lh-16");

                words = links.text();
            } catch (IOException e) {
                e.printStackTrace();
            } return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            words = words.replace("Wikipedia","");
            description.setText(words);
        }
    }

}
