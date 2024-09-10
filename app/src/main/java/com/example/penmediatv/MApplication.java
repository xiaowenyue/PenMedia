package com.example.penmediatv;

import android.app.Application;
import android.util.Log;

import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveBaseListener;


public class MApplication extends Application {
    private static final String TAG = "MApplication";
    private static final int MAX_RETRIES = 3;
    private static int retryCount = 0;
    private String licenceURL = "https://license.vod2.myqcloud.com/license/v2/1329031633_1/v_cube.license"; // 获取到的 licence url
    private String licenceKey = "5297b8a0cfc1108c5ad55c1cd6bdf75a"; // 获取到的 licence key;

    @Override
    public void onCreate() {
        super.onCreate();
        setLicenceWithRetry();
    }

    private void setLicenceWithRetry() {
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
        TXLiveBase.setListener(new TXLiveBaseListener() {
            @Override
            public void onLicenceLoaded(int result, String reason) {
                Log.i(TAG, "onLicenceLoaded: result:" + result + ", reason:" + reason);
                if (result == 0) {
                    // 设置成功
                    return;
                } else {
                    // 设置失败，进行重试
                    retryCount++;
                    Log.d("bo", "设置许可证失败，正在重试，当前重试次数：" + retryCount);
                    if (retryCount <= MAX_RETRIES) {
                        setLicenceWithRetry();
                    } else {
                        // 达到最大重试次数，处理失败情况
                        Log.e(TAG, "设置许可证失败，已达到最大重试次数：" + MAX_RETRIES);
                    }
                }
            }
        });
    }
}
