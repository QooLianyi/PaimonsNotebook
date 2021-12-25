package com.lianyi.paimonsnotebook.bean.dailynote;

import java.util.List;

public class CurrentMonthDailySignRewardInfoBean {
    /**
     * month : 当前月份
     * awards : 奖励列表
     * resign : true
     */

    private int month;
    private boolean resign;
    private List<AwardsBean> awards;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public boolean isResign() {
        return resign;
    }

    public void setResign(boolean resign) {
        this.resign = resign;
    }

    public List<AwardsBean> getAwards() {
        return awards;
    }

    public void setAwards(List<AwardsBean> awards) {
        this.awards = awards;
    }

    public static class AwardsBean {
        /**
         * icon : 图片URL
         * name : 名称
         * cnt : 数量
         */

        private String icon;
        private String name;
        private int cnt;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCnt() {
            return cnt;
        }

        public void setCnt(int cnt) {
            this.cnt = cnt;
        }
    }
}
