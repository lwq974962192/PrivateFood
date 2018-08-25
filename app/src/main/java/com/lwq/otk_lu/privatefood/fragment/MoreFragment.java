/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lwq.otk_lu.privatefood.AboutActivity;
import com.lwq.otk_lu.privatefood.HistoryActivity;
import com.lwq.otk_lu.privatefood.LoginActivity;
import com.lwq.otk_lu.privatefood.R;
import com.lwq.otk_lu.privatefood.adapter.MoreAdapter;
import com.lwq.otk_lu.privatefood.model.MoreSetting;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends Fragment {
    private ListView listView1, listView2;
    private List<MoreSetting> list1 = new ArrayList<>(), list2 = new ArrayList<>();
    private Button quitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_more, null, false);
        listView1 = view.findViewById(R.id.more_list_view_1);
        listView2 = view.findViewById(R.id.more_list_view_2);
        quitButton = view.findViewById(R.id.more_quit_button);
        list1.add(new MoreSetting(R.drawable.icon_09, "我的收藏"));
        list1.add(new MoreSetting(R.drawable.icon_11, "浏览记录"));
        list2.add(new MoreSetting(R.drawable.icon_13, "陛下，赏个好评吧"));
        list2.add(new MoreSetting(R.drawable.icon_15, "推荐给好友"));
        list2.add(new MoreSetting(R.drawable.icon_17, "联系我们"));
        list2.add(new MoreSetting(R.drawable.about, "关于"));
        MoreAdapter adapter1 = new MoreAdapter(list1, getContext());
        MoreAdapter adapter2 = new MoreAdapter(list2, getContext());
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor1.putBoolean("isLogin", false);
                editor1.apply();
                SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("history", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                editor2.clear();
                editor2.apply();
                SharedPreferences sharedPreferences3 = getActivity().getSharedPreferences("browsing_history", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor3 = sharedPreferences3.edit();
                editor3.clear();
                editor3.apply();
                SharedPreferences sharedPreferences4 = getActivity().getSharedPreferences("liked", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor4 = sharedPreferences4.edit();
                editor4.clear();
                editor4.apply();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), HistoryActivity.class);
                switch (i){
                    case 0:
                        intent.putExtra("flag",0);
                        break;
                    case 1:
                        intent.putExtra("flag",1);
                        break;
                }
                startActivity(intent);
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        String packageName = getActivity().getPackageName();
                        String str = "market://details?id=" + packageName;
                        Intent intent1 = new Intent(Intent.ACTION_VIEW);
                        intent1.setData(Uri.parse(str));
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Intent.ACTION_SEND);
                        intent2.setType("text/plain");
                        intent2.putExtra(Intent.EXTRA_TEXT, "私房菜");
                        startActivity(Intent.createChooser(intent2, "分享"));
                        break;
                    case 2:
                        Uri uri1 = Uri.parse("tel:10086");
                        Intent intent3 = new Intent(Intent.ACTION_DIAL, uri1);
                        startActivity(intent3);
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), AboutActivity.class));
                        break;
                }
            }
        });
        return view;
    }
}
