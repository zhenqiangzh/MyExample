package com.example.super_yu.myexample.serialp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.super_yu.myexample.R;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import weiqian.hardware.SerialPort;

public class SerialPort2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "whsgzcy";

    private TextView mSerial;
    private TextView mBaud;
    private TextView mData;
    private TextView mCompile;
    private TextView mStop;
    private TextView mState;
    private Button mSend;
    private Button mOpen;
    private Button mClose;

    private SerialPort mSerialPort;

    private boolean mIsStop = false;

    private Button red;
    private Button yellow;
    private Button green;
    private Button light;

    private boolean mIsLight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_port2);

        mSerial = (TextView) findViewById(R.id.serial);
        mBaud = (TextView) findViewById(R.id.baud);
        mData = (TextView) findViewById(R.id.data);
        mCompile = (TextView) findViewById(R.id.compile);
        mStop = (TextView) findViewById(R.id.stop);
        mState = (TextView) findViewById(R.id.state);

        mSerial.setText("com1");
        mBaud.setText("9600");
        mData.setText("8");
        mCompile.setText("none");
        mStop.setText("1");

        mSend = (Button) findViewById(R.id.send);

        mOpen = (Button) findViewById(R.id.open);
        mClose = (Button) findViewById(R.id.close);
        light = (Button) findViewById(R.id.light);

        mSend.setOnClickListener(this);
        mOpen.setOnClickListener(this);
        mClose.setOnClickListener(this);
        light.setOnClickListener(this);

        mSend.setEnabled(false);
        mClose.setEnabled(false);

        mSerialPort = new SerialPort();

        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        Runnable command = new Runnable() {
            @Override
            public void run() {
                if (mIsStop) {

                    String msg = mSerialPort.read();

                    Message message = new Message();
                    message.what = 1;
                    message.obj = msg;

                    mHandler.sendMessage(message);

                    byte[] d = HexToByte("aa");
                    int m = d.length;
                    int f = mSerialPort.write(d, m);
                }
            }
        };
        scheduledThreadPoolExecutor.scheduleAtFixedRate(command, 1, 1,
                TimeUnit.SECONDS);

        ScheduledThreadPoolExecutor lightScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        Runnable lightCommand = new Runnable() {
            @Override
            public void run() {
                if (mIsLight) {

                    byte[] rry = HexToByte("f6");
                    int ry = rry.length;
                    int y = mSerialPort.write(rry, ry);

                    byte[] gy = HexToByte("05");
                    int ly = gy.length;
                    int by = mSerialPort.write(gy, ly);

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            byte[] ggy = HexToByte("f5");
                            int lly = ggy.length;
                            int bby = mSerialPort.write(ggy, lly);

                            byte[] gy = HexToByte("06");
                            int ly = gy.length;
                            int by = mSerialPort.write(gy, ly);
                        }
                    }, 500);

                }
            }
        };
        lightScheduledThreadPoolExecutor.scheduleAtFixedRate(lightCommand, 1, 1,
                TimeUnit.SECONDS);


        red = (Button) findViewById(R.id.red);
        yellow = (Button) findViewById(R.id.yellow);
        green = (Button) findViewById(R.id.green);

        red.setOnClickListener(this);
        yellow.setOnClickListener(this);
        green.setOnClickListener(this);

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mState.setText(msg.obj.toString());
                    Log.d("ww", msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.send:
                byte[] c = HexToByte("01");
                int l = c.length;
                int b = mSerialPort.write(c, l);
                Log.d(TAG, "SerialPort2Activity send = " + b);

                mSend.setClickable(false);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        byte[] cc = HexToByte("f1");
                        int ll = cc.length;
                        int bb = mSerialPort.write(cc, ll);
                        mSend.setClickable(true);
                    }
                }, 1000);

                break;
            case R.id.open:
                mSend.setEnabled(true);
                mClose.setEnabled(true);
                mOpen.setEnabled(false);
                mSerialPort.open("COM1", 9600, 8, "None", 1);
                mIsStop = true;
                break;
            case R.id.close:
                mOpen.setEnabled(true);
                mSend.setEnabled(false);
                mClose.setEnabled(false);
                mSerialPort.close();
                mIsStop = false;
                break;
            case R.id.red:
                if (r == 0) {
                    r = 1;
                    byte[] cred = HexToByte("04");
                    int lred = cred.length;
                    int bred = mSerialPort.write(cred, lred);
                    Log.d(TAG, "SerialPort2Activity send = " + bred);
                    return;
                }
                r = 0;
                byte[] cred = HexToByte("f4");
                int lred = cred.length;
                int bred = mSerialPort.write(cred, lred);
                Log.d(TAG, "SerialPort2Activity send = " + bred);
                break;
            case R.id.yellow:
                if (y == 0) {
                    y = 1;
                    byte[] cy = HexToByte("05");
                    int ly = cy.length;
                    int by = mSerialPort.write(cy, ly);
                    Log.d(TAG, "SerialPort2Activity send = " + by);
                    return;
                }
                y = 0;
                byte[] cy = HexToByte("f5");
                int ly = cy.length;
                int by = mSerialPort.write(cy, ly);
                Log.d(TAG, "SerialPort2Activity send = " + by);
                break;
            case R.id.green:
                if (g == 0) {
                    g = 1;
                    byte[] cg = HexToByte("06");
                    int lg = cg.length;
                    int bg = mSerialPort.write(cg, lg);
                    Log.d(TAG, "SerialPort2Activity send = " + bg);
                    return;
                }
                g = 0;
                byte[] cg = HexToByte("f6");
                int lg = cg.length;
                int bg = mSerialPort.write(cg, lg);
                Log.d(TAG, "SerialPort2Activity send = " + bg);
                break;

            case R.id.light:

                if (mIsLight) {

                    mIsLight = false;

                    byte[] rry = HexToByte("f6");
                    int ry = rry.length;
                    int y = mSerialPort.write(rry, ry);

                    byte[] gy = HexToByte("f5");
                    int my = gy.length;
                    mSerialPort.write(gy, my);

                } else {
                    mIsLight = true;
                }

                break;
        }
    }

    int r = 0;
    int y = 0;
    int g = 0;

    //16进制字符串转换为byte[]
    public byte[] HexToByte(String hexString) {
        int len = hexString.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return b;
    }

//    protected void onDataReceived(byte[] buffer, int size) {
//        final byte[] nbuffer = buffer.clone();
//        final int nsize = size;
//        String msg = bytesToHexString(nbuffer, nsize);
//        Message message = new Message();
//        message.obj = msg;
//        message.what = 1;
//        mHandler.sendMessage(message);
//    }
//
//    private String bytesToHexString(byte[] bArray, int size) {
//
//        return new String(bArray);

//        StringBuffer strBuf = new StringBuffer(bArray.length);
//        String strTemp;
//        for (int i = 0; i < size; i++) {
//            strTemp = Integer.toHexString(0xAA & bArray[i]);
//            strBuf.append("0x");
//            if (strTemp.length() < 2) {
//                strBuf.append(0);
//            }
//            strBuf.append(strTemp.toUpperCase());
//            strBuf.append(" ");
//        }
//        return strBuf.toString();
//    }
}
