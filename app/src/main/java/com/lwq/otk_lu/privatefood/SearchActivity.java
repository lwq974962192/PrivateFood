/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SearchActivity extends AppCompatActivity {
    private int flag=0;
    private Button cancelButton;
    private Button clearButton;
    private EditText editText;
    private ListView listView;
    private GridView gridView;
    private ArrayAdapter<String> adapter1, adapter2;
    private String item1[] = { "慕斯", "焖饭", "土豆鲜虾", "咸鸭蛋", "炒饭", "曲奇", "蛋炒饭", "油饼", "豆腐脑"};
    private List<String> list = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cancelButton = findViewById(R.id.search_cancel_button);
        editText = findViewById(R.id.search_view);
        gridView = findViewById(R.id.search_gridview);
        listView = findViewById(R.id.search_history_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("factor", list.get(i));
                startActivity(intent);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("factor",item1[i]);
                checkHistory(item1[i]);
                //list.add(item1[i]);
                adapter2.notifyDataSetChanged();
                saveList();
                startActivity(intent);
            }
        });

        clearButton = findViewById(R.id.search_clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                adapter2.clear();
                adapter2.notifyDataSetChanged();
                SharedPreferences preferences = getSharedPreferences("history", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Drawable drawable = editText.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (motionEvent.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (motionEvent.getX() > editText.getWidth()
                        - editText.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    editText.setText("");
                }
                return false;
            }
        });
        readList();
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item1);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (editText.getText().toString().length() != 0 && (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (keyEvent.getAction()) {
                        case KeyEvent.ACTION_UP:
                            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                            intent.putExtra("factor", editText.getText().toString());
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            assert imm != null;
                            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                            checkHistory(editText.getText().toString());
                            saveList();
                            startActivity(intent);

                            return true;
                        default:
                            return true;
                    }
                }
                return false;
            }
        });
        gridView.setAdapter(adapter1);
        listView.setAdapter(adapter2);

    }

    public void saveList() {
        SharedPreferences.Editor editor = getSharedPreferences("history", MODE_PRIVATE).edit();
        editor.putInt("history", list.size());
        for (int i = 0; i < list.size(); i++) {
            editor.putString("item_" + i, list.get(i));
        }
        editor.apply();
    }

    public void readList() {
        SharedPreferences sharedPreferences = getSharedPreferences("history", MODE_PRIVATE);
        int listNum = sharedPreferences.getInt("history", 0);
        for (int i = 0; i < listNum; i++) {
            String listItem = sharedPreferences.getString("item_" + i, null);
            list.add(listItem);
        }
    }

    private void checkHistory(String s) {
        SharedPreferences sharedPreferences = getSharedPreferences("history", MODE_PRIVATE);
        Map<String,?> history= sharedPreferences.getAll();
        Set<? extends Map.Entry<String, ?>> set=history.entrySet();
        for (Map.Entry<String,?> map:set){
            if (s.equals(map.getValue()))
                flag=1;
        }
        if (flag==0)
        {
            list.add(s);
            adapter2.notifyDataSetChanged();
        }


    }
}

