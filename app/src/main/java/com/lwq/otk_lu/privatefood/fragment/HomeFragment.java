/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lwq.otk_lu.privatefood.LoginActivity;
import com.lwq.otk_lu.privatefood.MainActivity;
import com.lwq.otk_lu.privatefood.R;
import com.lwq.otk_lu.privatefood.SearchActivity;
import com.lwq.otk_lu.privatefood.SearchResultActivity;
import com.lwq.otk_lu.privatefood.adapter.BoutiqueAdapter;
import com.lwq.otk_lu.privatefood.model.Boutique;
import com.lwq.otk_lu.privatefood.util.MyListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.loader.ImageLoaderInterface;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import okhttp3.Call;
import okhttp3.Request;

public class HomeFragment extends Fragment {
    private Unbinder unbinder;
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private LoadingDialog.Builder loadBuilder = null;
    private LoadingDialog dialog = null;
    private TextView textView;
    private MyListView boutiqueList;
    private List<Boutique> list = new ArrayList<>();
    private ArrayAdapter<String> adapter = null;
    private GridView gridView;
    private ImageView upView;
    private ScrollView scrollView;
    private PtrFrameLayout frame;
    private PtrFrameLayout ptr;
    private String item[] = {"家常菜", "川菜", "早餐", "素菜", "煲汤", "下饭菜", "烘焙", "更多"};
    private String server = "https://hbe.ink:8443/PrivateFood/api?";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null, false);
        unbinder = ButterKnife.bind(this, view);
        textView = view.findViewById(R.id.main_search_view);
        gridView = view.findViewById(R.id.main_gridview);
        banner = view.findViewById(R.id.banner);
        upView = view.findViewById(R.id.up_button);
        scrollView = view.findViewById(R.id.myScrollView);
        ptr = view.findViewById(R.id.ptr_home);
        upView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        boutiqueList = view.findViewById(R.id.main_boutique_list);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        final PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(getActivity());
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);

        ptr.setHeaderView(header);
        ptr.addPtrUIHandler(header);
        ptr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !canChildScrollUp(scrollView);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getBoutique();
                getRecyclerPic();
                getFrame(frame);
            }
        });
        setGridView();
        getRecyclerPic();
        getBoutique();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getFrame(PtrFrameLayout frame) {
        this.frame = frame;
    }

    private void setGridView() {
        adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, item);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {

                    case 7:
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.gotoCategory();
                        break;
                    default:
                        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                        intent.putExtra("factor", item[i]);
                        intent.putExtra("from",1);
                        startActivity(intent);
                }
            }
        });
    }

    private void getRecyclerPic() {
        list_path = new ArrayList<String>();
        list_title = new ArrayList<String>();
        list_path.add("https://s1.st.meishij.net/r/50/123/6030800/s6030800_153377626298744.jpg");
        list_path.add("https://s1.st.meishij.net/r/61/235/12871311/s12871311_153412269928741.jpg");
        list_path.add("https://s1.st.meishij.net/r/112/158/2414612/s2414612_153239817994951.jpg");
        list_path.add("https://s1.st.meishij.net/r/19/99/9087269/s9087269_153250573292271.jpg");
        list_title.add("1");
        list_title.add("2");
        list_title.add("3");
        list_title.add("4");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.LEFT)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Log.d("lotk", " you clicked " + position + " th");
                    }
                })
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    private void getBoutique() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        OkHttpUtils.get()
                .url(server)
                .addParams("flag", "getfoodmemus")
                .build()
                .connTimeOut(1000)
                .readTimeOut(1000)
                .writeTimeOut(1000)
                .execute(new StringCallback() {
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
                        if (list.size() > 0) {
                            BoutiqueAdapter adapter = new BoutiqueAdapter(getActivity(), list);
                            boutiqueList.setAdapter(adapter);
                            if (frame != null) {
                                frame.refreshComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        builder.setTitle("获取资源失败");
                        builder.setMessage("网络不佳，请下拉刷新");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                        if (frame != null) {
                            frame.refreshComplete();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("data");
                            JSONObject tmpObject;
                            for (int k = 0; k < array.length(); k++) {
                                tmpObject = array.getJSONObject(k);
                                list.add(new Gson().fromJson(tmpObject.toString(), Boutique.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load((String) path).into(imageView);

        }
    }
}
