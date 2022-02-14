package com.lianyi.paimonsnotebook.bean.gacha;


public class UIGFJsonBean {
    private  String count;
    private  String gacha_type;
    private  String id;
    private  String item_id;
    private  String item_type;
    private  String name;
    private  String rank_type;
    private  String time;
    private  String uigf_gacha_type;

    public UIGFJsonBean(String count, String gacha_type, String id, String item_id, String item_type, String name, String rank_type, String time,  String uigf_gacha_type) {
        this.count = count;
        this.gacha_type = gacha_type;
        this.id = id;
        this.item_id = item_id;
        this.item_type = item_type;
        this.name = name;
        this.rank_type = rank_type;
        this.time = time;
        this.uigf_gacha_type = uigf_gacha_type;
    }

    public UIGFJsonBean() {
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
                ", name='" + name + '\'' +
                ", rank_type='" + rank_type + '\'' +
                ", time='" + time + '\'' +
                ", uigf_gacha_type='" + uigf_gacha_type + '\'' +
                '}';
    }
}
