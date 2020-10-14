package com.figure.anothertest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class UtilitiesERAdapter extends RecyclerView.Adapter<UtilitiesERAdapter.ViewHolder>{
    Context context;
    List<UtilitiesERitem> items;
    String taskNum,date;

    UtilitiesERAdapter(Context c, List<UtilitiesERitem> list,String taskNum,String date){
        this.context = c;
        this.items = list;
        this.taskNum = taskNum;
        this.date = date;
    }

    UtilitiesERAdapter(Context c, List<UtilitiesERitem> list,String taskNum){
        this.context = c;
        this.items = list;
        this.taskNum = taskNum;
    }

    UtilitiesERAdapter(Context c, List<UtilitiesERitem> list){
        this.context = c;
        this.items = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_utility_e_r,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new UtilitiesERAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(items.size()>0){
            holder.name.setText(items.get(position).getCustomerName());
            holder.meternum.setText(items.get(position).getMeterNum());
            holder.town.setText(items.get(position).getTown());

            if(SharedPrefs.getMeternum("meternum"+position) != null && SharedPrefs.getMeterRead("reading"+position) != null && SharedPrefs.getImageUri("picread"+position) != null){
                holder.done.setVisibility(View.VISIBLE);
                holder.done.setText("DONE");
            }

            holder.directionsbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    holder.img_directionsBtn_clicked.setVisibility(View.VISIBLE);
                    holder.img_directionsBtn.setVisibility(View.GONE);
                    */

                    new Functions().showNotifications(context,items.get(position).getCustomerName(),items.get(position).getMeterNum());

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q= "+items.get(position).getLocation().latitude+","+items.get(position).getLocation().longitude));
                    //5.709347,-0.205360
                   //intent.setPackage("com.google.android.apps.apps.maps");

                    context.startActivity(intent);
                }
            });

            holder.rowlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,UtilTakeDataActivity.class);
                    i.putExtra("Name",""+items.get(position).getCustomerName());
                    i.putExtra("Meterno",""+items.get(position).getMeterNum());
                    i.putExtra("Town",""+items.get(position).getTown());
                    i.putExtra("position",""+position);
                    i.putExtra("tasknum",""+taskNum);
                    i.putExtra("Date",""+date);
                    context.startActivity(i);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView meternum,name,town,done;
        RelativeLayout rowlayout,directionsbtn;
        ImageView img_directionsBtn, img_directionsBtn_clicked;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            town = itemView.findViewById(R.id.tv_town);
            name = itemView.findViewById(R.id.tv_name);
            meternum = itemView.findViewById(R.id.tv_tp_post);
            rowlayout = itemView.findViewById(R.id.row_item_layout);
            directionsbtn = itemView.findViewById(R.id.directionsbtn);
            done = itemView.findViewById(R.id.tv_tweet_edited);
            img_directionsBtn = itemView.findViewById(R.id.img_directions);
            img_directionsBtn_clicked = itemView.findViewById(R.id.img_directions_clicked);
        }
    }
}
