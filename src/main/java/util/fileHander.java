package util;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class fileHander {


    public Set<String> readInputFile(String inputFilePath) throws Exception {


        Gson gson = new Gson();
        // 读取文件的所有行
        List<String> lines = Files.readAllLines(Paths.get(inputFilePath));
        //input Set
        HashSet<String> inputSet = new HashSet<>();

        // 创建一个输出流来写入修改后的内容
        System.out.println("开始整合数据！！！");
        int inputcount = 0;
        for (String line : lines) {
            inputSet.add(line);
            // 写入到输出文件
            inputcount++;

        }
        System.out.println("输入数据：" + inputcount + "条！！！");
        Iterator<String> iterator = inputSet.iterator();
        int outputcount = 0;
        StringBuffer stringBuffer = new StringBuffer();

        while (iterator.hasNext()) {
            String element = iterator.next();
            String modifiedLine = "'" + element + "',";
            stringBuffer.append(modifiedLine + "\n");
            outputcount++;
            if (outputcount % 2000 == 0) {
                stringBuffer = new StringBuffer();
            }
            if (outputcount == inputSet.size()) {
                stringBuffer = new StringBuffer();
            }

        }
        int diff = inputcount - outputcount;
        System.out.println("输出数据：" + outputcount + "条！！！");
        System.out.println("其中重复数据：" + diff + "条！！！");
        return inputSet;

    }






}
