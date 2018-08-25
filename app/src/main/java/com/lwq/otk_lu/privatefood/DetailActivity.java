/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {
    private int isLiked = 0;
    private int position = -1;
    private String factor;
    @BindView(R.id.detail_Name_view)
    TextView nameView;
    @BindView(R.id.detail_content_view)
    TextView contentView;
    @BindView(R.id.detail_back_button)
    Button backButton;
    @BindView(R.id.detail_like_button)
    Button likeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        factor = intent.getStringExtra("factor");
        int from = intent.getIntExtra("from", 0);
        nameView.setText(factor);
        contentView.setText("这里是 " + factor + " 的详细介绍");
        if (from == 1) {
            checkHistory();
        }
        checkIsLiked();
    }

    public void backToNotify() {
        Intent intent = new Intent();
        //intent.putExtra("empty","");
        setResult(1, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                backToNotify();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void checkHistory() {
        int flag = 0;
        SharedPreferences sharedPreferences = getSharedPreferences("browsing_history", MODE_PRIVATE);
        Map<String, ?> history = sharedPreferences.getAll();
        Set<? extends Map.Entry<String, ?>> set = history.entrySet();
        for (Map.Entry<String, ?> map : set) {
            if (factor.equals(map.getValue()))
                flag = 1;
        }
        if (flag == 0)
            outToHistory();

    }

    private boolean checkLiked() {
        int flag = 0;
        SharedPreferences sharedPreferences = getSharedPreferences("liked", MODE_PRIVATE);
        Map<String, ?> history = sharedPreferences.getAll();
        Set<? extends Map.Entry<String, ?>> set = history.entrySet();
        for (Map.Entry<String, ?> map : set) {
            if (factor.equals(map.getValue()))
                flag = 1;
        }
        return flag == 0;
    }

    private void outToHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences("browsing_history", MODE_PRIVATE);
        int history_num = sharedPreferences.getInt("history_num", 0);
        SharedPreferences.Editor editor = getSharedPreferences("browsing_history", MODE_PRIVATE).edit();
        history_num += 1;
        int index = history_num - 1;
        editor.putInt("history_num", history_num);
        editor.putString("item_" + index, factor);
        editor.apply();
    }


    private void delFromLiked() {
        if (!checkLiked()) {
            SharedPreferences sharedPreferences = getSharedPreferences("liked", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int likedNum = sharedPreferences.getInt("liked_num", 0);
            for (int i = 0; i < likedNum; i++) {
                String item = sharedPreferences.getString("item_" + i, null);
                if (factor.equals(item)) {
                    position = i;
                    break;
                }
            }
            if (position == likedNum - 1) {
                if (position == 0) {
                    editor.clear();
                } else {
                    editor.remove("item_" + position);
                    editor.putInt("liked_num", position);
                }
            } else {
                for (int i = position; i < likedNum - 1; i++) {
                    editor.putString("item_" + i, sharedPreferences.getString("item_" + (i + 1), null));

                }
                editor.putInt("liked_num", likedNum - 1);
                editor.remove("item_" + (likedNum - 1));
            }
            editor.apply();
        }

    }


    private void outToLiked() {
        if (checkLiked()) {
            SharedPreferences sharedPreferences = getSharedPreferences("liked", MODE_PRIVATE);
            int liked_num = sharedPreferences.getInt("liked_num", 0);
            SharedPreferences.Editor editor = getSharedPreferences("liked", MODE_PRIVATE).edit();
            liked_num += 1;
            int index = liked_num - 1;
            editor.putInt("liked_num", liked_num);
            editor.putString("item_" + index, factor);
            editor.apply();
        }
    }

    private void checkIsLiked() {
        SharedPreferences sharedPreferences = getSharedPreferences("liked", MODE_PRIVATE);
        Map<String, ?> likes = sharedPreferences.getAll();

        Set<? extends Map.Entry<String, ?>> set = likes.entrySet();
        for (Map.Entry<String, ?> map : set) {
            if (factor.equals(map.getValue())) {
                likeButton.setActivated(true);
                isLiked = 1;
            }
        }
    }

    @OnClick(R.id.detail_back_button)
    public void back() {
        backToNotify();
        finish();
    }

    @OnClick(R.id.detail_like_button)
    public void like() {
        switch (isLiked) {
            case 0:
                likeButton.setActivated(true);
                isLiked = 1;
                outToLiked();
                break;
            case 1:
                likeButton.setActivated(false);
                isLiked = 0;
                delFromLiked();
                break;
        }
    }
}
