package com.figure.anothertest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WorldAdapter extends RecyclerView.Adapter<WorldAdapter.ViewHolder> {
    private Context context;
    List<UtilGenItem> list;
    boolean tasks;

    WorldAdapter(Context c, List<UtilGenItem> allposts,boolean tsks){
        this.context = c;
        Collections.reverse(allposts);
        this.list = allposts;
        this.tasks = tsks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_world,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.postText.setText(list.get(position).getMessage());
        holder.postUserID.setText(list.get(position).getTiperID());

        holder.rowlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tasks){
                    Intent i = new Intent(context,  UtilGEList.class);
                    i.putExtra("tasknum",""+list.get(position).tasknum());
                    i.putExtra("posterID",""+list.get(position).getPosterID());
                    context.startActivity(i);
                }
                else{
                    Toast.makeText(context,"Submit or drop previous Errands to proceed",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView postText;
        TextView postUserID;
        RelativeLayout rowlayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            postText = itemView.findViewById(R.id.tv_tp_post);
            rowlayout = itemView.findViewById(R.id.row_item_layout);
            postUserID = itemView.findViewById(R.id.tv_name);
        }
    }
}
