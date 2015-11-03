package com.icer.burgerkingcn.parser;

import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icer on 2015/11/3.
 */
public class BKParser {

    public static final String ROOT_URL = "https://tellburgerking.com.cn/";
    public static final String FINISH_FLAG = "ValCode";

    public static List<String> getNextRequestUrls(String xmlString) {
        List<String> res = new ArrayList<>();
        if (xmlString == null)
            res.add(ROOT_URL);

        return res;
    }

    public static List<RequestBody> getNextRequestBodies(String xmlString) {
        List<RequestBody> res = new ArrayList<>();
        if (xmlString == null)
            res.add(null);

//        FormEncodingBuilder bodyBuilder = new FormEncodingBuilder();

        return res;
    }

    public static boolean isFinish(String xmlString) {
        return xmlString != null && xmlString.contains(FINISH_FLAG);
    }

    public static String getFinalResult(String xmlString) {
        String res = xmlString.split(FINISH_FLAG)[1];
        res = res.substring(res.indexOf(">") + 1, res.indexOf("<"));
        return res;
    }

}
