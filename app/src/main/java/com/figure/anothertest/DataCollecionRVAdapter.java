package com.figure.anothertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataCollecionRVAdapter extends RecyclerView.Adapter<DataCollecionRVAdapter.DCViewHolder> {

    private Context context;
    private List<Bitmap> images;
    List<String> names;


    DataCollecionRVAdapter(Context c, List<Bitmap> imgs){
        this.context = c;
        this.images = imgs;
    }

    @NonNull
    @Override
    public DCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_request,parent,false);
        return new DCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DCViewHolder holder, int position) {
        holder.imageView.setImageBitmap(images.get(position));

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class DCViewHolder extends RecyclerView.ViewHolder {
        //TextView postText;
        ImageView imageView;
        RelativeLayout rowlayout;

        DCViewHolder(@NonNull View itemView) {
            super(itemView);
            //postText = itemView.findViewById(R.id.live_img);
            rowlayout = itemView.findViewById(R.id.row_dc_layout);
            imageView = itemView.findViewById(R.id.live_img);
        }
    }
}
