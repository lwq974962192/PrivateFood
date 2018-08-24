/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lwq.otk_lu.privatefood.R;
import com.lwq.otk_lu.privatefood.model.MoreSetting;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreAdapter extends BaseAdapter {
    private List<MoreSetting> list;
    private Context context;

    public MoreAdapter() {
        super();
    }

    public MoreAdapter(List<MoreSetting> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public long getItemId(int i) {
        return i;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_moresetting, null, false);
            holder = new ViewHolder(view);
            //holder.imageView = view.findViewById(R.id.item_setting_pic_view);
            //holder.textView = view.findViewById(R.id.item_setting_text_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(view).load(list.get(i).getPic()).into(holder.imageView);
        holder.textView.setText(list.get(i).getTitle());
        return view;
    }

    class ViewHolder {
        @BindView(R.id.item_setting_pic_view)
        ImageView imageView;
        @BindView(R.id.item_setting_text_view)
        TextView textView;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
