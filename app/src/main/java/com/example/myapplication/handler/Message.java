package com.example.myapplication.handler;

public final class Message {
    /** 消息携带的处理数据(Demo只携带一个整型数据) **/
    public int what;

    /** 以下3个字段属于内部标志属性 **/
    /** 消息处理期望时刻，用于消息队列对消息排序和处理时机的判断，
     * 需要插入队列头部就将该参数设置为0，通常赋值为发送时间 **/
    long when;
    /** 消息发送和处理的Handler **/
    Handler target;
    /** 消息队列中下一条消息的引用 **/
    Message next;

    /** 获取消息API **/
    public static Message obtain() {
        return new Message();
    }
    public static Message obtain(Handler h) {
        Message m = obtain();
        m.target = h;
        return m;
    }
}