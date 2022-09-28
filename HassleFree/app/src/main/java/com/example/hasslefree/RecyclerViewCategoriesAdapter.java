package com.example.hasslefree;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewCategoriesAdapter extends RecyclerView.Adapter<RecyclerViewCategoriesAdapter.MyViewHolderCategories>{
    private final List<Categories> categoriesList;
    private OnItemClickListener onItemClickListener;
    private Context ctx;
    List<CardView>cardViewList = new ArrayList<>();

    RecyclerViewCategoriesAdapter(List<Categories>categories,  OnItemClickListener onItemClickListener, Context ctx){
        this.categoriesList = categories;
        this.onItemClickListener = onItemClickListener;
        this.ctx = ctx;
    }


    @NonNull
    @Override
    public RecyclerViewCategoriesAdapter.MyViewHolderCategories onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_categories,parent,false);
        for(CardView cardView : cardViewList){
            cardView.setCardBackgroundColor(ctx.getResources().getColor(R.color.white));
        }
        return new MyViewHolderCategories(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategoriesAdapter.MyViewHolderCategories holder, int position) {
        final Categories categories = categoriesList.get(position);
        holder.categoryTitle.setText(categories.getTitle());
        Glide.with(ctx).load(categories.getImage()).into(holder.categoryImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v , position);
                cardViewList.add(holder.cardView); //add all the cards to this list
                for(CardView cardView : cardViewList){
                    cardView.setCardBackgroundColor(ctx.getResources().getColor(R.color.white));
                }
                //The selected card is set to colorSelected
                holder.cardView.setCardBackgroundColor(Color.parseColor("#E5E7E9"));

            }
        });
   }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class MyViewHolderCategories extends RecyclerView.ViewHolder{
        private final TextView categoryTitle;
        private final ImageView categoryImage;
        private final CardView cardView;
        public MyViewHolderCategories(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryName);
            categoryImage = itemView.findViewById(R.id.categoryImage);
            cardView = itemView.findViewById(R.id.cardViewCategories);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
