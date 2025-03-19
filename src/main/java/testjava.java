import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.Gson;
import pojp.orderwin0122;
import pojp.orderwinnowin;
import pojp.usercnt;
import pojp.userinfo;

public class testjava {

    public static void main(String[] args) {
        String inputFilePath = "/Users/Shared/Desktop/input.txt";  // 输入文件路径
        String outputFilePath = "/Users/Shared/output2.csv"; // 输出文件路径
        String output_unmatchuserid = "/Users/Shared/output_unmatchuserid.txt"; // 输出文件路径


        try {
            Gson gson = new Gson();
            // 读取文件的所有行
            System.out.println(Paths.get(inputFilePath));
            List<String> lines = new ArrayList<>();
            if(Files.exists(Paths.get(inputFilePath))){
                lines = Files.readAllLines(Paths.get(inputFilePath));
            }

            //input Set
            HashSet<String> inputSet = new HashSet<>();
            // database Set
            HashSet<String> outputSet = new HashSet<>();
            // 创建一个输出流来写入修改后的内容
            System.out.println("开始整合数据！！！");
            int inputcount = 0;
            for (String line : lines) {
                inputSet.add(line);
                // 写入到输出文件
                inputcount  ++;

            }
            System.out.println("输入数据："+inputcount+"条！！！");
            Iterator<String> iterator = inputSet.iterator();
            int outputcount = 0;
            StringBuffer stringBuffer = new StringBuffer();
            HashMap<Integer,StringBuffer> hashMap = new HashMap<>();
            int batchId = 0;

            while(iterator.hasNext()){
              String element = iterator.next();
                // 给每行两边加上分号
                String modifiedLine = "'" + element + "',";
                stringBuffer.append(modifiedLine+"\n");
                outputcount ++;
                if(outputcount%2000==0) {
                    batchId ++;
                    hashMap.put(batchId,stringBuffer);
                    stringBuffer =  new StringBuffer();
                }
                if(outputcount==inputSet.size()){
                    batchId ++;
                    hashMap.put(batchId,stringBuffer);
                    stringBuffer =  new StringBuffer();
                }

            }
            int diff = inputcount-outputcount;
            System.out.println("输出数据："+outputcount+"条！！！");
            System.out.println("其中重复数据："+diff+"条！！！");
            // 关闭文件写入流

            System.out.println("清洗输入数据: " + outputFilePath);

            System.out.println("执行脚本部分: ==================================" );

            int count = 0;

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
//            writer.write("uid,first_visit_source,register_time,kyc_state,ekyc_state,first_recharge_time,last_recharge_time,first_order_time,last_order_time,first_recharge_amount,first_winning_time,first_withdraw_time,last_withdraw_time");
//            writer.newLine();

            writer.write("time , cnt");
            writer.newLine();

            BufferedWriter writerunmatchuserid = new BufferedWriter(new FileWriter(output_unmatchuserid));
            if(hashMap.isEmpty()){

                String[] command = {
                        "curl",
                        "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                        "-X", "POST",
                        "--data-urlencode", "q= SELECT \n" +
                        "    uid, \n" +
                        "    first_visit_source,\n" +
                        "    register_time,\n" +
                        "    kyc_state, ekyc_state,\n" +
                        "     first_recharge_time, last_recharge_time ,\n" +
                        "   first_order_time, last_order_time , \n" +
                        "    first_recharge_amount , first_winning_time , \n" +
                        "    first_withdraw_time , last_withdraw_time \n" +
                        "FROM users\n" +
                        "WHERE first_visit_source is not null ",
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

                        }


                        outputSet.add(person.uid);
//                        writer.write(person.uid+","+person.first_order_time+","+person.first_order_amount+","+person.lottery_type+","+person.register_time);
                        writer.write(person.uid+","+person.first_visit_source+","+person.register_time);
//                        writer.write(person.uid+","+person.register_time);
                        writer.newLine();
                    }

                    // 等待命令执行完成
                    int exitCode = process.waitFor();
                    if (exitCode == 0) {
                        count ++;
                        System.out.println("Curl command executed successfully 第"+count+"批次完成！");
                    } else {
                        System.out.println("Curl command failed with exit code: " + exitCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                for(Map.Entry<Integer, StringBuffer> entry : hashMap.entrySet())
                {
                    String SQLString = entry.getValue().toString().substring(0,entry.getValue().toString().length()-2);
//                System.out.println("Key: " + entry.getKey() + ", Value: " +SQLString );
                    //  login_result,lottery_order_result,recharge_result
                    String[] command = {
                            "curl",
                            "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                            "-X", "POST",
                            "--data-urlencode", "q= SELECT count(distinct users.uid) usernum,time data1 from users join ( select user_id,substr(cast(time as string),1,10) time from events where event= 'lottery_order_result' and is_success  = 1 and time>'2025-01-23 00:00:00' ) a on users.id = a.user_id where users.uid in ( "+SQLString +" ) group by time  " ,
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
                            usercnt person = gson.fromJson(line, usercnt.class);
//                            outputSet.add(person.date);
                            writer.write(person.usernum+","+person.data1);
                            writer.newLine();
                        }

                        // 等待命令执行完成
                        int exitCode = process.waitFor();
                        if (exitCode == 0) {
                            count ++;
                            System.out.println("Curl command executed successfully 第"+count+"批次完成！");
                        } else {
                            System.out.println("Curl command failed with exit code: " + exitCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            writer.close();
            inputSet.removeAll(outputSet);
            Iterator<String> inSet = inputSet.iterator();
            System.out.println("没有匹配上的UID有：====================================");
            int unmatchinput = 0;
            while(inSet.hasNext()){
                unmatchinput++;
               String unmatchid =  inSet.next();
                System.out.println(unmatchid);
                writerunmatchuserid.write(unmatchid+"\n");
            }
            System.out.println("没有匹配上的有："+unmatchinput);
            writerunmatchuserid.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
