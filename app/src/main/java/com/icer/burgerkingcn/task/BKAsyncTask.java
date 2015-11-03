package com.icer.burgerkingcn.task;

import android.os.AsyncTask;
import android.util.Log;

import com.icer.burgerkingcn.app.AppConfig;

/**
 * Created by icer on 2015/11/3.
 */
public class BKAsyncTask extends AsyncTask<String, String, String> {

    public static final String TAG = BKAsyncTask.class.getCanonicalName();

    private static final String RESULT_FAILURE = "FAILURE";

    private BKListener mBKListener;

    private String mCode;
    private boolean misFinish;

    public BKAsyncTask(BKListener BKListener) {
        mBKListener = BKListener;
    }

    @Override
    protected String doInBackground(String... params) {
        mCode = params[0];
        if (AppConfig.IS_DEBUG_MODE)
            Log.i(TAG, "CODE: " + mCode);

        String res = "";
        while (!misFinish) {

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

    public interface BKListener {
        void onSuccess(String result);

        void onFailure();
    }
}
