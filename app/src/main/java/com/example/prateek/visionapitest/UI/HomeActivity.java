package com.example.prateek.visionapitest.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prateek.visionapitest.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView bChooseGallery;
    LinearLayout llJoy, llAnger, llSorrow, llSurprise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bChooseGallery = (TextView) findViewById(R.id.bChooseGallery);
        llJoy = (LinearLayout) findViewById(R.id.llJoy);
        llAnger = (LinearLayout) findViewById(R.id.llAnger);
        llSorrow = (LinearLayout) findViewById(R.id.llSorrow);
        llSurprise = (LinearLayout) findViewById(R.id.llSurprise);


        bChooseGallery.setOnClickListener(this);
        llJoy.setOnClickListener(this);
        llAnger.setOnClickListener(this);
        llSorrow.setOnClickListener(this);
        llSurprise.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bChooseGallery:
                Intent mainIntent2 = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(mainIntent2);
                break;

            case R.id.llJoy:
                Intent t1 = new Intent(HomeActivity.this, TabData.class);
                t1.putExtra("mood", "joy");
                startActivity(t1);
                break;

            case R.id.llAnger:
                Intent t2 = new Intent(HomeActivity.this, TabData.class);
                t2.putExtra("mood", "anger");
                startActivity(t2);
                break;

            case R.id.llSorrow:
                Intent t3 = new Intent(HomeActivity.this, TabData.class);
                t3.putExtra("mood", "sorrow");
                startActivity(t3);
                break;

            case R.id.llSurprise:
                Intent t4 = new Intent(HomeActivity.this, TabData.class);
                t4.putExtra("mood", "surprise");
                startActivity(t4);
                break;
        }
    }
}
