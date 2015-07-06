package com.example.carol.inozzle;

import com.example.carol.inozzle.util.SystemUiHider;
import com.squareup.picasso.Picasso;

import android.annotation.TargetApi;
import android.app.Activity;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


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

    public static TextView left;
    public static TextView center;
    public static TextView right;

    public static TextView topLeft;
    public static TextView topCenter;
    public static TextView topRight;

    public static TextView bottomLeft;
    public static TextView bottomCenter;
    public static TextView bottomRight;


    //screen elements
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        contentView = findViewById(R.id.imageView);

        left = (TextView)findViewById(R.id.option_left);
        center = (TextView)findViewById(R.id.option_center);
        right = (TextView)findViewById(R.id.option_right);

        topLeft = (TextView)findViewById(R.id.top_left);
        topCenter = (TextView)findViewById(R.id.top_center);
        topRight = (TextView)findViewById(R.id.top_right);

        bottomLeft = (TextView)findViewById(R.id.bottom_left);
        bottomCenter = (TextView)findViewById(R.id.bottom_center);
        bottomRight = (TextView)findViewById(R.id.bottom_right);

        test = (TextView) findViewById(R.id.textView);

        //runTest();


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
        Log.i("ManiUI", "Set Picture " + s);
        //test.setText(s);

        switch (s) {
            case "call":
                replace(R.drawable.callingmrsrobinson);
                break;
            case "navigate":
                replace(R.drawable.navigasstation);
                break;
            case "-1":
                resetDisplay();
                replace(R.drawable.intro01);
                break;
            case "0x":
                //telephone
                resetDisplay();
                resetTextColorMain();
                replace(R.drawable.call);
                editTextLabelsTop("","","");
                editTextLabelsMain("Redial", "Missed Calls", "Favorites");
                break;
            case "1x":
                //media
                resetDisplay();
                resetTextColorMain();
                replace(R.drawable.media);
                //replace(R.drawable.media);
                editTextLabelsTop("","","");
                editTextLabelsMain("CD", "External Devices", "Radio");
                break;
            case "2x":
                //navigation
                //replace(R.drawable.menu2x);
                //replace(R.drawable.nav);
                resetDisplay();
                resetTextColorMain();
                replace(R.drawable.nav);
                editTextLabelsTop("", "", "");
                editTextLabelsMain("Previous Destinations", "Favorites", "Points of Interest");
                break;
            case "00":
                //redial
                //replace(R.drawable.menu00);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_left);
                editTextLabelsTop("Sonja Süßmilch", "Mrs. Robinson", "Blixa Reed");
                break;
            case "01":
                //missed calls
                //replace(R.drawable.menu01);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_center);
                editTextLabelsTop("Donald Morris", "Mrs. Robinson", "+49 6666 66666");
                break;
            case "02":
                //favorites (telephone)
                //replace(R.drawable.menu02);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_right);
                editTextLabelsTop("J. H. Joplin", "Mrs. Robinson", "Ing. J. Shiwago");
                break;
            case "10":
                //CD
                //replace(R.drawable.menu10);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_left);
                editTextLabelsTop("Start from beginning", "Continue", "Random");
                break;
            case "11":
                //External Devices
                //replace(R.drawable.menu11);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_center);
                editTextLabelsTop("USB", "AUX", "Bluetooth");
                break;
            case "12":
                //Radio
                //replace(R.drawable.menu12);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_right);
                editTextLabelsTop("Sight Ways 33.3", "Zero Zone 00.0", "Mercury 666.6");
                break;
            case "20":
                //Prevoious destinations
                //replace(R.drawable.menu20);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_left);
                editTextLabelsTop("66 Battery Ave, NY 66666", "1 North End Ave, NY 10001", "0 Rue Courbet, 75116 Paris");
                break;
            case "21":
                //Favorites (Nav)
                //replace(R.drawable.menu21);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_center);
                editTextLabelsTop("55 5th Ave, NY 10055", "00 Cheers Ave, NY 10000", "137 Leo Road, QLD 4880");
                break;
            case "22":
                //Points of interest
                //replace(R.drawable.menu21);
                resetTextColorMain();
                resetTextColorTop();
                highlightTextLabel(R.id.option_right);
                editTextLabelsTop("Gas Station", "Coffee Shop", "Liquid Store");
                break;
            case "000":
                //Sonja Süßmilch
                //replace(R.drawable.menu000);
                replace(R.drawable.call01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "001":
                //Mrs. Robinson
                //replace(R.drawable.menu001);
                replace(R.drawable.call02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "002":
                //Blixa Reed
                //replace(R.drawable.menu002);
                replace(R.drawable.call03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "010":
                //Donald Morris
                //replace(R.drawable.menu010);
                replace(R.drawable.call01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "011":
                //Mrs. Robinson
                //replace(R.drawable.menu011);
                replace(R.drawable.call02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "012":
                //+49 6666 66666
                //replace(R.drawable.menu012);
                replace(R.drawable.call03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "020":
                //J. H. Joplin
                replace(R.drawable.call01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "021":
                //Mrs. Robinson
                replace(R.drawable.call02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "022":
                //Ing. J. Shiwago
                replace(R.drawable.call03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "100":
                //Start from beginning
                //replace(R.drawable.menu100);
                replace(R.drawable.media01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "101":
                //Continue
                //replace(R.drawable.menu101);
                replace(R.drawable.media02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "102":
                //Random
                //replace(R.drawable.menu102);
                replace(R.drawable.media03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "110":
                //USB
                //replace(R.drawable.menu110);
                replace(R.drawable.media01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "111":
                //AUX
                //replace(R.drawable.menu111);
                replace(R.drawable.media02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "112":
                //Bluetooth
                //replace(R.drawable.menu112);
                replace(R.drawable.media03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "120":
                //Sight Ways 33.3
                //replace(R.drawable.menu120);
                replace(R.drawable.media01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "121":
                //Zero Zone 00.0
                //replace(R.drawable.menu121);
                replace(R.drawable.media02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "122":
                //Mercury 666.6
                //replace(R.drawable.menu122);
                replace(R.drawable.media03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "200":
                //66 Battery Ave, NY 66666
                //replace(R.drawable.menu200);
                replace(R.drawable.nav01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "201":
                //1 North End Ave, NY 10001
                //replace(R.drawable.menu201);
                replace(R.drawable.nav02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "202":
                //0 Rue Courbet, 75116 Paris
                //replace(R.drawable.menu202);
                replace(R.drawable.nav03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "210":
                //55 5th Ave, NY 10055
                //replace(R.drawable.menu210);
                replace(R.drawable.nav01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "211":
                //00 Cheers Ave, NY 10000
                //replace(R.drawable.menu211);
                replace(R.drawable.nav02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "212":
                //137 Leo Road, QLD 4880
                //replace(R.drawable.menu212);
                replace(R.drawable.nav03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "220":
                //Gas Station
                //replace(R.drawable.menu220);
                replace(R.drawable.nav01);
                resetTextColorTop();
                highlightTextLabel(R.id.top_left);
                break;
            case "221":
                //Coffee Shop
                //replace(R.drawable.menu221);
                replace(R.drawable.nav02);
                resetTextColorTop();
                highlightTextLabel(R.id.top_center);
                break;
            case "222":
                //Liquid Store
                //replace(R.drawable.menu222);
                replace(R.drawable.nav03);
                resetTextColorTop();
                highlightTextLabel(R.id.top_right);
                break;
            case "000x":
                //select Sonja Süßmilch
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling Sonja Süßmilch");
                //highlightTextLabel(R.id.bottom_left);

                break;
            case "001x":
                //select Mrs. Robinson
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling Mrs. Robinson");
                //highlightTextLabel(R.id.bottom_left);

                break;
            case "002x":
                //Blixa Reed
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling Blixa Reed");
                //highlightTextLabel(R.id.bottom_left);

                break;
            case "010x":
                //Donald Morris
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling Donald Morris");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "011x":
                //Mrs. Robinson
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling Mrs. Robinson");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "012x":
                //+49 6666 66666
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling +49 6666 66666");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "020x":
                //J. H. Joplin
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling J. H. Joplin");
                //highlightTextLabel(R.id.bottom_right);
                break;
            case "021x":
                //Mrs. Robinson
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling Mrs. Robinson");
                //highlightTextLabel(R.id.bottom_right);
                break;
            case "022x":
                //Ing. J. Shiwago
                replace(R.drawable.finalcall);
                //editTextLabelsBottom("Redial", "Missed Calls", "Favorites");
                finalSelection("Calling Ing. J. Shiwago");
                //highlightTextLabel(R.id.bottom_right);
                break;
            case "100x":
                //Start from beginning
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("Start from beginning");
                //highlightTextLabel(R.id.bottom_left);
                break;
            case "101x":
                //Continue
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("Continue");
                //highlightTextLabel(R.id.bottom_left);
                break;
            case "102x":
                //Random
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("Random");
                //highlightTextLabel(R.id.bottom_left);
                break;
            case "110x":
                //USB
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("USB");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "111x":
                //AUX
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("AUX");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "112x":
                //Bluetooth
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("Bluetooth");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "120x":
                //Sight Ways 33.3
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("Sight Ways 33.3");
                //highlightTextLabel(R.id.bottom_right);
                break;
            case "121x":
                //Zero Zone 00.0
                //replace(R.drawable.menu121);
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("Zero Zone 00.0");
                //highlightTextLabel(R.id.bottom_right);
                break;
            case "122x":
                //Mercury 666.6
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("CD", "External Devices", "Radio");
                finalSelection("Mercury 666.6");
                //highlightTextLabel(R.id.bottom_right);
                break;
            case "200x":
                //66 Battery Ave, NY 66666
                replace(R.drawable.finalmedia);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to 66 Battery Ave, NY 66666");
                //highlightTextLabel(R.id.bottom_left);
                break;
            case "201x":
                //1 North End Ave, NY 10001
                replace(R.drawable.finalnav);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to 1 North End Ave, NY 10001");
                //highlightTextLabel(R.id.bottom_left);
                break;
            case "202x":
                //0 Rue Courbet, 75116 Paris
                replace(R.drawable.finalnav);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to 0 Rue Courbet, 75116 Paris");
                //highlightTextLabel(R.id.bottom_left);
                break;
            case "210x":
                //55 5th Ave, NY 10055
                replace(R.drawable.finalnav);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to 55 5th Ave, NY 10055");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "211x":
                //00 Cheers Ave, NY 10000
                replace(R.drawable.finalnav);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to 00 Cheers Ave, NY 10000");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "212x":
                //137 Leo Road, QLD 4880
                replace(R.drawable.finalnav);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to 137 Leo Road, QLD 4880");
                //highlightTextLabel(R.id.bottom_center);
                break;
            case "220x":
                //Gas Station
                replace(R.drawable.finalnav);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to Gas Station");
                //highlightTextLabel(R.id.bottom_right);
                break;
            case "221x":
                //Coffee Shop
                replace(R.drawable.finalnav);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to Coffee Shop");
                //highlightTextLabel(R.id.bottom_right);
                break;
            case "222x":
                //Liquid Store
                replace(R.drawable.finalnav);
                //editTextLabelsBottom("Previous Destinations", "Favorites", "Points of Interest");
                finalSelection("Navigating to Liquid Store");
                //highlightTextLabel(R.id.bottom_right);
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

    public void highlightTextLabel(int identifier) {
        TextView label = (TextView)findViewById(identifier);
        label.setTextColor(getResources().getColor(R.color.font_white));
    }
    public void resetTextColorMain() {
        left.setTextColor(getResources().getColor(R.color.font_grey));
        center.setTextColor(getResources().getColor(R.color.font_grey));
        right.setTextColor(getResources().getColor(R.color.font_grey));
    }

    public void resetTextColorTop() {
        topLeft.setTextColor(getResources().getColor(R.color.font_grey));
        topCenter.setTextColor(getResources().getColor(R.color.font_grey));
        topRight.setTextColor(getResources().getColor(R.color.font_grey));
    }
    public void resetTextColorBottom() {
        bottomLeft.setTextColor(getResources().getColor(R.color.font_grey));
        bottomCenter.setTextColor(getResources().getColor(R.color.font_grey));
        bottomRight.setTextColor(getResources().getColor(R.color.font_grey));
    }
    public void editTextLabelsMain(String newLeft, String newCenter, String newRight) {
        left.setText(newLeft);
        center.setText(newCenter);
        right.setText(newRight);
    }
    public void editTextLabelsTop(String newLeft, String newCenter, String newRight) {
        topLeft.setText(newLeft);
        topCenter.setText(newCenter);
        topRight.setText(newRight);
    }
    public void editTextLabelsBottom(String newLeft, String newCenter, String newRight) {
        bottomLeft.setText(newLeft);
        bottomCenter.setText(newCenter);
        bottomRight.setText(newRight);
    }

    public void finalSelection(String selection) {
        editTextLabelsMain("", "", "");
        editTextLabelsTop("", "", "");
        TextView selectionText = (TextView)findViewById(R.id.selected);
        selectionText.setText(selection);

    }
    public void resetDisplay() {
        TextView selectionText = (TextView)findViewById(R.id.selected);
        selectionText.setText("");
        editTextLabelsBottom("", "", "");
    }


    public void runTest() {
        // Execute some code after 2 seconds have passed
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setPicture("0x");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setPicture("01");

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                setPicture("02");

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        setPicture("021");

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                setPicture("022");

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    public void run() {
                                                        setPicture("021");
                                                    }
                                                }, 2000);
                                            }
                                        }, 2000);
                                    }
                                }, 2000);
                            }
                        }, 2000);
                    }
                }, 2000);
            }
        }, 2000);
    }
}

