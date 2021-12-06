package com.lianyi.paimonsnotebook.bean;

import java.util.List;

public class HomeBannerBean {

    /**
     * retcode : 0
     * message : OK
     * data : {"list":[{"post_id":"12656628","subject":"【有奖活动】提瓦特冒险记\u2014丘丘雪人、酱酱雪人\u2026？","banner":"https://upload-bbs.mihoyo.com/upload/2021/12/03/76387920/f1b4b6d234a8c2535baa661889ee01c8_8907086442956721357.png","official_type":2},{"post_id":"12654697","subject":"「旅行绘本」网页活动：完成绘制得原石奖励！","banner":"https://upload-bbs.mihoyo.com/upload/2021/12/03/75276539/8370b24a2e43a609cfd2783d3b30dd5a_7207484174396512402.png","official_type":2},{"post_id":"12617678","subject":"原神-万有铺子 现货周边情报更新","banner":"https://upload-bbs.mihoyo.com/upload/2021/12/02/218080295/ac2df487dbdac92c12b57fc95eba3fba_2956542312335029368.jpg","official_type":3},{"post_id":"12617637","subject":"甘雨生日快乐｜难得见到这样的花丛，很漂亮对吧？","banner":"https://upload-bbs.mihoyo.com/upload/2021/12/01/75276539/1a315a2e3a95b10b17b5e6104be0ff03_4973445414116350303.png","official_type":3},{"post_id":"12671347","subject":"致旅行者的答谢信","banner":"","official_type":1},{"post_id":"12656629","subject":"「周末一小时 x 原神」 Apple 授权专营店线下特别活动","banner":"https://upload-bbs.mihoyo.com/upload/2021/12/03/75276539/71aaf599c49f5f627ccc4f24baf4a08d_1963148861045492066.jpg","official_type":1}]}
     */

    private int retcode;
    private String message;
    private DataBean data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * post_id : 12656628
             * subject : 【有奖活动】提瓦特冒险记—丘丘雪人、酱酱雪人…？
             * banner : https://upload-bbs.mihoyo.com/upload/2021/12/03/76387920/f1b4b6d234a8c2535baa661889ee01c8_8907086442956721357.png
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
}
