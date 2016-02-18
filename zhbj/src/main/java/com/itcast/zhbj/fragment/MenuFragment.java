package com.itcast.zhbj.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itcast.zhbj.R;
import com.itcast.zhbj.activity.MainUI;
import com.itcast.zhbj.bean.NewsCenterBean;
import com.itcast.zhbj.utils.LogUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mListView;
    private MenuAdapter mAdapter;
    private int mCurrentPosition;
    private List<NewsCenterBean.NewsCenterMenuBean> mDatas;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化view
        mListView = (ListView) view.findViewById(R.id.menu_fm_listview);
        mListView.setOnItemClickListener(this);
    }

    public void setMenuDatas(List<NewsCenterBean.NewsCenterMenuBean> menuDatas) {
        //初始化数据
        this.mDatas = menuDatas;
        mAdapter = new MenuAdapter();
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击收起slidingMenu
        SlidingMenu menu = ((MainUI) getActivity()).getSlidingMenu();
        menu.toggle();

        //点击的是选中的不执行
        if (mCurrentPosition == position) {
            return;
        }

        //菜单显示选中项目
        mCurrentPosition = position;

        mAdapter.notifyDataSetChanged();

        //手动切换内容控制器
        LogUtils.p("当前选中页面标记"+position);
        ContentFragment contentFragment = ((MainUI)getActivity()).getContentFragment();

        contentFragment.changeMenu(position);
    }

    private class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (mDatas != null) {
                return mDatas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDatas != null) {
                return mDatas.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_menu, null);
                holder = new ViewHolder();
                //第一屏标记
                convertView.setTag(holder);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.item_menu_tv_title);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //设置条目文字
            NewsCenterBean.NewsCenterMenuBean bean = mDatas.get(position);
            holder.tvTitle.setText(bean.title);
            //设置选中
            holder.tvTitle.setEnabled(mCurrentPosition == position);

            return convertView;
        }

        private class ViewHolder {
            TextView tvTitle;
        }
    }
}
