import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import com.google.gson.Gson;
public class testjava {

    public static void main(String[] args) {
        String inputFilePath = "/Users/zhihuachai/Desktop/input.txt";  // 输入文件路径
        String outputFilePath = "/Users/zhihuachai/Desktop/output2.csv"; // 输出文件路径
        String output_unmatchuserid = "/Users/zhihuachai/Desktop/output_unmatchuserid.txt"; // 输出文件路径


        try {
            Gson gson = new Gson();
            // 读取文件的所有行
            List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
            //input Set
            HashSet<String> inputSet = new HashSet<>();
            // database Set
            HashSet<String> outputSet = new HashSet<>();
            // 创建一个输出流来写入修改后的内容
//            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
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
//                writer.write(modifiedLine);
//                writer.newLine(); // 换行
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
//            System.out.println(stringBuffer);
            int diff = inputcount-outputcount;
            System.out.println("输出数据："+outputcount+"条！！！");
            System.out.println("其中重复数据："+diff+"条！！！");
            // 关闭文件写入流

            System.out.println("清洗输入数据: " + outputFilePath);

            System.out.println("执行脚本部分: ==================================" );

//            String bak1 =  "--data-urlencode", "q=select u.uid,\n" +
//                    "min(FROM_UNIXTIME(cast(floor(u.first_order_time/1000) as int), 'yyyy-MM-dd HH:mm:ss')) first_order_time,\n" +
//                    "FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') register_time,\n" +
//                    "u.first_order_amount,\n" +
//                    "e.lottery_type  from (\n" +
//                    "select * from users \n" +
//                    "where uid in (\n" +
//                    SQLString+
//                    ")) u left join (select * from (\n" +
//                    "  select \n" +
//                    "   user_id,\n" +
//                    "   lottery_type,\n" +
//                    "   UNIX_TIMESTAMP(time) time,\n" +
//                    "   CASE \n" +
//                    "        WHEN lottery_type REGEXP '^[0-9]+$' THEN 'Numeric'\n" +
//                    "        ELSE 'Non-Numeric' end lottery,\n" +
//                    "   CASE \n" +
//                    "        WHEN lottery_type REGEXP '\\\\s' THEN 'NoLuckyDay'\n" +
//                    "        ELSE '' end lottery2\n" +
//                    "  from events e where lottery_type is not null  ) t1 where lottery = 'Non-Numeric' and lottery2 ='NoLuckyDay') e \n" +
//                    "  on u.id = e.user_id\n" +
//                    "  and floor(u.first_order_time/1000) = e.time\n" +
//                    "group by u.register_time,e.lottery_type,u.first_order_amount,u.uid";


//            q=select distinct uu2.uid,register_time registertime ,FROM_UNIXTIME(CAST(mintime  as int), 'yyyy-MM-dd HH:mm:ss') mintime ,ee1.minprice,FROM_UNIXTIME(cast(maxtime as int),'yyyy-MM-dd HH:mm:ss') maxtime,ee3.maxprice  from (\n" +
//                    "select u.uid,\n" +
//                            "FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') register_time ,\n" +
//                            "min(e.time) mintime,\n" +
//                            "max(e.time) maxtime\n" +
//                            "from\n" +
//                            "(\n" +
//                            "select uid,id ,register_time from users \n" +
//                            "where uid in (\n" +
//                            SQLString +
//                            ")) u  join \n" +
//                            "(\n" +
//                            "select order_id,user_id,time from events \n" +
//                            "where order_id is not null ) e on u.id = e.user_id\n" +
//                            "group by u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss')\n" +
//                            ") uu2\n" +
//                            "\n" +
//                            "left join (\n" +
//                            "select uid,time,estimated_price minprice\n" +
//                            "from \n" +
//                            "(\n" +
//                            "select u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') register_time ,e.order_id,e.time,e.estimated_price\n" +
//                            "from\n" +
//                            "(\n" +
//                            "select uid,id ,register_time from users \n" +
//                            "where uid in (\n" +
//                            SQLString +
//                            ")) u  join (\n" +
//                            "select order_id,user_id,time,estimated_price from events \n" +
//                            "where order_id is not null ) e on u.id = e.user_id\n" +
//                            "group by u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') ,e.order_id,e.time,e.estimated_price\n" +
//                            ") ee \n" +
//                            ") ee1 on uu2.uid = ee1.uid\n" +
//                            "and uu2.mintime = ee1.time\n" +
//                            "\n" +
//                            "left join (\n" +
//                            "select uid,time,estimated_price maxprice\n" +
//                            "from \n" +
//                            "(\n" +
//                            "select u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') register_time ,e.order_id,e.time,e.estimated_price\n" +
//                            "from\n" +
//                            "(\n" +
//                            "select uid,id ,register_time from users \n" +
//                            "where uid in (\n" +
//                            SQLString +
//                            ")) u  join (\n" +
//                            "select order_id,user_id,time,estimated_price from events \n" +
//                            "where order_id is not null ) e on u.id = e.user_id\n" +
//                            "group by u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') ,e.order_id,e.time,e.estimated_price\n" +
//                            ") ee \n" +
//                            ") ee3 on uu2.uid = ee3.uid\n" +
//                            "and uu2.maxtime = ee3.time
//
            int count = 0;

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            writer.write("uid,registertime,mintime,minprice,maxtime,maxprice,ordercnt");
            writer.newLine();
            BufferedWriter writerunmatchuserid = new BufferedWriter(new FileWriter(output_unmatchuserid));
            for(Map.Entry<Integer, StringBuffer> entry : hashMap.entrySet())
            {
                String SQLString = entry.getValue().toString().substring(0,entry.getValue().toString().length()-2);
//                System.out.println("Key: " + entry.getKey() + ", Value: " +SQLString );
                String[] command = {
                        "curl",
                        "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                        "-X", "POST",
                        "--data-urlencode", "q=select uuu1.uid,uuu1.registertime,uuu1.mintime,uuu1.minprice, uuu1.maxtime,uuu1.maxprice,eee2.ordercnt from (\n" +
                        "select uu2.uid,register_time registertime ,FROM_UNIXTIME(CAST(mintime  as int), 'yyyy-MM-dd HH:mm:ss') mintime ,ee1.minprice,FROM_UNIXTIME(cast(maxtime as int),'yyyy-MM-dd HH:mm:ss') maxtime,ee3.maxprice \n" +
                        "from (\n" +
                        "select u.uid,\n" +
                        "FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') register_time ,\n" +
                        "min(e.time) mintime,\n" +
                        "max(e.time) maxtime\n" +
                        "from\n" +
                        "(\n" +
                        "select uid,id ,register_time from users \n" +
                        "where uid in (\n" +
                        SQLString+
                        ")) u  join \n" +
                        "(\n" +
                        "select order_id,user_id,time from events \n" +
                        "where order_id is not null and event='recharge_result' and cast(time as int) < UNIX_TIMESTAMP('2024-12-24 00:00:00', 'yyyy-MM-dd HH:mm:ss') ) e on u.id = e.user_id\n" +
                        "group by u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss')\n" +
                        ") uu2\n" +
                        "\n" +
                        "left join (\n" +
                        "select uid,time,estimated_price minprice\n" +
                        "from \n" +
                        "(\n" +
                        "select u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') register_time ,e.order_id,e.time,e.estimated_price\n" +
                        "from\n" +
                        "(\n" +
                        "select uid,id ,register_time from users \n" +
                        "where uid in (\n" +
                        SQLString+
                        ")) u  join (\n" +
                        "select order_id,user_id,time,estimated_price from events \n" +
                        "where order_id is not null  and event='recharge_result' and cast(time as int) < UNIX_TIMESTAMP('2024-12-24 00:00:00', 'yyyy-MM-dd HH:mm:ss') ) e on u.id = e.user_id\n" +
                        "group by u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') ,e.order_id,e.time,e.estimated_price\n" +
                        ") ee \n" +
                        ") ee1 on uu2.uid = ee1.uid\n" +
                        "and uu2.mintime = ee1.time\n" +
                        "\n" +
                        "left join (\n" +
                        "select uid,time,estimated_price maxprice\n" +
                        "from \n" +
                        "(\n" +
                        "select u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') register_time ,e.order_id,e.time,e.estimated_price\n" +
                        "from\n" +
                        "(\n" +
                        "select uid,id ,register_time from users \n" +
                        "where uid in (\n" +
                        SQLString+
                        ")) u  join (\n" +
                        "select order_id,user_id,time,estimated_price from events \n" +
                        "where order_id is not null and event='recharge_result'   and cast(time as int) < UNIX_TIMESTAMP('2024-12-24 00:00:00', 'yyyy-MM-dd HH:mm:ss') ) e on u.id = e.user_id\n" +
                        "group by u.uid,FROM_UNIXTIME(cast(u.register_time/1000 as int), 'yyyy-MM-dd HH:mm:ss') ,e.order_id,e.time,e.estimated_price\n" +
                        ") ee \n" +
                        ") ee3 on uu2.uid = ee3.uid\n" +
                        "and uu2.maxtime = ee3.time\n" +
                        ") uuu1\n" +
                        "\n" +
                        "left join \n" +
                        "\n" +
                        "(\n" +
                        "select u1.uid,count(distinct u2.order_id) ordercnt from (\n" +
                        "select uid,id,register_time \n" +
                        "from users \n" +
                        "where uid in (\n" +
                        SQLString+
                        ")) u1\n" +
                        "join \n" +
                        "(\n" +
                        "select order_id,user_id,time from events \n" +
                        "where order_id is not null ) u2 on u1.id = u2.user_id\n" +
                        "group by u1.uid ) eee2\n" +
                        "on uuu1.uid = eee2.uid\n ",
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
                        norechargesuccesspersion person = gson.fromJson(line,norechargesuccesspersion.class);
                        outputSet.add(person.uid);
//                        writer.write(person.uid+","+person.first_order_time+","+person.first_order_amount+","+person.lottery_type+","+person.register_time);
                        writer.write(person.uid+","+person.registertime+","+person.mintime+","+person.minprice+","+person.maxtime+","+person.maxprice+","+person.ordercnt);
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
