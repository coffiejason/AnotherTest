package com.figure.anothertest.bottomvavigation;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.figure.anothertest.R;

public class BottomNavigationViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout bottom_item_parent;
    public RelativeLayout bottom_parent;
    public ImageView bottom_icon;
    public TextView item_title;
    public CardView notification_badge;

    public BottomNavigationViewHolder(@NonNull View itemView) {
        super(itemView);
        bottom_item_parent = itemView.findViewById(R.id.bottom_item_parent);
        bottom_parent = itemView.findViewById(R.id.bottom_parent);
        bottom_icon = itemView.findViewById(R.id.bottom_icon);
        item_title = itemView.findViewById(R.id.item_title);
        notification_badge = itemView.findViewById(R.id.notification_badge);
    }
}
