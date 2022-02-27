package com.lianyi.paimonsnotebook.bean.hutaoapi;

import java.util.List;

public class HutaoDatabaseUploadBean {
    public HutaoDatabaseUploadBean() {
    }

    public HutaoDatabaseUploadBean(String uid, List<PlayerAvatarsBean> playerAvatars, List<PlayerSpiralAbyssesLevelsBean> playerSpiralAbyssesLevels) {
        this.uid = uid;
        this.playerAvatars = playerAvatars;
        this.playerSpiralAbyssesLevels = playerSpiralAbyssesLevels;

    }

    /**
     * uid : string
     * playerAvatars : [{"id":0,"level":0,"activedConstellationNum":0,"weapon":{"id":0,"level":0,"affixLevel":0},"reliquarySets":[{"id":0,"count":0}]}]
     * playerSpiralAbyssesLevels : [{"floorIndex":0,"levelIndex":0,"star":0,"battles":[{"battleIndex":0,"avatarIds":[0]}]}]
     */

    private String uid;
    private List<PlayerAvatarsBean> playerAvatars;
    private List<PlayerSpiralAbyssesLevelsBean> playerSpiralAbyssesLevels;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<PlayerAvatarsBean> getPlayerAvatars() {
        return playerAvatars;
    }

    public void setPlayerAvatars(List<PlayerAvatarsBean> playerAvatars) {
        this.playerAvatars = playerAvatars;
    }

    public List<PlayerSpiralAbyssesLevelsBean> getPlayerSpiralAbyssesLevels() {
        return playerSpiralAbyssesLevels;
    }

    public void setPlayerSpiralAbyssesLevels(List<PlayerSpiralAbyssesLevelsBean> playerSpiralAbyssesLevels) {
        this.playerSpiralAbyssesLevels = playerSpiralAbyssesLevels;
    }

    public static class PlayerAvatarsBean {
        public PlayerAvatarsBean(int id, int level, int activedConstellationNum, WeaponBean weapon, List<ReliquarySetsBean> reliquarySets) {
            this.id = id;
            this.level = level;
            this.activedConstellationNum = activedConstellationNum;
            this.weapon = weapon;
            this.reliquarySets = reliquarySets;
        }

        public PlayerAvatarsBean() {
        }

        /**
         * id : 0
         * level : 0
         * activedConstellationNum : 0
         * weapon : {"id":0,"level":0,"affixLevel":0}
         * reliquarySets : [{"id":0,"count":0}]
         */

        private int id;
        private int level;
        private int activedConstellationNum;
        private WeaponBean weapon;
        private List<ReliquarySetsBean> reliquarySets;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getActivedConstellationNum() {
            return activedConstellationNum;
        }

        public void setActivedConstellationNum(int activedConstellationNum) {
            this.activedConstellationNum = activedConstellationNum;
        }

        public WeaponBean getWeapon() {
            return weapon;
        }

        public void setWeapon(WeaponBean weapon) {
            this.weapon = weapon;
        }

        public List<ReliquarySetsBean> getReliquarySets() {
            return reliquarySets;
        }

        public void setReliquarySets(List<ReliquarySetsBean> reliquarySets) {
            this.reliquarySets = reliquarySets;
        }

        public static class WeaponBean {
            public WeaponBean() {
            }

            public WeaponBean(int id, int level, int affixLevel) {
                this.id = id;
                this.level = level;
                this.affixLevel = affixLevel;
            }

            /**
             * id : 0
             * level : 0
             * affixLevel : 0
             */

            private int id;
            private int level;
            private int affixLevel;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public int getAffixLevel() {
                return affixLevel;
            }

            public void setAffixLevel(int affixLevel) {
                this.affixLevel = affixLevel;
            }
        }

        public static class ReliquarySetsBean {
            public ReliquarySetsBean(int id, int count) {
                this.id = id;
                this.count = count;
            }

            public ReliquarySetsBean() {
            }

            /**
             * id : 0
             * count : 0
             */



            private int id;
            private int count;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
    }

    public static class PlayerSpiralAbyssesLevelsBean {
        public PlayerSpiralAbyssesLevelsBean() {
        }

        public PlayerSpiralAbyssesLevelsBean(int floorIndex, int levelIndex, int star, List<BattlesBean> battles) {
            this.floorIndex = floorIndex;
            this.levelIndex = levelIndex;
            this.star = star;
            this.battles = battles;
        }

        /**
         * floorIndex : 0
         * levelIndex : 0
         * star : 0
         * battles : [{"battleIndex":0,"avatarIds":[0]}]
         */

        private int floorIndex;
        private int levelIndex;
        private int star;
        private List<BattlesBean> battles;

        public int getFloorIndex() {
            return floorIndex;
        }

        public void setFloorIndex(int floorIndex) {
            this.floorIndex = floorIndex;
        }

        public int getLevelIndex() {
            return levelIndex;
        }

        public void setLevelIndex(int levelIndex) {
            this.levelIndex = levelIndex;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public List<BattlesBean> getBattles() {
            return battles;
        }

        public void setBattles(List<BattlesBean> battles) {
            this.battles = battles;
        }

        public static class BattlesBean {
            public BattlesBean() {
            }

            public BattlesBean(int battleIndex, List<Integer> avatarIds) {
                this.battleIndex = battleIndex;
                this.avatarIds = avatarIds;
            }

            /**
             * battleIndex : 0
             * avatarIds : [0]
             */

            private int battleIndex;
            private List<Integer> avatarIds;

            public int getBattleIndex() {
                return battleIndex;
            }

            public void setBattleIndex(int battleIndex) {
                this.battleIndex = battleIndex;
            }

            public List<Integer> getAvatarIds() {
                return avatarIds;
            }

            public void setAvatarIds(List<Integer> avatarIds) {
                this.avatarIds = avatarIds;
            }
        }
    }
}
