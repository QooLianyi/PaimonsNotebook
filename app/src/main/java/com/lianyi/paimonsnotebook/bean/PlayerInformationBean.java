package com.lianyi.paimonsnotebook.bean;

import java.util.List;

public class PlayerInformationBean {
    private Object role;
    private StatsBean stats;
    private List<AvatarsBean> avatars;
    private List<?> city_explorations;
    private List<WorldExplorationsBean> world_explorations;
    private List<HomesBean> homes;

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public StatsBean getStats() {
        return stats;
    }

    public void setStats(StatsBean stats) {
        this.stats = stats;
    }

    public List<AvatarsBean> getAvatars() {
        return avatars;
    }

    public void setAvatars(List<AvatarsBean> avatars) {
        this.avatars = avatars;
    }

    public List<?> getCity_explorations() {
        return city_explorations;
    }

    public void setCity_explorations(List<?> city_explorations) {
        this.city_explorations = city_explorations;
    }

    public List<WorldExplorationsBean> getWorld_explorations() {
        return world_explorations;
    }

    public void setWorld_explorations(List<WorldExplorationsBean> world_explorations) {
        this.world_explorations = world_explorations;
    }

    public List<HomesBean> getHomes() {
        return homes;
    }

    public void setHomes(List<HomesBean> homes) {
        this.homes = homes;
    }

    public static class StatsBean {
        private int active_day_number;
        private int achievement_number;
        private int win_rate;
        private int anemoculus_number;
        private int geoculus_number;
        private int avatar_number;
        private int way_point_number;
        private int domain_number;
        private String spiral_abyss;
        private int precious_chest_number;
        private int luxurious_chest_number;
        private int exquisite_chest_number;
        private int common_chest_number;
        private int electroculus_number;
        private int magic_chest_number;

        public int getActive_day_number() {
            return active_day_number;
        }

        public void setActive_day_number(int active_day_number) {
            this.active_day_number = active_day_number;
        }

        public int getAchievement_number() {
            return achievement_number;
        }

        public void setAchievement_number(int achievement_number) {
            this.achievement_number = achievement_number;
        }

        public int getWin_rate() {
            return win_rate;
        }

        public void setWin_rate(int win_rate) {
            this.win_rate = win_rate;
        }

        public int getAnemoculus_number() {
            return anemoculus_number;
        }

        public void setAnemoculus_number(int anemoculus_number) {
            this.anemoculus_number = anemoculus_number;
        }

        public int getGeoculus_number() {
            return geoculus_number;
        }

        public void setGeoculus_number(int geoculus_number) {
            this.geoculus_number = geoculus_number;
        }

        public int getAvatar_number() {
            return avatar_number;
        }

        public void setAvatar_number(int avatar_number) {
            this.avatar_number = avatar_number;
        }

        public int getWay_point_number() {
            return way_point_number;
        }

        public void setWay_point_number(int way_point_number) {
            this.way_point_number = way_point_number;
        }

        public int getDomain_number() {
            return domain_number;
        }

        public void setDomain_number(int domain_number) {
            this.domain_number = domain_number;
        }

        public String getSpiral_abyss() {
            return spiral_abyss;
        }

        public void setSpiral_abyss(String spiral_abyss) {
            this.spiral_abyss = spiral_abyss;
        }

        public int getPrecious_chest_number() {
            return precious_chest_number;
        }

        public void setPrecious_chest_number(int precious_chest_number) {
            this.precious_chest_number = precious_chest_number;
        }

        public int getLuxurious_chest_number() {
            return luxurious_chest_number;
        }

        public void setLuxurious_chest_number(int luxurious_chest_number) {
            this.luxurious_chest_number = luxurious_chest_number;
        }

        public int getExquisite_chest_number() {
            return exquisite_chest_number;
        }

        public void setExquisite_chest_number(int exquisite_chest_number) {
            this.exquisite_chest_number = exquisite_chest_number;
        }

        public int getCommon_chest_number() {
            return common_chest_number;
        }

        public void setCommon_chest_number(int common_chest_number) {
            this.common_chest_number = common_chest_number;
        }

        public int getElectroculus_number() {
            return electroculus_number;
        }

        public void setElectroculus_number(int electroculus_number) {
            this.electroculus_number = electroculus_number;
        }

        public int getMagic_chest_number() {
            return magic_chest_number;
        }

        public void setMagic_chest_number(int magic_chest_number) {
            this.magic_chest_number = magic_chest_number;
        }
    }

    public static class AvatarsBean {

        private int id;
        private String image;
        private String name;
        private String element;
        private int fetter;
        private int level;
        private int rarity;
        private int actived_constellation_num;
        private String card_image;
        private boolean is_chosen;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getElement() {
            return element;
        }

        public void setElement(String element) {
            this.element = element;
        }

        public int getFetter() {
            return fetter;
        }

        public void setFetter(int fetter) {
            this.fetter = fetter;
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

        public int getActived_constellation_num() {
            return actived_constellation_num;
        }

        public void setActived_constellation_num(int actived_constellation_num) {
            this.actived_constellation_num = actived_constellation_num;
        }

        public String getCard_image() {
            return card_image;
        }

        public void setCard_image(String card_image) {
            this.card_image = card_image;
        }

        public boolean isIs_chosen() {
            return is_chosen;
        }

        public void setIs_chosen(boolean is_chosen) {
            this.is_chosen = is_chosen;
        }
    }

    public static class WorldExplorationsBean {

        private int level;
        private int exploration_percentage;
        private String icon;
        private String name;
        private String type;
        private int id;
        private List<OfferingsBean> offerings;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getExploration_percentage() {
            return exploration_percentage;
        }

        public void setExploration_percentage(int exploration_percentage) {
            this.exploration_percentage = exploration_percentage;
        }

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<OfferingsBean> getOfferings() {
            return offerings;
        }

        public void setOfferings(List<OfferingsBean> offerings) {
            this.offerings = offerings;
        }

        public static class OfferingsBean {

            private String name;
            private int level;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }
        }
    }

    public static class HomesBean {

        private int level;
        private int visit_num;
        private int comfort_num;
        private int item_num;
        private String name;
        private String icon;
        private String comfort_level_name;
        private String comfort_level_icon;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getVisit_num() {
            return visit_num;
        }

        public void setVisit_num(int visit_num) {
            this.visit_num = visit_num;
        }

        public int getComfort_num() {
            return comfort_num;
        }

        public void setComfort_num(int comfort_num) {
            this.comfort_num = comfort_num;
        }

        public int getItem_num() {
            return item_num;
        }

        public void setItem_num(int item_num) {
            this.item_num = item_num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getComfort_level_name() {
            return comfort_level_name;
        }

        public void setComfort_level_name(String comfort_level_name) {
            this.comfort_level_name = comfort_level_name;
        }

        public String getComfort_level_icon() {
            return comfort_level_icon;
        }

        public void setComfort_level_icon(String comfort_level_icon) {
            this.comfort_level_icon = comfort_level_icon;
        }
    }
}
