package com.example.penmediatv;

import android.app.Application;
import android.util.Log;

import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveBaseListener;

public class MApplication extends Application {
    public void onCreate() {
        super.onCreate();
        String licenceURL = "https://license.vod2.myqcloud.com/license/v2/1329031633_1/v_cube.license"; // 获取到的 licence url
        String licenceKey = "5297b8a0cfc1108c5ad55c1cd6bdf75a"; // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
        TXLiveBase.setListener(new TXLiveBaseListener() {
            @Override
            public void onLicenceLoaded(int result, String reason) {
                Log.i("TAG", "onLicenceLoaded: result:" + result + ", reason:" + reason);
                if (result != 0) {
                    // 如果 result 不为 0，表示设置失败，需要进行重试
                    TXLiveBase.getInstance().setLicence(MApplication.this, licenceURL, licenceKey);
                }
            }
        });
    }
}