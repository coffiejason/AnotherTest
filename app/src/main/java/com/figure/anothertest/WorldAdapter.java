package com.figure.anothertest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WorldAdapter extends RecyclerView.Adapter<WorldAdapter.ViewHolder> {
    private Context context;
    List<UtilGenItem> list;
    Activity activity;

    WorldAdapter(Context c, List<UtilGenItem> allposts,Activity activity){
        this.context = c;
        Collections.reverse(allposts);
        this.list = allposts;
        this.activity = activity;
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

        if(list.get(position).getStatus().equals("PENDING")){
            holder.status_msg.setVisibility(View.VISIBLE);
        }

        holder.rowlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context,SharedPrefs.getTaskId()+" "+list.get(position).tasknum(),Toast.LENGTH_SHORT).show();

                if(SharedPrefs.getTaskId().equals(list.get(position).tasknum()) ){

                    Intent i = new Intent(context,  UtilGEList.class);
                    i.putExtra("tasknum",""+list.get(position).tasknum());
                    i.putExtra("posterID",""+list.get(position).getPosterID());
                    context.startActivity(i);

                }
                else if(SharedPrefs.getTaskId().equals("none")){
                    Intent i = new Intent(context,  UtilGEList.class);
                    i.putExtra("tasknum",""+list.get(position).tasknum());
                    i.putExtra("posterID",""+list.get(position).getPosterID());
                    context.startActivity(i);

                }
                else{
                    Toast.makeText(context," Complete or Drop current Errands to proceed ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.rowlayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Functions().clearTask(context,list.get(position).tasknum());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView postText,status_msg;
        TextView postUserID;
        RelativeLayout rowlayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            postText = itemView.findViewById(R.id.tv_tp_post);
            rowlayout = itemView.findViewById(R.id.row_item_layout);
            postUserID = itemView.findViewById(R.id.tv_name);
            status_msg = itemView.findViewById(R.id.status_msg);

        }
    }
}
