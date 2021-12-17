package com.lianyi.paimonsnotebook.bean;

import java.util.List;

public class MonthLedgerBean {
    /**
     * uid : 游戏id
     * region : 服务器id
     * account_id : 登录id
     * nickname : nickNmae
     * date : 获取日期
     * month : 当前月份
     * optional_month : 可查询的月份(数组)
     * data_month : 数据月份
     * data_last_month : 上一个条数据是几月
     * day_data : {"current_primogems":,"current_mora":,"last_primogems":,"last_mora":} 当天数据
     * month_data : 月数据
     * lantern :
     */

    private int uid;
    private String region;
    private int account_id;
    private String nickname;
    private String date;
    private int month;
    private int data_month;
    private int data_last_month;
    private DayDataBean day_data;
    private MonthDataBean month_data;
    private boolean lantern;
    private List<Integer> optional_month;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getData_month() {
        return data_month;
    }

    public void setData_month(int data_month) {
        this.data_month = data_month;
    }

    public int getData_last_month() {
        return data_last_month;
    }

    public void setData_last_month(int data_last_month) {
        this.data_last_month = data_last_month;
    }

    public DayDataBean getDay_data() {
        return day_data;
    }

    public void setDay_data(DayDataBean day_data) {
        this.day_data = day_data;
    }

    public MonthDataBean getMonth_data() {
        return month_data;
    }

    public void setMonth_data(MonthDataBean month_data) {
        this.month_data = month_data;
    }

    public boolean isLantern() {
        return lantern;
    }

    public void setLantern(boolean lantern) {
        this.lantern = lantern;
    }

    public List<Integer> getOptional_month() {
        return optional_month;
    }

    public void setOptional_month(List<Integer> optional_month) {
        this.optional_month = optional_month;
    }

    public static class DayDataBean {
        /**
         * current_primogems : 0
         * current_mora : 20200
         * last_primogems : 630
         * last_mora : 182676
         */

        private int current_primogems;
        private int current_mora;
        private int last_primogems;
        private int last_mora;

        public int getCurrent_primogems() {
            return current_primogems;
        }

        public void setCurrent_primogems(int current_primogems) {
            this.current_primogems = current_primogems;
        }

        public int getCurrent_mora() {
            return current_mora;
        }

        public void setCurrent_mora(int current_mora) {
            this.current_mora = current_mora;
        }

        public int getLast_primogems() {
            return last_primogems;
        }

        public void setLast_primogems(int last_primogems) {
            this.last_primogems = last_primogems;
        }

        public int getLast_mora() {
            return last_mora;
        }

        public void setLast_mora(int last_mora) {
            this.last_mora = last_mora;
        }
    }

    public static class MonthDataBean {
        /**
         * current_primogems : 3310
         * current_mora : 2272320
         * last_primogems : 5875
         * last_mora : 5858069
         * current_primogems_level : 2
         * primogems_rate : -43
         * mora_rate : -61
         * group_by : [{"action_id":5,"action":"邮件奖励","num":1300,"percent":40},{"action_id":3,"action":"每日活跃","num":660,"percent":20},{"action_id":4,"action":"深境螺旋","num":600,"percent":18},{"action_id":6,"action":"活动奖励","num":480,"percent":14},{"action_id":2,"action":"任务奖励","num":0,"percent":0},{"action_id":1,"action":"冒险奖励","num":0,"percent":0},{"action_id":0,"action":"其他","num":270,"percent":8}]
         */

        private int current_primogems;
        private int current_mora;
        private int last_primogems;
        private int last_mora;
        private int current_primogems_level;
        private int primogems_rate;
        private int mora_rate;
        private List<GroupByBean> group_by;

        public int getCurrent_primogems() {
            return current_primogems;
        }

        public void setCurrent_primogems(int current_primogems) {
            this.current_primogems = current_primogems;
        }

        public int getCurrent_mora() {
            return current_mora;
        }

        public void setCurrent_mora(int current_mora) {
            this.current_mora = current_mora;
        }

        public int getLast_primogems() {
            return last_primogems;
        }

        public void setLast_primogems(int last_primogems) {
            this.last_primogems = last_primogems;
        }

        public int getLast_mora() {
            return last_mora;
        }

        public void setLast_mora(int last_mora) {
            this.last_mora = last_mora;
        }

        public int getCurrent_primogems_level() {
            return current_primogems_level;
        }

        public void setCurrent_primogems_level(int current_primogems_level) {
            this.current_primogems_level = current_primogems_level;
        }

        public int getPrimogems_rate() {
            return primogems_rate;
        }

        public void setPrimogems_rate(int primogems_rate) {
            this.primogems_rate = primogems_rate;
        }

        public int getMora_rate() {
            return mora_rate;
        }

        public void setMora_rate(int mora_rate) {
            this.mora_rate = mora_rate;
        }

        public List<GroupByBean> getGroup_by() {
            return group_by;
        }

        public void setGroup_by(List<GroupByBean> group_by) {
            this.group_by = group_by;
        }

        public static class GroupByBean {
            /**
             * action_id : 5
             * action : 邮件奖励
             * num : 1300
             * percent : 40
             */

            private int action_id;
            private String action;
            private int num;
            private int percent;

            public int getAction_id() {
                return action_id;
            }

            public void setAction_id(int action_id) {
                this.action_id = action_id;
            }

            public String getAction() {
                return action;
            }

            public void setAction(String action) {
                this.action = action;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getPercent() {
                return percent;
            }

            public void setPercent(int percent) {
                this.percent = percent;
            }
        }
    }
}
