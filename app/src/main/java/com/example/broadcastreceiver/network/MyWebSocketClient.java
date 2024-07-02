package com.example.broadcastreceiver.network;

import android.util.Log;
import java.net.URI;
import tech.gusavila92.websocketclient.WebSocketClient;

public class MyWebSocketClient {

    private static final String TAG = "MyWebSocketClient";
    private WebSocketClient webSocketClient;
    private boolean isConnected = false;

    public MyWebSocketClient() {
        createWebSocketClient();
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI("ws://192.168.1.46:8080");  // Cambiar al puerto 8080
        } catch (Exception e) {
            Log.e(TAG, "URI error: " + e.getMessage());
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.d(TAG, "WebSocket opened");
                isConnected = true;
            }

            @Override
            public void onTextReceived(String message) {
                Log.d(TAG, "Message received: " + message);
            }

            @Override
            public void onBinaryReceived(byte[] data) {}

            @Override
            public void onPingReceived(byte[] data) {}

            @Override
            public void onPongReceived(byte[] data) {}

            @Override
            public void onException(Exception e) {
                Log.e(TAG, "WebSocket error: " + e.getMessage());
                isConnected = false;
            }

            @Override
            public void onCloseReceived() {
                Log.d(TAG, "WebSocket closed");
                isConnected = false;
            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage(String message) {
        if (webSocketClient != null && isConnected) {
            webSocketClient.send(message);
            Log.d(TAG, "Message sent: " + message);
        } else {
            Log.e(TAG, "WebSocket not connected");
        }
    }
}
