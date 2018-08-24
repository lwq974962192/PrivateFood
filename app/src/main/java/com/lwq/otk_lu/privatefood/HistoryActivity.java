/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends AppCompatActivity {
    private int flag=0;
    private List<String> list=new ArrayList<>();
    private  ArrayAdapter<String> adapter;
    @BindView(R.id.history_title)
    TextView textView;
    @BindView(R.id.history_button)
    Button button;
    @BindView(R.id.history_list_view)
    ListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        flag=intent.getIntExtra("flag",0);
        switch (flag){
            case 0:
                textView.setText("收藏记录");
                break;
            case 1:
                textView.setText("浏览记录");
                break;
        }
        initList();
        initListListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        initList();
        Log.d("lotk"," onresume");
        adapter.notifyDataSetChanged();
    }

    private void initListListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                intent.putExtra("factor",list.get(i));
                intent.putExtra("from",0);
                startActivity(intent);
            }
        });
    }

    private void initList() {
        switch (flag){
            case 0:
                initLikedList();

                break;
            case 1:
                initHistoryList();
                break;
        }
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }

    private void initHistoryList() {
        SharedPreferences sharedPreferences = getSharedPreferences("browsing_history", MODE_PRIVATE);
        int listNum = sharedPreferences.getInt("history_num", 0);
        for (int i = 0; i < listNum; i++) {
            String listItem = sharedPreferences.getString("item_" + i, null);
            list.add(listItem);
        }
    }

    private void initLikedList() {
        SharedPreferences sharedPreferences = getSharedPreferences("liked", MODE_PRIVATE);
        int listNum = sharedPreferences.getInt("liked_num", 0);
        for (int i = 0; i < listNum; i++) {
            String listItem = sharedPreferences.getString("item_" + i, null);
            list.add(listItem);
        }
    }

    @OnClick(R.id.history_button)
    public void del(){
        switch (flag){
            case 0:
                delLiked();
                break;
            case 1:
                delHistory();
                break;
        }
    }

    private void delLiked() {
        SharedPreferences preferences = getSharedPreferences("liked", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        adapter.clear();
        adapter.notifyDataSetChanged();
        editor.clear();
        editor.apply();
    }

    private void delHistory() {
        SharedPreferences preferences = getSharedPreferences("browsing_history", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        adapter.clear();
        adapter.notifyDataSetChanged();
        editor.clear();
        editor.apply();
    }
}
