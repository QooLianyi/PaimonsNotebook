package com.lianyi.paimonsnotebook.bean.dailynote;

import java.util.List;

public class DailyNoteBean {
    /**
     * current_resin : 当前树脂
     * max_resin :最大树脂
     * resin_recovery_time : 树脂剩余恢复事件
     * finished_task_num : 完成的每日任务数
     * total_task_num : 每日任务上限数量
     * is_extra_task_reward_received : 是否已经领取了完成四个每日委托后的额外奖励(20原石)
     * remain_resin_discount_num : 剩余周本消耗减半次数
     * resin_discount_num_limit : 最大周本减半次数
     * current_expedition_num : 当前探险委托数
     * max_expedition_num : 最大探险委托书
     * expeditions : [{"avatar_side_icon":"https://upload-bbs.mihoyo.com/game_record/genshin/character_side_icon/UI_AvatarIcon_Side_Chongyun.png","status":"Finished","remained_time":"0"},{"avatar_side_icon":"https://upload-bbs.mihoyo.com/game_record/genshin/character_side_icon/UI_AvatarIcon_Side_Fischl.png","status":"Finished","remained_time":"0"},{"avatar_side_icon":"https://upload-bbs.mihoyo.com/game_record/genshin/character_side_icon/UI_AvatarIcon_Side_Bennett.png","status":"Finished","remained_time":"0"},{"avatar_side_icon":"https://upload-bbs.mihoyo.com/game_record/genshin/character_side_icon/UI_AvatarIcon_Side_Keqing.png","status":"Finished","remained_time":"0"},{"avatar_side_icon":"https://upload-bbs.mihoyo.com/game_record/genshin/character_side_icon/UI_AvatarIcon_Side_Sara.png","status":"Finished","remained_time":"0"}]
     */

    private int current_resin;
    private int max_resin;
    private String resin_recovery_time;
    private int finished_task_num;
    private int total_task_num;
    private boolean is_extra_task_reward_received;
    private int remain_resin_discount_num;
    private int resin_discount_num_limit;
    private int current_expedition_num;
    private int max_expedition_num;
    private List<ExpeditionsBean> expeditions;

    public int getCurrent_resin() {
        return current_resin;
    }

    public void setCurrent_resin(int current_resin) {
        this.current_resin = current_resin;
    }

    public int getMax_resin() {
        return max_resin;
    }

    public void setMax_resin(int max_resin) {
        this.max_resin = max_resin;
    }

    public String getResin_recovery_time() {
        return resin_recovery_time;
    }

    public void setResin_recovery_time(String resin_recovery_time) {
        this.resin_recovery_time = resin_recovery_time;
    }

    public int getFinished_task_num() {
        return finished_task_num;
    }

    public void setFinished_task_num(int finished_task_num) {
        this.finished_task_num = finished_task_num;
    }

    public int getTotal_task_num() {
        return total_task_num;
    }

    public void setTotal_task_num(int total_task_num) {
        this.total_task_num = total_task_num;
    }

    public boolean isIs_extra_task_reward_received() {
        return is_extra_task_reward_received;
    }

    public void setIs_extra_task_reward_received(boolean is_extra_task_reward_received) {
        this.is_extra_task_reward_received = is_extra_task_reward_received;
    }

    public int getRemain_resin_discount_num() {
        return remain_resin_discount_num;
    }

    public void setRemain_resin_discount_num(int remain_resin_discount_num) {
        this.remain_resin_discount_num = remain_resin_discount_num;
    }

    public int getResin_discount_num_limit() {
        return resin_discount_num_limit;
    }

    public void setResin_discount_num_limit(int resin_discount_num_limit) {
        this.resin_discount_num_limit = resin_discount_num_limit;
    }

    public int getCurrent_expedition_num() {
        return current_expedition_num;
    }

    public void setCurrent_expedition_num(int current_expedition_num) {
        this.current_expedition_num = current_expedition_num;
    }

    public int getMax_expedition_num() {
        return max_expedition_num;
    }

    public void setMax_expedition_num(int max_expedition_num) {
        this.max_expedition_num = max_expedition_num;
    }

    public List<ExpeditionsBean> getExpeditions() {
        return expeditions;
    }

    public void setExpeditions(List<ExpeditionsBean> expeditions) {
        this.expeditions = expeditions;
    }

    public static class ExpeditionsBean {
        /**
         * avatar_side_icon :
         * status : Finished
         * remained_time : 0
         */

        private String avatar_side_icon;
        private String status;
        private String remained_time;

        public String getAvatar_side_icon() {
            return avatar_side_icon;
        }

        public void setAvatar_side_icon(String avatar_side_icon) {
            this.avatar_side_icon = avatar_side_icon;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemained_time() {
            return remained_time;
        }

        public void setRemained_time(String remained_time) {
            this.remained_time = remained_time;
        }
    }
}
