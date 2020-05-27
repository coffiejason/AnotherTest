package com.figure.anothertest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecievedAdapter extends RecyclerView.Adapter<RecievedAdapter.RViewHolder>{
    private Context context;

    RecievedAdapter(Context context){
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class RViewHolder extends RecyclerView.ViewHolder{
        public RViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
