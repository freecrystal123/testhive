package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class deduplication {

    static String log;
    public static String LogSender()
    {
        return log;
    }
    public static String deduplication(String inputText) {


        StringBuffer OutPutLog = new StringBuffer();
        String deduplicationInputDate  = "";
        HashSet<String> dupucates = new HashSet<>();
        System.out.println("开始整合数据！！！");
        //input Set
        HashSet<String> inputSet = new HashSet<>();
        String[] lines = inputText.split("\n");
        int inputcount = 0;
        for (String line : lines) {
            if(inputSet.contains(line)){
                dupucates.add(line);
            };
            inputSet.add(line);
            // 写入到输出文件
            inputcount++;

        }
        OutPutLog.append("输入数据：" + inputcount + "条！！！"+"\n");

        Iterator<String> iterator = inputSet.iterator();
        int outputcount = 0;
        StringBuffer stringBuffer = new StringBuffer();
        HashMap<Integer, StringBuffer> hashMap = new HashMap<>();
        System.out.println("输入数据：" + inputcount + "条！！！");
        int batchId = 0;

        while (iterator.hasNext()) {
            String element = iterator.next();
            // 给每行两边加上分号
            String modifiedLine = "'" + element + "',";
            stringBuffer.append(modifiedLine + "\n");
//                writer.write(modifiedLine);
//                writer.newLine(); // 换行
            outputcount++;
            if (outputcount % 2000 == 0) {
                batchId++;
                hashMap.put(batchId, stringBuffer);
            }
            if (outputcount == inputSet.size()) {
                batchId++;
                hashMap.put(batchId, stringBuffer);
            }

        }
//            System.out.println(stringBuffer);
        int diff = inputcount - outputcount;
        OutPutLog.append("输出数据：" + outputcount + "条！！！"+"\n");
        OutPutLog.append("其中重复数据：" + diff + "条！！！"+"\n");
        StringBuffer depbuffer = new StringBuffer();
        for(String dep : dupucates){
            depbuffer.append(dep+"\n");
        }
//        OutPutLog.append("重复数据有：\n");
//        OutPutLog.append(depbuffer);
        for (Map.Entry<Integer, StringBuffer> entry : hashMap.entrySet()) {
            deduplicationInputDate = entry.getValue().toString().substring(0, entry.getValue().toString().length() - 2);
        }
        log = OutPutLog.toString();
        return deduplicationInputDate;

    }
}
