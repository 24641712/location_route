package dao;

import beans.TrackPoint;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ly on 2018/4/23.
 */
public interface TrajectoryInfoMapper {
    Boolean insertTrajectoryInfo(TrackPoint traData);  //将轨迹点加入数据库

    ArrayList<TrackPoint> getTrajectoryInfo(@Param("userId") Integer userId,@Param("traId") Integer traId);
    ArrayList<TrackPoint> getTrajectoryDataByUserid(Integer userId);
    List<TrackPoint> getTimeInfo();
}
