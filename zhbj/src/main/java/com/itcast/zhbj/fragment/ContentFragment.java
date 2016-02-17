package com.itcast.zhbj.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.itcast.zhbj.R;
import com.itcast.zhbj.view.NoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {


    private NoScrollViewPager mPager;
    private RadioGroup mTabs;

    public ContentFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化view
        mPager = (NoScrollViewPager) view.findViewById(R.id.content_pager);
        mTabs = (RadioGroup) view.findViewById(R.id.content_tabs);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPager.setAdapter(new ContentPagerAdapter());
    }

    private class ContentPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
