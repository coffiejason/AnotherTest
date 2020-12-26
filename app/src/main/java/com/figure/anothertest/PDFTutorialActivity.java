package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class PDFTutorialActivity extends AppCompatActivity {

    RelativeLayout pdf,begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_tutorial);

        pdf = findViewById(R.id.recieved_img);
        begin = findViewById(R.id.begin);

        pdf.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://elpregonero.info/PARTITURA/PDF/619%20-%20the%20beatles%20%20-%20something.pdf"));
            startActivity(browserIntent);
        });

        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PDFTutorialActivity.this,QuizActivity.class));
            }
        });
    }
}
