import com.google.gson.Gson;
import pojp.userinfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class etlsqls {


    public static Gson gson = new Gson();

    public static void main(String[] args) {
        userinfo2SQL();
    }


    public static String userinfo2SQL(){
        StringBuffer msg = new StringBuffer();

        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q= SELECT \n" +
                "              uid, \n" +
                "                  first_visit_source,\n" +
                "                  EPOCH_TO_TIMESTAMP(register_time ) register_time  ,\n" +
                "                   kyc_state, ekyc_state,\n" +
                "                  case when first_recharge_time is null then '1970-01-01 00:00:00' else substr(cast(EPOCH_TO_TIMESTAMP(first_recharge_time) as string),1,19) end first_recharge_time , \n" +
                "                 case when last_recharge_time  is null then '1970-01-01 00:00:00' else  substr(cast(EPOCH_TO_TIMESTAMP(last_recharge_time ) as string),1,19) end last_recharge_time  ,\n" +
                "                  case when first_order_time  is null then '1970-01-01 00:00:00' else substr(cast( EPOCH_TO_TIMESTAMP(first_order_time) as string),1,19) end first_order_time,\n" +
                "                  case when last_order_time  is null then '1970-01-01 00:00:00' else   substr(cast(EPOCH_TO_TIMESTAMP( last_order_time)  as string),1,19) end last_order_time,\n" +
                "                  case when first_winning_time  is null then '1970-01-01 00:00:00' else  substr(cast(EPOCH_TO_TIMESTAMP(first_winning_time)  as string),1,19) end first_winning_time,\n" +
                "               case when first_withdraw_time  is null then '1970-01-01 00:00:00' else  substr(cast(EPOCH_TO_TIMESTAMP(first_withdraw_time) as string),1,19) end first_withdraw_time  ,\n" +
                "                  case when last_withdraw_time  is null then '1970-01-01 00:00:00' else    substr(cast(EPOCH_TO_TIMESTAMP(last_withdraw_time ) as string),1,19) end last_withdraw_time  \n" +
                "               FROM users\n" +
                "                WHERE first_visit_source is not null" +
                "WHERE first_visit_source is not null  ",
                "--data-urlencode", "format=json",
        };


        // 输出 逻辑
        try {
            // 执行 curl 命令
            Process process = Runtime.getRuntime().exec(command);
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int databaseoutputcount = 0;
            while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 打印输出
                userinfo person = gson.fromJson(line, userinfo.class);
                databaseoutputcount ++;
                if(databaseoutputcount%10==0){
                    System.out.println(" 目前是"+databaseoutputcount);
                    msg.append(" 目前是"+databaseoutputcount+"\n");

                }
            }
            // 等待命令执行完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Curl command executed successfully ！");
                return msg + "Curl command executed successfully ！";
            } else {
                System.out.println("Curl command failed with exit code: " + exitCode);
                return msg + "Curl command failed with exit code: " + exitCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return msg + e.getMessage();
        }

    }

//
//    public static void output2SQL(){
//
//
//
//    }







}
