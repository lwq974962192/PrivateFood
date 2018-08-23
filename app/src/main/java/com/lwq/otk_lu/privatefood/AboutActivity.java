/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AboutActivity extends AppCompatActivity {
    private String item[] = {"Glide", "ButterKnife", "Gson", "Banner", "OkHttpUtils", "LoadingDialog", "Ultra Pull To Refresh"};
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        listView = findViewById(R.id.about_listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                switch (i) {
                    case 0:
                        intent.setData(Uri.parse("http://www.github.com/bumptech/glide"));
                        break;
                    case 1:
                        intent.setData(Uri.parse("http://jakewharton.github.io/butterknife/"));
                        break;
                    case 2:
                        intent.setData(Uri.parse("https://github.com/google/gson"));
                        break;
                    case 3:
                        intent.setData(Uri.parse("https://github.com/youth5201314/banner"));
                        break;
                    case 4:
                        intent.setData(Uri.parse("https://github.com/hongyangAndroid/okhttputils"));
                        break;
                    case 5:
                        intent.setData(Uri.parse("https://github.com/gittjy/LoadingDialog"));
                        break;
                    case 6:
                        intent.setData(Uri.parse("https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh"));
                        break;
                }
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent);
            }
        });
    }
}
