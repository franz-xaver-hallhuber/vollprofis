package com.example.carol.inozzle;

import com.example.carol.inozzle.util.SystemUiHider;
import com.squareup.picasso.Picasso;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.ClientInfoStatus;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MenuActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;
    public static View contentView;
    private Log logger;
    public String pictoshow;
    public Handler cHandler;

    //screen elements
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        contentView = findViewById(R.id.imageView);

        test = (TextView) findViewById(R.id.textView);

        cHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String serverInput = (String) msg.getData().getString("message");

                if (null != serverInput) {
                    setPicture(serverInput);
                }

            }
        };

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        readFromServer();
    }

    public void setPicture(String s) {
        Log.i("ManiUI","Set Picture " + s);
        test.setText(s);

        switch (s) {
            case "call":
                replace(R.drawable.callingmrsrobinson);
                break;
            case "navigate":
                replace(R.drawable.navigasstation);
                break;
            case "-1":
                replace(R.drawable.intro);
                break;
            case "0x":
                replace(R.drawable.menu0x);
                break;
            case "1x":
                replace(R.drawable.menu1x);
                break;
            case "2x":
                replace(R.drawable.menu2x);
                break;
            case "00":
                replace(R.drawable.menu00);
                break;
            case "01":
                replace(R.drawable.menu01);
                break;
            case "02":
                replace(R.drawable.menu02);
                break;
            case "10":
                replace(R.drawable.menu10);
                break;
            case "11":
                replace(R.drawable.menu11);
                break;
            case "12":
                replace(R.drawable.menu12);
                break;
            case "20":
                replace(R.drawable.menu20);
                break;
            case "21":
                replace(R.drawable.menu21);
                break;
            case "22":
                replace(R.drawable.menu21);
                break;
            case "000":
                replace(R.drawable.menu000);
                break;
            case "001":
                replace(R.drawable.menu001);
                break;
            case "002":
                replace(R.drawable.menu002);
                break;
            case "010":
                replace(R.drawable.menu010);
                break;
            case "011":
                replace(R.drawable.menu011);
                break;
            case "012":
                replace(R.drawable.menu012);
                break;
            case "100":
                replace(R.drawable.menu100);
                break;
            case "101":
                replace(R.drawable.menu101);
                break;
            case "102":
                replace(R.drawable.menu102);
                break;
            case "110":
                replace(R.drawable.menu110);
                break;
            case "111":
                replace(R.drawable.menu111);
                break;
            case "112":
                replace(R.drawable.menu112);
                break;
            case "120":
                replace(R.drawable.menu120);
                break;
            case "121":
                replace(R.drawable.menu121);
                break;
            case "122":
                replace(R.drawable.menu122);
                break;
            case "200":
                replace(R.drawable.menu200);
                break;
            case "201":
                replace(R.drawable.menu201);
                break;
            case "202":
                replace(R.drawable.menu202);
                break;
            case "210":
                replace(R.drawable.menu210);
                break;
            case "211":
                replace(R.drawable.menu211);
                break;
            case "212":
                replace(R.drawable.menu212);
                break;
            case "220":
                replace(R.drawable.menu220);
                break;
            case "221":
                replace(R.drawable.menu221);
                break;
            case "222":
                replace(R.drawable.menu222);
                break;
            default:
                Log.e("Main Handler","No Such Code");
                break;
        }
    }

    private void replace(int res) {
        Picasso.with(this).load(res).fit().noFade().into((ImageView) contentView);
    }

    private void readFromServer() {

        Thread th = new Thread(new Runnable() {

            String ip = "192.168.188.27";
            int port = 5005;

            Socket client;
            BufferedReader inS;

            ByteArrayOutputStream bos;

            String inLine;

            @Override
            public void run() {
                try {
                    Log.i("Client","Attempt Connect");

                    bos = new ByteArrayOutputStream(512);
                    client = new Socket(ip, port);
                    Log.i("Client", "Created Socket");

                    //inS = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    InputStream inStr = client.getInputStream();
                    Log.i("Client", "Create Buffered Reader");

                    Log.i("Client", "Start receiving data");

                    byte[] temp = new byte[512];
                    int bytesread;

                    while ((bytesread = inStr.read(temp)) != -1) {
                        bos.write(temp, 0, bytesread);
                        Log.i("Received from Server:", bos.toString("utf-8"));
                        String res = bos.toString("utf-8");
                        bos.reset();

                        if (res != null && res != "") {
                            Message msg = cHandler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message",res);
                            msg.setData(b);
                            cHandler.sendMessage(msg);
                        }
                    }

                    Log.i("Client","Connection Close");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


}


