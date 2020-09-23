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

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private Context context;
    List<String> errandmsgs;

    private List<ErrandItem> errandItems;

    NotificationsAdapter(Context c,List<ErrandItem>msgs){
        this.context = c;
        this.errandItems = msgs;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_viewnotifications,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(errandItems.size()>0){
            holder.postText.setText(errandItems.get(position).getErrandMessage());

            /*
                holder.rowlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context,Tper2Activity.class);
                        i.putExtra("msg",errandItems.get(position).getErrandMessage());
                        i.putExtra("tid",errandItems.get(position).getTiperID());
                        context.startActivity(i);
                    }
                });*/

            //working
            /*
            holder.rowlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,  UtititiesERActivity.class);
                    context.startActivity(i);
                }
            });*/

        }


    }

    @Override
    public int getItemCount() {
        return errandItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView postText;
        RelativeLayout rowlayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            postText = itemView.findViewById(R.id.tv_tp_post);
            rowlayout = itemView.findViewById(R.id.row_item_layout);
        }
    }
}
