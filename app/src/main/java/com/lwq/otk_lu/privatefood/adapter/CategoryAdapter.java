/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lwq.otk_lu.privatefood.fragment.CategoryMainFragment;
import com.lwq.otk_lu.privatefood.fragment.CategoryMaterialFragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends FragmentPagerAdapter {
    private List<Fragment> list = new ArrayList<>();

    public CategoryAdapter(FragmentManager fm) {
        super(fm);
        list.add(new CategoryMainFragment());
        list.add(new CategoryMaterialFragment());
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
