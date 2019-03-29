package core;

/**
 * url域名
 * 
 * @author baidu
 *
 */
public class UrlDomain {

    /*
         http://yingyan.baidu.com/api/v3/
         analysis/staypoint?
         ak=用户的ak&
         service_id=用户的service_id&
         entity_name=car_plate&
         start_time=1467734400&
         end_time=1467817200&
         stay_time=300

     */

    public static final String YINGYAN_HTTP_URL = "http://yingyan.baidu.com/api/v3/";

    // public static final String YINGYAN_HTTP_URL =
    // "http://cp01-rdqa-dev403-huangshaowen01.epc.baidu.com:8070/trace/v3/";

    public static final String YINGYAN_HTTPS_URL = "https://yingyan.baidu.com/api/v3/";

    public static final String ACTION_ADD_POINT = "track/addpoint";//单个点上传

    public static final String ACTION_ADD_POINTS = "track/addpoints";//多个点上传（100个点）

}
