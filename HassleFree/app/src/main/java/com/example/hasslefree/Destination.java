package com.example.hasslefree;

import java.util.Comparator;

public class Destination  {
    private final String destinationName, exactLocation, image;
    private final double distance;
    private final double rating;

    Destination(String destinationName, String exactLocation, double distance, double rating, String image){
        this.distance = distance;
        this.exactLocation = exactLocation;
        this.destinationName = destinationName;
        this.rating = rating;
        this.image = image;
    }


    public String getDestinationName() {
        return this.destinationName;
    }
    public String getExactLocation(){
        return this.exactLocation;
    }
    public double getDistance(){
        return this.distance;
    }
    public double getRating(){
        return this.rating;
    }
    public String getImage(){
        return this.image;
    }
/*
    @Override
    public int compareTo(Destination destination) {
        double otherRating = destination.getRating();

        if (this.getRating() == otherRating)
            return 0;
        else if (this.getRating() < otherRating)
            return 1;
        else
            return -1;
    }*/
    static Comparator<Destination> ratingComparator() {
        return new Comparator<Destination>() {

            @Override
            public int compare(Destination d1, Destination d2) {
                if (d1.getRating() == d2.getRating())
                    return 0;
                else if (d1.getRating() < d2.getRating())
                    return 1;
                else
                    return -1;
            }
        };
    }

    static Comparator<Destination> distanceComparator() {
        return new Comparator<Destination>() {
            @Override
            public int compare(Destination d1, Destination d2) {
                if (d1.getDistance() == d2.getDistance())
                    return 0;
                else if (d1.getDistance() > d2.getDistance())
                    return 1;
                else
                    return -1;
            }
        };
    }

}
