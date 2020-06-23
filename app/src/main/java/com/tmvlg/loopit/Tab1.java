package com.tmvlg.loopit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.airbnb.lottie.LottieAnimationView;
import com.jorgecastilloprz.expandablepanel.ExpandablePanelView;
import com.jorgecastilloprz.expandablepanel.listeners.ExpandableListener;
import com.mr_sarsarabi.library.LockableViewPager;
import com.sdsmdg.harjot.crollerTest.Croller;


import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import in.goodiebag.carouselpicker.CarouselPicker;
import pl.droidsonroids.gif.GifImageButton;
import android.view.View.OnTouchListener;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment implements ExpandableListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView arrow_view;
    private Animation animationRotateCenter;
    Animation animationAppearCenter;
    private Animation animationDisappearCenter;

    private Rec recBtn1;
    private Rec recBtn2;
    private Rec recBtn3;
    private Rec recBtn4;
    private Rec recBtn5;
    private Rec recBtn6;

    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
    private MediaPlayer mediaPlayer3;
    private MediaPlayer mediaPlayer4;
    private MediaPlayer mediaPlayer5;
    private MediaPlayer mediaPlayer6;
    private MediaPlayer tempMediaPlayer;
    private MediaPlayer tempStopMediaPlayer;
    private String audioName;
    private boolean stopFlag = false;
    private ArrayDeque<Rec> arrPressedBtns;
    private HashSet<MediaPlayer> playList;
    private int dequeLength = 0;
    private String temptAudioName = "";
    private long startTime;
    private long endTime;
    private int elapsedMilliSeconds;
    private int tempBtn = 0;
    private boolean stopRecFlag = false;
    private RehearsalAudioRecorder recorder;
    private Rec currentBtn;
    private boolean flagPreSet = false;
    private int audioNumber = 0;

    private FrameLayout frameLayout1;
    private FrameLayout frameLayout2;
    private LinearLayout linearLayout;
    private TableLayout tableLayout;
    private String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private boolean isWork;
    private static final int REQUEST_CODE_PERMISSION = 100;

//    ExpandablePanelView expandablePanelView;

    TimerAsyncTask timerAsyncTask;
    MetronomeAsyncTask metronomeAsyncTask;
    private Thread timerThread;
    private Thread metronomeThread;
    private Croller croller;
    private RelativeLayout rLayout;
    private Toolbar tb;
    private LockableViewPager vp;
    private GifImageButton recGifBtn1;
    private GifImageButton recGifBtn2;
    private GifImageButton recGifBtn3;
    private GifImageButton recGifBtn4;
    private GifImageButton recGifBtn5;
    private GifImageButton recGifBtn6;
    private Rec[] recBtns;
    private CarouselPicker measureCarouselPicker1;
    private CarouselPicker measureCarouselPicker2;
    private CarouselPicker BPMCarouselPicker;
    private LinearLayout topll;
    private LinearLayout dmcll;
    private LinearLayout dotsll;
    private LinearLayout.LayoutParams small;
    private LinearLayout.LayoutParams large;
    private ExpandablePanelView expandablePanelView;
    private TextView timeTextView;
    static ImageView[] dots;
    int topMeasureValue = 4;
    public static boolean stopTimer = true;
    public static boolean stopMetronome = true;
    int bottomMeasureValue = 4;
    int bpm = 120;
    int min = 0;
    int sec = 0;
    private int numberOfDot = 0;
    int DIALOG_CROLLER = 1;
    private int screen_width;
    private int screen_height;
    private static final int DEFAULT_TOOLBAR_HEIGHT = 56;
    private static int toolBarHeight = -1;
    private boolean panelsScrolled[] = {false, false, false};


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        rLayout = view.findViewById(R.id.RelativeLayout);
        tb = view.findViewById(R.id.toolbar);


