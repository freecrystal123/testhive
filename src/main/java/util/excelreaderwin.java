package util;

import jdbc.mysqljdbc;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojp.bussinfo;
import pojp.optdata;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class excelreader {

    public static void main(String[] args) {
        // 设置文件夹路径
        String folderPath = "/Users/zhihuachai/Desktop/jiangzhou";

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
        List<optdata> optdataList = new ArrayList<optdata>();
        List<bussinfo> bussinfoList = new ArrayList<bussinfo>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            // 逐行读取 CSV 文件
            if(FileName.contains("Buss Info")){

                while ((line = br.readLine()) != null) {
                    // 按逗号分隔每一行
                    String[] values = line.split(",");
                    count ++;
                    if(count ==1){
                        continue;
                    }
                    bussinfo dataLine =  new bussinfo();
                    dataLine.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(values[0]));
                    dataLine.setRegister_num(Integer.parseInt(values[1]));
                    dataLine.setRecharger_num(Integer.parseInt(values[2]));
                    dataLine.setLottery_order_num(Integer.parseInt(values[3]));
                    dataLine.setLogin_result_num(Integer.parseInt(values[4]));
                    dataLine.setPageview_num(Integer.parseInt(values[5]));
                    bussinfoList.add(dataLine);

                }

            } else {
                while ((line = br.readLine()) != null) {
                    // 按逗号分隔每一行
                    String[] values = line.split(",");
                    count ++;
                    if(count ==1){
                        continue;
                    }
                    optdata dataLine =  new optdata();
                    dataLine.setData(Double.parseDouble(values[1]));
                    dataLine.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(values[0]));
                    optdataList.add(dataLine);
                    // 输出每个字段
                    for (String value : values) {
                        System.out.print(value + "\t");  // 使用 tab 来分隔输出
                    }
                    System.out.println();  // 换行
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            mysqljdbc.insertTable(FileName,optdataList,bussinfoList);
        }


    }


}
