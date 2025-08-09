package com.example.myapplication.handler;

public class LooperThread extends Thread {
    /** 保存该线程对应的Looper  **/
    Looper mLooper;
    public LooperThread(String name) {
        super(name);
    }
    @Override
    public void run() {
        /** 初始化Looper **/
        Looper.prepare();
        synchronized (this) {
            /** 初始化Looper，并通知唤醒等待者（getLooper可能存在等待） **/
            mLooper = Looper.myLooper();
            notifyAll();
        }
        /** 启动消息泵，处理或者等待处理消息  **/
        Looper.loop();
    }

    /** 获取消息处理线程对应的消息泵（该方法一般为发送线程调用），
     * 由于该消息泵为消息处理线程之后才能初始化，
     * 考虑到获取的是的时候可能还没有初始化，所以内部利用循环等待方式获取  **/
    public Looper getLooper() {
        /** 如果线程已死，则返回空  **/
        if (!isAlive()) {
            return null;
        }
        synchronized (this) {
            /** 循环等待 **/
            while (isAlive() && mLooper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return mLooper;
    }
}