//        croller.setOnProgressChangedListener(crollListener);


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_width = size.x;
        screen_height = size.y;

        Log.d("tagn1", "x=" + screen_width + " h=" + screen_height);

        frameLayout1 = view.findViewById(R.id.frameLayout1);
        frameLayout2 = view.findViewById(R.id.frameLayout2);
        linearLayout = view.findViewById(R.id.dotsll);
        tableLayout = view.findViewById(R.id.tl);
        recBtn1 = new Rec((LottieAnimationView) view.findViewById(R.id.imageButton7));
        recBtn2 = new Rec((LottieAnimationView) view.findViewById(R.id.imageButton8));
        recBtn3 = new Rec((LottieAnimationView) view.findViewById(R.id.imageButton9));
        recBtn4 = new Rec((LottieAnimationView) view.findViewById(R.id.imageButton10));
        recBtn5 = new Rec((LottieAnimationView) view.findViewById(R.id.imageButton11));
        recBtn6 = new Rec((LottieAnimationView) view.findViewById(R.id.imageButton12));
        recBtn1.image = view.findViewById(R.id.staticImageButton7);
        recBtn2.image = view.findViewById(R.id.staticImageButton8);
        recBtn3.image = view.findViewById(R.id.staticImageButton9);
        recBtn4.image = view.findViewById(R.id.staticImageButton10);
        recBtn5.image = view.findViewById(R.id.staticImageButton11);
        recBtn6.image = view.findViewById(R.id.staticImageButton12);
        /*recBtn1.btn.setFreezesAnimation(true);
        recBtn2.btn.setFreezesAnimation(true);
        recBtn3.btn.setFreezesAnimation(true);
        recBtn4.btn.setFreezesAnimation(true);
        recBtn5.btn.setFreezesAnimation(true);
        recBtn6.btn.setFreezesAnimation(true);*/
        arrow_view = view.findViewById(R.id.arrow_view);
        topll = view.findViewById(R.id.topll);
        dotsll = view.findViewById(R.id.dotsll);
        recBtn1.btn.setOnLongClickListener(recBtnLCL);
        recBtn2.btn.setOnLongClickListener(recBtnLCL);
        recBtn3.btn.setOnLongClickListener(recBtnLCL);
        recBtn4.btn.setOnLongClickListener(recBtnLCL);
        recBtn5.btn.setOnLongClickListener(recBtnLCL);
        recBtn6.btn.setOnLongClickListener(recBtnLCL);
        recBtn1.btn.setOnClickListener(recBtnCL);
        recBtn2.btn.setOnClickListener(recBtnCL);
        recBtn3.btn.setOnClickListener(recBtnCL);
        recBtn4.btn.setOnClickListener(recBtnCL);
        recBtn5.btn.setOnClickListener(recBtnCL);
        recBtn6.btn.setOnClickListener(recBtnCL);
        /*recBtn1.btn.setOnTouchListener(new recBtnTL());
        recBtn2.btn.setOnTouchListener(new recBtnTL());
        recBtn3.btn.setOnTouchListener(new recBtnTL());
        recBtn4.btn.setOnTouchListener(new recBtnTL());
        recBtn5.btn.setOnTouchListener(new recBtnTL());
        recBtn6.btn.setOnTouchListener(new recBtnTL());*/

        expandablePanelView = view.findViewById(R.id.EPV);
        timeTextView = view.findViewById(R.id.timeTextView);
        recBtns = new Rec[6];
        recBtns[0] = recBtn1;
        recBtns[1] = recBtn2;
        recBtns[2] = recBtn3;
        recBtns[3] = recBtn4;
        recBtns[4] = recBtn5;
        recBtns[5] = recBtn6;
        registerForContextMenu(recBtn1.btn);
        registerForContextMenu(recBtn2.btn);
        registerForContextMenu(recBtn3.btn);
        registerForContextMenu(recBtn4.btn);
        registerForContextMenu(recBtn5.btn);
        registerForContextMenu(recBtn6.btn);
        expandablePanelView.attachExpandableListener(this);
        small = new LinearLayout.LayoutParams(screen_width/54, screen_width/54);
        large = new LinearLayout.LayoutParams(screen_width/36, screen_width/36);
        small.leftMargin=screen_width/72;
        small.rightMargin=screen_width/72;
        large.leftMargin=screen_width/72;
        large.rightMargin=screen_width/72;
        dots = createDots(small, large, topMeasureValue, "13");
        arrPressedBtns = new ArrayDeque<>();
        playList = new HashSet<>();
        mediaPlayer1 = new MediaPlayer();
        mediaPlayer2 = new MediaPlayer();
        mediaPlayer3 = new MediaPlayer();
        mediaPlayer4 = new MediaPlayer();
        mediaPlayer5 = new MediaPlayer();
        mediaPlayer6 = new MediaPlayer();

        isWork = hasPermissions(this.getActivity(), PERMISSIONS);
        if (!isWork) {
            ActivityCompat.requestPermissions(this.getActivity(), PERMISSIONS,
                    REQUEST_CODE_PERMISSION);
        }

