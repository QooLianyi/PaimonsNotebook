package com.lianyi.paimonsnotebook.bean.gacha;


public class UIGFExcelBean {
    private  String count;
    private  String gacha_type;
    private  String id;
    private  String item_id;
    private  String item_type;
    private  String lang;
    private  String name;
    private  String rank_type;
    private  String time;
    private  String uid;
    private  String uigf_gacha_type;

    public UIGFExcelBean(String count, String gacha_type, String id, String item_id, String item_type, String lang, String name, String rank_type, String time, String uid, String uigf_gacha_type) {
        this.count = count;
        this.gacha_type = gacha_type;
        this.id = id;
        this.item_id = item_id;
        this.item_type = item_type;
        this.lang = lang;
        this.name = name;
        this.rank_type = rank_type;
        this.time = time;
        this.uid = uid;
        this.uigf_gacha_type = uigf_gacha_type;
    }

    public UIGFExcelBean() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGacha_type() {
        return gacha_type;
    }

    public void setGacha_type(String gacha_type) {
        this.gacha_type = gacha_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank_type() {
        return rank_type;
    }

    public void setRank_type(String rank_type) {
        this.rank_type = rank_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUigf_gacha_type() {
        return uigf_gacha_type;
    }

    public void setUigf_gacha_type(String uigf_gacha_type) {
        this.uigf_gacha_type = uigf_gacha_type;
    }

    @Override
    public String toString() {
        return "UIGFGachaBean{" +
                "count='" + count + '\'' +
                ", gacha_type='" + gacha_type + '\'' +
                ", id='" + id + '\'' +
                ", item_id='" + item_id + '\'' +
                ", item_type='" + item_type + '\'' +
                ", lang='" + lang + '\'' +
                ", name='" + name + '\'' +
                ", rank_type='" + rank_type + '\'' +
                ", time='" + time + '\'' +
                ", uid='" + uid + '\'' +
                ", uigf_gacha_type='" + uigf_gacha_type + '\'' +
                '}';
    }
}
