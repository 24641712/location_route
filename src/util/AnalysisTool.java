package util;

import routes.GetLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.sql.Timestamp;
//�ٶȵ�ͼ��ӥ�۹�����
public class AnalysisTool {
    private static final double X_PI = Math.PI * 3000.0 / 180.0;
    private static final String entity_name = GetLocation.entity_name;

    // GCJ-02=>BD09 ��������ϵ�����ظ�ʽ��=>�ٶ�����ϵ���ٶȵ�ͼ��ʽ��
    public static double[] gcj2BD09(double glat, double glon) {

        double x = glon;
        double y = glat;
        double[] latlon = new double[2];
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
        latlon[0] = z * Math.sin(theta) + 0.006;
        latlon[1] = z * Math.cos(theta) + 0.0065;
        return latlon;
    }

    //BD09=>GCJ-02 �ٶ�����ϵ���ٶȵ�ͼ��ʽ��=>��������ϵ���ļ���ʽ��
    public static double[] bd092GCJ(double glat, double glon){
        double x = glon - 0.0065;
        double y = glat - 0.006;
        double[] latlon = new double[2];
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
        latlon[0] = z * Math.sin(theta);
        latlon[1] = z * Math.cos(theta);
        return latlon;
    }
     //ʱ���ʽתUnix(String)��ʽ
    public static long timeToUnix(String date, int control) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");// 24Сʱ��

        SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf2= new SimpleDateFormat("dd");
        SimpleDateFormat sdf3= new SimpleDateFormat("HH:mm:ss");
        String str0 = sdf0.format(simpleDateFormat.parse(date));
        String str1 = sdf1.format(simpleDateFormat.parse(date));
        String str2 = sdf2.format(simpleDateFormat.parse(date));
        String str3 = sdf3.format(simpleDateFormat.parse(date));
    if(control != 1){
        date = "2018" + "-"+4+"-"+13+" "+str3;
    }

        long time = simpleDateFormat.parse(date).getTime() / 1000; // �ӿ�Ŀǰֻ֧��2017��3��1�տ�ʼ// ��������˾���

        return time;
    }
     //��unix��ʽ����תdate��ʽ
    public static String unixToTime(long date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");// 24Сʱ��
        String time = simpleDateFormat.format(new java.util.Date(date * 1000));

        return String.valueOf(time);
    }

    public static Timestamp TimetoT(long date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String time = dateFormat.format(new java.util.Date(date * 1000));
        dateFormat.setLenient(false);
        java.util.Date timeDate = dateFormat.parse(time);//util����
        java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());//Timestamp����,timeDat
        return dateTime;
    }


    // ʹ��ӥ�۶Թ켣��chaxun
    public static void selectTrajectory(String ak, int service_id, int Sid, long start_time,long end_time,String entity)
            throws Exception {

        String urlString = "http://yingyan.baidu.com/api/v3/track/gettrack";
    /*    String parm = "ak=" + ak + "&entity_name=" + entity_name + Sid + "&service_id=" + service_id + "&start_time="
                + start_time + "&end_time=" + end_time + "&page_size=" + 1000  + "&is_processed=" + 1
                + "&need_denoise=" + 1 + "&radius_threshold=" + 1 + "&need_vacuate=" + 1 + "&need_mapmatch=" + 1
                + "&radius_threhold=" + 0  + "&transport_mode=" + "walking";*/

        String parm = "ak=" + ak + "&entity_name=" + entity + "&service_id=" + service_id + "&start_time="
                + start_time + "&end_time=" + end_time + "&page_size=" + 2000;

  //      need_denoise=1,radius_threshold=0, need_vacuate=1,need_mapmatch=0, radius_threhold=0,transport_mode=driving;

        sendGet(urlString, parm);//�������ϴ����ٶ�ӥ��

    }
    //
    public static void postFormReq(String urlReq, String param) {
        String res = "";
        try {
            URL url = new URL(urlReq);
            System.out.println(url);
            URLConnection urlConnection = url.openConnection();
            // ����doOutput����Ϊtrue��ʾ��ʹ�ô�urlConnectionд������
            urlConnection.setDoOutput(true);
            // �����д�����ݵ��������ͣ���������Ϊapplication/x-www-form-urlencoded����
            urlConnection.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");

            // �õ���������������
            OutputStreamWriter out = new OutputStreamWriter(
                    urlConnection.getOutputStream());
            // ������д�������Body
            out.write(param);
            out.flush();
            out.close();

            // �ӷ�������ȡ��Ӧ
            java.io.BufferedReader in = new java.io.BufferedReader(
                    new java.io.InputStreamReader(
                            urlConnection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
        } catch (Exception e) {
            System.out.println("error in wapaction,and e is " + e.getMessage());
        }
       System.out.println("�ϴ��켣�����"+res);
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlName = url + "?" + param;
            URL realUrl = new URL(urlName);
            System.out.println(realUrl);
            // �򿪺�URL֮�������
            URLConnection conn = realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");
            // ����ʵ�ʵ�����
            conn.connect();
            // ����BufferedReader����������ȡURL����Ӧ
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }

        } catch (Exception e) {
        System.out.println("����GET��������쳣��" + e);
        e.printStackTrace();
    }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }

}
