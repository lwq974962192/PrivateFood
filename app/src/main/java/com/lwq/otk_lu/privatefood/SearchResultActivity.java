/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDialog;
import com.lwq.otk_lu.privatefood.adapter.SearchResultAdapter;
import com.lwq.otk_lu.privatefood.model.SearchResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import okhttp3.Call;
import okhttp3.Request;

public class SearchResultActivity extends AppCompatActivity {
    private TextView editText;
    private Button button;
    private ListView listView;
    private List<SearchResult> list = new ArrayList<>();
    private AlertDialog.Builder builder = null;
    private SearchResultAdapter adapter;
    private PtrFrameLayout frame;
    private PtrFrameLayout ptr;
    private String server = "https://hbe.ink:8443/PrivateFood/api?";
    private LoadingDialog.Builder loadBuilder = null;
    private LoadingDialog dialog = null;
    private String result;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        editText = findViewById(R.id.result_search_view);
        button = findViewById(R.id.result_search_cancel_button);
        listView = findViewById(R.id.result_list_view);
        ptr = findViewById(R.id.ptr_result);
        builder = new AlertDialog.Builder(this);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                finish();
                return false;
            }
        });

        Intent intent = getIntent();
        final String c = intent.getStringExtra("factor");


        editText.setText(c);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(this);
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);

        ptr.setHeaderView(header);
        ptr.addPtrUIHandler(header);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !canChildScrollUp(listView);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getResult(c);
                getFrame(frame);
            }
        });
        getResult(c);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!result.equals("[]")){
                    Intent intent1=new Intent(SearchResultActivity.this,DetailActivity.class);
                    intent1.putExtra("factor",c);
                    intent1.putExtra("from",1);
                    startActivity(intent1);
                }

            }
        });

    }

    public void getFrame(PtrFrameLayout frame) {
        this.frame = frame;
    }

    private void setDialog(String title) {
        builder.setTitle(title);
        builder.setMessage("网络不佳，请下拉刷新");
        builder.setPositiveButton("OK", null);
    }

    public void getResult(String factor) {
        list.clear();
        OkHttpUtils.get().url(server)
                .addParams("flag", "searchbyname")
                .addParams("search_name", factor)
                .build().execute(new StringCallback() {
            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                loadBuilder = new LoadingDialog
                        .Builder(SearchResultActivity.this)
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
                if (list != null) {
                    adapter = new SearchResultAdapter(list, SearchResultActivity.this);
                    listView.setAdapter(adapter);
                    if (frame != null) {
                        frame.refreshComplete();
                    }
                }
            }

            private JSONObject object;

            @Override
            public void onError(Call call, Exception e, int id) {
                setDialog("获取结果失败");
                dialog.dismiss();
                if (frame != null) {
                    frame.refreshComplete();
                }
                builder.show();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    object = new JSONObject(response);
                    result = object.getString("data");
                    if (result.equals("[]")) {
                        Toast.makeText(SearchResultActivity.this, "没有结果", Toast.LENGTH_SHORT).show();
                    } else {
                        result = result.substring(2, result.length() - 2);
                        //Log.d("lotk",response);
                        //Log.d("lotk",result);
                        list.add(new SearchResult(result, "content here", "https://s1.st.meishij.net/r/61/235/12871311/s12871311_153412269928741.jpg", 123456));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
