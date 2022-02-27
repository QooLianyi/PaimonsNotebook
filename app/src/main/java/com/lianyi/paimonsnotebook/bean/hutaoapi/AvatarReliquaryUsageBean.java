package com.lianyi.paimonsnotebook.bean.hutaoapi;

import java.util.List;

public class AvatarReliquaryUsageBean {

    /**
     * avatar : 10000022
     * reliquaryUsage : [{"id":"2150021-4","value":0.8694503171247357},{"id":"2150021-2","value":0.06342494714587738},{"id":"2150011-2;2150021-2","value":0.010570824524312896},{"id":"2150021-2;2150071-2","value":0.007928118393234672},{"id":"2150021-2;2150031-2","value":0.006871035940803382},{"id":"2150201-4","value":0.0047568710359408035},{"id":"2150031-2","value":0.004228329809725159},{"id":"2150011-2","value":0.0036997885835095136}]
     */

    private int avatar;
    private List<ReliquaryUsageBean> reliquaryUsage;

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public List<ReliquaryUsageBean> getReliquaryUsage() {
        return reliquaryUsage;
    }

    public void setReliquaryUsage(List<ReliquaryUsageBean> reliquaryUsage) {
        this.reliquaryUsage = reliquaryUsage;
    }

    public static class ReliquaryUsageBean {
        /**
         * id : 2150021-4
         * value : 0.8694503171247357
         */

        private String id;
        private double value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
