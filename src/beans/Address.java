package beans;

public class Address {

    private String LONG;//经度
    private String lat;//纬度
    private String province;//省
    private String city;//市
    private String area;//区
    private String POI;//poi信息
    private String date;//停留时间年月日
    private String time;//停留时间时分秒

    public Address() {

    }

    public Address(String province, String city, String area, String POI, String date, String time) {
        this.province = province;
        this.city = city;
        this.area = area;
        this.POI = POI;
        this.date = date;
        this.time = time;
    }

    public Address(String LONG, String lat, String date, String time) {
        this.LONG = LONG;
        this.lat = lat;
        this.date = date;
        this.time = time;
    }


    public String getLONG() {
        return LONG;
    }

    public void setLONG(String lONG) {
        LONG = lONG;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;
    }


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPOI() {
        return POI;
    }

    public void setPOI(String pOI) {
        POI = pOI;
    }


}
