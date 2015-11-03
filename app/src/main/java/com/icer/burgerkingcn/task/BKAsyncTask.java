package com.icer.burgerkingcn.task;

import android.os.AsyncTask;
import android.util.Log;

import com.icer.burgerkingcn.app.AppConfig;
import com.icer.burgerkingcn.parser.BKParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by icer on 2015/11/3.
 */
public class BKAsyncTask extends AsyncTask<String, String, String> {

    public static final String TAG = BKAsyncTask.class.getCanonicalName();

    private static final String RESULT_FAILURE = "FAILURE";
    private static final String COOKIE = "Cookie";
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String LINE_DIVIDER = "---------------------------------------------------------";

    private BKListener mBKListener;
    private OkHttpClient mOkHttpClient;
    private List<String> mCookies;
    private String mCode;

    private String mFinalResult;

    public BKAsyncTask(BKListener BKListener) {
        mBKListener = BKListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        initOkHttp();
        mCookies = new ArrayList<>();
    }

    private void initOkHttp() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCookieHandler(new CookieHandler() {
            @Override
            public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
                Map<String, List<String>> rMap = new HashMap<>(requestHeaders);
                if (!mCookies.isEmpty()) {
                    rMap.put(COOKIE, mCookies);
                    log(LINE_DIVIDER);
                    log(COOKIE + ": " + mCookies);
                    log(LINE_DIVIDER);
                }
                return rMap;
            }

            @Override
            public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
                Set<String> keySet = responseHeaders.keySet();
                for (String s : keySet) {
                    if (SET_COOKIE.equals(s)) {
                        refreshCookie(responseHeaders.get(s));
                    }
                }
            }
        });
    }

    private void refreshCookie(List<String> cookies) {
        log(LINE_DIVIDER);
        log(SET_COOKIE + ": " + cookies);
        log(LINE_DIVIDER);
        for (String str : cookies)
            if (!mCookies.contains(str))
                mCookies.add(str);
    }

    @Override
    protected String doInBackground(String... params) {
        mCode = params[0];
        log(LINE_DIVIDER);
        log("CODE: " + mCode);
        log(LINE_DIVIDER);
        try {
            String tXmlString = go(null);
            while (!BKParser.isFinish(tXmlString)) {
                tXmlString = go(tXmlString);
                if (tXmlString == null || tXmlString.length() == 0)
                    break;
            }
            if (tXmlString != null && tXmlString.length() > 0)
                mFinalResult = BKParser.getFinalResult(tXmlString);
            else
                mFinalResult = RESULT_FAILURE;
        } catch (IOException e) {
            e.printStackTrace();
            mFinalResult = RESULT_FAILURE;
        }

        return mFinalResult;
    }

    private String go(String xmlString) throws IOException {
        String res = "";
        List<String> urls = BKParser.getNextRequestUrls(xmlString);
        List<RequestBody> bodies = BKParser.getNextRequestBodies(xmlString);
        for (int i = 0; i < urls.size(); i++) {

            String url = urls.get(i);
            log(LINE_DIVIDER);
            log("URL: " + url);
            log(LINE_DIVIDER);

            Request request = new Request.Builder()
                    .url(url)
                    .method(bodies.get(i) != null ? "POST" : "GET", bodies.get(i))
                    .build();

            Response response = mOkHttpClient.newCall(request).execute();
            res = response.body().string();
            log(LINE_DIVIDER);
            log(res);
            log(LINE_DIVIDER);
        }
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!RESULT_FAILURE.equals(s))
            mBKListener.onSuccess(s);
        else
            mBKListener.onFailure();
    }

    private void log(String msg) {
        if (AppConfig.IS_DEBUG_MODE)
            Log.i(TAG, msg);
    }

    public interface BKListener {
        void onSuccess(String result);

        void onFailure();
    }
}
