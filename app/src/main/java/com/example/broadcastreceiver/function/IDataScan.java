package com.example.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class IDataScan {

    private static final String TAG = "IDataScan";
    private static final String RES_ACTION = "android.intent.action.SCANRESULT";
    private Context context;
    private OnScanListener mOnScanListener;

    public IDataScan(Context context) {
        this.context = context;
        initScan();
    }

    private void initScan() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(RES_ACTION);
        context.registerReceiver(mScanReceiver, filter);
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String scanResult = intent.getStringExtra("value");
            if (RES_ACTION.equals(action)) {
                if (scanResult != null && scanResult.length() > 0) {
                    if (mOnScanListener != null) {
                        mOnScanListener.scanSuccess(scanResult);
                        Log.d(TAG, "Escaneo exitoso: " + scanResult);
                    }
                } else {
                    if (mOnScanListener != null) {
                        mOnScanListener.scanFailed("Decodificación fallida");
                    }
                    Log.d(TAG, "Decodificación fallida");
                }
            }
        }
    };

    public void setOnScanListener(OnScanListener listener) {
        this.mOnScanListener = listener;
    }

    public interface OnScanListener {
        void scanSuccess(String result);
        void scanFailed(String error);
    }

    // Es importante también añadir métodos para liberar el BroadcastReceiver cuando ya no sea necesario
    public void unregisterReceiver() {
        context.unregisterReceiver(mScanReceiver);
    }
}