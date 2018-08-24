/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lwq.otk_lu.privatefood.MainActivity;
import com.lwq.otk_lu.privatefood.R;
import com.lwq.otk_lu.privatefood.model.Boutique;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class BoutiqueAdapter extends BaseAdapter {
    private Context context;
    private List<Boutique> list;

    public BoutiqueAdapter() {
    }

    public BoutiqueAdapter(Context context, List<Boutique> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_boutique, null, false);
            holder = new ViewHolder(view);
            //holder.picView = view.findViewById(R.id.boutique_pic);
            //holder.titleView = view.findViewById(R.id.boutique_title);
            //holder.nameView = view.findViewById(R.id.boutique_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.titleView.setText(list.get(i).getName());
        holder.nameView.setText(list.get(i).getCate());
        Glide.with(context).load(list.get(i).getImgurl()).into(holder.picView);
        return view;
    }

    class ViewHolder {
        @BindView(R.id.boutique_pic)
        ImageView picView;
        @BindView(R.id.boutique_title)
        TextView titleView;
        @BindView(R.id.boutique_name)
        TextView nameView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
