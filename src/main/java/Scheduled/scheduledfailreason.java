package Scheduled;
import util.etlsqls;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class scheduledfailreason {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 每 2 秒执行一次任务
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("定时任务执行：" + System.currentTimeMillis());
            try {
                etlsqls.fail_reason_monitoring();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 15, TimeUnit.MINUTES);


    }
}
