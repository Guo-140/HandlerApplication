package com.example.myapplication.handler;

public class Handler {
    /** 处理线程对应的消息队列 **/
    final MessageQueue mQueue;

    /** 处理线程对应的消息获取泵 **/
    final Looper mLooper;

    /** 处理消息函数（一般需要子类重写来处理消息） **/
    public void handleMessage(Message msg) {
    }

    /** 消息分发，直接调用处理函数消息 **/
    void dispatchMessage(Message msg) {
        handleMessage(msg);
    }

    public Handler(Looper looper) {
        mLooper = looper;
        mQueue = looper.mQueue;
    }

    /** 获取 消息 **/
    public final Message obtainMessage() {
        return Message.obtain(this);
    }

    /** 立刻发送消息 **/
    public final boolean sendMessage(Message msg) {
        return sendMessageDelayed(msg, 0);
    }


    /** 延时发送消息（其实也是立刻发送，只是延时处理而已，原因上一个类 消息队列 已经说明） **/
    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return sendMessageAtTime(msg, System.currentTimeMillis() + delayMillis);
    }

    /** 指定时刻发送消息（其实也是立刻发送，只是控制了处理时刻） **/
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            return false;
        }
        return enqueueMessage(queue, msg, uptimeMillis);
    }

    /** 将消息发送到队列头（也就是期望处理时间设置为0） **/
    public final boolean sendMessageAtFrontOfQueue(Message msg) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            return false;
        }
        return enqueueMessage(queue, msg, 0);
    }

    /** 内部发送消息方法 **/
    private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;
        return queue.enqueueMessage(msg, uptimeMillis);
    }
}