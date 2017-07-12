package com.hgad.warehousemanager.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/10.
 */
public class WeatherEntity {

    /**
     * data : {"yesterday":{"date":"9日星期日","high":"高温 35℃","fx":"东北风","low":"低温 24℃","fl":"微风","type":"多云"},"city":"北京","aqi":"45","forecast":[{"date":"10日星期一","high":"高温 36℃","fengli":"微风级","low":"低温 23℃","fengxiang":"南风","type":"晴"},{"date":"11日星期二","high":"高温 36℃","fengli":"微风级","low":"低温 27℃","fengxiang":"南风","type":"多云"},{"date":"12日星期三","high":"高温 35℃","fengli":"微风级","low":"低温 27℃","fengxiang":"南风","type":"多云"},{"date":"13日星期四","high":"高温 36℃","fengli":"微风级","low":"低温 27℃","fengxiang":"南风","type":"多云"},{"date":"14日星期五","high":"高温 35℃","fengli":"微风级","low":"低温 26℃","fengxiang":"南风","type":"雷阵雨"}],"ganmao":"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。","wendu":"35"}
     * status : 1000
     * desc : OK
     */
    private DataEntity data;
    private int status;
    private String desc;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public DataEntity getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

    public class DataEntity {
        /**
         * yesterday : {"date":"9日星期日","high":"高温 35℃","fx":"东北风","low":"低温 24℃","fl":"微风","type":"多云"}
         * city : 北京
         * aqi : 45
         * forecast : [{"date":"10日星期一","high":"高温 36℃","fengli":"微风级","low":"低温 23℃","fengxiang":"南风","type":"晴"},{"date":"11日星期二","high":"高温 36℃","fengli":"微风级","low":"低温 27℃","fengxiang":"南风","type":"多云"},{"date":"12日星期三","high":"高温 35℃","fengli":"微风级","low":"低温 27℃","fengxiang":"南风","type":"多云"},{"date":"13日星期四","high":"高温 36℃","fengli":"微风级","low":"低温 27℃","fengxiang":"南风","type":"多云"},{"date":"14日星期五","high":"高温 35℃","fengli":"微风级","low":"低温 26℃","fengxiang":"南风","type":"雷阵雨"}]
         * ganmao : 各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。
         * wendu : 35
         */
        private YesterdayEntity yesterday;
        private String city;
        private String aqi;
        private List<ForecastEntity> forecast;
        private String ganmao;
        private String wendu;

        public void setYesterday(YesterdayEntity yesterday) {
            this.yesterday = yesterday;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public void setForecast(List<ForecastEntity> forecast) {
            this.forecast = forecast;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }

        public YesterdayEntity getYesterday() {
            return yesterday;
        }

        public String getCity() {
            return city;
        }

        public String getAqi() {
            return aqi;
        }

        public List<ForecastEntity> getForecast() {
            return forecast;
        }

        public String getGanmao() {
            return ganmao;
        }

        public String getWendu() {
            return wendu;
        }

        public class YesterdayEntity {
            /**
             * date : 9日星期日
             * high : 高温 35℃
             * fx : 东北风
             * low : 低温 24℃
             * fl : 微风
             * type : 多云
             */
            private String date;
            private String high;
            private String fx;
            private String low;
            private String fl;
            private String type;

            public void setDate(String date) {
                this.date = date;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public void setFx(String fx) {
                this.fx = fx;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public void setFl(String fl) {
                this.fl = fl;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDate() {
                return date;
            }

            public String getHigh() {
                return high;
            }

            public String getFx() {
                return fx;
            }

            public String getLow() {
                return low;
            }

            public String getFl() {
                return fl;
            }

            public String getType() {
                return type;
            }
        }

        public class ForecastEntity {
            /**
             * date : 10日星期一
             * high : 高温 36℃
             * fengli : 微风级
             * low : 低温 23℃
             * fengxiang : 南风
             * type : 晴
             */
            private String date;
            private String high;
            private String fengli;
            private String low;
            private String fengxiang;
            private String type;

            public void setDate(String date) {
                this.date = date;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public void setFengli(String fengli) {
                this.fengli = fengli;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public void setFengxiang(String fengxiang) {
                this.fengxiang = fengxiang;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDate() {
                return date;
            }

            public String getHigh() {
                return high;
            }

            public String getFengli() {
                return fengli;
            }

            public String getLow() {
                return low;
            }

            public String getFengxiang() {
                return fengxiang;
            }

            public String getType() {
                return type;
            }
        }
    }
}
