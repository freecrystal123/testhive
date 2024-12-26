package util;

import java.util.*;

public class minus {


    public String minusdata(Set<String> input1,Set<String> input2) {


        Set<String> temp1 = input1;
        Set<String> temp2 = input2;
        // 求 Set1 - Set2 差集
        temp1.removeAll(temp2);
        System.out.println("Difference (input1 - input2): " + temp1);
//
//        // 求 Set2 - Set1 差集
//        temp2.removeAll(temp1);
//        System.out.println("Difference (Set2 - Set1): " + temp2);
        StringBuffer stringBuffer = new StringBuffer();
        while(temp1.iterator().hasNext()){
            String lines = temp1.iterator().next();
            stringBuffer.append(lines+"\n");
        }

        return stringBuffer.toString();

    }


    public static String minusdata(String input1,String input2) {

        String[] line1 = input1.split("\n");
        String[] line2 = input2.split("\n");
        Set<String> rawlinestr1 = new HashSet<String>();
        Set<String> rawlinestr2 = new HashSet<String>();
        for(String rawline1:line1){
            rawlinestr1.add(rawline1.trim());
        }
        for(String rawline2:line2){
            rawlinestr2.add(rawline2.trim());
        }

        // 求 Set1 - Set2 差集
        rawlinestr1.removeAll(rawlinestr2);
        System.out.println("Difference (input1 - input2): " + rawlinestr1);
//
//        // 求 Set2 - Set1 差集
//        temp2.removeAll(temp1);
//        System.out.println("Difference (Set2 - Set1): " + temp2);
        StringBuffer stringBuffer = new StringBuffer();

        for (String lines : rawlinestr1) {
            stringBuffer.append(lines+"\n");
        }

        return stringBuffer.toString();

    }






}
