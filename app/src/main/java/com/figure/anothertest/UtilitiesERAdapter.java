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
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        if(items.size()>0){
            holder.name.setText(items.get(position).getCustomerName());
            holder.meternum.setText(items.get(position).getMeterNum());
            holder.town.setText(items.get(position).getTown());


            holder.directionsbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q= 5.709347,-0.205360"));
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
        TextView meternum,name,town;
        RelativeLayout rowlayout,directionsbtn;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            town = itemView.findViewById(R.id.tv_town);
            name = itemView.findViewById(R.id.tv_name);
            meternum = itemView.findViewById(R.id.tv_tp_post);
            rowlayout = itemView.findViewById(R.id.row_item_layout);
            directionsbtn = itemView.findViewById(R.id.directionsbtn);
        }
    }
}
