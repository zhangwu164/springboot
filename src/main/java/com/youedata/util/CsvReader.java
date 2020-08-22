package com.youedata.util;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CsvReader {

    public  void deailData(String fileUrl) {
        try {
            InputStreamReader freader = new InputStreamReader(new FileInputStream(
                    new File(fileUrl)), "UTF-8");
            // 读取cvs数据
            ICsvListReader csvReader  = new CsvListReader(freader, CsvPreference.STANDARD_PREFERENCE);
            // 头部
            String headerArray[] = csvReader.getHeader(true);
            ArrayList< String> arrayList = new ArrayList<String>(headerArray.length);
            Collections.addAll(arrayList, headerArray);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            InputStreamReader freader = new InputStreamReader(new FileInputStream(
                    new File("D:\\dataqe\\upload\\csv\\7\\DataTest0.csv")), "UTF-8");
            // 读取cvs数据
            ICsvListReader csvReader  = new CsvListReader(freader, CsvPreference.STANDARD_PREFERENCE);
            // 头部
            String headerArray[] = csvReader.getHeader(true);

            ArrayList< String> arrayList = new ArrayList<String>(headerArray.length);
            Collections.addAll(arrayList, headerArray);

            List<String> rowListData;
            long num = 0;
            while ((rowListData = csvReader.read()) != null) {
                for (int i=0;i < rowListData.size(); i++){
                    System.out.println(rowListData.get(i));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
