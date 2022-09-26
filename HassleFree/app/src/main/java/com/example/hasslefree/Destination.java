package com.example.hasslefree;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class Destination implements Parcelable {
    private final String destinationName, exactLocation, image, description;
    private final double distance, lat ,lng, dropLat, dropLng, rating;

    Destination(String destinationName, String exactLocation, double distance, double rating, String image, String description, double lat, double lng, double dropLat, double dropLng){
        this.distance = distance;
        this.exactLocation = exactLocation;
        this.destinationName = destinationName;
        this.rating = rating;
        this.image = image;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.dropLat = dropLat;
        this.dropLng = dropLng;
    }

    public String getDescription(){
        return this.description;
    }

    protected Destination(Parcel in) {
        destinationName = in.readString();
        exactLocation = in.readString();
        image = in.readString();
        distance = in.readDouble();
        rating = in.readDouble();
        description = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        dropLat = in.readDouble();
        dropLng = in.readDouble();
    }

    public static final Creator<Destination> CREATOR = new Creator<Destination>() {
        @Override
        public Destination createFromParcel(Parcel in) {
            return new Destination(in);
        }

        @Override
        public Destination[] newArray(int size) {
            return new Destination[size];
        }
    };

    public String getDestinationName() {
        return this.destinationName;
    }
    public String getExactLocation(){
        return this.exactLocation;
    }
    public double getDistance(){
        return this.distance;
    }
    public double getLat(){
        return this.lat;
    }
    public double getLng(){
        return this.lng;
    }
    public double getDropLat(){
        return this.dropLat;
    }
    public double getDropLng(){
        return this.dropLng;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(destinationName);
        parcel.writeString(exactLocation);
        parcel.writeString(image);
        parcel.writeDouble(distance);
        parcel.writeDouble(rating);
        parcel.writeString(description);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
        parcel.writeDouble(dropLat);
        parcel.writeDouble(dropLng);
    }

    @Override
    public String toString(){
        return this.destinationName + " " + this.rating;
    }
}
