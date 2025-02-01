package util;

import jdbc.mysqljdbc;
import pojp.bussinfo;
import pojp.optdata;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class excelreadeandpin {

    static String  outputPath = "/Users/zhihuachai/Desktop/pin" +
            "/merged.csv";
    static int count = 0;
    static BufferedWriter writer =  null;
    public static void main(String[] args) {
        // 设置文件夹路径
        String folderPath = "/Users/zhihuachai/Desktop/pin";


        // 获取文件夹中的所有 Excel 文件
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        try {
            writer = Files.newBufferedWriter(Paths.get(outputPath));
        } catch (IOException e) {
            e.printStackTrace();
        }


        // 如果没有 Excel 文件
        if (files == null || files.length == 0) {
            System.out.println("No Excel files found in the folder.");
            return;
        }
        try {
        // 遍历每个 Excel 文件并读取数据
        for (File file : files) {
            System.out.println("Reading file: " + file.getName());
            readCSVFile(file,file.getName());
        }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // 读取 Excel 文件
    private static void readCSVFile(File file,String FileName) {
        List<optdata> optdataList = new ArrayList<optdata>();
        List<bussinfo> bussinfoList = new ArrayList<bussinfo>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    
            String line;
            // 逐行读取 CSV 文件
            if(FileName.contains("lucky_day")){

                while ((line = br.readLine()) != null) {
                    count ++;
                    System.out.println("现在："+count);
                    writer.write(line);
                    writer.newLine();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
//            mysqljdbc.insertTable(FileName,optdataList,bussinfoList);

        }


    }


}
