package com.figure.anothertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataCollecionRVAdapter extends RecyclerView.Adapter<DataCollecionRVAdapter.DCViewHolder> {

    private Context context;
    private List<RP> rpList;
    List<String> names;


    DataCollecionRVAdapter(Context c, List<RP> rps){
        this.context = c;
        this.rpList = rps;
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
        if(rpList.get(position).getIsPicture()){
            holder.videoViewl.setVisibility(View.GONE);
            holder.imageView.setImageBitmap(rpList.get(position).getBitmap());
        }else{
            holder.imageView.setVisibility(View.GONE);
            holder.videoViewl.setVideoURI(rpList.get(position).getVideouri());
        }


    }

    @Override
    public int getItemCount() {
        return rpList.size();
    }

    static class DCViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout rowlayout;
        VideoView videoViewl;

        DCViewHolder(@NonNull View itemView) {
            super(itemView);
            videoViewl = itemView.findViewById(R.id.live_video);
            rowlayout = itemView.findViewById(R.id.row_dc_layout);
            imageView = itemView.findViewById(R.id.live_img);
        }
    }
}
