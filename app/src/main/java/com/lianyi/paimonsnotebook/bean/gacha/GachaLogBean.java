package com.lianyi.paimonsnotebook.bean.gacha;

import java.util.List;

public class GachaLogBean {
    /**
     * page : 页数
     * size : 本页抽卡记录数量
     * total :
     * list : 列表
     * region : 服务器
     */

    private String page;
    private String size;
    private String total;
    private String region;
    private List<ListBean> list;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * uid :
         * gacha_type : 武器池子类型
         * item_id :
         * count :
         * time :
         * name :
         * lang : zh-cn
         * item_type :
         * rank_type :
         * id :
         */

        private String uid;
        private String gacha_type;
        private String item_id;
        private String count;
        private String time;
        private String name;
        private String lang;
        private String item_type;
        private String rank_type;
        private String id;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getGacha_type() {
            return gacha_type;
        }

        public void setGacha_type(String gacha_type) {
            this.gacha_type = gacha_type;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getItem_type() {
            return item_type;
        }

        public void setItem_type(String item_type) {
            this.item_type = item_type;
        }

        public String getRank_type() {
            return rank_type;
        }

        public void setRank_type(String rank_type) {
            this.rank_type = rank_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "uid='" + uid + '\'' +
                    ", gacha_type='" + gacha_type + '\'' +
                    ", item_id='" + item_id + '\'' +
                    ", count='" + count + '\'' +
                    ", time='" + time + '\'' +
                    ", name='" + name + '\'' +
                    ", lang='" + lang + '\'' +
                    ", item_type='" + item_type + '\'' +
                    ", rank_type='" + rank_type + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }
}
