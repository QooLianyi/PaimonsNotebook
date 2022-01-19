package com.lianyi.paimonsnotebook.bean;

import java.util.List;

public class SpiralAbyssBean {
    /**
     * schedule_id :
     * start_time :
     * end_time :
     * total_battle_times :
     * total_win_times :
     * max_floor :
     * reveal_rank :
     * defeat_rank :
     * damage_rank :
     * take_damage_rank :
     * normal_skill_rank :
     * energy_skill_rank :
     * floors :
     * total_star : 9
     * is_unlock : true
     */

    private int schedule_id;
    private String start_time;
    private String end_time;
    private int total_battle_times;
    private int total_win_times;
    private String max_floor;
    private int total_star;
    private boolean is_unlock;
    private List<RevealRankBean> reveal_rank;
    private List<DefeatRankBean> defeat_rank;
    private List<DamageRankBean> damage_rank;
    private List<TakeDamageRankBean> take_damage_rank;
    private List<NormalSkillRankBean> normal_skill_rank;
    private List<EnergySkillRankBean> energy_skill_rank;
    private List<FloorsBean> floors;

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getTotal_battle_times() {
        return total_battle_times;
    }

    public void setTotal_battle_times(int total_battle_times) {
        this.total_battle_times = total_battle_times;
    }

    public int getTotal_win_times() {
        return total_win_times;
    }

    public void setTotal_win_times(int total_win_times) {
        this.total_win_times = total_win_times;
    }

    public String getMax_floor() {
        return max_floor;
    }

    public void setMax_floor(String max_floor) {
        this.max_floor = max_floor;
    }

    public int getTotal_star() {
        return total_star;
    }

    public void setTotal_star(int total_star) {
        this.total_star = total_star;
    }

    public boolean isIs_unlock() {
        return is_unlock;
    }

    public void setIs_unlock(boolean is_unlock) {
        this.is_unlock = is_unlock;
    }

    public List<RevealRankBean> getReveal_rank() {
        return reveal_rank;
    }

    public void setReveal_rank(List<RevealRankBean> reveal_rank) {
        this.reveal_rank = reveal_rank;
    }

    public List<DefeatRankBean> getDefeat_rank() {
        return defeat_rank;
    }

    public void setDefeat_rank(List<DefeatRankBean> defeat_rank) {
        this.defeat_rank = defeat_rank;
    }

    public List<DamageRankBean> getDamage_rank() {
        return damage_rank;
    }

    public void setDamage_rank(List<DamageRankBean> damage_rank) {
        this.damage_rank = damage_rank;
    }

    public List<TakeDamageRankBean> getTake_damage_rank() {
        return take_damage_rank;
    }

    public void setTake_damage_rank(List<TakeDamageRankBean> take_damage_rank) {
        this.take_damage_rank = take_damage_rank;
    }

    public List<NormalSkillRankBean> getNormal_skill_rank() {
        return normal_skill_rank;
    }

    public void setNormal_skill_rank(List<NormalSkillRankBean> normal_skill_rank) {
        this.normal_skill_rank = normal_skill_rank;
    }

    public List<EnergySkillRankBean> getEnergy_skill_rank() {
        return energy_skill_rank;
    }

    public void setEnergy_skill_rank(List<EnergySkillRankBean> energy_skill_rank) {
        this.energy_skill_rank = energy_skill_rank;
    }

