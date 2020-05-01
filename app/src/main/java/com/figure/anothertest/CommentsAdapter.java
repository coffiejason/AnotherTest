package com.figure.anothertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<ClusterListAdapter.CLViewHolder> {
    private Context context;
    private String message;

    CommentsAdapter(Context context, Comment c){
        this.context = context;
        this.message = c.getMessage();
    }

    @NonNull
    @Override
    public ClusterListAdapter.CLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_post,parent,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ClusterListAdapter.CLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClusterListAdapter.CLViewHolder holder, int position) {
        holder.postText.setText(message);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class CLViewHolder extends RecyclerView.ViewHolder {
        TextView postText;
        RelativeLayout rowlayout;

        public CLViewHolder(@NonNull View itemView) {
            super(itemView);

            postText = itemView.findViewById(R.id.tv_tp_post);
            rowlayout = itemView.findViewById(R.id.row_item_layout);
        }
    }
}
