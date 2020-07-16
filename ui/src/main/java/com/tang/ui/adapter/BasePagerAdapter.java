/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tang.ui.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class BasePagerAdapter extends FragmentStatePagerAdapter {

    protected int[] resId;
    protected String[] titles;
    protected Fragment[] fragments;

    public BasePagerAdapter(FragmentManager fm, String[] titles, Fragment[] fragments){
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    public BasePagerAdapter(FragmentManager fm, int[] resId, String[] titles, Fragment[] fragments){
        this(fm,titles,fragments);
        this.resId = resId;
    }

    @Override
    public int getCount() {
        return fragments!=null?fragments.length:0;
    }

    @Override
    public Fragment getItem(int i) {
        return (fragments!=null&&fragments.length!=0)?fragments[i]:null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public int getImageResource(int position) {
        return resId[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
