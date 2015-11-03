package com.icer.burgerkingcn.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.icer.burgerkingcn.R;
import com.icer.burgerkingcn.app.AppConfig;
import com.icer.burgerkingcn.app.BaseActivity;
import com.icer.burgerkingcn.task.BKAsyncTask;

public class MainActivity extends BaseActivity {

    private TextView mTextView;
    private EditText mEditText;

    private boolean mIsRequesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.tv);
        mEditText = (EditText) findViewById(R.id.et);
        if(AppConfig.IS_DEBUG_MODE)
            mEditText.setText("1908011201100146");
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
    }

    private void startRequest(String code) {
        mTextView.setText("");
        setIsRequesting(true);
        new BKAsyncTask(new BKAsyncTask.BKListener() {
            @Override
            public void onSuccess(String result) {
                mTextView.setText(result);
                setIsRequesting(false);
            }

            @Override
            public void onFailure() {
                showToast(R.string.network_error);
                setIsRequesting(false);
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
