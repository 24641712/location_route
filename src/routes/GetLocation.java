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

    public static final int pTotal = 1;// ����ʵ��һ����Ҫ�����Ͳ�ѯ�����˵�����
    protected static final int pNum = 20;// ÿ���˲ɼ����ٸ��ļ�������
    private static final int pTime = 1;// ÿ���˲ɼ����ݵ�ʱ���������ļ����Ǹ�pTime��ȡһ�����ݵ�
    private static final String start_time = "2018-04-13 00:00:15";//һ���е����
    private static final String end_time = "2018-04-14 00:00:15";//һ���е��յ�
    private static final int stay_time = 20;// ͣ��ʱ�� ��λ���루ѡ�
    private static final int stay_radius = 30;// ͣ���뾶 ��ѡ�
    private static final String fileAddress = "F:\\GeolifeTrajectoriesData\\";
    private static final String writefileAddress = "F:\\GeolifeTrajectoriesData\\Result\\���.txt";
    private static final String writefileAddressPOI = "F:\\GeolifeTrajectoriesData\\Result\\POI���.txt";
    private static final String ak = "zSKAToLRDyF9yhL2WV1X4lrvo8lBmQqL";// ��ȡ�����˵�����
    private static final int service_id = 200347;
    public static final String entity_name = "Person";
    public static final double pointDistance = 5;
    public static final double minSupportRate = 0.8;
    public static final int maxTimeThreshold = 1200;

    //ͨ���˺�������ȡ����ͣ������Ϣת��Ϊ��Ӧ��poi��Ϣ��Ĭ��һ����ֻ��һ���켣����ÿ���켣Ϊ��λ���д���
    public static void getAddressPoi(ArrayList<AnalysisData> points, int people, int Tra) throws ParseException {
        try {
            FileWriter bw = new FileWriter(writefileAddressPOI, true);
            bw.write("************************The Person_" + people + "'s trajectory" + Tra + "************************\r\n");
            String add = "";

            for (AnalysisData pointTmp : points) {// �Եõ���json���д����洢
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
                // 1ʡ 2�� 3�� 4poi��Ϣ 5������ 6 ʱ����
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


    //����һ���켣���ݽ������ݿ⣨MyBatis���ݴ洢��
    public static void database(ArrayList<TrackPoint> points) throws IOException {
        SqlSession sqlSession = null;
        try {
            sqlSession = FKSqlSessionFactory.opensqlSession();
            TrajectoryInfoMapper mapper = sqlSession.getMapper(TrajectoryInfoMapper.class);
            for (TrackPoint point : points) {
                Boolean bool = mapper.insertTrajectoryInfo(point);  //�����ݿ���뵥������
            }
            sqlSession.commit();  //���ݿ��ύ
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    // ��γ��ת��Ϊ��ַ����
    public static String getCoordinate(String log, String lat) {
        // lat С log ��
        // ��������: γ��,���� type 001 (100�����·��010����POI��001������ַ��111����ͬʱ��ʾǰ����)
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

    // ��txt�ļ��ж�ȡ������
    public static ArrayList<Address> readData(String fileAddress) {
        ArrayList<Address> points = new ArrayList<Address>();
        int count = 0;

		/* ��ȡ���� */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(fileAddress)), "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if (count % pTime == 0 && count >= 0) {// ÿ��5����ȡһ����
                    String[] names = lineTxt.split(",");
                    Address point = new Address(names[0], names[1], names[5],
                            names[6]);// 0���Ⱥ�1γ�� 5������ 6 ʱ����
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

    // ��������ʱ���Ĳ�ֵ ����ʱ�䵥λΪ��
    public static long calculationTime(String time1, String time2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long days = 0;
        long minutes = 0;
        long hours = 0;
        try {
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d1.getTime() - d2.getTime();// �����õ��Ĳ�ֵ��΢�뼶��

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
        /* ������� */

        try {
            FileWriter bw = new FileWriter(adress);
            bw.flush();

            bw.close();
        } catch (Exception e) {
            System.err.println("write errors :" + e);
        }
    }

    //����ӥ��api��ѯ�ϴ���ȥ�Ĺ켣����
    public static void Select(int peple) throws NumberFormatException, ParseException,
            Exception {
        /* ������� */
        for (int i = 0; i <peple; i++) {
            for (int j = 1; j <= pNum; j++) {
                //AnalysisTool.timeToUnix(start_time, 1)��ʱ���ʽת����Unix(String)��ʽ�ϴ����ٶ�ӥ��
                AnalysisTool.selectTrajectory(ak, service_id, i,
                        AnalysisTool.timeToUnix(start_time, 1), AnalysisTool.timeToUnix(end_time, 1), entity_name + "_" + i + "_" + j);
            }
        }
    }


    public static void writeFile(int Peaple, LBSTraceClient client) throws Exception {
        ArrayList<TrackPoint> pointsAddress = new ArrayList<TrackPoint>();
        ;// ���ֵ�ַ�����
        ArrayList<Address> points = null;
        String address = "";
        long require_id = 0;
        int sum = 0;
        for (int i = 0; i < Peaple; i++) {
            System.out.println("************************���ǵ�" + i + "�˵Ĺ켣����************************");
            for (int j = 1; j <= pNum; j++) {// ÿ���˶�ȡ�ļ�����Ŀ

                address = fileAddress + "00" + i + "\\Trajectory\\"
                        + j + ".plt";//�з����Ὣ�����ļ���ʽתΪtxt�ļ���ʽ
                require_id++;
                points = readData(address);// ����api�Ķ�ÿ�����е�ÿ���켣�ļ���ȡ���ݵ㵽points��

                /*
                  AnalysisTool.timeToUnix(pointTmp.getDate()
                  //AnalysisTool��timeToUnix����������ʱ���ʽת��Unix��ʽ
                 */
                for (Address pointTmp : points) {// ��ȡtxt�ļ���õ��ڵ����ݼ����Եõ���json���д����洢
                    double[] a = AnalysisTool.gcj2BD09(Double.parseDouble(pointTmp.getLat()), Double.parseDouble(pointTmp.getLONG()));
                    TrackPoint trackPoint = new TrackPoint(new LatLng(a[1], a[0]), CoordType.bd09ll, 30,
                            AnalysisTool.timeToUnix(pointTmp.getDate() + " " + pointTmp.getTime(), 0), 4, 20, 40, null, null);
                    sum++;
                    pointsAddress.add(trackPoint);
                    if (sum % 100 == 0) {   /////����100�����ݵ����ϴ�����
                        Map<String, List<TrackPoint>> trackPointMap = new HashMap<String, List<TrackPoint>>();//һ���˵Ĺ켣������һ������
                        trackPointMap.put("Person_" + i + "_" + j, pointsAddress);
                        AddPointsRequest request = new AddPointsRequest(require_id, ak, service_id, trackPointMap);//ÿ100����Ϊһ���̣߳�������������
                        client.addPoints(request);// ������ӹ켣��
                        pointsAddress = new ArrayList<TrackPoint>();//���켣�̳߳س�ʼ��
                    }
                }
            }

        }
    }

}