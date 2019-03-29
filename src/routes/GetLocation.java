package routes;

import beans.*;
import dao.TrajectoryInfoMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import track.AddPointsRequest;
import util.AnalysisTool;
import util.FKSqlSessionFactory;

import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetLocation {

    public static final int pTotal = 1;// 本次实验一共需要分析和查询多少人的数据
    protected static final int pNum = 20;// 每个人采集多少个文件的数据
    private static final int pTime = 1;// 每个人采集数据的时间间隔，在文件中是隔pTime行取一个数据点
    private static final String start_time = "2018-04-13 00:00:15";//一天中的起点
    private static final String end_time = "2018-04-14 00:00:15";//一天中的终点
    private static final int stay_time = 20;// 停留时间 单位：秒（选填）
    private static final int stay_radius = 30;// 停留半径 （选填）
    private static final String fileAddress = "F:\\GeolifeTrajectoriesData\\";
    private static final String writefileAddress = "F:\\GeolifeTrajectoriesData\\Result\\结果.txt";
    private static final String writefileAddressPOI = "F:\\GeolifeTrajectoriesData\\Result\\POI结果.txt";
    private static final String ak = "zSKAToLRDyF9yhL2WV1X4lrvo8lBmQqL";// 读取多少人的数据
    private static final int service_id = 200347;
    public static final String entity_name = "Person";
    public static final double pointDistance = 5;
    public static final double minSupportRate = 0.8;
    public static final int maxTimeThreshold = 1200;

    //通过此函数讲获取到的停留点信息转换为对应的poi信息，默认一个人只有一条轨迹，以每条轨迹为单位进行处理
    public static void getAddressPoi(ArrayList<AnalysisData> points, int people, int Tra) throws ParseException {
        try {
            FileWriter bw = new FileWriter(writefileAddressPOI, true);
            bw.write("************************The Person_" + people + "'s trajectory" + Tra + "************************\r\n");
            String add = "";

            for (AnalysisData pointTmp : points) {// 对得到的json进行处理并存储
                TrackPoint point = new TrackPoint();
                double[] a = AnalysisTool.gcj2BD09(pointTmp.getLatitude(), pointTmp.getLongitude());
                add = getCoordinate(String.valueOf(a[1]), String.valueOf(a[0]));

                JSONObject jsonObject = JSONObject.fromObject(add);
                JSONArray jsonArray = JSONArray.fromObject(jsonObject
                        .getString("addrList"));
                JSONObject j_2 = JSONObject.fromObject(jsonArray.get(0));
                String allAdd = j_2.getString("admName");
                String poiName = j_2.getString("name");
                String arr[] = allAdd.split(",");
                // 1省 2市 3区 4poi信息 5年月日 6 时分秒
                //  AnalysisData point = new AnalysisData();
                System.out.println(arr[0] + arr[1] + arr[2] + poiName
                        + "  " + pointTmp.getStart_time() + " " + pointTmp.getEnd_time() + "  " + pointTmp.getStay_time());
                String data = arr[0] + arr[1] + arr[2] + poiName
                        + "  " + AnalysisTool.unixToTime(Long.parseLong(pointTmp.getStart_time())) + " " + AnalysisTool.unixToTime(Long.parseLong(pointTmp.getEnd_time())) + "  " + pointTmp.getStay_time();
                bw.write(data + "\r\n");
                bw.flush();
            }
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int minSupport;


    //插入一条轨迹数据进入数据库（MyBatis数据存储）
    public static void database(ArrayList<TrackPoint> points) throws IOException {
        SqlSession sqlSession = null;
        try {
            sqlSession = FKSqlSessionFactory.opensqlSession();
            TrajectoryInfoMapper mapper = sqlSession.getMapper(TrajectoryInfoMapper.class);
            for (TrackPoint point : points) {
                Boolean bool = mapper.insertTrajectoryInfo(point);  //向数据库插入单条数据
            }
            sqlSession.commit();  //数据库提交
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    // 经纬度转换为地址坐标
    public static String getCoordinate(String log, String lat) {
        // lat 小 log 大
        // 参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
        String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat
                + "," + log + "&type=010";
        String res = "";
        try {
            URL url = new URL(urlString);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url
                    .openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(conn.getInputStream(),
                            "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
        // System.out.println(res);
        return res;
    }

    // 从txt文件中读取点坐标
    public static ArrayList<Address> readData(String fileAddress) {
        ArrayList<Address> points = new ArrayList<Address>();
        int count = 0;

		/* 读取数据 */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(fileAddress)), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if (count % pTime == 0 && count >= 0) {// 每隔5分钟取一个点
                    String[] names = lineTxt.split(",");
                    Address point = new Address(names[0], names[1], names[5],
                            names[6]);// 0经度和1纬度 5年月日 6 时分秒
                    points.add(point);
                }
                count++;
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }

        return points;
    }

    // 计算两个时间点的差值 返回时间单位为分
    public static long calculationTime(String time1, String time2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long days = 0;
        long minutes = 0;
        long hours = 0;
        try {
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别

            days = diff / (1000 * 60 * 60 * 24);
            hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);

        } catch (Exception e) {
            System.out.println("calculationTime have ERROR!");
        }

        return minutes;
    }

    public static void cleanFile(String adress) {
        /* 输出数据 */

        try {
            FileWriter bw = new FileWriter(adress);
            bw.flush();

            bw.close();
        } catch (Exception e) {
            System.err.println("write errors :" + e);
        }
    }

    //调用鹰眼api查询上传上去的轨迹数据
    public static void Select(int peple) throws NumberFormatException, ParseException,
            Exception {
        /* 输出数据 */
        for (int i = 0; i <peple; i++) {
            for (int j = 1; j <= pNum; j++) {
                //AnalysisTool.timeToUnix(start_time, 1)将时间格式转化成Unix(String)格式上传到百度鹰眼
                AnalysisTool.selectTrajectory(ak, service_id, i,
                        AnalysisTool.timeToUnix(start_time, 1), AnalysisTool.timeToUnix(end_time, 1), entity_name + "_" + i + "_" + j);
            }
        }
    }


    public static void writeFile(int Peaple, LBSTraceClient client) throws Exception {
        ArrayList<TrackPoint> pointsAddress = new ArrayList<TrackPoint>();
        ;// 汉字地址结果集
        ArrayList<Address> points = null;
        String address = "";
        long require_id = 0;
        int sum = 0;
        for (int i = 0; i < Peaple; i++) {
            System.out.println("************************这是第" + i + "人的轨迹数据************************");
            for (int j = 1; j <= pNum; j++) {// 每个人读取文件的数目

                address = fileAddress + "00" + i + "\\Trajectory\\"
                        + j + ".plt";//有方法会将各种文件格式转为txt文件格式
                require_id++;
                points = readData(address);// 调用api阅读每个人中的每个轨迹文件，取数据点到points。

                /*
                  AnalysisTool.timeToUnix(pointTmp.getDate()
                  //AnalysisTool的timeToUnix（）方法将时间格式转成Unix格式
                 */
                for (Address pointTmp : points) {// 读取txt文件后得到节点数据集，对得到的json进行处理并存储
                    double[] a = AnalysisTool.gcj2BD09(Double.parseDouble(pointTmp.getLat()), Double.parseDouble(pointTmp.getLONG()));
                    TrackPoint trackPoint = new TrackPoint(new LatLng(a[1], a[0]), CoordType.bd09ll, 30,
                            AnalysisTool.timeToUnix(pointTmp.getDate() + " " + pointTmp.getTime(), 0), 4, 20, 40, null, null);
                    sum++;
                    pointsAddress.add(trackPoint);
                    if (sum % 100 == 0) {   /////满足100个数据点则上传数据
                        Map<String, List<TrackPoint>> trackPointMap = new HashMap<String, List<TrackPoint>>();//一个人的轨迹集合是一个任务
                        trackPointMap.put("Person_" + i + "_" + j, pointsAddress);
                        AddPointsRequest request = new AddPointsRequest(require_id, ak, service_id, trackPointMap);//每100个点为一个线程，放入阻塞队列
                        client.addPoints(request);// 批量添加轨迹点
                        pointsAddress = new ArrayList<TrackPoint>();//将轨迹线程池初始化
                    }
                }
            }

        }
    }

}