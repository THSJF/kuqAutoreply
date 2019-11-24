package com.meng.tools;

import com.meng.Autoreply;

public class DeleteMessageRunnable implements Runnable {

    private int msgID;

    public DeleteMessageRunnable(int msgID) {
        this.msgID = msgID;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Autoreply.CQ.deleteMsg(msgID);
    }
}
