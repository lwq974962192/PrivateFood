/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.tu.loadingdialog.LoadingDialog;
import com.lwq.otk_lu.privatefood.R;
import com.lwq.otk_lu.privatefood.SearchActivity;
import com.lwq.otk_lu.privatefood.SearchResultActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import okhttp3.Call;
import okhttp3.Request;

public class CategoryMainFragment extends Fragment {
    private String server = "https://hbe.ink:8443/PrivateFood/api?";
    private GridView gridView;
    private ListView listView;
    private String item1[], item2[];
    private ArrayAdapter<String> adapter, adapter2;
    private AlertDialog.Builder builder = null;
    private PtrFrameLayout frame;
    private PtrFrameLayout ptr;
    private LoadingDialog.Builder loadBuilder = null;
    private LoadingDialog dialog = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_category_detail, null, false);
        listView = view.findViewById(R.id.category_detail_sort);
        gridView = view.findViewById(R.id.category_detail_content);
        ptr = view.findViewById(R.id.ptr_category);
        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);

        ptr.setHeaderView(header);
        ptr.addPtrUIHandler(header);

        builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        getAllCategoryType();
        listListener();
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !canChildScrollUp(listView);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                item2 = new String[0];
                adapter2 = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, item2);
                gridView.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
                getAllCategoryType();
                getFrame(frame);


            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("factor",item2[i]);
                startActivity(intent);
            }
        });
        return view;
    }

    public void getFrame(PtrFrameLayout frame) {
        this.frame = frame;
    }

    private void setDialog(String title) {
        builder.setTitle(title);
        builder.setMessage("网络不佳，请下拉刷新");
        builder.setPositiveButton("OK", null);
    }

    private void listListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clearAllChild();
                listView.getChildAt(i-listView.getFirstVisiblePosition()).setBackgroundColor(getResources().getColor(R.color.colorListPress));
                getCategoryByType(i);
            }

            private void clearAllChild() {
                for (int j = 0; j < listView.getChildCount(); j++) {
                    listView.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.colorWhiteGray));
                }
            }
        });
    }

    private void getCategoryByType(int i) {
        OkHttpUtils.get().url(server)
                .addParams("flag", "getcategorybytype")
                .addParams("cate_type", item1[i])
                .build()
                .connTimeOut(1000)
                .readTimeOut(1000)
                .writeTimeOut(1000)
                .execute(new StringCallback() {
                    private JSONArray array;
                    private JSONObject object;

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        loadBuilder = new LoadingDialog
                                .Builder(getActivity())
                                .setCancelable(false)
                                .setCancelOutside(false)
                                .setMessage("加载中");
                        dialog = loadBuilder.create();
                        dialog.show();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        dialog.dismiss();
                        adapter2 = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, item2);
                        gridView.setAdapter(adapter2);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        setDialog("获取类型失败");
                        dialog.dismiss();
                        builder.show();
                        // Toast.makeText(getActivity(), "get category by type failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            object = new JSONObject(response);
                            array = object.getJSONArray("data");
                            item2 = new String[array.length()];
                            for (int i = 0; i < array.length(); i++) {
                                item2[i] = array.get(i).toString();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getAllCategoryType() {
        OkHttpUtils.get().url(server).addParams("flag", "getallcategorytype").build().execute(new StringCallback() {
            private JSONArray array;
            private JSONObject object;

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                loadBuilder = new LoadingDialog
                        .Builder(getActivity())
                        .setCancelable(false)
                        .setCancelOutside(false)
                        .setMessage("加载中");
                dialog = loadBuilder.create();
                dialog.show();
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                dialog.dismiss();
                if (item1 != null) {
                    adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, item1);
                    listView.setAdapter(adapter);
                    if (frame != null) {
                        frame.refreshComplete();
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                setDialog("获取分类失败");
                dialog.dismiss();
                if (frame != null) {
                    frame.refreshComplete();
                }
                builder.show();
                // Toast.makeText(getActivity(), "get all category by type failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    object = new JSONObject(response);
                    array = object.getJSONArray("data");
                    item1 = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        item1[i] = array.get(i).toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
