package com.icer.burgerkingcn.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icer.burgerkingcn.R;
import com.icer.burgerkingcn.app.BaseActivity;
import com.icer.burgerkingcn.task.BKAsyncTask;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private EditText mEditText;

    private ProgressDialog mProgressDialog;
    private boolean mIsRequesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        AdManager.getInstance(this).init("2b44afd35935cc6e", "21d7750ac359e818");
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.tv);
        mEditText = (EditText) findViewById(R.id.et);

        {
            AdView adView = new AdView(this, AdSize.FIT_SCREEN);
            LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
            adLayout.addView(adView);
        }

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = mEditText.getText().toString();
                if (code != null && code.length() > 0 && code.matches("[0-9]{16}")) {
                    if (!isRequesting()) {
                        startRequest(code);
                    } else {
                        showToast(R.string.network_requesting);
                    }
                } else {
                    showToast(R.string.plz_input_ticket_code_correctly);
                }
            }
        });

        mProgressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        mProgressDialog.setCancelable(false);
    }

    private void startRequest(String code) {
        mTextView.setText("");
        setIsRequesting(true);
        mProgressDialog.show();
        new BKAsyncTask(new BKAsyncTask.BKListener() {
            @Override
            public void onSuccess(String result) {
                mTextView.setText(result);
                setIsRequesting(false);
                mProgressDialog.dismiss();
            }

            @Override
            public void onProgress(String progress) {
                mTextView.setText(progress);
            }

            @Override
            public void onFailure() {
                showToast(R.string.network_error);
                setIsRequesting(false);
                mProgressDialog.dismiss();
            }
        }).execute(code);
    }

    public boolean isRequesting() {
        return mIsRequesting;
    }

    public void setIsRequesting(boolean isRequesting) {
        mIsRequesting = isRequesting;
    }

}
