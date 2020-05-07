package com.syd.oden.gesturelock.view.listener;

public interface GesturePasswordSettingListener {
    boolean onFirstInputComplete(int len);

    void onSuccess();

    void onFail();
}
