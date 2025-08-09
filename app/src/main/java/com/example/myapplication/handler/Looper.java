package com.example.myapplication.handler;

public final class Looper {
    /** 线程私有数据，用于保存线程对应的Looper。底下会建立一个Map，将线程与Looper实现映射关系 **/
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();

    /** 该Looper对应的消息队列  **/
    final MessageQueue mQueue;

    /** 该Looper对应的处理线程  **/
    final Thread mThread;

    /** 初始化函数。该函数由哪一个线程调用，将为该线程初始化一个Looper **/
    public static void prepare() {
        if (sThreadLocal.get() != null) {
            /** 该 线程已经映射了一个Looper了，抛出异常 **/
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        /** 初始化一个Looper，并利用ThreadLocal关联关系 **/
        sThreadLocal.set(new Looper());
    }

    /** 启动消息泵，获取和处理消息。该函数由哪一个线程调用，将是启动该线程的消息泵 **/
    public static void loop() {
        /** 获取调用线程的消息泵 **/
        final Looper me = myLooper();
        if (me == null) {
            /** 获取不到消息泵，抛出异常 **/
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        /** 获取消息泵对应的队列 **/
        final MessageQueue queue = me.mQueue;
        /** 利用死循环获取和处理消息 **/
        for (;;) {
            /** 获取下一条消息，如果没有下一条能处理的消息将阻塞直到有下一条消息 **/
            Message msg = queue.next();
            if (msg == null) {
                return;
            }
            /** 将该消息交由对应的Handler处理  **/
            msg.target.dispatchMessage(msg);
        }
    }

    /** 返回本县城对应的Looper **/
    public static Looper myLooper() {
        return sThreadLocal.get();
    }

    private Looper() {
        mQueue = new MessageQueue();
        mThread = Thread.currentThread();
    }

    public Thread getThread() {
        return mThread;
    }
}
