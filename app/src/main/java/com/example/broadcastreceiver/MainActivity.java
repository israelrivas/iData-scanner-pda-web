package com.example.broadcastreceiver;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTvText;
    private ScanReceiver scanReceiver;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvText = findViewById(R.id.tv_text);
        mTvText.setText("Resultado del escaneo:");

        Log.d(TAG, "MainActivity creada");

        // Inicializar y registrar ScanReceiver
        scanReceiver = new ScanReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCANRESULT");
        filter.addAction("com.jt.jitu.scan_receive_scan_action");
        registerReceiver(scanReceiver, filter);

        Log.d(TAG, "ScanReceiver registrado");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scanReceiver != null) {
            unregisterReceiver(scanReceiver);
        }
    }
}












// package com.example.broadcastreceiver;

// import android.os.Bundle;
// import android.view.View;
// import android.widget.TextView;
// import androidx.annotation.Nullable;
// import androidx.appcompat.app.AppCompatActivity;
// import com.example.broadcastreceiver.function.IDataScan;
// import android.util.Log;
// import com.example.broadcastreceiver.R;
// import android.content.Context;
// import android.content.Intent;
// import android.content.IntentFilter;
// import com.example.broadcastreceiver.function.IDataScan.OnScanListener;


// public class MainActivity extends AppCompatActivity {

//     private TextView mTvText;
//     private IDataScan mIDataScan;

//     @Override
//     protected void onCreate(@Nullable Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_main);

//         mTvText = findViewById(R.id.tv_text);
//         mTvText.setText("扫描结果:");

//         initScan();
//     }

//     private void initScan() {
//         mIDataScan = new IDataScan();
//         mIDataScan.initScan(this, new OnScanListener() {
//             @Override
//             public void scanSuccess(String code) {
//                 // Decoding successful
//                 mTvText.setText("扫描结果: " + code);
//             }

//             @Override
//             public void scanFailed(String message) {
//                 // Decoding failed
//                 mTvText.setText("扫描失败");
//             }
//         });
//     }

//     @Override
//     protected void onDestroy() {
//         super.onDestroy();
//         if (mIDataScan != null) {
//             mIDataScan.destroy();
//         }
//     }
// }





// // package com.example.broadcastreceiver;
// // import com.example.broadcastreceiver.R;
// // import android.content.BroadcastReceiver;
// // import android.content.Context;
// // import android.content.Intent;
// // import android.content.IntentFilter;
// // import android.os.Bundle;
// // import android.widget.TextView;
// // import androidx.annotation.Nullable;
// // import androidx.appcompat.app.AppCompatActivity;

// // public class MainActivity extends AppCompatActivity {

// //     private static final String RES_ACTION = "android.intent.action.SCANRESULT";
// //     private TextView mTvText;
// //     private BroadcastReceiver mScanReceiver;

// //     @Override
// //     protected void onCreate(@Nullable Bundle savedInstanceState) {
// //         super.onCreate(savedInstanceState);
// //         setContentView(R.layout.activity_main);

// //         mTvText = findViewById(R.id.tv_text);
// //         mTvText.setText("扫描结果:");

// //         initScan();
// //     }

// //     private void initScan() {
// //         mScanReceiver = new BroadcastReceiver() {
// //             @Override
// //             public void onReceive(Context context, Intent intent) {
// //                 String action = intent.getAction();
// //                 if (RES_ACTION.equals(action)) {
// //                     String scanResult = intent.getStringExtra("value");
// //                     if (scanResult != null && scanResult.length() > 0) {
// //                         mTvText.setText("扫描结果: " + scanResult);
// //                     } else {
// //                         mTvText.setText("扫描失败");
// //                     }
// //                 }
// //             }
// //         };
// //         IntentFilter filter = new IntentFilter();
// //         filter.addAction(RES_ACTION);
// //         registerReceiver(mScanReceiver, filter);
// //     }

// //     @Override
// //     protected void onDestroy() {
// //         super.onDestroy();
// //         if (mScanReceiver != null) {
// //             unregisterReceiver(mScanReceiver);
// //         }
// //     }
// // }



