package com.lianyi.paimonsnotebook.bean.hutaoapi;

import java.util.List;

public class WeaponUsageBean {
    /**
     * avatar : 10000022
     * weapons : [{"id":15402,"value":0.3000997008973081},{"id":15503,"value":0.23080757726819542},{"id":15401,"value":0.22582253240279163},{"id":15501,"value":0.11365902293120637},{"id":15409,"value":0.03140578265204387},{"id":15413,"value":0.028414755732801594},{"id":15403,"value":0.018943170488534396},{"id":15101,"value":0.011465603190428714}]
     */

    private int avatar;
    private List<WeaponsBean> weapons;

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public List<WeaponsBean> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<WeaponsBean> weapons) {
        this.weapons = weapons;
    }

    public static class WeaponsBean {
        /**
         * id : 15402
         * value : 0.3000997008973081
         */

        private int id;
        private double value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
