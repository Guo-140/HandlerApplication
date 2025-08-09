package com.example.myapplication;


import android.util.Log;

import com.example.myapplication.base.BaseActivity;
import com.example.myapplication.handler.Handler;
import com.example.myapplication.handler.LooperThread;
import com.example.myapplication.handler.Message;


public class MainActivity extends BaseActivity {
    public static final int WHAT_1 = 1;
    public static final int WHAT_2 = 2;
    @Override
    protected void init() {
        /** 创建，启动 消息处理线程  **/
        LooperThread ht = new LooperThread("handleThread");
        ht.start();

        /** 创建Handler，并重写处理消息方法  **/
        final Handler h1 = new Handler(ht.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_1:
                        append(WHAT_1 + ": " + System.currentTimeMillis());
                        break;
                    case WHAT_2:
                        append(WHAT_2 + ": " + System.currentTimeMillis());
                        break;
                    default:
                        break;
                }
                Log.i(TAG, "time:" + System.currentTimeMillis() +
                        "; thread:" + Thread.currentThread().getName() +
                        "; message what:" + msg.what);
            }
        };

        createBtn("send 1", view -> {
            append("");
            Message msg1 = h1.obtainMessage();
            msg1.what = WHAT_1;
            h1.sendMessage(msg1);
        });
        createBtn("send 2", view -> {
            append("");
            Message msg1 = h1.obtainMessage();
            msg1.what = WHAT_2;
            h1.sendMessage(msg1);
        });
    }
}