package com.lianyi.paimonsnotebook.bean.dailynote;

public class CurrentMonthDailySignInfoBean {
    /**
     * total_sign_day : 签到天数
     * today : 当前日期
     * is_sign : 今天是否已经签到
     * first_bind : false
     * is_sub : false
     * month_first : false
     * sign_cnt_missed : 漏签天数
     */

    private int total_sign_day;
    private String today;
    private boolean is_sign;
    private boolean first_bind;
    private boolean is_sub;
    private boolean month_first;
    private int sign_cnt_missed;

    public int getTotal_sign_day() {
        return total_sign_day;
    }

    public void setTotal_sign_day(int total_sign_day) {
        this.total_sign_day = total_sign_day;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public boolean isIs_sign() {
        return is_sign;
    }

    public void setIs_sign(boolean is_sign) {
        this.is_sign = is_sign;
    }

    public boolean isFirst_bind() {
        return first_bind;
    }

    public void setFirst_bind(boolean first_bind) {
        this.first_bind = first_bind;
    }

    public boolean isIs_sub() {
        return is_sub;
    }

    public void setIs_sub(boolean is_sub) {
        this.is_sub = is_sub;
    }

    public boolean isMonth_first() {
        return month_first;
    }

    public void setMonth_first(boolean month_first) {
        this.month_first = month_first;
    }

    public int getSign_cnt_missed() {
        return sign_cnt_missed;
    }

    public void setSign_cnt_missed(int sign_cnt_missed) {
        this.sign_cnt_missed = sign_cnt_missed;
    }
}
