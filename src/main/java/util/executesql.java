package util;

import pojp.orderwinnowin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;


public class executesql {

    public static void voidexecutesql ( String StrSQL) {

        // 提取SQL部分
        String regexSQL = "(?i)SELECT(.*?)FROM";  // (?i) 忽略大小写，(.*?) 非贪婪匹配
        Pattern pattern = Pattern.compile(regexSQL);
        Matcher matcher = pattern.matcher(StrSQL);


        if (matcher.find()) {
            String selectPart = matcher.group(0).trim(); // 提取
            System.out.println("selectPart:"+selectPart);
            try {
                throw new Exception("");
            } catch (Exception e) {

                return  ;
            }

        }


        String outputFilePath = "/Users/zhihuachai/Desktop/output2.csv"; // 输出文件路径
        String[] command = {
                "curl",
                "https://data.admin-uaenl.ae/api/sql/query?token=0303149a7f47af8d6c34e803c5b42b32e199114857e52e6d1333f7331a6d379f&project=production",  // 替换为你实际的 URL
                "-X", "POST",
                "--data-urlencode", "q="+StrSQL+"",
                "--data-urlencode", "format=json",
        };
        int count = 0;

        // 输出 逻辑
        try {
            Gson gson = new Gson();
            // 执行 curl 命令
            Process process = Runtime.getRuntime().exec(command);
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int databaseoutputcount = 0;
//                    writer.write("uid , first_order_time,first_order_amount,lottery_type,register_time");
//                    writer.write("uid , register_time,order_id,lottery_type,time");
            HashSet<String> outputSet = new HashSet<>();
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            while ((line = reader.readLine()) != null) {
//                        System.out.println(line);  // 打印输出
                orderwinnowin person = gson.fromJson(line, orderwinnowin.class);
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
    }

}
