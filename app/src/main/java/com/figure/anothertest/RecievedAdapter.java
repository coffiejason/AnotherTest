package com.figure.anothertest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecievedAdapter extends RecyclerView.Adapter<RecievedAdapter.RViewHolder>{
    private Context context;
    private ErrandResItem resItem;
    private List<String> uris;

    RecievedAdapter(Context context,ErrandResItem items){
        this.context = context;
        this.resItem = items;


    }


    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_completed,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new RViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        //new Functions().loadImages(refs[position],holder.imageview);
        Picasso.get().load(resItem.uriNames.get(position)).into(holder.imageview);

        holder.imagecick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ECompleteFullscreen.class);
                //i.putExtra()

            }
        });
    }

    @Override
    public int getItemCount() { return resItem.uriNames.size(); }

    public class RViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout imagecick;
        ImageView imageview;

        public RViewHolder(@NonNull View itemView) {
            super(itemView);
            imagecick = itemView.findViewById(R.id.recieved_img);
            imageview = itemView.findViewById(R.id.recievedImage);
        }
    }
}
