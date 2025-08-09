package com.example.myapplication.handler;

public final class MessageQueue {
    /** 消息队列头引用，也是下一条要处理消息的引用  **/
    Message mMessages;

    /** 标志当前队列是否处于阻塞状态  **/
    private boolean mBlocked;

    MessageQueue() {
    }

    /** 获取下一条消息，如果没有下一条能处理的消息将阻塞直到有下一条消息，线程被终止返回null **/
    Message next() {
        /** 等待时间。<0：表示不阻塞；0：表示一直阻塞直到被notify；其他数据表示阻塞时间。默认-1 **/
        int nextPollTimeoutMillis = -1;
        /** 利用死循环一直获取下一条消息，获取不到将阻塞 **/
        for (;;) {
            synchronized (this) {
                if (nextPollTimeoutMillis >= 0) {
                    /** 不为负数将阻塞 **/
                    try {
                        mBlocked = true;
                        wait(nextPollTimeoutMillis);
                    } catch (InterruptedException e) {
                        return null;
                    }
                }
                final long now = System.currentTimeMillis();
                /** 判断队列头消息 **/
                Message msg = mMessages;
                if (msg != null) {
                    if (now < msg.when) {
                        /** 队列头消息处理时间还没到，需要等待  **/
                        nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                    } else {
                        /** 队列头消息处理时间已到，队列头引用后移，前队列头消息脱离队列并返回  **/
                        mBlocked = false;
                        mMessages = msg.next;
                        msg.next = null;
                        return msg;
                    }
                } else {
                    /** 队列为空，将等待 **/
                    nextPollTimeoutMillis = 0;
                }
            }
        }
    }

    /** 消息入列方法，when为该消息处理的期望时刻 **/
    boolean enqueueMessage(Message msg, long when) {
        synchronized (this) {
            /** 将期望时刻复制到消息对应的属性上  **/
            msg.when = when;
            /** 获取当前队列头消息 **/
            Message p = mMessages;
            if (p == null || when == 0 || when < p.when) {
                /** 如果队列头为空（也就是队列为空）
                 * 或者将要插入的消息的期望处理时刻为0
                 * 或者比队列消息头期望处理时刻还早，那就插入到队列头 **/
                msg.next = p;
                mMessages = msg;
            } else {
                /** 查找对应位置插入 **/
                Message prev;
                for (;;) {
                    prev = p;
                    p = p.next;
                    if (p == null || when < p.when) {
                        break;
                    }
                }
                msg.next = p;
                prev.next = msg;
            }
            if (mBlocked) {
                /** 插入完毕，如果当前阻塞则唤醒 **/
                notifyAll();
            }
        }
        return true;
    }
}