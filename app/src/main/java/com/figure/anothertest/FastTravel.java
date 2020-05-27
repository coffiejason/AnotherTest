package com.figure.anothertest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

public class FastTravel extends AutoCompleteTextView {

    public FastTravel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener l) {
        super.setOnItemClickListener(l);
        Log.d("azxcvbnjhg","true");
    }
}
