package routes;

import beans.OnUploadListener;
import track.UploadResponse;
import util.AnalysisTool;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ly on 2018/3/6.
 */
public class Experiment {


    private static AtomicInteger successCounter = new AtomicInteger();

    private static AtomicInteger failedCounter = new AtomicInteger();

    public static void main(String[] args) throws Exception {
        // System.out.println("************************实验正式开始**********************");
      upload();//上传轨迹
        GetLocation.Select(GetLocation.pTotal);
//         System.out.println(AnalysisTool.unixToTime(String.valueOf(1525852167)));
//         System.out.println(AnalysisTool.unixToTime(String.valueOf(1525852167)));
         System.out.println("************************实验结束************************");
    }



    public static void upload() throws Exception {

        LBSTraceClient client = LBSTraceClient.getInstance();
        client.init();
        client.start();   //消费者队列，取数据上传到百度鹰眼

        client.registerUploadListener(new OnUploadListener() {

            @Override
            public void onSuccess(long responseId) {
                System.out.println("成功 : " + responseId + ", successCounter : " + successCounter.incrementAndGet());
            }

            @Override
            public void onFailed(UploadResponse response) {
                System.err.println("失败: " + response.getResponseID() + ", failedCounter : "
                        + failedCounter.incrementAndGet() + ", " + response);
            }
        });

        GetLocation.writeFile(GetLocation.pTotal, client);//  生产者队列 ，将数据放入线程池
    }
}
