package com.figure.anothertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WorldAdapter extends RecyclerView.Adapter<WorldAdapter.ViewHolder> {
    private Context context;
    List<TPPost> list;

    WorldAdapter(Context c, List<TPPost> allposts){
        this.context = c;
        Collections.reverse(allposts);
        this.list = allposts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_post,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.postText.setText(list.get(position).getpMessage());
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
            postUserID = itemView.findViewById(R.id.tv_username);
        }
    }
}
