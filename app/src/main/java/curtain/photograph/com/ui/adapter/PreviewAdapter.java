/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package curtain.photograph.com.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import curtain.photograph.com.ui.fragment.PreviewPicFragment;

public class PreviewAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mItems = new ArrayList<>();
    private OnPrimaryItemSetListener mListener;

    public PreviewAdapter(FragmentManager manager, ArrayList<String> mItems) {
        super(manager);
        this.mItems = mItems;
    }

    @Override
    public Fragment getItem(int position) {

        return PreviewPicFragment.newInstance(mItems.get(position));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    public String getMediaItem(int position) {
        return mItems.get(position);
    }

    public void addAll(List<String> mPaths) {
        mItems.addAll(mPaths);
    }

    interface OnPrimaryItemSetListener {

        void onPrimaryItemSet(int position);
    }

}
