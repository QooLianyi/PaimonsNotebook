package com.lianyi.paimonsnotebook.bean.hutaoapi;

import java.util.List;

public class TeamCollocationBean {
    /**
     * avater : 10000052
     * collocations : [{"id":10000032,"value":0.24108985826338242},{"id":10000023,"value":0.17769827072152652},{"id":10000025,"value":0.14591073803953947},{"id":10000058,"value":0.05550204119077107},{"id":10000047,"value":0.05284161277005642},{"id":10000030,"value":0.039264253933305814},{"id":10000056,"value":0.03371404981422871},{"id":10000051,"value":0.02564102564102564}]
     */

    private int avater;
    private List<CollocationsBean> collocations;

    public int getAvater() {
        return avater;
    }

    public void setAvater(int avater) {
        this.avater = avater;
    }

    public List<CollocationsBean> getCollocations() {
        return collocations;
    }

    public void setCollocations(List<CollocationsBean> collocations) {
        this.collocations = collocations;
    }

    public static class CollocationsBean {
        /**
         * id : 10000032
         * value : 0.24108985826338242
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
