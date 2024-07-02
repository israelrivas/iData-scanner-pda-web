package com.example.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.example.broadcastreceiver.network.MyWebSocketClient;

public class ScanReceiver extends BroadcastReceiver {

    private static final String TAG = "ScanReceiver";
    private MyWebSocketClient webSocketClient;

    public ScanReceiver() {
        webSocketClient = new MyWebSocketClient();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "Intent recibido: " + action);

        if ("android.intent.action.SCANRESULT".equals(action) || "com.jt.jitu.scan_receive_scan_action".equals(action)) {
            if (intent.getExtras() != null) {
                for (String key : intent.getExtras().keySet()) {
                    Object value = intent.getExtras().get(key);
                    Log.d(TAG, "Extra: " + key + " = " + value);
                }
            }
            String scanResult = intent.getStringExtra("JT_JITU_SCANDATA");
            if (scanResult != null && !scanResult.isEmpty()) {
                Log.d(TAG, "Resultado del escaneo: " + scanResult);
                Toast.makeText(context, "Resultado del escaneo: " + scanResult, Toast.LENGTH_LONG).show();

                // Enviar los datos escaneados al servidor WebSocket
                webSocketClient.sendMessage(scanResult);
                Log.d(TAG, "Mensaje enviado al WebSocket: " + scanResult);
            } else {
                Log.d(TAG, "Escaneo fallido: resultado vacío");
                Toast.makeText(context, "Escaneo fallido", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Acción no manejada: " + action);
        }
    }
}




