package com.lianyi.paimonsnotebook.bean;

import java.util.List;

/*
* 通过Cookie获取原神游戏角色
*
* */

public class GetGameRolesByCookieBean {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        private String game_biz;
        private String region;
        private String game_uid;
        private String nickname;
        private int level;
        private boolean is_chosen;
        private String region_name;
        private boolean is_official;

        public String getGame_biz() {
            return game_biz;
        }

        public void setGame_biz(String game_biz) {
            this.game_biz = game_biz;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getGame_uid() {
            return game_uid;
        }

        public void setGame_uid(String game_uid) {
            this.game_uid = game_uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public boolean isIs_chosen() {
            return is_chosen;
        }

        public void setIs_chosen(boolean is_chosen) {
            this.is_chosen = is_chosen;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public boolean isIs_official() {
            return is_official;
        }

        public void setIs_official(boolean is_official) {
            this.is_official = is_official;
        }
    }
}
