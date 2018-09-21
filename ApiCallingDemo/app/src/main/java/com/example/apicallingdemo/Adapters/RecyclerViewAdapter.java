package com.example.apicallingdemo.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apicallingdemo.Model.DataModelClass;
import com.example.apicallingdemo.R;

import java.util.ArrayList;

/**
 * Created by Pawan on 26-03-2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    ArrayList<DataModelClass> dataModelClassArrayList = new ArrayList<>();
    DataModelClass dataModelClass = new DataModelClass();
    Context context;
    private static ClickListener clickListener;
    private int lastPosition = -1;

    public RecyclerViewAdapter(ArrayList<DataModelClass> dataModelClassArrayList, DataModelClass dataModelClass, Context context){
        this.dataModelClassArrayList = dataModelClassArrayList;
        this.dataModelClass = dataModelClass;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerlayout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        dataModelClass = dataModelClassArrayList.get(position);

        String name = dataModelClass.getName();
        Log.d("RecyclerAdapter","name : "+name);
        holder.title.setText(name);

        Glide.with(context)
                .load(dataModelClass.getImageurl())
                .into(holder.imageView);

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position){
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return dataModelClassArrayList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerViewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        /*void onItemLongClick(int position, View v);*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title, imageUrl;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            title =  itemView.findViewById(R.id.txt_title);
            imageUrl = itemView.findViewById(R.id.txt_description);
            imageView = itemView.findViewById(R.id.img_view);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
            Toast.makeText(context,"Hello",Toast.LENGTH_SHORT).show();
        }
    }
}
