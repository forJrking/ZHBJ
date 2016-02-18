package com.itcast.zhbj.bean;

import java.util.List;

public class NewsCenterBean {
    public List<NewsCenterMenuBean> data;
    public List<Long> extend;
    public int retcode;

    public class NewsCenterMenuBean{
        public List<NewsCenterPagerBean> children;
        public long id;
        public String title;
        public int type;

        public String url;
        public String url1;

        public String dayurl;
        public String excurl;
        public String weekurl;
    }

    public class NewsCenterPagerBean{
        public long id;
        public String title;
        public String url;
        public int type;
    }
}
