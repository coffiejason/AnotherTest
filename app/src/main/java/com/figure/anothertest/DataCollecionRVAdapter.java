package com.figure.anothertest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new DCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DCViewHolder holder, final int position) {
        if(rpList.get(position).getIsPicture()){
            holder.videoViewl.setVisibility(View.GONE);
            try{
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),rpList.get(position).getImageUri());
                holder.imageView.setImageBitmap(bitmap);

                holder.rowlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, FullScreenImage.class);
                        i.putExtra("imguri",rpList.get(position).getImageUri().toString());
                        context.startActivity(i);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
