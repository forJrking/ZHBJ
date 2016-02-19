package com.itcast.zhbj.bean;

import java.util.List;

public class NewsListPagerBean {

    public NewsListDataBean data;
    public int              retcode;


    public class NewsListDataBean {
        public String            countcommenturl;
        public String            more;
        public List<NewsBean>    news;
        public String            title;
        public List<TopicBean>   topic;
        public List<TopNewsBean> topnews;
    }

    public class NewsBean {
        public boolean comment;
        public String  commentlist;
        public String  commenturl;
        public long    id;
        public String  listimage;
        public String  pubdate;
        public String  title;
        public String  type;
        public String  url;
    }

    public class TopicBean {
        public String description;
        public long   id;
        public String listimage;
        public int    sort;
        public String title;
        public String url;
    }

    public class TopNewsBean {
        public boolean comment;
        public String  commentlist;
        public String  commenturl;
        public long    id;
        public String  pubdate;
        public String  title;
        public String  topimage;
        public String  type;
        public String  url;
    }
}
