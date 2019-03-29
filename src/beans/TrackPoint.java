package beans;

import java.sql.Timestamp;
import java.util.Map;

/**
 * 轨迹点信息
 *从一个文件中获取一条轨迹
 * @author baidu
 */
public class TrackPoint extends Point {

    /**
     * 对象数据名称
     */
    private String objectKey;
    /**
     * 轨迹地址信息
     */
    private String adress;
    /**
     * 轨迹编号
     */
    private int pointId;
    private int traId;
    private int userId;
    private Timestamp startTime;// 开始时间（必填）
    private Timestamp endTime;// 结束时间（必填）
    private String timeName;//时间片名称
    private int stayTime;// 停留时间 单位：秒（选填）
    private int localType;// 停留区域类型
    private int timeId;//时间片类型id
    /**
     * track自定义字段
     */
    private Map<String, String> columns;

    /**
     * 获取对象数据名称
     *
     * @return
     */
    public String getObjectKey() {
        return objectKey;
    }

    /**
     * 设置对象数据名称
     *
     * @param objectKey
     */
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    /**
     * 获取自定义属性
     *
     * @return
     */
    public Map<String, String> getColumns() {
        return columns;
    }

    /**
     * 设置自定义属性
     *
     * @param columns
     */
    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public TrackPoint() {
        super();
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getTraId() {
        return traId;
    }

    public void setTraId(int traId) {
        this.traId = traId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public int getLocalType() {
        return localType;
    }

    public void setLocalType(int localType) {
        this.localType = localType;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    /**
     * @param location  经纬度坐标
     * @param coordType 坐标类型
     * @param radius    定位精度
     * @param locTime   定位时间
     * @param direction 方向
     * @param speed     速度
     * @param height    高度
     * @param objectKey 对象数据名称
     * @param columns   自定义属性
     * @param adress
     * @param traId     自定义属性
     */
    public TrackPoint(LatLng location, CoordType coordType, double radius, long locTime, int direction, double speed,
                      double height, String objectKey, Map<String, String> columns) {
        super(location, coordType, radius, locTime, direction, speed, height);
        this.objectKey = objectKey;
        this.columns = columns;
   /*     this.adress = adress;
        this.traId = traId;*/
    }

    @Override
    public String toString() {
        return "TrackPoint{" +
                "objectKey='" + objectKey + '\'' +
                ", adress='" + adress + '\'' +
                ", pointId=" + pointId +
                ", traId=" + traId +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timeName='" + timeName + '\'' +
                ", stayTime=" + stayTime +
                ", localType=" + localType +
                ", timeId=" + timeId +
                ", columns=" + columns +
                '}';
    }
}
