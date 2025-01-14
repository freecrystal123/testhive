import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.Gson;
import pojp.orderwinnowin;

public class testjava {

    public static void main(String[] args) {
        String inputFilePath = "/Users/zhihuachai/Desktop/input.txt";  // 输入文件路径
        String outputFilePath = "/Users/zhihuachai/Downloads/output2.csv"; // 输出文件路径
        String output_unmatchuserid = "/Users/zhihuachai/Desktop/output_unmatchuserid.txt"; // 输出文件路径


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
            writer.write("uid,lottery_type,order_id,investment_amount,lottery_entries,order_time,series_number,winning_flag,winning_amount,winning_time");
            writer.newLine();


            BufferedWriter writerunmatchuserid = new BufferedWriter(new FileWriter(output_unmatchuserid));
            if(hashMap.isEmpty()){

                String[] command = {
                        "curl",
                        "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                        "-X", "POST",
                        "--data-urlencode", "q=select \n" +
                        "uid,\n" +
                        "lottery_type,\n" +
                        "order_id,\n" +
                        "investment_amount,\n" +
                        "lottery_entries , \n" +
                        "order_time,\n" +
                        "series_number,\n" +
                        "case when uid is not null and winning_amount is null then 'no winning' else 'winning' end winning_flag,\n" +
                        "winning_amount,\n" +
                        "winning_time\n" +
                        "from (\n" +
                        "select users.uid, events.* from (\n" +
                        "select \n" +
                        "winning.winning_amount,\n" +
                        "winning.winning_time,\n" +
                        "ordered.lottery_type,\n" +
                        "ordered.order_id,\n" +
                        "ordered.user_id,\n" +
                        "ordered.investment_amount,\n" +
                        "ordered.lottery_entries,\n" +
                        "ordered.series_number,\n" +
                        "ordered.order_time from (\n" +
                        " select \n" +
                        " order_id,\n" +
                        " user_id,\n" +
                        " winning_amount,\n" +
                        " `time` winning_time\n" +
                        " from events\n" +
                        " where event = 'winning_detail') winning\n" +
                        " right join (\n" +
                        " select  order_id,\n" +
                        " user_id,\n" +
                        " estimated_price investment_amount,\n" +
                        " lottery_type,\n" +
                        " lottery_entries,\n" +
                        " `time` order_time,\n" +
                        " case when time >= '2024-12-14 21:00:00' and  time <= '2024-12-28 19:00:00' then '20241228' \n" +
                        "      when time < '2024-12-14 19:00:00'  then '20241214 ' \n" +
                        "      when time > '2024-12-28 21:00:00' and  time <= '2025-01-11 19:00:00'  then '20250111' end  series_number\n" +
                        " from events\n" +
                        " where event = 'lottery_order_result'\n" +
                        " and is_success = 1    ) ordered \n" +
                        " on winning.order_id = ordered.order_id ) events \n" +
                        " left join users on events.user_id = users.id ) abc where uid is not null",
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
                        orderwinnowin person = gson.fromJson(line, orderwinnowin.class);
                        databaseoutputcount ++;
                        if(databaseoutputcount%10==0){
                            System.out.println(" 目前是"+databaseoutputcount);

                        }

                        outputSet.add(person.uid);
//                        writer.write(person.uid+","+person.first_order_time+","+person.first_order_amount+","+person.lottery_type+","+person.register_time);
                        writer.write(person.uid+","+person.lottery_type+","+person.order_id+","+person.investment_amount+","+person.lottery_entries+","+person.order_time+","+person.series_number+","+person.winning_flag + "," + person.winning_amount + "," + person.winning_time);
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
                    String[] command = {
                            "curl",
                            "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                            "-X", "POST",
                            "--data-urlencode", "q=select \n" +
                            "uid,\n" +
                            "lottery_type,\n" +
                            "order_id,\n" +
                            "investment_amount,\n" +
                            "lottery_entries , \n" +
                            "order_time,\n" +
                            "series_number,\n" +
                            "case when uid is not null and winning_amount is null then 'no winning' else 'winning' end winning_flag,\n" +
                            "winning_amount,\n" +
                            "winning_time\n" +
                            "from (\n" +
                            "select users.uid, events.* from (\n" +
                            "select \n" +
                            "winning.winning_amount,\n" +
                            "winning.winning_time,\n" +
                            "ordered.lottery_type,\n" +
                            "ordered.order_id,\n" +
                            "ordered.user_id,\n" +
                            "ordered.investment_amount,\n" +
                            "ordered.lottery_entries,\n" +
                            "ordered.series_number,\n" +
                            "ordered.order_time from (\n" +
                            " select \n" +
                            " order_id,\n" +
                            " user_id,\n" +
                            " winning_amount,\n" +
                            " `time` winning_time\n" +
                            " from events\n" +
                            " where event = 'winning_detail') winning\n" +
                            " right join (\n" +
                            " select  order_id,\n" +
                            " user_id,\n" +
                            " estimated_price investment_amount,\n" +
                            " lottery_type,\n" +
                            " lottery_entries,\n" +
                            " `time` order_time,\n" +
                            " series_number\n" +
                            " from events\n" +
                            " where event = 'lottery_order_result'\n" +
                            " and is_success = 1   ) ordered \n" +
                            " on winning.order_id = ordered.order_id ) events \n" +
                            " left join users on events.user_id = users.id ) abc where uid is not null\n",
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
//                    writer.write("uid , first_order_time,first_order_amount,lottery_type,register_time");
//                    writer.write("uid , register_time,order_id,lottery_type,time");

                        while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 打印输出
                            orderwinnowin person = gson.fromJson(line, orderwinnowin.class);
                            outputSet.add(person.uid);
//                        writer.write(person.uid+","+person.first_order_time+","+person.first_order_amount+","+person.lottery_type+","+person.register_time);
                            writer.write(person.uid+","+person.lottery_type+","+person.order_id+","+person.investment_amount+","+person.lottery_entries+","+person.order_time+","+","+person.series_number+","+person.winning_flag + "," + person.winning_amount + "," + person.winning_time);
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