//        recBtns[5].btn.setVisibility(View.GONE);
//        recBtn6.btn.setVisibility(View.GONE);
        return view;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
// permission granted
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()){
            case R.id.imageButton7:
        }
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){

        }

        return super.onContextItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void rr(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    Croller.onProgressChangedListener crollListener = new Croller.onProgressChangedListener(){
        @Override
        public void onProgressChanged(int progress) {
            float volume = (float)(progress / 1000.0);
            Log.d("crollerlist", "progress: " + volume);
            switch (tempBtn){
                case 1:{
                    mediaPlayer1.setVolume(volume, volume);
                    break;
                }
                case 2:{
                    mediaPlayer2.setVolume(volume, volume);
                    break;
                }
                case 3:{
                    mediaPlayer3.setVolume(volume, volume);
                    break;
                }
                case 4:{
                    mediaPlayer4.setVolume(volume, volume);
                    break;
                }
                case 5:{
                    mediaPlayer5.setVolume(volume, volume);
                    break;
                }
                case 6:{
                    mediaPlayer6.setVolume(volume, volume);
                    break;
                }
            }
        }
    };




    View.OnLongClickListener recBtnLCL = new View.OnLongClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onLongClick(View v) {
            animationDisappearCenter = AnimationUtils.loadAnimation(
                    v.getContext(), R.anim.disappearing);
            final Dialog d = new Dialog(getActivity(), R.style.PauseDialog);
            //  d.getWindow().setBackgroundDrawable(R.color.action_bar_bg);
            Window window = d.getWindow();
            window.setGravity(Gravity.AXIS_X_SHIFT & Gravity.AXIS_Y_SHIFT);
            WindowManager.LayoutParams layoutParams = d.getWindow().getAttributes();
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_croller);
            croller = d.findViewById(R.id.di_croller);
            croller.setOnProgressChangedListener(crollListener);
            d.getWindow().setAttributes(layoutParams);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = frameLayout1.getWidth();
            int height = frameLayout1.getHeight();
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)tableLayout.getLayoutParams();
            switch (v.getId()) {
                case R.id.imageButton7: {
                    tempBtn = 1;
                    recBtn1.btn.startAnimation(animationDisappearCenter);
                    recBtn1.btn.setVisibility(v.GONE);
                    recBtn1.btn.setVisibility(v.VISIBLE);
                    layoutParams.x = lp.leftMargin;
                    layoutParams.y = linearLayout.getHeight() + frameLayout1.getHeight() - 100;
                    d.getWindow().setLayout(width, height);
                    d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); //hz
                    d.show();
                    break;
                }
                case R.id.imageButton8: {
                    tempBtn = 2;
                    recBtn2.btn.startAnimation(animationDisappearCenter);
                    recBtn2.btn.setVisibility(v.GONE);
                    recBtn2.btn.setVisibility(v.VISIBLE);
                    layoutParams.x = lp.leftMargin + (int) frameLayout2.getX();
                    layoutParams.y = linearLayout.getHeight() + frameLayout1.getHeight() - 100;;
                    d.getWindow().setLayout(width, height);
                    d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); //hz
                    d.show();
                    break;
                }
                case R.id.imageButton9: {
                    tempBtn = 3;
                    recBtn3.btn.startAnimation(animationDisappearCenter);
                    recBtn3.btn.setVisibility(v.GONE);
                    recBtn3.btn.setVisibility(v.VISIBLE);
                    layoutParams.x = lp.leftMargin;
                    layoutParams.y = linearLayout.getHeight() + 2*frameLayout1.getHeight() - 100;
                    d.getWindow().setLayout(width, height);
                    d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); //hz
                    d.show();
                    break;
                }
                case R.id.imageButton10: {
                    tempBtn = 4;
                    recBtn4.btn.startAnimation(animationDisappearCenter);
                    recBtn4.btn.setVisibility(v.GONE);
                    recBtn4.btn.setVisibility(v.VISIBLE);
                    layoutParams.x = lp.leftMargin + (int) frameLayout2.getX();
                    layoutParams.y = linearLayout.getHeight() + 2*frameLayout1.getHeight() - 100;
                    d.getWindow().setLayout(width, height);
                    d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); //hz
                    d.show();
                    break;
                }
                case R.id.imageButton11: {
                    tempBtn = 5;
                    recBtn5.btn.startAnimation(animationDisappearCenter);
                    recBtn5.btn.setVisibility(v.GONE);
                    recBtn5.btn.setVisibility(v.VISIBLE);
                    layoutParams.x = lp.leftMargin;
                    layoutParams.y = linearLayout.getHeight() + 3*frameLayout1.getHeight() - 100;
                    d.getWindow().setLayout(width, height);
                    d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); //hz
                    d.show();
                    break;
                }
                case R.id.imageButton12: {
                    tempBtn = 6;
                    recBtn6.btn.startAnimation(animationDisappearCenter);
                    recBtn6.btn.setVisibility(v.GONE);
                    recBtn6.btn.setVisibility(v.VISIBLE);
                    layoutParams.x = lp.leftMargin + (int) frameLayout2.getX();
                    layoutParams.y = linearLayout.getHeight() + 3*frameLayout1.getHeight() - 100;
                    d.getWindow().setLayout(width, height);
                    d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); //hz
                    d.show();
                    break;
                }
            }
            return false;
        }
    };


    private final class recBtnTL implements OnTouchListener {

        float mPreviousX = 0;
        float mPreviousY = 0;
        boolean mIsDown = false;

        public boolean onTouch(View v, MotionEvent e) {
            // MotionEvent reports input details from the touch screen
            // and other input controls. In this case, you are only
            // interested in events where the touch position changed.

            float x = e.getX();
            float y = e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mIsDown = true;
                    break;
                case MotionEvent.ACTION_MOVE:

                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;

                    // Here you can try to detect the swipe. It will be necessary to
                    // store more than the previous value to check that the user move constantly in the same direction

                case MotionEvent.ACTION_UP:
                    mIsDown = false;
                    break;
            }

            mPreviousX = x;
            mPreviousY = y;
            return true;
        }
    };


    View.OnClickListener recBtnCL = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!isAnyRecPressed()){
                //timerAsyncTask = new TimerAsyncTask(timeTextView);
                //timerAsyncTask.execute();
                Log.d("tagn1", "dots length=" + dots.length);
                //metronomeAsyncTask = new MetronomeAsyncTask(dots, bpm/bottomMeasureValue, getActivity());
                //metronomeAsyncTask.execute();
                timerThread = new Thread(timerRunnable);
                stopTimer = false;
                timerThread.start();
                metronomeThread = new Thread(metronomeRunnable);
                stopMetronome = false;
                metronomeThread.start();

            }
            for (final Rec button : recBtns){
                if (button.btn.equals(v)){
                    if (button.getStatus().equals("start")) {
                        arrPressedBtns.addLast(button);
                        dequeLength++;
                        button.setStatus("stop");

                    }
                    else if (button.getStatus().equals("stop")){
                        button.setStatus("pause");
                        stopRecFlag = true;
                        Thread thread = new Thread(preSet);
                        thread.start();
                    }
                    else if (button.getStatus().equals("pause")){
                        switch (v.getId()) {
                            case R.id.imageButton7: {
                                tempStopMediaPlayer = mediaPlayer1;
                                break;
                            }
                            case R.id.imageButton8: {
                                tempStopMediaPlayer = mediaPlayer2;
                                break;
                            }
                            case R.id.imageButton9: {
                                tempStopMediaPlayer = mediaPlayer3;
                                break;
                            }
                            case R.id.imageButton10: {
                                tempStopMediaPlayer = mediaPlayer4;
                                break;
                            }
                            case R.id.imageButton11: {
                                tempStopMediaPlayer = mediaPlayer5;
                                break;
                            }
                            case R.id.imageButton12: {
                                tempStopMediaPlayer = mediaPlayer6;
                                break;
                            }
                        }
                        onPlayStop(tempStopMediaPlayer);
                        button.setStatus("play");
                        //button.btn.setImageResource(R.drawable.pause_static);
                        button.setImage(R.drawable.pause_static);
                        button.btn.setAnimation("LottieBrecPauseToPlay.json");
                        button.btn.playAnimation();
                    }
                    else if (button.getStatus().equals("play")){
                        endTime = SystemClock.elapsedRealtime();
                        elapsedMilliSeconds = (int)endTime - (int)startTime;
                        switch (v.getId()) {
                            case R.id.imageButton7: {
                                tempMediaPlayer = mediaPlayer1;
                                temptAudioName = "/"+mediaPlayer1.hashCode()+".wav";
                                break;
                            }
                            case R.id.imageButton8: {
                                tempMediaPlayer = mediaPlayer2;
                                temptAudioName = "/"+mediaPlayer2.hashCode()+".wav";
                                break;
                            }
                            case R.id.imageButton9: {
                                tempMediaPlayer = mediaPlayer3;
                                temptAudioName = "/"+mediaPlayer3.hashCode()+".wav";
                                break;
                            }
                            case R.id.imageButton10: {
                                tempMediaPlayer = mediaPlayer4;
                                temptAudioName = "/"+mediaPlayer4.hashCode()+".wav";
                                break;
                            }
                            case R.id.imageButton11: {
                                tempMediaPlayer = mediaPlayer5;
                                temptAudioName = "/"+mediaPlayer5.hashCode()+".wav";
                                break;
                            }
                            case R.id.imageButton12: {
                                tempMediaPlayer = mediaPlayer6;
                                temptAudioName = "/"+mediaPlayer6.hashCode()+".wav";
                                break;
                            }
                        }
                        onPlayStart(tempMediaPlayer, temptAudioName, elapsedMilliSeconds);
                        button.setStatus("pause");
                        //button.btn.setImageResource(R.drawable.play_static);
                        button.setImage(R.drawable.play_static);
                        button.btn.setAnimation("LottieBrecPlayToPause.json");
                        button.btn.playAnimation();
                        // button.btn.setImageResource(R.drawable.play_to_pause);

                    }

                }
            }
            if (!isAnyRecPressed()){
                //timerAsyncTask.cancel(false);
                stopTimer = true;
                stopMetronome = true;
                //metronomeAsyncTask.cancel(false);
                Log.d("tagn1", "all threads r stopped");
            }
