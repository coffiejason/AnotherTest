package com.figure.anothertest.bottomvavigation;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.figure.anothertest.R;

import java.util.ArrayList;

public class BottomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<BottomItem> bottomItems;
    private int itemWidth;
    private int selected;
    private String selectedColor;
    private String unselectedColor;
    private BottomItemClickInterface bottomItemClickInterface;

    public BottomAdapter(int selected, ArrayList<BottomItem> bottomItems, int itemWidth, String selectedColor, String unselectedColor, BottomItemClickInterface bottomItemClickInterface) {
        this.bottomItems = bottomItems;
        this.itemWidth = itemWidth;
        this.selected = selected;
        this.selectedColor = selectedColor;
        this.unselectedColor = unselectedColor;
        this.bottomItemClickInterface = bottomItemClickInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_navigation_item, parent, false);
        return new BottomNavigationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        resizeItemWidth(((BottomNavigationViewHolder) holder).bottom_parent);
        setTitle(((BottomNavigationViewHolder) holder).item_title, bottomItems.get(position).getItemName());
        selectedStyle(((BottomNavigationViewHolder) holder).bottom_icon, ((BottomNavigationViewHolder) holder).item_title, bottomItems.get(position).getItemId());
        setIcon(((BottomNavigationViewHolder) holder).bottom_icon, bottomItems.get(position).getItemIconId());
        notificationBadgeVisibility(((BottomNavigationViewHolder) holder).notification_badge, bottomItems.get(position).isHasNotification(), position);

        setOnClickItem(
                ((BottomNavigationViewHolder) holder).bottom_item_parent,
                ((BottomNavigationViewHolder) holder).bottom_icon,
                ((BottomNavigationViewHolder) holder).item_title,
                bottomItems.get(position).getItemId(),
                position);
    }

    @Override
    public int getItemCount() {
        return bottomItems.size();
    }

    private void notificationBadgeVisibility(CardView cardView, boolean hasNotification, final int position) {
        if (hasNotification) {
            if(selected != position){
                cardView.setVisibility(View.VISIBLE);
            }
        } else {
            cardView.setVisibility(View.GONE);
        }
    }

    private void resizeItemWidth(RelativeLayout parent) {
        parent.getLayoutParams().width = itemWidth;
    }

    private void setIcon(ImageView imageView, int iconId) {
        imageView.setImageResource(iconId);
    }

    private void selectedStyle(ImageView imageView, TextView textView, int itemId) {
        if (itemId == selected) {
            imageView.setColorFilter(Color.parseColor(selectedColor), android.graphics.PorterDuff.Mode.SRC_IN);
            textView.setTextColor(Color.parseColor(selectedColor));
        } else {
            imageView.setColorFilter(Color.parseColor(unselectedColor), android.graphics.PorterDuff.Mode.SRC_IN);
            textView.setTextColor(Color.parseColor(unselectedColor));
        }
    }

    private void setOnClickItem(RelativeLayout parent, final ImageView imageView, final TextView textView, final int itemId, final int position) {
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomItemClickInterface.itemSelect(itemId);
                selected = itemId;
                bottomItems.get(position).setHasNotification(false);
                selectedStyle(imageView, textView, itemId);
                notifyDataSetChanged();
            }
        });
    }

    private void setTitle(TextView textView, String text) {
        textView.setText(text);
    }

    public interface BottomItemClickInterface {
        void itemSelect(int itemId);
    }
}