    public List<FloorsBean> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorsBean> floors) {
        this.floors = floors;
    }

    public static class RevealRankBean {

        private int avatar_id;
        private String avatar_icon;
        private int value;
        private int rarity;

        public int getAvatar_id() {
            return avatar_id;
        }

        public void setAvatar_id(int avatar_id) {
            this.avatar_id = avatar_id;
        }

        public String getAvatar_icon() {
            return avatar_icon;
        }

        public void setAvatar_icon(String avatar_icon) {
            this.avatar_icon = avatar_icon;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getRarity() {
            return rarity;
        }

        public void setRarity(int rarity) {
            this.rarity = rarity;
        }
    }

    public static class DefeatRankBean {

        private int avatar_id;
        private String avatar_icon;
        private int value;
        private int rarity;

        public int getAvatar_id() {
            return avatar_id;
        }

        public void setAvatar_id(int avatar_id) {
            this.avatar_id = avatar_id;
        }

        public String getAvatar_icon() {
            return avatar_icon;
        }

        public void setAvatar_icon(String avatar_icon) {
            this.avatar_icon = avatar_icon;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getRarity() {
            return rarity;
        }

        public void setRarity(int rarity) {
            this.rarity = rarity;
        }
    }

    public static class DamageRankBean {

        private int avatar_id;
        private String avatar_icon;
        private int value;
        private int rarity;

        public int getAvatar_id() {
            return avatar_id;
        }

        public void setAvatar_id(int avatar_id) {
            this.avatar_id = avatar_id;
        }

        public String getAvatar_icon() {
            return avatar_icon;
        }

        public void setAvatar_icon(String avatar_icon) {
            this.avatar_icon = avatar_icon;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getRarity() {
            return rarity;
        }

        public void setRarity(int rarity) {
            this.rarity = rarity;
        }
    }

    public static class TakeDamageRankBean {

        private int avatar_id;
        private String avatar_icon;
        private int value;
        private int rarity;

        public int getAvatar_id() {
            return avatar_id;
        }

        public void setAvatar_id(int avatar_id) {
            this.avatar_id = avatar_id;
        }

        public String getAvatar_icon() {
            return avatar_icon;
        }

        public void setAvatar_icon(String avatar_icon) {
            this.avatar_icon = avatar_icon;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getRarity() {
            return rarity;
        }

        public void setRarity(int rarity) {
            this.rarity = rarity;
        }
    }

    public static class NormalSkillRankBean {

        private int avatar_id;
        private String avatar_icon;
        private int value;
        private int rarity;

        public int getAvatar_id() {
            return avatar_id;
        }

        public void setAvatar_id(int avatar_id) {
            this.avatar_id = avatar_id;
        }

        public String getAvatar_icon() {
            return avatar_icon;
        }

        public void setAvatar_icon(String avatar_icon) {
            this.avatar_icon = avatar_icon;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getRarity() {
            return rarity;
        }

        public void setRarity(int rarity) {
            this.rarity = rarity;
        }
    }

    public static class EnergySkillRankBean {

        private int avatar_id;
        private String avatar_icon;
        private int value;
        private int rarity;

        public int getAvatar_id() {
            return avatar_id;
        }

        public void setAvatar_id(int avatar_id) {
            this.avatar_id = avatar_id;
        }

        public String getAvatar_icon() {
            return avatar_icon;
        }

        public void setAvatar_icon(String avatar_icon) {
            this.avatar_icon = avatar_icon;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getRarity() {
            return rarity;
        }

        public void setRarity(int rarity) {
            this.rarity = rarity;
        }
    }

    public static class FloorsBean {

        private int index;
        private String icon;
        private boolean is_unlock;
        private String settle_time;
        private int star;
        private int max_star;
        private List<LevelsBean> levels;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public boolean isIs_unlock() {
            return is_unlock;
        }

        public void setIs_unlock(boolean is_unlock) {
            this.is_unlock = is_unlock;
        }

        public String getSettle_time() {
            return settle_time;
        }

        public void setSettle_time(String settle_time) {
            this.settle_time = settle_time;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public int getMax_star() {
            return max_star;
        }

        public void setMax_star(int max_star) {
            this.max_star = max_star;
        }

        public List<LevelsBean> getLevels() {
            return levels;
        }

        public void setLevels(List<LevelsBean> levels) {
            this.levels = levels;
        }

        public static class LevelsBean {

            private int index;
            private int star;
            private int max_star;
            private List<BattlesBean> battles;

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public int getMax_star() {
                return max_star;
            }

            public void setMax_star(int max_star) {
                this.max_star = max_star;
            }

            public List<BattlesBean> getBattles() {
                return battles;
            }

            public void setBattles(List<BattlesBean> battles) {
                this.battles = battles;
            }

            public static class BattlesBean {

                private int index;
                private String timestamp;
                private List<AvatarsBean> avatars;

                public int getIndex() {
                    return index;
                }

                public void setIndex(int index) {
                    this.index = index;
                }

                public String getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(String timestamp) {
                    this.timestamp = timestamp;
                }

                public List<AvatarsBean> getAvatars() {
                    return avatars;
                }

                public void setAvatars(List<AvatarsBean> avatars) {
                    this.avatars = avatars;
                }

                public static class AvatarsBean {

                    private int id;
                    private String icon;
                    private int level;
                    private int rarity;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getIcon() {
                        return icon;
                    }

                    public void setIcon(String icon) {
                        this.icon = icon;
                    }

                    public int getLevel() {
                        return level;
                    }

                    public void setLevel(int level) {
                        this.level = level;
                    }

                    public int getRarity() {
                        return rarity;
                    }

                    public void setRarity(int rarity) {
                        this.rarity = rarity;
                    }
                }
            }
        }
    }
}