//            btnDisable();
        }
    };


    public void onPlayStart(final MediaPlayer mediaPlayer, final String audioName, final int msec) {
        Runnable runnable = new Runnable() {
            public void run() {
                playList.add(mediaPlayer);
//                playList.remove(mediaPlayer);
                if (mediaPlayer != null) {
                    mediaPlayer.reset();
                }
                try {
                    mediaPlayer.setDataSource(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + audioName);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    mediaPlayer.seekTo(msec);
//                    Log.d("tempor", "audio added");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    public void onPlayStop(final MediaPlayer mediaPlayer) {
        Runnable runnable = new Runnable() {
            public void run() {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
                Iterator<MediaPlayer> iterator = playList.iterator();
                while(iterator.hasNext()) {
                    MediaPlayer setElement = iterator.next();
                    if(setElement==mediaPlayer) {
                        iterator.remove();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


    Runnable preSet = new Runnable() {
        @Override
        public void run() {
            currentBtn = arrPressedBtns.pollFirst();
            switch (currentBtn.btn.getId()) {
                case R.id.imageButton7: {
                    tempMediaPlayer = mediaPlayer1;
                    temptAudioName = "/"+mediaPlayer1.hashCode()+".wav";
                    playList.add(mediaPlayer1);
                    break;
                }
                case R.id.imageButton8: {
                    tempMediaPlayer = mediaPlayer2;
                    temptAudioName = "/"+mediaPlayer2.hashCode()+".wav";
                    playList.add(mediaPlayer2);
                    break;
                }
                case R.id.imageButton9: {
                    tempMediaPlayer = mediaPlayer3;
                    temptAudioName = "/"+mediaPlayer3.hashCode()+".wav";
                    playList.add(mediaPlayer3);
                    break;
                }
                case R.id.imageButton10: {
                    tempMediaPlayer = mediaPlayer4;
                    temptAudioName = "/"+mediaPlayer4.hashCode()+".wav";
                    playList.add(mediaPlayer4);
                    break;
                }
                case R.id.imageButton11: {
                    tempMediaPlayer = mediaPlayer5;
                    temptAudioName = "/"+mediaPlayer5.hashCode()+".wav";
                    playList.add(mediaPlayer5);
                    break;
                }
                case R.id.imageButton12: {
                    tempMediaPlayer = mediaPlayer6;
                    temptAudioName = "/"+mediaPlayer6.hashCode()+".wav";
                    playList.add(mediaPlayer6);
                    break;
                }
            }
        }
    };



    private void stopRecording() {
        try {
            TimeUnit.MILLISECONDS.sleep((long)(60000 / (bpm * bottomMeasureValue / 4)));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recorder.stop();
        recorder.release();
        stopRecFlag = false;
        flagPreSet = false;
        dequeLength--;
        currentBtn.setStatus("pause");
        stopFlag = false;
        recorder = null;
    }


    @Override
    public void onExpandingStarted() {
        animationRotateCenter = AnimationUtils.loadAnimation(
                this.getContext(), R.anim.myrotate);
        arrow_view.startAnimation(animationRotateCenter);

        Log.d("tagn1", "why not works yet?");
    }

    @Override
    public void onExpandingFinished() {
        ViewGroup.LayoutParams llparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        topll.addView(vert, llparams);

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.dialog_measure_carousel, null);

        dmcll = promptsView.findViewById(R.id.dmc_ll);
        topll.addView(dmcll, llparams);

        LayoutInflater li2 = LayoutInflater.from(getActivity());
        View promptsView2 = li.inflate(R.layout.activity_main, null);


        List<CarouselPicker.PickerItem> measureTextItems1 = new ArrayList<>();
        List<CarouselPicker.PickerItem> measureTextItems2 = new ArrayList<>();
//20 here represents the textSize in dp, change it to the value you want.
        measureTextItems1.add(new CarouselPicker.TextItem("2", 20));
        measureTextItems1.add(new CarouselPicker.TextItem("3", 20));
        measureTextItems1.add(new CarouselPicker.TextItem("4", 20));
        measureTextItems1.add(new CarouselPicker.TextItem("6", 20));
        measureTextItems1.add(new CarouselPicker.TextItem("9", 20));
        measureTextItems1.add(new CarouselPicker.TextItem("12", 20));

        measureTextItems2.add(new CarouselPicker.TextItem("2", 20));
        measureTextItems2.add(new CarouselPicker.TextItem("4", 20));
        measureTextItems2.add(new CarouselPicker.TextItem("8", 20));
        measureTextItems2.add(new CarouselPicker.TextItem("16", 20));

        CarouselPicker.CarouselViewAdapter measureTextAdapter1 = new CarouselPicker.CarouselViewAdapter(getActivity(), measureTextItems1, 0);
        CarouselPicker.CarouselViewAdapter measureTextAdapter2 = new CarouselPicker.CarouselViewAdapter(getActivity(), measureTextItems2, 0);

        List<CarouselPicker.PickerItem> bpmTextItems = new ArrayList<>();
        bpmTextItems.add(new CarouselPicker.TextItem("60", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("70", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("80", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("90", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("100", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("110", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("120", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("130", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("140", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("150", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("160", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("170", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("180", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("190", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("200", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("210", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("220", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("230", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("240", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("250", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("260", 16));
        bpmTextItems.add(new CarouselPicker.TextItem("270", 16));

        CarouselPicker.CarouselViewAdapter bpmTextAdapter = new CarouselPicker.CarouselViewAdapter(getActivity(), bpmTextItems, 0);

        measureCarouselPicker1 = promptsView.findViewById(R.id.upper_carousel);
        measureCarouselPicker2 = promptsView.findViewById(R.id.lower_carousel);
        BPMCarouselPicker = promptsView.findViewById(R.id.bpm_carousel);

        measureTextAdapter1.setTextColor(Color.WHITE);
        measureTextAdapter2.setTextColor(Color.WHITE);
        bpmTextAdapter.setTextColor(Color.WHITE);

        measureCarouselPicker1.setAdapter(measureTextAdapter1);
        measureCarouselPicker2.setAdapter(measureTextAdapter2);
        BPMCarouselPicker.setAdapter(bpmTextAdapter);

        vp = promptsView2.findViewById(R.id.pager); ////not works
        vp.setSwipeLocked(true);                    ////yet

        for (boolean item : panelsScrolled)
        {
            item = false;
        }

        measureCarouselPicker1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        topMeasureValue = 2;
                        break;
                    case 1:
                        topMeasureValue = 3;
                        break;
                    case 2:
                        topMeasureValue = 4;
                        break;
                    case 3:
                        topMeasureValue = 6;
                        break;
                    case 4:
                        topMeasureValue = 9;
                        break;
                    case 5:
                        topMeasureValue = 12;
                        break;
                }
                panelsScrolled[0] = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        measureCarouselPicker2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomMeasureValue = 2;
                        break;
                    case 1:
                        bottomMeasureValue = 4;
                        break;
                    case 2:
                        bottomMeasureValue = 8;
                        break;
                    case 3:
                        bottomMeasureValue = 16;
                        break;
                }
                panelsScrolled[1] = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        BPMCarouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bpm = 60 + position * 10;
                panelsScrolled[2] = true;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onShrinkStarted() {

    }

    @Override
    public void onShrinkFinished() {
        topll.removeView(dmcll);
        animationRotateCenter = AnimationUtils.loadAnimation(
                this.getContext(), R.anim.myreverserotate);
        arrow_view.startAnimation(animationRotateCenter);

        if (!panelsScrolled[0])
        {
            topMeasureValue = 2;
        }
        if (!panelsScrolled[1])
        {
            bottomMeasureValue = 2;
        }
        if (!panelsScrolled[2])
        {
            bpm = 60;
        }


        Toast.makeText(getActivity(), "tmv " + topMeasureValue + " bmv " + bottomMeasureValue + " bpm " + bpm, Toast.LENGTH_SHORT).show();
        if (topMeasureValue == 2) {
            dots = createDots(small, large, topMeasureValue, "1");
        }
        if (topMeasureValue == 3) {
            dots = createDots(small, large, topMeasureValue, "1");
        }
        if (topMeasureValue == 4) {
            dots = createDots(small, large, topMeasureValue, "13");
        }
        if (topMeasureValue == 6) {
            dots = createDots(small, large, topMeasureValue, "14");
        }
        if (topMeasureValue == 9) {
            dots = createDots(small, large, topMeasureValue, "147");
        }
        if (topMeasureValue == 12) {
            dots = createDots(small, large, topMeasureValue, "14710");
        }

    }

    @Override
    public void onExpandingTouchEvent(MotionEvent motionEvent) {

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public static int getToolBarHeight(Context context) {
        if (toolBarHeight > 0) {
            return toolBarHeight;
        }
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("action_bar_size", "dimen", "android");
        toolBarHeight = resourceId > 0 ?
                resources.getDimensionPixelSize(resourceId) :
                (int) convertDpToPixel(context, DEFAULT_TOOLBAR_HEIGHT);
        return toolBarHeight;
    }

    public static float convertDpToPixel(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }




    private void moveViewToScreenCenter( View view )
    {
        RelativeLayout root = (RelativeLayout) getActivity().findViewById( R.id.RelativeLayout );
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        view.getLocationOnScreen( originalPos );

        int h = view.getHeight();
        int w = view.getWidth();

        int xDest = dm.widthPixels/2;
        xDest -= (view.getMeasuredWidth()/2);
        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2);

        TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
        ScaleAnimation anim2 = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 0.1f, 0.1f);
        AnimationSet as = new AnimationSet(true);
        as.setFillAfter(false);
        as.setInterpolator(new LinearInterpolator());
        anim2.setDuration(400);
        as.addAnimation(anim2);
        anim.setDuration(400);
        as.addAnimation(anim);
        view.startAnimation(as);
    }

    private ImageView[] createDots(LinearLayout.LayoutParams small, LinearLayout.LayoutParams large, int numberOfDots, String larges)
    {
        ImageView dotsTemp[] = new ImageView[numberOfDots];


        dotsll.removeAllViewsInLayout();
        int count = 1;
        for (int i = 0; i < dotsTemp.length; i++)
        {
            dotsTemp[i] = new ImageView(getActivity());
            dotsTemp[i].setElevation(1);
            dotsTemp[i].setScaleType(ImageView.ScaleType.FIT_CENTER);

            Log.d("tagn1", "works");
            dotsTemp[i].setImageResource(R.drawable.oval_metronome_indicator);
            if (larges.contains("" + count))
            {
                dotsll.addView(dotsTemp[i], large);
            }
            else {
                dotsll.addView(dotsTemp[i], small);
            }
            count++;
        }
        Log.d("tagn1", "dot class:"+dotsTemp[0]);
        Log.d("tagn1", "arrow class:"+arrow_view);
        return dotsTemp;
    }

    private boolean isAnyRecPressed(){
        boolean result = false;
        for (Rec button : recBtns) {
            if (!button.getStatus().equals("start"))
                result = true;
        }
        return result;
    }

    Runnable timerRunnable = new Runnable() {
        public void run() {
            min = 0;
            sec = 0;
            while (!stopTimer) {
                try {
                    getActivity().runOnUiThread(settingTimeView);
                    TimeUnit.SECONDS.sleep(1);
                    if (sec == 59)
                    {
                        min++;
                        sec = -1;
                    }
                    sec++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable settingTimeView = new Runnable() {
        @Override
        public void run() {
            if (sec < 10)
                timeTextView.setText("" + min + ":0" + sec);
            else {
                timeTextView.setText("" + min + ":" + sec);
            }
        }
    };


    Runnable startRec = new Runnable() {
        @Override
        public void run() {
            currentBtn = arrPressedBtns.peekFirst(); //достает но не удаляет
            switch (currentBtn.btn.getId()) {
                case R.id.imageButton7: {
                    audioName = "/"+mediaPlayer1.hashCode()+".wav";
                    break;
                }
                case R.id.imageButton8: {
                    audioName = "/"+mediaPlayer2.hashCode()+".wav";
                    break;
                }
                case R.id.imageButton9: {
                    audioName = "/"+mediaPlayer3.hashCode()+".wav";
                    break;
                }
                case R.id.imageButton10: {
                    audioName = "/"+mediaPlayer4.hashCode()+".wav";
                    break;
                }
                case R.id.imageButton11: {
                    audioName = "/"+mediaPlayer5.hashCode()+".wav";
                    break;
                }
                case R.id.imageButton12: {
                    audioName = "/"+mediaPlayer6.hashCode()+".wav";
                    break;
                }
            }

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    currentBtn.btn.playAnimation();
                }
            });
            recorder = new RehearsalAudioRecorder(RehearsalAudioRecorder.RECORDING_UNCOMPRESSED, MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            recorder.setOutputFile(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + audioName);
            recorder.prepare();
            recorder.start();
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("rec ", "recording button " + audioNumber);
                    Toast.makeText(getContext(), "Recording started!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    };

    Runnable metronomeRunnable = new Runnable() {
        @Override
        public void run() {
            while (!stopMetronome) {
                Log.d("tagn1", "GO!");
                for (numberOfDot = 0; numberOfDot < dots.length;) {
                    if (numberOfDot == 0) {
                        startTime = SystemClock.elapsedRealtime();
                        if (playList.size() != 0) {
                            Iterator<MediaPlayer> iterator = playList.iterator();
                            while(iterator.hasNext()) {
                                MediaPlayer setElement = iterator.next();
                                if (setElement.getCurrentPosition() == 0 || setElement.getDuration()-setElement.getCurrentPosition()<200)
                                    onPlayStart(setElement, "/"+setElement.hashCode()+".wav",0);
                            }
                        }

                    }
                    if (dequeLength == 1)
                        if (numberOfDot == 0 && !stopFlag){
                            stopFlag = true;
                            Thread thread = new Thread(startRec);
                            thread.setPriority(10);
                            thread.start();
                        }

                    makeDotsAlive();
                    if (dequeLength == 2)
                        if (numberOfDot == dots.length-1){
                            stopRecording();
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    currentBtn.setImage(R.drawable.stop_static);
                                    currentBtn.btn.setAnimation("LottieBrecStopToPause.json");
                                    currentBtn.btn.playAnimation();
                                }
                            });
                        }

                    if (dequeLength == 2 && !flagPreSet){
                        flagPreSet = true;
                        Thread thread = new Thread(preSet);
                        thread.start();
                    }

                    if (numberOfDot == dots.length-1 && stopRecFlag){
                        stopRecording();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                currentBtn.setImage(R.drawable.stop_static);
                                currentBtn.btn.setAnimation("LottieBrecStopToPause.json");
                                currentBtn.btn.playAnimation();
                            }
                        });
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep((long) 60000 / (bpm * bottomMeasureValue / 4));
                        numberOfDot++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    };

    private void btnDisable(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if (dequeLength == 2)
                    for (Rec button : recBtns) {
                        if (button != arrPressedBtns.peekFirst() && button != arrPressedBtns.peekLast()) {
                            button.btn.setClickable(false);
                        }
                    }
                if (dequeLength == 1)
                    for (Rec button : recBtns) {
                        if (button != arrPressedBtns.peekFirst()) {
                            button.btn.setClickable(true);
                        }
                    }
            }
        });
    }

    public void makeDotsAlive() {
        final AnimationSet as = new AnimationSet(true);
        Animation onime = null;
        onime = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
        onime.setDuration((long) 30000 / (bpm * bottomMeasureValue / 4));
        as.addAnimation(onime);
        Animation onime_reversed = null;
        onime_reversed = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce_reversed);
        onime_reversed.setStartOffset((long) 30000 / (bpm * bottomMeasureValue / 4));
        onime_reversed.setDuration((long) 30000 / (bpm * bottomMeasureValue / 4));
        as.addAnimation(onime_reversed);
        Log.d("tagn1", "dot number #" + numberOfDot);
        Log.d("tagn9", "speed " + (long) 30000 / (bpm * bottomMeasureValue / 4));
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                dots[numberOfDot].startAnimation(as);
            }
        });
    }

}
