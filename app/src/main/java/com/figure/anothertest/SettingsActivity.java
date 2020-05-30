package com.figure.anothertest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.figure.anothertest.bottomvavigation.BadgeBottomNavigtion;
import com.figure.anothertest.bottomvavigation.BottomAdapter;
import com.figure.anothertest.bottomvavigation.BottomItem;


public class SettingsActivity extends AppCompatActivity implements BottomAdapter.BottomItemClickInterface {

    private final int HOME = 0;
    private final int NOTIFICATIONS = 1;
    private final int FRIENDS = 2;
    private final int SETTINGS = 3;
    private final int CART = 4;
    private int selectedId = 3;

    private BadgeBottomNavigtion badgeBottomNavigtion;

    private LinearLayout profile,myposts,notification,media,help,invites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();

        badgeBottomNavigtion = new BadgeBottomNavigtion(findViewById(R.id.SBottomNavigation), SettingsActivity.this, SettingsActivity.this);
        initBottomItems();

    }


    @SuppressLint("ResourceType")
    private void initBottomItems() {
        BottomItem home = new BottomItem(HOME, R.drawable.ic_home, "Home", false);
        BottomItem notifications = new BottomItem(NOTIFICATIONS, R.drawable.ic_plus, "Post", false);
        //BottomItem friends = new BottomItem(FRIENDS, R.drawable.ic_people, "Friends", false);
        //BottomItem cart = new BottomItem(CART, R.drawable.ic_cart, "Cart", true);
        BottomItem settings = new BottomItem(SETTINGS, R.drawable.ic_settings, "Settings", false);

        badgeBottomNavigtion.addBottomItem(home);
        badgeBottomNavigtion.addBottomItem(notifications);
        //badgeBottomNavigtion.addBottomItem(friends);
        //badgeBottomNavigtion.addBottomItem(cart);
        badgeBottomNavigtion.addBottomItem(settings);

        badgeBottomNavigtion.apply(selectedId, getString(R.color.colorAccent), getString(R.color.tipeeGreenDark));
        itemSelect(selectedId);
    }


    @Override
    public void itemSelect(int itemId) {
        switch (itemId){
            case 0:
                /*
                Intent i = new Intent(SettingsActivity.this,ErrandMapActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);*/
                onBackPressed();
                break;

            case 1:
                Intent i1 = new Intent(SettingsActivity.this,Tper1Activity.class);
                startActivity(i1);
                break;

            case 3:
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        badgeBottomNavigtion.apply(selectedId, getString(R.color.colorAccent), getString(R.color.tipeeGreenDark));
    }

    private void init(){
        profile = findViewById(R.id.ln_profile);
        myposts = findViewById(R.id.ln_chats);
        notification = findViewById(R.id.ln_notification);
        media = findViewById(R.id.ln_data);
        help = findViewById(R.id.ln_help);
        invites = findViewById(R.id.ln_invite_friend);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,EditProfileActivity.class));
            }
        });

        myposts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,MyPosts.class));
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,NotificationOptions.class));
            }
        });
        /*
        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,));
            }
        });*/

        /*
        invites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,));
            }
        });*/

    }
}
