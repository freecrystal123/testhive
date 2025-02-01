package util;

import jdbc.mysqljdbc;
import pojp.bussinfo;
import pojp.optdata;
import pojp.userdata;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class excelreaderwin {


    public static HashMap<String,List<String>>  prizeRules = new HashMap<>();
    public static HashMap<String,userdata>  userDataMap = new HashMap<>();
    public static String outputFilePath = "/Users/zhihuachai/Desktop/output3.csv"; // 输出文件路径
    static{
        // 设置规则
        String  oneLucky = "09|11|17|18|19|26|07";
        String  twoLucky = "08|11|17|20|23|27|08";
        String  threeLucky = "02|06|11|15|27|31|03";

        List<String> oneLuckyStr = Arrays.asList(oneLucky.split("\\|"));
        List<String> twoLuckyStr = Arrays.asList(twoLucky.split("\\|"));
        List<String> threeLuckyStr = Arrays.asList(threeLucky.split("\\|"));
        prizeRules.put("241214",oneLuckyStr);
        prizeRules.put("241228",twoLuckyStr);
        prizeRules.put("250111",threeLuckyStr);
    }
    public static void main(String[] args) {
        // 设置文件夹路径
        String folderPath = "/Users/zhihuachai/Desktop/viviane";

        // 获取文件夹中的所有 Excel 文件
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        // 如果没有 Excel 文件
        if (files == null || files.length == 0) {
            System.out.println("No Excel files found in the folder.");
            return;
        }

        // 遍历每个 Excel 文件并读取数据
        for (File file : files) {
            System.out.println("Reading file: " + file.getName());
            try {
                readCSVFile(file,file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 读取 Excel 文件
    private static void readCSVFile(File file,String FileName) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            // 逐行读取 CSV 文件


                while ((line = br.readLine()) != null) {
                    // 按逗号分隔每一行
                    String[] values = line.split(",");
                    count ++;
                    if(count ==1){
                        continue;
                    }
                    // 对奖逻辑开始
                   List<String> priceRule = prizeRules.get(values[4]);
                    if(priceRule==null){
                        continue;
                    }

                   List<String> userDataList = Arrays.asList( values[6].split("\\|"));
                   long count1 = priceRule.stream()
                            .filter(userDataList::contains)  // 过滤出 list1 中在 list2 中的元素
                            .distinct()               // 去重，避免重复计算
                            .count();  // 获取档期奖期数据
                   // 如果一个都没有直接下一个循环
                   if(count1==0){
                       continue;
                   }
                  userdata ud = userDataMap.get(values[0]);
                   if(count1>=1&&count1<5){ // T5

                       if(ud!=null){
                           //获奖票数加一
                        Integer won_entries =   ud.getWon_entries()+1;
                        ud.setWon_entries(won_entries);
                        // 获奖金额 + 100
                        Integer won_amount =     ud.getWon_amount()+100;
                        ud.setWon_amount(won_amount);

                       } else {
                          ud = new userdata();
                           ud.setUser_id(values[1]);
                          ud.setWon_amount(100);
                          ud.setWon_entries(1);
                          ud.setOrder_id(values[0]);
                          ud.setSeries_number(values[4]);
                          ud.setOrder_time(new Date(values[2]));
                          ud.setEntries(values[3]);
                          ud.setCost_entries(values[5]);

                       }

                   } else if(count1==5){


                       if(ud!=null){
                           //获奖票数加一
                           Integer won_entries =   ud.getWon_entries()+1;
                           ud.setWon_entries(won_entries);
                           // 获奖金额 + 100
                           Integer won_amount =     ud.getWon_amount()+1000;
                           ud.setWon_amount(won_amount);

                       } else {
                           ud = new userdata();
                           ud.setUser_id(values[1]);
                           ud.setWon_amount(1000);
                           ud.setWon_entries(1);
                           ud.setOrder_id(values[0]);
                           ud.setSeries_number(values[4]);
                           ud.setOrder_time(new Date(values[2]));
                           ud.setEntries(values[3]);
                           ud.setCost_entries(values[5]);

                       }

                   } else if(count1==6){
                       // 获取特别奖期号
                       String result = userDataList.stream()
                               .map(String::valueOf)  // 将每个元素转换为字符串
                               .collect(Collectors.joining(", "));
                       System.out.println("result:" + result +"奖期"+values[4]);
                       String specialnum = priceRule.get(priceRule.size()-1) ;
                       String userLastOne = userDataList.get(userDataList.size()-1);
                       Integer priceAmount  = 0;
                       if(specialnum==userLastOne){
                           priceAmount = 100000;
                       } else {
                           priceAmount = 1000000;
                       }
                       if(ud!=null){
                           //获奖票数加一
                           Integer won_entries =   ud.getWon_entries()+1;
                           ud.setWon_entries(won_entries);
                           // 获奖金额 + 100
                           Integer won_amount =     ud.getWon_amount()+priceAmount;
                           ud.setWon_amount(won_amount);

                       } else {
                           ud = new userdata();
                           ud.setUser_id(values[1]);
                           ud.setWon_amount(priceAmount);
                           ud.setWon_entries(1);
                           ud.setOrder_id(values[0]);
                           ud.setSeries_number(values[4]);
                           ud.setOrder_time(new Date(values[2]));
                           ud.setEntries(values[3]);
                           ud.setCost_entries(values[5]);

                       }

                   }else if(count1==7){
                       if(ud!=null){
                           //获奖票数加一
                           Integer won_entries =   ud.getWon_entries()+1;
                           ud.setWon_entries(won_entries);
                           // 获奖金额 + 100
                           Integer won_amount =     ud.getWon_amount()+100000000;
                           ud.setWon_amount(won_amount);

                       } else {
                           ud = new userdata();
                           ud.setUser_id(values[1]);
                           ud.setWon_amount(100000000);
                           ud.setWon_entries(1);
                           ud.setOrder_id(values[0]);
                           ud.setOrder_time(new Date(values[2]));
                           ud.setEntries(values[3]);
                           ud.setSeries_number(values[4]);
                           ud.setCost_entries(values[5]);

                       }
                   }

                    userDataMap.put(values[0],ud);

                }






        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
        // 文档写入
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        writer.write("order_id,uid,order_time,entries,series_number,cost_entries,won_amount,won_entries");
        writer.newLine();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Map.Entry<String, userdata> entry : userDataMap.entrySet()){
            userdata userdata = entry.getValue();
            String ordertime =    sdf.format(userdata.getOrder_time());
            writer.write(userdata.getOrder_id()+","+
                    userdata.getUser_id()+","+
                    ordertime+","+
                    userdata.getEntries()+","+
                    userdata.getSeries_number()+","+
                    userdata.getCost_entries()+","+
                    userdata.getWon_amount()+","+
                    userdata.getWon_entries());
            writer.newLine();
        }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
