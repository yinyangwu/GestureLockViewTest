package com.youngwu.gesturelockview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.syd.oden.gesturelock.view.GestureLockViewGroup;
import com.syd.oden.gesturelock.view.listener.GesturePasswordSettingListener;

/**
 * Desc:手势密码视图
 * <p>
 * Created by YoungWu on 2019-08-29.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private TextView tv_reset;
    private TextView tv_status;
    private GestureLockViewGroup gestureLockView;
    private boolean isReset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initView();
        initData();
        setListener();
    }

    private void initView() {
        tv_reset = findViewById(R.id.tv_reset);
        tv_status = findViewById(R.id.tv_status);
        gestureLockView = findViewById(R.id.gestureLockView);
    }

    private void initData() {

    }

    private void setListener() {
        tv_reset.setOnClickListener(this);
        gestureLockView.setGesturePasswordSettingListener(new GesturePasswordSettingListener() {
            @Override
            public boolean onFirstInputComplete(int len) {
                if (len > 3) {
                    tv_status.setTextColor(Color.WHITE);
                    tv_status.setText("再次绘制手势密码");
                    return true;
                } else {
                    tv_status.setTextColor(Color.RED);
                    tv_status.setText("最少连接4个点，请重新输入");
                    return false;
                }
            }

            @Override
            public void onSuccess() {
                tv_status.setTextColor(Color.WHITE);
                Toast.makeText(MainActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                tv_status.setText("请输入手势密码解锁：" + gestureLockView.getPassword());
            }

            @Override
            public void onFail() {
                tv_status.setTextColor(Color.RED);
                tv_status.setText("与上一次绘制不一致，请重新绘制");
            }
        });
        gestureLockView.setGestureEventListener((matched, remainedTimes) -> {
            if (!matched) {
                tv_status.setTextColor(Color.RED);
                tv_status.setText("手势密码错误，还剩" + remainedTimes + "次机会");
            } else {
                if (isReset) {
                    isReset = false;
                    Toast.makeText(MainActivity.this, "清除成功", Toast.LENGTH_SHORT).show();
                    resetGesturePattern();
                } else {
                    tv_status.setTextColor(Color.WHITE);
                    tv_status.setText("手势密码正确：" + gestureLockView.getPassword());
                }
            }
        });
        gestureLockView.setGestureUnmatchedExceedListener(2, () -> {
            tv_status.setTextColor(Color.RED);
            tv_status.setText("错误次数过多，请稍后再试");
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_reset) {
            //重置密码
            isReset = true;
            tv_status.setTextColor(Color.WHITE);
            tv_status.setText("请输入原手势密码");
            gestureLockView.resetView();
            gestureLockView.setRetryTimes(2);
        }
    }

    private void setGestureWhenNoSet() {
        if (!gestureLockView.isSetPassword()) {
            tv_status.setTextColor(Color.WHITE);
            tv_status.setText("绘制手势密码");
        }
    }

    private void resetGesturePattern() {
        gestureLockView.removePassword();
        setGestureWhenNoSet();
        gestureLockView.resetView();
    }
}
