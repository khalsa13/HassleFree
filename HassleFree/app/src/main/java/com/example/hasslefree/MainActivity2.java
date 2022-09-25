package com.example.hasslefree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private Destination defaultDestination;
    private String errorImage = "<default_image_path>";
    private ArrayList<Destination> destinations;

    // get the fields using R.ID thing and set the data from the destinations.
    private void populateData(Destination destination){
        TextView destinationName = findViewById(R.id.textView5);
        TextView rating = findViewById(R.id.textView6);
        TextView description = findViewById(R.id.description);
        TextView travelCost = findViewById(R.id.travelCost);
        ImageView imageView = findViewById(R.id.imageView4);

        destinationName.setText(destination.getDestinationName());
        rating.setText(Double.toString(destination.getRating()));
        String _description = destination.getDescription().replace("\n", "&lt;br&gt").replace("_", " ");
        description.setText(Html.fromHtml(Html.fromHtml(_description).toString()));

        travelCost.setText(CalculateTravelCost(destination));
        Glide.with(getApplicationContext()).load(destination.getImage()).into(imageView);
    }

    private String CalculateTravelCost(Destination destination){
        double cost = 50.0 + destination.getDistance() * 7.5;
        int _cost = (int)cost;
        return "around " + Integer.toString(_cost);
    }

    private Destination getDestinationByName(String current){
        for (Destination x : destinations){
            if (x.getDestinationName() == current){
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
        defaultDestination = new Destination("default_name", "default_destination", 0, 0, errorImage, "Dummy Description");
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
}