package com.yll.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";

    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mMessenger.getBinder();
    }

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyContants.MSG_FROM_CLIENT:
                    Log.i(TAG, "receive msg from client " + msg.getData().getString("msg"));

                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, MyContants.MSG_FROM_SERVER);
                    Bundle data = new Bundle();
                    data.putString("reply", "嗯，你的消息我已经收到，稍后会回复您。");
                    replyMessage.setData(data);

                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
