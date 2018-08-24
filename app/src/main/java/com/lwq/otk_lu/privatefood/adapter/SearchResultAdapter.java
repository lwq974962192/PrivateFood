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
import com.lwq.otk_lu.privatefood.model.SearchResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultAdapter extends BaseAdapter {
    private List<SearchResult> list;
    private Context context;

    public SearchResultAdapter() {
    }

    public SearchResultAdapter(List<SearchResult> list, Context context) {
        this.list = list;
        this.context = context;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_search_result, null, false);
            holder = new ViewHolder(view);
            //holder.imageView = view.findViewById(R.id.result_pic_view);
            //holder.contentView = view.findViewById(R.id.result_content_view);
            //holder.titleView = view.findViewById(R.id.result_title_view);
            //holder.viewView = view.findViewById(R.id.result_view_view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(view).load(list.get(i).getImgurl()).into(holder.imageView);
        holder.titleView.setText(list.get(i).getTitle());
        holder.contentView.setText(list.get(i).getContent());
        holder.viewView.setText(list.get(i).getView_num());
        return view;
    }

    class ViewHolder {
        @BindView(R.id.result_pic_view)
        ImageView imageView;
        @BindView(R.id.result_title_view)
        TextView titleView;
        @BindView(R.id.result_content_view)
        TextView contentView;
        @BindView(R.id.result_view_view)
        TextView viewView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
