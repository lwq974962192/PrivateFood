/*
 * Copyright @ 鹿文权
 */

package com.lwq.otk_lu.privatefood;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lwq.otk_lu.privatefood.fragment.CategoryFragment;
import com.lwq.otk_lu.privatefood.fragment.HomeFragment;
import com.lwq.otk_lu.privatefood.fragment.MoreFragment;

public class MainActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private LinearLayout content;
    private FragmentManager manager;
    private HomeFragment homeFragment;
    private MoreFragment moreFragment;
    private CategoryFragment categoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        content = findViewById(R.id.main_content);
        radioGroup = findViewById(R.id.main_radio_group);
        manager = getSupportFragmentManager();
        radioGroupClick();
        initHome();
    }

    public void gotoCategory() {
        radioGroup.check(R.id.radioCategoryButton);
    }

    private void initHome() {
        ((RadioButton) (radioGroup.getChildAt(0))).setChecked(true);
    }

    private void radioGroupClick() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FragmentTransaction transaction = manager.beginTransaction();
                hideAll(transaction);
                switch (i) {
                    case R.id.radioHomeButton:
                        if (homeFragment == null) {
                            homeFragment = new HomeFragment();
                            transaction.add(R.id.main_content, homeFragment);
                        }
                        transaction.show(homeFragment);
                        break;
                    case R.id.radioCategoryButton:
                        if (categoryFragment == null) {
                            categoryFragment = new CategoryFragment();
                            transaction.add(R.id.main_content, categoryFragment);
                        }
                        transaction.show(categoryFragment);
                        break;
                    case R.id.radioMoreButton:
                        if (moreFragment == null) {
                            moreFragment = new MoreFragment();
                            transaction.add(R.id.main_content, moreFragment);
                        }
                        transaction.show(moreFragment);
                        break;
                }
                transaction.commit();
            }
        });
    }

    private void hideAll(FragmentTransaction transaction) {
        if (homeFragment != null)
            transaction.hide(homeFragment);
        if (moreFragment != null)
            transaction.hide(moreFragment);
        if (categoryFragment != null) {
            transaction.hide(categoryFragment);
        }
    }
}
