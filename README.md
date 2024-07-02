# iData scanner pda web
 Desarrollo de escaneo de pda de la marca iData

En muchos escenarios comerciales, se involucrará el escaneo de códigos QR y PDA se enfoca en escanear dispositivos móviles. Las diferentes marcas de PDA tendrán diferentes métodos de llamada de escaneo. Hoy hablaremos sobre cómo obtener resultados de escaneo bajo la marca iData.

Los temas de hoy incluyen:

Cómo iData pda obtiene los resultados del escaneo
Introducción a la clase de paquete de escaneo IDataScan
El uso del escaneo en MainActivity
Código fuente de ScannerInterface
Representaciones y diagramas de estructura del proyecto.
Primero echemos un vistazo a las representaciones.

1. Cómo obtiene la pda iData los resultados del análisis
Después de ser compatible con múltiples PDA, existen aproximadamente dos formas de obtener los resultados del escaneo de PDA. Una es obtener la integración del código a través del paquete jar proporcionado por el fabricante y la otra es obtener los resultados del escaneo a través de la transmisión, iDta PDA. De lo que vamos a hablar hoy es de obtener resultados de escaneo a través de transmisión.

2. Introducción a la clase de paquete de escaneo IDataScan
IDataScan encapsula principalmente una clase de recepción de transmisión para recibir datos escaneados, que involucra principalmente varios métodos que deben usarse para llamadas externas:
//初始化，在listener接口中接收扫描返回值
void initScan(Context context,OnScanListener listener)
//扫描结束后，注销扫描相关的广播
void destroy()
IDataScan全部代码如下：

package com.example.function;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.util.LogUtil;
/**
 * Description:
 * <p>
 * Author:pei
 * Date: 2019/8/23
 */
public class IDataScan {
    private static final String RES_ACTION = "android.intent.action.SCANRESULT";
    private ScannerInterface mScannerInterface;
    private Context mContext;
    private OnScanListener mOnScanListener;
    //注册广播接受者
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            //注意Extral为"value"
            String scanResult = intent.getStringExtra("value");
            if(RES_ACTION.equals(RES_ACTION)){
                if(scanResult.length()>0){
                    //如果条码长度>0，解码成功,否则失败
                   if(mOnScanListener!=null){
                       mOnScanListener.scanSuccess(scanResult);
                   }
                }else{//解码失败
                    mOnScanListener.scanFailed("解码失败");
                }
            }
        }
    };
    public void initScan(Context context,OnScanListener listener){
        this.mContext=context;
        this.mOnScanListener=listener;
        mScannerInterface = new ScannerInterface(context);
        //设置扫描结果的输出模式
        mScannerInterface.setOutputMode(1);
        IntentFilter filter=new IntentFilter();
        filter.addAction(RES_ACTION);
        context.registerReceiver(mScanReceiver, filter);
    }
    public void destroy(){
        LogUtil.i("=======注销idata品牌pda广播=========");
        if(mScanReceiver!=null&&mContext!=null) {
            mContext.unregisterReceiver(mScanReceiver);
        }
    }
    public interface OnScanListener{
        void scanSuccess(String code);
        void scanFailed(String message);
    }
}
三. 扫描在 MainActivity 中的使用
在MainActivity中使用扫描的时候，先要初始化 IDataScan 类

mIDataScan=new IDataScan();
然后，调用IDataScan 的init方法，用于注册扫描广播并接收扫描结果返回值

        mIDataScan.initScan(this, new IDataScan.OnScanListener() {
            @Override
            public void scanSuccess(String code) {
                //解码成功
                LogUtil.i("=======解码成功==gg===="+code);
            }
            @Override
            public void scanFailed(String message) {
                //解码失败
                LogUtil.i("=======解码失败==gg===="+message);
            }
        });
当界面要退出的时候，注销扫描广播

        if (mIDataScan!=null){
            mIDataScan.destroy();
        }
下面贴出MainActivity代码：

package com.example.testdemo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.function.IDataScan;
import com.util.LogUtil;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvText;
    private IDataScan mIDataScan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        setListener();
    }
    private void initData() {
        mTvText = findViewById(R.id.tv_text);
        mTvText.setText("扫描结果:");
        initScan();
    }
    private void initScan(){
        mIDataScan=new IDataScan();
        mIDataScan.initScan(this, new IDataScan.OnScanListener() {
            @Override
            public void scanSuccess(String code) {
                //解码成功
                LogUtil.i("=======解码成功==gg===="+code);
                mTvText.setText("扫描结果:"+code);
            }
            @Override
            public void scanFailed(String message) {
                //解码失败
                LogUtil.i("=======解码失败==gg===="+message);
                mTvText.setText("扫描失败");
            }
        });
    }
    private void setListener() {
    }
    @Override
    public void onClick(View v) {
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIDataScan!=null){
            mIDataScan.destroy();
        }
    }
}
