/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lwq.otk_lu.privatefood.R;
import com.lwq.otk_lu.privatefood.SearchActivity;
import com.lwq.otk_lu.privatefood.adapter.CategoryAdapter;

public class CategoryFragment extends Fragment {
    private RadioGroup radioGroup;
    private ViewPager viewPager;
    private RadioButton button1, button2;
    private TextView category_search_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_category, null, false);
        category_search_view = view.findViewById(R.id.category_search_view);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.search);
        drawable.setBounds(0, 0, 50, 50);
        category_search_view.setCompoundDrawables(drawable, null, null, null);
        category_search_view.setCompoundDrawablePadding(0);
        radioGroup = view.findViewById(R.id.category_radio_group);
        button1 = view.findViewById(R.id.category_category_button);
        button2 = view.findViewById(R.id.category_material_button);
        viewPager = view.findViewById(R.id.category_content);
        category_search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        CategoryAdapter adapter = new CategoryAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == 2) {
                    switch (viewPager.getCurrentItem()) {
                        case 0:
                            button1.setChecked(true);
                            break;
                        case 1:
                            button2.setChecked(true);
                            break;
                    }
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.category_category_button:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.category_material_button:

                        viewPager.setCurrentItem(1);
                        break;

                }
            }
        });
        return view;
    }
}
