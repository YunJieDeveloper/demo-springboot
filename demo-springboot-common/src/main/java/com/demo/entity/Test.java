package com.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
/***
 * @Date 2017/12/7
 *@Description  GsonFormat插件使用  control＋enter
 * @author zhanghesheng
 * */
public class Test {


    /**
     * user_id : 9527
     * context : {"ip":"192.168.1.1","city":"北京市","province":"北京市","lat":"","lon":"","name":"邹波","phone":"18302991865","idcard":"53210119780626091"}
     */
    @JsonProperty("user_id")
    private String userId;

    private ContextBean context;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ContextBean getContext() {
        return context;
    }

    public void setContext(ContextBean context) {
        this.context = context;
    }

    public static class ContextBean {
        /**
         * ip : 192.168.1.1
         * city : 北京市
         * province : 北京市
         * lat :
         * lon :
         * name : 邹波
         * phone : 18302991865
         * idcard : 53210119780626091
         */

        private String ip;
        private String city;
        private String province;
        private String lat;
        private String lon;
        private String name;
        private String phone;
        private String idcard;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }
    }
}
