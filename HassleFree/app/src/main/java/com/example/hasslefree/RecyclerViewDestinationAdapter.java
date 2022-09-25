package com.example.hasslefree;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewDestinationAdapter extends RecyclerView.Adapter<RecyclerViewDestinationAdapter.MyViewHolder>{

    private final List<Destination> destinationsList;
    private  ClickListener<Destination>clickListener = null;
    private Context ctx;

    RecyclerViewDestinationAdapter(List<Destination>destinations, Context ctx)
    {
        this.destinationsList = destinations;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RecyclerViewDestinationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_destinations,parent,false);
        return new MyViewHolder(view);
    }

    public void setOnItemClickListener(ClickListener<Destination> destinationClickListener) {
        this.clickListener = destinationClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDestinationAdapter.MyViewHolder holder, int position) {
        final Destination destination = destinationsList.get(position);
        holder.destinationName.setText(destination.getDestinationName());
        Glide.with(ctx).load(destination.getImage()).into(holder.destinationImage);
        holder.exactLocation.setText(destination.getExactLocation());
        holder.rating.setText(String.valueOf(destination.getRating()));
        String distanceValue = String.valueOf(destination.getDistance()).substring(0,2).replace(".","") + " Km";
        holder.distance.setText(distanceValue);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(ctx, "clicking new clicker!!", Toast.LENGTH_SHORT).show();
                clickListener.onItemClick(destination);
            }
        });

    }

    @Override
    public int getItemCount() {
        return destinationsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView destinationName, distance, exactLocation, rating;
        private final ImageView destinationImage;
        private final CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            destinationName = itemView.findViewById(R.id.destinationName);
            distance = itemView.findViewById(R.id.distance);
            exactLocation = itemView.findViewById(R.id.exactLocation);
            rating = itemView.findViewById(R.id.rating);
            destinationImage = itemView.findViewById(R.id.destinationImage);
            cardView = itemView.findViewById(R.id.cardViewDestination);
        }
    }

    interface ClickListener<T> {
        void onItemClick(T data);
    }
}
