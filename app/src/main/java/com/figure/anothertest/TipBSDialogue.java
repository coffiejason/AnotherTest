package com.figure.anothertest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TipBSDialogue extends BottomSheetDialogFragment {
    private BottomSheetListner mListner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tip_bottomsheet_layout,container,false);
        RelativeLayout acceptbtn = v.findViewById(R.id.accept_btn);
        RelativeLayout declinebtn = v.findViewById(R.id.decline_btn);

        acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.buttonClicked(true);
                dismiss();
            }
        });

        declinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.buttonClicked(false);
                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListner{
        void buttonClicked(Boolean choice);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListner = (BottomSheetListner)context;
        }catch (Exception e){
            throw new ClassCastException(context.toString() + "bottom sheet Listner");
        }

    }
}
