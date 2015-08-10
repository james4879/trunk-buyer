package com.datapush.buyhand.view;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.daoshun.lib.communication.http.HttpAccessor;
import com.daoshun.lib.communication.http.JSONAccessor;
import com.datapush.buyhand.common.Settings;
import com.datapush.buyhand.net.data.UploadFileParam;
import com.datapush.buyhand.net.data.UploadFileResult;

public class UploadFileTask extends AsyncTask<String, Void, UploadFileResult> {
    
    private JSONAccessor mAccessor;
    
    public UploadFileTask(Context context, String filename) {
        mAccessor = new JSONAccessor(context, HttpAccessor.METHOD_POST_MULTIPART);
    }

    // 文件上传
    @Override
    protected UploadFileResult doInBackground(String... params) {
        File file = new File(params[0]);

        if (!file.exists() || !file.isFile())
            return null;
        UploadFileParam param = new UploadFileParam();
        param.setFile(file);
        if(params[1] != null && params[1].length() >0){
        param.setChestId(params[1]);
        }
        UploadFileResult result = mAccessor.execute(Settings.FILE_URL, param, UploadFileResult.class);
        return result;
    }
    
    // 中断通信
    public void stop() {
        if(mAccessor != null) {
            mAccessor.stop();
        }
    }
    
}