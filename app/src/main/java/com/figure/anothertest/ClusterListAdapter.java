package com.figure.anothertest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClusterListAdapter extends RecyclerView.Adapter<ClusterListAdapter.CLViewHolder> {

    private Context context;
    private String[] mMessage;
    private String[] mUserid;
    private String[] mPostid;
    private String message;
    private boolean single = false;



    ClusterListAdapter(Context c, String[] message,String[] userIDs,String[] postid){
        this.mMessage = message;
        this.mUserid = userIDs;
        this.context = c;
        this.mPostid = postid;

    }

    ClusterListAdapter(Context c, String message){
        this.message = message;
        this.context = c;
        single = true;
    }

    @NonNull
    @Override
    public CLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rowitem_post,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new CLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CLViewHolder holder, final int position) {

        if(single){
            holder.postText.setText(message);
            Log.d("artgcdrthjk","this code runned");
        }
        else{
            holder.postText.setText(mMessage[position]);
            holder.postUserID.setText(mUserid[position]);

            holder.rowlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CommentActivity.class);
                    i.putExtra("msg",mMessage[position]);
                    i.putExtra("id",mUserid[position]);
                    i.putExtra("postid",mPostid[position]);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(single){
            return 0;
        }
        else{
            return mMessage.length-1;
        }
    }

    public static class CLViewHolder extends RecyclerView.ViewHolder {
        TextView postText;
        TextView postUserID;
        RelativeLayout rowlayout;

        public CLViewHolder(@NonNull View itemView) {
            super(itemView);
            postText = itemView.findViewById(R.id.tv_tp_post);
            rowlayout = itemView.findViewById(R.id.row_item_layout);
            postUserID = itemView.findViewById(R.id.tv_username);
        }
    }
}
