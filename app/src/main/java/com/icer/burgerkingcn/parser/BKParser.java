package com.icer.burgerkingcn.parser;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

/**
 * Created by icer on 2015/11/3.
 */
public class BKParser {

    public static final String ROOT_URL = "https://tellburgerking.com.cn/";
    public static final String FINISH_FLAG = "ValCode";

    public static String[] getNextRequestUrl(String xmlString) {
        String url = "";

        return new String[]{url};
    }

    public static RequestBody[] getNextRequestBody(String xmlString) {
        FormEncodingBuilder bodyBuilder = new FormEncodingBuilder();


        return new RequestBody[]{bodyBuilder.build()};
    }

    public static boolean isFinish(String xmlString) {
        return xmlString != null && xmlString.contains(FINISH_FLAG);
    }
}
