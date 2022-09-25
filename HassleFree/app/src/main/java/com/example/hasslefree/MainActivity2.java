package com.example.hasslefree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private Destination defaultDestination;
    private String errorImage = "<default_image_path>";
    private ArrayList<Destination> destinations;

    // get the fields using R.ID thing and set the data from the destinations.
    private void populateData(Destination destination){
        TextView destinationName = findViewById(R.id.textView5);
        TextView rating = findViewById(R.id.rating);
        TextView description = findViewById(R.id.description);
        TextView travelCost = findViewById(R.id.travelCost);

        destinationName.setText(destination.getDestinationName());
        rating.setText(new Double(destination.getRating()).toString());
        description.setText(destination.getDescription());
        travelCost.setText(CalculateTravelCost(destination));
    }

    private String CalculateTravelCost(Destination destination){
        return "<Dummy_Cost>";
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
        defaultDestination = new Destination("default_name", "default_destination", 0, 0, errorImage);
        destinations = getIntent().getParcelableArrayListExtra("destinations");
        String current = getIntent().getStringExtra("current");
        Destination currentDestination = getDestinationByName(current);
        populateData(currentDestination);

    }
}