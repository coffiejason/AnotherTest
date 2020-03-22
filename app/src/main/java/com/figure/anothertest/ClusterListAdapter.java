package com.figure.anothertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClusterListAdapter extends RecyclerView.Adapter<ClusterListAdapter.CLViewHolder> {

    Context context;
    String mMessage[];


    public ClusterListAdapter(Context c, String message[]){
        this.mMessage = message;
        this.context = c;

    }


    @NonNull
    @Override
    public CLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_post,parent,false);
        return new CLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CLViewHolder holder, int position) {

        holder.postText.setText(mMessage[position]);

    }

    @Override
    public int getItemCount() {
        return mMessage.length;
    }

    public class CLViewHolder extends RecyclerView.ViewHolder{
        TextView postText;
        public CLViewHolder(@NonNull View itemView) {
            super(itemView);

            postText = itemView.findViewById(R.id.tv_tp_post);
        }
    }
}
