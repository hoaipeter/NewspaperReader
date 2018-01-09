package com.example.peterhoai.newspaperreader;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    ImageButton btnVNEx, btnVNNet, btnNld, btnDtri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDtri = (ImageButton) findViewById(R.id.btnDTri);
        btnVNEx = (ImageButton) findViewById(R.id.btnVNExprress);
        btnNld = (ImageButton) findViewById(R.id.btnNLD);
        btnVNNet = (ImageButton) findViewById(R.id.btnVNNet);

        btnVNEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] URLList = getResources().getStringArray(R.array.rss_url_list_VN);
                String[] Topic = getResources().getStringArray(R.array.rss_list_Topic);
                Intent intent = new Intent(MainActivity.this, RssActivity.class);
                intent.putExtra("topic", new ArrayList<String>(Arrays.asList(Topic)));
                intent.putExtra("url", new ArrayList<String>(Arrays.asList(URLList)));
                startActivity(intent);
            }
        });

        btnDtri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] URLList = getResources().getStringArray(R.array.rss_url_list_DT);
                String[] Topic = getResources().getStringArray(R.array.rss_list_Topic);
                Intent intent = new Intent(MainActivity.this, RssActivity.class);
                intent.putExtra("topic", new ArrayList<String>(Arrays.asList(Topic)));
                intent.putExtra("url", new ArrayList<String>(Arrays.asList(URLList)));
                startActivity(intent);
            }
        });

        btnVNNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] URLList = getResources().getStringArray(R.array.rss_url_list_VNet);
                String[] Topic = getResources().getStringArray(R.array.rss_list_Topic);
                Intent intent = new Intent(MainActivity.this, RssActivity.class);
                intent.putExtra("topic", new ArrayList<String>(Arrays.asList(Topic)));
                intent.putExtra("url", new ArrayList<String>(Arrays.asList(URLList)));
                startActivity(intent);
            }
        });

        btnNld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] URLList = getResources().getStringArray(R.array.rss_url_list_NLD);
                String[] Topic = getResources().getStringArray(R.array.rss_list_Topic);
                Intent intent = new Intent(MainActivity.this, RssActivity.class);
                intent.putExtra("topic", new ArrayList<String>(Arrays.asList(Topic)));
                intent.putExtra("url", new ArrayList<String>(Arrays.asList(URLList)));
                startActivity(intent);
            }
        });
    }
}
