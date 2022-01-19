package com.lianyi.paimonsnotebook.bean.home;

import java.util.List;

public class HomeOfficialCommendPostBean {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * post_id : 13731259
         * subject : 《原神》「雪峰胜景 星耀奇旅」网页活动现已上线
         * banner : https://upload-bbs.mihoyo.com/upload/2021/12/28/75276539/3aef6856cad6620afe19e3a70672ac9b_1240214931433818669.png
         * official_type : 2
         */

        private String post_id;
        private String subject;
        private String banner;
        private int official_type;

        public String getPost_id() {
            return post_id;
        }

        public void setPost_id(String post_id) {
            this.post_id = post_id;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public int getOfficial_type() {
            return official_type;
        }

        public void setOfficial_type(int official_type) {
            this.official_type = official_type;
        }
    }
}
