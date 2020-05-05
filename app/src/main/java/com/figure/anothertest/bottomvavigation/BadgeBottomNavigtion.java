package com.figure.anothertest.bottomvavigation;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.figure.anothertest.R;

import java.util.ArrayList;

public class BadgeBottomNavigtion {
    private final int ITEM_LIMIT = 5;
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<BottomItem> bottomItems;
    private BottomAdapter.BottomItemClickInterface bottomItemClickInterface;

    public BadgeBottomNavigtion(View view, Context context, BottomAdapter.BottomItemClickInterface bottomItemClickInterface) {
        this.context = context;
        this.bottomItemClickInterface = bottomItemClickInterface;
        setType(view);
    }

    private void setType(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        bottomItems = new ArrayList<>();
    }

    public void addBottomItem(BottomItem item) {
        if (bottomItems.size() != ITEM_LIMIT) {
            bottomItems.add(item);
        }
    }

    private int calculateItemWidth() {
        int mCount = bottomItems.size() + 1;
        int mWidth = context.getResources().getDisplayMetrics().widthPixels;
        return mWidth / mCount;
    }

    private void setAdapter(int selected, String selectedColor, String unselectedColor) {
        BottomAdapter bottomAdapter = new BottomAdapter(selected, bottomItems, calculateItemWidth(), selectedColor, unselectedColor, bottomItemClickInterface);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(bottomAdapter);
    }

    public void apply(int selected, String selectedColor, String unselectedColor) {
        setAdapter(selected, selectedColor, unselectedColor);
    }
}