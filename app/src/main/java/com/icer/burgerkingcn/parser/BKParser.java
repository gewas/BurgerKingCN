package com.icer.burgerkingcn.parser;

import android.util.Log;

import com.icer.burgerkingcn.app.AppConfig;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by icer on 2015/11/3.
 */
public class BKParser {

    public static final String TAG = BKParser.class.getCanonicalName();
    public static final String LINE_DIVIDER = "---------------------------------------------------------";
    public static final String ROOT_URL = "https://tellburgerking.com.cn/";
    public static final String FINISH_FLAG = "ValCode";

    public static final String KW_CDID = "_bhtg_logurl";

    public static final String RE_FNS = "id=\"FNS";

    public static final String KW_Progress = "\"ProgressPercentage\"";


    private static final Map<String, String> Q_MAP = new HashMap<>();

    static {
        Q_MAP.put("R000091", "1");
        Q_MAP.put("R000097", "1");
        Q_MAP.put("R001000", "3");
        Q_MAP.put("R002000", "1");
        Q_MAP.put("R004000", "5");
        Q_MAP.put("R007000", "5");
        Q_MAP.put("R008000", "5");
        Q_MAP.put("R009000", "5");
        Q_MAP.put("R010000", "5");
        Q_MAP.put("R011000", "5");
        Q_MAP.put("R012000", "5");
        Q_MAP.put("R013000", "5");
        Q_MAP.put("R014000", "5");
        Q_MAP.put("R015000", "5");
        Q_MAP.put("R017000", "5");
        Q_MAP.put("R020000", "5");
        Q_MAP.put("R021000", "9");
        Q_MAP.put("R023000", "5");
        Q_MAP.put("R029000", "9");
        Q_MAP.put("R030000", "9");
        Q_MAP.put("R031000", "9");
        Q_MAP.put("R041000", "2");
        Q_MAP.put("R044000", "5");
        Q_MAP.put("R045000", "5");
        Q_MAP.put("R046000", "5");
        Q_MAP.put("R047000", "5");
        Q_MAP.put("R048000", "1");
        Q_MAP.put("R049000", "1");
        Q_MAP.put("R050000", "1");
        Q_MAP.put("R054000", "1");
        Q_MAP.put("R055000", "1");
        Q_MAP.put("R057000", "2");
        Q_MAP.put("R058000", "2");
        Q_MAP.put("R060000", "3");
        Q_MAP.put("R068000", "4");
        Q_MAP.put("R069000", "9");
        Q_MAP.put("R070000", "1");
        Q_MAP.put("R107000", "5");
        Q_MAP.put("R108000", "9");
        Q_MAP.put("S076000", "");
        Q_MAP.put("S081000", "All is well");
    }

    public static List<String> getNextRequestUrls(String xmlString) {
        List<String> res = new ArrayList<>();
        if (xmlString == null)
            res.add(ROOT_URL);
        else {
            if (xmlString.contains(KW_CDID)) {
                String params = "";
                String u = xmlString.split("_bhtg_userid = \"")[1];
                u = u.substring(0, u.indexOf("\""));

                String s = xmlString.split("_bhtg_sessionid = \"")[1];
                s = s.substring(0, s.indexOf("\""));

                String sid = xmlString.split("_bhtg_siteid = \"")[1];
                sid = sid.substring(0, sid.indexOf("\""));

                String bhud = sid;
                String acct = "servicemgmt";
                String rand = System.currentTimeMillis() + "";

                params += "?u=" + u
                        + "&s=" + s
                        + "&sid=" + sid
                        + "&bhud=" + bhud
                        + "&acct=" + acct
                        + "&rand=" + rand;
                res.add(ROOT_URL + "CDID.aspx" + params);
            }
            {
                String next = xmlString.split("action=\"")[1];
                String urlTail = next.substring(0, next.indexOf('\"'));
                res.add(ROOT_URL + urlTail);
            }
        }
        log(LINE_DIVIDER);
        log("URLS: " + res);
        log(LINE_DIVIDER);
        return res;
    }

    public static List<RequestBody> getNextRequestBodies(String xmlString, String ticketCode) {
        List<RequestBody> res = new ArrayList<>();
        if (xmlString == null)
            res.add(null);
        else {
            if (xmlString.contains(KW_CDID)) {
                res.add(null);
            }
            {
                String nextButton = xmlString.split("\"NextButton\" value=\"")[1];
                nextButton = nextButton.substring(0, nextButton.indexOf("\""));
                if ("继续".equals(nextButton)) {
                    FormEncodingBuilder bodyBuilder = new FormEncodingBuilder();
                    bodyBuilder
                            .add("JavaScriptEnabled", "1")
                            .add("FIP", "True")
                            .add("AcceptCookies", "Y")
                            .add("NextButton", "继续");
                    res.add(bodyBuilder.build());
                } else if ("开始".equals(nextButton)) {
                    FormEncodingBuilder bodyBuilder = new FormEncodingBuilder();
                    bodyBuilder
                            .add("JavaScriptEnabled", "1")
                            .add("FIP", "True")
                            .add("CN1", ticketCode.substring(0, 3))
                            .add("CN2", ticketCode.substring(3, 6))
                            .add("CN3", ticketCode.substring(6, 9))
                            .add("CN4", ticketCode.substring(9, 12))
                            .add("CN5", ticketCode.substring(12, 15))
                            .add("CN6", ticketCode.substring(15))
                            .add("NextButton", "开始");
                    res.add(bodyBuilder.build());
                } else if ("下一步".equals(nextButton)) {
                    FormEncodingBuilder bodyBuilder = new FormEncodingBuilder();
                    Map<String, String> kvs = getFNSData(xmlString);
                    for (String key : kvs.keySet()) {
                        bodyBuilder.add(key, kvs.get(key));
                        bodyBuilder.add(key, kvs.get(key));
                    }
                    res.add(bodyBuilder.build());
                }
            }

        }
        log(LINE_DIVIDER);
        log("BODIES: " + res);
        log(LINE_DIVIDER);
        return res;
    }

    private static Map<String, String> getFNSData(String xmlString) {
        Map<String, String> res = new HashMap<>();
        {
            String tIoNF = xmlString.split("\"IoNF\" value=\"")[1];
            tIoNF = tIoNF.substring(0, tIoNF.indexOf("\""));
            res.put("IoNF", tIoNF);
        }
        {
            String tPostedFNS = xmlString.split("\"PostedFNS\" value=\"")[1];
            tPostedFNS = tPostedFNS.substring(0, tPostedFNS.indexOf("\""));
            res.put("PostedFNS", tPostedFNS);
        }
        {
            String[] temp = xmlString.split(RE_FNS);
            List<String> tFNS = new ArrayList<>();
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].startsWith("S") || temp[i].startsWith("R")) {
                    String a = temp[i].substring(0, temp[i].indexOf("\""));
                    if (a.matches("[SR][0-9]{6}"))
                        tFNS.add(a);
                }
            }
            for (String key : tFNS) {
                String value = Q_MAP.get(key);
                if (value == null)
                    log("CAN'T FIND " + key);
                else
                    res.put(key, value);
            }

        }
        return res;
    }

    public static String getProgress(String xmlString) {
        String res;
        if (xmlString.contains(KW_Progress)) {
            String progress = xmlString.split(KW_Progress)[1];
            progress = progress.substring(progress.indexOf(">") + 1, progress.indexOf("%") + 1);
            res = progress;
        } else
            res = "0%";
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

    public static void log(String msg) {
        if (AppConfig.IS_DEBUG_MODE)
            Log.i(TAG, msg);
    }

}
