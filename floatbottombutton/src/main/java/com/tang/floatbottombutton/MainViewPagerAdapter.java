package com.tang.floatbottombutton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

/**
 * Created by tanghongtu on 2020/7/13.
 */
public class MainViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> pages;

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> pages) {
        super(fragmentActivity);
        this.pages = pages;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return pages.get(position);
    }

    @Override
    public int getItemCount() {
        return pages == null ? 0 : pages.size();
    }


}
