package com.lianyi.paimonsnotebook.bean.hutaoapi;

import java.util.List;

public class ConstellationBean {
    /**
     * avatar : 10000022
     * holdingRate : 0.48910648714810284
     * rate : [{"id":0,"value":0.8583583583583584},{"id":1,"value":0.08958958958958958},{"id":2,"value":0.033533533533533534},{"id":3,"value":0.005005005005005005},{"id":4,"value":0.002002002002002002},{"id":5,"value":5.005005005005005E-4},{"id":6,"value":0.011011011011011011}]
     */

    private int avatar;
    private double holdingRate;
    private List<RateBean> rate;

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public double getHoldingRate() {
        return holdingRate;
    }

    public void setHoldingRate(double holdingRate) {
        this.holdingRate = holdingRate;
    }

    public List<RateBean> getRate() {
        return rate;
    }

    public void setRate(List<RateBean> rate) {
        this.rate = rate;
    }

    public static class RateBean {
        /**
         * id : 0
         * value : 0.8583583583583584
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
