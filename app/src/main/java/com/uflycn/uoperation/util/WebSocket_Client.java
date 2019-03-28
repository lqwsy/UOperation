package com.uflycn.uoperation.util;


import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by Administrator on 2017/12/4.
 */
public class WebSocket_Client extends WebSocketClient {
    private static final String TAG = WebSocket_Client.class.getSimpleName();

//    private static final String TAG = "WebSocket_Client";

    public WebSocket_Client(URI serverUri) {
        super(serverUri);
    }

    public WebSocket_Client(URI serverUri, Draft protocolDraft) {
        super(serverUri, protocolDraft);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        //Log.i(TAG, "状态: " + "连接到服务器");
    }

    @Override
    public void onMessage(String message) {
       // Log.i(TAG, "状态: " + "收到消息" + message);
        onRecMessage.onNotificationContent(message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
       // Log.i(TAG, "状态: " + "关闭连接");
        Log.d(TAG, "onClose: ");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void setRecviveMessage(onMessageCallBack callback){
        onRecMessage = callback;
    }

    onMessageCallBack onRecMessage = null;

    public interface onMessageCallBack{
        void onNotificationContent(String msg);
    }

}
