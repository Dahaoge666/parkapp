package com.example.parkapp.Bean;

import java.util.List;

public class ListBean {

    private List<FinishBean> finish;
    private List<UnfinishBean> unfinish;

    public List<FinishBean> getFinish() {
        return finish;
    }

    public void setFinish(List<FinishBean> finish) {
        this.finish = finish;
    }

    public List<UnfinishBean> getUnfinish() {
        return unfinish;
    }

    public void setUnfinish(List<UnfinishBean> unfinish) {
        this.unfinish = unfinish;
    }

    public static class FinishBean {
        /**
         * name : 文心二路
         * type : long
         * state : 未完成
         * starttime : 20190625 10:00:00
         * endtime : 20190626 10:00:00
         */

        private String name;
        private String type;
        private String state;
        private String starttime;
        private String endtime;

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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }
    }

    public static class UnfinishBean {
        /**
         * name : 文心二路
         * type : long
         * state : 已完成
         * starttime : 20190625 10:00:00
         * endtime : 20190626 10:00:00
         */

        private String name;
        private String type;
        private String state;
        private String starttime;
        private String endtime;

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

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }
    }
}
