package com.lianyi.paimonsnotebook.bean.hutaoapi;

import java.util.List;

public class AvatarParticipationBean {
    /**
     * floor : 9
     * avatarUsage : [{"id":10000052,"value":0.06881702621009482},{"id":10000032,"value":0.09084509958179786},{"id":10000025,"value":0.09254275185292535},{"id":10000030,"value":0.0956068071715457},{"id":10000031,"value":0.0076601382965508676},{"id":10000043,"value":0.028860088609167323},{"id":10000023,"value":0.05925220487764482},{"id":10000002,"value":0.041737402177963644},{"id":10000051,"value":0.010641381309262557},{"id":10000027,"value":0.009192165955861042},{"id":10000056,"value":0.008198418284957145},{"id":10000047,"value":0.03788662995321105},{"id":10000046,"value":0.051509254275185296},{"id":10000037,"value":0.05602252494720716},{"id":10000058,"value":0.025340565608049356},{"id":10000064,"value":0.0059210798724690485},{"id":10000057,"value":0.0069562336963272745},{"id":10000039,"value":0.040785060660014076},{"id":10000048,"value":0.002649993789077057},{"id":10000041,"value":0.026541344043724897},{"id":10000049,"value":0.009771852097221647},{"id":10000022,"value":0.04703738975611776},{"id":10000055,"value":0.007453107531779223},{"id":10000014,"value":0.005258581425199784},{"id":10000045,"value":0.005299987578154114},{"id":10000033,"value":0.0134155935572026},{"id":10000034,"value":0.0057554552606517324},{"id":10000007,"value":0.0024429630243054117},{"id":10000063,"value":0.03026789780961451},{"id":10000029,"value":0.00335389838930065},{"id":10000038,"value":0.022897602583743945},{"id":10000016,"value":0.006832015237464287},{"id":10000054,"value":0.02310463334851559},{"id":10000026,"value":0.01556871351082771},{"id":10000003,"value":0.010724193615171214},{"id":10000015,"value":0.0011593722827212124},{"id":10000035,"value":0.005175769119291127},{"id":10000042,"value":0.0039749906836155854},{"id":10000024,"value":0.0025257753302140697},{"id":10000053,"value":6.210922943149352E-4},{"id":10000050,"value":0.003229679930437663},{"id":10000036,"value":0.0046788952838391785},{"id":10000005,"value":0.0015320276593101736},{"id":10000020,"value":4.1406152954329015E-4},{"id":10000021,"value":1.6562461181731606E-4},{"id":10000006,"value":3.7265537658896114E-4}]
     */

    private int floor;
    private List<AvatarUsageBean> avatarUsage;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public List<AvatarUsageBean> getAvatarUsage() {
        return avatarUsage;
    }

    public void setAvatarUsage(List<AvatarUsageBean> avatarUsage) {
        this.avatarUsage = avatarUsage;
    }

    public static class AvatarUsageBean {
        /**
         * id : 10000052
         * value : 0.06881702621009482
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
