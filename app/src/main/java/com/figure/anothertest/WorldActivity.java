package com.figure.anothertest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.figure.anothertest.bottomvavigation.BadgeBottomNavigtion;
import com.figure.anothertest.bottomvavigation.BottomAdapter;
import com.figure.anothertest.bottomvavigation.BottomItem;

import java.util.List;

public class WorldActivity extends AppCompatActivity implements BottomAdapter.BottomItemClickInterface{

    //bottom nav stuff, might delete
    private final int HOME = 0;
    private final int NOTIFICATIONS = 1;
    private final int FRIENDS = 2;
    private final int SETTINGS = 3;
    private final int CART = 4;
    private int selectedId = 0;

    private BadgeBottomNavigtion badgeBottomNavigtion;

    RelativeLayout mapBtn;

    List<TPPost> list;
    TPPost post;
    String[] postMsgs;
    String[] userIDs;
    String[] postIDs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);

        //list = new Functions().getWorldPosts();

        badgeBottomNavigtion = new BadgeBottomNavigtion(findViewById(R.id.BottomNavigation), WorldActivity.this, WorldActivity.this);
        initBottomItems();

        mapBtn = findViewById(R.id.mapbtn);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Log.d("allpostsinlist",""+ErrandMapActivity.allposts.size());
        RecyclerView rv = findViewById(R.id.world_recyclerview);
        WorldAdapter adapter = new WorldAdapter(WorldActivity.this,ErrandMapActivity.allposts);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

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
        Toast.makeText(this," "+itemId,Toast.LENGTH_SHORT).show();
        switch (itemId){
            case 0:
                break;

            case 1:
                Intent i1 = new Intent(WorldActivity.this,Tper1Activity.class);
                startActivity(i1);
                break;

            case 3:
                Intent i = new Intent(WorldActivity.this,SettingsActivity.class);
                startActivity(i);
                break;
        }
    }
}
